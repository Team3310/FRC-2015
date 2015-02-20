package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.utility.CANTalonAnalogPID;
import edu.rhhs.frc.utility.CANTalonEncoderPID;
import edu.rhhs.frc.utility.PIDParams;
import edu.rhhs.frc.utility.RobotUtility;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *	BinGrabber (name to be determined) is a subsystem that controls the two motors associated with each of the two arms (codenames: "Bubba" and "Gump").
 */
public class BinGrabber extends Subsystem 
{
	public static enum BinGrabberState {EXTENDED, RETRACTED};
	
	private static final int LEFT_ANALOG_ZERO_PRACTICE = 362;
	private static final int RIGHT_ANALOG_ZERO_PRACTICE = -590;
	
	public static final double DEPLOYED_POSITION_DEG = 70;
	public static final double DEPLOYED_POSITION_TIMED_DEG = DEPLOYED_POSITION_DEG - 10;
	public static final double STOWED_POSITION_DEG = 0;
	public static final double DRAG_BIN_POSITION_DEG = 60;
			
	private DoubleSolenoid m_clawPositionSolenoid;
	private DoubleSolenoid m_pivotLockSolenoid;
	
	private CANTalonAnalogPID m_rightMotor;
	private CANTalonAnalogPID m_leftMotor;

	private CANTalon.ControlMode m_controlMode;

    private PIDParams downPositionPidParams = new PIDParams(10.0, 0.001, 0.0, 0.0, 50, 0.0);
    private PIDParams upPositionPidParams = new PIDParams(2.0, 0.004, 0.0, 0.0, 50, 0.0);

	private double m_error;

	public BinGrabber() {
		try {
			m_leftMotor = new CANTalonAnalogPID(RobotMap.BIN_GRABBER_LEFT_CAN_ID, 1.0, 0.0, STOWED_POSITION_DEG, STOWED_POSITION_DEG, DEPLOYED_POSITION_DEG, LEFT_ANALOG_ZERO_PRACTICE);
			m_rightMotor = new CANTalonAnalogPID(RobotMap.BIN_GRABBER_RIGHT_CAN_ID, 1.0, 0.0, STOWED_POSITION_DEG, STOWED_POSITION_DEG, DEPLOYED_POSITION_DEG, RIGHT_ANALOG_ZERO_PRACTICE);
			
			m_leftMotor.setSafetyEnabled(false);
			m_rightMotor.setSafetyEnabled(false);
	
			m_leftMotor.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogPot);
			m_leftMotor.reverseOutput(true);
			m_leftMotor.reverseSensor(false);
	
			m_rightMotor.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogPot);
			m_rightMotor.reverseOutput(false);
			m_rightMotor.reverseSensor(true);

			m_leftMotor.setPIDParams(downPositionPidParams, CANTalonEncoderPID.POSITION_PROFILE);
			m_rightMotor.setPIDParams(downPositionPidParams, CANTalonEncoderPID.POSITION_PROFILE);
	
			// Start with the Talons in throttle mode
			setTalonControlMode(CANTalon.ControlMode.PercentVbus);
			m_leftMotor.set(0);
			m_rightMotor.set(0);
			
