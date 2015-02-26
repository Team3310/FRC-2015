package edu.rhhs.frc.utility.motionprofile;

import edu.rhhs.frc.commands.robotarm.HumanLoadCommandListGenerator;
import edu.rhhs.frc.utility.motionprofile.CoordinatedMotion.CoMotionFilterOutput;
import edu.rhhs.frc.utility.motionprofile.Filter.ServoFilterOutput;
import edu.rhhs.frc.utility.motionprofile.Kinematics.IKINOutput;

public class MotionProfile {
	public enum ProfileMode {CartesianInputLinearMotion, JointInputJointMotion, CartesianInputJointMotion};
	
	public static final double DEFAULT_CONTROLLER_UPDATE_RATE = 10.0/1000.0; 		// seconds
	public static final double DEFAULT_PATH_VELOCITY = 130;   						// inches/second
	public static final double J1_JOINT_VELOCITY = 90;   							// deg/second
	public static final double J2_JOINT_VELOCITY = 100;   							// deg/second
	public static final double J3_JOINT_VELOCITY = 100;   							// deg/second
	public static final double J4_JOINT_VELOCITY = 150;   							// deg/second
	public static final double DEFAULT_CARTESIAN_ACCEL1 = 400.0/1000.0;   			// seconds
	public static final double DEFAULT_CARTESIAN_ACCEL2 = 200.0/1000.0;   			// seconds	
	public static final double DEFAULT_JOINT_ACCEL1 = 200.0/1000.0;   				// seconds
	public static final double DEFAULT_JOINT_ACCEL2 = 100.0/1000.0;   				// seconds	
	public static final double DEFAULT_END_TYPE_CNT = 0;   							

	public static final double INNER_ARM_LENGTH = 28.0; 		// inches 
	public static final double OUTER_ARM_LENGTH = 30.0; 		// inches	
	public static final double GROUNDING_LINK_LENGTH = 4.125; 	// inches 	
	public static final double TOOL_LENGTH = 4.875; 			// inches 	

	protected double[] armLengths = {0, INNER_ARM_LENGTH, OUTER_ARM_LENGTH, GROUNDING_LINK_LENGTH, 0, TOOL_LENGTH};   // arm lengths in inches
	protected double[] dHLengths = {38.85, 0, 0, 0, 1.839, 0};   // DH lengths in inches

    protected boolean isElbowUp = true; 
    protected boolean isFront = true; 
    
    protected ProfileMode profileMode = ProfileMode.CartesianInputLinearMotion;
    
    protected int numPaths;  				// Number of paths = number of taught points - 1
    protected double[][] taughtPositions; 	// path taught xyz positions in meters and toolAngle in degs
    protected double[] pathVelocities;  	// in/sec, entry for each path segment
    protected double cartesianAccel1 = DEFAULT_CARTESIAN_ACCEL1;   // linear accel in seconds
    protected double cartesianAccel2 = DEFAULT_CARTESIAN_ACCEL2;   // linear accel in seconds 

    protected double[] endTypeCNT; 

    protected double controllerUpdateRateSec = DEFAULT_CONTROLLER_UPDATE_RATE;  // Controller update rate seconds 
    protected int outputRateMs = 1;   //  Output rate milliseconds
    protected double serverExponentialFilterDecayTime = controllerUpdateRateSec;   // Servo filter exponential decay
    protected boolean isServerExponentialFilterEnable = false;
    
    protected ProfileOutput profileOutput;

    protected double[] jointPercentVelocity = {100, 100, 100, 100};   
    protected double[] jointVelocities = {
    		Math.toRadians(J1_JOINT_VELOCITY), 
    		Math.toRadians(J2_JOINT_VELOCITY), 
    		Math.toRadians(J3_JOINT_VELOCITY), 
    		Math.toRadians(J4_JOINT_VELOCITY)};   
    protected double[] jointAccels1 = {
    		DEFAULT_JOINT_ACCEL1, 
    		DEFAULT_JOINT_ACCEL1, 
    		DEFAULT_JOINT_ACCEL1, 
    		DEFAULT_JOINT_ACCEL1};  		
    protected double[] jointAccels2 = {
    		DEFAULT_JOINT_ACCEL2, 
    		DEFAULT_JOINT_ACCEL2, 
    		DEFAULT_JOINT_ACCEL2, 
    		DEFAULT_JOINT_ACCEL2};    
    
