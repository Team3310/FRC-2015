package edu.rhhs.frc.commands.robotarm;

import edu.rhhs.frc.utility.motionprofile.MotionProfile.ProfileMode;
import edu.rhhs.frc.utility.motionprofile.WaypointList;

public class HumanLoadCommandListGenerator extends RobotArmCommandListGenerator {

	public enum StackPriority {VERTICAL, HORIZONTAL};

	public static final double[] HUMAN_LOAD_START_COORD =  {-39.577, -39.187, 22.961, -129}; 
	public static final double[] HUMAN_LOAD_FINISH_COORD = {-29.556, -25.889, 16.0, -129}; 
	public static final double[] LEFT_STACK_UNLOAD_COORD = { 29, -18, 11, 0.0};
	
	public static final double STACK_DELTA_Y_SPACING = -17.0; 
	public static final double STACK_DELTA_Z_SPACING = 12.0; 
	
	public static final double STACK_X_PRE_UNLOAD_OFFSET = 0;   
	public static final double STACK_Y_PRE_UNLOAD_OFFSET = 0;   
	public static final double STACK_Z_PRE_UNLOAD_OFFSET = 4; 
	
	public static final double STACK_X_POST_UNLOAD_OFFSET = -6;   
	public static final double STACK_Y_POST_UNLOAD_OFFSET = 0;   
	public static final double STACK_Z_POST_UNLOAD_OFFSET = 0; 

	public boolean debug = false;
	
	private StackPriority stackPriority = StackPriority.VERTICAL;
	private int numStacks = 1;
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
    	double[] toteReleasePosition = LEFT_STACK_UNLOAD_COORD;   	
		
		for (int j = 0; j < secondaryCount; j++) {
			
			for (int i = 0; i < primaryCount; i++) {
				if (debug) {
					System.out.println("Tote position, primary = " + i + ", secondary = " + j + " (in) = " + toteReleasePosition[0] + ", " + toteReleasePosition[1] + ", " + toteReleasePosition[2]);
				}
				
				// Wait for tote detection
//				addToteGrabberAutoCloseCommand();
				addWaitForNextCommand();
				
				// Pull away from human load station
				WaypointList waypointsHumanToStack = new WaypointList(ProfileMode.CartesianInputJointMotion);	
		    	waypointsHumanToStack.addWaypoint(HUMAN_LOAD_START_COORD);
		    	waypointsHumanToStack.addWaypoint(HUMAN_LOAD_FINISH_COORD);

		    	// Move up
		    	double[] totePreUnloadPosition = addPositionOffset(toteReleasePosition, STACK_X_PRE_UNLOAD_OFFSET, STACK_Y_PRE_UNLOAD_OFFSET, STACK_Z_PRE_UNLOAD_OFFSET, 0);
		    	double[] totePreUnloadVerticalPosition = new double[] {HUMAN_LOAD_FINISH_COORD[0], HUMAN_LOAD_FINISH_COORD[1], totePreUnloadPosition[2], HUMAN_LOAD_FINISH_COORD[3]};
		    	waypointsHumanToStack.addWaypoint(totePreUnloadVerticalPosition);

				// Move to the pre-unload position
		    	waypointsHumanToStack.addWaypoint(totePreUnloadPosition);
				
				// Move to the final unload position
		    	waypointsHumanToStack.addWaypoint(toteReleasePosition);
		    	addMotionProfileCommand(waypointsHumanToStack);
				
				// Release tote
				addToteGrabberOpenCommand();
				
				// Joint motion back to human load station
				WaypointList waypointsReturnToHuman = new WaypointList(ProfileMode.CartesianInputJointMotion);				
				waypointsReturnToHuman.addWaypoint(toteReleasePosition);

				// Move to post-unload position
				double[] totePostUnloadPosition = addPositionOffset(toteReleasePosition, STACK_X_POST_UNLOAD_OFFSET, STACK_Y_POST_UNLOAD_OFFSET, STACK_Z_POST_UNLOAD_OFFSET, 0);
				waypointsReturnToHuman.addWaypoint(totePostUnloadPosition);
				
				waypointsReturnToHuman.addWaypoint(HUMAN_LOAD_START_COORD);
		    	addMotionProfileCommand(waypointsReturnToHuman);
				
		    	toteReleasePosition = incrementPrimaryTotePosition(toteReleasePosition);
			}
			toteReleasePosition = incrementSecondaryTotePosition(toteReleasePosition);
		}
	}
	
	private double[] incrementPrimaryTotePosition(double[] waypoint) {
		double[] output;
		if (stackPriority == StackPriority.VERTICAL) {
			output = addPositionOffset(waypoint, 0, 0, STACK_DELTA_Z_SPACING, 0);
		}
		else {
			output = addPositionOffset(waypoint, 0, STACK_DELTA_Y_SPACING, 0, 0);
		}
		return output;
	}

	private double[] incrementSecondaryTotePosition(double[] waypoint) {
		double[] output;
		if (stackPriority == StackPriority.VERTICAL) {
			waypoint[2] = LEFT_STACK_UNLOAD_COORD[2];  // Reset Z
			output = addPositionOffset(waypoint, 0, STACK_DELTA_Y_SPACING, 0, 0);
		}
		else {
			waypoint[1] = LEFT_STACK_UNLOAD_COORD[1];  // Reset Y
			output = addPositionOffset(waypoint, 0, 0, STACK_DELTA_Z_SPACING, 0);
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
    	HumanLoadCommandListGenerator humanLoad = new HumanLoadCommandListGenerator(StackPriority.VERTICAL, 1, 6);
    	humanLoad.debug = true;
    	humanLoad.calculate();
    }
}