package edu.rhhs.frc.subsystems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class BinGrabber extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setMotorSpeed(1.0);
    }
    
    /**
     * Where 'x' is the speed, ranging from -1 to +1
     */
    public void setMotorSpeed(double x) {
    	getAllocatedMotor(1).set(.5);
    }
	private SpeedController getAllocatedMotor(int i) {
		return new Victor(i);
	}
}

