package edu.rhhs.frc.subsystems;

import java.util.TimerTask;

import edu.rhhs.frc.OI;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.commands.RobotArmWithJoystick;
import edu.rhhs.frc.utility.CANTalonAnalogPID;
import edu.rhhs.frc.utility.CANTalonEncoderPID;
import edu.rhhs.frc.utility.PIDParams;
import edu.rhhs.frc.utility.RobotUtility;
import edu.rhhs.frc.utility.motionprofile.ProfileOutput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RobotArm extends Subsystem {

	public static enum ToteGrabberPosition {OPEN, CLOSED};

	private static final double OUTER_LOOP_UPDATE_RATE_SEC = 0.010;

	// Robot arm joint offset angles to help deal with feed forward gain (Kf * target)
	// This offset is applied inside the Talon only, it gets added on for input, subtracted off on output
	private static final double J1_ENCODER_OFFSET_DEG = 0.0;
	private static final double J2_ENCODER_OFFSET_DEG = 0.0;
	private static final double J3_ENCODER_OFFSET_DEG = 90.0;
	private static final double J4_ENCODER_OFFSET_DEG = 0.0;

	// Robot arm joint angles when system is powered on (or new code is downloaded)
	private static final double J1_ENCODER_INIT_DEG = 0.0;
	private static final double J2_ENCODER_INIT_DEG = 0.0;
	private static final double J3_ENCODER_INIT_DEG = 0.0;
	private static final double J4_ENCODER_INIT_DEG = 0.0;

	// J4 raw analog value when J4 is set to 0 deg 
	private static final int 	J4_ANALOG_ZERO_PRACTICE = 512;

	// Robot arm joint to sensor gear ratios
	private static final double J1_SENSOR_GEAR_RATIO = 84.0/18.0;
	private static final double J2_SENSOR_GEAR_RATIO = 84.0/18.0;
	private static final double J3_SENSOR_GEAR_RATIO = 84.0/18.0;
	private static final double J4_SENSOR_GEAR_RATIO = 60.0/30.0;

	private static final double J1_MAX_ANGLE_DEG = 170.0;
	private static final double J1_MIN_ANGLE_DEG = -170.0;
	private static final double J2_MAX_ANGLE_DEG = 90.0;
	private static final double J2_MIN_ANGLE_DEG = -30.0;
	private static final double J3_MAX_ANGLE_DEG = 20.0;
	private static final double J3_MIN_ANGLE_DEG = -60.0;
	private static final double J4_MAX_ANGLE_DEG = 70.0;
	private static final double J4_MIN_ANGLE_DEG = -70.0;

	private static final double J1_MAX_SPEED_DEG_PER_SEC = 180.0;
	private static final double J2_MAX_SPEED_DEG_PER_SEC = 60.0;
	private static final double J3_MAX_SPEED_DEG_PER_SEC = 60.0;
	private static final double J4_MAX_SPEED_DEG_PER_SEC = 200.0;

	private CANTalonEncoderPID m_j1Motor;
	private CANTalonEncoderPID m_j2Motor;
	private CANTalonEncoderPID m_j3Motor;
	private CANTalonAnalogPID  m_j4Motor;

	private PIDParams j1PositionPidParams = new PIDParams(1.4, 0.0, 0.001, 0.0, 50, 0.0);
	private PIDParams j2PositionPidParams = new PIDParams(3.5, 0.0, 0.01, 0.167, 50, 0.0);
	private PIDParams j3PositionPidParams = new PIDParams(4.5, 0.005, 0.01, 0.167, 100, 0.0);
	private PIDParams j4PositionPidParams = new PIDParams(3.0, 0.005, 0.0, 0.0, 50, 0.0);

	private PIDParams j1VelocityPidParams = new PIDParams(0.5, 0.005, 0.0, 0.0, 0, 0.0);
	private PIDParams j2VelocityPidParams = new PIDParams(0.5, 0.02, 0.0, 0.0, 0, 0.0);
	private PIDParams j3VelocityPidParams = new PIDParams(0.5, 0.008, 0.0, 0.0, 0, 0.0);
	private PIDParams j4VelocityPidParams = new PIDParams(1.0, 0.027, 0.0, 0.0, 0, 0.0);

	private DigitalInput m_toteGrabberSwitch;

	private DoubleSolenoid m_toteGrabberSolenoid;

	private RobotUtility.ControlMode m_robotArmControlMode = RobotUtility.ControlMode.VELOCITY_POSITION_HOLD;

	private ProfileOutput m_currentMotionProfile;
	private int m_currentProfileIndex;
	private long m_controllerLoopTime;

	private double positionCommandJ1 = J1_ENCODER_INIT_DEG;
	private double positionCommandJ2 = J2_ENCODER_INIT_DEG;
	private double positionCommandJ3 = J3_ENCODER_INIT_DEG;
	private double positionCommandJ4 = J4_ENCODER_INIT_DEG;

	private java.util.Timer m_controlLoop;
	private boolean m_enabled = false;

	private class PIDTask extends TimerTask {

		private RobotArm m_arm;

		public PIDTask(RobotArm arm) {
			if (arm == null) {
				throw new NullPointerException("Given RobotArm was null");
			}
			m_arm = arm;
		}

		@Override
		public void run() {
			m_arm.usePIDOutput(0.1);
		}
	}

	public RobotArm() {
		//    	super(0.0, 0.0, 0.0, OUTER_LOOP_UPDATE_RATE_SEC);
		try {
			m_j1Motor = new CANTalonEncoderPID(RobotMap.ROBOT_ARM_J1_CAN_ID, J1_SENSOR_GEAR_RATIO, J1_ENCODER_OFFSET_DEG, J1_ENCODER_INIT_DEG, J1_MIN_ANGLE_DEG, J1_MAX_ANGLE_DEG);
			m_j2Motor = new CANTalonEncoderPID(RobotMap.ROBOT_ARM_J2_CAN_ID, J2_SENSOR_GEAR_RATIO, J2_ENCODER_OFFSET_DEG, J2_ENCODER_INIT_DEG, J2_MIN_ANGLE_DEG, J2_MAX_ANGLE_DEG);
			m_j3Motor = new CANTalonEncoderPID(RobotMap.ROBOT_ARM_J3_CAN_ID, J3_SENSOR_GEAR_RATIO, J3_ENCODER_OFFSET_DEG, J3_ENCODER_INIT_DEG, J3_MIN_ANGLE_DEG, J3_MAX_ANGLE_DEG);
			m_j4Motor = new CANTalonAnalogPID (RobotMap.ROBOT_ARM_J4_CAN_ID, J4_SENSOR_GEAR_RATIO, J4_ENCODER_OFFSET_DEG, J4_ENCODER_INIT_DEG, J4_MIN_ANGLE_DEG, J4_MAX_ANGLE_DEG, J4_ANALOG_ZERO_PRACTICE);

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

			m_j1Motor.setPIDParams(j1PositionPidParams, RobotUtility.POSITION_PROFILE);
			m_j2Motor.setPIDParams(j2PositionPidParams, RobotUtility.POSITION_PROFILE);
			m_j3Motor.setPIDParams(j3PositionPidParams, RobotUtility.POSITION_PROFILE);
			m_j4Motor.setPIDParams(j4PositionPidParams, RobotUtility.POSITION_PROFILE);

			m_j1Motor.setPIDParams(j1VelocityPidParams, RobotUtility.VELOCITY_PROFILE);
			m_j2Motor.setPIDParams(j2VelocityPidParams, RobotUtility.VELOCITY_PROFILE);
			m_j3Motor.setPIDParams(j3VelocityPidParams, RobotUtility.VELOCITY_PROFILE);
			m_j4Motor.setPIDParams(j4VelocityPidParams, RobotUtility.VELOCITY_PROFILE);

			m_j1Motor.inititializeSensorPosition();
			m_j2Motor.inititializeSensorPosition();
			m_j3Motor.inititializeSensorPosition();

			m_j1Motor.setControlMode(RobotUtility.ControlMode.POSITION);
			m_j2Motor.setControlMode(RobotUtility.ControlMode.POSITION);
			m_j3Motor.setControlMode(RobotUtility.ControlMode.POSITION);
			m_j4Motor.setControlMode(RobotUtility.ControlMode.POSITION);

			m_j1Motor.setInitPosition();
			m_j2Motor.setInitPosition();
			m_j3Motor.setInitPosition();
			m_j4Motor.setInitPosition();	

			m_toteGrabberSwitch = new DigitalInput(RobotMap.TOTE_GRABBER_SWITCH);	
			m_toteGrabberSolenoid = new DoubleSolenoid(RobotMap.TOTE_GRABBER_EXTEND_PNEUMATIC_MODULE_ID, RobotMap.TOTE_GRABBER_RETRACT_PNEUMATIC_MODULE_ID);
			setToteGrabberPosition(ToteGrabberPosition.OPEN);		

			m_controlLoop = new java.util.Timer();
			m_controlLoop.schedule(new PIDTask(this), 0L, (long) (OUTER_LOOP_UPDATE_RATE_SEC * 1000));
		}
		catch (Exception e) {
			System.out.println("Unknown error initializing robot arm.  Message = " + e.getMessage());
		}
	}

	public void initDefaultCommand() {
		setDefaultCommand(new RobotArmWithJoystick());
	}

	public void setControlMode(RobotUtility.ControlMode mode) {
		m_robotArmControlMode = mode;
		m_j1Motor.setControlMode(mode);
		m_j2Motor.setControlMode(mode);
		m_j3Motor.setControlMode(mode);
		m_j4Motor.setControlMode(mode);
	}

	public boolean getToteGrabberSwitch() {
		return !m_toteGrabberSwitch.get();
	}

	public void setToteGrabberPosition(ToteGrabberPosition position) {
		if (position == ToteGrabberPosition.OPEN) {
			m_toteGrabberSolenoid.set(DoubleSolenoid.Value.kForward);
		}
		else {
			m_toteGrabberSolenoid.set(DoubleSolenoid.Value.kReverse);
		}
	}

	public ToteGrabberPosition getToteGrabberPosition() {
		if (m_toteGrabberSolenoid.get() == DoubleSolenoid.Value.kForward) {
			return ToteGrabberPosition.OPEN;
		}
		return ToteGrabberPosition.CLOSED;
	}

	public void controlWithJoystick() {
		double throttleRightX = OI.getInstance().getXBoxController2().getRightXAxis();
		double throttleRightY = OI.getInstance().getXBoxController2().getRightYAxis();
		double throttleLeftX = OI.getInstance().getXBoxController2().getLeftXAxis();
		double throttleLeftY = OI.getInstance().getXBoxController2().getLeftYAxis();

		SmartDashboard.putNumber("Right X Throttle", throttleRightX);
		SmartDashboard.putNumber("Right Y Throttle", throttleRightY);
		SmartDashboard.putNumber("Left X Throttle" , throttleLeftX);
		SmartDashboard.putNumber("Left Y Throttle" , throttleLeftY);

		if (m_robotArmControlMode == RobotUtility.ControlMode.VELOCITY_POSITION_HOLD) {
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
		else if (m_robotArmControlMode == RobotUtility.ControlMode.POSITION) {
			//			double positionCommandJ1 = -throttleRightX * Math.max(J1_MAX_ANGLE_DEG/2, Math.abs(J1_MIN_ANGLE_DEG/2));
			//			double positionCommandJ2 = -throttleRightY * Math.max(J2_MAX_ANGLE_DEG, Math.abs(J2_MIN_ANGLE_DEG));
			//			double positionCommandJ3 = -throttleLeftY  * Math.max(J3_MAX_ANGLE_DEG, Math.abs(J3_MIN_ANGLE_DEG));
			//			double positionCommandJ4 = -throttleLeftX  * Math.max(J4_MAX_ANGLE_DEG, Math.abs(J4_MIN_ANGLE_DEG));

			if (Math.abs(throttleRightX) > 0.2) {
				positionCommandJ1 = -throttleRightX * 5.0 + m_j1Motor.getPositionDeg();
			}
			if (Math.abs(throttleRightY) > 0.2) {
				positionCommandJ2 = -throttleRightY * 5.0 + m_j2Motor.getPositionDeg();
			}
			if (Math.abs(throttleLeftY) > 0.2) {
				positionCommandJ3 = -throttleLeftY  * 5.0 + m_j3Motor.getPositionDeg();
			}
			if (Math.abs(throttleLeftX) > 0.2) {
				positionCommandJ4 = -throttleLeftX  * 5.0 + m_j4Motor.getPositionDeg();
			}

			SmartDashboard.putNumber("J1 Stick Command", positionCommandJ1);
			SmartDashboard.putNumber("J2 Stick Command", positionCommandJ2);
			SmartDashboard.putNumber("J3 Stick Command", positionCommandJ3);
			SmartDashboard.putNumber("J4 Stick Command", positionCommandJ4);

			m_j1Motor.setPIDPositionDeg(positionCommandJ1);
			m_j2Motor.setPIDPositionDeg(positionCommandJ2);
			m_j3Motor.setPIDPositionDeg(positionCommandJ3);
			m_j4Motor.setPIDPositionDeg(positionCommandJ4);
		}
		else if (m_robotArmControlMode == RobotUtility.ControlMode.PERCENT_VBUS) {
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

	// Motion profiling
	public void startMotionProfile(ProfileOutput profile) {
		m_currentProfileIndex = 0;
		m_controllerLoopTime = 0;
		m_currentMotionProfile = profile;
		this.enable();
	}

	//	@Override
	//	protected double returnPIDInput() {
	//		return 0;
	//	}
	//
	//	@Override
	protected void usePIDOutput(double output) {
        boolean enabled;

        synchronized (this) {
            enabled = m_enabled; // take snapshot of this value
        }
		if (enabled) {
			if (m_currentProfileIndex >= m_currentMotionProfile.jointPos.length) {
				if (m_currentProfileIndex - m_currentMotionProfile.jointPos.length + 1 < 10) {
					m_currentProfileIndex = m_currentMotionProfile.jointPos.length - 1;
				}
				else {
					this.disable();
					return;
				}
			}

			double[] jointAngles = m_currentMotionProfile.jointPos[m_currentProfileIndex];
			positionCommandJ1 = jointAngles[0];
			m_j1Motor.setPIDPositionDeg(jointAngles[0]);
			m_j2Motor.setPIDPositionDeg(jointAngles[1]);
			m_j3Motor.setPIDPositionDeg(jointAngles[2]);
			m_j4Motor.setPIDPositionDeg(jointAngles[3]);	

			m_controllerLoopTime++;

			m_currentProfileIndex += 10;
		}
	}

	public synchronized void enable() {
		m_enabled = true;
	}

	/**
	 * Stop running the PIDController, this sets the output to zero before stopping.
	 */
	public synchronized void disable() {
		m_enabled = false;
	}

	/**
	 * Return true if PIDController is enabled.
	 */
	public synchronized boolean isEnable() {
		return m_enabled;
	}

	public void updateStatus() {
		SmartDashboard.putNumber("J1 Profile Command (deg)", positionCommandJ1);
		SmartDashboard.putNumber("J1 Profile Actual (deg)", m_j1Motor.getPositionDeg());
		SmartDashboard.putNumber("controller time", m_controllerLoopTime);

		SmartDashboard.putNumber("J1 Position (raw)", 		m_j1Motor.getPosition());
		SmartDashboard.putNumber("J1 Position (deg)", 		m_j1Motor.getPositionDeg());
		SmartDashboard.putNumber("J1 Velocity (deg-sec)", 	m_j1Motor.getVelocityDegPerSec());

		SmartDashboard.putNumber("J2 Position (raw)", 		m_j2Motor.getPosition());
		SmartDashboard.putNumber("J2 Position (deg)", 		m_j2Motor.getPositionDeg());
		SmartDashboard.putNumber("J2 Velocity (deg-sec)", 	m_j2Motor.getVelocityDegPerSec());

		SmartDashboard.putNumber("J3 Position (raw)", 		m_j3Motor.getPosition());
		SmartDashboard.putNumber("J3 Position (deg)", 		m_j3Motor.getPositionDeg());
		SmartDashboard.putNumber("J3 Velocity (deg-sec)", 	m_j3Motor.getVelocityDegPerSec());

		SmartDashboard.putNumber("J4 Position (raw)", 		m_j4Motor.getPosition());
		SmartDashboard.putNumber("J4 Position (deg)", 		m_j4Motor.getPositionDeg());
		SmartDashboard.putNumber("J4 Velocity (deg-sec)", 	m_j4Motor.getVelocityDegPerSec());

		SmartDashboard.putBoolean("IR Tote Grabber Switch", getToteGrabberSwitch());
		SmartDashboard.putString("Tote Grabber Position", 	getToteGrabberPosition().toString());

		SmartDashboard.putBoolean("RobotArm Controller", 	m_enabled);
	}
}
