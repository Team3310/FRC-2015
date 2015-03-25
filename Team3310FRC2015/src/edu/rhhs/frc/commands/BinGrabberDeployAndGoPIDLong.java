package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.BinGrabber;
import edu.rhhs.frc.subsystems.BinGrabber.BinGrabberState;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitForChildren;

/**
 *
 */
public class BinGrabberDeployAndGoPIDLong extends CommandGroup 
{
    public BinGrabberDeployAndGoPIDLong() {
    	addSequential(new BinGrabberPositionDownPID(BinGrabber.DEPLOYED_POSITION_DEG, BinGrabber.DEPLOYED_POSITION_DEG));
        addSequential(new DriveTrainPositionControl(148, 148, true, 148));
        addSequential(new BinGrabberClawPosition(BinGrabberState.RETRACTED));  
        addSequential(new BinGrabberPivotLockPosition(BinGrabberState.RETRACTED)); 
//        addParallel(new DriveTrainMotionProfileStraight(40, 30));
        addParallel(new DriveTrainSpeedTimeout(0.4, 1.5));
    	addSequential(new BinGrabberPositionStowedPID());
        addSequential(new WaitForChildren());
        addSequential(new DriveTrainStopPID());
    }
}