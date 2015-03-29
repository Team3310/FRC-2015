package edu.rhhs.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class BinGrabberWaitAndStow extends CommandGroup 
{
    public BinGrabberWaitAndStow() {
    	addSequential(new WaitCommand(1));
    	addSequential(new BinGrabberPositionStowedPID());
    }
}