    public MotionProfile() {
    }
    
    public MotionProfile(WaypointList waypointList) {
    	this.taughtPositions = waypointList.getWaypoints();
    	this.profileMode = waypointList.getProfileMode();
    	initInputs();
    }
    
    private void initInputs() {
    	this.numPaths = taughtPositions.length - 1;
    	updatePathVelocities();
    	updateEndTypes();    	
    }
    
    private void updatePathVelocities() {
    	if (pathVelocities == null) {
    		pathVelocities = new double[numPaths];
    		for (int i = 0; i < numPaths; i++) {
    			pathVelocities[i] = DEFAULT_PATH_VELOCITY;
    		}
    	}
    }
 
    private void updateEndTypes() {
    	if (endTypeCNT == null) {
    		endTypeCNT = new double[numPaths - 1];
    		for (int i = 0; i < numPaths - 1; i++) {
    			endTypeCNT[i] = DEFAULT_END_TYPE_CNT;
    		}
    	}
    }

    // Perform profile calculations.  Return success/failure.
	public boolean calculatePath(boolean calcVelocitiesAccels, double servoFilterOutputMs) {
        
    	IKINOutput iKINOutput = null;
    	Kinematics kinematics = new Kinematics();

    	// Convert all angles to radians
    	double[][] taughtPositionsConverted = new double[taughtPositions.length][4];
    	if (profileMode == ProfileMode.JointInputJointMotion) { 
            for (int i = 0; i < taughtPositions.length; i++) {
                for (int j = 0; j < 4; j++) {
                	taughtPositionsConverted[i][j] = Math.toRadians(taughtPositions[i][j]);
                }
            }
    	}
    	
    	// Convert tool angles to radians
    	else { 
            for (int i = 0; i < taughtPositions.length; i++) {
            	taughtPositionsConverted[i][0] = taughtPositions[i][0];
            	taughtPositionsConverted[i][1] = taughtPositions[i][1];
            	taughtPositionsConverted[i][2] = taughtPositions[i][2];
            	taughtPositionsConverted[i][3] = Math.toRadians(taughtPositions[i][3]);
            }
    	}
    	    	
		// Convert input xyz taught point coordinates into joint angles
    	if (profileMode == ProfileMode.CartesianInputJointMotion) { 
        	iKINOutput = kinematics.iKIN(taughtPositionsConverted, taughtPositionsConverted.length - 1, armLengths, dHLengths, isElbowUp, isFront);
                                
            if (iKINOutput.errorFlag == true) { 
                System.out.println("XYZ Taught Point Unreachable. Reteach!");
                return false;
            }
            
            // Put the calculated angles back into the taught positions array for joint motion calculations
            taughtPositionsConverted = iKINOutput.userAngles;
        }	
		
        // Calculate the x(J1), y(J2), & z(J3) distance for each path.
    	double[][] dDis = new double[numPaths][4];
        for (int i = 0; i < numPaths; i++) {
            for (int j = 0; j < 4; j++) {
                dDis[i][j] = taughtPositionsConverted[i+1][j] - taughtPositionsConverted[i][j];
            }
        }
                		
	    // Convert cartesian acceleration filters to integration points
	    for (int j = 0; j < 4; j++) {
	    	jointAccels1[j] = Math.ceil(jointAccels1[j] / controllerUpdateRateSec);
	    	jointAccels2[j] = Math.ceil(jointAccels2[j] / controllerUpdateRateSec);
	    }
	    
	    cartesianAccel1 = Math.ceil(cartesianAccel1 / controllerUpdateRateSec);
	    cartesianAccel2 = Math.ceil(cartesianAccel2 / controllerUpdateRateSec);

	    // Call procedure to determine coordinated motion.
	    CoordinatedMotion coordinatedMotion = new CoordinatedMotion();
        CoordinatedMotion.CoMotionOutput coMotionOutput = coordinatedMotion.coMotion(	
        		numPaths, dDis, pathVelocities, jointPercentVelocity, jointVelocities, jointAccels1, jointAccels2, profileMode, endTypeCNT, 
                cartesianAccel1, cartesianAccel2, controllerUpdateRateSec);

        // Procedure calculates all the input positions to the filter or Inverse Kin
        CoMotionFilterOutput coMotionFilterOutput = coordinatedMotion.filterInput(
        		dDis, coMotionOutput.Tseg, coMotionOutput.Tcnt, taughtPositionsConverted, numPaths);
                                                
        // This portion calculates Inverse Kinematics for a linear move.
        // Modified by PDC on 12/5/00
    	if (profileMode == ProfileMode.CartesianInputLinearMotion) { 
        	iKINOutput = kinematics.iKIN(coMotionFilterOutput.PosFilter, coMotionFilterOutput.NumITPs, armLengths, dHLengths, isElbowUp, isFront);
                                
            if (iKINOutput.errorFlag == true) { 
                System.out.println("Point Unreachable. Reteach!");
                return false;
            }
        }
    	else {
    		iKINOutput = kinematics.getInstanceIKINOutput(coMotionFilterOutput.NumITPs);
    		iKINOutput.userAngles = coMotionFilterOutput.PosFilter;
    	}
        
        // Runs the RJ-3 joint filter
        double[][] Out2 = Filter.filter(profileMode, jointAccels1, jointAccels2, cartesianAccel1, cartesianAccel2, 
        		coMotionFilterOutput.NumITPs, iKINOutput.userAngles);
 
        // Input code to call the Servo filters
        Filter filter = new Filter();
        ServoFilterOutput serverFilterOutput = filter.servoFilter(servoFilterOutputMs/1000.0, controllerUpdateRateSec, serverExponentialFilterDecayTime, Out2, coMotionFilterOutput.NumITPs, isServerExponentialFilterEnable);
        
        // Servo position [ServoOut()] is actually deltaPos, so we take the
        // joint position of the 1st taught point [PosFilterIn(0,J)] to
        // use as a starting point.

        for (int i = 0; i < serverFilterOutput.pointCount + 1; i++) {
            if (i == 0) {
                for (int j = 0; j < 4; j++) {
                	serverFilterOutput.ServoOut[i][j] = iKINOutput.userAngles[i][j];
                }
        	}                
            else {
                for (int j = 0; j < 4; j++) {
                	serverFilterOutput.ServoOut[i][j] = serverFilterOutput.ServoOut[i-1][j] + serverFilterOutput.ServoOut[i][j];
                }
        	}
        }
       
        // ServoOut is now an array containing User Angles [radians]
        
        if (calcVelocitiesAccels) {
	        // CALL Kinematic routine to calculate X,Y,Z position
	        // The Filter only outputs DeltaPosition in Joint Space.
	        double[][] CartPos = Kinematics.fKIN(serverFilterOutput.ServoOut, serverFilterOutput.pointCount, armLengths, dHLengths);
	        
	        // Call procedure to calculate velocity and acceleration
	        profileOutput = Physics.VelAccPos(serverFilterOutput.pointCount, serverFilterOutput.ServoOut, CartPos);
        }
        else {
	        profileOutput = new ProfileOutput(serverFilterOutput.pointCount, false);
	        profileOutput.setJointAngles(serverFilterOutput.ServoOut);
        }
        
        return true;
	} 
	
