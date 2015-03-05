package edu.rhhs.frc.commands.robotarm;

import java.util.ArrayList;

import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.MotionProfile.ProfileMode;
import edu.rhhs.frc.utility.motionprofile.WaypointList;

public class HumanLoadCommandListGenerator extends RobotArmCommandListGenerator {

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
	public static final double[] DEFAULT_HOME_COORD =  {-37.5, -40.0, 22.5, -125.0};  // {-39.577, -39.187, 22.961, -129}; 
	public static final double[] HOME_EXTRACT_TOTE_COORD = {-22.0, -28.0, 22.5, -125.0}; 

	public static final double[] LEFT_POSITION_BUILD_STACK_RELEASE_COORD = {35, -12, 11, 0.0};  // {29, -24, 11, 0.0}
	public static final double[] LEFT_POSITION_MOVE_STACK_RELEASE_COORD = {29, -8, 11, 0.0};

	public ArrayList<double[]> stackStartPositions = new ArrayList<double[]>();
	public ArrayList<double[]> stackOffsetPositions = new ArrayList<double[]>();
	
	public static final double STACK_DELTA_Y_SPACING = -18.0;  // 0.0
	public static final double STACK_DELTA_Z_SPACING = 12.0; 
	
	public static final double MOVE_STACK_DELTA_Y_SPACING = 0.0; 
	
	public static final double STACK_X_PRE_UNLOAD_OFFSET = 0;   
	public static final double STACK_Y_PRE_UNLOAD_OFFSET = 0;   
	public static final double STACK_Z_PRE_UNLOAD_OFFSET = 4; 
	
	public static final double STACK_X_POST_UNLOAD_OFFSET = -10;   
	public static final double STACK_Y_POST_UNLOAD_OFFSET = 0;   
	public static final double STACK_Z_POST_UNLOAD_OFFSET = 0; 

	public boolean debug = false;
	
	private StackPriority stackPriority = StackPriority.VERTICAL;
	private int numStacks = 1;
	private int numTotesPerStack = 6;
	private double[] homePosition = DEFAULT_HOME_COORD;
	
	public HumanLoadCommandListGenerator() {	
		stackStartPositions.add(new double[]  {35.5, 24.2, 11, 40});
		stackOffsetPositions.add(new double[] {-5.5, -4, 0, 0});

		stackStartPositions.add(new double[]  {37, 5.5, 11, 40});
		stackOffsetPositions.add(new double[] {-5, -3, 0, 0});

		stackStartPositions.add(new double[] {37, -12, 11, 0});
		stackOffsetPositions.add(new double[] {-9, 0, 0, 0});

		stackStartPositions.add(new double[] {37, -30, 11, 0});
		stackOffsetPositions.add(new double[] {-9, 0, 0, 0});

		stackStartPositions.add(new double[] {37, -30, 11, 0});
		stackOffsetPositions.add(new double[] {-9, 0, 0, 0});

		stackStartPositions.add(new double[] {37, -30, 11, 0});
		stackOffsetPositions.add(new double[] {-9, 0, 0, 0});
	}

	public HumanLoadCommandListGenerator(StackPriority stackPriority, int numStacks, int numTotesPerStack) {
		this.stackPriority = stackPriority;
		this.numStacks = numStacks;
		this.numTotesPerStack = numTotesPerStack;
	}
	
	public synchronized double[] getHome() {
		return homePosition;
	}

