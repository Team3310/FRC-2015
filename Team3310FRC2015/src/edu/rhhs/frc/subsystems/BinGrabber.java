package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMap;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *	BinGrabber (name to be determined) is a subsystem that controls the two motors associated with each of the two arms (codenames: "Bubba" and "Gump").
 */
public class BinGrabber extends Subsystem 
{
	private CANTalon m_rightMotor;
    private CANTalon m_leftMotor;
    
    public BinGrabber() {
    	m_rightMotor = new CANTalon(RobotMap.BINGRABBER_RIGHT_CAN_ID);
    	m_leftMotor = new CANTalon(RobotMap.BINGRABBER_LEFT_CAN_ID);
    }

    @Override
    public void initDefaultCommand() {
        
    }
    
    public void setSpeedLeft(double speed) {
    	m_leftMotor.set(speed);
    }
    
    public void setSpeedRight(double speed) {
    	m_rightMotor.set(speed);
    }
    
    public void setSpeed(double speed) {
    	m_leftMotor.set(speed);
    	m_rightMotor.set(speed);
    }
}