	public double[] calcInverseKinematicsRad(double[] xyzToolPointRad) {
		double[][] inputPoints = { xyzToolPointRad };
		Kinematics kinematics = new Kinematics();
    	IKINOutput iKINOutput = kinematics.iKIN(inputPoints, inputPoints.length - 1, armLengths, dHLengths, isElbowUp, isFront);
    	return iKINOutput.userAngles[0];
	}
	
	public double[] calcInverseKinematicsDeg(double[] xyzToolPointDeg) {
    	// Convert output tool angle to radians
		xyzToolPointDeg[3] = Math.toRadians(xyzToolPointDeg[3]);
		
		double[] jointAngleRad = calcInverseKinematicsRad(xyzToolPointDeg);
		
		// Convert output joint angles to degrees
		for (int i = 0; i < 4; i++) {
			jointAngleRad[i] = Math.toDegrees(jointAngleRad[i]);
		}
		return jointAngleRad;
	}
	
	public double[] calcForwardKinematicsRad(double[] jointAnglesRad) {
		double[][] inputPoints = { jointAnglesRad };
    	double[][] output = Kinematics.fKIN(inputPoints, inputPoints.length - 1, armLengths, dHLengths);
    	return output[0];
	}
	
	public double[] calcForwardKinematicsDeg(double[] jointAnglesDeg) {
		// Convert input angles to radians
		for (int i = 0; i < 4; i++) {
			jointAnglesDeg[i] = Math.toRadians(jointAnglesDeg[i]);
		}
		
    	double[] xyzToolRad = calcForwardKinematicsRad(jointAnglesDeg);
    	
    	// Convert output tool angle to degrees
    	xyzToolRad[3] = Math.toDegrees(xyzToolRad[3]);
    	
    	return xyzToolRad;
	}
	