	public synchronized void setHome(double[] homeXYZInchesGammaDeg) {
		this.homePosition[0] = homeXYZInchesGammaDeg[0];
		this.homePosition[1] = homeXYZInchesGammaDeg[1];
		this.homePosition[2] = homeXYZInchesGammaDeg[2];
		this.homePosition[3] = homeXYZInchesGammaDeg[3];
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
    	waypointsGoHome.addWaypoint(homePosition);
    	addMotionProfileCurrentToPathCommand(waypointsGoHome);
		
		// Initialize to position of first tote
 //   	double[] toteReleasePosition = LEFT_POSITION_BUILD_STACK_RELEASE_COORD; 
     	double[] toteMovePosition = LEFT_POSITION_MOVE_STACK_RELEASE_COORD;
		
		for (int j = 0; j < secondaryCount; j++) {
			int stackId = 4 - secondaryCount + j;
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
		    	waypointsHumanToStack.addWaypoint(HOME_EXTRACT_TOTE_COORD);

		    	double[] totePreUnloadPosition = addPositionOffset(toteReleasePosition, STACK_X_PRE_UNLOAD_OFFSET, STACK_Y_PRE_UNLOAD_OFFSET, STACK_Z_PRE_UNLOAD_OFFSET, 0);
		    	double[] toteGetToHeightPosition = new double[] {HOME_EXTRACT_TOTE_COORD[0], HOME_EXTRACT_TOTE_COORD[1], totePreUnloadPosition[2],  HOME_EXTRACT_TOTE_COORD[3]};
		    	
		    	// Move tote straight up to pre unload height
			    waypointsHumanToStack.addWaypoint(toteGetToHeightPosition);
		    	
				// Move to the pre-unload position
		    	waypointsHumanToStack.addWaypoint(totePreUnloadPosition);

		    	// Move to the final unload position
//		    	double[] totePrePushPosition = null;
//		    	if (i == 0 && j > 0) {
//		    		totePrePushPosition = addPositionOffset(toteReleasePosition, 0, -10, 0, -15);
//		    		waypointsHumanToStack.addWaypoint(toteReleasePosition);
//		    	}
//		    	else {
		    		waypointsHumanToStack.addWaypoint(toteReleasePosition);
//		    	}
		    	addMotionProfileCommand(waypointsHumanToStack);
				
				// Move stack to left
//		    	if (i == 0 && j > 0) {
//					WaypointList waypointsMoveStack = new WaypointList(ProfileMode.CartesianInputJointMotion);				
//					waypointsMoveStack.addWaypoint(totePrePushPosition);
//					waypointsMoveStack.addWaypoint(toteMovePosition);
//					waypointsMoveStack.addWaypoint(totePreUnloadPosition);
//					waypointsMoveStack.addWaypoint(toteReleasePosition);
//			    	addMotionProfileCommand(waypointsMoveStack, new double[] {40,40,40,40});
//		    	}

		    	// Wait for release ok
				addWaitForNextCommand();
				
				// Release tote
				addToteGrabberOpenCommand();
				
//				double[] totePostUnloadPosition = addPositionOffset(toteReleasePosition, STACK_X_POST_UNLOAD_OFFSET, STACK_Y_POST_UNLOAD_OFFSET, STACK_Z_POST_UNLOAD_OFFSET, 0);
				double[] totePostUnloadPosition = addPositionOffset(toteReleasePosition, toteOffsetPosition[0], toteOffsetPosition[1], toteOffsetPosition[2], 0);
//				if (i < primaryCount - 1) {			
					// Joint motion back to human load station
					WaypointList waypointsReturnToHuman = new WaypointList(ProfileMode.CartesianInputJointMotion);				
					waypointsReturnToHuman.addWaypoint(toteReleasePosition);
	
					// Move to post-unload position
					waypointsReturnToHuman.addWaypoint(totePostUnloadPosition);
					
					waypointsReturnToHuman.addWaypoint(homePosition);
			    	addMotionProfileCommand(waypointsReturnToHuman);
					
			    	toteReleasePosition = incrementPrimaryTotePosition(toteReleasePosition);
//				}
//				else {
//					// Joint motion back to human load station
//					WaypointList waypointsPrepareToMoveStack = new WaypointList(ProfileMode.CartesianInputJointMotion);				
//					waypointsPrepareToMoveStack.addWaypoint(toteReleasePosition);
//	
//					// Move to post-unload position
//					waypointsPrepareToMoveStack.addWaypoint(totePostUnloadPosition);
//					
//					double[] toteGrabStackPosition = addPositionOffset(LEFT_POSITION_BUILD_STACK_RELEASE_COORD, STACK_X_POST_UNLOAD_OFFSET, STACK_Y_POST_UNLOAD_OFFSET, STACK_Z_POST_UNLOAD_OFFSET, 0);
//					waypointsPrepareToMoveStack.addWaypoint(toteGrabStackPosition);
//					waypointsPrepareToMoveStack.addWaypoint(LEFT_POSITION_BUILD_STACK_RELEASE_COORD);
//			    	addMotionProfileCommand(waypointsPrepareToMoveStack);
//					
//					// Wait for tote detection
//					addWaitForNextCommand();
//					
//					// Grab tote
//					addToteGrabberCloseCommand();
//					
//					// Move stack to left
//					WaypointList waypointsMoveStack = new WaypointList(ProfileMode.CartesianInputJointMotion);				
//					waypointsMoveStack.addWaypoint(LEFT_POSITION_BUILD_STACK_RELEASE_COORD);
//					waypointsMoveStack.addWaypoint(toteMovePosition);
//			    	addMotionProfileCommand(waypointsMoveStack);
//
//					// Wait for tote detection
//					addWaitForNextCommand();
//					
//					// Grab tote
//					addToteGrabberOpenCommand();
//					
//					// Joint motion back to human load station
//					WaypointList waypointsReturnToHuman = new WaypointList(ProfileMode.CartesianInputJointMotion);				
//					waypointsReturnToHuman.addWaypoint(toteMovePosition);
//	
//					// Move to post-unload position
//					double[] totePostMovePosition = addPositionOffset(toteMovePosition, STACK_X_POST_UNLOAD_OFFSET, STACK_Y_POST_UNLOAD_OFFSET, STACK_Z_POST_UNLOAD_OFFSET, 0);
//					waypointsReturnToHuman.addWaypoint(totePostMovePosition);
//					
//					waypointsReturnToHuman.addWaypoint(HUMAN_LOAD_START_COORD);
//			    	addMotionProfileCommand(waypointsReturnToHuman);
//				}
			}
			toteMovePosition = addPositionOffset(toteMovePosition, 0, MOVE_STACK_DELTA_Y_SPACING, 0, 0);
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
			waypoint[2] = LEFT_POSITION_BUILD_STACK_RELEASE_COORD[2];  // Reset Z
			output = addPositionOffset(waypoint, 0, STACK_DELTA_Y_SPACING, 0, 0);
		}
		else {
			waypoint[1] = LEFT_POSITION_BUILD_STACK_RELEASE_COORD[1];  // Reset Y
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