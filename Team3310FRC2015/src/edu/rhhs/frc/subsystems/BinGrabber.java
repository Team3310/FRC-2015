package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.OI;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.commands.BinGrabberWithJoystick;
import edu.rhhs.frc.utility.RobotUtility;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *	BinGrabber (name to be determined) is a subsystem that controls the two motors associated with each of the two arms (codenames: "Bubba" and "Gump").
 */
public class BinGrabber extends Subsystem 
{
	private static final double LEFT_ANALOG_ZERO = 362.0;
	private static final double RIGHT_ANALOG_ZERO = -590.0;
	public static final double DEPLOYED_POSITION_DEG = 70;
	public static final double STOWED_POSITION_DEG = 0;
	public static final double DRAG_BIN_POSITION_DEG = 60;
			
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
		m_rightMotor = new CANTalon(RobotMap.BINGRABBER_RIGHT_CAN_ID);
		m_leftMotor = new CANTalon(RobotMap.BINGRABBER_LEFT_CAN_ID);

		m_leftMotor.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogPot);
		m_leftMotor.setPID(m_kP, m_kI, m_kD, m_kF, m_iZone, m_rampRatePID, m_profile);
		m_leftMotor.setVoltageRampRate(m_rampRateVBus);
		//m_leftMotor.enableForwardSoftLimit(true);
		//m_leftMotor.enableReverseSoftLimit(true);
		//m_leftMotor.setForwardSoftLimit((int) RobotUtility.convertDegToAnalogPosition(DEPLOYED_POSITION_DEG, LEFT_ANALOG_ZERO));
		//m_leftMotor.setReverseSoftLimit((int) RobotUtility.convertDegToAnalogPosition(STOWED_POSITION_DEG, LEFT_ANALOG_ZERO));
		m_leftMotor.reverseOutput(true);
		m_leftMotor.reverseSensor(false);

		m_rightMotor.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogPot);
		m_rightMotor.setPID(m_kP, m_kI, m_kD, m_kF, m_iZone, m_rampRatePID, m_profile);
		m_rightMotor.setVoltageRampRate(m_rampRateVBus);
		m_rightMotor.reverseOutput(false);
		m_rightMotor.reverseSensor(true);

		// Start with the Talons in throttle mode
		setTalonControlMode(CANTalon.ControlMode.PercentVbus, 0, 0);
	}

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new BinGrabberWithJoystick());
	}

	public void setSpeed(double leftSpeed, double rightSpeed) {
		m_leftMotor.set(leftSpeed);
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
				RobotUtility.convertDegToAnalogPosition(leftTargetDeg, LEFT_ANALOG_ZERO), 
				RobotUtility.convertDegToAnalogPosition(rightTargetDeg, RIGHT_ANALOG_ZERO));
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
	
	public double getLeftError() {
		return m_leftMotor.getClosedLoopError();
	}
	
	public double getRightError() {
		return m_rightMotor.getClosedLoopError();
	}
	
	public void controlWithJoystick() {
		setTalonInput(0, -OI.getInstance().getXBoxController().getRightYAxis());
	}

	public void updateStatus() {
		SmartDashboard.putNumber("Left Bin Grabber Throttle (raw)", m_leftMotor.get());
		SmartDashboard.putNumber("Right Bin Grabber get", m_rightMotor.get());
		SmartDashboard.putNumber("Left Bin Grabber getAnalogInRaw)", m_leftMotor.getAnalogInRaw());
		SmartDashboard.putNumber("Right Bin Grabber getPosition", m_rightMotor.getPosition());
		SmartDashboard.putNumber("Right Bin Grabber Analog Position (raw)", m_rightMotor.getAnalogInRaw());
		SmartDashboard.putNumber("Left Bin Grabber Position (Deg)", RobotUtility.convertAnalogPositionToDeg(m_leftMotor.getAnalogInRaw(), LEFT_ANALOG_ZERO));
		SmartDashboard.putNumber("Right Bin Grabber Position (Deg)", RobotUtility.convertAnalogPositionToDeg(m_rightMotor.getAnalogInRaw(), RIGHT_ANALOG_ZERO));
	}
}