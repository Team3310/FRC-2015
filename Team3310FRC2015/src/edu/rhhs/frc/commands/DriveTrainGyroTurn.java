package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainGyroTurn extends Command 
{
	private double turnAngleDeg;
	private double toleranceDeg;
	private double maxThrottle;

    public DriveTrainGyroTurn(double turnAngleDeg, double toleranceDeg, double maxThrottle) {
    	this.turnAngleDeg = turnAngleDeg;
    	this.toleranceDeg = toleranceDeg;
    	this.maxThrottle = maxThrottle;
    	
    	requires(RobotMain.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	RobotMain.driveTrain.startGyroTurn(turnAngleDeg, toleranceDeg, maxThrottle);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return Math.abs(RobotMain.driveTrain.getSoftwarePIDError()) < toleranceDeg; 
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}