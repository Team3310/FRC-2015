Attribute VB_Name = "P50_Main"
Option Explicit
Public Const Pi As Double = 3.14159265358979


Sub P50_Main()
Attribute P50_Main.VB_Description = "Calculates servo velocity commands to the robot"
Attribute P50_Main.VB_ProcData.VB_Invoke_Func = "r\n14"

    'Declare all INPUT variables
    Dim itp As Double, Vpath() As Double, OutPoints As Integer
    Dim Ted As Double, islinear As Boolean, CartAccel1 As Double
    Dim CartAccel2 As Double, JNTaccel1() As Double, JNTaccel2() As Double
    Dim NumPaths As Integer, CNT() As Double, TaughtPos() As Double
    Dim JointSpeed() As Double, RealPathSpeed() As Double
    Dim ElbowUp As Boolean, Front As Boolean, CalcDynamics As Boolean
                

    'Declare all OUTPUT variables
    Dim JntPos() As Double, JntAcc() As Double, JntVel() As Double
    Dim CartPos() As Double, CartAcc() As Double, CartVel() As Double
    Dim Tweighted() As Double, AveJntSpeed() As Double
    Dim MaxJntSpeed() As Double, MinJntSpeed() As Double
    Dim MinPathSpeed As Double, MaxPathSpeed As Double
    Dim AvePathSpeed As Double, Length() As Double
    Dim Mass() As Double, CG() As Double, Inertia() As Double
    Dim Torque() As Double, Force() As Double, Trms() As Double
    Dim Tmax() As Double, Tmin() As Double

    'Declare all other variables
    Dim Tseg() As Double, FL1() As Integer, FL2() As Integer
    Dim Npts As Integer, I As Integer, J As Integer
    Dim TotalPts As Integer, Out2() As Double
    Dim ServoOut() As Double, dDis() As Double
    Dim RealPoints As Integer, Tcnt() As Double
    Dim LineLength() As Double, PosFilterIn() As Double
    Dim dDisStep() As Double, NumITPs As Integer
    Dim FlipCond As Boolean, ErrorFlag As Boolean
    Dim WantTorque As Boolean, WantForce As Boolean, Grounded As Boolean
    Dim Theta() As Double, ThetaDot() As Double, Theta2Dot() As Double
    Dim UserAngle() As Double, DOF As Integer
    Dim A() As Double, D() As Double, Alpha() As Double, Q() As Double
    Dim ArmLength() As Double
    
    Screen.MousePointer = vbHourglass
    
    
    'CALL the USER_INPUT subroutine
    Call P50_INPUT.P50_INPUT(islinear, CartAccel1, CartAccel2, _
                 JNTaccel1, JNTaccel2, NumPaths, CNT, TaughtPos, _
                itp, Ted, Vpath, OutPoints, JointSpeed)
                
    'CALL the Dynamics/Kinematics input subroutine
    Call Dynamics.DynamicInput(ArmLength, Mass, CG, Inertia, ElbowUp, Front, _
                               CalcDynamics, WantTorque, WantForce, Grounded)
    
    ReDim Tseg(1 To NumPaths, 1 To 3), LineLength(1 To NumPaths)
    ReDim dDis(1 To NumPaths, 1 To 3)
    
    'Initialize variables
    For I = 1 To NumPaths
        LineLength(I) = 0#
        For J = 1 To 3
            Tseg(I, J) = 0#
            dDis(I, J) = 0#
        Next J
    Next I
    
    'Calculate the x(J1), y(J2), & z(J3) distance for each path.
    For I = 1 To NumPaths
        For J = 1 To 3
            dDis(I, J) = TaughtPos(I, J) - TaughtPos(I - 1, J)
        Next J
    Next I
    
    frmInput.lblInfo.Caption = "Determining Coordinated Motion"
    'call procedure to determine coordinated motion.
    Call MotionPrePlan.CoMotion(NumPaths, dDis, Vpath, JointSpeed, JNTaccel1, _
                                    JNTaccel2, islinear, CNT, RealPathSpeed, _
                                    CartAccel1, CartAccel2, itp, Tseg, LineLength, Tcnt)
    
    
    frmInput.lblInfo.Caption = "Calculating Filter Inputs"
    
    'Procedure calculates all the input positions to the filter or Inverse Kin
    Call MotionPrePlan.FilterInput(dDis, Tseg, Tcnt, TaughtPos, NumPaths, _
                                    PosFilterIn, NumITPs, dDisStep)
                                    
    
    'This portion calculates Inverse Kinematics for a linear move.
    'Modified by PDC on 12/5/00
    If islinear = True Then
        frmInput.lblInfo.Caption = "Calculating Inverse Kinematics"
        Call Kinematics.IKIN(PosFilterIn, NumITPs, ArmLength, ErrorFlag, _
                            ElbowUp, Front)
                            
        'Reshow the input form and notify user of unreachable point
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

    'Runs the RJ-3 joint filter
    Call Filter.Filter(islinear, JNTaccel1, JNTaccel2, CartAccel1, CartAccel2, _
                       NumITPs, PosFilterIn, Out2)
    

    'Input code to call the Servo filters
    Call Filter.ServoFilter(itp, Ted, Out2, NumITPs, ServoOut, RealPoints)
    
    'Servo position [ServoOut()] is actually deltaPos, so we take the
    'joint position of the 1st taught point [PosFilterIn(0,J)] to
    'use as a starting point.
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
    
    'ServoOut is now an array containing User Angles [radians]
    
    
    
    'Calculate Dynamics ONLY if user selects it.
    If CalcDynamics = True Then
        
        frmInput.lblInfo.Caption = "Calculating Forces and Torques"
        
        'Convert User angles into Kinematic angles
        Call Kinematics.UserToKin(ServoOut, RealPoints)
        
        'Read the D-H parameters for current Robot
        Call Dynamics.DH_Parameters(DOF, A, D, Alpha, Q, ArmLength)
        'NOTE: Q (Theta) is 0 only when j=4,5,6; otherwise it is time
        '      dependant based on the inverse kinematics.
        
        'We will redimension ServoOut to contain Q(4,5,6) as its
        '4th, 5th, & 6th elements in its 2nd dimension.
        ReDim Preserve ServoOut(0 To RealPoints, 1 To DOF)
        
        'Set all values of ServoOut(0 to RealPoints,dof) to Q(4,5,6).
        For I = 0 To RealPoints
            ServoOut(I, 4) = Q(4)
            ServoOut(I, 5) = Q(6)
            ServoOut(I, 6) = Q(6)
        Next I
        
        'Start Recursive Newton - Euler
        Call Dynamics.NewtonEuler(RealPoints, ServoOut, Alpha, A, D, CG, _
                                  Inertia, Mass, DOF, Torque, Force)
        
        'Convert Kinematic Angles back to User Angles
        Call Kinematics.KinToUser(ServoOut, RealPoints)
    
    End If
    
    frmInput.lblInfo.Caption = "Calculating Forward Kinematics"
    
    'CALL Kinematic routine to calculate X,Y,Z position
    'The Filter only outputs DeltaPosition in Joint Space.
    Call Kinematics.FKIN(ServoOut, RealPoints, ArmLength, _
                       CartPos, PosFilterIn)
    
    
    'Used for filter output debug.
    'Call MoPlanDebug.MoPlan_Debug(NumITPs, Out2, RealPoints, ServoOut)


    frmInput.lblInfo.Caption = "Calculating Position, Velocity & Acceleration"

    'Call procedure to calculate velocity and acceleration
    Call P50_Post.VelAccPos(RealPoints, ServoOut, CartPos, JntVel, CartVel, _
                            JntAcc, CartAcc, MaxJntSpeed, MinJntSpeed, _
                            AveJntSpeed, MaxPathSpeed, MinPathSpeed, _
                            AvePathSpeed)
    
    
    If CalcDynamics = True Then
        'Call Procedure to Calculate Dynamics Summary Information
        Call P50_Post.Dynamics_Post(Torque, JntVel, RealPoints, Trms, _
                                    Tweighted, Tmax, Tmin, Grounded)
    End If
        

        'Send results to the spreadsheet
    
        Call P50_OUTPUT.P50_OUTPUT(ServoOut, JntAcc, JntVel, CartPos, _
                                   CartVel, CartAcc, RealPoints, OutPoints, _
                                   AveJntSpeed, MinJntSpeed, MaxJntSpeed, _
                                   MaxPathSpeed, MinPathSpeed, AvePathSpeed, _
                                   NumPaths, CalcDynamics, Torque, Tmin, _
                                   Tmax, Trms, Tweighted, Force, WantTorque, _
                                   WantForce)
        
        frmInput.lblInfo.Caption = "Motion Generated!!"
        Screen.MousePointer = vbDefault
    

    End Sub

   
