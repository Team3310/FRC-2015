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
    	double[] jointAngles = RobotMain.robotArm.getJointAngles();
    	
    	WaypointList waypoints = new WaypointList(ProfileMode.JointInputJointMotion);
    	waypoints.addWaypoint(jointAngles);
    	waypoints.addWaypoint(0, jointAngles[1], jointAngles[2], jointAngles[3]);
    	
    	MotionProfile motionProfile = new MotionProfile();
    	motionProfile = new MotionProfile(waypoints);
		motionProfile.calculatePath(false, RobotArm.OUTER_LOOP_UPDATE_RATE_MS, 0, MotionProfile.ZERO_OFFSET);
    	profileOutput = motionProfile.getProfile();

    	currentProfileIndex = 0;
    	isFinished = false;
    }
}