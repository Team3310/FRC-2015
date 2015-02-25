package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;

public class DriveTrainPositionHoldOn extends ExtraTimeoutCommand 
{
    public DriveTrainPositionHoldOn() {
        // Use requires() here to declare subsystem dependencies
        requires(RobotMain.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	RobotMain.driveTrain.startPIDPositionHold();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
