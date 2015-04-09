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
    	addSequential(new BinGrabberPivotLockPosition(BinGrabberState.RETRACTED)); 
    	addSequential(new DriveTrainStraightSoftwarePID(102, 2, 1.0));
//        addSequential(new DriveTrainPositionControl(102, 102, true, 102));
        addSequential(new BinGrabberClawPosition(BinGrabberState.RETRACTED));  
        addSequential(new DriveTrainStraightSoftwarePID(50, 2, 0.3));
//        addParallel(new DriveTrainMotionProfileStraight(40, 30));
//        addParallel(new DriveTrainSpeedTimeout(0.4, 1.5));
        addParallel(new DriveTrainStraightSoftwarePID(35, 2, 0.5));
    	addSequential(new BinGrabberPositionHalfStowedPID());
        addSequential(new WaitForChildren());
        addSequential(new DriveTrainGyroTurn(45, 3, 0.7));
        addSequential(new DriveTrainStraightSoftwarePID(30, 2, 0.4));
        addSequential(new DriveTrainStopPID());
    }
}