package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.OI;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.commands.RobotArmWithJoystick;
import edu.rhhs.frc.utility.CANTalonAnalogPID;
import edu.rhhs.frc.utility.CANTalonEncoderPID;
import edu.rhhs.frc.utility.PIDParams;
import edu.rhhs.frc.utility.RobotUtility;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RobotArm extends PIDSubsystem {
	
	public static enum ToteGrabberPosition {OPEN, CLOSED};
    
	private static final double OUTER_LOOP_UPDATE_RATE_SEC = 0.010;
	
	private static final double J1_ENCODER_OFFSET_DEG = 0.0;
	private static final double J2_ENCODER_OFFSET_DEG = 0.0;
	private static final double J3_ENCODER_OFFSET_DEG = 90.0;
	private static final double J4_ENCODER_OFFSET_DEG = 0.0;
	
	private static final double J1_ENCODER_INIT_DEG = 0.0;
	private static final double J2_ENCODER_INIT_DEG = 0.0;
	private static final double J3_ENCODER_INIT_DEG = J3_ENCODER_OFFSET_DEG;
	private static final double J4_ENCODER_INIT_DEG = 0.0;

	private static final int 	J4_ANALOG_ZERO_PRACTICE = 512;

	private static final double J1_SENSOR_GEAR_RATIO = 84.0/18.0;
	private static final double J2_SENSOR_GEAR_RATIO = 84.0/18.0;
	private static final double J3_SENSOR_GEAR_RATIO = 84.0/18.0;
	private static final double J4_SENSOR_GEAR_RATIO = 60.0/30.0;

	private static final double J1_MAX_ANGLE_DEG = 170.0;
	private static final double J1_MIN_ANGLE_DEG = -170.0;
	private static final double J2_MAX_ANGLE_DEG = 70.0;
	private static final double J2_MIN_ANGLE_DEG = -20.0;
	private static final double J3_MAX_ANGLE_DEG = 20.0;
	private static final double J3_MIN_ANGLE_DEG = -20.0;
	private static final double J4_MAX_ANGLE_DEG = 60.0;
	private static final double J4_MIN_ANGLE_DEG = -60.0;

	private static final double J1_MAX_SPEED_DEG_PER_SEC = 140.0;
	private static final double J2_MAX_SPEED_DEG_PER_SEC = 40.0;
	private static final double J3_MAX_SPEED_DEG_PER_SEC = 40.0;
	private static final double J4_MAX_SPEED_DEG_PER_SEC = 40.0;
	
	private CANTalonEncoderPID m_j1Motor;
	private CANTalonEncoderPID m_j2Motor;
	private CANTalonEncoderPID m_j3Motor;
	private CANTalonAnalogPID  m_j4Motor;
	
    private PIDParams j1PositionPidParams = new PIDParams(1.4, 0.0, 0.001, 0.0, 50, 0.0);
    private PIDParams j2PositionPidParams = new PIDParams(8.0, 0.04, 0.0, 0.0, 100, 0.0);
    private PIDParams j3PositionPidParams = new PIDParams(2.0, 0.0, 0.003, 0.167, 50, 0.0);
    private PIDParams j4PositionPidParams = new PIDParams(3.0, 0.005, 0.0, 0.0, 50, 0.0);

    private PIDParams j1VelocityPidParams = new PIDParams(0.5, 0.005, 0.0, 0.0, 0, 0.0);
    private PIDParams j2VelocityPidParams = new PIDParams(0.5, 0.02, 0.0, 0.0, 0, 0.0);
    private PIDParams j3VelocityPidParams = new PIDParams(0.5, 0.008, 0.0, 0.0, 0, 0.0);
    private PIDParams j4VelocityPidParams = new PIDParams(1.0, 0.027, 0.0, 0.0, 0, 0.0);

	private DigitalInput m_toteGrabberSwitch;
	
	private DoubleSolenoid m_toteGrabberSolenoid;
	
    private RobotUtility.ControlMode m_robotArmControlMode = RobotUtility.ControlMode.VELOCITY;

    public RobotArm() {
    	super(0.0, 0.0, 0.0, OUTER_LOOP_UPDATE_RATE_SEC);
    	try {
			m_j1Motor = new CANTalonEncoderPID(RobotMap.ROBOT_ARM_J1_CAN_ID, J1_SENSOR_GEAR_RATIO, J1_ENCODER_OFFSET_DEG, J1_ENCODER_INIT_DEG, J1_MIN_ANGLE_DEG, J1_MAX_ANGLE_DEG);
			m_j2Motor = new CANTalonEncoderPID(RobotMap.ROBOT_ARM_J2_CAN_ID, J2_SENSOR_GEAR_RATIO, J2_ENCODER_OFFSET_DEG, J2_ENCODER_INIT_DEG, J2_MIN_ANGLE_DEG, J2_MAX_ANGLE_DEG);
			m_j3Motor = new CANTalonEncoderPID(RobotMap.ROBOT_ARM_J3_CAN_ID, J3_SENSOR_GEAR_RATIO, J3_ENCODER_OFFSET_DEG, J3_ENCODER_INIT_DEG, J3_MIN_ANGLE_DEG, J3_MAX_ANGLE_DEG);
			m_j4Motor = new CANTalonAnalogPID (RobotMap.ROBOT_ARM_J4_CAN_ID, J4_SENSOR_GEAR_RATIO, J4_ENCODER_OFFSET_DEG, J4_ENCODER_INIT_DEG, J4_MIN_ANGLE_DEG, J4_MAX_ANGLE_DEG, J4_ANALOG_ZERO_PRACTICE);
			
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

		if (m_robotArmControlMode == RobotUtility.ControlMode.VELOCITY) {
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
			double positionCommandJ1 = -throttleRightX * Math.min(J1_MAX_ANGLE_DEG, Math.abs(J1_MIN_ANGLE_DEG));
			double positionCommandJ2 = -throttleRightY * Math.min(J2_MAX_ANGLE_DEG, Math.abs(J2_MIN_ANGLE_DEG));
			double positionCommandJ3 = -throttleLeftY  * Math.min(J3_MAX_ANGLE_DEG, Math.abs(J3_MIN_ANGLE_DEG));
			double positionCommandJ4 = -throttleLeftX  * Math.min(J4_MAX_ANGLE_DEG, Math.abs(J4_MIN_ANGLE_DEG));

			SmartDashboard.putNumber("J1 Stick Command", positionCommandJ1);
			SmartDashboard.putNumber("J2 Stick Command", positionCommandJ2);
			SmartDashboard.putNumber("J3 Stick Command", positionCommandJ3);
			SmartDashboard.putNumber("J4 Stick Command", positionCommandJ4);

			m_j1Motor.setPIDPositionDeg(positionCommandJ1);
			m_j2Motor.setPIDPositionDeg(positionCommandJ2);
			m_j3Motor.setPIDPositionDeg(positionCommandJ3);
			m_j4Motor.setPIDPositionDeg(positionCommandJ4);
		}
		else if (m_robotArmControlMode == RobotUtility.ControlMode.VBUS) {
			SmartDashboard.putNumber("J1 Stick Command", -throttleRightX);
			SmartDashboard.putNumber("J2 Stick Command", -throttleRightY);
			SmartDashboard.putNumber("J3 Stick Command", -throttleLeftY);
			SmartDashboard.putNumber("J4 Stick Command", -throttleLeftX);
			
			m_j1Motor.set(-throttleRightX);
			m_j2Motor.set(-throttleRightY);
			m_j3Motor.set(-throttleLeftY);
			m_j4Motor.set(-throttleLeftX);
		}	
	}
	
	public void updateStatus() {
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
	}

	@Override
	protected double returnPIDInput() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void usePIDOutput(double output) {
		// TODO Auto-generated method stub
		
	}
}

