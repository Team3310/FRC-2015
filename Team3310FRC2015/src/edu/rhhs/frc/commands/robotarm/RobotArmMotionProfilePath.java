package edu.rhhs.frc.commands.robotarm;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.RobotArm;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.ProfileOutput;
import edu.rhhs.frc.utility.motionprofile.WaypointList;

/**
 *
 */
public class RobotArmMotionProfilePath extends RobotArmCommand {

	protected ProfileOutput profileOutput;
	protected int currentProfileIndex;
	protected boolean isFinished;
	protected WaypointList waypoints;
	protected double[] jointVelocities; 
	
    public RobotArmMotionProfilePath(WaypointList waypoints) {
    	this.waypoints = waypoints;
    }

    public RobotArmMotionProfilePath(WaypointList waypoints, double[] jointVelocities) {
       	this.waypoints = waypoints;
       	this.jointVelocities = jointVelocities;
    }
    
    protected void initialize() {    	
    	if (waypoints != null) {
	    	MotionProfile motionProfile = new MotionProfile(waypoints);
	    	if (jointVelocities != null) {
	    		motionProfile.setJointVelocities(jointVelocities);
	    	}
			motionProfile.calculatePath(false, RobotArm.OUTER_LOOP_UPDATE_RATE_MS, RobotMain.getYawAngleDeg(), RobotMain.commandListGenerator.getWorldToRobotOffsetInches());
			profileOutput = motionProfile.getProfile();
    	}
    	currentProfileIndex = 0;
    	isFinished = false;
    }

    protected void execute() {
		if (currentProfileIndex >= profileOutput.jointPos.length) {
			
//			// Make sure we hit the last point on the profile
//			if (currentProfileIndex - profileOutput.jointPos.length + 1 < RobotArm.OUTER_LOOP_UPDATE_RATE_MS) {
//				currentProfileIndex = profileOutput.jointPos.length - 1;
//			}
//			else {
//				isFinished = true;
//				return;
//			}
			
			isFinished = true;
			return;
		}

		double[] jointAngles = profileOutput.jointPos[currentProfileIndex];
		RobotMain.robotArm.setJointAngles(jointAngles);	

//		currentProfileIndex += RobotArm.OUTER_LOOP_UPDATE_RATE_MS;
		// We now only output the points at the controller rate (used to be every ms)
		currentProfileIndex++;
    }

    protected boolean isFinished() {
        return isFinished;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
