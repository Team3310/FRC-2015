package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.BinGrabber.BinGrabberState;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitForChildren;

public class BinGrabberNoDeployAndGoPID extends CommandGroup 
{
    public BinGrabberNoDeployAndGoPID() {
        addSequential(new BinGrabberPivotLockPosition(BinGrabberState.RETRACTED)); 
        addSequential(new DriveTrainPositionControl(108, 108, true, 108));
        addSequential(new BinGrabberClawPosition(BinGrabberState.RETRACTED));  
        addParallel(new DriveTrainStraightSoftwarePID(40, 2, 0.5));
//       addParallel(new DriveTrainMotionProfileStraight(40, 30));
    //    addParallel(new DriveTrainSpeedTimeout(0.4, 1.5));
    	addSequential(new BinGrabberPositionStowedPID());
        addSequential(new WaitForChildren());
        addSequential(new DriveTrainStopPID());
    }
}