Attribute VB_Name = "Kinematics"
'---------------------------------------------------------------------------
'Subroutine IKIN
'   The P-50 Kinematics Routines assume no J2 or J3 offset
'
'Inputs:
'   (1) Pos() - X,Y,Z position data
'   (2) Numpoints - Number of ITPs in entire path
'   (3) L() - Arm Length Data
'   (4) ElbowUp - To determine the configuration of the outer arm
'   (5) Front - To determine the configuration of the turret
'
'Outputs:
'   (1) ErrorFlag - To notify the Main code that the position is unreachable
'   (2) Pos() - J1, J2, J3 position data
'----------------------------------------------------------------------------

Sub IKIN(Pos() As Double, NumPoints As Integer, L() As Double, _
         ErrorFlag As Boolean, ElbowUp As Boolean, Front As Boolean)
                       
    'NOTE: Pos() enters the Subroutine as a cartesian value
    'and exits a Joint Angle.
    
    Dim J As Integer, I As Integer
    Dim Kappa As Double, C1 As Double, S1 As Double
    Dim C2 As Double, S2 As Double, C3 As Double, S3 As Double
    Dim Den As Double, C2Num As Double, S2Num As Double
    Dim Theta(1 To 3) As Double
    
    ErrorFlag = False
    
    'Loop through each ITP and calculate the IKIN
    For I = 0 To NumPoints
    
        If Front = True Then 'Front reach solution
            If Pos(I, 1) = 0# And Pos(I, 2) = 0# Then
                Theta(1) = 0#
            Else
                Theta(1) = excel.WorksheetFunction.Atan2(Pos(I, 1), Pos(I, 2))
            End If
            
        Else 'Back reach solution
            If Pos(I, 1) = 0# And Pos(I, 2) = 0# Then
                Theta(1) = Pi
            Else
                Theta(1) = excel.WorksheetFunction.Atan2(Pos(I, 1), Pos(I, 2)) _
                           + Pi
            End If
        End If
    
        'Where Theta(3)=asin(Kappa)
        Kappa = (Pos(I, 1) ^ 2 + Pos(I, 2) ^ 2 + Pos(I, 3) ^ 2 - _
                L(2) ^ 2 - L(3) ^ 2) / (2 * L(2) * L(3))
                
        'Check to see if position is reachable & Calculate Theta3
        If Abs(Kappa) > 1# Then
            MsgBox "Position at time = " & NumPoints * 0.001 & _
            " seconds is unreachable", vbExclamation, _
            "Position Unreachable"
            ErrorFlag = True
            Exit Sub
        ElseIf Abs(Kappa) = 1# Then
            Theta(3) = excel.WorksheetFunction.Asin(Kappa)
        ElseIf ElbowUp = True Then
            Theta(3) = excel.WorksheetFunction.Asin(Kappa)
        Else
            Theta(3) = Pi - excel.WorksheetFunction.Asin(Kappa)
        End If
        
        'Calculate Theta2
        '--Define cos and sin of theta1 and theta3
        C1 = Cos(Theta(1))
        C3 = Cos(Theta(3))
        S1 = Sin(Theta(1))
        S3 = Sin(Theta(3))
        
        '--Calcultae the denominator in the S2 and C2 equations
        Den = L(3) ^ 2 + 2 * L(2) * L(3) * S3 + L(2) ^ 2
        
        '--Calculate the numerators in the C2 and S2 equations
        C2Num = Pos(I, 1) * (L(2) * C1 + L(3) * C1 * S3) + _
                Pos(I, 2) * (L(2) * S1 + L(3) * S1 * S3) - _
                Pos(I, 3) * L(3) * C3
        
        
        S2Num = Pos(I, 1) * L(3) * C1 * C3 + _
                Pos(I, 2) * L(3) * S1 * C3 + _
                Pos(I, 3) * (L(2) + L(3) * S3)
                
        '--Determine if in Singularity
        If Den = 0# Then
            MsgBox "Position at time = " & NumPoints * 0.001 & _
            " seconds causes singularity", vbExclamation, _
            "Singularity"
            ErrorFlag = True
            Exit Sub
        Else
            '--Calculate the sin and cos of theta2
            C2 = C2Num / Den
            S2 = S2Num / Den
        End If
        
        '--Calculate Theta2
        If C2 = 0# And S2 = 0# Then
            Theta(2) = 0#
        Else
            Theta(2) = excel.WorksheetFunction.Atan2(C2, S2)
        End If
        
        'Calculate User Angles and reallocate them to pos(i,?)
        Pos(I, 1) = Theta(1)
        Pos(I, 2) = Pi / 2 - Theta(2)
        Pos(I, 3) = Theta(2) + Theta(3) - Pi / 2
    Next I
       
    ErrorFlag = False