	public void printOutput(double serverOutputRateMs) {
		profileOutput.output(outputRateMs, serverOutputRateMs);
	}
    
    // Getters and setters
	public ProfileOutput getProfile() {
		return profileOutput;
	}
	
	
    public double[] getArmLengths() {
		return armLengths;
	}

	public void setArmLengths(double[] armLengths) {
		this.armLengths = armLengths;
	}

	public boolean isElbowUp() {
		return isElbowUp;
	}

	public void setElbowUp(boolean isElbowUp) {
		this.isElbowUp = isElbowUp;
	}

	public boolean isFront() {
		return isFront;
	}

	public void setFront(boolean isFront) {
		this.isFront = isFront;
	}

	public ProfileMode getProfileMode() {
		return profileMode;
	}

	public void setProfileMode(ProfileMode profileMode) {
		this.profileMode = profileMode;
	}

	public double[] getJointPercentVelocity() {
		return jointPercentVelocity;
	}

	public void setJointPercentVelocity(double[] jointPercentVelocity) {
		this.jointPercentVelocity = jointPercentVelocity;
	}

	public double[] getJointVelocities() {
		return jointVelocities;
	}

	public void setJointVelocities(double[] jointVelocities) {
		for (int i = 0; i < 4; i++) {
			this.jointVelocities[i] = Math.toRadians(jointVelocities[i]);
		}
	}

	public double[] getJointAccels1() {
		return jointAccels1;
	}

	public void setJointAccels1(double[] jointAccels1) {
		this.jointAccels1 = jointAccels1;
	}

	public double[] getJointAccels2() {
		return jointAccels2;
	}

	public void setJointAccels2(double[] jointAccels2) {
		this.jointAccels2 = jointAccels2;
	}

	public double[] getPathVelocities() {
		return pathVelocities;
	}

	public void setPathVelocities(double[] pathVelocities) {
		this.pathVelocities = pathVelocities;
	}

	public double getCartesianAccel1() {
		return cartesianAccel1;
	}

	public void setCartesianAccel1(double cartesianAccel1) {
		this.cartesianAccel1 = cartesianAccel1;
	}

	public double getCartesianAccel2() {
		return cartesianAccel2;
	}

	public void setCartesianAccel2(double cartesianAccel2) {
		this.cartesianAccel2 = cartesianAccel2;
	}

	public double[] getEndTypeCNT() {
		return endTypeCNT;
	}

	public void setEndTypeCNT(double[] endTypeCNT) {
		this.endTypeCNT = endTypeCNT;
	}

