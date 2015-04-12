package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.BinGrabber.BinGrabberState;
import edu.rhhs.frc.subsystems.DriveTrain.ToteSledPosition;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class BinGrabberNoDeployAndGoPID extends CommandGroup 
{
    public BinGrabberNoDeployAndGoPID() {
     	addSequential(new BinGrabberPositionStowedPID());
    	addSequential(new BinGrabberPivotLockPosition(BinGrabberState.RETRACTED)); 
        addSequential(new BinGrabberClawPosition(BinGrabberState.RETRACTED));  
    	addSequential(new DriveTrainStraightSoftwarePID(181, 4, 0.5));
        addSequential(new DriveTrainGyroTurn(33, 4, 0.6));
        addSequential(new DriveTrainStopPID());
     	addSequential(new BinGrabberPositionHalfStowedPID());
        addSequential(new DriveTrainToteSledPosition(ToteSledPosition.DOWN));
    }
}