package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.RobotArm;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SecondaryToteGrabberPosition extends Command 
{	
	private RobotArm.ToteGrabberPosition m_position;
	
    public SecondaryToteGrabberPosition(RobotArm.ToteGrabberPosition position) {
    	m_position = position;
        requires(RobotMain.robotArm);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	RobotMain.robotArm.setSecondaryToteGrabberPosition(m_position);
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