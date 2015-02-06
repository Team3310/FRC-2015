
package edu.rhhs.frc.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class ExampleSubsystem extends Subsystem {
    
    private CANTalon motor1;
    private DigitalInput limitSwitch;
    
    public ExampleSubsystem() {
    	motor1 = new CANTalon(2);
    	limitSwitch = new DigitalInput(1);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void setSpeed(double speed) {
    	motor1.set(speed);
    }
    
    public boolean isDepressed() {
    	return limitSwitch.get();
    }
}

