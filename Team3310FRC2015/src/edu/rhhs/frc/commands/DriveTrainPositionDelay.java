package edu.rhhs.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class DriveTrainPositionDelay extends CommandGroup 
{
    public DriveTrainPositionDelay() {
        addSequential(new WaitCommand(0.050));  
        addSequential(new DriveTrainPositionControl(120, 120, true, 120));  
    }
}