package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.OI;
import edu.rhhs.frc.RobotMap;
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
	
	private static final double LEFT_ANALOG_ZERO_PRACTICE = 362.0;
	private static final double RIGHT_ANALOG_ZERO_PRACTICE = -590.0;
	
	public static final double DEPLOYED_POSITION_DEG = 70;
	public static final double DEPLOYED_POSITION_TIMED_DEG = DEPLOYED_POSITION_DEG - 10;
	public static final double STOWED_POSITION_DEG = 0;
	public static final double DRAG_BIN_POSITION_DEG = 60;
			
	private DoubleSolenoid m_clawPositionSolenoid;
	private DoubleSolenoid m_pivotLockSolenoid;
	
	private CANTalon m_rightMotor;
	private CANTalon m_leftMotor;

	private CANTalon.ControlMode m_talonControlMode = CANTalon.ControlMode.PercentVbus;
	private double m_leftTarget;
	private double m_rightTarget;

	private double m_error;
	private double m_kP = 3.0;  // 10
	private double m_kI = 0.000;
	private double m_kD = 0.0;
	private double m_kF = 0.0; 
	private int m_iZone = 0;
	private double m_rampRatePID = 0.0;
	private double m_rampRateVBus = 0.0;
	private int m_profile = 0;

	public BinGrabber() {
		try {
			m_rightMotor = new CANTalon(RobotMap.BIN_GRABBER_RIGHT_CAN_ID);
			m_leftMotor = new CANTalon(RobotMap.BIN_GRABBER_LEFT_CAN_ID);
	
			m_leftMotor.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogPot);
			m_leftMotor.setPID(m_kP, m_kI, m_kD, m_kF, m_iZone, m_rampRatePID, m_profile);
			m_leftMotor.setVoltageRampRate(m_rampRateVBus);
			m_leftMotor.reverseOutput(true);
			m_leftMotor.reverseSensor(false);
	
			m_rightMotor.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogPot);
			m_rightMotor.setPID(m_kP, m_kI, m_kD, m_kF, m_iZone, m_rampRatePID, m_profile);
			m_rightMotor.setVoltageRampRate(m_rampRateVBus);
			m_rightMotor.reverseOutput(false);
			m_rightMotor.reverseSensor(true);
	
			// Start with the Talons in throttle mode
			setTalonControlMode(CANTalon.ControlMode.PercentVbus, 0, 0);
			
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

	public void setSpeed(double leftSpeed, double rightSpeed) {
		m_leftMotor.set(leftSpeed);
		m_rightMotor.set(rightSpeed);
	}
	
	public void setLeftSpeed(double leftSpeed) {
		m_leftMotor.set(leftSpeed);
	}
	
	public void setRightSpeed(double rightSpeed) {
		m_rightMotor.set(rightSpeed);
	}
	
	private void setTalonControlMode(CANTalon.ControlMode mode, double leftInput, double rightInput) {
		m_talonControlMode = mode;
		m_leftMotor.changeControlMode(m_talonControlMode);
		m_rightMotor.changeControlMode(m_talonControlMode);
		setTalonInput(leftInput, rightInput);
	}
	
	private void setTalonInput(double leftInput, double rightInput) {
		m_leftMotor.set(leftInput);
		m_rightMotor.set(rightInput);
	}
	
	public void startPositionPID(double leftTargetDeg, double rightTargetDeg, double errorDeg) {
		m_leftTarget = leftTargetDeg;
		m_rightTarget = rightTargetDeg;
		setTalonControlMode(CANTalon.ControlMode.Position, 
				RobotUtility.convertDegToAnalogPosition(leftTargetDeg, LEFT_ANALOG_ZERO_PRACTICE), 
				RobotUtility.convertDegToAnalogPosition(rightTargetDeg, RIGHT_ANALOG_ZERO_PRACTICE));
		m_error = errorDeg;
	}
	
	public void stopPID() {
		setTalonControlMode(CANTalon.ControlMode.PercentVbus, 0, 0);
	}
	
	public boolean isAtLeftTarget() {
		return Math.abs(m_leftMotor.getClosedLoopError()) < m_error;
	}
	
	public boolean isAtRightTarget() {
		return Math.abs(m_rightMotor.getClosedLoopError()) < m_error;
	}
	
	public double getLeftErrorDeg() {
		return RobotUtility.convertAnalogPositionToDeg(m_leftMotor.getClosedLoopError(), LEFT_ANALOG_ZERO_PRACTICE);
	}
	
	public double getRightErrorDeg() {
		return RobotUtility.convertAnalogPositionToDeg(m_rightMotor.getClosedLoopError(), RIGHT_ANALOG_ZERO_PRACTICE);
	}
	
	// TODO put zero values in global practice/comp robot flag
	public double getLeftPositionDeg() {
		return RobotUtility.convertAnalogPositionToDeg(m_leftMotor.getAnalogInRaw(), LEFT_ANALOG_ZERO_PRACTICE);
	}
	
	public double getRightPositionDeg() {
		return RobotUtility.convertAnalogPositionToDeg(m_rightMotor.getAnalogInRaw(), RIGHT_ANALOG_ZERO_PRACTICE);
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