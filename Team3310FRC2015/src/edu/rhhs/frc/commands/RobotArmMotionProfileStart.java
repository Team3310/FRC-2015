package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.WaypointList;
import edu.wpi.first.wpilibj.command.Command;

public class RobotArmMotionProfileStart extends Command 
{
	private MotionProfile motionProfile;
	
	public RobotArmMotionProfileStart(WaypointList waypoints) {
		requires(RobotMain.robotArm);
		MotionProfile motionProfile = new MotionProfile(waypoints);
		motionProfile.calculatePath();
	}
	
	@Override
	protected void initialize() {
		RobotMain.robotArm.startMotionProfile(motionProfile.getProfile());
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return RobotMain.robotArm.getPIDController().isEnable();
	}

	@Override
	protected void end() {
		
	}

	@Override
	protected void interrupted() {
		
	}
}
