Attribute VB_Name = "Dynamics"
Option Explicit
Const Gravity As Double = 9.807

'Sub DynamicInput takes the user input and saves to variables

Sub DynamicInput(Length() As Double, Mass() As Double, CG() As Double, _
                 Inertia() As Double, ElbowUp As Boolean, Front As Boolean, _
                 CalcDynamics As Boolean, WantTorque As Boolean, _
                 WantForces As Boolean, Grounded As Boolean)

    ReDim Mass(1 To 4)
    ReDim CG(1 To 4, 1 To 3) '(2nd dim x,y,z)
    ReDim Inertia(1 To 4, 1 To 3) '(2nd dim Ixx, Iyy, Izz)
    ReDim Length(2 To 3) 'Only axis 2 and 3 have length
    
    Dim I As Integer, J As Integer
    
    'Read the Arm lengths from the input sheet
    Length(2) = Val(frmDynamics.txtLength(2).Text) / 1000#
    Length(3) = Val(frmDynamics.txtLength(3).Text) / 1000#
    
    'Determine which Inverse Kinematic Solution to use.
    'Axis 3 Configuration
    If frmDynamics.optElbowUp = True Then
        ElbowUp = True
    Else
        ElbowUp = False
    End If
    
    'Axis 1 Configuration
    If frmDynamics.optFrontReach = True Then
        Front = True
    Else
        Front = False
    End If
    
    'Read the mass, cg, and inertia data from Dynamics Input Sheet
    'only if the dynamics is enabled
    If frmDynamics.optDynamics = True Then
        CalcDynamics = True
        For I = 1 To 4
            Mass(I) = CDbl(Val(frmDynamics.txtMass(I).Text))
            CG(I, 1) = CDbl(Val(frmDynamics.txtCGx(I).Text)) / 1000#
            CG(I, 2) = CDbl(Val(frmDynamics.txtCGy(I).Text)) / 1000#
            CG(I, 3) = CDbl(Val(frmDynamics.txtCGz(I).Text)) / 1000#
            Inertia(I, 1) = CDbl(Val(frmDynamics.txtIxx(I).Text))
            Inertia(I, 2) = CDbl(Val(frmDynamics.txtIyy(I).Text))
            Inertia(I, 3) = CDbl(Val(frmDynamics.txtIzz(I).Text))
        Next I
        
        If frmDynamics.chkWantForce.Value = Checked Then
            WantForces = True
        End If
        
        If frmDynamics.chkWantTorque.Value = Checked Then
            WantTorque = True
        End If
        
        If frmDynamics.chkGrounded.Value = Checked Then
            Grounded = True
        End If
        
    Else
    
        For I = 1 To 4
            Mass(I) = 0
            CG(I, 1) = 0
            CG(I, 2) = 0
            CG(I, 3) = 0
            Inertia(I, 1) = 0
            Inertia(I, 2) = 0
            Inertia(I, 3) = 0
        Next I
        
        CalcDynamics = False
        WantTorque = False
        WantForces = False
        Grounded = False
    End If
    
End Sub

'Subroutine DH_Parameters provides the Denavit-Hartenburg parameters
'needed to perform the recursive Newton-Euler Dynamics.

Sub DH_Parameters(NumAxes As Integer, A() As Double, D() As Double, _
                   Alpha() As Double, Q() As Double, L() As Double)

Dim J As Integer

NumAxes = 6 'The 4th, 5th, & 6th axes are fixed to the outer arm

ReDim A(1 To NumAxes), D(1 To NumAxes), Alpha(1 To NumAxes), Q(1 To NumAxes)

'D-H Parameters for 6 axis P-50 Robot, with wrist fixed
A(1) = 0#
A(2) = L(2)
A(3) = 0#
A(4) = 0#
A(5) = 0#
A(6) = 0#

D(1) = 0#
D(2) = 0#
D(3) = 0#
D(4) = L(3)
D(5) = 0#
D(6) = 0#

Alpha(1) = Pi / 2#
Alpha(2) = 0#
Alpha(3) = Pi / 2#
Alpha(4) = -Pi / 2#
Alpha(5) = Pi / 2#
Alpha(6) = 0#

'Initialize all joint angles
'NOTE: q(4-6) have a constant value of 0. All others are time dependant
For J = 1 To NumAxes
    Q(J) = 0#
Next J

End Sub

