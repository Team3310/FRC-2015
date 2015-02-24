package edu.rhhs.frc.commands.robotarm;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.RobotArm;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.ProfileOutput;
import edu.rhhs.frc.utility.motionprofile.WaypointList;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RobotArmToteGrabberAutoClose extends RobotArmCommand {

	public RobotArmToteGrabberAutoClose() {
    }

    protected void initialize() {
		RobotMain.robotArm.setToteGrabberPosition(RobotArm.ToteGrabberPosition.OPEN);
    }

    protected void execute() {
    }

    protected boolean isFinished() {
    	if (RobotMain.robotArm.getToteGrabberSwitch() == false) {
    		RobotMain.robotArm.setToteGrabberPosition(RobotArm.ToteGrabberPosition.CLOSE);
            return true;
    	}
    	return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
