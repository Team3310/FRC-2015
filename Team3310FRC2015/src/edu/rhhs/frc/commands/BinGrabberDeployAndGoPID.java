package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.BinGrabber;
import edu.rhhs.frc.subsystems.BinGrabber.BinGrabberState;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitForChildren;

public class BinGrabberDeployAndGoPID extends CommandGroup 
{
    public BinGrabberDeployAndGoPID() {
    	addSequential(new BinGrabberPositionDownPID(BinGrabber.DEPLOYED_POSITION_DEG, BinGrabber.DEPLOYED_POSITION_DEG));
        addSequential(new BinGrabberPivotLockPosition(BinGrabberState.RETRACTED)); 
        addSequential(new DriveTrainPositionControl(108, 108, true, 108));
        addSequential(new BinGrabberClawPosition(BinGrabberState.RETRACTED));  
//        addParallel(new DriveTrainMotionProfileStraight(40, 30));
        addParallel(new DriveTrainSpeedTimeout(0.4, 1.5));
    	addSequential(new BinGrabberPositionStowedPID());
        addSequential(new WaitForChildren());
        addSequential(new DriveTrainStopPID());
    }
}