package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainPositionControl extends Command 
{
	private double leftTargetInches;
	private double rightTargetInches;
	private double errorInches;
	private double timeoutSeconds;

    public DriveTrainPositionControl(double leftPositionInches, double rightPositionInches, double errorInches, double timeoutSeconds) {
        // Use requires() here to declare subsystem dependencies
    	this.leftTargetInches = leftPositionInches;
    	this.rightTargetInches = rightPositionInches;
    	this.errorInches = errorInches;
    	this.timeoutSeconds = timeoutSeconds;
        requires(RobotMain.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	RobotMain.driveTrain.startPIDPosition(leftTargetInches, rightTargetInches, errorInches);
    	setTimeout(timeoutSeconds);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut(); // || RobotMain.driveTrain.isAtLeftTarget() || RobotMain.driveTrain.isAtRightTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    	RobotMain.driveTrain.stopPID();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
