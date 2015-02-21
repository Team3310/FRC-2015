package edu.rhhs.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class DrivetrainPositionDelay extends CommandGroup 
{
    public  DrivetrainPositionDelay() {
        addSequential(new WaitCommand(0.050));  
        addSequential(new DriveTrainPositionControl(120, 120, 1, 2));  
    }
}