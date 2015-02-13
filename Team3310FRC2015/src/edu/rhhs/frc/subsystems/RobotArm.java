package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.OI;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.commands.RobotArmWithJoystick;
import edu.rhhs.frc.utility.PIDParams;
import edu.rhhs.frc.utility.RobotUtility;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RobotArm extends Subsystem {
    
	private static final double J4_ANALOG_ZERO_PRACTICE = 477.0;

	private static final double J4_SENSOR_GEAR_RATIO = 60.0/30.0;

	private static final double J4_MAX_ANGLE = 60.0;
	private static final double J4_MIN_ANGLE = -60.0;
	
	private static final int POSITION_PROFILE = 0;
	private static final int VELOCITY_PROFILE = 1;

	private CANTalon m_j1Motor;
	private CANTalon m_j2Motor;
	private CANTalon m_j3Motor;
	private CANTalon m_j4Motor;
	private DigitalInput m_toteGrabberSwitch;
	
	private CANTalon.ControlMode m_talonControlMode;
	
    private PIDParams j1PositionPidParams = new PIDParams(0.3, 0.0, 0.0, 0.0, 0, 0.0);
    private PIDParams j2PositionPidParams = new PIDParams(0.3, 0.0, 0.0, 0.0, 0, 0.0);
    private PIDParams j3PositionPidParams = new PIDParams(0.3, 0.0, 0.0, 0.0, 0, 0.0);
    private PIDParams j4PositionPidParams = new PIDParams(4.0, 0.0, 0.0, 0.0, 0, 0.0);

    private PIDParams j1VelocityPidParams = new PIDParams(0.3, 0.005, 0.0, 0.0, 0, 0.0);
    private PIDParams j2VelocityPidParams = new PIDParams(0.3, 0.005, 0.0, 0.0, 0, 0.0);
    private PIDParams j3VelocityPidParams = new PIDParams(0.3, 0.005, 0.0, 0.0, 0, 0.0);
    private PIDParams j4VelocityPidParams = new PIDParams(1.0, 0.027, 0.0, 0.0, 0, 0.0);

    public RobotArm() {
		m_j1Motor = new CANTalon(RobotMap.ROBOT_ARM_J1_CAN_ID);
		m_j2Motor = new CANTalon(RobotMap.ROBOT_ARM_J2_CAN_ID);
		m_j3Motor = new CANTalon(RobotMap.ROBOT_ARM_J3_CAN_ID);
		m_j4Motor = new CANTalon(RobotMap.ROBOT_ARM_J4_CAN_ID);
		
		m_j1Motor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		m_j1Motor.reverseOutput(true);
		m_j1Motor.reverseSensor(true);

		m_j2Motor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		m_j2Motor.reverseOutput(true);
		m_j2Motor.reverseSensor(true);

		m_j3Motor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		m_j3Motor.reverseOutput(true);
		m_j3Motor.reverseSensor(true);

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

//		setTalonControlMode(CANTalon.ControlMode.PercentVbus, 0, 0, 0, 0);
//		setTalonControlMode(CANTalon.ControlMode.Speed, 0, 0, 0, 0);
		setTalonControlMode(CANTalon.ControlMode.Position, 0, 0, 0, J4_ANALOG_ZERO_PRACTICE);
    }
    
    public void initDefaultCommand() {
		setDefaultCommand(new RobotArmWithJoystick());
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
		else {
			m_j1Motor.setProfile(POSITION_PROFILE);
			m_j2Motor.setProfile(POSITION_PROFILE);
			m_j3Motor.setProfile(POSITION_PROFILE);
			m_j4Motor.setProfile(POSITION_PROFILE);
			m_j4Motor.setForwardSoftLimit((int)RobotUtility.convertDegToAnalogPosition(70, J4_ANALOG_ZERO_PRACTICE, J4_SENSOR_GEAR_RATIO));
			m_j4Motor.setReverseSoftLimit((int)RobotUtility.convertDegToAnalogPosition(-70, J4_ANALOG_ZERO_PRACTICE, J4_SENSOR_GEAR_RATIO));
			m_j4Motor.enableForwardSoftLimit(true);
			m_j4Motor.enableReverseSoftLimit(true);
		}
		setTalonInput(j1Input, j3Input, j3Input, j4Input);
	}
	
	private void setTalonInput(double j1Input, double j2Input, double j3Input, double j4Input) {
		m_j1Motor.set(j1Input);
		m_j2Motor.set(j2Input);
		m_j3Motor.set(j3Input);
		m_j4Motor.set(j4Input);
	}
	
	public double getJ4PositionDeg() {
		return RobotUtility.convertAnalogPositionToDeg(m_j4Motor.getAnalogInRaw(), J4_ANALOG_ZERO_PRACTICE, J4_SENSOR_GEAR_RATIO);
	}
	
	public double getJ4VelocityDegPerSec() {
		return RobotUtility.convertAnalogVelocityToDegPerSec(m_j4Motor.getSpeed(), J4_SENSOR_GEAR_RATIO);
	}
	
	public void controlWithJoystick() {
//		m_j4Motor.set(-OI.getInstance().getXBoxController().getRightXAxis());
		double throttle = OI.getInstance().getXBoxController().getRightXAxis();
//		double speedCommand = RobotUtility.convertDegPerSecToAnalogVelocity(throttle*140, J4_SENSOR_GEAR_RATIO);
//		SmartDashboard.putNumber("J4 Speed Command raw", speedCommand);
//		SmartDashboard.putNumber("J4 Speed Command deg-sec", RobotUtility.convertAnalogVelocityToDegPerSec(speedCommand));
//		SmartDashboard.putNumber("J4 Throttle", throttle);
//		m_j4Motor.set(speedCommand);

		double positionCommand = throttle * J4_MAX_ANGLE;
		SmartDashboard.putNumber("J4 Position Attempted Command (deg)", positionCommand);
		SmartDashboard.putNumber("J4 Throttle", throttle);
		
		setJ4PositionDeg(positionCommand);
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

	public void updateStatus() {
		SmartDashboard.putNumber("J4 Analog Raw", m_j4Motor.getAnalogInRaw());
		SmartDashboard.putNumber("J4 Position (raw)", m_j4Motor.getPosition());
		SmartDashboard.putNumber("J4 Position (deg)", getJ4PositionDeg());
		SmartDashboard.putNumber("J4 Speed (raw)", m_j4Motor.getSpeed());
		SmartDashboard.putNumber("J4 Error (raw)", m_j4Motor.getClosedLoopError());
		SmartDashboard.putNumber("J4 Speed (deg-sec)", getJ4VelocityDegPerSec());
	}
}

