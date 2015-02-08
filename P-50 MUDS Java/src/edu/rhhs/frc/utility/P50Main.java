package edu.rhhs.frc.utility;

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
    

    double[] RealPathSpeed;
    boolean CalcDynamics = false;
    boolean Grounded = true;

    // Declare all OUTPUT variables
    double[] JntPos; 
    double[] JntAcc; 
    double[] JntVel;
    double[] CartPos; 
    double[] CartAcc; 
    double[] CartVel;
    double[] Tweighted; 
    double[] AveJntSpeed;
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
    double[] Tseg; 
    int[] FL1; 
    int[] FL2;
    int Npts; 
    int TotalPts; 
    double[] Out2;
    double[] ServoOut; 
    double[] dDis;
    int RealPoints; 
    double[] Tcnt;
    double[] LineLength; 
    double[] PosFilterIn;
    double[] dDisStep; 
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

    public static void main(String[] args) {
                            
        ReDim Tseg(1 To NumPath, 1 To 3), LineLength(1 To NumPath)
        ReDim dDis(1 To NumPath, 1 To 3)
        
        // Initialize variables
        For I = 1 To NumPath
            LineLength[i] = 0.0;
            For J = 1 To 3
                Tseg(I, J) = 0.0;
                dDis(I, J) = 0.0;
            Next J
        Next I
        
        // Calculate the x(J1), y(J2), & z(J3) distance for each path.
        For I = 1 To NumPath
            For J = 1 To 3
                dDis(I, J) = TaughtPos(I, J) - TaughtPos(I - 1, J)
            Next J
        Next I
        
        frmInput.lblInfo.Caption = "Determining Coordinated Motion"
        // call procedure to determine coordinated motion.
        Call MotionPrePlan.CoMotion(NumPath, dDis, Vpath, JointSpeed, JNTaccel1, _
                                        JNTaccel2, islinear, CNT, RealPathSpeed, _
                                        CartAccel1, CartAccel2, itp, Tseg, LineLength, Tcnt)
        
        
        frmInput.lblInfo.Caption = "Calculating Filter Inputs"
        
        // Procedure calculates all the input positions to the filter or Inverse Kin
        Call MotionPrePlan.FilterInput(dDis, Tseg, Tcnt, TaughtPos, NumPath, _
                                        PosFilterIn, NumITPs, dDisStep)
                                        
        
        // This portion calculates Inverse Kinematics for a linear move.
        // Modified by PDC on 12/5/00
        If islinear = True Then
            frmInput.lblInfo.Caption = "Calculating Inverse Kinematics"
            Call Kinematics.IKIN(PosFilterIn, NumITPs, ArmLength, ErrorFlag, _
                                ElbowUp, Front)
                                
            // Reshow the input form and notify user of unreachable point
            If ErrorFlag = True Then
                frmDynamics.Hide
                Screen.MousePointer = vbDefault
                frmInput.Show
                frmInput.cmdCalculate.Enabled = False
                frmInput.lblInfo.Caption = "Point Unreachable. Reteach!"
                Exit Sub
            End If
        End If
        
        
        frmInput.lblInfo.Caption = "RJ-3 Filter Running"

        // Runs the RJ-3 joint filter
        Call Filter.Filter(islinear, JNTaccel1, JNTaccel2, CartAccel1, CartAccel2, _
                           NumITPs, PosFilterIn, Out2)
        

        // Input code to call the Servo filters
        Call Filter.ServoFilter(itp, Ted, Out2, NumITPs, ServoOut, RealPoints)
        
        // Servo position [ServoOut()] is actually deltaPos, so we take the
        // joint position of the 1st taught point [PosFilterIn(0,J)] to
        // use as a starting point.
        For I = 0 To RealPoints
            If I = 0 Then
                For J = 1 To 3
                    ServoOut(I, J) = PosFilterIn(I, J)
                Next J
                
            Else
            
                For J = 1 To 3
                    ServoOut(I, J) = ServoOut(I - 1, J) + ServoOut(I, J)
                Next J
                
            End If
        Next I
        
        // ServoOut is now an array containing User Angles [radians]
        
        
        
        // Calculate Dynamics ONLY if user selects it.
        If CalcDynamics = True Then
            
            frmInput.lblInfo.Caption = "Calculating Forces and Torques"
            
            // Convert User angles into Kinematic angles
            Call Kinematics.UserToKin(ServoOut, RealPoints)
            
            // Read the D-H parameters for current Robot
            Call Dynamics.DH_Parameters(DOF, A, D, Alpha, Q, ArmLength)
            // NOTE: Q (Theta) is 0 only when j=4,5,6; otherwise it is time
            //       dependant based on the inverse kinematics.
            
            // We will redimension ServoOut to contain Q(4,5,6) as its
            // 4th, 5th, & 6th elements in its 2nd dimension.
            ReDim Preserve ServoOut(0 To RealPoints, 1 To DOF)
            
            // Set all values of ServoOut(0 to RealPoints,dof) to Q(4,5,6).
            For I = 0 To RealPoints
                ServoOut(I, 4) = Q(4)
                ServoOut(I, 5) = Q(6)
                ServoOut(I, 6) = Q(6)
            Next I
            
            // Start Recursive Newton - Euler
            Call Dynamics.NewtonEuler(RealPoints, ServoOut, Alpha, A, D, CG, _
                                      Inertia, Mass, DOF, Torque, Force)
            
            // Convert Kinematic Angles back to User Angles
            Call Kinematics.KinToUser(ServoOut, RealPoints)
        
        End If
        
        frmInput.lblInfo.Caption = "Calculating Forward Kinematics"
        
        // CALL Kinematic routine to calculate X,Y,Z position
        // The Filter only outputs DeltaPosition in Joint Space.
        Call Kinematics.FKIN(ServoOut, RealPoints, ArmLength, _
                           CartPos, PosFilterIn)
        
        
        // Used for filter output debug.
        // Call MoPlanDebug.MoPlan_Debug(NumITPs, Out2, RealPoints, ServoOut)


        frmInput.lblInfo.Caption = "Calculating Position, Velocity & Acceleration"

        // Call procedure to calculate velocity and acceleration
        Call P50_Post.VelAccPos(RealPoints, ServoOut, CartPos, JntVel, CartVel, _
                                JntAcc, CartAcc, MaxJntSpeed, MinJntSpeed, _
                                AveJntSpeed, MaxPathSpeed, MinPathSpeed, _
                                AvePathSpeed)
        
        
        If CalcDynamics = True Then
            // Call Procedure to Calculate Dynamics Summary Information
            Call P50_Post.Dynamics_Post(Torque, JntVel, RealPoints, Trms, _
                                        Tweighted, Tmax, Tmin, Grounded)
        End If
            

            // Send results to the spreadsheet
        
            Call P50_OUTPUT.P50_OUTPUT(ServoOut, JntAcc, JntVel, CartPos, _
                                       CartVel, CartAcc, RealPoints, OutPoints, _
                                       AveJntSpeed, MinJntSpeed, MaxJntSpeed, _
                                       MaxPathSpeed, MinPathSpeed, AvePathSpeed, _
                                       NumPath, CalcDynamics, Torque, Tmin, _
                                       Tmax, Trms, Tweighted, Force, WantTorque, _
                                       WantForce)
            
            frmInput.lblInfo.Caption = "Motion Generated!!"
            Screen.MousePointer = vbDefault
        

        End Sub
	}
    
}
