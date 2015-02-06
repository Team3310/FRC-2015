Attribute VB_Name = "P50_Post"
Sub VelAccPos(Npts As Integer, JointPos() As Double, CartPos() As Double, _
              JointVel() As Double, CartVel() As Double, _
              JointAccel() As Double, CartAccel() As Double, _
              MaxJVel() As Double, MinJVel() As Double, AveJVel() As Double, _
              MaxPathVel As Double, MinPathVel As Double, AvePathVel As Double)

    'NOTE: 4th array spot on Vel & Acc is for magnitude on a linear move
    
    ReDim JointAccel(0 To Npts, 1 To 3)
    ReDim JointVel(0 To Npts, 1 To 3)
    ReDim CartVel(0 To Npts, 1 To 4)
    ReDim CartAccel(0 To Npts, 1 To 4)
    ReDim MaxJVel(1 To 3)
    ReDim MinJVel(1 To 3)
    ReDim AveJVel(1 To 3)
    
    
    Dim I As Integer, J As Integer
    
    'Calculate X, Y, & Z components of Cartesian Speed & Acceleration
    For J = 1 To 3
    
        'Determine the Start values for V and A
        CartVel(0, J) = 0#
        CartAccel(0, J) = 0#
        
        'Determine Cartesian V and Accel at each millisecond using
        'numerical methods
        For I = 1 To Npts
            CartVel(I, J) = (CartPos(I, J) - CartPos(I - 1, J)) / 0.001
            CartAccel(I, J) = (CartVel(I, J) - CartVel(I - 1, J)) / 0.001
        Next I
    Next J

    'Calculate Magnitude of Cartesian Speed & Acceleration
      For I = 1 To Npts
       CartVel(I, 4) = Sqr(CartVel(I, 1) ^ 2 + CartVel(I, 2) ^ 2 + _
                           CartVel(I, 3) ^ 2)
       CartAccel(I, 4) = Sqr(CartAccel(I, 1) ^ 2 + CartAccel(I, 2) ^ 2 + _
                             CartAccel(I, 3) ^ 2)
      Next I
      
      
    'Calculate J1, J2, and J3 Speed & Acceleration
    For J = 1 To 3
        
        'Determine the Start values for V and A
        JointVel(0, J) = 0#
        JointAccel(0, J) = 0#
        
        'Determine Joint V and Accel at each millisecond using
        'numerical methods
        For I = 1 To Npts
            JointVel(I, J) = (JointPos(I, J) - JointPos(I - 1, J)) / 0.001
            JointAccel(I, J) = (JointVel(I, J) - JointVel(I - 1, J)) / 0.001
        Next I
    Next J
    
    'Determine Max, Min, and Ave joint speeds and path speeds
    'Initialize parameters
    For J = 1 To 3
        MaxJVel(J) = JointVel(0, J)
        MinJVel(J) = JointVel(0, J)
        AveJVel(J) = 0#
    Next J
    AvePathVel = 0#
    MaxPathVel = CartVel(0, 4)
    MinPathVel = CartVel(0, 4)
    For I = 1 To Npts
        For J = 1 To 3
        
            'Determine maximum joint velocity
            If JointVel(I, J) > MaxJVel(J) Then
                MaxJVel(J) = JointVel(I, J)
            End If
    
            'Determine minimum joint velocity
            If JointVel(I, J) < MinJVel(J) Then
                MinJVel(J) = JointVel(I, J)
            End If
            
        Next J
        
        'Determine maximum path speed
        If CartVel(I, 4) > MaxPathVel Then
            MaxPathVel = CartVel(I, 4)
        End If
        
        'Determine minimum path speed
        If CartVel(I, 4) < MinPathVel Then
            MinPathVel = CartVel(I, 4)
        End If
    Next I
    
    'Calculate Average joint and path speeds
    For I = 0 To Npts
        
        'Sum each joint velocity value
        For J = 1 To 3
            AveJVel(J) = AveJVel(J) + Abs(JointVel(I, J))
        Next J
        
        'Sum each path velocity value
        AvePathVel = AvePathVel + Abs(CartVel(I, 4))
        
    Next I
    
    'Calculate the average speed
    For J = 1 To 3
        AveJVel(J) = AveJVel(J) / Npts
    Next J
    
    AvePathVel = AvePathVel / Npts
    
            
    'Convert all joint angles to degrees and all distances to mm.
    For I = 0 To Npts
        For J = 1 To 3
            CartPos(I, J) = CartPos(I, J) * 1000#           'mm
            CartVel(I, J) = CartVel(I, J) * 1000#           'mm/s
            CartAccel(I, J) = CartAccel(I, J) * 1000#       'mm/s^2
            JointPos(I, J) = JointPos(I, J) * 180# / Pi     'deg
            JointVel(I, J) = JointVel(I, J) * 180# / Pi     'deg/s
            JointAccel(I, J) = JointAccel(I, J) * 180# / Pi 'deg/s^2
        Next J
        
        CartVel(I, 4) = CartVel(I, 4) * 1000#               'mm/s
        CartAccel(I, 4) = CartAccel(I, 4) * 1000#           'mm/s^2
        
    Next I
    
    For J = 1 To 3
        AveJVel(J) = AveJVel(J) * 180# / Pi                 'deg/s
        MaxJVel(J) = MaxJVel(J) * 180# / Pi                 'deg/s
        MinJVel(J) = MinJVel(J) * 180# / Pi                 'deg/s
    Next J
    
    MaxPathVel = MaxPathVel * 1000#                         'mm/s
    MinPathVel = MinPathVel * 1000#                         'mm/s
    AvePathVel = AvePathVel * 1000#                         'mm/s
            

