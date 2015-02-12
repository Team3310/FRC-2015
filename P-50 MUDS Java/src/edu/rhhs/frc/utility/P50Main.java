package edu.rhhs.frc.utility;

import edu.rhhs.frc.utility.CoordinatedMotion.CoMotionFilterOutput;
import edu.rhhs.frc.utility.Filter.ServoFilterOutput;
import edu.rhhs.frc.utility.Kinematics.IKINOutput;
import edu.rhhs.frc.utility.P50Post.P50PostOutput;


public class P50Main {

    double[] ArmLength = {0, 0.5, 0.5, 0};   // arm lengths in meters
    boolean ElbowUp = true; 
    boolean Front = true; 

    boolean islinear = true; 

    // isLinear == true
    int NumPath = 1;  // Number of paths = number of taught points - 1
    double[][] TaughtPos = {{0.101,0.102,0.103},{0.201,0.202,0.203}}; // path taught xyz positions in meters
    double[] Vpath = {1.5};  // m/sec 
    double CartAccel1 = 400.0/1000.0;   // linear accel in seconds
    double CartAccel2 = 200.0/1000.0;   // linear accel in seconds 
    double[] JointSpeed = {0.0, 0.0, 0.0}; // Not needed for isLinear = true
    double[] JNTaccel1 = {0.0, 0.0, 0.0};   // Not needed for isLinear = true 
    double[] JNTaccel2 = {0.0, 0.0, 0.0};   // Not needed for isLinear = true 
    
    // isLinear == false
//    int NumPath = 1; 
//    double[][] TaughtPos = {{Math.toRadians(45),Math.toRadians(45),Math.toRadians(45)},{Math.toRadians(90),Math.toRadians(90),Math.toRadians(90)}}; // path taught joint angle in rad
//    double[] Vpath = {100.0};  // joint velocities in percent 
//    double[] JointSpeed = {Math.toRadians(90), Math.toRadians(90), Math.toRadians(90)};  // J1, J2, J3 joint velocities in rad/sec
//    double[] JNTaccel1 = {400.0/1000.0, 400.0/1000.0, 400.0/1000.0};   // J1,J2,J3 joint accel in seconds 
//    double[] JNTaccel2 = {200.0/1000.0, 200.0/1000.0, 200.0/1000.0};   // J1,J2,J3 joint accel in seconds 
//    double CartAccel1 = 0; // Not needed for isLinear = false  
//    double CartAccel2 = 0; // Not needed for isLinear = false 

    double[] CNT = {0}; 

    double itp = 8.0/1000.0;  // Controller update rate seconds 
    int outPoints = 4;   //  Output rate milliseconds
    double Ted = itp;   // Servo filter exponential decay
    boolean ExpEnable = false;

    public P50Main() {
    	
    }
    
    public void calculatePaths() {
        
        // Calculate the x(J1), y(J2), & z(J3) distance for each path.
    	double[][] dDis = new double[NumPath][3];
        for (int i = 0; i < NumPath; i++) {
            for (int j = 0; j < 3; j++) {
                dDis[i][j] = TaughtPos[i+1][j] - TaughtPos[i][j];
            }
        }
        
        System.out.println("Determining Coordinated Motion");
        		
	    // Convert cartesian acceleration filters to integration points
	    for (int j = 0; j < 3; j++) {
	    	JNTaccel1[j] = Math.ceil(JNTaccel1[j] / itp);
	    	JNTaccel2[j] = Math.ceil(JNTaccel2[j] / itp);
	    }
	    
	    CartAccel1 = Math.ceil(CartAccel1 / itp);
	    CartAccel2 = Math.ceil(CartAccel2 / itp);

	    // Call procedure to determine coordinated motion.
	    CoordinatedMotion coordinatedMotion = new CoordinatedMotion();
        CoordinatedMotion.CoMotionOutput coMotionOutput = coordinatedMotion.coMotion(	
        		NumPath, dDis, Vpath, JointSpeed, JNTaccel1, JNTaccel2, islinear, CNT, 
                CartAccel1, CartAccel2, itp);

        // RealPathSpeed missing...
        System.out.println("Calculating Filter Inputs");

        // Procedure calculates all the input positions to the filter or Inverse Kin
        CoMotionFilterOutput coMotionFilterOutput = coordinatedMotion.filterInput(
        		dDis, coMotionOutput.Tseg, coMotionOutput.Tcnt, TaughtPos, NumPath);
                                                
        // This portion calculates Inverse Kinematics for a linear move.
        // Modified by PDC on 12/5/00
    	IKINOutput iKINOutput = null;
    	Kinematics kinematics = new Kinematics();
    	if (islinear == true) { 
        	System.out.println("Calculating Inverse Kinematics");
        	iKINOutput = kinematics.iKIN(coMotionFilterOutput.PosFilter, coMotionFilterOutput.NumITPs, ArmLength, ElbowUp, Front);
                                
            // Reshow the input form and notify user of unreachable point
            if (iKINOutput.errorFlag == true) { 
                System.out.println("Point Unreachable. Reteach!");
            }
        }
    	else {
    		iKINOutput = kinematics.getInstanceIKINOutput(coMotionFilterOutput.NumITPs);
    	}
        
        System.out.println("RJ-3 Filter Running");

        // Runs the RJ-3 joint filter
        double[][] Out2 = Filter.filter(islinear, JNTaccel1, JNTaccel2, CartAccel1, CartAccel2, 
        		coMotionFilterOutput.NumITPs, iKINOutput.userAngles);
 
        // Input code to call the Servo filters
        Filter filter = new Filter();
        ServoFilterOutput serverFilterOutput = filter.servoFilter(itp, Ted, Out2, coMotionFilterOutput.NumITPs, ExpEnable);
        
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
                
        System.out.println("Calculating Forward Kinematics");
        
        // CALL Kinematic routine to calculate X,Y,Z position
        // The Filter only outputs DeltaPosition in Joint Space.
        double[][] CartPos = Kinematics.fKIN(serverFilterOutput.ServoOut, serverFilterOutput.pointCount, ArmLength, iKINOutput.userAngles);
        
        // Used for filter output debug.
        // Call MoPlanDebug.MoPlan_Debug(NumITPs, Out2, RealPoints, ServoOut)

        System.out.println("Calculating Position, Velocity & Acceleration");

        // Call procedure to calculate velocity and acceleration
        P50Post p50Post = new P50Post();
        P50PostOutput p50PostOutput = p50Post.VelAccPos(serverFilterOutput.pointCount, serverFilterOutput.ServoOut, CartPos);

        p50Post.output(p50PostOutput, outPoints);

        // Send results to the spreadsheet 
//        Call P50_OUTPUT.P50_OUTPUT(ServoOut, JntAcc, JntVel, CartPos, 
//                                   CartVel, CartAcc, RealPoints, OutPoints, 
//                                   AveJntSpeed, MinJntSpeed, MaxJntSpeed, 
//                                   MaxPathSpeed, MinPathSpeed, AvePathSpeed, 
//                                   NumPath, CalcDynamics, Torque, Tmin,
//                                   Tmax, Trms, Tweighted, Force, WantTorque,
//                                   WantForce);
        System.out.println("Motion Generated!!");
	} 
                    
    public static void main(String[] args) {
    	long startTime = System.nanoTime();
    	P50Main main = new P50Main();
    	main.calculatePaths();
    	System.out.println("Total time = " + (System.nanoTime() - startTime) / 1000000000.0);
    }
                                                    
}

