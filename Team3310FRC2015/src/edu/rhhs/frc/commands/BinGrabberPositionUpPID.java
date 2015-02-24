package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.CANTalon.StatusFrameRate;
import edu.wpi.first.wpilibj.command.Command;

public class BinGrabberPositionUpPID extends Command 
{
	private double leftTargetDeg;
	private double rightTargetDeg;
	private double errorDeg;
	private double timeoutMs;

    public BinGrabberPositionUpPID(double leftPositionDeg, double rightPositionDeg, double errorDeg, double timeoutMs) {
        // Use requires() here to declare subsystem dependencies
    	this.leftTargetDeg = leftPositionDeg;
    	this.rightTargetDeg = rightPositionDeg;
    	this.errorDeg = errorDeg;
    	this.timeoutMs = timeoutMs;
        requires(RobotMain.binGrabber);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	RobotMain.binGrabber.startPositionUpPID(leftTargetDeg, rightTargetDeg, errorDeg);
    	setTimeout(timeoutMs/1000.0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut() || (RobotMain.binGrabber.getLeftErrorDeg() < errorDeg && RobotMain.binGrabber.getRightErrorDeg() < errorDeg);
    }

    // Called once after isFinished returns true
    protected void end() {
    	RobotMain.binGrabber.stopPID();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
