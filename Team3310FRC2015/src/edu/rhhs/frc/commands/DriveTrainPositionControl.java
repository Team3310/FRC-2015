package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainPositionControl extends Command 
{
	private double leftTargetInches;
	private double rightTargetInches;
	private double avgFinishedInches;
	private boolean isFinishedByInches;

    public DriveTrainPositionControl(double leftPositionInches, double rightPositionInches, boolean isFinishedByInches, double avgFinishedInches) {
        // Use requires() here to declare subsystem dependencies
    	this.leftTargetInches = leftPositionInches;
    	this.rightTargetInches = rightPositionInches;
    	this.avgFinishedInches = avgFinishedInches;
    	this.isFinishedByInches = isFinishedByInches;
        requires(RobotMain.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	setTimeout(3);
    	RobotMain.driveTrain.startPIDPosition(leftTargetInches, rightTargetInches, avgFinishedInches);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (isTimedOut()) {
    		return true;
    	}
    	double error = 5;
    	if (isFinishedByInches) {
    		if (Math.abs((RobotMain.driveTrain.getLeftDistanceInches() + RobotMain.driveTrain.getRightDistanceInches()) / 2 - avgFinishedInches) < error) {
    			return true;
    		}
    		return false;
    	}
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