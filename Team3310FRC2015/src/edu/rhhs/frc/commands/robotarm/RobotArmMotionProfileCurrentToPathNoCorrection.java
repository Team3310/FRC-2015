package edu.rhhs.frc.commands.robotarm;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.RobotArm;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.MotionProfile.ProfileMode;
import edu.rhhs.frc.utility.motionprofile.WaypointList;

/**
 *
 */
public class RobotArmMotionProfileCurrentToPathNoCorrection extends RobotArmMotionProfilePath {

	private WaypointList waypoints;
	
    public RobotArmMotionProfileCurrentToPathNoCorrection(WaypointList waypoints) {
    	super(null);
    	this.waypoints = waypoints;
    }

    protected void initialize() {
    	// Need to calculate the path on the fly
    	MotionProfile motionProfile = new MotionProfile();
    	double[] jointAngles = RobotMain.robotArm.getJointAngles();
    	
    	if (waypoints.getProfileMode() != ProfileMode.JointInputJointMotion) {
        	// Do forward kinematics to get current cartesian coordinate
    		double[] currentXYZTool = motionProfile.calcForwardKinematicsDeg(jointAngles);
    		
        	// Insert the current point as the first point
         	waypoints.insertWaypoint(currentXYZTool, 0);
     	}
    	else {
        	// Insert the current point as the first point
         	waypoints.insertWaypoint(jointAngles, 0);   
       	
    	}
    	
    	motionProfile = new MotionProfile(waypoints);
		motionProfile.calculatePath(false, RobotArm.OUTER_LOOP_UPDATE_RATE_MS, 0, MotionProfile.ZERO_OFFSET);
    	profileOutput = motionProfile.getProfile();

    	currentProfileIndex = 0;
    	isFinished = false;
    }
}
