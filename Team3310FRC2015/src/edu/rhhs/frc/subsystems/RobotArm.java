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
    
	private static final double J4_ANALOG_ZERO_PRACTICE = 512.0;

	private CANTalon m_j1Motor;
	private CANTalon m_j2Motor;
	private CANTalon m_j3Motor;
	private CANTalon m_j4Motor;
	private DigitalInput m_toteGrabberSwitch;
	
	private CANTalon.ControlMode m_talonControlMode;
	
    private PIDParams j1PositionPidParams = new PIDParams(0.3, 0.0, 0.0, 0.0, 0, 0.0);
    private PIDParams j2PositionPidParams = new PIDParams(0.3, 0.0, 0.0, 0.0, 0, 0.0);
    private PIDParams j3PositionPidParams = new PIDParams(0.3, 0.0, 0.0, 0.0, 0, 0.0);
    private PIDParams j4PositionPidParams = new PIDParams(0.3, 0.0, 0.0, 0.0, 0, 0.0);

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
		m_j4Motor.reverseSensor(true);

		j1PositionPidParams.setTalonPID(m_j1Motor, 0);
		j2PositionPidParams.setTalonPID(m_j2Motor, 0);
		j3PositionPidParams.setTalonPID(m_j3Motor, 0);
		j4PositionPidParams.setTalonPID(m_j4Motor, 0);

		setTalonControlMode(CANTalon.ControlMode.PercentVbus, 0, 0, 0, 0);
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
		setTalonInput(j1Input, j3Input, j3Input, j4Input);
	}
	
	private void setTalonInput(double j1Input, double j2Input, double j3Input, double j4Input) {
		m_j1Motor.set(j1Input);
		m_j2Motor.set(j2Input);
		m_j3Motor.set(j3Input);
		m_j4Motor.set(j4Input);
	}
	
	public double getJ4PositionDeg() {
		return RobotUtility.convertAnalogPositionToDeg(m_j4Motor.getAnalogInRaw(), J4_ANALOG_ZERO_PRACTICE);
	}
	
	public void controlWithJoystick() {
		m_j4Motor.set(OI.getInstance().getXBoxController().getRightYAxis());
	}

	public void updateStatus() {
		SmartDashboard.putNumber("J4 Analog Raw", m_j4Motor.getAnalogInRaw());
		SmartDashboard.putNumber("J4 Position (raw)", m_j4Motor.getPosition());
		SmartDashboard.putNumber("J4 Position (deg)", getJ4PositionDeg());
		SmartDashboard.putNumber("J4 Throttle", m_j4Motor.get());
	}
}

