package edu.rhhs.frc.commands.robotarm;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.RobotArm;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.MotionProfile.ProfileMode;
import edu.rhhs.frc.utility.motionprofile.WaypointList;

public class RobotArmMotionProfileJ1ToZero extends RobotArmMotionProfilePath 
{
    public RobotArmMotionProfileJ1ToZero() {
    	super(null);
    }

    protected void initialize() {
    	// Need to calculate the path on the fly
    	MotionProfile motionProfile = new MotionProfile();
    	double[] jointAngles = RobotMain.robotArm.getJointAngles();
		double[] currentXYZTool = motionProfile.calcForwardKinematicsDeg(jointAngles);
   	
    	WaypointList waypoints = new WaypointList(ProfileMode.CartesianInputJointMotion);
    	waypoints.addWaypoint(currentXYZTool);
    	
    	if (currentXYZTool[0] < -10) {
    		waypoints.addWaypoint(new double[] { -11, 15, 50, 0});  // middle stack
    	}
    	else {
	    	waypoints.addWaypoint(new double[] { -2, 24, 50, 0});  // last stack
    	}
    	waypoints.addWaypoint(RobotArm.X_MIN_INCHES, 0, 45, 0);
    	
    	motionProfile = new MotionProfile(waypoints);
		motionProfile.calculatePath(false, RobotArm.OUTER_LOOP_UPDATE_RATE_MS, 0, MotionProfile.ZERO_OFFSET);
    	profileOutput = motionProfile.getProfile();

    	if (profileOutput == null) {
    		System.out.println("Error calculating path for RobotArmMotionProfileJ1ToZero");
    		isFinished = true;
    		return;
    	}
    	currentProfileIndex = 0;
    	isFinished = false;
    }
    
    protected void end() {
    }
}