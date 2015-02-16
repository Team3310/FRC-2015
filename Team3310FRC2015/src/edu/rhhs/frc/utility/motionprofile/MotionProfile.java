package edu.rhhs.frc.utility.motionprofile;

import edu.rhhs.frc.utility.motionprofile.CoordinatedMotion.CoMotionFilterOutput;
import edu.rhhs.frc.utility.motionprofile.Filter.ServoFilterOutput;
import edu.rhhs.frc.utility.motionprofile.Kinematics.IKINOutput;

public class MotionProfile {
	public static final double DEFAULT_CONTROLLER_UPDATE_RATE = 8.0/1000.0; // seconds
	public static final double DEFAULT_PATH_VELOCITY = 1.5;   				// meters/second
	public static final double DEFAULT_CARTESIAN_ACCEL1 = 400.0/1000.0;   	// seconds
	public static final double DEFAULT_CARTESIAN_ACCEL2 = 200.0/1000.0;   	// seconds	
	public static final double DEFAULT_END_TYPE_CNT = 0;   				// meters/second

	public static final double INNER_ARM_LENGTH = 0.5;  // meters
	public static final double OUTER_ARM_LENGTH = 0.5;  // meters	

	protected double[] armLengths = {0, INNER_ARM_LENGTH, OUTER_ARM_LENGTH, 0};   // arm lengths in meters

    protected boolean isElbowUp = true; 
    protected boolean isFront = true; 
    protected boolean isLinear = true;
    
    protected int numPaths;  // Number of paths = number of taught points - 1
    protected double[][] taughtPositions; // path taught xyz positions in meters
    protected double[] pathVelocities;  // m/sec, entry for each path segment
    protected double cartesianAccel1 = DEFAULT_CARTESIAN_ACCEL1;   // linear accel in seconds
    protected double cartesianAccel2 = DEFAULT_CARTESIAN_ACCEL2;   // linear accel in seconds 

    protected double[] endTypeCNT; 

    protected double controllerUpdateRateSec = DEFAULT_CONTROLLER_UPDATE_RATE;  // Controller update rate seconds 
    protected int outputRateMs = 4;   //  Output rate milliseconds
    protected double serverExponentialFilterDecayTime = controllerUpdateRateSec;   // Servo filter exponential decay
    protected boolean isServerExponentialFilterEnable = true;
    
    protected ProfileOutput profileOutput;

    protected double[] jointSpeed = {0.0, 0.0, 0.0}; // Not needed for isLinear = true
    protected double[] jntAccel1 = {0.0, 0.0, 0.0};   // Not needed for isLinear = true 
    protected double[] jntAccel2 = {0.0, 0.0, 0.0};   // Not needed for isLinear = true 
    
    public MotionProfile(double[][] taughtPositions) {
    	this.taughtPositions = taughtPositions;
    	initInputs();
    }

