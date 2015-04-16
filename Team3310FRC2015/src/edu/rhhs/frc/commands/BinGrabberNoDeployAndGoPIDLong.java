package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.BinGrabber.BinGrabberState;
import edu.rhhs.frc.subsystems.DriveTrain.ToteSledPosition;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.command.WaitForChildren;

public class BinGrabberNoDeployAndGoPIDLong extends CommandGroup 
{
    public BinGrabberNoDeployAndGoPIDLong() {
    	addSequential(new BinGrabberPositionStowedPID());
    	addSequential(new DriveTrainStraightSoftwarePID(40, 4, 0.3));
        addSequential(new WaitCommand(0.5));
        addParallel(new DriveTrainStraightSoftwarePID(45, 4, 0.6));
        addSequential(new WaitForChildren());
        addSequential(new DriveTrainGyroTurn(45, 4, 0.6));
        addSequential(new DriveTrainStopPID());
    	addSequential(new BinGrabberPositionHalfStowedPID());
        addSequential(new DriveTrainToteSledPosition(ToteSledPosition.DOWN));
    	/*
     	addSequential(new BinGrabberPositionStowedPID());
    	addSequential(new BinGrabberPivotLockPosition(BinGrabberState.RETRACTED)); 
        addSequential(new BinGrabberClawPosition(BinGrabberState.RETRACTED));  
    	addSequential(new DriveTrainStraightSoftwarePID(181, 4, 0.5));
        addSequential(new DriveTrainGyroTurn(33, 4, 0.6));
        addSequential(new DriveTrainStopPID());
     	addSequential(new BinGrabberPositionHalfStowedPID());
        addSequential(new DriveTrainToteSledPosition(ToteSledPosition.DOWN));*/
    }
}