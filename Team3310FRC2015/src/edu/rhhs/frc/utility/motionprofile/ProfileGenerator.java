package edu.rhhs.frc.utility.motionprofile;

import java.util.ArrayList;

import edu.rhhs.frc.utility.motionprofile.MotionProfile.ProfileMode;

public class ProfileGenerator {

	public enum StackPriority {VERTICAL, HORIZONTAL};
	public enum StackDirection {LEFT_TO_RIGHT, RIGHT_TO_LEFT};

	public static final double[] HUMAN_LOAD_START_COORDS = {500,500,500,0}; 
	public static final double[] HUMAN_LOAD_FINISH_COORDS = {600,600,500,0}; 
	
	private StackPriority stackPriority = StackPriority.HORIZONTAL;
	private StackDirection stackDirection = StackDirection.LEFT_TO_RIGHT;
	private int numStacks = 5;
	private int numTotesPerStack = 6;
	private ArrayList<ProfileOutput> profiles = new ArrayList<ProfileOutput>();
	
	public ProfileGenerator() {
		
	}
	
	public void calculate() {
		int primaryCount = 0;
		int secondaryCount = 0;
		if (stackPriority == StackPriority.HORIZONTAL) {
			primaryCount = numTotesPerStack;
			secondaryCount = numStacks;
		}
		else {
			primaryCount = numStacks;
			secondaryCount = numTotesPerStack;
		}
		
		// Initialize to position of first tote
    	double[] toteStackPosition = initializeFirstTotePosition();   	
		
		for (int j = 0; j < secondaryCount; j++) {
			
			for (int i = 0; i < primaryCount; i++) {
				// Wait for tote detection
				
				
				// Pull linearly away from human load station
				WaypointList waypoints1 = new WaypointList(ProfileMode.CartesianInputLinearMotion);	
		    	waypoints1.addWaypoint(HUMAN_LOAD_START_COORDS);
		    	waypoints1.addWaypoint(HUMAN_LOAD_FINISH_COORDS);
				MotionProfile motionProfile1 = new MotionProfile(waypoints1);
				profiles.add(motionProfile1.getProfile());

				// Joint motion to the tote unload position
				WaypointList waypoints2 = new WaypointList(ProfileMode.CartesianInputJointMotion);				
		    	waypoints2.addWaypoint(HUMAN_LOAD_FINISH_COORDS);
		    	
		    	waypoints2.addWaypoint(toteStackPosition);
				MotionProfile motionProfile2 = new MotionProfile(waypoints2);
				profiles.add(motionProfile2.getProfile());
				
				// Move linearly down to place tote on stack
				WaypointList waypoints3 = new WaypointList(ProfileMode.CartesianInputLinearMotion);	
		    	waypoints3.addWaypoint(toteStackPosition);
  	
		    	double[] toteReleasePosition = addPositionOffset(toteStackPosition, 0, 0, -30, 0);
		    	waypoints3.addWaypoint(toteReleasePosition);
				MotionProfile motionProfile3 = new MotionProfile(waypoints3);
				profiles.add(motionProfile3.getProfile());
				
				// Release tote
				
				
				// Joint motion back to human load station
				WaypointList waypoints4 = new WaypointList(ProfileMode.CartesianInputJointMotion);				
		    	waypoints4.addWaypoint(toteReleasePosition);
		    	waypoints4.addWaypoint(HUMAN_LOAD_START_COORDS);
				MotionProfile motionProfile4 = new MotionProfile(waypoints4);
				profiles.add(motionProfile4.getProfile());	
				
				incrementPrimaryTotePosition(toteStackPosition);
			}
			incrementSecondaryTotePosition(toteStackPosition);
		}
	}
	
	private double[] addPositionOffset(double[] waypoint, double deltaX, double deltaY, double deltaZ, double deltaToolAngle) {
		double[] position = new double[4];
		position[0] = waypoint[0] + deltaX;
		position[1] = waypoint[1] + deltaY;
		position[2] = waypoint[2] + deltaZ;
		position[3] = waypoint[3] + deltaToolAngle;
		return position;
	}

	private double[] initializeFirstTotePosition() {
		double[] position = new double[4];
		return position;
	}

	private void incrementPrimaryTotePosition(double[] waypoint) {
		return;
	}

	private void incrementSecondaryTotePosition(double[] waypoint) {
		return;
	}

	public StackPriority getStackPriority() {
		return stackPriority;
	}

	public void setStackPriority(StackPriority stackPriority) {
		this.stackPriority = stackPriority;
	}

	public StackDirection getStackDirection() {
		return stackDirection;
	}

	public void setStackDirection(StackDirection stackDirection) {
		this.stackDirection = stackDirection;
	}

	public int getNumStacks() {
		return numStacks;
	}

	public void setNumStacks(int numStacks) {
		this.numStacks = numStacks;
	}

	public int getNumTotesPerStack() {
		return numTotesPerStack;
	}

	public void setNumTotesPerStack(int numTotesPerStack) {
		this.numTotesPerStack = numTotesPerStack;
	}

	public ArrayList<ProfileOutput> getProfiles() {
		return profiles;
	}

}
