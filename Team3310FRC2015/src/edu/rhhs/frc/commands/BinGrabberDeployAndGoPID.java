package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.BinGrabber;
import edu.rhhs.frc.subsystems.BinGrabber.BinGrabberState;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class BinGrabberDeployAndGoPID extends CommandGroup 
{
    public BinGrabberDeployAndGoPID() {
    	addSequential(new BinGrabberPositionDownPID(BinGrabber.DEPLOYED_POSITION_DEG, BinGrabber.DEPLOYED_POSITION_DEG));
        addSequential(new DriveTrainPositionControl(96, 96, true, 96));
        addSequential(new BinGrabberPivotLockPosition(BinGrabberState.RETRACTED)); 
        addSequential(new BinGrabberClawPosition(BinGrabberState.RETRACTED));  
        addSequential(new DriveTrainSpeedTimeout(0.4, 3));
        addSequential(new BinGrabberPositionStowedPID());
        addSequential(new DriveTrainStopPID());
    }
}