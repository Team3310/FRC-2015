package edu.rhhs.frc.commands.robotarm;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.RobotArm;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.ProfileOutput;
import edu.rhhs.frc.utility.motionprofile.WaypointList;

/**
 *
 */
public class RobotArmMotionProfileCurrentToPath extends RobotArmMotionProfilePath {

	private WaypointList waypoints;
	
    public RobotArmMotionProfileCurrentToPath(WaypointList waypoints) {
    	super(null);
    	this.waypoints = waypoints;
    }

    protected void initialize() {
    	// Need to calculate the path on the fly
    	MotionProfile motionProfile = new MotionProfile();
    	double[] currentXYZTool = motionProfile.calcForwardKinematicsRad(RobotMain.robotArm.getJointAngles());

    	// Insert the current point as the first point
     	waypoints.insertWaypoint(currentXYZTool, 0);
 		
    	motionProfile = new MotionProfile(waypoints);
		motionProfile.calculatePath();
    	profileOutput = motionProfile.getProfile();

    	super.initialize();
    }
}
