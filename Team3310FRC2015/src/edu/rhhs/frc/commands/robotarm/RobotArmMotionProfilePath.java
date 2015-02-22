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
public class RobotArmMotionProfilePath extends RobotArmCommand {

	private ProfileOutput profileOutput;
	private int currentProfileIndex;
	private boolean isFinished;
	private double[] goFromCurrentToThisPoint = null;
	private MotionProfile.ProfileMode profileMode;
	
    public RobotArmMotionProfilePath(WaypointList waypoints) {
    	MotionProfile motionProfile = new MotionProfile(waypoints);
		motionProfile.calculatePath();
		profileOutput = motionProfile.getProfile();
    }

    public RobotArmMotionProfilePath(double[] goFromCurrentToThisPoint, MotionProfile.ProfileMode profileMode) {
    	this.goFromCurrentToThisPoint = goFromCurrentToThisPoint;
    	this.profileMode = profileMode;
    }

    protected void initialize() {
    	if (goFromCurrentToThisPoint != null) {
        	MotionProfile motionProfile = new MotionProfile();
        	double[] currentXYZTool = motionProfile.calcForwardKinematicsRad(RobotMain.robotArm.getJointAngles());

        	WaypointList waypoints = new WaypointList(profileMode);
        	waypoints.addWaypoint(currentXYZTool);
        	waypoints.addWaypoint(goFromCurrentToThisPoint);
    		
        	motionProfile = new MotionProfile(waypoints);
    		motionProfile.calculatePath();
    		profileOutput = motionProfile.getProfile();
    	}
    	
    	currentProfileIndex = 0;
    	isFinished = false;
    }

    protected void execute() {
		if (currentProfileIndex >= profileOutput.jointPos.length) {
			
			// Make sure we hit the last point on the profile
			if (currentProfileIndex - profileOutput.jointPos.length + 1 < RobotArm.OUTER_LOOP_UPDATE_RATE_MS) {
				currentProfileIndex = profileOutput.jointPos.length - 1;
			}
			else {
				isFinished = true;
				return;
			}
		}

		double[] jointAngles = profileOutput.jointPos[currentProfileIndex];
		RobotMain.robotArm.setJointAngles(jointAngles);	

		currentProfileIndex += RobotArm.OUTER_LOOP_UPDATE_RATE_MS;
    }

    protected boolean isFinished() {
        return isFinished;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