End Sub
Sub Dynamics_Post(Torque() As Double, Speed() As Double, NumPoints As Integer, _
                  Trms() As Double, TRVWeighted() As Double, Tmax() As Double, _
                  Tmin() As Double, Grounded As Boolean)

    ReDim Tmax(1 To 3)
    ReDim Tmin(1 To 3)
    
    Dim I As Integer, J As Integer
    
    'If a fourbar or other linkage which grounds J3 motor to the turret
    'then we need to subtract J3 torque from J2 Torque
    If Grounded = True Then
        For I = 0 To NumPoints
            Torque(I, 2, 3) = Torque(I, 2, 3) - Torque(I, 3, 3)
        Next I
    End If
    
    'RMS Torque
    Trms() = RMS(Torque, NumPoints)
    
    '10/3 Weighted for RV
    TRVWeighted() = LifeCalc(Speed, Torque, NumPoints)
    
    'Determine Max & Min Joint Torques
    'Initialize parameters
    For J = 1 To 3
        Tmax(J) = -1000000#
        Tmin(J) = 10000000#
        
    Next J
    
    For I = 0 To NumPoints
        For J = 1 To 3
        
            'Determine maximum joint Torque (Z component is joint torque)
            If Torque(I, J, 3) > Tmax(J) Then
                Tmax(J) = Torque(I, J, 3)
            End If
    
            'Determine minimum joint Torque
            If Torque(I, J, 3) < Tmin(J) Then
                Tmin(J) = Torque(I, J, 3)
            End If
            
        Next J
    Next I

End Sub
 Function LifeCalc(V() As Double, T() As Double, N As Integer) As Double()
    
        Dim Tweighted() As Double
        ReDim Tweighted(1 To 3)
        Dim I As Integer, J As Integer, Num As Long
        Dim SumV As Double
        
        Num = CLng(N)
        
    
        For J = 1 To 3
            Tweighted(J) = 0#
            SumV = 0#
            For I = 0 To Num
                SumV = SumV + Abs(V(I, J))
                Tweighted(J) = Tweighted(J) + Abs(V(I, J)) _
                               * Abs(T(I, J, 3)) ^ (10# / 3#)
            Next I
        
        
            'Calculate weighted Torque
            If SumV <> 0 Then
                Tweighted(J) = (Tweighted(J) / SumV) ^ (3# / 10#)
            Else
                Tweighted(J) = 0#
            End If
        Next J
        
        LifeCalc = Tweighted
        
End Function

Function RMS(T() As Double, Npts As Integer) As Double()
    
    Dim cur_sum As Double
    Dim I As Integer, J As Integer
    Dim Trms() As Double
    ReDim Trms(1 To 3)
    
    For J = 1 To 3
    
        cur_sum = 0#
        For I = 0 To Npts
          cur_sum = cur_sum + T(I, J, 3) * T(I, J, 3)
          
        Next I
        
        Trms(J) = Sqr(cur_sum / (Npts + 1))
        
    Next J
            
    RMS = Trms
End Function