'This Function fills the rotation matrix iRi-1 with the correct data.
Function jRj_1(Theta() As Double, Alpha() As Double, _
               I As Integer, J As Integer) As Double()

    'Remember that Theta is the actual output from the servo and
    'is the only time dependant variable. I represents time and
    'J represents the joint angle

    Dim Rot(1 To 3, 1 To 3) As Double
    
    Rot(1, 1) = Cos(Theta(I, J))
    Rot(2, 1) = -Cos(Alpha(J)) * Sin(Theta(I, J))
    Rot(3, 1) = Sin(Alpha(J)) * Sin(Theta(I, J))
    Rot(1, 2) = Sin(Theta(I, J))
    Rot(2, 2) = Cos(Alpha(J)) * Cos(Theta(I, J))
    Rot(3, 2) = -Sin(Alpha(J)) * Cos(Theta(I, J))
    Rot(1, 3) = 0#
    Rot(2, 3) = Sin(Alpha(J))
    Rot(3, 3) = Cos(Alpha(J))
    
    jRj_1 = Rot

End Function

'This Function fills the rotation matrix i-1Ri with the correct data.
Function j_1Rj(Theta() As Double, Alpha() As Double, _
               I As Integer, J As Integer) As Double()

    'Remember that Theta is the actual output from the servo and
    'is the only time dependant variable. I represents time and
    'J represents the joint angle

    Dim Rot(1 To 3, 1 To 3) As Double
    
    Rot(1, 1) = Cos(Theta(I, J))
    Rot(2, 1) = Sin(Theta(I, J))
    Rot(3, 1) = 0#
    Rot(1, 2) = -Cos(Alpha(J)) * Sin(Theta(I, J))
    Rot(2, 2) = Cos(Alpha(J)) * Cos(Theta(I, J))
    Rot(3, 2) = Sin(Alpha(J))
    Rot(1, 3) = Sin(Alpha(J)) * Sin(Theta(I, J))
    Rot(2, 3) = -Sin(Alpha(J)) * Cos(Theta(I, J))
    Rot(3, 3) = Cos(Alpha(J))
    
    j_1Rj = Rot

End Function

'Function jRj is the position vector from the i-1 origin to
'the i origin in the frame of the i origin.  It is a constant
'for each axis on this robot.

Function jRj(A() As Double, D() As Double, Alpha() As Double, _
             J As Integer) As Double()
             
    Dim Rvec(1 To 3) As Double
    
    Rvec(1) = A(J)
    Rvec(2) = D(J) * Sin(Alpha(J))
    Rvec(3) = D(J) * Cos(Alpha(J))

    jRj = Rvec
    
End Function

'---------------------------------------------------------------------
'Subroutine NewtonEuler
'
' This subroutine performs the recursive N-E formulation for dynamics.
' The outputs are Force and torque at each axis.  We define a 4th axis
' for this robot to get the forces and torques at the wrist center.
' This information will be used to determine the wrist loads due
' to major axis motion and will be needed to help size the wrist.
'
' INPUTS:
'   (1) Joint Speeds and Accelerations
'   (2) D-H Parameters
'   (3) Robot Geometry
'   (4) Robot Mass Properties
'
' OUTPUTS:
'   (1) Time History of each axis' torque and forces
'       i. JointTorque(0 to RealPoints, 1 to 4) - Used for Motor sizing
'      ii. JointMoment(0 to realpoints, 1 to 4, 1 to 2)
'          a. The 3rd dimension is for the 2 other moments used
'             for bearing sizing.
'     iii. JointForce(0 to RealPoints, 1 to 4, 1 to 3)
'          a. The 3rd dimension is to store all 3 components
'             of force.
'------------------------------------------------------------------------
Sub NewtonEuler(Npts As Integer, Theta() As Double, Alpha() As Double, _
                A() As Double, D() As Double, CG() As Double, _
                Inertia() As Double, Mass() As Double, DOF As Integer, _
                JointTorque() As Double, JointForce() As Double)
    
    Dim I As Integer, J As Integer, K As Integer
    Dim M As Integer, N As Integer
    
    Dim LinkCG(1 To 3) As Double
    Dim LinkInertia(1 To 3, 1 To 3) As Double
    Dim LinkMass As Double
    

    'Variables to save joint speed and accel as vectors
    Dim ThetaDotVec(1 To 3) As Double
    Dim Theta2DotVec(1 To 3) As Double
    
    'Rotational velocities and accelerations
    '1st dimension is joint axis, second is vector component
    Dim W() As Double
    ReDim W(1 To DOF, 1 To 3)
    
    Dim Wdot() As Double
    ReDim Wdot(1 To DOF, 1 To 3)
    
    'Temporary values to speed through N-E
    Dim Wold() As Double
    Dim WdotOld() As Double
    Dim Wnew() As Double
    Dim WdotNew() As Double
    
    ReDim Wold(1 To 3)
    ReDim WdotOld(1 To 3)
    ReDim Wnew(1 To 3)
    ReDim WdotNew(1 To 3)
    

    'Linear velocities and accelerations
    
    Dim V() As Double
    Dim Vdot() As Double
    Dim VdotCG() As Double
    
    ReDim V(1 To DOF, 1 To 3)
    ReDim Vdot(1 To DOF, 1 To 3)
    ReDim VdotCG(1 To DOF, 1 To 3)
    
    
    'Temporary values to speed through N-E
    Dim Vold() As Double
    Dim VdotOld() As Double
    Dim Vnew() As Double
    Dim VdotNew() As Double
    Dim VdotCGNew() As Double
    
    ReDim Vold(1 To 3)
    ReDim VdotOld(1 To 3)
    ReDim Vnew(1 To 3)
    ReDim VdotNew(1 To 3)
    ReDim VdotCGNew(1 To 3)
    
    'Redimension Forces and Moments
    ReDim JointForce(0 To Npts, 1 To DOF, 1 To 3)
    ReDim JointTorque(0 To Npts, 1 To DOF, 1 To 3)
    
    'Temporary Forces & Moments to speed through N-E
    Dim Fold() As Double, Mold() As Double
    Dim Fnew() As Double, Mnew() As Double
    Dim InertiaF() As Double, InertiaM() As Double
    
    ReDim Fold(1 To 3)
    ReDim Mold(1 To 3)
    ReDim Fnew(1 To 3)
    ReDim Mnew(1 To 3)
    ReDim InertiaF(1 To 3)
    ReDim InertiaM(1 To 3)
    
    
