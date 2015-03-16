package edu.rhhs.frc.commands.robotarm;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.RobotArm;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.ProfileOutput;
import edu.rhhs.frc.utility.motionprofile.WaypointList;

public class RobotArmMotionProfilePath extends RobotArmCommand 
{
	protected ProfileOutput profileOutput;
	protected int currentProfileIndex;
	protected boolean isFinished;
	protected WaypointList waypoints;
	protected double[] jointVelocities; 
	protected double[] jointPercentVelocities; 
	protected double[] endTypeCnt; 
	
    public RobotArmMotionProfilePath(WaypointList waypoints) {
    	this.waypoints = waypoints;
    }

    public RobotArmMotionProfilePath(WaypointList waypoints, double[] jointVelocities) {
       	this.waypoints = waypoints;
       	this.jointVelocities = jointVelocities;
    }
    
    public RobotArmMotionProfilePath(WaypointList waypoints, double[] jointVelocities, double[] jointPercentVelocities) {
       	this.waypoints = waypoints;
       	this.jointVelocities = jointVelocities;
       	this.jointPercentVelocities = jointPercentVelocities;
    }
    
    public RobotArmMotionProfilePath(WaypointList waypoints, double[] jointVelocities, double[] jointPercentVelocities, double[] endTypeCnt) {
       	this.waypoints = waypoints;
       	this.jointVelocities = jointVelocities;
       	this.jointPercentVelocities = jointPercentVelocities;
       	this.endTypeCnt = endTypeCnt;
    }
    
    protected void initialize() {    	
    	if (waypoints != null) {
	    	MotionProfile motionProfile = new MotionProfile(waypoints);
	    	if (jointVelocities != null) {
	    		motionProfile.setJointVelocities(jointVelocities);
	    	}
	    	if (jointPercentVelocities != null) {
	    		motionProfile.setJointVelocityPercents(jointPercentVelocities);
	    	}
	    	if (endTypeCnt != null) {
	    		motionProfile.setEndTypeCNT(endTypeCnt);
	    	}
			motionProfile.calculatePath(false, RobotArm.OUTER_LOOP_UPDATE_RATE_MS, 0, RobotMain.commandListGenerator.getWorldToRobotOffsetInches());
			profileOutput = motionProfile.getProfile();
    	}
    	
    	if (profileOutput == null) {
    		System.out.println("Error calculating path for RobotArmMotionProfilePath");
    		isFinished = true;
    		return;
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
    
    public WaypointList getWaypoints() {
    	return waypoints;
    }
}