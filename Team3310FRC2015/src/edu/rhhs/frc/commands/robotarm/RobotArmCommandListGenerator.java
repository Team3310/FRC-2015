package edu.rhhs.frc.commands.robotarm;

import edu.rhhs.frc.subsystems.RobotArm.ToteGrabberPosition;
import edu.rhhs.frc.utility.motionprofile.WaypointList;


public abstract class RobotArmCommandListGenerator {

	protected RobotArmCommandList commandList = new RobotArmCommandList();
	
	public abstract void calculate();
	
	protected void addMotionProfileCommand(WaypointList waypoints) {
    	RobotArmMotionProfilePath motionProfile = new RobotArmMotionProfilePath(waypoints);
		commandList.add(motionProfile);
	}
	
	protected void addToteGrabberAutoCloseCommand() {
		commandList.add(new RobotArmToteGrabberAutoClose());
	}

	protected void addToteGrabberCloseCommand() {
		commandList.add(new RobotArmToteGrabberSetPosition(ToteGrabberPosition.CLOSED));
	}

	protected void addToteGrabberOpenCommand() {
		commandList.add(new RobotArmToteGrabberSetPosition(ToteGrabberPosition.OPEN));
	}

	protected double[] addPositionOffset(double[] waypoint, double deltaX, double deltaY, double deltaZ, double deltaToolAngle) {
		double[] position = new double[4];
		position[0] = waypoint[0] + deltaX;
		position[1] = waypoint[1] + deltaY;
		position[2] = waypoint[2] + deltaZ;
		position[3] = waypoint[3] + deltaToolAngle;
		return position;
	}

	public RobotArmCommandList getCommandList() {
		return commandList;
	}

}
