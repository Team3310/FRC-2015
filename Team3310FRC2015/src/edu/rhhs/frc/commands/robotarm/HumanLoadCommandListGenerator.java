package edu.rhhs.frc.commands.robotarm;

import java.util.ArrayList;

import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.MotionProfile.ProfileMode;
import edu.rhhs.frc.utility.motionprofile.WaypointList;

public class HumanLoadCommandListGenerator extends RobotArmCommandListGenerator 
{
	public enum StackPriority {VERTICAL, HORIZONTAL};

	// Fork gripper
//	public static final double[] HUMAN_LOAD_START_COORD =  {-32.6, -35.6, 22.8, -129}; 
//	public static final double[] HUMAN_LOAD_FINISH_COORD = {-25.8, -28.8, 22.8, -129}; 

//	public static final double[] LEFT_POSITION_BUILD_STACK_RELEASE_COORD = {29, -32, 11, 0.0};
//	public static final double[] LEFT_POSITION_MOVE_STACK_RELEASE_COORD = {29, 40, 11, 0.0};

//	public static final double[] LEFT_POSITION_BUILD_STACK_RELEASE_COORD = {29, -32, 11, 0.0};
//	public static final double[] LEFT_POSITION_MOVE_STACK_RELEASE_COORD = {29, 40, 11, 0.0};
//	
//	public static final double STACK_DELTA_Y_SPACING = 0.0; 
//	public static final double STACK_DELTA_Z_SPACING = 12.0; 
//	
//	public static final double MOVE_STACK_DELTA_Y_SPACING = -16.0; 
//	
//	public static final double STACK_X_PRE_UNLOAD_OFFSET = -18;   
//	public static final double STACK_Y_PRE_UNLOAD_OFFSET = 0;   
//	public static final double STACK_Z_PRE_UNLOAD_OFFSET = 7; 
//	
//	public static final double STACK_X_POST_UNLOAD_OFFSET = -20;   
//	public static final double STACK_Y_POST_UNLOAD_OFFSET = 0;   
//	public static final double STACK_Z_POST_UNLOAD_OFFSET = -4; 

	// Original gripper
	public static final double[] DEFAULT_HOME_COORD =  {28.779, 0.0, 11, 0};  // {-39.577, -39.187, 22.961, -129}; 
	public static final double[] HOME_STACK_CLEAR_COORD =  {24, 0.0, 32, 0};  // {-39.577, -39.187, 22.961, -129}; 
	public static final double[] HOME_STACK_EXIT_COORD =  {23, 0.0, 20, 0};  // {-39.577, -39.187, 22.961, -129}; 

	public ArrayList<double[]> stackStartPositions = new ArrayList<double[]>();
	public ArrayList<double[]> stackOffsetPositions = new ArrayList<double[]>();
	
	public static final double STACK_DELTA_Y_SPACING = 0.0; 
	public static final double STACK_DELTA_Z_SPACING = 24.0; 
	
	public static final double STACK_X_PRE_UNLOAD_OFFSET = 0;   
	public static final double STACK_Y_PRE_UNLOAD_OFFSET = 0;   
	public static final double STACK_Z_PRE_UNLOAD_OFFSET = 6; 
	
	public static final double STACK_X_POST_UNLOAD_OFFSET = 0;   
	public static final double STACK_Y_POST_UNLOAD_OFFSET = 0;   
	public static final double STACK_Z_POST_UNLOAD_OFFSET = 18; 

	public boolean debug = false;
	
	private StackPriority stackPriority = StackPriority.VERTICAL;
	private int numStacks = 1;
	private int maxStacks = 3;
	private int numTotesPerStack = 6;
	private double[] homePosition = DEFAULT_HOME_COORD;
	private double[] worldToRobotOffsetInches = {0, 0, 0};
	
	public HumanLoadCommandListGenerator() {	
		stackStartPositions.add(new double[] {-48, 21, 11, 0});
		stackOffsetPositions.add(new double[] {-35, 36, 0, 0});

		stackStartPositions.add(new double[]  {-26, 40, 11, 40});
		stackOffsetPositions.add(new double[] {-1, 40, 0, 0});

		stackStartPositions.add(new double[]  {-3, 51, 11, 0});
		stackOffsetPositions.add(new double[] {17, 34, 0, 0});
	
		maxStacks = 3;
	}

	public HumanLoadCommandListGenerator(StackPriority stackPriority, int numStacks, int numTotesPerStack) {
		this();
		this.stackPriority = stackPriority;
		this.numStacks = numStacks;
		this.numTotesPerStack = numTotesPerStack / 2;
	}
	
	private synchronized double[] getHome() {
		return homePosition;
	}

	private synchronized void setHome(double[] homeXYZInchesGammaDeg) {
		this.homePosition[0] = homeXYZInchesGammaDeg[0];
		this.homePosition[1] = homeXYZInchesGammaDeg[1];
		this.homePosition[2] = homeXYZInchesGammaDeg[2];
		this.homePosition[3] = homeXYZInchesGammaDeg[3];
	}