			m_clawPositionSolenoid = new DoubleSolenoid(RobotMap.BIN_GRABBER_CLAW_EXTEND_PNEUMATIC_MODULE_ID, RobotMap.BIN_GRABBER_CLAW_RETRACT_PNEUMATIC_MODULE_ID);
			m_pivotLockSolenoid = new DoubleSolenoid(RobotMap.BIN_GRABBER_PIVOT_LOCK_EXTEND_PNEUMATIC_MODULE_ID, RobotMap.BIN_GRABBER_PIVOT_LOCK_RETRACT_PNEUMATIC_MODULE_ID);
		} 
		catch (Exception e) {
	        System.out.println("Unknown error initializing bin grabber.  Message = " + e.getMessage());
	    }
	}

	@Override
	public void initDefaultCommand() {
//		setDefaultCommand(new BinGrabberWithJoystick());
	}
	
	public void keepAlive() {
		m_leftMotor.enableBrakeMode(true);
		m_rightMotor.enableBrakeMode(true);
	}

	public void teleopInit() {
	}
	
	public void setClawPosition(BinGrabberState position) {
		if (position == BinGrabberState.EXTENDED) {
			m_clawPositionSolenoid.set(DoubleSolenoid.Value.kForward);
    	}
		else {
    		m_clawPositionSolenoid.set(DoubleSolenoid.Value.kReverse);
		}
	}

	public void setPivotLockPosition(BinGrabberState position) {
		if (position == BinGrabberState.EXTENDED) {
    		m_pivotLockSolenoid.set(DoubleSolenoid.Value.kForward);
    	}
		else {
			m_pivotLockSolenoid.set(DoubleSolenoid.Value.kReverse);
		}
	}

	private void setTalonControlMode(CANTalon.ControlMode mode) {
		m_controlMode = mode;
		m_leftMotor.changeControlMode(mode);
		m_rightMotor.changeControlMode(mode);
	}
		
	private void setTalonProfile(int profile) {
		m_leftMotor.setProfile(profile);
		m_rightMotor.setProfile(profile);
	}
		
	public void setSpeed(double leftSpeed, double rightSpeed) {
		setLeftSpeed(leftSpeed);
		setRightSpeed(rightSpeed);
	}
	
	public void setLeftSpeed(double leftSpeed) {
		m_leftMotor.set(-leftSpeed);
	}
	
	public void setRightSpeed(double rightSpeed) {
		m_rightMotor.set(rightSpeed);
	}
	
	public void startPositionPID(double leftTargetDeg, double rightTargetDeg, double errorDeg) {
		setTalonControlMode(CANTalon.ControlMode.Position); 
		m_leftMotor.setPIDPositiveNegativePositionDeg(leftTargetDeg, downPositionPidParams, upPositionPidParams); 
		m_leftMotor.setPIDPositiveNegativePositionDeg(rightTargetDeg, downPositionPidParams, upPositionPidParams); 
		m_error = errorDeg;
	}
	
	public void stopPID() {
		setTalonControlMode(CANTalon.ControlMode.PercentVbus);
		setSpeed(0,0);
	}
	
	public boolean isAtLeftTarget() {
		return Math.abs(m_leftMotor.getClosedLoopError()) < m_error;
	}
	
	public boolean isAtRightTarget() {
		return Math.abs(m_rightMotor.getClosedLoopError()) < m_error;
	}
	
	public double getLeftErrorDeg() {
		return m_leftMotor.getErrorDeg();
	}
	
	public double getRightErrorDeg() {
		return m_rightMotor.getErrorDeg();
	}
	
	public double getLeftPositionDeg() {
		return m_leftMotor.getPositionDeg();
	}
	
	public double getRightPositionDeg() {
		return m_rightMotor.getPositionDeg();
	}
	
	public void controlWithJoystick() {
//		setTalonInput(0, -OI.getInstance().getXBoxController().getRightYAxis());
	}

	public void updateStatus() {
		SmartDashboard.putNumber("Left Bin Grabber Get", m_leftMotor.get());
		SmartDashboard.putNumber("Right Bin Grabber Get", m_rightMotor.get());

		SmartDashboard.putNumber("Left Bin Grabber Analog Position (raw)", m_leftMotor.getAnalogInRaw());
		SmartDashboard.putNumber("Right Bin Grabber Analog Position (raw)", m_rightMotor.getAnalogInRaw());

		SmartDashboard.putNumber("Left Bin Grabber Position (Deg)", getLeftPositionDeg());
		SmartDashboard.putNumber("Right Bin Grabber Position (Deg)", getRightPositionDeg());
	}
}