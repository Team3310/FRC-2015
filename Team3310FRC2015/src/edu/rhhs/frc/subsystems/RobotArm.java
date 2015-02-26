package edu.rhhs.frc.subsystems;

import java.util.Timer;
import java.util.TimerTask;

import edu.rhhs.frc.OI;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.commands.RobotArmWithJoystick;
import edu.rhhs.frc.commands.robotarm.RobotArmCommand;
import edu.rhhs.frc.commands.robotarm.RobotArmCommandList;
import edu.rhhs.frc.utility.CANTalonAnalogPID;
import edu.rhhs.frc.utility.CANTalonEncoderPID;
import edu.rhhs.frc.utility.CANTalonEncoderPID.ControlMode;
import edu.rhhs.frc.utility.PIDParams;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RobotArm extends Subsystem {

	public static enum ToteGrabberPosition {OPEN, CLOSE};

	public static final long OUTER_LOOP_UPDATE_RATE_MS = 10;

	// Robot arm joint offset angles to help deal with feed forward gain (Kf * target)
	// This offset is applied inside the Talon only, it gets added on for input, subtracted off on output
	private static final double J1_ENCODER_OFFSET_DEG = 0.0;
	private static final double J2_ENCODER_OFFSET_DEG = 0.0;
	private static final double J3_ENCODER_OFFSET_DEG = 90.0;
	private static final double J4_ENCODER_OFFSET_DEG = 0.0;

	// Robot arm joint angles when system is powered on (or new code is downloaded)
	public static final double J1_MASTER_ANGLE_DEG = 0.0;
	public static final double J2_MASTER_ANGLE_DEG = 97.36;
	public static final double J3_MASTER_ANGLE_DEG = -109.88;
	public static final double J4_MASTER_ANGLE_DEG = 0.0;
	
	public static final double X_MASTER_POSITION_IN = 26.567764225774383;
	public static final double Y_MASTER_POSITION_IN = 0.0;
	public static final double Z_MASTER_POSITION_IN = 8.889902513201704;
	public static final double GAMMA_MASTER_ANGLE_DEG = 0.0;
 
	// J4 raw analog value when J4 is set to 0 deg 
	private static final int 	J4_ANALOG_ZERO_PRACTICE = 512;

	// Robot arm joint to sensor gear ratios
	private static final double J1_SENSOR_GEAR_RATIO = 84.0/18.0;
	private static final double J2_SENSOR_GEAR_RATIO = 84.0/18.0;
	private static final double J3_SENSOR_GEAR_RATIO = 84.0/18.0;
	private static final double J4_SENSOR_GEAR_RATIO = 60.0/30.0;

	private static final double J1_MAX_ANGLE_DEG = 170.0;
	private static final double J1_MIN_ANGLE_DEG = -170.0;
	private static final double J2_MAX_ANGLE_DEG = 100.0;
	private static final double J2_MIN_ANGLE_DEG = -30.0;
	private static final double J3_MAX_ANGLE_DEG = 27.0;
	private static final double J3_MIN_ANGLE_DEG = -110.0;
	private static final double J4_MAX_ANGLE_DEG = 70.0;
	private static final double J4_MIN_ANGLE_DEG = -60.0;
	
	private static final double J3_INTERFERENCE_J2_PLUS_J3_MIN_ANGLE_DEG = -60.0;
	private static final double J3_INTERFERENCE_J2_PLUS_J3_MAX_ANGLE_DEG = 60.0;
	private static final double J3_HEIGHT_LIMIT_J2_MIN_ANGLE_DEG = 0.0;
	private static final double J3_HEIGHT_LIMIT_J2_MAX_ANGLE_DEG = 40.0;
	private static final double J3_HEIGHT_LIMIT_FACTOR_J2_LESS_THAN_MIN = -0.5;
	private static final double J3_HEIGHT_LIMIT_FACTOR_J2_GREATER_THAN_MIN = 0.5;

	// This is for manual control in velocity mode.  The rates for motion profile are 
	// set in MotionProfile.java
	private static final double J1_MAX_SPEED_DEG_PER_SEC = 200.0;
	private static final double J2_MAX_SPEED_DEG_PER_SEC = 180.0;
	private static final double J3_MAX_SPEED_DEG_PER_SEC = 180.0;
	private static final double J4_MAX_SPEED_DEG_PER_SEC = 200.0;

	// Jog speed
	private static final double JOG_SPEED_INCHES_PER_SEC = 40.0;

	private static final double JOYSTICK_DEADBAND_THROTTLE_POSITION = 0.2;

	private CANTalonEncoderPID m_j1Motor;
	private CANTalonEncoderPID m_j2Motor;
	private CANTalonEncoderPID m_j3Motor;
	private CANTalonAnalogPID  m_j4Motor;

	private PIDParams j1PositionPidParams = new PIDParams(3.0, 0.0, 0.1, 0.0, 50, 0);
//	private PIDParams j2PositionPidParams = new PIDParams(3.5, 0.0006, 0.15, -0.426, 150, 0);
//	private PIDParams j3PositionPidParams = new PIDParams(1.5, 0.0006, 0.15, 0.341, 150, 0);
	private PIDParams j2PositionPidParams = new PIDParams(3.5, 0.0006, 0.15, -0.2, 150, 0);
	private PIDParams j3PositionPidParams = new PIDParams(1.5, 0.0006, 0.15, 0.17, 150, 0);
	private PIDParams j4PositionPidParams = new PIDParams(5.0, 0.005, 0.0, 0.0, 50, 0);

	private PIDParams j1VelocityPidParams = new PIDParams(0.5, 0.005, 0.0, 0.0, 0, 0);
	private PIDParams j2VelocityPidParams = new PIDParams(0.5, 0.02, 0.0, 0.0, 0, 0);
	private PIDParams j3VelocityPidParams = new PIDParams(0.5, 0.008, 0.0, 0.0, 0, 0);
	private PIDParams j4VelocityPidParams = new PIDParams(1.0, 0.027, 0.0, 0.0, 0, 0);

	private DigitalInput m_toteGrabberSwitch;
	private DoubleSolenoid m_toteGrabberSolenoid;

	private CANTalonEncoderPID.ControlMode m_robotArmControlMode = CANTalonEncoderPID.ControlMode.VELOCITY_POSITION_HOLD;

	// Initialize position commands for proper robot startup
	private double m_positionCommandJ1 = J1_MASTER_ANGLE_DEG;
	private double m_positionCommandJ2 = J2_MASTER_ANGLE_DEG;
	private double m_positionCommandJ3 = J3_MASTER_ANGLE_DEG;
	private double m_positionCommandJ4 = J4_MASTER_ANGLE_DEG;
	private double m_limitJ3Deg;
	private boolean isJ3Limited = false;
	
	private RobotArmCommandList m_controllerLoopCommandList;
	private RobotArmCommand m_currentControllerLoopCommand;
	private int m_currentControllerLoopCommandIndex;

	private Timer m_controlLoop;
	private boolean m_controlLoopEnabled = false;
	private long m_controlLoopStartTime = 0;
	private long m_controlLoopDeltaTime = 0;
	private boolean m_waitForNext;
	
	private MotionProfile motionProfileForOutput = new MotionProfile();

	private class ControlLoopTask extends TimerTask {

		private RobotArm arm;

		public ControlLoopTask(RobotArm arm) {
			if (arm == null) {
				throw new NullPointerException("Given RobotArm was null");
			}
			this.arm = arm;
		}

		@Override
		public void run() {
			arm.controlLoopUpdate();
		}
	}

	public RobotArm() {
		try {
			m_j1Motor = new CANTalonEncoderPID(RobotMap.ROBOT_ARM_J1_CAN_ID, J1_SENSOR_GEAR_RATIO, J1_ENCODER_OFFSET_DEG, J1_MASTER_ANGLE_DEG, J1_MIN_ANGLE_DEG, J1_MAX_ANGLE_DEG);
			m_j2Motor = new CANTalonEncoderPID(RobotMap.ROBOT_ARM_J2_CAN_ID, J2_SENSOR_GEAR_RATIO, J2_ENCODER_OFFSET_DEG, J2_MASTER_ANGLE_DEG, J2_MIN_ANGLE_DEG, J2_MAX_ANGLE_DEG);
			m_j3Motor = new CANTalonEncoderPID(RobotMap.ROBOT_ARM_J3_CAN_ID, J3_SENSOR_GEAR_RATIO, J3_ENCODER_OFFSET_DEG, J3_MASTER_ANGLE_DEG, J3_MIN_ANGLE_DEG, J3_MAX_ANGLE_DEG);
			m_j4Motor = new CANTalonAnalogPID (RobotMap.ROBOT_ARM_J4_CAN_ID, J4_SENSOR_GEAR_RATIO, J4_ENCODER_OFFSET_DEG, J4_MASTER_ANGLE_DEG, J4_MIN_ANGLE_DEG, J4_MAX_ANGLE_DEG, J4_ANALOG_ZERO_PRACTICE);

			m_j1Motor.setSafetyEnabled(false);
			m_j2Motor.setSafetyEnabled(false);
			m_j3Motor.setSafetyEnabled(false);
			m_j4Motor.setSafetyEnabled(false);

			m_j1Motor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
			m_j1Motor.reverseOutput(true);
			m_j1Motor.reverseSensor(false);

			m_j2Motor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
			m_j2Motor.reverseOutput(true);
			m_j2Motor.reverseSensor(false);

			m_j3Motor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
			m_j3Motor.reverseOutput(true);
			m_j3Motor.reverseSensor(false);

			m_j4Motor.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogPot);
			m_j4Motor.reverseOutput(true);
			m_j4Motor.reverseSensor(false);
			
			m_j1Motor.setPIDParams(j1PositionPidParams, CANTalonEncoderPID.POSITION_PROFILE);
			m_j2Motor.setPIDParams(j2PositionPidParams, CANTalonEncoderPID.POSITION_PROFILE);
			m_j3Motor.setPIDParams(j3PositionPidParams, CANTalonEncoderPID.POSITION_PROFILE);
			m_j4Motor.setPIDParams(j4PositionPidParams, CANTalonEncoderPID.POSITION_PROFILE);

			m_j1Motor.setPIDParams(j1VelocityPidParams, CANTalonEncoderPID.VELOCITY_PROFILE);
			m_j2Motor.setPIDParams(j2VelocityPidParams, CANTalonEncoderPID.VELOCITY_PROFILE);
			m_j3Motor.setPIDParams(j3VelocityPidParams, CANTalonEncoderPID.VELOCITY_PROFILE);
			m_j4Motor.setPIDParams(j4VelocityPidParams, CANTalonEncoderPID.VELOCITY_PROFILE);

			resetMasterPosition();
			setControlMode(CANTalonEncoderPID.ControlMode.POSITION_INCREMENTAL);	

			m_toteGrabberSwitch = new DigitalInput(RobotMap.TOTE_GRABBER_SWITCH);	
			m_toteGrabberSolenoid = new DoubleSolenoid(RobotMap.TOTE_GRABBER_EXTEND_PNEUMATIC_MODULE_ID, RobotMap.TOTE_GRABBER_RETRACT_PNEUMATIC_MODULE_ID);
			setToteGrabberPosition(ToteGrabberPosition.OPEN);		

			m_controlLoop = new Timer();
			m_controlLoop.schedule(new ControlLoopTask(this), 0L, OUTER_LOOP_UPDATE_RATE_MS);
		}
		catch (Exception e) {
			System.out.println("Unknown error initializing robot arm.  Message = " + e.getMessage());
		}
	}

	public void initDefaultCommand() {
		setDefaultCommand(new RobotArmWithJoystick());
	}
	
	public void keepAlive() {
		m_j1Motor.enableBrakeMode(true);
		m_j2Motor.enableBrakeMode(true);
		m_j3Motor.enableBrakeMode(true);
		m_j4Motor.enableBrakeMode(true);
	}
	
	public void resetMasterPosition() {
		m_j1Motor.inititializeSensorPosition();
		m_j2Motor.inititializeSensorPosition();
		m_j3Motor.inititializeSensorPosition();
	}

	public void setControlMode(CANTalonEncoderPID.ControlMode mode) {
		m_robotArmControlMode = mode;
		m_j1Motor.setControlMode(mode);
		m_j2Motor.setControlMode(mode);
		m_j3Motor.setControlMode(mode);
		m_j4Motor.setControlMode(mode);
		
		// If you change mode, need to update the set command
		if (mode == ControlMode.POSITION || mode == ControlMode.POSITION_INCREMENTAL) {			
			m_positionCommandJ1 = m_j1Motor.setInitPosition();
			m_positionCommandJ2 = m_j2Motor.setInitPosition();
			m_positionCommandJ3 = m_j3Motor.setInitPosition();
			m_positionCommandJ4 = m_j4Motor.setInitPosition();
		}
		else {
			m_j1Motor.set(0);
			m_j2Motor.set(0);
			m_j3Motor.set(0);
			m_j4Motor.set(0);
		}
	}

	public synchronized boolean getToteGrabberSwitch() {
		return !m_toteGrabberSwitch.get();
	}

	public synchronized void setToteGrabberPosition(ToteGrabberPosition position) {
		if (position == ToteGrabberPosition.OPEN) {
			m_toteGrabberSolenoid.set(DoubleSolenoid.Value.kForward);
		}
		else {
			m_toteGrabberSolenoid.set(DoubleSolenoid.Value.kReverse);
		}
	}

	public synchronized ToteGrabberPosition getToteGrabberPosition() {
		if (m_toteGrabberSolenoid.get() == DoubleSolenoid.Value.kForward) {
			return ToteGrabberPosition.OPEN;
		}
		return ToteGrabberPosition.CLOSE;
	}

	public synchronized boolean isWaitForNext() {
		return m_waitForNext;
	}

	public synchronized void setWaitForNext(boolean waitForNext) {
		this.m_waitForNext = waitForNext;
	}

	public void controlWithJoystick() {
		if (!m_controlLoopEnabled) {
			double throttleRightX = OI.getInstance().getRobotArmController().getRightXAxis();
			double throttleRightY = OI.getInstance().getRobotArmController().getRightYAxis();
			double throttleLeftX = OI.getInstance().getRobotArmController().getLeftXAxis();
			double throttleLeftY = OI.getInstance().getRobotArmController().getLeftYAxis();

			double triggerRight = OI.getInstance().getRobotArmController().getRightTriggerAxis();
			double triggerLeft = OI.getInstance().getRobotArmController().getLeftTriggerAxis();

//			SmartDashboard.putNumber("Right X Throttle", throttleRightX);
//			SmartDashboard.putNumber("Right Y Throttle", throttleRightY);
//			SmartDashboard.putNumber("Left X Throttle" , throttleLeftX);
//			SmartDashboard.putNumber("Left Y Throttle" , throttleLeftY);

			if (m_robotArmControlMode == CANTalonEncoderPID.ControlMode.VELOCITY_POSITION_HOLD) {
				double velocityCommandJ1 = -throttleRightX * J1_MAX_SPEED_DEG_PER_SEC;
				double velocityCommandJ2 = -throttleRightY * J2_MAX_SPEED_DEG_PER_SEC;
				double velocityCommandJ3 = -throttleLeftY  * J3_MAX_SPEED_DEG_PER_SEC;
				double velocityCommandJ4 = -throttleLeftX  * J4_MAX_SPEED_DEG_PER_SEC;

				SmartDashboard.putNumber("J1 Stick Command", velocityCommandJ1);
				SmartDashboard.putNumber("J2 Stick Command", velocityCommandJ2);
				SmartDashboard.putNumber("J3 Stick Command", velocityCommandJ3);
				SmartDashboard.putNumber("J4 Stick Command", velocityCommandJ4);

				m_j1Motor.setStickInputVelocityDegPerSec(velocityCommandJ1);
				m_j2Motor.setStickInputVelocityDegPerSec(velocityCommandJ2);
				m_j3Motor.setStickInputVelocityDegPerSec(velocityCommandJ3);
				m_j4Motor.setStickInputVelocityDegPerSec(velocityCommandJ4);
			}
			else if (m_robotArmControlMode == CANTalonEncoderPID.ControlMode.POSITION) {
				m_positionCommandJ1 = -throttleRightX * Math.max(J1_MAX_ANGLE_DEG, -J1_MIN_ANGLE_DEG) / 2;
				m_positionCommandJ2 = -throttleRightY * Math.max(J2_MAX_ANGLE_DEG, -J2_MIN_ANGLE_DEG);
				m_positionCommandJ3 = -throttleLeftY  * Math.max(J2_MAX_ANGLE_DEG, -J3_MIN_ANGLE_DEG);
				m_positionCommandJ4 = -throttleLeftX  * Math.max(J2_MAX_ANGLE_DEG, -J4_MIN_ANGLE_DEG);
				
				m_positionCommandJ3 = limitJ3(m_positionCommandJ2, m_positionCommandJ3);

				SmartDashboard.putNumber("J1 Stick Command", m_positionCommandJ1);
				SmartDashboard.putNumber("J2 Stick Command", m_positionCommandJ2);
				SmartDashboard.putNumber("J3 Stick Command", m_positionCommandJ3);
				SmartDashboard.putNumber("J4 Stick Command", m_positionCommandJ4);

				m_j1Motor.setPIDPositionDeg(m_positionCommandJ1);
				m_j2Motor.setPIDPositionDeg(m_positionCommandJ2);
				m_j3Motor.setPIDPositionDeg(m_positionCommandJ3);
				m_j4Motor.setPIDPositionDeg(m_positionCommandJ4);
			}
			
			// Incremental joint angle position control
//			else if (m_robotArmControlMode == CANTalonEncoderPID.ControlMode.POSITION_INCREMENTAL) {
//				if (Math.abs(throttleRightX) > JOYSTICK_DEADBAND_THROTTLE_POSITION) {
//					m_positionCommandJ1 = -throttleRightX * J1_MAX_SPEED_DEG_PER_SEC * 0.01 + m_positionCommandJ1;
//				}
//				if (Math.abs(throttleRightY) > JOYSTICK_DEADBAND_THROTTLE_POSITION) {
//					m_positionCommandJ2 = -throttleRightY * J2_MAX_SPEED_DEG_PER_SEC * 0.01 + m_positionCommandJ2; 
//				}
//				if (Math.abs(throttleLeftY) > JOYSTICK_DEADBAND_THROTTLE_POSITION) {
//					m_positionCommandJ3 = -throttleLeftY  * J3_MAX_SPEED_DEG_PER_SEC * 0.01 + m_positionCommandJ3;
//				}
//				if (Math.abs(throttleLeftX) > JOYSTICK_DEADBAND_THROTTLE_POSITION) {
//					m_positionCommandJ4 = -throttleLeftX  * J4_MAX_SPEED_DEG_PER_SEC * 0.01 + m_positionCommandJ4;
//				}
//
//				SmartDashboard.putNumber("J1 Stick Command", m_positionCommandJ1);
//				SmartDashboard.putNumber("J2 Stick Command", m_positionCommandJ2);
//				SmartDashboard.putNumber("J3 Stick Command", m_positionCommandJ3);
//				SmartDashboard.putNumber("J4 Stick Command", m_positionCommandJ4);
//
//				setPIDPosition(m_positionCommandJ1, m_positionCommandJ2, m_positionCommandJ3, m_positionCommandJ4);
//			}

			// Incremental robot cartesian position control
//			else if (m_robotArmControlMode == CANTalonEncoderPID.ControlMode.POSITION_INCREMENTAL) {
//				
//				// First calculate the current XYZ coordinate
//				double[] xyzToolDeg = motionProfileForOutput.calcForwardKinematicsDeg(new double[] {m_positionCommandJ1, m_positionCommandJ2, m_positionCommandJ3, m_positionCommandJ4});
//
//				// Add delta stick command to XYZ coordinate
//				if (Math.abs(throttleLeftY) > JOYSTICK_DEADBAND_THROTTLE_POSITION) {
//					xyzToolDeg[0] = -throttleLeftY * JOG_SPEED_INCHES_PER_SEC * 0.05 + xyzToolDeg[0];
//				}
//				if (Math.abs(throttleLeftX) > JOYSTICK_DEADBAND_THROTTLE_POSITION) {
//					xyzToolDeg[1] = -throttleLeftX * JOG_SPEED_INCHES_PER_SEC * 0.05 + xyzToolDeg[1]; 
//				}
//				if (Math.abs(throttleRightY) > JOYSTICK_DEADBAND_THROTTLE_POSITION) {
//					xyzToolDeg[2] = -throttleRightY  * JOG_SPEED_INCHES_PER_SEC * 0.05 + xyzToolDeg[2];
//				}
//				if (Math.abs(throttleRightX) > JOYSTICK_DEADBAND_THROTTLE_POSITION) {
//					xyzToolDeg[3] = -throttleRightX  * J4_MAX_SPEED_DEG_PER_SEC * 0.05 + xyzToolDeg[3];
//				}
//
//				// Calculate joint angles from adjusted XYZ coordinate
//				double[] jointAngles = motionProfileForOutput.calcInverseKinematicsDeg(xyzToolDeg);
//				m_positionCommandJ1 = jointAngles[0];
//				m_positionCommandJ2 = jointAngles[1];
//				m_positionCommandJ3 = jointAngles[2];
//				m_positionCommandJ4 = jointAngles[3];
//				
//				SmartDashboard.putNumber("J1 Stick Command", m_positionCommandJ1);
//				SmartDashboard.putNumber("J2 Stick Command", m_positionCommandJ2);
//				SmartDashboard.putNumber("J3 Stick Command", m_positionCommandJ3);
//				SmartDashboard.putNumber("J4 Stick Command", m_positionCommandJ4);
//
//				setPIDPosition(m_positionCommandJ1, m_positionCommandJ2, m_positionCommandJ3, m_positionCommandJ4);
//			}

			// Incremental robot tool coordinate position control
			else if (m_robotArmControlMode == CANTalonEncoderPID.ControlMode.POSITION_INCREMENTAL) {
				
				// First calculate the current XYZ coordinate
				double[] xyzToolDeg = motionProfileForOutput.calcForwardKinematicsDeg(new double[] {m_positionCommandJ1, m_positionCommandJ2, m_positionCommandJ3, m_positionCommandJ4});

				// Get stick command in tool coordinates
				double deltaToolX = 0;
				double deltaToolY = 0;
				if (Math.abs(throttleLeftY) > JOYSTICK_DEADBAND_THROTTLE_POSITION) {
					deltaToolX = -throttleLeftY * JOG_SPEED_INCHES_PER_SEC * 0.06;
				}
				if (Math.abs(throttleLeftX) > JOYSTICK_DEADBAND_THROTTLE_POSITION) {
					deltaToolY = -throttleLeftX * JOG_SPEED_INCHES_PER_SEC * 0.06; 
				}
				if (Math.abs(throttleRightY) > JOYSTICK_DEADBAND_THROTTLE_POSITION) {
					xyzToolDeg[2] = -throttleRightY  * JOG_SPEED_INCHES_PER_SEC * 0.06 + xyzToolDeg[2];
				}
				if (Math.abs(throttleRightX) > JOYSTICK_DEADBAND_THROTTLE_POSITION) {
					xyzToolDeg[3] = -throttleRightX  * J4_MAX_SPEED_DEG_PER_SEC * 0.06 + xyzToolDeg[3];
				}

				// Convert delta tool coordinate to world coordinate (use new gamma value?)
				double cosGamma = Math.cos(Math.toRadians(xyzToolDeg[3]));
				double sinGamma = Math.sin(Math.toRadians(xyzToolDeg[3]));
				xyzToolDeg[0] = deltaToolX * cosGamma - deltaToolY * sinGamma + xyzToolDeg[0];
				xyzToolDeg[1] = deltaToolX * sinGamma + deltaToolY * cosGamma + xyzToolDeg[1];
				
				// Calculate joint angles from adjusted XYZ coordinate
				double[] jointAngles = motionProfileForOutput.calcInverseKinematicsDeg(xyzToolDeg);
				
				// Add in the J1 position jog
				if (Math.abs(triggerLeft) > JOYSTICK_DEADBAND_THROTTLE_POSITION) {
					jointAngles[0] += triggerLeft * J1_MAX_SPEED_DEG_PER_SEC * 0.04;
				}
				if (Math.abs(triggerRight) > JOYSTICK_DEADBAND_THROTTLE_POSITION) {
					jointAngles[0] += -triggerRight * J1_MAX_SPEED_DEG_PER_SEC * 0.04;
				}

				m_positionCommandJ1 = jointAngles[0];
				m_positionCommandJ2 = jointAngles[1];
				m_positionCommandJ3 = jointAngles[2];
				m_positionCommandJ4 = jointAngles[3];
				
				SmartDashboard.putNumber("J1 Stick Command", m_positionCommandJ1);
				SmartDashboard.putNumber("J2 Stick Command", m_positionCommandJ2);
				SmartDashboard.putNumber("J3 Stick Command", m_positionCommandJ3);
				SmartDashboard.putNumber("J4 Stick Command", m_positionCommandJ4);

				setPIDPosition(m_positionCommandJ1, m_positionCommandJ2, m_positionCommandJ3, m_positionCommandJ4);
			}

			else if (m_robotArmControlMode == CANTalonEncoderPID.ControlMode.PERCENT_VBUS) {
				SmartDashboard.putNumber("J1 Stick Command", throttleRightX);
				SmartDashboard.putNumber("J2 Stick Command", throttleRightY);
				SmartDashboard.putNumber("J3 Stick Command", throttleLeftY);
				SmartDashboard.putNumber("J4 Stick Command", throttleLeftX);

				m_j1Motor.set(throttleRightX);
				m_j2Motor.set(throttleRightY);
				m_j3Motor.set(throttleLeftY);
				m_j4Motor.set(throttleLeftX);
			}
		}
	}

	// Motion profiling
	public void startRobotArmCommandList(RobotArmCommandList commandList) {
		this.disableControlLoop();
		m_controllerLoopCommandList = commandList;
		m_currentControllerLoopCommandIndex = 0;
		if (m_controllerLoopCommandList != null && m_controllerLoopCommandList.size() > 0) {
			m_currentControllerLoopCommand = m_controllerLoopCommandList.get(m_currentControllerLoopCommandIndex);
			m_currentControllerLoopCommand.reset();
			this.enableControlLoop();
		}
	}

	protected void controlLoopUpdate() {
		boolean enabled;
        synchronized (this) {
            enabled = m_controlLoopEnabled; // take snapshot of this value
        }
		
        if (enabled) {
			boolean isFinished = m_currentControllerLoopCommand.run();
			if (isFinished) {
				if (m_currentControllerLoopCommandIndex < m_controllerLoopCommandList.size() - 1) {
					m_currentControllerLoopCommandIndex++;
					m_currentControllerLoopCommand = m_controllerLoopCommandList.get(m_currentControllerLoopCommandIndex);
					m_currentControllerLoopCommand.reset();
				}
				else {
					disableControlLoop();
					return;
				}
			}
		}
        
        long currentTime = System.nanoTime();
        setControlLoopDeltaTime(currentTime - getControlLoopStartTime());
        setControlLoopStartTime(currentTime);
	}
	
	public synchronized void setJointAngles(double[] jointAngles) {
		setPIDPosition(jointAngles[0], jointAngles[1], jointAngles[2], jointAngles[3]);
	}
	
	public synchronized void setPIDPosition(double j1PositionDeg, double j2PositionDeg, double j3PositionDeg, double j4PositionDeg) {
		m_limitJ3Deg = limitJ3(j2PositionDeg, j3PositionDeg);
		
		m_positionCommandJ1 = j1PositionDeg;
		m_positionCommandJ2 = j2PositionDeg;
		m_positionCommandJ3 = m_limitJ3Deg;
		m_positionCommandJ4 = j4PositionDeg;
		
		m_j1Motor.setPIDPositionDeg(m_positionCommandJ1);
		m_j2Motor.setPIDPositionDeg(m_positionCommandJ2);
		m_j3Motor.setPIDPositionDeg(m_positionCommandJ3);
		m_j4Motor.setPIDPositionDeg(m_positionCommandJ4);	
		
		// Setting the PID coefficients each pass causes a 30-40 ms delay. This killed the 10 ms control loop.
		// If you must schedule the gains, much better to switch between the 2 profiles stored in the Talon.
//		m_j2Motor.setPIDPositiveNegativePositionDeg(m_positionCommandJ2, j2PositionPidParamsNegative, j2PositionPidParamsPositive);
//		m_j3Motor.setPIDPositiveNegativePositionDeg(m_positionCommandJ3, j3PositionPidParamsNegative, j3PositionPidParamsPositive);
	}
	
	public synchronized double[] getJointAngles() {
		return new double[] {
				m_j1Motor.getPositionDeg(),
				m_j2Motor.getPositionDeg(),
				m_j3Motor.getPositionDeg(),
				m_j4Motor.getPositionDeg()				
		};
	}
	
	private double limitJ3(double j2AngleDeg, double j3AngleDeg) {
		isJ3Limited = false;
		
		// Interference checks
		if ((j2AngleDeg + j3AngleDeg) > J3_INTERFERENCE_J2_PLUS_J3_MAX_ANGLE_DEG) {
			j3AngleDeg = J3_INTERFERENCE_J2_PLUS_J3_MAX_ANGLE_DEG - j2AngleDeg;
			isJ3Limited = true;
		}
		else if ((j2AngleDeg + j3AngleDeg) < J3_INTERFERENCE_J2_PLUS_J3_MIN_ANGLE_DEG) {
			j3AngleDeg = J3_INTERFERENCE_J2_PLUS_J3_MIN_ANGLE_DEG - j2AngleDeg;
			isJ3Limited = true;
		}

		// Height limit check
//		if (j2AngleDeg <= J3_HEIGHT_LIMIT_J2_MIN_ANGLE_DEG) {
//			if (j3AngleDeg > J3_HEIGHT_LIMIT_FACTOR_J2_LESS_THAN_MIN * j2AngleDeg) {
//				j3AngleDeg = J3_HEIGHT_LIMIT_FACTOR_J2_LESS_THAN_MIN * j2AngleDeg;
//				isJ3Limited = true;
//			}
//		}
//		if (j2AngleDeg >= J3_HEIGHT_LIMIT_J2_MIN_ANGLE_DEG && j2AngleDeg <= J3_HEIGHT_LIMIT_J2_MAX_ANGLE_DEG) {
//			if (j3AngleDeg > J3_HEIGHT_LIMIT_FACTOR_J2_GREATER_THAN_MIN * j2AngleDeg) {
//				j3AngleDeg = J3_HEIGHT_LIMIT_FACTOR_J2_GREATER_THAN_MIN * j2AngleDeg;
//				isJ3Limited = true;
//			}			
//		}
		
		return j3AngleDeg;
	}

	public synchronized void enableControlLoop() {
		m_controlLoopEnabled = true;
	}

	public synchronized void setControlLoopStartTime(long time) {
		m_controlLoopStartTime = time;
	}

	public synchronized long getControlLoopStartTime() {
		return m_controlLoopStartTime;
	}

	public synchronized void setControlLoopDeltaTime(long time) {
		m_controlLoopDeltaTime = time;
	}

	public synchronized long getControlLoopDeltaTime() {
		return m_controlLoopDeltaTime;
	}

	/**
	 * Stop running the PIDController, this sets the output to zero before stopping.
	 */
	public synchronized void disableControlLoop() {
		m_controlLoopEnabled = false;
	}

	/**
	 * Return true if PIDController is enabled.
	 */
	public synchronized boolean isControlLoopEnabled() {
		return m_controlLoopEnabled;
	}

	public void updateStatus() {
//		SmartDashboard.putNumber("J1 Throttle Calc", 		m_j1Motor.getOutputVoltage() / m_j1Motor.getBusVoltage());
//		SmartDashboard.putNumber("J1 Profile Command (deg)", m_positionCommandJ1);
//		SmartDashboard.putNumber("J1 Profile Actual (deg)", m_j1Motor.getPositionDeg());

//		SmartDashboard.putNumber("J1 Position (raw)", 		m_j1Motor.getPosition());
		SmartDashboard.putNumber("J1 Position (deg)", 		m_j1Motor.getPositionDeg());
//		SmartDashboard.putNumber("J1 Velocity (deg-sec)", 	m_j1Motor.getVelocityDegPerSec());
//		SmartDashboard.putNumber("J1 Current", 				m_j1Motor.getOutputCurrent());

		SmartDashboard.putNumber("J2 Throttle Calc", 		m_j2Motor.getOutputVoltage() / m_j2Motor.getBusVoltage());
//		SmartDashboard.putNumber("J2 Output Voltage", 		m_j2Motor.getOutputVoltage());
//		SmartDashboard.putNumber("J2 Position (raw)", 		m_j2Motor.getPosition());
		SmartDashboard.putNumber("J2 Position (deg)", 		m_j2Motor.getPositionDeg());
//		SmartDashboard.putNumber("J2 Velocity (deg-sec)", 	m_j2Motor.getVelocityDegPerSec());
//		SmartDashboard.putNumber("J2 Current", 				m_j2Motor.getOutputCurrent());		

//		SmartDashboard.putNumber("J3 Limit (deg)", 			m_limitJ3Deg);
//		SmartDashboard.putBoolean("J3 isLimited", 			isJ3Limited);
		SmartDashboard.putNumber("J3 Throttle Calc", 		m_j3Motor.getOutputVoltage() / m_j3Motor.getBusVoltage());
//		SmartDashboard.putNumber("J3 Position (raw)", 		m_j3Motor.getPosition());
		SmartDashboard.putNumber("J3 Position (deg)", 		m_j3Motor.getPositionDeg());
//		SmartDashboard.putNumber("J3 Velocity (deg-sec)", 	m_j3Motor.getVelocityDegPerSec());
//		SmartDashboard.putNumber("J3 Current", 				m_j3Motor.getOutputCurrent());

		SmartDashboard.putNumber("J4 Throttle Calc", 		m_j4Motor.getOutputVoltage() / m_j4Motor.getBusVoltage());
//		SmartDashboard.putNumber("J4 Position (raw)", 		m_j4Motor.getPosition());
		SmartDashboard.putNumber("J4 Position (deg)", 		m_j4Motor.getPositionDeg());
//		SmartDashboard.putNumber("J4 Velocity (deg-sec)", 	m_j4Motor.getVelocityDegPerSec());

//		SmartDashboard.putBoolean("IR Tote Grabber Switch", getToteGrabberSwitch());
//		SmartDashboard.putString("Tote Grabber Position", 	getToteGrabberPosition().toString());

		SmartDashboard.putBoolean("RobotArm Controller", 	m_controlLoopEnabled);
//		SmartDashboard.putNumber("RobotArm Control loop time (ms)", 	getControlLoopDeltaTime() / 1000000);
		
		double[] jointAnglesDeg = new double[] {m_j1Motor.getPositionDeg(), m_j2Motor.getPositionDeg(), m_j3Motor.getPositionDeg(), m_j4Motor.getPositionDeg()};
		double[] xyzToolDeg = motionProfileForOutput.calcForwardKinematicsDeg(jointAnglesDeg);
		
		SmartDashboard.putNumber("X Robot (in)", xyzToolDeg[0]);
		SmartDashboard.putNumber("Y Robot (in)", xyzToolDeg[1]);
		SmartDashboard.putNumber("Z Robot (in)", xyzToolDeg[2]);
		SmartDashboard.putNumber("Tool angle (deg)", xyzToolDeg[3]);
	}
}

