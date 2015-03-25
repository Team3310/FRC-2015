package edu.rhhs.frc.subsystems;

import com.kauailabs.navx_mxp.AHRS;
import edu.rhhs.frc.OI;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.commands.DriveWithJoystick;
import edu.rhhs.frc.utility.CANTalonEncoderPID;
import edu.rhhs.frc.utility.ControlLoopable;
import edu.rhhs.frc.utility.ControlLooper;
import edu.rhhs.frc.utility.PIDParams;
import edu.rhhs.frc.utility.motionprofile.ProfileOutput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Subsystem implements ControlLoopable
{	
	public static enum PIDMode {SOFTWARE_TURN, TALON_MOTION_PROFILE, TALON_MOTION_PROFILE_WITH_GYRO};
	public static enum ToteSledPosition {UP, DOWN};

	public static final long OUTER_LOOP_UPDATE_RATE_MS = 10;
	public static final double TRACK_WIDTH_INCHES = 26.425;

	// Talons
	private CANTalonEncoderPID m_frontLeftMotor;
	private CANTalonEncoderPID m_frontRightMotor;
	private CANTalon m_rearLeftMotor;
	private CANTalon m_rearRightMotor;
	
	private CANTalonEncoderPID.ControlMode m_controlMode;
	
	private PIDParams positionMovePidParams = new PIDParams(0.6, 0, 0.0, 0, 50, 0);  
	private PIDParams positionHoldPidParams = new PIDParams(10, 0, 0.0, 0, 50, 0);   
	private PIDParams positionMotionProfilePidParams = new PIDParams(3, 0, 0.0, 0, 50, 0);   
	private PIDParams velocityPidParams = new PIDParams(0.15, 0.007, 0.0, 1.8, 50, 0);
	
	private double m_kPGyroCorrection = 0.005;
	private double m_gyroPathOffset;

	private double m_error;
	private boolean isHoldOn = false;
	 
    private RobotDrive m_drive;
    
	private DoubleSolenoid m_toteSledSolenoid;
    
    // Controllers
    public static final int CONTROLLER_JOYSTICK_ARCADE = 0;
	public static final int CONTROLLER_JOYSTICK_TANK = 1;
	public static final int CONTROLLER_JOYSTICK_CHEESY = 2;
	public static final int CONTROLLER_XBOX_CHEESY = 3;
	public static final int CONTROLLER_XBOX_ARCADE_LEFT = 4;
	public static final int CONTROLLER_XBOX_ARCADE_RIGHT = 5;
	public static final int CONTROLLER_WHEEL = 6;
 
    public static final double STEER_NON_LINEARITY = 1.0;
    public static final double MOVE_NON_LINEARITY = 1.0;
    
 	private int m_moveNonLinear = 0;
    private int m_steerNonLinear = 0;
    
    private double m_moveScale = 0.75;
    private double m_steerScale = 0.75;
    private double m_moveScaleTurbo = 1.0;
    private double m_steerScaleTurbo = 1.0;
    
    private double m_moveInput = 0.0;
    private double m_steerInput = 0.0;
    
    private double m_moveOutput = 0.0;
    private double m_steerOutput = 0.0;
    
    private double m_moveTrim = 0.0;
    private double m_steerTrim = 0.0;
    
	private int m_controllerMode;

	private PIDMode m_pidMode = PIDMode.TALON_MOTION_PROFILE;
    private double m_P = 0.03;    
    private double m_I = 0.00003; // 0.00015;     
    private double m_D = 0.0;     
    private double m_F = 0.0;                 
    private double m_maximumOutput = 1.0; // |maximum output|
    private double m_minimumOutput = -1.0;  // |minimum output|
    private double m_prevError = 0.0; // the prior sensor input (used to compute velocity)
    private double m_totalError = 0.0; //the sum of the errors for use in the integral calc
    private double m_tolerance;  //the tolerance object used to check if on target
    private double m_setpoint = 0.0;
    private double m_result = 0.0;
	
	private ControlLooper m_controlLoop;
	private ProfileOutput m_motionProfile;
	private int m_motionProfileIndex;
    
    private SerialPort m_imuSerialPort;
    private AHRS m_imu = null;
    private boolean m_imuFirstIteration;
    
    public DriveTrain() {
		try {
			m_toteSledSolenoid = new DoubleSolenoid(RobotMap.TOTE_SLED_EXTEND_PNEUMATIC_MODULE_ID, RobotMap.TOTE_SLED_RETRACT_PNEUMATIC_MODULE_ID);

			m_frontLeftMotor = new CANTalonEncoderPID(RobotMap.DRIVETRAIN_FRONT_LEFT_CAN_ID);
			m_frontRightMotor = new CANTalonEncoderPID(RobotMap.DRIVETRAIN_FRONT_RIGHT_CAN_ID);
			
			m_rearLeftMotor = new CANTalon(RobotMap.DRIVETRAIN_REAR_LEFT_CAN_ID);
			m_rearRightMotor = new CANTalon(RobotMap.DRIVETRAIN_REAR_RIGHT_CAN_ID);
			
			m_frontLeftMotor.setSafetyEnabled(false);
			m_frontRightMotor.setSafetyEnabled(false);
			m_rearLeftMotor.setSafetyEnabled(false);
			m_rearRightMotor.setSafetyEnabled(false);
			
			// The front motors are setup to "follow" the rear motors
			m_rearLeftMotor.changeControlMode(CANTalon.ControlMode.Follower);
			m_rearLeftMotor.set(RobotMap.DRIVETRAIN_FRONT_LEFT_CAN_ID);

			m_rearRightMotor.changeControlMode(CANTalon.ControlMode.Follower);
			m_rearRightMotor.set(RobotMap.DRIVETRAIN_FRONT_RIGHT_CAN_ID);

			// The rear motors have encoders attached so they will be used for the main control input
			m_frontLeftMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
			m_frontLeftMotor.setPIDParams(positionMovePidParams, CANTalonEncoderPID.POSITION_PROFILE);
			m_frontLeftMotor.setPIDParams(velocityPidParams, CANTalonEncoderPID.VELOCITY_PROFILE);
			m_frontLeftMotor.reverseSensor(true);
			m_frontLeftMotor.reverseOutput(false);
			m_frontLeftMotor.setPosition(0);

			m_frontRightMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
			m_frontRightMotor.setPIDParams(positionMovePidParams, CANTalonEncoderPID.POSITION_PROFILE);
			m_frontRightMotor.setPIDParams(velocityPidParams, CANTalonEncoderPID.VELOCITY_PROFILE);
			m_frontRightMotor.reverseSensor(false);
			m_frontRightMotor.reverseOutput(true);
			m_frontRightMotor.setPosition(0);

			// Start with the Talons in throttle mode
			setControlMode(CANTalonEncoderPID.ControlMode.PERCENT_VBUS);
			m_frontLeftMotor.set(0);
			m_frontRightMotor.set(0);
			
			m_drive = new RobotDrive(m_frontLeftMotor, m_frontRightMotor);            
            m_drive.setSafetyEnabled(false);
            m_drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
            m_drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

			m_controlLoop = new ControlLooper(this, OUTER_LOOP_UPDATE_RATE_MS);
			m_controlLoop.start();
			
			// You can add a second parameter to modify the 
			// update rate (in hz) from.  The minimum is 4.  
	    	// The maximum (and the default) is 100 on a nav6, 60 on a navX MXP.
			// If you need to minimize CPU load, you can set it to a
			// lower value, as shown here, depending upon your needs.
	    	// The recommended maximum update rate is 50Hz
			
			// You can also use the IMUAdvanced class for advanced
			// features on a nav6 or a navX MXP.
	    	
	    	// You can also use the AHRS class for advanced features on 
	    	// a navX MXP.  This offers superior performance to the
	    	// IMU Advanced class, and also access to 9-axis headings
	    	// and magnetic disturbance detection.  This class also offers
	    	// access to altitude/barometric pressure data from a
	    	// navX MXP Aero.
	    	m_imuSerialPort = new SerialPort(57600,SerialPort.Port.kMXP);
			
			byte updateRateHz = 50;
			m_imu = new AHRS(m_imuSerialPort, updateRateHz);
        } 
		catch (Exception e) {
            System.out.println("Unknown error initializing drivetrain.  Message = " + e.getMessage());
        }
		m_imuFirstIteration = true;
	}
	
	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new DriveWithJoystick()); 
	}
	
	public void teleopInit() {
		stopPID();
	}
	
	public void calibrateIMU() {
		// Set up the IMU
        boolean isCalibrating = m_imu.isCalibrating();
        if ( m_imuFirstIteration && !isCalibrating ) {
            Timer.delay( 0.3 );
            m_imu.zeroYaw();
            m_imuFirstIteration = false; 
        }
	}

    public AHRS getIMU() {
    	return m_imu;
    }
    
    public double getYawAngleDeg() {
    	return -m_imu.getYaw();
    }
    
    public void setYawAngleZero() {
    	m_imu.zeroYaw();
    }
    
	public void keepAlive() {
		m_frontLeftMotor.enableBrakeMode(false);
		m_frontRightMotor.enableBrakeMode(false);
		m_rearLeftMotor.enableBrakeMode(false);
		m_rearRightMotor.enableBrakeMode(false);
	}

	public void setToteSledPosition(ToteSledPosition position) {
		if (position == ToteSledPosition.DOWN) {
			m_toteSledSolenoid.set(DoubleSolenoid.Value.kForward);
    	}
		else {
			m_toteSledSolenoid.set(DoubleSolenoid.Value.kReverse);
		}
	}

	public void setControlMode(CANTalonEncoderPID.ControlMode controlMode) {
		m_frontLeftMotor.setControlMode(controlMode);
		m_frontRightMotor.setControlMode(controlMode);
		m_controlMode = controlMode;
	}
		
	public void startPIDVelocity(double leftTargetDegPerSec, double rightTargetDegPerSec, double errorDegPerSec) {
		setControlMode(CANTalonEncoderPID.ControlMode.VELOCITY);
		m_frontLeftMotor.setPIDVelocityDegPerSecNoLimits(leftTargetDegPerSec);
		m_frontRightMotor.setPIDVelocityDegPerSecNoLimits(rightTargetDegPerSec);
		m_error = errorDegPerSec;
		isHoldOn = false;
	}
	
	public void startPIDPositionHold() {
		m_frontLeftMotor.setPIDParams(positionHoldPidParams, CANTalonEncoderPID.POSITION_PROFILE);
		m_frontRightMotor.setPIDParams(positionHoldPidParams, CANTalonEncoderPID.POSITION_PROFILE);
		startPIDPosition(0, 0, 1);
		isHoldOn = true;
	}
	
	public void startPIDPosition(double leftTargetInches, double rightTargetInches, double errorInches) {
		setControlMode(CANTalonEncoderPID.ControlMode.POSITION);
		m_frontLeftMotor.setPosition(0);
		m_frontRightMotor.setPosition(0);
		m_frontLeftMotor.setPIDPositionInches(leftTargetInches);
		m_frontRightMotor.setPIDPositionInches(rightTargetInches);
		m_error = errorInches;
		isHoldOn = false;
	}
	
	public void stopPID() {
		disableControlLoop();
		setControlMode(CANTalonEncoderPID.ControlMode.PERCENT_VBUS);
		m_frontLeftMotor.set(0);
		m_frontRightMotor.set(0);
		isHoldOn = false;
	}
	
	public void setSpeed(double speed) {
		setControlMode(CANTalonEncoderPID.ControlMode.PERCENT_VBUS);
		m_frontLeftMotor.set(speed);
		m_frontRightMotor.set(-speed);
	}
	
	public boolean isAtLeftTarget() {
		return Math.abs(m_frontLeftMotor.getClosedLoopError()) < m_error;
	}
	
	public boolean isAtRightTarget() {
		return Math.abs(m_frontRightMotor.getClosedLoopError()) < m_error;
	}
	
	public double getLeftDistanceInches() {
		return m_frontLeftMotor.getPositionInches();
	}
	
	public double getRightDistanceInches() {
		return m_frontRightMotor.getPositionInches();
	}
	
	public double getLeftError() {
		return m_frontLeftMotor.getClosedLoopError();
	}
	
	public double getRightError() {
		return m_frontRightMotor.getClosedLoopError();
	}

	public void setJoystickControllerMode(int driveMode) {
		m_controllerMode = driveMode;
	}

	// Motion profiling
	public void startMotionProfile(ProfileOutput profile) {
		if (isControlLoopEnabled()) {
			disableControlLoop();
		}
		m_pidMode = PIDMode.TALON_MOTION_PROFILE;
		m_motionProfile = profile;
		m_motionProfileIndex = 0;
		m_frontLeftMotor.setPIDParams(positionMotionProfilePidParams, CANTalonEncoderPID.POSITION_PROFILE);
		m_frontRightMotor.setPIDParams(positionMotionProfilePidParams, CANTalonEncoderPID.POSITION_PROFILE);
		setControlMode(CANTalonEncoderPID.ControlMode.POSITION);
		m_frontLeftMotor.setPosition(0);
		m_frontRightMotor.setPosition(0);
		m_frontLeftMotor.set(0);
		m_frontRightMotor.set(0);
		isHoldOn = false;
		enableControlLoop();
	}

	public void startMotionProfileWithGyro(ProfileOutput profile, boolean resetGyro) {
		if (isControlLoopEnabled()) {
			disableControlLoop();
		}
		m_pidMode = PIDMode.TALON_MOTION_PROFILE_WITH_GYRO;
		m_motionProfile = profile;
		m_motionProfileIndex = 0;
		m_frontLeftMotor.setPIDParams(positionMotionProfilePidParams, CANTalonEncoderPID.POSITION_PROFILE);
		m_frontRightMotor.setPIDParams(positionMotionProfilePidParams, CANTalonEncoderPID.POSITION_PROFILE);
		setControlMode(CANTalonEncoderPID.ControlMode.POSITION);
		m_frontLeftMotor.setPosition(0);
		m_frontRightMotor.setPosition(0);
		m_frontLeftMotor.set(0);
		m_frontRightMotor.set(0);
		if (resetGyro) {
			setYawAngleZero();
		}
		isHoldOn = false;
		enableControlLoop();
	}

	public void startGyroTurn(double deltaAngleDeg, double toleranceDeg, double maxThrottle) {
		if (isControlLoopEnabled()) {
			disableControlLoop();
		}
		m_pidMode = PIDMode.SOFTWARE_TURN;
		m_setpoint = deltaAngleDeg;
		m_tolerance = toleranceDeg;
		m_maximumOutput = maxThrottle;
		setYawAngleZero();
		setControlMode(CANTalonEncoderPID.ControlMode.PERCENT_VBUS);
		m_frontLeftMotor.set(0);
		m_frontRightMotor.set(0);
		m_error = m_setpoint - getYawAngleDeg();
		isHoldOn = false;
		enableControlLoop();
	}

	public double getGyroTurnError() {
		return m_error;
	}

	public void controlLoopUpdate() {
		if (m_pidMode == PIDMode.TALON_MOTION_PROFILE || m_pidMode == PIDMode.TALON_MOTION_PROFILE_WITH_GYRO) {
			if (m_motionProfileIndex < m_motionProfile.numPoints) {
				double distanceLeft = m_motionProfile.jointPos[m_motionProfileIndex][0];    // hack, use J1
				double distanceRight = m_motionProfile.jointPos[m_motionProfileIndex][1];   // hack, use J2
				
				if ( m_pidMode == PIDMode.TALON_MOTION_PROFILE_WITH_GYRO) {
					m_gyroPathOffset += getYawAngleDeg() * m_kPGyroCorrection;
					distanceLeft += m_gyroPathOffset;
					distanceRight -= m_gyroPathOffset;
				}
				
				m_frontLeftMotor.setPIDPositionInches(distanceLeft);
				m_frontRightMotor.setPIDPositionInches(distanceRight);
				m_motionProfileIndex++;
			}
			else {
				disableControlLoop();
				return;
			}
		}

		else if (m_pidMode == PIDMode.SOFTWARE_TURN) {
			m_error = m_setpoint - getYawAngleDeg();
	        if (m_I != 0) {
	            double potentialIGain = (m_totalError + m_error) * m_I;
	            if (potentialIGain < m_maximumOutput) {
	                if (potentialIGain > m_minimumOutput) {
	                    m_totalError += m_error;
	                } else {
	                    m_totalError = m_minimumOutput / m_I;
	                }
	            } else {
	                m_totalError = m_maximumOutput / m_I;
	            }
	        }
	
	        m_result = m_P * m_error + m_I * m_totalError + m_D * (m_error - m_prevError) + m_setpoint * m_F;
	        m_prevError = m_error;
	
	        if (m_result > m_maximumOutput) {
	            m_result = m_maximumOutput;
	        } else if (m_result < m_minimumOutput) {
	            m_result = m_minimumOutput;
	        }
	        
	        m_frontLeftMotor.set(-m_result);
	        m_frontRightMotor.set(-m_result);
		}
	}

	public synchronized void enableControlLoop() {
		m_controlLoop.enable();
	}

	public synchronized void disableControlLoop() {
		m_controlLoop.disable();
	}

	public synchronized boolean isControlLoopEnabled() {
		return m_controlLoop.isEnabled();
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
				boolean turbo = OI.getInstance().getDriveTrainController().getLeftJoystickButton();
				m_moveInput = OI.getInstance().getDriveTrainController().getLeftYAxis();
				m_steerInput = OI.getInstance().getDriveTrainController().getRightXAxis();
				m_moveOutput = adjustForSensitivity(turbo ? m_moveScaleTurbo : m_moveScale, m_moveTrim, m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
				m_steerOutput = adjustForSensitivity(turbo ? m_steerScaleTurbo : m_steerScale, m_steerTrim, m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
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
//		SmartDashboard.putNumber("Rear Left Pos (deg)", m_frontLeftMotor.getPositionDeg());
//		SmartDashboard.putNumber("Rear Right Pos (deg)", m_frontRightMotor.getPositionDeg());
//		SmartDashboard.putNumber("Rear Left Speed (deg-sec)", m_frontLeftMotor.getVelocityDegPerSec());
//		SmartDashboard.putNumber("Rear Right Speed (deg-sec)", m_frontRightMotor.getVelocityDegPerSec());
//		SmartDashboard.putNumber("Rear Left Speed (ft-sec)", m_frontLeftMotor.getVelocityFtPerSec());
//		SmartDashboard.putNumber("Rear Right Speed (ft-sec)", m_frontRightMotor.getVelocityFtPerSec());		
		SmartDashboard.putNumber("Rear Left Distance (Inches)", m_frontLeftMotor.getPositionInches());
		SmartDashboard.putNumber("Rear Right Distance (Inches)", m_frontRightMotor.getPositionInches());
		SmartDashboard.putBoolean("Drivetrain Hold On", isHoldOn);
		SmartDashboard.putNumber("IMU Yaw (deg)", m_imu.getYaw());
	}
}