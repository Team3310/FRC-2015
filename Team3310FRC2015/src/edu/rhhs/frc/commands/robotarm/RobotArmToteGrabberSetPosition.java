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
public class RobotArmToteGrabberSetPosition extends RobotArmCommand {

	private RobotArm.ToteGrabberPosition m_position;

	public RobotArmToteGrabberSetPosition(RobotArm.ToteGrabberPosition position) {
    	m_position = position;
    }

    protected void initialize() {
    	RobotMain.robotArm.setToteGrabberPosition(m_position);
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
