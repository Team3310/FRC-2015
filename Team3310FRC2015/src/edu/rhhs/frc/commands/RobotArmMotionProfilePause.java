package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.WaypointList;
import edu.wpi.first.wpilibj.command.Command;

public class RobotArmMotionProfilePause extends Command 
{
	public RobotArmMotionProfilePause() {
		requires(RobotMain.robotArm);
	}
	
	@Override
	protected void initialize() {
		RobotMain.robotArm.disable();
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
		
	}

	@Override
	protected void interrupted() {
		
	}
}
