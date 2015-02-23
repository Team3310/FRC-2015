package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.BinGrabber;
import edu.rhhs.frc.subsystems.BinGrabber.BinGrabberState;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class BinGrabberDeployAndGo extends CommandGroup 
{
    public BinGrabberDeployAndGo() {
    	addSequential(new BinGrabberDeployAngle(1.0, BinGrabber.DEPLOYED_POSITION_DRIVETRAIN_ENGAGE_DEG, 2000));
        addSequential(new DriveTrainPositionControl(120, 120, true, 120));
        addSequential(new BinGrabberPivotLockPosition(BinGrabberState.RETRACTED));  
    }
}