package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.OI;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.commands.DriveWithJoystick;
import edu.rhhs.frc.utility.RobotUtility;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Subsystem
{	
	// Talons
	private CANTalon m_frontLeftMotor;
	private CANTalon m_frontRightMotor;
	private CANTalon m_rearLeftMotor;
	private CANTalon m_rearRightMotor;
	
	private CANTalon.ControlMode m_talonControlMode = CANTalon.ControlMode.PercentVbus;
	private DigitalInput di;
	private double m_rearLeftTarget;
	
	private double m_error;
    private double m_kP = 0.15;
    private double m_kI = 0.003;
    private double m_kD = 0.0;
    private double m_kF = 1.4; 
    private int m_iZone = 0;
    private double m_rampRatePID = 0.0;
    private double m_rampRateVBus = 0.0;
    private int m_profile = 0;
	 
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
    
    private int m_controllerMode = CONTROLLER_XBOX_CHEESY;

    public DriveTrain() {
		try {
			di = new DigitalInput(0);
			m_frontLeftMotor = new CANTalon(RobotMap.DRIVETRAIN_FRONT_LEFT_CAN_ID);
			m_frontRightMotor = new CANTalon(RobotMap.DRIVETRAIN_FRONT_RIGHT_CAN_ID);
			
			m_rearLeftMotor = new CANTalon(RobotMap.DRIVETRAIN_REAR_LEFT_CAN_ID);
			m_rearRightMotor = new CANTalon(RobotMap.DRIVETRAIN_REAR_RIGHT_CAN_ID);
			
			// The front motors are setup to "follow" the rear motors
			m_frontLeftMotor.changeControlMode(CANTalon.ControlMode.Follower);
			m_frontLeftMotor.set(RobotMap.DRIVETRAIN_REAR_LEFT_CAN_ID);

			m_frontRightMotor.changeControlMode(CANTalon.ControlMode.Follower);
			m_frontRightMotor.set(RobotMap.DRIVETRAIN_REAR_RIGHT_CAN_ID);

			// The rear motors have encoders attached so they will be used for the main control input
			m_rearLeftMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
			m_rearLeftMotor.setPID(m_kP, m_kI, m_kD, m_kF, m_iZone, m_rampRatePID, m_profile); 
			m_rearLeftMotor.setPosition(0);
			m_rearLeftMotor.setVoltageRampRate(m_rampRateVBus);
			m_rearLeftMotor.reverseSensor(true);

			m_rearRightMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
			m_rearRightMotor.setPID(m_kP, m_kI, m_kD, m_kF, m_iZone, m_rampRatePID, m_profile); 
			m_rearRightMotor.setPosition(0);
			m_rearRightMotor.setVoltageRampRate(m_rampRateVBus);
			m_rearRightMotor.reverseOutput(true);

			// Start with the Talons in throttle mode
			setTalonControlMode(CANTalon.ControlMode.PercentVbus, 0, 0);
			
			m_drive = new RobotDrive(m_rearLeftMotor, m_rearRightMotor);            
            m_drive.setSafetyEnabled(false);
            m_drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
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
		setDefaultCommand(new DriveWithJoystick()); 
	}
	
	private void setTalonControlMode(CANTalon.ControlMode mode, double leftInput, double rightInput) {
		m_talonControlMode = mode;
		m_rearLeftMotor.changeControlMode(m_talonControlMode);
		m_rearRightMotor.changeControlMode(m_talonControlMode);
		setTalonInput(leftInput, rightInput);
	}
	
	private void setTalonInput(double leftInput, double rightInput) {
		m_rearLeftMotor.set(leftInput);
		m_rearRightMotor.set(rightInput);
	}
	
	public void startVelocityPID(double leftTargetDegPerSec, double rightTargetDegPerSec, double errorDegPerSec) {
		m_rearLeftMotor.setPosition(0);
		m_rearRightMotor.setPosition(0);
		m_rearLeftTarget = leftTargetDegPerSec;
		setTalonControlMode(CANTalon.ControlMode.Speed, 
				RobotUtility.convertDegPerSecToEncoderVelocity(leftTargetDegPerSec), 
				RobotUtility.convertDegPerSecToEncoderVelocity(rightTargetDegPerSec));
		m_error = errorDegPerSec;
	}
	
	public void stopPID() {
		setTalonControlMode(CANTalon.ControlMode.PercentVbus, 0, 0);
	}
	
	public boolean isAtLeftTarget() {
		return Math.abs(m_rearLeftMotor.getClosedLoopError()) < m_error;
	}
	
	public boolean isAtRightTarget() {
		return Math.abs(m_rearRightMotor.getClosedLoopError()) < m_error;
	}
	
	public double getLeftError() {
		return m_rearLeftMotor.getClosedLoopError();
	}
	
	public double getRightError() {
		return m_rearRightMotor.getClosedLoopError();
	}
	
	public void setControllerMode(int driveMode) {
		m_controllerMode = driveMode;
	}

	public void driveWithJoystick() {
		if (m_drive != null && m_talonControlMode == CANTalon.ControlMode.PercentVbus) {
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
	
	private String getSwitchPosition() {
		return (!di.get()) ? "Open" : "Closed";
	}

	public void updateStatus() {
		SmartDashboard.putString("Switch 2", getSwitchPosition());
		SmartDashboard.putNumber("Rear Left Encoder Speed Talon", m_rearLeftMotor.getSpeed());
		SmartDashboard.putNumber("Rear Left Target Input", RobotUtility.convertDegPerSecToEncoderVelocity(m_rearLeftTarget));
		SmartDashboard.putNumber("Rear Left SetPoint Talon", m_rearLeftMotor.getSetpoint());
		SmartDashboard.putNumber("Rear Left Output Talon", m_rearLeftMotor.get());
		SmartDashboard.putNumber("Rear Left Error Talon", m_rearLeftMotor.getClosedLoopError());
		SmartDashboard.putNumber("Rear Left Error", RobotUtility.convertEncoderVelocityToDegPerSec(m_rearLeftMotor.getClosedLoopError()));
		SmartDashboard.putNumber("Rear Right Error", RobotUtility.convertEncoderVelocityToDegPerSec(m_rearRightMotor.getClosedLoopError()));
		SmartDashboard.putNumber("Rear Left Pos (deg)", RobotUtility.convertEncoderPositionToDeg(m_rearLeftMotor.getPosition()));
		SmartDashboard.putNumber("Rear Right Pos (deg)", RobotUtility.convertEncoderPositionToDeg(m_rearRightMotor.getPosition()));
		SmartDashboard.putNumber("Rear Left Speed (deg-sec)", RobotUtility.convertEncoderVelocityToDegPerSec(m_rearLeftMotor.getSpeed()));
		SmartDashboard.putNumber("Rear Right Speed (deg-sec)", RobotUtility.convertEncoderVelocityToDegPerSec(m_rearRightMotor.getSpeed()));
		SmartDashboard.putNumber("Rear Left Speed (ft-sec)", RobotUtility.convertEncoderVelocityToFtPerSec(m_rearLeftMotor.getSpeed()));
		SmartDashboard.putNumber("Rear Right Speed (ft-sec)", RobotUtility.convertEncoderVelocityToFtPerSec(m_rearRightMotor.getSpeed()));		
		SmartDashboard.putNumber("Front Left Pos (raw)", m_frontLeftMotor.getAnalogInRaw());		
		SmartDashboard.putNumber("Front Left Pos (deg)", RobotUtility.convertAnalogPositionToDeg(m_frontLeftMotor.getAnalogInRaw(), 512));		
	}
}