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
public class RobotArmWaitForNext extends RobotArmCommand {

	public RobotArmWaitForNext() {
    }

    protected void initialize() {
		RobotMain.robotArm.setWaitForNext(true);
    }

    protected void execute() {
    }

    protected boolean isFinished() {
    	return !RobotMain.robotArm.isWaitForNext();
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
