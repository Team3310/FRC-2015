Attribute VB_Name = "P50_Input"
Option Explicit
'-------------------------------------------------------------------------
' Subroutine MoPlan2000_INPUT()                                           |
' OBJECTIVES:                                                             |
'   1. Take user inputs from Input form and assign variable names.        |
'                                                                         |
' INPUTS:                                                                 |
'   (1)                                                                   |
'                                                                         |
'                                                                         |
'-------------------------------------------------------------------------

Sub P50_INPUT(islinear As Boolean, CartAccel1 As Double, CartAccel2 As _
                Double, JNTaccel1() As Double, JNTaccel2() As Double, _
                NumTaught As Integer, CNT() As Double, TaughtPos() As Double, _
                Tupdate As Double, Texp As Double, Vpath() As Double, _
                OutputPoints As Integer, Vjoint() As Double)
                
    Dim I As Integer
    ReDim JNTaccel1(1 To 3), JNTaccel2(1 To 3), Vjoint(1 To 3)
    
    
    'Determine move type and use appropriate accel times.
    If frmInput.optLinear.Value = True Then
        islinear = True
        CartAccel1 = CDbl(Val(frmInput.txtCartAcc1)) / 1000# 'sec
        CartAccel2 = CDbl(Val(frmInput.txtCartAcc2)) / 1000# 'sec
        For I = 1 To 3 'Set joint accelerations to 0.
            JNTaccel1(I) = 0#
            JNTaccel2(I) = 0#
        Next I
    Else
        islinear = False
        'Set linear accelerations to 0.
        CartAccel1 = 0#
        CartAccel2 = 0#
        
        'Joint move must read in joint accelerations
        For I = 1 To 3
            JNTaccel1(I) = CDbl(Val(frmInput.txtJNTAcc1(I))) / 1000# 'sec
            JNTaccel2(I) = CDbl(Val(frmInput.txtJNTAcc2(I))) / 1000# 'sec
        Next I
    End If
    
    'Determine the number of taught points
    For I = 2 To 10
        If frmInput.optNumPoints(I).Value = True Then
            NumTaught = I
        End If
    Next I
    
    'Dimension the taught points from pt. 0 to NumTaught-1
    ReDim TaughtPos(0 To NumTaught - 1, 1 To 3)
    ReDim Vpath(1 To NumTaught - 1)
    
    'If the number of path points is > 2 then redim CNT.
    If NumTaught > 2 Then
        ReDim CNT(1 To NumTaught - 2)
    Else
        ReDim CNT(1 To 1)
        CNT(1) = 0#
    End If
    
    'Read the taught point positions and save to array
    For I = 0 To NumTaught - 1
    
        If islinear = True Then
            TaughtPos(I, 1) = CDbl(Val(frmInput.txtPx(I).Text)) / 1000# 'meters
            TaughtPos(I, 2) = CDbl(Val(frmInput.txtPy(I).Text)) / 1000# 'meters
            TaughtPos(I, 3) = CDbl(Val(frmInput.txtPz(I).Text)) / 1000# 'meters
        Else
            TaughtPos(I, 1) = CDbl(Val(frmInput.txtPx(I).Text)) * Pi / 180 'rad
            TaughtPos(I, 2) = CDbl(Val(frmInput.txtPy(I).Text)) * Pi / 180 'rad
            TaughtPos(I, 3) = CDbl(Val(frmInput.txtPz(I).Text)) * Pi / 180 'rad
        End If
        
    Next I
    
    'Read the Path speed and end condition
    For I = 1 To NumTaught - 1
    
        If islinear = False Then
            Vpath(I) = CDbl(Val(frmInput.txtSpeed(I).Text)) 'Percent
        Else
            Vpath(I) = CDbl(Val(frmInput.txtSpeed(I).Text)) / 1000# 'm/sec
        End If
        
        'Read end condition up to the next to last point
        If I < NumTaught - 1 Then
            CNT(I) = CDbl(Val(frmInput.txtEnd(I).Text))
        End If
        
    Next I
    
    'For joint read the joint speeds
    For I = 1 To 3
        If islinear = False Then
            'Vjoint is now in radians/sec
            Vjoint(I) = CDbl(Val(frmInput.txtJNTSpeed(I).Text)) * Pi / 180#
        Else
            Vjoint(I) = 0#
        End If
    Next I
    
    
    'Change numtaught to indicate number of paths
    NumTaught = NumTaught - 1
        
        
    'Read the controller update rate
    Tupdate = CDbl(frmInput.txtUpdate.Text) / 1000# 'seconds

    'Determine type of servo filter and read value accordingly
    If frmInput.optExp.Value = True Then
        Texp = CDbl(Val(frmInput.txtExp.Text)) / 1000# 'seconds
    Else
        Texp = Tupdate
    End If

    'Read the number of desired output points
    OutputPoints = Val(frmInput.txtOutput.Text)
    
    
    

End Sub