'Cycle through each time step
    For I = 0 To Npts
    
    'FORWARD COMPUTATION to solve for the velocities & accels.
        'STEP 1---Set base velocities to 0 and base accels to 0.
        For K = 1 To 3
            Wold(K) = 0#
            WdotOld(K) = 0#
            Vold(K) = 0#
            VdotOld(K) = 0#
        Next K
        
        'However, The acceleration due to gravity can be simulated by
        'saying the base frame is accelerating upward with 1g.
        'Added Wall mount or Floor Mount Option
        If frmDynamics.optFloor = True Then
            
            VdotOld(3) = Gravity
        
        ElseIf frmDynamics.optWall = True Then
            VdotOld(1) = -Gravity
        
        End If
        
        
        
        
        'STEP 2---Propogate Velocities and Accelerations
        For J = 1 To DOF
            'Takes the time history joint angle (speed/accel)  and
            'puts it in a vector - z*Theta, where z is a unit vector.
            
            'Calculates rotational velocity
            ThetaDotVec(1) = 0#
            ThetaDotVec(2) = 0#
            
            If I = 0 Then
                ThetaDotVec(3) = 0#
            Else
                ThetaDotVec(3) = (Theta(I, J) - Theta(I - 1, J)) / 0.001
            End If
            
            'Calculates rotational acceleration
            Theta2DotVec(1) = 0#
            Theta2DotVec(2) = 0#
            
            If I = 0 Then
                Theta2DotVec(3) = 0#
            ElseIf I = 1 Then
                Theta2DotVec(3) = ThetaDotVec(3) / 0.001
            Else
                'Accel = (velNow - velPrior) / time step
                Theta2DotVec(3) = (ThetaDotVec(3) - (Theta(I - 1, J) _
                                  - Theta(I - 2, J)) / 0.001) / 0.001
            End If
            
            'Take the Center of Gravity and put into a 1-d array
            'The Center of gravity is read in as a 2-d array with
            'the second dimension being the joint the cg is for.
            If J = 4 Or J = 5 Then
                LinkCG(1) = 0#
                LinkCG(2) = 0#
                LinkCG(3) = 0#
            ElseIf J = 6 Then
                LinkCG(1) = CG(4, 1)
                LinkCG(2) = CG(4, 2)
                LinkCG(3) = CG(4, 3)
            Else
                LinkCG(1) = CG(J, 1)
                LinkCG(2) = CG(J, 2)
                LinkCG(3) = CG(J, 3)
            End If
            
            'Step 2a  - Equation for a revolute joint
            '         - Calculates i absolute rot vel in i frame
            Wnew = MVecMult(jRj_1(Theta, Alpha, I, J), _
                               VecAdd(Wold, ThetaDotVec))
            
            'Step 2b - Calculates i absolute rot acc in i frame
            WdotNew = MVecMult(jRj_1(Theta, Alpha, I, J), _
                      VecAdd(WdotOld, VecAdd(Theta2DotVec, _
                      CrossProd(Wold, ThetaDotVec))))
                      
            'Step 2c - Calculates i origin linear velocity in i frame
            Vnew = VecAdd(MVecMult(jRj_1(Theta, Alpha, I, J), Vold), _
                   CrossProd(Wnew, jRj(A, D, Alpha, J)))
            
            'Step 2d - Calculates i origin linear accel in i frame
            VdotNew = VecAdd(MVecMult(jRj_1(Theta, Alpha, I, J), VdotOld), _
                      VecAdd(CrossProd(WdotNew, jRj(A, D, Alpha, J)), _
                      CrossProd(Wnew, CrossProd(Wnew, jRj(A, D, Alpha, J)))))
            
            'Step 2e - Calculates i CG linear accel in i frame
            VdotCGNew = VecAdd(VdotNew, VecAdd(CrossProd(WdotNew, LinkCG), _
                     CrossProd(Wnew, CrossProd(Wnew, LinkCG))))
                     
            
            'Write all the temporary Ws and Vs to arrays in order to save
            'link 1 through link 4 values.  The base values are not needed.
            For K = 1 To 3
                W(J, K) = Wnew(K)
                Wdot(J, K) = WdotNew(K)
                Vdot(J, K) = VdotNew(K)
                VdotCG(J, K) = VdotCGNew(K)
            Next K
            
            Wold = Wnew
            WdotOld = WdotNew
            Vold = Vnew
            VdotOld = VdotNew
            
                     
        Next J
        
    'BACKWARD COMPUTATION to calculate joint forces and torques
       'STEP 1---Set tooltip force and moment to initial condition
        For K = 1 To 3
            Fold(K) = 0#
            Mold(K) = 0#
        Next K
        
        
        For J = DOF To 1 Step -1
        
        
            'Take the Center of Gravity and put into a 1-d array
            'The Center of gravity is read in as a 2-d array with
            'the second dimension being the joint the cg is for.
            'Initialize the Inertia Matrix to 0
            'Need to get mass as a scalar for the Inertia equation
            For M = 1 To 3
                For N = 1 To 3
                    LinkInertia(M, N) = 0#
                Next N
            Next M
            
            If J = 4 Or J = 5 Then
                LinkInertia(1, 1) = 0#
                LinkInertia(2, 2) = 0#
                LinkInertia(3, 3) = 0#
                LinkCG(1) = 0#
                LinkCG(2) = 0#
                LinkCG(3) = 0#
                LinkMass = 0#
            ElseIf J = 6 Then
                LinkInertia(1, 1) = Inertia(4, 1)
                LinkInertia(2, 2) = Inertia(4, 2)
                LinkInertia(3, 3) = Inertia(4, 3)
                LinkCG(1) = CG(4, 1)
                LinkCG(2) = CG(4, 2)
                LinkCG(3) = CG(4, 3)
                LinkMass = Mass(4)
            Else
                LinkInertia(1, 1) = Inertia(J, 1)
                LinkInertia(2, 2) = Inertia(J, 2)
                LinkInertia(3, 3) = Inertia(J, 3)
                LinkCG(1) = CG(J, 1)
                LinkCG(2) = CG(J, 2)
                LinkCG(3) = CG(J, 3)
                LinkMass = Mass(J)
            End If
            
            'We need the link velocities and accels for each link
            For K = 1 To 3
                Wnew(K) = W(J, K)
                WdotNew(K) = Wdot(J, K)
                VdotNew(K) = Vdot(J, K)
                VdotCGNew(K) = VdotCG(J, K)
            Next K
            
            'Calculate Inertial Force and Moment
            InertiaF = MVecByScalar(LinkMass, VdotCGNew)
            InertiaM = VecAdd(MVecMult(LinkInertia, WdotNew), _
                       CrossProd(Wnew, MVecMult(LinkInertia, Wnew)))
            
            'Calculate Link i Force at Link i-1 origin in i frame
            Fnew = VecAdd(Fold, InertiaF)
            Mnew = VecAdd(Mold, VecAdd(CrossProd(VecAdd(jRj(A, D, Alpha, J), _
                   LinkCG), Fnew), VecSub(InertiaM, CrossProd(LinkCG, Fold))))
                   
            'Calculate Link i Force at Link i-1 origin in i-1 frame
            Fnew = MVecMult(j_1Rj(Theta, Alpha, I, J), Fnew)
            Mnew = MVecMult(j_1Rj(Theta, Alpha, I, J), Mnew)
            
            
            'Save the Joint Forces and torques to each axis at each
            'time step for each x,y,z component
            For K = 1 To 3
                JointForce(I, J, K) = Fnew(K)
                JointTorque(I, J, K) = Mnew(K)
            Next K
            
            Fold = Fnew
            Mold = Mnew
            
        Next J
    Next I

    

End Sub
