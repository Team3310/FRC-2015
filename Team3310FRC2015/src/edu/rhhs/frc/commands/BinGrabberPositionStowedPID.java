package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.BinGrabber;
import edu.wpi.first.wpilibj.command.Command;

public class BinGrabberPositionStowedPID extends Command 
{

    public BinGrabberPositionStowedPID() {
        // Use requires() here to declare subsystem dependencies
        requires(RobotMain.binGrabber);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	RobotMain.binGrabber.startPositionUpPID(BinGrabber.STOWED_POSITION_DEG, BinGrabber.STOWED_POSITION_DEG, 1);
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