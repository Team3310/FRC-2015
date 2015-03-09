package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.commands.robotarm.HumanLoadCommandListGenerator;
import edu.rhhs.frc.commands.robotarm.RobotArmCommandList;
import edu.rhhs.frc.commands.robotarm.RobotArmMotionProfileCurrentToPath;
import edu.rhhs.frc.commands.robotarm.RobotArmMotionProfileCurrentToPathNoCorrection;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.WaypointList;
import edu.wpi.first.wpilibj.command.Command;

public class RobotArmMotionProfileCurrentToPosition extends Command 
{
	protected RobotArmCommandList commandList;
	
	public RobotArmMotionProfileCurrentToPosition(double x, double y, double z, MotionProfile.ProfileMode profileMode) { 	
		WaypointList waypoints = new WaypointList(profileMode);
    	waypoints.addWaypoint(x, y, z, 0);
    	
    	commandList = new RobotArmCommandList();
    	commandList.add(new RobotArmMotionProfileCurrentToPathNoCorrection(waypoints));

    	requires(RobotMain.robotArm);
	}
	
	@Override
	protected void initialize() {
		RobotMain.robotArm.startRobotArmCommandList(commandList);
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return RobotMain.robotArm.isControlLoopEnabled();
	}

	@Override
	protected void end() {
		
	}

	@Override
	protected void interrupted() {
		
	}
}
