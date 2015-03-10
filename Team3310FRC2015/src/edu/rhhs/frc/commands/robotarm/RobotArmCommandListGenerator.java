package edu.rhhs.frc.commands.robotarm;

import edu.rhhs.frc.subsystems.RobotArm.ToteGrabberPosition;
import edu.rhhs.frc.utility.motionprofile.WaypointList;

public abstract class RobotArmCommandListGenerator 
{
	protected RobotArmCommandList commandList = new RobotArmCommandList();
	
	public abstract void calculate();
	
	protected void addMotionProfileCommand(WaypointList waypoints, double[] jointSpeeds) {
    	RobotArmMotionProfilePath motionProfile = new RobotArmMotionProfilePath(waypoints, jointSpeeds);
		commandList.add(motionProfile);
	}
	
	protected void addMotionProfileCommand(WaypointList waypoints) {
    	RobotArmMotionProfilePath motionProfile = new RobotArmMotionProfilePath(waypoints);
		commandList.add(motionProfile);
	}
	
	protected void addMotionProfileCurrentToPathCommand(WaypointList waypoints) {
    	RobotArmMotionProfileCurrentToPath motionProfile = new RobotArmMotionProfileCurrentToPath(waypoints);
		commandList.add(motionProfile);
	}
	
	protected void addToteGrabberAutoCloseCommand() {
		commandList.add(new RobotArmToteGrabberAutoClose());
	}

	protected void addWaitForNextCommand() {
		commandList.add(new RobotArmWaitForNext());
	}

	protected void addToteGrabberCloseCommand() {
		commandList.add(new RobotArmToteGrabberSetPosition(ToteGrabberPosition.CLOSE));
	}

	protected void addToteGrabberOpenCommand() {
		commandList.add(new RobotArmToteGrabberSetPosition(ToteGrabberPosition.OPEN));
	}

	public static double[] addPositionOffset(double[] waypoint, double deltaX, double deltaY, double deltaZ, double deltaToolAngle) {
		double[] position = new double[4];
		position[0] = waypoint[0] + deltaX;
		position[1] = waypoint[1] + deltaY;
		position[2] = waypoint[2] + deltaZ;
		position[3] = waypoint[3] + deltaToolAngle;
		return position;
	}

	public static double[] calcPositionOffset(double[] waypointToSubtractFrom, double[] waypointToSubtract) {
		double[] position = new double[4];
		position[0] = waypointToSubtractFrom[0] - waypointToSubtract[0];
		position[1] = waypointToSubtractFrom[1] - waypointToSubtract[1];
		position[2] = waypointToSubtractFrom[2] - waypointToSubtract[2];
		position[3] = waypointToSubtractFrom[3] - waypointToSubtract[3];
		return position;
	}

	public RobotArmCommandList getCommandList() {
		return commandList;
	}

}
