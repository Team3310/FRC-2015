package edu.rhhs.frc.commands.robotarm;

import edu.rhhs.frc.utility.motionprofile.MotionProfile.ProfileMode;
import edu.rhhs.frc.utility.motionprofile.WaypointList;

public class HumanLoadCommandListGenerator extends RobotArmCommandListGenerator {

	public enum StackPriority {VERTICAL, HORIZONTAL};

	public static final double[] HUMAN_LOAD_START_COORD =  {-1.182, -0.756, -0.203,0}; 
	public static final double[] HUMAN_LOAD_FINISH_COORD = {-0.748, -0.168, -0.203,0}; 
	public static final double[] LEFT_STACK_START_COORD =  { 0.775,  1.007, -0.625,0}; 
	public static final double STACK_Y_SPACING = -0.483; 
	public static final double STACK_Z_SPACING = 0.317; 
	public static final double STACK_Z_RELEASE_OFFSET = 0.050; 
	
	public boolean debug = false;
	
	private StackPriority stackPriority = StackPriority.VERTICAL;
	private int numStacks = 5;
	private int numTotesPerStack = 6;
	
	public HumanLoadCommandListGenerator() {		
	}

	public HumanLoadCommandListGenerator(StackPriority stackPriority, int numStacks, int numTotesPerStack) {
		this.stackPriority = stackPriority;
		this.numStacks = numStacks;
		this.numTotesPerStack = numTotesPerStack;
	}
	
	public void calculate() {
		int primaryCount = 0;
		int secondaryCount = 0;
		if (stackPriority == StackPriority.VERTICAL) {
			primaryCount = numTotesPerStack;
			secondaryCount = numStacks;
		}
		else {
			primaryCount = numStacks;
			secondaryCount = numTotesPerStack;
		}
		
		// Initialize to position of first tote
    	double[] toteReleasePosition = LEFT_STACK_START_COORD;   	
		
		for (int j = 0; j < secondaryCount; j++) {
			
			for (int i = 0; i < primaryCount; i++) {
				if (debug) {
					System.out.println("Tote position (mm) = " + toteReleasePosition[0] * 1000 + ", " + toteReleasePosition[1] * 1000 + ", " + toteReleasePosition[2] * 1000);
				}
				
				// Wait for tote detection
				addToteGrabberAutoCloseCommand();
				
				// Pull away from human load station
				WaypointList waypoints = new WaypointList(ProfileMode.CartesianInputJointMotion);	
		    	waypoints.addWaypoint(HUMAN_LOAD_START_COORD);
		    	waypoints.addWaypoint(HUMAN_LOAD_FINISH_COORD);

				// Move above tote unload position
		    	double[] toteAboveReleasePosition = addPositionOffset(toteReleasePosition, 0, 0, STACK_Z_RELEASE_OFFSET, 0);
		    	waypoints.addWaypoint(toteAboveReleasePosition);
				
				// Move down to place tote on stack
		    	waypoints.addWaypoint(toteReleasePosition);
		    	addMotionProfileCommand(waypoints);
				
				// Release tote
				addToteGrabberOpenCommand();
				
				// Joint motion back to human load station
				WaypointList waypointsReturn = new WaypointList(ProfileMode.CartesianInputJointMotion);				
				waypointsReturn.addWaypoint(toteReleasePosition);
				waypointsReturn.addWaypoint(HUMAN_LOAD_START_COORD);
		    	addMotionProfileCommand(waypointsReturn);
				
		    	toteReleasePosition = incrementPrimaryTotePosition(toteReleasePosition);
			}
			toteReleasePosition = incrementSecondaryTotePosition(toteReleasePosition);
		}
	}
	
	private double[] incrementPrimaryTotePosition(double[] waypoint) {
		double[] output;
		if (stackPriority == StackPriority.VERTICAL) {
			output = addPositionOffset(waypoint, 0, 0, STACK_Z_SPACING, 0);
		}
		else {
			output = addPositionOffset(waypoint, 0, STACK_Y_SPACING, 0, 0);
		}
		return output;
	}

	private double[] incrementSecondaryTotePosition(double[] waypoint) {
		double[] output;
		if (stackPriority == StackPriority.VERTICAL) {
			waypoint[2] = LEFT_STACK_START_COORD[2];  // Reset Z
			output = addPositionOffset(waypoint, 0, STACK_Y_SPACING, 0, 0);
		}
		else {
			waypoint[1] = LEFT_STACK_START_COORD[1];  // Reset Y
			output = addPositionOffset(waypoint, 0, 0, STACK_Z_SPACING, 0);
		}
		return output;
	}

	public StackPriority getStackPriority() {
		return stackPriority;
	}

	public void setStackPriority(StackPriority stackPriority) {
		this.stackPriority = stackPriority;
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
	
    public static void main(String[] args) {
    	HumanLoadCommandListGenerator humanLoad = new HumanLoadCommandListGenerator(StackPriority.HORIZONTAL, 5, 6);
    	humanLoad.debug = true;
    	humanLoad.calculate();
    }
}