End Sub

'This subroutine takes the Joint(User) angles and converts them to Kinematic
'angles.  It is necessary to do this to calculate Newton-Euler Dynamics.

Sub UserToKin(JointPos() As Double, NumPoints As Integer)
    
    Dim I As Integer
    
    For I = 0 To NumPoints
        'Theta1 and J1 are the same
        
        'Theta2 = 90 - J2
        JointPos(I, 2) = Pi / 2 - JointPos(I, 2) 'JointPos(I,2) is now Theta2
        
        'Theta3 = J3 - Theta2 + 90
        JointPos(I, 3) = JointPos(I, 3) - JointPos(I, 2) + Pi / 2
    Next I

End Sub

'Subroutine KinToUser:
'
'This subroutine takes the Kinematic angles and converts them to Joint
'(User) angles. JointPos() starts as Theta(I) and Leaves as J(I)

Sub KinToUser(JointPos() As Double, NumPoints As Integer)
    
    Dim I As Integer
    
    For I = 0 To NumPoints
        'Theta1 and J1 are the same
        
        ' J2 = 90 - Theta2
        JointPos(I, 2) = Pi / 2 - JointPos(I, 2)
        
        ' J3 = Theta3 - J2
        JointPos(I, 3) = JointPos(I, 3) - JointPos(I, 2)
    Next I

End Sub
'---------------------------------------------------------------------------
'Subroutine FKIN
'Inputs:
'   (1) Pos() - Joint (J1, J2, J3) filter output data
'   (2) Numpoints - Number of real points in entire path
'   (3) L() - Arm Length Data
'   (4) InputPos() - Joint input position
'       - We only need InputPos(0,J), where J is 1,2,and 3
'       - InputPos(0,J) is Taught point 0 in joint space.
'
'Outputs:
'   (1) CartPos() - X, Y, Z actual position data
'   (2) Pos() - J1, J2, J3 actual position data
'
'Remarks:
'   (1) Pos() is enters as servo output, but exits as actual joint
'----------------------------------------------------------------------------

Sub FKIN(Pos() As Double, NumPoints As Integer, L() As Double, _
        CartPos() As Double, InputPos() As Double)
        
    Dim I As Integer, J As Integer
    
    ReDim CartPos(0 To NumPoints, 1 To 3)
    
    For I = 0 To NumPoints
    
'-------THE FOLLOWING CODE HAS BEEN MOVED TO THE MAIN SUBASSEMBLY
'-------RIGHT AFTER THE SERVO FILTER SUBROUTINE -----------------
'        If I = 0 Then
'            For J = 1 To 3
'                Pos(I, J) = InputPos(I, J)
'            Next J
'
'        Else
'
'            For J = 1 To 3
'                Pos(I, J) = Pos(I - 1, J) + Pos(I, J)
'            Next J
'
'        End If
        
        
        'We can start FKIN because the FKIN are solved with
        'user angles, not kinematic angles.
        'Start Forward Kinematics
        'X position
        CartPos(I, 1) = Cos(Pos(I, 1)) * (Cos(Pos(I, 3)) * L(3) + _
                        L(2) * Sin(Pos(I, 2)))
        
        'Y position
        CartPos(I, 2) = Sin(Pos(I, 1)) * (Cos(Pos(I, 3)) * L(3) + _
                        L(2) * Sin(Pos(I, 2)))
                        
        'Z position
        CartPos(I, 3) = L(3) * Sin(Pos(I, 3)) + L(2) * Cos(Pos(I, 2))
    Next I
        
End Sub

