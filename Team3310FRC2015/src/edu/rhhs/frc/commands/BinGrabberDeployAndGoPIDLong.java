package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.BinGrabber;
import edu.rhhs.frc.subsystems.BinGrabber.BinGrabberState;
import edu.rhhs.frc.subsystems.DriveTrain.ToteSledPosition;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.command.WaitForChildren;

/**
 *
 */
public class BinGrabberDeployAndGoPIDLong extends CommandGroup 
{
    public BinGrabberDeployAndGoPIDLong() {
    	addSequential(new BinGrabberPositionDownPID(BinGrabber.DEPLOYED_POSITION_DEG, BinGrabber.DEPLOYED_POSITION_DEG));
    	addSequential(new BinGrabberPivotLockPosition(BinGrabberState.RETRACTED)); 
    	addSequential(new DriveTrainStraightSoftwarePID(102, 4, 1.0));
        addSequential(new BinGrabberClawPosition(BinGrabberState.EXTENDED));  
    	addSequential(new DriveTrainStraightSoftwarePID(40, 4, 0.3));
        addSequential(new BinGrabberClawPosition(BinGrabberState.RETRACTED));  
        addSequential(new WaitCommand(0.5));
        addParallel(new DriveTrainStraightSoftwarePID(50, 4, 0.6));
    	addSequential(new BinGrabberPositionStowedPID());
        addSequential(new WaitForChildren());
        addSequential(new DriveTrainGyroTurn(47, 4, 0.6));
        addSequential(new DriveTrainStopPID());
    	addSequential(new BinGrabberPositionHalfStowedPID());
        addSequential(new DriveTrainToteSledPosition(ToteSledPosition.DOWN));
    }
}