	public double getControllerUpdateRateSec() {
		return controllerUpdateRateSec;
	}

	public void setControllerUpdateRateSec(double controllerUpdateRateSec) {
		this.controllerUpdateRateSec = controllerUpdateRateSec;
	}

	public int getOutputRateMs() {
		return outputRateMs;
	}

	public void setOutputRateMs(int outputRateMs) {
		this.outputRateMs = outputRateMs;
	}

	public double getServerExponentialFilterDecayTime() {
		return serverExponentialFilterDecayTime;
	}

	public void setServerExponentialFilterDecayTime(
			double serverExponentialFilterDecayTime) {
		this.serverExponentialFilterDecayTime = serverExponentialFilterDecayTime;
	}

	public boolean isServerExponentialFilterEnable() {
		return isServerExponentialFilterEnable;
	}

	public void setServerExponentialFilterEnable(
			boolean isServerExponentialFilterEnable) {
		this.isServerExponentialFilterEnable = isServerExponentialFilterEnable;
	}
                    
    public static void main(String[] args) {
    	long startTime = System.nanoTime();
        
//    	WaypointList waypoints = new WaypointList(ProfileMode.CartesianInputLinearMotion);
//    	waypoints.addWaypoint(0.101, 0.102, 0.103, 0);
//    	waypoints.addWaypoint(0.201, 0.202, 0.203, 90);
//    	MotionProfile profile = new MotionProfile(waypoints);
   	
//    	WaypointList waypoints = new WaypointList(ProfileMode.CartesianInputLinearMotion);
//    	waypoints.addWaypoint(0.500, 0.500, 0.707, 90);
//    	waypoints.addWaypoint(0.500, 0.000, 0.500, 0);
//    	MotionProfile profile = new MotionProfile(waypoints);

//    	WaypointList waypoints = new WaypointList(ProfileMode.JointInputJointMotion);
//    	waypoints.addWaypoint(0, 0, -60, 0);
//    	waypoints.addWaypoint(-140, 0, -60, 0);
//    	MotionProfile profile = new MotionProfile(waypoints);
   	
//    	WaypointList waypoints = new WaypointList(ProfileMode.CartesianInputJointMotion);
//    	waypoints.addWaypoint(0.500, 0.500, 0.707, 90);
//    	waypoints.addWaypoint(0.500, 0.000, 0.500, 0);
//    	MotionProfile profile = new MotionProfile(waypoints);
 
//    	WaypointList waypointsM2H = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
//    	waypointsM2H.addWaypoint(RobotArm.J1_MASTER_ANGLE_DEG, RobotArm.J2_MASTER_ANGLE_DEG, RobotArm.J3_MASTER_ANGLE_DEG, RobotArm.J4_MASTER_ANGLE_DEG);
//    	waypointsM2H.addWaypoint(-129, 89,  -101.7,   0);
//    	waypointsM2H.addWaypoint(-129, 73, -42.69, 0);
//    	MotionProfile profile = new MotionProfile(waypointsM2H);
//    	profile.calculatePath();
//    	profile.printOutput();
    	
//		WaypointList waypointsHumanToStack = new WaypointList(ProfileMode.JointInputJointMotion);	
//    	waypointsHumanToStack.addWaypoint(new double[] {0,0,0,0});
//    	waypointsHumanToStack.addWaypoint(new double[] {50,40,30,30});
//    	waypointsHumanToStack.addWaypoint(new double[] {100,80,60,60});
//    	MotionProfile profile = new MotionProfile(waypointsHumanToStack);
//    	profile.calculatePath(false, 10);
//    	profile.printOutput(10);   	
//    	
//		WaypointList waypointsMoveStack = new WaypointList(ProfileMode.CartesianInputLinearMotion);				
//		waypointsMoveStack.addWaypoint(HumanLoadCommandListGenerator.LEFT_POSITION_BUILD_STACK_RELEASE_COORD);
//		waypointsMoveStack.addWaypoint(HumanLoadCommandListGenerator.LEFT_POSITION_MOVE_STACK_RELEASE_COORD);
//    	MotionProfile profile = new MotionProfile(waypointsMoveStack);
//    	profile.calculatePath(true, 10);
//    	profile.printOutput(10);

		WaypointList waypointsMoveStack = new WaypointList(ProfileMode.CartesianInputLinearMotion);				
		waypointsMoveStack.addWaypoint(HumanLoadCommandListGenerator.LEFT_POSITION_BUILD_STACK_RELEASE_COORD);
		waypointsMoveStack.addWaypoint(HumanLoadCommandListGenerator.LEFT_POSITION_MOVE_STACK_RELEASE_COORD);
		waypointsMoveStack.addWaypoint(HumanLoadCommandListGenerator.LEFT_POSITION_BUILD_STACK_RELEASE_COORD);
    	MotionProfile profile = new MotionProfile(waypointsMoveStack);
    	profile.calculatePath(true, 10);
    	profile.printOutput(10);

//    	WaypointList waypointsM2H = new WaypointList(MotionProfile.ProfileMode.CartesianInputJointMotion);
//    	waypointsM2H.addWaypoint(RobotArm.X_MASTER_POSITION_IN, RobotArm.Y_MASTER_POSITION_IN, RobotArm.Z_MASTER_POSITION_IN, RobotArm.GAMMA_MASTER_ANGLE_DEG);
//    	waypointsM2H.addWaypoint(-19.45362530920832,-24.023226578639672,11.800983061590966,-129.0);
//    	waypointsM2H.addWaypoint(-36.392038457272776,-44.94042481140042,28.534465949865748,-129.0);
//    	MotionProfile profile = new MotionProfile(waypointsM2H);
//    	profile.calculatePath();
//    	profile.printOutput();

    	System.out.println("Total time = " + (System.nanoTime() - startTime) / 1000000000.0);
    	
//    	WaypointList waypointsLinear = new WaypointList(ProfileMode.JointInputJointMotion);
//    	waypointsLinear.addWaypoint(0, 0, 0, 0);
//    	waypointsLinear.addWaypoint(30, 0, 0, 0);
//
//    	MotionProfile profileLinear = new MotionProfile(waypointsLinear);
//    	profileLinear.setJointVelocities(new double[] {200,200,200,200});
//    	profileLinear.setJointAccels1(new double[] {0.2, 0.2, 0.2, 0.2});
//    	profileLinear.setJointAccels2(new double[] {0.1, 0.1, 0.1, 0.1});
//    	profileLinear.calculateLinearPath();
//    	profileLinear.profileOutput.outputLinear(10);

    	// Radians check
//    	MotionProfile profile = new MotionProfile();
//    	double[] jointAngleInputRad = new double[] {Math.PI/4,Math.PI/4,-Math.PI/4,Math.PI/4};
//    	System.out.println("Forward KIN joint angle input radians = " + jointAngleInputRad[0] + "," + jointAngleInputRad[1] + "," + jointAngleInputRad[2] + "," + jointAngleInputRad[3]);
//    	
//    	double[] xyzToolOutputRad = profile.calcForwardKinematicsRad(jointAngleInputRad);
//    	System.out.println("Forward KIN xyzTool output radian = " + xyzToolOutputRad[0] + "," + xyzToolOutputRad[1] + "," + xyzToolOutputRad[2] + "," + xyzToolOutputRad[3]);
//    	
//    	double[] jointAngleOutputRad = profile.calcInverseKinematicsRad(xyzToolOutputRad);
//    	System.out.println("Inverse KIN joint angle output radians = " + jointAngleOutputRad[0] + "," + jointAngleOutputRad[1] + "," + jointAngleOutputRad[2] + "," + jointAngleOutputRad[3]);
// 
//    	// Degrees check
//    	double[] jointAngleInputDeg = new double[] {45, 45, -45, 45};
//    	System.out.println("Forward KIN joint angle input deg = " + jointAngleInputDeg[0] + "," + jointAngleInputDeg[1] + "," + jointAngleInputDeg[2] + "," + jointAngleInputDeg[3]);
//    	
//    	double[] xyzToolOutputDeg = profile.calcForwardKinematicsDeg(jointAngleInputDeg);
//    	System.out.println("Forward KIN xyzTool output deg = " + xyzToolOutputDeg[0] + "," + xyzToolOutputDeg[1] + "," + xyzToolOutputDeg[2] + "," + xyzToolOutputDeg[3]);
//    	
//    	double[] jointAngleOutputDeg = profile.calcInverseKinematicsDeg(xyzToolOutputDeg);
//    	System.out.println("Inverse KIN joint angle output deg = " + jointAngleOutputDeg[0] + "," + jointAngleOutputDeg[1] + "," + jointAngleOutputDeg[2] + "," + jointAngleOutputDeg[3]);
 
    	// Master conversions
//    	double[] masterAngleInputDeg = new double[] {RobotArm.J1_MASTER_ANGLE_DEG, RobotArm.J2_MASTER_ANGLE_DEG, RobotArm.J3_MASTER_ANGLE_DEG, RobotArm.J4_MASTER_ANGLE_DEG};
//    	System.out.println("Master angle input degs = " + masterAngleInputDeg[0] + "," + masterAngleInputDeg[1] + "," + masterAngleInputDeg[2] + "," + masterAngleInputDeg[3]);
//    	
//    	double[] masterXYZOutputDeg = profile.calcForwardKinematicsDeg(masterAngleInputDeg);
//    	System.out.println("Master xyzTool output deg = " + masterXYZOutputDeg[0] + "," + masterXYZOutputDeg[1] + "," + masterXYZOutputDeg[2] + "," + masterXYZOutputDeg[3]);
//
//    	double[] masterAngleOutputDeg = profile.calcInverseKinematicsDeg(masterXYZOutputDeg);
//    	System.out.println("Inverse KIN joint angle output deg = " + masterAngleOutputDeg[0] + "," + masterAngleOutputDeg[1] + "," + masterAngleOutputDeg[2] + "," + masterAngleOutputDeg[3]);

    	double[] HUMAN_LOAD_START_COORD =  {-36.4, -44.9, 28.5, -129.0}; 
    	
    	double[] jointAngleInputDeg = new double[] {0, 89,  -101.7,   0};
//    	double[] jointAngleInputDeg = new double[] {RobotArm.J1_MASTER_ANGLE_DEG, RobotArm.J2_MASTER_ANGLE_DEG, RobotArm.J3_MASTER_ANGLE_DEG, RobotArm.J4_MASTER_ANGLE_DEG};
    	System.out.println("Forward KIN joint angle input deg = " + jointAngleInputDeg[0] + "," + jointAngleInputDeg[1] + "," + jointAngleInputDeg[2] + "," + jointAngleInputDeg[3]);
    	
    	double[] xyzToolOutputDeg = profile.calcForwardKinematicsDeg(jointAngleInputDeg);
    	System.out.println("Forward KIN xyzTool output inches and deg = " + xyzToolOutputDeg[0] + "," + xyzToolOutputDeg[1] + "," + xyzToolOutputDeg[2] + "," + xyzToolOutputDeg[3]);
    	
    	double[] jointAngleOutputDeg = profile.calcInverseKinematicsDeg(xyzToolOutputDeg);
    	System.out.println("Inverse KIN joint angle output deg = " + jointAngleOutputDeg[0] + "," + jointAngleOutputDeg[1] + "," + jointAngleOutputDeg[2] + "," + jointAngleOutputDeg[3]);

    	double[] humanAngleOutputDeg = profile.calcInverseKinematicsDeg(HUMAN_LOAD_START_COORD);
    	System.out.println("Inverse KIN human angle output deg = " + humanAngleOutputDeg[0] + "," + humanAngleOutputDeg[1] + "," + humanAngleOutputDeg[2] + "," + humanAngleOutputDeg[3]);
    }
}
