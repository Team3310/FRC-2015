package edu.rhhs.frc.utility;

import jdk.nashorn.internal.codegen.CompilerConstants.Call;

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
    int OutPoints = 4;   //  Output rate milliseconds
    double Ted = itp;   // Servo filter exponential decay
    
    boolean ExpEnable = false;
    
    double[] RealPathSpeed;
    boolean CalcDynamics = false;
    boolean Grounded = true;

    // Declare all OUTPUT variables
    double[] JntPos; 
    double[] JntAcc; 
    double[] JntVel;
    double[][] CartPos; 
    double[] CartAcc; 
    double[] CartVel;
    double[] Tweighted; 
    double[] AveJntSpeed;
    double[] MinJntSpeed; 
    double[] MaxJntSpeed; 
    double[] JntSpeed;
    double[] MinPathSpeed; 
    double[] MaxPathSpeed;
    double[] AvePathSpeed; 
    double[] Length;

    
    double[] Mass; 
    double[] CG; 
    double[] Inertia;
    double[] Torque; 
    double[] Force; 

    double[] Trms;
    double[] Tmax; 
    double[] Tmin;

    // Declare all other variables
    double[][] Tseg = new double[NumPath][3];
    int[] FL1; 
    int[] FL2;
    int Npts; 
    int TotalPts; 
    double[][] Out2;
    double[][] ServoOut; 
    double[][] dDis = new double[NumPath][3];
    int RealPoints; 
    double[][] Tcnt;
    double[] LineLength = new double[NumPath]; 
    double[][] PosFilterIn;
    double[][] dDisStep; 
    int NumITPs;
    boolean FlipCond; 
    boolean ErrorFlag;
    boolean WantTorque; 
    boolean WantForce; 
    double[] Theta; 
    double[] ThetaDot; 
    double[] Theta2Dot;
    double[] UserAngle; 
    int DOF;
    double[] A; 
    double[] D; 
    double[] Alpha; 
    double[] Q;

    public P50Main() {
    	
    }
    
    public void calculatePaths() {
        // Initialize variables
        for (int i = 0; i < NumPath; i++) {
            LineLength[i] = 0.0;
            for (int j = 0; j < 3; j++) {
                Tseg[i][j] = 0.0;
                dDis[i][j] = 0.0;
            }
        }
        
        // Calculate the x(J1), y(J2), & z(J3) distance for each path.
        for (int i = 0; i < NumPath; i++) {
            for (int j = 0; j < 3; j++) {
                dDis[i][j] = TaughtPos[i+1][j] - TaughtPos[i][j];
            }
        }
        
        System.out.println("Determining Coordinated Motion");
        		
        // call procedure to determine coordinated motion.
        Tcnt = CoordinatedMotion.coMotion(NumPath, dDis, Vpath, JointSpeed, JNTaccel1, 
                                        JNTaccel2, islinear, CNT, 
                                        CartAccel1, CartAccel2, itp, Tseg, LineLength);

        // RealPathSpeed missing...
        System.out.println("Calculating Filter Inputs");

        // Procedure calculates all the input positions to the filter or Inverse Kin
        CoordinatedMotion.filterInput(dDis, Tseg, Tcnt, TaughtPos, NumPath, 
                                        PosFilterIn, NumITPs, dDisStep);
                                                
        // This portion calculates Inverse Kinematics for a linear move.
        // Modified by PDC on 12/5/00
        if (islinear == true) { 
        	System.out.println("Calculating Inverse Kinematics");
        	PosFilterIn = Kinematics.iKIN(PosFilterIn, NumITPs, ArmLength, ErrorFlag, 
                                ElbowUp, Front);
                                
            // Error flag missing...
            
            // Reshow the input form and notify user of unreachable point
            if (ErrorFlag == true) { 
                System.out.println("Point Unreachable. Reteach!");
            }
        }
        
        System.out.println("RJ-3 Filter Running");

        // Runs the RJ-3 joint filter
        Out2 = Filter.filter(islinear, JNTaccel1, JNTaccel2, CartAccel1, CartAccel2, 
                           NumITPs, PosFilterIn);
 
        // Input code to call the Servo filters
        ServoOut = Filter.servoFilter(itp, Ted, Out2, NumITPs, RealPoints, ExpEnable);
        
        // Servo position [ServoOut()] is actually deltaPos, so we take the
        // joint position of the 1st taught point [PosFilterIn(0,J)] to
        // use as a starting point.

        for (int i = 0; i < RealPoints; i++) {
            if (i == 0) {
                for (int j = 0; j < 3; j++) {
                    ServoOut[i][j] = PosFilterIn[i][j];
                }
        	}                
            else {
                for (int j = 0; j < 3; j++) {
                    ServoOut[i][j] = ServoOut[i-1][j] + ServoOut[i][j];
                }
        	}
        }
        
        // ServoOut is now an array containing User Angles [radians]
                
        System.out.println("Calculating Forward Kinematics");
        
        // CALL Kinematic routine to calculate X,Y,Z position
        // The Filter only outputs DeltaPosition in Joint Space.
        CartPos = Kinematics.fKIN(ServoOut, RealPoints, ArmLength, PosFilterIn);
        
        // Used for filter output debug.
        // Call MoPlanDebug.MoPlan_Debug(NumITPs, Out2, RealPoints, ServoOut)


        System.out.println("Calculating Position, Velocity & Acceleration");

        // Call procedure to calculate velocity and acceleration
        P50Post p50Post = new P50Post(Npts);
        p50Post.VelAccPos(ServoOut, CartPos);

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
    	P50Main main = new P50Main();
    	main.calculatePaths();
    }
                                                    
}

