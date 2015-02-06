Attribute VB_Name = "MotionPrePlan"
Option Explicit
'-------------------------------------------------------------------------
'| Sub CoMotion:
'|     INPUTS  - Npts, dJoint, VpathIn, JointSpeed, Tacc1, Tacc2, IsLinear
'|     OUTPUTS - ActualPathSpeed, T4, PathLength
'|
'|  Description:
'|          Takes input parameters and determines each joint speed
'|      in order to end up at the final point at the same time.  For
'|      linear moves, simply calculates T4 based on path length.
'|      Coordinated motion is automatically achieved for linear move.
'-------------------------------------------------------------------------


Sub CoMotion(Npts As Integer, dJoint() As Double, VpathIn() As Double, _
             JointSpeed() As Double, Tacc1() As Double, Tacc2() As Double, _
             islinear As Boolean, CNT() As Double, ActualPathSpeed() As Double, _
             CartAcc1 As Double, CartAcc2 As Double, itp As Double, _
             T4() As Double, PathLength() As Double, Tcnt() As Double)

     
    
    Dim I As Integer, J As Integer
    ReDim Ttemp(1 To Npts, 1 To 3) As Double, Tmax(1 To Npts) As Double
    ReDim ActualPathSpeed(1 To Npts, 1 To 3), Tcnt(1 To Npts, 1 To 3) As Double
    
    For J = 1 To 3
        Tacc1(J) = Excel.WorksheetFunction.RoundUp(Tacc1(J) / itp, 0)
        Tacc2(J) = Excel.WorksheetFunction.RoundUp(Tacc2(J) / itp, 0)
    Next J
    
    'Convert cartesian acceleration filters to integration points
    CartAcc1 = Excel.WorksheetFunction.RoundUp(CartAcc1 / itp, 0)
    CartAcc2 = Excel.WorksheetFunction.RoundUp(CartAcc2 / itp, 0)
    
    If islinear = False Then
    
        'Calculate each joint speed based on percent speed
        
        For I = 1 To Npts
            Tmax(I) = 0#
            
            
            For J = 1 To 3
                ActualPathSpeed(I, J) = VpathIn(I) / 100# * JointSpeed(J)
                T4(I, J) = Abs(dJoint(I, J)) / ActualPathSpeed(I, J)
                T4(I, J) = Excel.WorksheetFunction.RoundUp(T4(I, J) / itp, 0)
                
                
                'Determines the itp number for the end condition
                If I < Npts Then
                    Tcnt(I, J) = Excel.WorksheetFunction.Round((1 - Sqr(CNT(I) / 100#)) _
                                * (Tacc1(J) + Tacc2(J)), 0)
                Else
                    Tcnt(I, J) = Tacc1(J) + Tacc2(J)
                End If
                
                
                If Abs(dJoint(I, J)) > 0 Then
                    Ttemp(I, J) = T4(I, J) + Tcnt(I, J)
                Else
                    Ttemp(I, J) = T4(I, J)
                End If
            
                If Ttemp(I, J) > Tmax(I) Then
                    Tmax(I) = Ttemp(I, J)
                End If
                
            Next J
            
            'Use longest joint time for every joint speed
            For J = 1 To 3
                 'T4 is in integration points, NOT seconds
                T4(I, J) = Tmax(I) - Tcnt(I, J)
                If T4(I, J) <> 0 Then
                    ActualPathSpeed(I, J) = dJoint(I, J) / (T4(I, J) * itp)
                Else
                    ActualPathSpeed(I, J) = 0#
                End If
            Next J
        Next I
    Else
        
        'Calculate linear speed based on Input Speed
        For I = 1 To Npts
            For J = 1 To 3
               ActualPathSpeed(I, J) = VpathIn(I)
            Next J
        Next I
        
        'Calculate T4 for linear moves
        For I = 1 To Npts
            PathLength(I) = Sqr(dJoint(I, 1) ^ 2 + dJoint(I, 2) ^ 2 + dJoint(I, 3) ^ 2)
            For J = 1 To 3
                T4(I, J) = Abs(PathLength(I) / ActualPathSpeed(I, J))
                T4(I, J) = Excel.WorksheetFunction.RoundUp(T4(I, J) / itp, 0)
                If I < Npts Then
                    Tcnt(I, J) = Excel.WorksheetFunction.Round((1 - Sqr(CNT(I) _
                                / 100#)) * (CartAcc1 + CartAcc2), 0)
                Else
                    Tcnt(I, J) = CartAcc1 + CartAcc2
                End If
            Next J
        Next I
    End If
    
End Sub

Sub FilterInput(dDis() As Double, T4() As Double, Tcnt() As Double, _
                Ptaught() As Double, NumPaths As Integer, _
                PosFilterIn() As Double, NumITPs As Integer, dDisStep() As Double)
                
    Dim I As Integer, J As Integer, k As Integer
    Dim Counter1 As Integer
    ReDim dDisStep(1 To NumPaths, 1 To 3)
    
    NumITPs = 0
    For I = 1 To NumPaths
        NumITPs = NumITPs + CInt(T4(I, 1) + Tcnt(I, 1))
    Next I
    
    ReDim PosFilterIn(0 To NumITPs, 1 To 3)
    'Calculate the input position vs. time for the entire path
    'NOTE: Each axis NumITPs should be the same, that is why we can have a constant
    For J = 1 To 3
        k = 0
        NumITPs = 0
        Counter1 = 0
        'Set the position at time 0 to the 1st taught position
        PosFilterIn(k, J) = Ptaught(k, J)
        k = 1
        For I = 1 To NumPaths
            Counter1 = NumITPs + CInt(T4(I, J))
            NumITPs = Counter1 + CInt(Tcnt(I, J))
            If T4(I, J) <> 0# Then
                dDisStep(I, J) = dDis(I, J) / T4(I, J)
            Else
                dDisStep(I, J) = 0#
            End If
            
            'The following loop determines the filter input position
            'at each ITP. If using joint filter for linear motion,
            'then IKIN can be called after this Subroutine.
            
            Do Until k > NumITPs
                If k <= Counter1 Then
                    PosFilterIn(k, J) = PosFilterIn(k - 1, J) + dDisStep(I, J)
                    k = k + 1
                Else
                    PosFilterIn(k, J) = PosFilterIn(k - 1, J)
                    k = k + 1
                End If
            Loop
        Next I
    Next J
    
    
End Sub


