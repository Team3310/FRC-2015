package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.RobotArm;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ToteGrabberAutoClose extends Command 
{	
    public ToteGrabberAutoClose() {
         requires(RobotMain.robotArm);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
		RobotMain.robotArm.setToteGrabberPosition(RobotArm.ToteGrabberPosition.OPEN);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
   }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (RobotMain.robotArm.getToteGrabberSwitch() == false) {
    		RobotMain.robotArm.setToteGrabberPosition(RobotArm.ToteGrabberPosition.CLOSE);
            return true;
    	}
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}