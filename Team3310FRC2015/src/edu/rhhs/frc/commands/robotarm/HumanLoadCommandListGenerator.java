package edu.rhhs.frc.commands.robotarm;

import java.util.ArrayList;
import edu.rhhs.frc.subsystems.RobotArm;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.MotionProfile.ProfileMode;
import edu.rhhs.frc.utility.motionprofile.ProfileOutput;
import edu.rhhs.frc.utility.motionprofile.WaypointList;

public class HumanLoadCommandListGenerator extends RobotArmCommandListGenerator 
{
	public enum StackPriority {VERTICAL, HORIZONTAL};
	
	public static final double[] DEFAULT_HOME_COORD =     {21, 0.0, 14, 0};  // Gripper position for bottom tote in stacker tray
	public static final double[] HOME_LOAD_COORD =        {21, 0.0, 22, 0};  // Gripper position for second tote to seat it in bottom tote
	public static final double[] HOME_STACK_EXIT_COORD =  {18, 0.0, 26, 0};  // Gripper position to clear stacker tray when gripped on bottom tote
	public static final double[] HOME_STACK_CLEAR_COORD = {18, 0.0, 36, 0};  // Gripper position to clear top tote in stacker tray on return to home gripper open
	public static final double[] WALL_CLEAR_COORD = 	  {11, 15.5, 36, 0};  // Gripper position to clear wall

	public ArrayList<double[]> stackStartPositions = new ArrayList<double[]>();
	public ArrayList<double[]> stackPreReleaseOffsetPositions = new ArrayList<double[]>();
	public ArrayList<double[]> stackExtractOffsetPositions = new ArrayList<double[]>();
	public ArrayList<double[]> stackPullBackOffsetPositions = new ArrayList<double[]>();
	
	public static final double STACK_DELTA_Y_SPACING = 0.0;
	public static final double STACK_DELTA_Z_SPACING = 24.0;
	
	public static final double STACK_X_PRE_UNLOAD_OFFSET = 0;
	public static final double STACK_Y_PRE_UNLOAD_OFFSET = 0;  
	public static final double STACK_Z_PRE_UNLOAD_OFFSET = 12;
	
	public static final double STACK_X_POST_UNLOAD_OFFSET = 0;   
	public static final double STACK_Y_POST_UNLOAD_OFFSET = 0;   
	public static final double STACK_Z_POST_UNLOAD_OFFSET = 22; 

	public boolean debug = false;
	
	private StackPriority stackPriority = StackPriority.VERTICAL;
	private int numStacks = 1;
	private int maxStacks = 3;
	private int numTotesPerStack = 6;
	private double[] homePosition = DEFAULT_HOME_COORD;
	private double[] worldToRobotOffsetInches = {0, 0, 0};
	