	public synchronized double[] getWorldToRobotOffsetInches() {
		return worldToRobotOffsetInches;
	}
	
	public synchronized void updateWorldToRobotOffset(double[] nominalWorldPosition, double[] currentRobotPosition, double worldToRobotAngleDeg) {
		MotionProfile motionProfile = new MotionProfile();
		double[] currentWorldPosition = motionProfile.xformRobotToWorld(currentRobotPosition, worldToRobotAngleDeg, MotionProfile.ZERO_OFFSET);
		worldToRobotOffsetInches[0] = nominalWorldPosition[0] - currentWorldPosition[0];
		worldToRobotOffsetInches[1] = nominalWorldPosition[1] - currentWorldPosition[1];
		worldToRobotOffsetInches[2] = nominalWorldPosition[2] - currentWorldPosition[2];
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
		
		commandList.clear();
		
		// Go from the current location to human station
    	WaypointList waypointsGoHome = new WaypointList(MotionProfile.ProfileMode.CartesianInputJointMotion);
    	waypointsGoHome.addWaypoint(HOME_STACK_CLEAR_COORD);
    	waypointsGoHome.addWaypoint(homePosition);
    	addMotionProfileCurrentToPathCommand(waypointsGoHome);
				
		for (int j = 0; j < secondaryCount; j++) {
			int stackId = maxStacks - secondaryCount + j;
		   	double[] toteReleasePosition = stackStartPositions.get(stackId); 
		   	double[] toteOffsetPosition = stackOffsetPositions.get(stackId); 
			
			for (int i = 0; i < primaryCount; i++) {
				if (debug) {
					System.out.println("Tote position, primary = " + i + ", secondary = " + j + " (in) = " + toteReleasePosition[0] + ", " + toteReleasePosition[1] + ", " + toteReleasePosition[2]);
				}
				
				// Wait for tote detection
				addWaitForNextCommand();
				
				// Grab tote
				addToteGrabberCloseCommand();
				
				// Pull away from human load station
				WaypointList waypointsHumanToStack = new WaypointList(ProfileMode.CartesianInputJointMotion);	
		    	waypointsHumanToStack.addWaypoint(homePosition);
		    	waypointsHumanToStack.addWaypoint(HOME_STACK_EXIT_COORD);
		    	
		    	// Move tote to offset position
		    	double[] totePreUnloadPosition = addPositionOffset(toteReleasePosition, STACK_X_PRE_UNLOAD_OFFSET, STACK_Y_PRE_UNLOAD_OFFSET, STACK_Z_PRE_UNLOAD_OFFSET, 0);
		    	double[] toteGetToHeightPosition = new double[] {toteOffsetPosition[0], toteOffsetPosition[1], totePreUnloadPosition[2],  0};
			    waypointsHumanToStack.addWaypoint(toteGetToHeightPosition);
		    	
				// Move to the pre-unload position
		    	waypointsHumanToStack.addWaypoint(totePreUnloadPosition);

		    	// Move to unload position
		    	waypointsHumanToStack.addWaypoint(toteReleasePosition);
		    	addMotionProfileCommand(waypointsHumanToStack);
				
		    	// Wait for release ok
				addWaitForNextCommand();
				
				// Release tote
				addToteGrabberOpenCommand();
				
				// Joint motion back to human load station
				WaypointList waypointsReturnToHuman = new WaypointList(ProfileMode.CartesianInputJointMotion);				
				waypointsReturnToHuman.addWaypoint(toteReleasePosition);

				// Move staight up to post-unload position
				double[] totePostUnloadPosition = addPositionOffset(toteReleasePosition, STACK_X_POST_UNLOAD_OFFSET, STACK_Y_POST_UNLOAD_OFFSET, STACK_Z_POST_UNLOAD_OFFSET, 0);
				waypointsReturnToHuman.addWaypoint(totePostUnloadPosition);
				
				// Move over to the offset position at the post-unload height
		    	double[] totePostUnloadOffsetPosition = new double[] {toteOffsetPosition[0], toteOffsetPosition[1], totePostUnloadPosition[2],  0};
		    	waypointsReturnToHuman.addWaypoint(totePostUnloadOffsetPosition);

			    // Move above the new stack at the human station
		    	waypointsReturnToHuman.addWaypoint(HOME_STACK_CLEAR_COORD);
		    	
		    	// Finally move to the home position
			    waypointsReturnToHuman.addWaypoint(homePosition);
		    	addMotionProfileCommand(waypointsReturnToHuman);
				
		    	toteReleasePosition = incrementPrimaryTotePosition(toteReleasePosition);
			}
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
		this.numTotesPerStack = numTotesPerStack / 2;
	}
	
    public static void main(String[] args) {
    	HumanLoadCommandListGenerator humanLoad = new HumanLoadCommandListGenerator(StackPriority.VERTICAL, 3, 6);
    	humanLoad.debug = true;
    	humanLoad.calculate();
    }
}