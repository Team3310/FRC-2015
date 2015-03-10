package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainVelocityControl extends Command 
{
	private double leftTargetDegPerSec;
	private double rightTargetDegPerSec;
	private double errorDegPerSec;
	private double timeoutSeconds;

    public DriveTrainVelocityControl(double leftVelocityDegPerSec, double rightVelocityDegPerSec, double errorDegPerSec, double timeoutSeconds) {
        // Use requires() here to declare subsystem dependencies
    	this.leftTargetDegPerSec = leftVelocityDegPerSec;
    	this.rightTargetDegPerSec = rightVelocityDegPerSec;
    	this.errorDegPerSec = errorDegPerSec;
    	this.timeoutSeconds = timeoutSeconds;
        requires(RobotMain.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	RobotMain.driveTrain.startPIDVelocity(leftTargetDegPerSec, rightTargetDegPerSec, errorDegPerSec);
    	setTimeout(timeoutSeconds);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	RobotMain.driveTrain.stopPID();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}