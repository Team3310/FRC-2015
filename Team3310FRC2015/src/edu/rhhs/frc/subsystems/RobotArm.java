package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.OI;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.commands.RobotArmWithJoystick;
import edu.rhhs.frc.utility.PIDParams;
import edu.rhhs.frc.utility.RobotUtility;
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
    
	private static final double J3_ENCODER_OFFSET_DEG = 90.0;
	
	private static final double J1_ENCODER_INIT_DEG = 0.0;
	private static final double J2_ENCODER_INIT_DEG = 0.0;
	private static final double J3_ENCODER_INIT_DEG = J3_ENCODER_OFFSET_DEG;
	private static final double J4_ANALOG_ZERO_PRACTICE = 512.0;

	private static final double J1_SENSOR_GEAR_RATIO = 84.0/18.0;
	private static final double J2_SENSOR_GEAR_RATIO = 84.0/18.0;
	private static final double J3_SENSOR_GEAR_RATIO = 84.0/18.0;
	private static final double J4_SENSOR_GEAR_RATIO = 60.0/30.0;

	private static final double J1_MAX_ANGLE = 170.0;
	private static final double J1_MIN_ANGLE = -170.0;
	private static final double J2_MAX_ANGLE = 70.0;
	private static final double J2_MIN_ANGLE = -20.0;
	private static final double J3_MAX_ANGLE = 20.0;
	private static final double J3_MIN_ANGLE = -20.0;
	private static final double J4_MAX_ANGLE = 60.0;
	private static final double J4_MIN_ANGLE = -60.0;

	private static final double J1_MAX_SPEED_DEG_PER_SEC = 100.0;
	private static final double J2_MAX_SPEED_DEG_PER_SEC = 40.0;
	private static final double J3_MAX_SPEED_DEG_PER_SEC = 100.0;
	private static final double J4_MAX_SPEED_DEG_PER_SEC = 40.0;
	
	private static final int POSITION_PROFILE = 0;
	private static final int VELOCITY_PROFILE = 1;

	private CANTalon m_j1Motor;
	private CANTalon m_j2Motor;
	private CANTalon m_j3Motor;
	private CANTalon m_j4Motor;
	
	private DigitalInput m_toteGrabberSwitch;
	
	private DoubleSolenoid m_toteGrabberSolenoid;
	
	private CANTalon.ControlMode m_talonControlMode;
	
    private PIDParams j1PositionPidParams = new PIDParams(1.4, 0.0, 0.001, 0.0, 50, 0.0);
    private PIDParams j2PositionPidParams = new PIDParams(7.0, 0.0, 0.01, 0.0, 50, 0.0);
    private PIDParams j3PositionPidParams = new PIDParams(2.0, 0.0, 0.003, 0.167, 50, 0.0);
    private PIDParams j4PositionPidParams = new PIDParams(3.0, 0.005, 0.0, 0.0, 50, 0.0);

    private PIDParams j1VelocityPidParams = new PIDParams(0.5, 0.005, 0.0, 0.0, 0, 0.0);
    private PIDParams j2VelocityPidParams = new PIDParams(0.5, 0.02, 0.0, 0.0, 0, 0.0);
    private PIDParams j3VelocityPidParams = new PIDParams(0.5, 0.008, 0.0, 0.0, 0, 0.0);
    private PIDParams j4VelocityPidParams = new PIDParams(1.0, 0.027, 0.0, 0.0, 0, 0.0);

    public RobotArm() {
    	try {
			m_j1Motor = new CANTalon(RobotMap.ROBOT_ARM_J1_CAN_ID);
			m_j2Motor = new CANTalon(RobotMap.ROBOT_ARM_J2_CAN_ID);
			m_j3Motor = new CANTalon(RobotMap.ROBOT_ARM_J3_CAN_ID);
			m_j4Motor = new CANTalon(RobotMap.ROBOT_ARM_J4_CAN_ID);
			
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
	
			j1PositionPidParams.setTalonPID(m_j1Motor, POSITION_PROFILE);
			j2PositionPidParams.setTalonPID(m_j2Motor, POSITION_PROFILE);
			j3PositionPidParams.setTalonPID(m_j3Motor, POSITION_PROFILE);
			j4PositionPidParams.setTalonPID(m_j4Motor, POSITION_PROFILE);
	
			j1VelocityPidParams.setTalonPID(m_j1Motor, VELOCITY_PROFILE);
			j2VelocityPidParams.setTalonPID(m_j2Motor, VELOCITY_PROFILE);
			j3VelocityPidParams.setTalonPID(m_j3Motor, VELOCITY_PROFILE);
			j4VelocityPidParams.setTalonPID(m_j4Motor, VELOCITY_PROFILE);
	
			m_j1Motor.setPosition(RobotUtility.convertDegToEncoderPosition(J1_ENCODER_INIT_DEG, J1_SENSOR_GEAR_RATIO));
			m_j2Motor.setPosition(RobotUtility.convertDegToEncoderPosition(J2_ENCODER_INIT_DEG, J2_SENSOR_GEAR_RATIO));
			m_j3Motor.setPosition(RobotUtility.convertDegToEncoderPosition(J3_ENCODER_INIT_DEG, J3_SENSOR_GEAR_RATIO));
			
			setTalonControlMode(CANTalon.ControlMode.Position, 
					RobotUtility.convertDegToEncoderPosition(J1_ENCODER_INIT_DEG, J1_SENSOR_GEAR_RATIO), 
					RobotUtility.convertDegToEncoderPosition(J2_ENCODER_INIT_DEG, J2_SENSOR_GEAR_RATIO), 
					RobotUtility.convertDegToEncoderPosition(J3_ENCODER_INIT_DEG, J3_SENSOR_GEAR_RATIO),
					J4_ANALOG_ZERO_PRACTICE);
	
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
    
	private void setTalonControlMode(CANTalon.ControlMode mode, double j1Input, double j2Input, double j3Input, double j4Input) {
		m_talonControlMode = mode;
		m_j1Motor.changeControlMode(m_talonControlMode);
		m_j2Motor.changeControlMode(m_talonControlMode);
		m_j3Motor.changeControlMode(m_talonControlMode);
		m_j4Motor.changeControlMode(m_talonControlMode);
		if (mode == CANTalon.ControlMode.Speed) {
			m_j1Motor.setProfile(VELOCITY_PROFILE);
			m_j2Motor.setProfile(VELOCITY_PROFILE);
			m_j3Motor.setProfile(VELOCITY_PROFILE);
			m_j4Motor.setProfile(VELOCITY_PROFILE);
		}
		else if (mode == CANTalon.ControlMode.Position) {
			m_j1Motor.setProfile(POSITION_PROFILE);
			m_j2Motor.setProfile(POSITION_PROFILE);
			m_j3Motor.setProfile(POSITION_PROFILE);
			m_j4Motor.setProfile(POSITION_PROFILE);
//			m_j4Motor.setForwardSoftLimit((int)RobotUtility.convertDegToAnalogPosition(70, J4_ANALOG_ZERO_PRACTICE, J4_SENSOR_GEAR_RATIO));
//			m_j4Motor.setReverseSoftLimit((int)RobotUtility.convertDegToAnalogPosition(-70, J4_ANALOG_ZERO_PRACTICE, J4_SENSOR_GEAR_RATIO));
//			m_j4Motor.enableForwardSoftLimit(true);
//			m_j4Motor.enableReverseSoftLimit(true);
		}
		setTalonInput(j1Input, j2Input, j3Input, j4Input);
	}
	
	private void setTalonInput(double j1Input, double j2Input, double j3Input, double j4Input) {
		m_j1Motor.set(j1Input);
		m_j2Motor.set(j2Input);
		m_j3Motor.set(j3Input);
		m_j4Motor.set(j4Input);
	}
	
	public double getJ1PositionDeg() {
		return RobotUtility.convertEncoderPositionToDeg(m_j1Motor.getPosition(), J1_SENSOR_GEAR_RATIO);
	}
	
	public double getJ1SpeedDegPerSec() {
		return RobotUtility.convertEncoderVelocityToDegPerSec(m_j1Motor.getSpeed(), J1_SENSOR_GEAR_RATIO);
	}
	
	public double getJ2PositionDeg() {
		return RobotUtility.convertEncoderPositionToDeg(m_j2Motor.getPosition(), J2_SENSOR_GEAR_RATIO);
	}
	
	public double getJ2SpeedDegPerSec() {
		return RobotUtility.convertEncoderVelocityToDegPerSec(m_j2Motor.getSpeed(), J2_SENSOR_GEAR_RATIO);
	}
	
	public double getJ3PositionDeg() {
		return RobotUtility.convertEncoderPositionToDeg(m_j3Motor.getPosition(), J3_SENSOR_GEAR_RATIO) - J3_ENCODER_OFFSET_DEG;
	}
	
	public double getJ3SpeedDegPerSec() {
		return RobotUtility.convertEncoderVelocityToDegPerSec(m_j3Motor.getSpeed(), J3_SENSOR_GEAR_RATIO);
	}
	
	public double getJ4PositionDeg() {
		return RobotUtility.convertAnalogPositionToDeg(m_j4Motor.getAnalogInRaw(), J4_ANALOG_ZERO_PRACTICE, J4_SENSOR_GEAR_RATIO);
	}
	
	public double getJ4VelocityDegPerSec() {
		return RobotUtility.convertAnalogVelocityToDegPerSec(m_j4Motor.getSpeed(), J4_SENSOR_GEAR_RATIO);
	}
	
	public void controlWithJoystick() {
		double throttleRightX = OI.getInstance().getXBoxController().getRightXAxis();
		double throttleRightY = OI.getInstance().getXBoxController().getRightYAxis();
		double throttleLeftX = OI.getInstance().getXBoxController().getLeftXAxis();
		double throttleLeftY = OI.getInstance().getXBoxController().getLeftYAxis();

//		double speedCommand = RobotUtility.convertDegPerSecToAnalogVelocity(throttle*140, J4_SENSOR_GEAR_RATIO);
//		SmartDashboard.putNumber("J4 Speed Command raw", speedCommand);
//		SmartDashboard.putNumber("J4 Speed Command deg-sec", RobotUtility.convertAnalogVelocityToDegPerSec(speedCommand));

		double velocityCommandJ1 = -throttleRightX * J1_MAX_SPEED_DEG_PER_SEC;
		double velocityCommandJ2 = -throttleRightY * J2_MAX_SPEED_DEG_PER_SEC;
		double velocityCommandJ3 = -throttleLeftY * J3_MAX_SPEED_DEG_PER_SEC;

		double positionCommandJ2 = -throttleRightY * J2_MAX_ANGLE;
		double positionCommandJ4 = -throttleLeftX * J4_MAX_ANGLE;

		SmartDashboard.putNumber("Right X Throttle", throttleRightX);
		SmartDashboard.putNumber("Right Y Throttle", throttleRightY);
		SmartDashboard.putNumber("Left X Throttle", throttleLeftX);
		SmartDashboard.putNumber("Left Y Throttle", throttleLeftY);

		//		m_j4Motor.set(throttle);	
		if (Math.abs(velocityCommandJ1) < 5) {
			if (m_j1Motor.getControlMode() == CANTalon.ControlMode.Speed) {
				m_j1Motor.changeControlMode(CANTalon.ControlMode.Position);
				m_j1Motor.setProfile(POSITION_PROFILE);
			}
			setJ1PositionDeg(getJ1PositionDeg());
		}
		else {
			if (m_j1Motor.getControlMode() == CANTalon.ControlMode.Position) {
				m_j1Motor.changeControlMode(CANTalon.ControlMode.Speed);
				m_j1Motor.setProfile(VELOCITY_PROFILE);
			}
			setJ1VelocityDegPerSec(velocityCommandJ1);
		}
		
		SmartDashboard.putNumber("velocityCommandJ2", velocityCommandJ2);
		if (Math.abs(velocityCommandJ2) < 5) {
			if (m_j2Motor.getControlMode() == CANTalon.ControlMode.Speed) {
				m_j2Motor.changeControlMode(CANTalon.ControlMode.Position);
				m_j2Motor.setProfile(POSITION_PROFILE);
			}
			SmartDashboard.putNumber("getJ2PositionDeg", getJ2PositionDeg());
			SmartDashboard.putNumber("getJ2 KP", m_j2Motor.getP());
			setJ2PositionDeg(getJ2PositionDeg());
		}
		else {
			if (m_j2Motor.getControlMode() == CANTalon.ControlMode.Position) {
				m_j2Motor.changeControlMode(CANTalon.ControlMode.Speed);
				m_j2Motor.setProfile(VELOCITY_PROFILE);
			}
			SmartDashboard.putNumber("getJ2 KP", m_j2Motor.getP());
			setJ2VelocityDegPerSec(velocityCommandJ2);
		}
		
		if (Math.abs(velocityCommandJ3) < 5) {
			if (m_j3Motor.getControlMode() == CANTalon.ControlMode.Speed) {
				m_j3Motor.changeControlMode(CANTalon.ControlMode.Position);
				m_j3Motor.setProfile(POSITION_PROFILE);
			}
			setJ3PositionDeg(getJ3PositionDeg());
		}
		else {
			if (m_j3Motor.getControlMode() == CANTalon.ControlMode.Position) {
				m_j3Motor.changeControlMode(CANTalon.ControlMode.Speed);
				m_j3Motor.setProfile(VELOCITY_PROFILE);
			}
			setJ3VelocityDegPerSec(velocityCommandJ3);
		}
//		setJ2PositionDeg(positionCommandJ2);
//		setJ3PositionDeg(positionCommandJ3);
//		setJ4PositionDeg(positionCommandJ4);

//		m_j2Motor.set(throttleY);		
	}
	
	public void setJ1PositionDeg(double positionCommandDeg) {
		if (positionCommandDeg > J1_MAX_ANGLE) {
			positionCommandDeg = J1_MAX_ANGLE;
		}
		if (positionCommandDeg < J1_MIN_ANGLE) {
			positionCommandDeg = J1_MIN_ANGLE;
		}
		SmartDashboard.putNumber("J1 Position Actual Command (deg)", positionCommandDeg);
		
		m_j1Motor.set(RobotUtility.convertDegToEncoderPosition(positionCommandDeg, J1_SENSOR_GEAR_RATIO));
	}

	public void setJ1VelocityDegPerSec(double velocityCommandDegPerSec) {
		if ((getJ1PositionDeg() > J1_MAX_ANGLE && velocityCommandDegPerSec > 0) ||
			(getJ1PositionDeg() < J1_MIN_ANGLE && velocityCommandDegPerSec < 0)) {
			if (m_j1Motor.getControlMode() == CANTalon.ControlMode.Speed) {
				m_j1Motor.changeControlMode(CANTalon.ControlMode.Position);
				m_j1Motor.setProfile(POSITION_PROFILE);
			}
			setJ1PositionDeg(getJ1PositionDeg());
			return;
		}
		
		m_j1Motor.set(RobotUtility.convertDegPerSecToEncoderVelocity(velocityCommandDegPerSec, J1_SENSOR_GEAR_RATIO));
	}

	public void setJ2PositionDeg(double positionCommandDeg) {
		if (positionCommandDeg > J2_MAX_ANGLE) {
			positionCommandDeg = J2_MAX_ANGLE;
		}
		if (positionCommandDeg < J2_MIN_ANGLE) {
			positionCommandDeg = J2_MIN_ANGLE;
		}
		SmartDashboard.putNumber("J2 Position Actual Command (deg)", positionCommandDeg);
		m_j2Motor.set(RobotUtility.convertDegToEncoderPosition(positionCommandDeg, J2_SENSOR_GEAR_RATIO));
	}

	public void setJ2VelocityDegPerSec(double velocityCommandDegPerSec) {
		if ((getJ2PositionDeg() > J2_MAX_ANGLE && velocityCommandDegPerSec > 0) ||
			(getJ2PositionDeg() < J2_MIN_ANGLE && velocityCommandDegPerSec < 0)) {
			if (m_j2Motor.getControlMode() == CANTalon.ControlMode.Speed) {
				m_j2Motor.changeControlMode(CANTalon.ControlMode.Position);
				m_j2Motor.setProfile(POSITION_PROFILE);
			}
			setJ2PositionDeg(getJ2PositionDeg());
			return;
		}
		
		m_j2Motor.set(RobotUtility.convertDegPerSecToEncoderVelocity(velocityCommandDegPerSec, J2_SENSOR_GEAR_RATIO));
	}

	public void setJ3PositionDeg(double positionCommandDeg) {
		if (positionCommandDeg > J3_MAX_ANGLE) {
			positionCommandDeg = J3_MAX_ANGLE;
		}
		if (positionCommandDeg < J3_MIN_ANGLE) {
			positionCommandDeg = J3_MIN_ANGLE;
		}
		SmartDashboard.putNumber("J3 Position Actual Command (deg)", positionCommandDeg);
		
		m_j3Motor.set(RobotUtility.convertDegToEncoderPosition(positionCommandDeg + J3_ENCODER_OFFSET_DEG, J3_SENSOR_GEAR_RATIO));
	}

	public void setJ3VelocityDegPerSec(double velocityCommandDegPerSec) {
		if ((getJ3PositionDeg() > J3_MAX_ANGLE && velocityCommandDegPerSec > 0) ||
			(getJ3PositionDeg() < J3_MIN_ANGLE && velocityCommandDegPerSec < 0)) {
			if (m_j3Motor.getControlMode() == CANTalon.ControlMode.Speed) {
				m_j3Motor.changeControlMode(CANTalon.ControlMode.Position);
				m_j3Motor.setProfile(POSITION_PROFILE);
			}
			setJ3PositionDeg(getJ3PositionDeg());
			return;
		}
		
		m_j3Motor.set(RobotUtility.convertDegPerSecToEncoderVelocity(velocityCommandDegPerSec, J3_SENSOR_GEAR_RATIO));
	}

	public void setJ4PositionDeg(double positionCommandDeg) {
		if (positionCommandDeg > J4_MAX_ANGLE) {
			positionCommandDeg = J4_MAX_ANGLE;
		}
		if (positionCommandDeg < J4_MIN_ANGLE) {
			positionCommandDeg = J4_MIN_ANGLE;
		}
		SmartDashboard.putNumber("J4 Position Actual Command (deg)", positionCommandDeg);
		
		m_j4Motor.set(RobotUtility.convertDegToAnalogPosition(positionCommandDeg, J4_ANALOG_ZERO_PRACTICE, J4_SENSOR_GEAR_RATIO));
	}

	public void setJ4VelocityDegPerSec(double velocityCommandDegPerSec) {
		if ((getJ4PositionDeg() > J4_MAX_ANGLE && velocityCommandDegPerSec > 0) ||
			(getJ4PositionDeg() < J4_MIN_ANGLE && velocityCommandDegPerSec < 0)) {
			if (m_j4Motor.getControlMode() == CANTalon.ControlMode.Speed) {
				m_j4Motor.changeControlMode(CANTalon.ControlMode.Position);
				m_j4Motor.setProfile(POSITION_PROFILE);
			}
			setJ4PositionDeg(getJ4PositionDeg());
			return;
		}
		
		m_j4Motor.set(RobotUtility.convertDegPerSecToEncoderVelocity(velocityCommandDegPerSec, J4_SENSOR_GEAR_RATIO));
	}

	public void updateStatus() {
		SmartDashboard.putNumber("J1 Position (raw)", m_j1Motor.getPosition());
		SmartDashboard.putNumber("J1 Position (deg)", getJ1PositionDeg());
		SmartDashboard.putNumber("J1 Speed (deg-sec)", getJ1SpeedDegPerSec());
		
		SmartDashboard.putNumber("J2 Position (raw)", m_j2Motor.getPosition());
		SmartDashboard.putNumber("J2 Position (deg)", getJ2PositionDeg());
		SmartDashboard.putNumber("J2 Speed (deg-sec)", getJ2SpeedDegPerSec());
		
		SmartDashboard.putNumber("J3 Position (raw)", m_j3Motor.getPosition());
		SmartDashboard.putNumber("J3 Position (deg)", getJ3PositionDeg());
		SmartDashboard.putNumber("J3 Speed (deg-sec)", getJ3SpeedDegPerSec());
		
		SmartDashboard.putNumber("J4 Position (raw)", m_j4Motor.getPosition());
		SmartDashboard.putNumber("J4 Position (deg)", getJ4PositionDeg());
		SmartDashboard.putNumber("J4 Speed (deg-sec)", getJ4VelocityDegPerSec());

		SmartDashboard.putBoolean("IR Tote Grabber Switch", getToteGrabberSwitch());
		
		SmartDashboard.putString("J2 Control Mode", m_j2Motor.getControlMode().toString());
	}
}

