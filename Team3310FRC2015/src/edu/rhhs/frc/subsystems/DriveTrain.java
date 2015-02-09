package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.OI;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.commands.DriveWithJoystick;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Subsystem
{	
	public static final int CONTROLLER_JOYSTICK_ARCADE = 0;
	public static final int CONTROLLER_JOYSTICK_TANK = 1;
	public static final int CONTROLLER_JOYSTICK_CHEESY = 2;
	public static final int CONTROLLER_XBOX_CHEESY = 3;
	public static final int CONTROLLER_XBOX_ARCADE_LEFT = 4;
	public static final int CONTROLLER_XBOX_ARCADE_RIGHT = 5;
	//public static final int CONTROLLER_WHEEL = 6;

	private CANTalon m_frontLeftMotor;
	private CANTalon m_frontRightMotor;
	private CANTalon m_rearLeftMotor;
	private CANTalon m_rearRightMotor;
 
    private RobotDrive m_drive;
    
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
	
    private int m_controllerMode = CONTROLLER_XBOX_CHEESY;

    public DriveTrain() {
		this.m_frontLeftMotor = new CANTalon(RobotMap.DRIVETRAIN_FRONT_LEFT_CAN_ID);
		this.m_frontRightMotor = new CANTalon(RobotMap.DRIVETRAIN_FRONT_RIGHT_CAN_ID);
		this.m_rearLeftMotor = new CANTalon(RobotMap.DRIVETRAIN_REAR_LEFT_CAN_ID);
		this.m_rearRightMotor = new CANTalon(RobotMap.DRIVETRAIN_REAR_RIGHT_CAN_ID);
		m_rearLeftMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		m_rearRightMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		m_rearLeftMotor.reverseSensor(true);
		//How much of Encoder position = 1 revolution? Customary to our robot?
		m_rearLeftMotor.setPosition(0);
		m_rearRightMotor.setPosition(0);
 
		try {
            m_drive = new RobotDrive(m_frontLeftMotor, m_rearLeftMotor, m_frontRightMotor, m_rearRightMotor);
            
            m_drive.setSafetyEnabled(false);
            m_drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
            m_drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
            m_drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
            m_drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
            
//            SmartDashboard.putNumber("Move NonLinear ", m_moveNonLinear);
//            SmartDashboard.putNumber("Steer NonLinear ", m_steerNonLinear);
//            SmartDashboard.putNumber("Move Scale ", m_moveScale);
//            SmartDashboard.putNumber("Steer Scale ", m_steerScale);
//            SmartDashboard.putNumber("Move Trim ", -m_moveTrim);
//            SmartDashboard.putNumber("Steer Trim ", m_steerTrim); 
        } catch (Exception e) {
            System.out.println("Unknown error initializing drivetrain.  Message = " + e.getMessage());
        }
	}
	
	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new DriveWithJoystick()); // set default command
	}
	
	public void setVelocity(double velocityDegPerSec) {
		m_rearLeftMotor.setPID(0.3, 0.0, 0.0);
		m_rearLeftMotor.setF(0.01);
		m_rearLeftMotor.changeControlMode(ControlMode.Speed);
		m_rearLeftMotor.set(velocityDegPerSec);
		m_rearLeftMotor.enableControl();
	}
	
	public boolean isAtTarget() {
		return m_rearLeftMotor.getClosedLoopError() < 100;
	}
	
	public void setDriveMode(int driveMode) {
		m_controllerMode = driveMode;
	}

	public void driveWithJoystick() {
		if (m_drive != null) {
			switch(m_controllerMode) {
			case CONTROLLER_JOYSTICK_ARCADE:
				m_moveInput = OI.getInstance().getJoystick1().getY();
				m_steerInput = OI.getInstance().getJoystick1().getX();
				m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
				m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim, m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
				m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
				break;
			case CONTROLLER_JOYSTICK_TANK:
				m_moveInput = OI.getInstance().getJoystick1().getY();
				m_steerInput = OI.getInstance().getJoystick2().getY();
				m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
				m_steerOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_steerInput, m_moveNonLinear, MOVE_NON_LINEARITY);
				m_drive.tankDrive(m_moveOutput, m_steerOutput);
				break;
			case CONTROLLER_JOYSTICK_CHEESY:
				m_moveInput = OI.getInstance().getJoystick1().getY();
				m_steerInput = OI.getInstance().getJoystick2().getX();
				m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
				m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim, m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
				m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
				break;
			case CONTROLLER_XBOX_CHEESY:
				m_moveInput = OI.getInstance().getXBoxController().getLeftYAxis();
				m_steerInput = OI.getInstance().getXBoxController().getRightXAxis();
				m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
				m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim, m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
				m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
				break;
			case CONTROLLER_XBOX_ARCADE_RIGHT:
				m_moveInput = OI.getInstance().getXBoxController().getRightYAxis();
				m_steerInput = OI.getInstance().getXBoxController().getRightXAxis();
				m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
				m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim, m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
				m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
				break;
			case CONTROLLER_XBOX_ARCADE_LEFT:
				m_moveInput = OI.getInstance().getXBoxController().getLeftYAxis();
				m_steerInput = OI.getInstance().getXBoxController().getLeftXAxis();
				m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
				m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim, m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
				m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
				break;
			// case CONTROLLER_WHEEL:
			// m_moveInput = OI.getInstance().getJoystick1().getY();
			// m_steerInput = OI.getInstance().getSteeringWheel().getX();
			// m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim,
			// m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
			// m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim,
			// m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
			// m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
			// break;
			}
			SmartDashboard.putNumber("Rear Left Pos (deg)", convertEncodePositionToDeg(m_rearLeftMotor.getPosition()));
			SmartDashboard.putNumber("Rear Right Pos (deg)", convertEncodePositionToDeg(m_rearRightMotor.getPosition()));
			SmartDashboard.putNumber("Rear Left Speed (deg-sec)", convertEncoderVelocityToDegPerSec(m_rearLeftMotor.getSpeed()));
			SmartDashboard.putNumber("Rear Right Speed (deg-sec)", convertEncoderVelocityToDegPerSec(m_rearRightMotor.getSpeed()));
			SmartDashboard.putNumber("Rear Left Speed (ft-sec)", convertEncoderVelocityToFtPerSec(m_rearLeftMotor.getSpeed()));
			SmartDashboard.putNumber("Rear Right Speed (ft-sec)", convertEncoderVelocityToFtPerSec(m_rearRightMotor.getSpeed()));
		}
	}
	
	private double convertEncodePositionToDeg(double encoderValue) {
		return 360.0 * encoderValue/ (4 * RobotMap.GRAYHILL_ENCODER_COUNT);
	}

	private double convertDegToEncoderPosition(double inputValue) {
		return (4 * RobotMap.GRAYHILL_ENCODER_COUNT) / 360.0;
	}

	private double convertEncoderVelocityToDegPerSec(double encoderValue) {
		return convertEncodePositionToDeg(encoderValue) * 10;
	}

	private double convertEncoderVelocityToFtPerSec(double encoderValue) {
		return convertEncoderVelocityToDegPerSec(encoderValue) * RobotMap.WHEEL_DIAMETER_IN * Math.PI / 12.0 / 360.0;
	}

	private double convertDegPerSecToEncoderVelocity(double encoderValue) {
		return convertDegToEncoderPosition(encoderValue) / 10.0;
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

}