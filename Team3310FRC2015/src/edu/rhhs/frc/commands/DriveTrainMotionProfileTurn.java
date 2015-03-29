package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.MotionProfile.ProfileMode;
import edu.rhhs.frc.utility.motionprofile.ProfileOutput;
import edu.rhhs.frc.utility.motionprofile.WaypointList;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainMotionProfileTurn extends Command 
{
	private ProfileOutput profileOutput;

    public DriveTrainMotionProfileTurn(double turnAngleDeg, double maxSpeedInchesPerSecond, boolean isTank) {
    	
    	WaypointList wayPoints = new WaypointList(ProfileMode.JointInputJointMotion);
    	wayPoints.addWaypoint(0, 0, 0, 0);
    	
    	if (isTank) {
        	double wheelDistanceInches = Math.abs(Math.toRadians(turnAngleDeg) * DriveTrain.TRACK_WIDTH_INCHES / 2);
	    	double leftWheelDistance = turnAngleDeg > 0 ? -wheelDistanceInches : wheelDistanceInches;
	    	double rightWheelDistance = turnAngleDeg > 0 ? wheelDistanceInches : -wheelDistanceInches;
	    	wayPoints.addWaypoint(leftWheelDistance, rightWheelDistance, 0, 0);
    	}
    	else {
        	double outerWheelDistanceInches = Math.abs(Math.toRadians(turnAngleDeg) * DriveTrain.TRACK_WIDTH_INCHES);
	    	double leftWheelDistance = turnAngleDeg > 0 ? 0 : outerWheelDistanceInches;
	    	double rightWheelDistance = turnAngleDeg > 0 ? outerWheelDistanceInches : 0;
	    	wayPoints.addWaypoint(leftWheelDistance, rightWheelDistance, 0, 0);
    	}

    	MotionProfile motionProfile = new MotionProfile(wayPoints);
    	motionProfile.setJointVelocities(new double[] {maxSpeedInchesPerSecond, maxSpeedInchesPerSecond, maxSpeedInchesPerSecond, maxSpeedInchesPerSecond});
    	motionProfile.setJointAccels(new double[] {0.8, 0.8, 0.8, 0.8});
    	motionProfile.setJointDecels(new double[] {0.4, 0.4, 0.4, 0.4});
		motionProfile.calculatePath(false, DriveTrain.OUTER_LOOP_UPDATE_RATE_MS);
    	profileOutput = motionProfile.getProfile();

    	requires(RobotMain.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	RobotMain.driveTrain.startMotionProfile(profileOutput);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return !RobotMain.driveTrain.isControlLoopEnabled(); 
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}