	public HumanLoadCommandListGenerator() {	
		stackStartPositions.add(new double[] {-44, 17, 9, 0});
		stackPreReleaseOffsetPositions.add(new double[] {-24, 24,  0, 0});
		stackExtractOffsetPositions.add(new double[] {-30, 30,  0, 0});
		stackPullBackOffsetPositions.add(new double[] { -22, 9,  0, 0});

		stackStartPositions.add(new double[] {-25, 34, 8, 0});
		stackPreReleaseOffsetPositions.add(new double[] {-5, 29,  0, 0});
		stackExtractOffsetPositions.add(new double[] {-7, 42,  0, 0});
		stackPullBackOffsetPositions.add(new double[] { -11, 15,  0, 0});

		stackStartPositions.add(new double[] { -4, 47, 8, 0});
		stackPreReleaseOffsetPositions.add(new double[] { 9, 26,  0, 0});
		stackExtractOffsetPositions.add(new double[] { 16, 44,  0, 0});
		stackPullBackOffsetPositions.add(new double[] { -2, 24,  0, 0});
	
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
		double[] currentWorldPosition = motionProfile.xfromRobotToWorld(currentRobotPosition, worldToRobotAngleDeg, MotionProfile.ZERO_OFFSET);
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
		addToteGrabberOpenCommand();
		
		// Go from the current location to human station
    	WaypointList waypointsGoHome = new WaypointList(MotionProfile.ProfileMode.CartesianInputJointMotion);
    	waypointsGoHome.addWaypoint(HOME_STACK_CLEAR_COORD);
    	waypointsGoHome.addWaypoint(HOME_LOAD_COORD);
    	addMotionProfileCurrentToPathCommand(waypointsGoHome, new double[] {120, 120, 120, 120}, new double[] {100, 100, 100}, new double[] {100, 100, 100});
				
		for (int j = 0; j < secondaryCount; j++) {
			int stackId = maxStacks - secondaryCount + j;
		   	double[] toteReleasePosition = stackStartPositions.get(stackId); 
		   	double[] totePreReleaseOffsetPosition = stackPreReleaseOffsetPositions.get(stackId); 
		   	double[] toteOffsetPosition = stackExtractOffsetPositions.get(stackId); 
		   	double[] totePullBackOffsetPosition = stackPullBackOffsetPositions.get(stackId); 
			
			for (int i = 0; i < primaryCount; i++) {
				if (debug) {
					System.out.println("Tote position, primary = " + i + ", secondary = " + j + " (in) = " + toteReleasePosition[0] + ", " + toteReleasePosition[1] + ", " + toteReleasePosition[2]);
				}
				
				// Wait for tote detection
				addWaitForNextCommand();
				
				// Grab tote
//				addToteGrabberCloseCommand();
//				addToteGrabberOpenCommand();
				
				// Pull away from human load station
				WaypointList waypointsGetTote = new WaypointList(ProfileMode.CartesianInputJointMotion);	
				waypointsGetTote.addWaypoint(HOME_LOAD_COORD);
				waypointsGetTote.addWaypoint(homePosition);
		    	addMotionProfileCommand(waypointsGetTote, new double[] {160, 120, 120, 120}, new double[] {100, 100, 100, 100, 100}, new double[] {25, 100, 100, 100, 100});

				addToteGrabberCloseCommand();

				// Pull away from human load station
				WaypointList waypointsHumanToStack = new WaypointList(ProfileMode.CartesianInputJointMotion);	
		    	waypointsHumanToStack.addWaypoint(homePosition);
		    	waypointsHumanToStack.addWaypoint(HOME_STACK_EXIT_COORD);
		    	
		    	// Move tote upward to proper height position
		    	double[] totePreUnloadPosition = addPositionOffset(toteReleasePosition, STACK_X_PRE_UNLOAD_OFFSET, STACK_Y_PRE_UNLOAD_OFFSET, STACK_Z_PRE_UNLOAD_OFFSET, 0);
		    	
				// Move to the pre-release turn position
		    	double[] totePreReleaseTurnPosition = new double[] {totePreReleaseOffsetPosition[0], totePreReleaseOffsetPosition[1], totePreUnloadPosition[2],  0};
		    	waypointsHumanToStack.addWaypoint(totePreReleaseTurnPosition);

		    	// Move to the pre-unload position
		    	waypointsHumanToStack.addWaypoint(totePreUnloadPosition);

		    	// Move to unload position
		    	waypointsHumanToStack.addWaypoint(toteReleasePosition);
		    	addMotionProfileCommand(waypointsHumanToStack, new double[] {110, 60, 60, 90}, new double[] {100, 100, 100, 100, 100}, new double[] {100, 100, 100, 100, 100});
				
		    	// Wait for release ok
				addWaitForNextCommand();
				
				// Release tote
				addToteGrabberOpenCommand();
				
				// Joint motion back to human load station
				WaypointList waypointsReturnToHuman = new WaypointList(ProfileMode.CartesianInputJointMotion);				
				waypointsReturnToHuman.addWaypoint(toteReleasePosition);

		    	// Move tote to offset position
				if (i < 2) {
					//Rise above the stack to return
			    	double[] toteReleaseOffsetPosition1 = addPositionOffset(toteReleasePosition, STACK_X_POST_UNLOAD_OFFSET, STACK_Y_POST_UNLOAD_OFFSET, STACK_Z_POST_UNLOAD_OFFSET, 0);
			    	waypointsReturnToHuman.addWaypoint(toteReleaseOffsetPosition1);
	
			    	double[] toteReleaseOffsetPosition2 = new double[] {toteOffsetPosition[0], toteOffsetPosition[1], toteReleaseOffsetPosition1[2],  0};
			    	waypointsReturnToHuman.addWaypoint(toteReleaseOffsetPosition2);
				}
				else {
					//Account for height limit (pull back rather than go above the stack)
			    	double[] toteReleaseOffsetPosition1 = new double[] {totePullBackOffsetPosition[0], totePullBackOffsetPosition[1], toteReleasePosition[2],  0};
			    	waypointsReturnToHuman.addWaypoint(toteReleaseOffsetPosition1);
				}
		    	
			    // Move above the new double stack at the human station
		    	waypointsReturnToHuman.addWaypoint(HOME_STACK_CLEAR_COORD);
		    	
		    	// Finally move to the home position
			    waypointsReturnToHuman.addWaypoint(HOME_LOAD_COORD);
		    	addMotionProfileCommand(waypointsReturnToHuman, new double[] {160, 120, 120, 120}, new double[] {100, 100, 100, 100, 100}, new double[] {25, 100, 100, 100, 100});
				
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
		this.numTotesPerStack = numTotesPerStack / 2; //TODO: Verify
	}
	
    public static void main(String[] args) {
    	HumanLoadCommandListGenerator humanLoad = new HumanLoadCommandListGenerator(StackPriority.VERTICAL, 3, 6);
    	humanLoad.debug = true;
    	humanLoad.calculate();
    	
    	RobotArmCommandList commandList = humanLoad.getCommandList();
    	for (RobotArmCommand command : commandList) {
    		if (command instanceof  RobotArmMotionProfilePath) {
    			System.out.println(" ");
    			System.out.println("Motion Profile Output");
    			WaypointList waypoints = ((RobotArmMotionProfilePath) command).getWaypoints();
    	    	if (waypoints != null) {
    		    	MotionProfile motionProfile = new MotionProfile(waypoints);
    				motionProfile.calculatePath(false, RobotArm.OUTER_LOOP_UPDATE_RATE_MS, 0, MotionProfile.ZERO_OFFSET);
    				ProfileOutput profileOutput = motionProfile.getProfile();
    				profileOutput.output(1, RobotArm.OUTER_LOOP_UPDATE_RATE_MS);
    	    	}
    		}
    	}
    }
}