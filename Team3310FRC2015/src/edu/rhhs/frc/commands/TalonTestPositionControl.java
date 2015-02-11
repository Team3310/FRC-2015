package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class TalonTestPositionControl extends Command 
{
	private double leftTargetDeg;
	private double rightTargetDeg;
	private double errorDeg;
	private double timeoutSeconds;

    public TalonTestPositionControl(double leftPositionDeg, double rightPositionDeg, double errorDeg, double timeoutSeconds) {
        // Use requires() here to declare subsystem dependencies
    	this.leftTargetDeg = leftPositionDeg;
    	this.rightTargetDeg = rightPositionDeg;
    	this.errorDeg = errorDeg;
    	this.timeoutSeconds = timeoutSeconds;
        requires(RobotMain.binGrabber);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	RobotMain.binGrabber.startPositionPID(leftTargetDeg, rightTargetDeg, errorDeg);
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
    	RobotMain.binGrabber.stopPID();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
