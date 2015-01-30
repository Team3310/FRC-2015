package edu.rhhs.frc.commands;

import edu.rhhs.frc.OI;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Command;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 * @author Author
 */
public abstract class CommandBase extends Command {

    // Get an instance of the OI
    
    // Create a single static instance of all of your subsystems
    public static DigitalInput limitSwitch;
    public static RobotDrive rd;
	
    public static void init() {
        OI.getInstance();
        limitSwitch = new DigitalInput(1);
        rd = new RobotDrive(1, 2);
        // No commands create this subsystem
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}