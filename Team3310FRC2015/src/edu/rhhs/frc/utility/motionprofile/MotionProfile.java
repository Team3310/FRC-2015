package edu.rhhs.frc.utility.motionprofile;

import edu.rhhs.frc.utility.motionprofile.CoordinatedMotion.CoMotionFilterOutput;
import edu.rhhs.frc.utility.motionprofile.Filter.ServoFilterOutput;
import edu.rhhs.frc.utility.motionprofile.Kinematics.IKINOutput;

public class MotionProfile {
	public enum ProfileMode {CartesianInputLinearMotion, JointInputJointMotion, CartesianInputJointMotion};
	
	public static final double DEFAULT_CONTROLLER_UPDATE_RATE = 10.0/1000.0; 		// seconds
	public static final double DEFAULT_PATH_VELOCITY = 1.5;   						// meters/second
	public static final double DEFAULT_JOINT_VELOCITY = 320;   						// deg/second
	public static final double DEFAULT_CARTESIAN_ACCEL1 = 400.0/1000.0;   			// seconds
	public static final double DEFAULT_CARTESIAN_ACCEL2 = 200.0/1000.0;   			// seconds	
	public static final double DEFAULT_JOINT_ACCEL1 = DEFAULT_CARTESIAN_ACCEL1;   	// seconds
	public static final double DEFAULT_JOINT_ACCEL2 = DEFAULT_CARTESIAN_ACCEL2;   	// seconds	
	public static final double DEFAULT_END_TYPE_CNT = 0;   							// meters/second

	public static final double INNER_ARM_LENGTH = 0.7112;  // meters
	public static final double OUTER_ARM_LENGTH = 0.7620;  // meters	
	public static final double GROUNDING_LINK_LENGTH = 0.104775;  // meters	
	public static final double TOOL_LENGTH = 0.123825;  // meters	

	protected double[] armLengths = {0, INNER_ARM_LENGTH, OUTER_ARM_LENGTH, GROUNDING_LINK_LENGTH, 0, TOOL_LENGTH};   // arm lengths in meters
	protected double[] dHLengths = {0.98679, 0, 0, 0, 0, 0.0467106};   // DH lengths in meters

    protected boolean isElbowUp = true; 
    protected boolean isFront = true; 
    
    protected ProfileMode profileMode = ProfileMode.CartesianInputLinearMotion;
    
    protected int numPaths;  				// Number of paths = number of taught points - 1
    protected double[][] taughtPositions; 	// path taught xyz positions in meters and toolAngle in degs
    protected double[] pathVelocities;  	// m/sec, entry for each path segment
    protected double cartesianAccel1 = DEFAULT_CARTESIAN_ACCEL1;   // linear accel in seconds
    protected double cartesianAccel2 = DEFAULT_CARTESIAN_ACCEL2;   // linear accel in seconds 

    protected double[] endTypeCNT; 

    protected double controllerUpdateRateSec = DEFAULT_CONTROLLER_UPDATE_RATE;  // Controller update rate seconds 
    protected int outputRateMs = 10;   //  Output rate milliseconds
    protected double serverExponentialFilterDecayTime = controllerUpdateRateSec;   // Servo filter exponential decay
    protected boolean isServerExponentialFilterEnable = false;
    
    protected ProfileOutput profileOutput;

    protected double[] jointPercentVelocity = {100, 100, 100, 100};   
    protected double[] jointVelocities = {
    		Math.toRadians(DEFAULT_JOINT_VELOCITY), 
    		Math.toRadians(DEFAULT_JOINT_VELOCITY), 
    		Math.toRadians(DEFAULT_JOINT_VELOCITY), 
    		Math.toRadians(DEFAULT_JOINT_VELOCITY)};   
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
	public boolean calculatePath() {
        
    	IKINOutput iKINOutput = null;
    	Kinematics kinematics = new Kinematics();

    	// Convert all angles to radians
    	if (profileMode == ProfileMode.JointInputJointMotion) { 
            for (int i = 0; i < taughtPositions.length; i++) {
                for (int j = 0; j < 4; j++) {
                	taughtPositions[i][j] = Math.toRadians(taughtPositions[i][j]);
                }
            }
    	}
    	
    	// Convert tool angles to radians
    	else { 
            for (int i = 0; i < taughtPositions.length; i++) {
                taughtPositions[i][3] = Math.toRadians(taughtPositions[i][3]);
            }
    	}
    	    	
		// Convert input xyz taught point coordinates into joint angles
    	if (profileMode == ProfileMode.CartesianInputJointMotion) { 
        	iKINOutput = kinematics.iKIN(taughtPositions, taughtPositions.length - 1, armLengths, dHLengths, isElbowUp, isFront);
                                
            if (iKINOutput.errorFlag == true) { 
                System.out.println("XYZ Taught Point Unreachable. Reteach!");
                return false;
            }
            
            // Put the calculated angles back into the taught positions array for joint motion calculations
            taughtPositions = iKINOutput.userAngles;
        }	
		
        // Calculate the x(J1), y(J2), & z(J3) distance for each path.
    	double[][] dDis = new double[numPaths][4];
        for (int i = 0; i < numPaths; i++) {
            for (int j = 0; j < 4; j++) {
                dDis[i][j] = taughtPositions[i+1][j] - taughtPositions[i][j];
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
        		dDis, coMotionOutput.Tseg, coMotionOutput.Tcnt, taughtPositions, numPaths);
                                                
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
        ServoFilterOutput serverFilterOutput = filter.servoFilter(controllerUpdateRateSec, serverExponentialFilterDecayTime, Out2, coMotionFilterOutput.NumITPs, isServerExponentialFilterEnable);
        
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
        
        // CALL Kinematic routine to calculate X,Y,Z position
        // The Filter only outputs DeltaPosition in Joint Space.
        double[][] CartPos = Kinematics.fKIN(serverFilterOutput.ServoOut, serverFilterOutput.pointCount, armLengths, dHLengths);
        
        // Call procedure to calculate velocity and acceleration
        profileOutput = Physics.VelAccPos(serverFilterOutput.pointCount, serverFilterOutput.ServoOut, CartPos);

        return true;
	} 
	
	public double[] calcInverseKinematics(double[] xyzToolPoint) {
		double[][] inputPoints = { xyzToolPoint };
		Kinematics kinematics = new Kinematics();
    	IKINOutput iKINOutput = kinematics.iKIN(inputPoints, inputPoints.length - 1, armLengths, dHLengths, isElbowUp, isFront);
    	return iKINOutput.userAngles[0];
	}
	
	public double[] calcForwardKinematics(double[] jointAngles) {
		double[][] inputPoints = { jointAngles };
    	double[][] output = Kinematics.fKIN(inputPoints, inputPoints.length - 1, armLengths, dHLengths);
    	return output[0];
	}
	
	public void printOutput() {
		profileOutput.output(outputRateMs);
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

    	WaypointList waypoints = new WaypointList(ProfileMode.JointInputJointMotion);
    	waypoints.addWaypoint(0, 0, 0, 0);
    	waypoints.addWaypoint(45, 0, 0, 0);
    	MotionProfile profile = new MotionProfile(waypoints);
   	
//    	WaypointList waypoints = new WaypointList(ProfileMode.CartesianInputJointMotion);
//    	waypoints.addWaypoint(0.500, 0.500, 0.707, 90);
//    	waypoints.addWaypoint(0.500, 0.000, 0.500, 0);
//    	MotionProfile profile = new MotionProfile(waypoints);

    	profile.calculatePath();
    	profile.printOutput();
    	System.out.println("Total time = " + (System.nanoTime() - startTime) / 1000000000.0);
    }
}