    public MotionProfile(WaypointList waypointList) {
    	this.taughtPositions = waypointList.getCoordinates();
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
	public boolean calculatePaths() {
        
        // Calculate the x(J1), y(J2), & z(J3) distance for each path.
    	double[][] dDis = new double[numPaths][3];
        for (int i = 0; i < numPaths; i++) {
            for (int j = 0; j < 3; j++) {
                dDis[i][j] = taughtPositions[i+1][j] - taughtPositions[i][j];
            }
        }
                		
	    // Convert cartesian acceleration filters to integration points
	    for (int j = 0; j < 3; j++) {
	    	jntAccel1[j] = Math.ceil(jntAccel1[j] / controllerUpdateRateSec);
	    	jntAccel2[j] = Math.ceil(jntAccel2[j] / controllerUpdateRateSec);
	    }
	    
	    cartesianAccel1 = Math.ceil(cartesianAccel1 / controllerUpdateRateSec);
	    cartesianAccel2 = Math.ceil(cartesianAccel2 / controllerUpdateRateSec);

	    // Call procedure to determine coordinated motion.
	    CoordinatedMotion coordinatedMotion = new CoordinatedMotion();
        CoordinatedMotion.CoMotionOutput coMotionOutput = coordinatedMotion.coMotion(	
        		numPaths, dDis, pathVelocities, jointSpeed, jntAccel1, jntAccel2, isLinear, endTypeCNT, 
                cartesianAccel1, cartesianAccel2, controllerUpdateRateSec);

        // Procedure calculates all the input positions to the filter or Inverse Kin
        CoMotionFilterOutput coMotionFilterOutput = coordinatedMotion.filterInput(
        		dDis, coMotionOutput.Tseg, coMotionOutput.Tcnt, taughtPositions, numPaths);
                                                
        // This portion calculates Inverse Kinematics for a linear move.
        // Modified by PDC on 12/5/00
    	IKINOutput iKINOutput = null;
    	Kinematics kinematics = new Kinematics();
    	if (isLinear == true) { 
        	iKINOutput = kinematics.iKIN(coMotionFilterOutput.PosFilter, coMotionFilterOutput.NumITPs, armLengths, isElbowUp, isFront);
                                
            // Reshow the input form and notify user of unreachable point
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
        double[][] Out2 = Filter.filter(isLinear, jntAccel1, jntAccel2, cartesianAccel1, cartesianAccel2, 
        		coMotionFilterOutput.NumITPs, iKINOutput.userAngles);
 
        // Input code to call the Servo filters
        Filter filter = new Filter();
        ServoFilterOutput serverFilterOutput = filter.servoFilter(controllerUpdateRateSec, serverExponentialFilterDecayTime, Out2, coMotionFilterOutput.NumITPs, isServerExponentialFilterEnable);
        
        // Servo position [ServoOut()] is actually deltaPos, so we take the
        // joint position of the 1st taught point [PosFilterIn(0,J)] to
        // use as a starting point.

        for (int i = 0; i < serverFilterOutput.pointCount + 1; i++) {
            if (i == 0) {
                for (int j = 0; j < 3; j++) {
                	serverFilterOutput.ServoOut[i][j] = iKINOutput.userAngles[i][j];
                }
        	}                
            else {
                for (int j = 0; j < 3; j++) {
                	serverFilterOutput.ServoOut[i][j] = serverFilterOutput.ServoOut[i-1][j] + serverFilterOutput.ServoOut[i][j];
                }
        	}
        }
        
        // ServoOut is now an array containing User Angles [radians]
        
        // CALL Kinematic routine to calculate X,Y,Z position
        // The Filter only outputs DeltaPosition in Joint Space.
        double[][] CartPos = Kinematics.fKIN(serverFilterOutput.ServoOut, serverFilterOutput.pointCount, armLengths, iKINOutput.userAngles);
        
        // Call procedure to calculate velocity and acceleration
        profileOutput = Physics.VelAccPos(serverFilterOutput.pointCount, serverFilterOutput.ServoOut, CartPos);

        return true;
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

	public boolean isLinear() {
		return isLinear;
	}

	public void setLinear(boolean isLinear) {
		this.isLinear = isLinear;
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
        
//    	double[][] taughtPositionsIn = {{0.101,0.102,0.103},{0.201,0.202,0.203},{0.301,0.302,0.303}}; // path taught xyz positions in meters
//    	MotionProfile profile = new MotionProfile(taughtPositionsIn);

    	WaypointList waypoints = new WaypointList();
    	waypoints.addWaypoint(0.101,0.102,0.103);
    	waypoints.addWaypoint(0.201,0.202,0.203);
    	waypoints.addWaypoint(0.301,0.302,0.303);

    	MotionProfile profile = new MotionProfile(waypoints);
   	
    	profile.calculatePaths();
    	profile.printOutput();
    	System.out.println("Total time = " + (System.nanoTime() - startTime) / 1000000000.0);
    }
}
