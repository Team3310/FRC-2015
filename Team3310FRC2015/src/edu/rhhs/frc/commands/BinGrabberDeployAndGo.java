package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.BinGrabber;
import edu.rhhs.frc.subsystems.BinGrabber.BinGrabberState;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class BinGrabberDeployAndGo extends CommandGroup {
    
    public  BinGrabberDeployAndGo() {
        addSequential(new BinGrabberDeployAngle(1.0, BinGrabber.DEPLOYED_POSITION_TIMED_DEG, 300));
        addSequential(new DriveTrainSpeedTimeout(1.0, 3));  
        addSequential(new BinGrabberPivotLockPosition(BinGrabberState.RETRACTED));  
    }
}
