package edu.rhhs.frc.commands.robotarm;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.RobotArm;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.MotionProfile.ProfileMode;
import edu.rhhs.frc.utility.motionprofile.WaypointList;

public class RobotArmMotionProfileCurrentToPath extends RobotArmMotionProfilePath 
{
	private static final double CLOSE_DELTA_ANGLE_DEG = 2.0;
	private static final double CLOSE_DELTA_POSITION_IN = 2.0;
	protected double[] jointVelocities; 
	protected double[] jointPercentVelocities; 
	protected double[] endTypeCnt; 
	
    public RobotArmMotionProfileCurrentToPath(WaypointList waypoints, double[] jointVelocities, double[] jointPercentVelocities, double[] endTypeCnt) {
       	super(null);
    	this.waypoints = waypoints;
       	this.jointVelocities = jointVelocities;
       	this.jointPercentVelocities = jointPercentVelocities;
       	this.endTypeCnt = endTypeCnt;
    }

    public RobotArmMotionProfileCurrentToPath(WaypointList waypoints) {
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
         	
         	// If we are sitting at master need to move out
//         	if (isCartesianClose(currentXYZTool[0], RobotArm.X_MASTER_POSITION_IN) &&
//         			isCartesianClose(currentXYZTool[1], RobotArm.Y_MASTER_POSITION_IN) &&
//         			isCartesianClose(currentXYZTool[2], RobotArm.Z_MASTER_POSITION_IN)) {
//         		double[] master = new double[] {RobotArm.X_MASTER_POSITION_IN, RobotArm.Y_MASTER_POSITION_IN, RobotArm.Z_MASTER_POSITION_IN, RobotArm.GAMMA_MASTER_ANGLE_DEG};
//         		double[] masterClearance = RobotArmCommandListGenerator.addPositionOffset(master, 4, 0, 4, 0);
//         		waypoints.insertWaypoint(masterClearance, 1);   
//         	}
    	}
    	else {
        	// Insert the current point as the first point
         	waypoints.insertWaypoint(jointAngles, 0);   
         	
         	// If we are sitting at master need to move out
//         	if (isAngleClose(jointAngles[1], RobotArm.J2_MASTER_ANGLE_DEG) &&
//         			isAngleClose(jointAngles[2], RobotArm.J3_MASTER_ANGLE_DEG)) {
//         		double[] master = new double[] {RobotArm.J1_MASTER_ANGLE_DEG, RobotArm.J2_MASTER_ANGLE_DEG, RobotArm.J3_MASTER_ANGLE_DEG, RobotArm.J4_MASTER_ANGLE_DEG};
//         		double[] masterClearance = RobotArmCommandListGenerator.addPositionOffset(master, 0, -10, 10, 0);
//         		waypoints.insertWaypoint(masterClearance, 1);   
//         	}
    	}
    	
    	motionProfile = new MotionProfile(waypoints);
    	if (jointVelocities != null) {
    		motionProfile.setJointVelocities(jointVelocities);
    	}
    	if (jointPercentVelocities != null) {
    		motionProfile.setJointVelocityPercents(jointPercentVelocities);
    	}
    	if (endTypeCnt != null) {
    		motionProfile.setEndTypeCNT(endTypeCnt);
    	}
		motionProfile.calculatePath(false, RobotArm.OUTER_LOOP_UPDATE_RATE_MS, RobotMain.driveTrain.getYawAngleDeg(), RobotMain.commandListGenerator.getWorldToRobotOffsetInches());
    	profileOutput = motionProfile.getProfile();

    	if (profileOutput == null) {
    		System.out.println("Error calculating path for RobotArmMotionProfileCurrentToPath");
    		isFinished = true;
    		return;
    	}
    	currentProfileIndex = 0;
    	isFinished = false;
    }
    
    private boolean isAngleClose(double angle1, double angle2) {
    	return Math.abs(angle1 - angle2) < CLOSE_DELTA_ANGLE_DEG;
    }
    
    private boolean isCartesianClose(double pos1, double pos2) {
    	return Math.abs(pos1 - pos2) < CLOSE_DELTA_POSITION_IN;
    }
}