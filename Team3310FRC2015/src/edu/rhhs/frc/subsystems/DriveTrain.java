package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.OI;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.commands.DriveWithJoystick;
import edu.rhhs.frc.utility.CANTalonEncoderPID;
import edu.rhhs.frc.utility.PIDParams;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Subsystem
{	
	// Talons
	private CANTalon m_frontLeftMotor;
	private CANTalon m_frontRightMotor;
	private CANTalonEncoderPID m_rearLeftMotor;
	private CANTalonEncoderPID m_rearRightMotor;
	
	private CANTalonEncoderPID.ControlMode m_controlMode;
	
	private PIDParams positionPidParams = new PIDParams(0.4, 0, 0.0, 0, 50, 3);
	private PIDParams velocityPidParams = new PIDParams(0.15, 0.007, 0.0, 1.8, 50, 5);

	private double m_error;
	 
    private RobotDrive m_drive;
    
    // Controllers
    public static final int CONTROLLER_JOYSTICK_ARCADE = 0;
	public static final int CONTROLLER_JOYSTICK_TANK = 1;
	public static final int CONTROLLER_JOYSTICK_CHEESY = 2;
	public static final int CONTROLLER_XBOX_CHEESY = 3;
	public static final int CONTROLLER_XBOX_ARCADE_LEFT = 4;
	public static final int CONTROLLER_XBOX_ARCADE_RIGHT = 5;
	public static final int CONTROLLER_WHEEL = 6;
 
    public static final double STEER_NON_LINEARITY = 0.9;
    public static final double MOVE_NON_LINEARITY = 0.9;
    
 	private int m_moveNonLinear = 0;
    private int m_steerNonLinear = 0;
    
    private double m_moveScale = 1.0;
    private double m_steerScale = 1.0;
    
    private double m_moveInput = 0.0;
    private double m_steerInput = 0.0;
    
    private double m_moveOutput = 0.0;
    private double m_steerOutput = 0.0;
    
    private double m_moveTrim = 0.0;
    private double m_steerTrim = 0.0;
    
	private int m_controllerMode;
    
    public DriveTrain() {
		try {
			m_frontLeftMotor = new CANTalon(RobotMap.DRIVETRAIN_FRONT_LEFT_CAN_ID);
			m_frontRightMotor = new CANTalon(RobotMap.DRIVETRAIN_FRONT_RIGHT_CAN_ID);
			
			m_rearLeftMotor = new CANTalonEncoderPID(RobotMap.DRIVETRAIN_REAR_LEFT_CAN_ID);
			m_rearRightMotor = new CANTalonEncoderPID(RobotMap.DRIVETRAIN_REAR_RIGHT_CAN_ID);
			
			m_frontLeftMotor.setSafetyEnabled(false);
			m_frontRightMotor.setSafetyEnabled(false);
			m_rearLeftMotor.setSafetyEnabled(false);
			m_rearRightMotor.setSafetyEnabled(false);
			
			// The front motors are setup to "follow" the rear motors
			m_frontLeftMotor.changeControlMode(CANTalon.ControlMode.Follower);
			m_frontLeftMotor.set(RobotMap.DRIVETRAIN_REAR_LEFT_CAN_ID);

			m_frontRightMotor.changeControlMode(CANTalon.ControlMode.Follower);
			m_frontRightMotor.set(RobotMap.DRIVETRAIN_REAR_RIGHT_CAN_ID);

			// The rear motors have encoders attached so they will be used for the main control input
			m_rearLeftMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
			m_rearLeftMotor.setPIDParams(positionPidParams, CANTalonEncoderPID.POSITION_PROFILE);
			m_rearLeftMotor.setPIDParams(velocityPidParams, CANTalonEncoderPID.VELOCITY_PROFILE);
			m_rearLeftMotor.reverseSensor(true);
			m_rearLeftMotor.reverseOutput(false);
			m_rearLeftMotor.setPosition(0);

			m_rearRightMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
			m_rearRightMotor.setPIDParams(positionPidParams, CANTalonEncoderPID.POSITION_PROFILE);
			m_rearRightMotor.setPIDParams(velocityPidParams, CANTalonEncoderPID.VELOCITY_PROFILE);
			m_rearRightMotor.reverseSensor(false);
			m_rearRightMotor.reverseOutput(true);
			m_rearRightMotor.setPosition(0);

			// Start with the Talons in throttle mode
			setControlMode(CANTalonEncoderPID.ControlMode.PERCENT_VBUS);
			m_rearLeftMotor.set(0);
			m_rearRightMotor.set(0);
			
			m_drive = new RobotDrive(m_rearLeftMotor, m_rearRightMotor);            
            m_drive.setSafetyEnabled(false);
            m_drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
            m_drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        } 
		catch (Exception e) {
            System.out.println("Unknown error initializing drivetrain.  Message = " + e.getMessage());
        }
	}
	
	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new DriveWithJoystick()); 
	}
	
	public void teleopInit() {
		setControlMode(CANTalonEncoderPID.ControlMode.PERCENT_VBUS);
		m_rearLeftMotor.set(0);
		m_rearRightMotor.set(0);
	}

	public void keepAlive() {
		m_frontLeftMotor.enableBrakeMode(true);
		m_frontRightMotor.enableBrakeMode(true);
		m_rearLeftMotor.enableBrakeMode(true);
		m_rearRightMotor.enableBrakeMode(true);
	}

	public void setControlMode(CANTalonEncoderPID.ControlMode controlMode) {
		m_rearLeftMotor.setControlMode(controlMode);
		m_rearRightMotor.setControlMode(controlMode);
		m_controlMode = controlMode;
	}
		
	public void startPIDVelocity(double leftTargetDegPerSec, double rightTargetDegPerSec, double errorDegPerSec) {
		setControlMode(CANTalonEncoderPID.ControlMode.VELOCITY);
		m_rearLeftMotor.setPIDVelocityDegPerSecNoLimits(leftTargetDegPerSec);
		m_rearRightMotor.setPIDVelocityDegPerSecNoLimits(rightTargetDegPerSec);
		m_error = errorDegPerSec;
	}
	
	public void startPIDPosition(double leftTargetInches, double rightTargetInches, double errorInches) {
		setControlMode(CANTalonEncoderPID.ControlMode.POSITION);
		m_rearLeftMotor.setPosition(0);
		m_rearRightMotor.setPosition(0);
		m_rearLeftMotor.setPIDPositionInches(leftTargetInches);
		m_rearRightMotor.setPIDPositionInches(rightTargetInches);
		m_error = errorInches;
	}
	
	public void stopPID() {
		setSpeed(0);
	}
	
	public void setSpeed(double speed) {
		setControlMode(CANTalonEncoderPID.ControlMode.PERCENT_VBUS);
		m_rearLeftMotor.set(speed);
		m_rearRightMotor.set(speed);
	}
	
	public boolean isAtLeftTarget() {
		return Math.abs(m_rearLeftMotor.getClosedLoopError()) < m_error;
	}
	
	public boolean isAtRightTarget() {
		return Math.abs(m_rearRightMotor.getClosedLoopError()) < m_error;
	}
	
	public double getLeftDistanceInches() {
		return m_rearLeftMotor.getPositionInches();
	}
	
	public double getRightDistanceInches() {
		return m_rearRightMotor.getPositionInches();
	}
	
	public double getLeftError() {
		return m_rearLeftMotor.getClosedLoopError();
	}
	
	public double getRightError() {
		return m_rearRightMotor.getClosedLoopError();
	}

	public void setJoystickControllerMode(int driveMode) {
		m_controllerMode = driveMode;
	}

	public void driveWithJoystick() {
		if (m_drive != null && m_controlMode == CANTalonEncoderPID.ControlMode.PERCENT_VBUS) {
//			switch(m_controllerMode) {
//			case CONTROLLER_JOYSTICK_ARCADE:
//				m_moveInput = OI.getInstance().getJoystick1().getY();
//				m_steerInput = OI.getInstance().getJoystick1().getX();
//				m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
//				m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim, m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
//				m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
//				break;
//			case CONTROLLER_JOYSTICK_TANK:
//				m_moveInput = OI.getInstance().getJoystick1().getY();
//				m_steerInput = OI.getInstance().getJoystick2().getY();
//				m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
//				m_steerOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_steerInput, m_moveNonLinear, MOVE_NON_LINEARITY);
//				m_drive.tankDrive(m_moveOutput, m_steerOutput);
//				break;
//			case CONTROLLER_JOYSTICK_CHEESY:
//				m_moveInput = OI.getInstance().getJoystick1().getY();
//				m_steerInput = OI.getInstance().getJoystick2().getX();
//				m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
//				m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim, m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
//				m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
//				break;
//			case CONTROLLER_XBOX_CHEESY:
				m_moveInput = OI.getInstance().getDrivetrainController().getLeftYAxis();
				m_steerInput = OI.getInstance().getDrivetrainController().getRightXAxis();
				m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
				m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim, m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
				m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
//				break;
//			case CONTROLLER_XBOX_ARCADE_RIGHT:
//				m_moveInput = OI.getInstance().getDrivetrainController().getRightYAxis();
//				m_steerInput = OI.getInstance().getDrivetrainController().getRightXAxis();
//				m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
//				m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim, m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
//				m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
//				break;
//			case CONTROLLER_XBOX_ARCADE_LEFT:
//				m_moveInput = OI.getInstance().getDrivetrainController().getLeftYAxis();
//				m_steerInput = OI.getInstance().getDrivetrainController().getLeftXAxis();
//				m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
//				m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim, m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
//				m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
//				break;
//			}
		}
	}
	
	private double adjustForSensitivity(double scale, double trim, double steer, int nonLinearFactor, double wheelNonLinearity) {
		steer += trim;
		steer *= scale;
		steer = limitValue(steer);

		int iterations = Math.abs(nonLinearFactor);
		for (int i = 0; i < iterations; i++) {
			if (nonLinearFactor > 0) {
				steer = nonlinearStickCalcPositive(steer, wheelNonLinearity);
			} else {
				steer = nonlinearStickCalcNegative(steer, wheelNonLinearity);
			}
		}
		return steer;
	}

	private double limitValue(double value) {
		if (value > 1.0) {
			value = 1.0;
		} else if (value < -1.0) {
			value = -1.0;
		}
		return value;
	}

	private double nonlinearStickCalcPositive(double steer, double steerNonLinearity) {
		return Math.sin(Math.PI / 2.0 * steerNonLinearity * steer) / Math.sin(Math.PI / 2.0 * steerNonLinearity);
	}

	private double nonlinearStickCalcNegative(double steer, double steerNonLinearity) {
		return Math.asin(steerNonLinearity * steer) / Math.asin(steerNonLinearity);
	}
	
	public void updateStatus() {
		SmartDashboard.putNumber("Rear Left Pos (deg)", m_rearLeftMotor.getPositionDeg());
		SmartDashboard.putNumber("Rear Right Pos (deg)", m_rearRightMotor.getPositionDeg());
		SmartDashboard.putNumber("Rear Left Speed (deg-sec)", m_rearLeftMotor.getVelocityDegPerSec());
		SmartDashboard.putNumber("Rear Right Speed (deg-sec)", m_rearRightMotor.getVelocityDegPerSec());
		SmartDashboard.putNumber("Rear Left Speed (ft-sec)", m_rearLeftMotor.getVelocityFtPerSec());
		SmartDashboard.putNumber("Rear Right Speed (ft-sec)", m_rearRightMotor.getVelocityFtPerSec());		
		SmartDashboard.putNumber("Rear Left Distance (Inches)", m_rearLeftMotor.getPositionInches());
		SmartDashboard.putNumber("Rear Right Distance (Inches)", m_rearRightMotor.getPositionInches());
	}
}