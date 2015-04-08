package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainStraightSoftwarePID extends Command 
{
	private double distanceInches;
	private double toleranceInches;
	private double maxThrottle;

    public DriveTrainStraightSoftwarePID(double distanceInches, double toleranceInches, double maxThrottle) {
    	this.distanceInches = distanceInches;
    	this.toleranceInches = toleranceInches;
    	this.maxThrottle = maxThrottle;
    	
    	requires(RobotMain.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	RobotMain.driveTrain.startStraightSoftwarePID(distanceInches, toleranceInches, maxThrottle);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return Math.abs(RobotMain.driveTrain.getSoftwarePIDError()) < toleranceInches; 
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}