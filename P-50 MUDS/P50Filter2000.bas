Attribute VB_Name = "Filter"
Option Explicit
'************************************************************************************
' Subroutine Filter
'
' OBJECTIVES:
'   (1) Takes a step input and returns a second order curve output.
'
'
' REVISIONS:
'   AUTHOR              DESCRIPTION                                          DATE
'    (1) PDC    - Initial Release                                            03 MAR 98
'    (2) PDC    - Changed the Filter Length Problem for Short Motion.        28 MAR 98
'    (3) PDC    - Changed to take any input and filter it.                   16 AUG 00
'
'*************************************************************************************

Sub Filter(islinear As Boolean, Tacc1() As Double, Tacc2() As Double, _
            CartAcc1 As Double, CartAcc2 As Double, NumITPs As Integer, _
            PosIn() As Double, Out2() As Double)

    Dim Fil1() As Double, Fil2() As Double
    Dim Out1In2 As Double
    Dim J As Integer, I As Integer, k As Integer
    Dim FL1 As Integer, FL2 As Integer
    
    ReDim Out2(0 To NumITPs, 1 To 3) As Double
    
    If islinear = True Then
        FL1 = CInt(CartAcc1)
        FL2 = CInt(CartAcc2)
        ReDim Fil1(1 To FL1), Fil2(1 To FL2)
    End If
    
    For k = 1 To 3
            
        If islinear = False Then
            FL1 = CInt(Tacc1(k))
            FL2 = CInt(Tacc2(k))
            ReDim Fil1(1 To FL1), Fil2(1 To FL2)
        End If
    
        '-------INITIALIZE FILTERS TO ZERO---------
        For I = 1 To FL1 Step 1
            Fil1(I) = 0#
        Next I
    
        For I = 1 To FL2 Step 1
            Fil2(I) = 0#
        Next I
             
        Out2(0, k) = 0#
        
        
        'LOOP UNTIL FILTERS ARE CLEAR (NumITPs)
        For I = 1 To NumITPs
            Out1In2 = 0#
            'MOVE FILTER 1 FILTER VALUES TO THE NEXT STEP
            For J = FL1 To 2 Step -1
                Fil1(J) = Fil1(J - 1)
                Out1In2 = Out1In2 + Fil1(J)
            Next J
            
            'ADD DeltaPos TO FILTER 1'S FIRST STEP
            Fil1(1) = PosIn(I, k) - PosIn(I - 1, k)
            
            'DEFINE THE OUTPUT FROM FILTER 1
            Out1In2 = Out1In2 + Fil1(1)
            Out1In2 = Out1In2 / FL1
            Out2(I, k) = 0#
        
            'MOVE FILTER 2 FILTER VALUES TO THE NEXT STEP
            For J = FL2 To 2 Step -1
                Fil2(J) = Fil2(J - 1)
                Out2(I, k) = Out2(I, k) + Fil2(J)
            Next J
            
            'FILTER 2 1st value set to FILTER 1 out
            Fil2(1) = Out1In2
            Out2(I, k) = Out2(I, k) + Fil2(1)
            Out2(I, k) = Out2(I, k) / FL2
            
        Next I
   Next k
End Sub

Sub ServoFilter(itp As Double, Ted As Double, ControlOut() As Double, TotalPts As Integer, _
                ServoOut() As Double, count As Integer)

    
    
    Dim SFIL1() As Double
    Dim SFIL2() As Double
    Dim Weight() As Double
    Dim SFL1 As Integer, SFL2 As Integer
    SFL1 = CInt(itp * 1000)
    SFL2 = CInt(Ted * 1000)
    ReDim SFIL1(1 To SFL1)
    ReDim SFIL2(1 To SFL2)
    Dim Sout As Double
    Dim TotalWeight As Double
    ReDim Weight(1 To SFL2)
    Dim Out1In2 As Double
    
    Dim ExpEnable As Boolean
    
    Dim k As Integer, L As Integer, M As Integer, J As Integer, I As Integer
    
    Dim ServoOutTranspose()
    'ServoOut is dimensioned backwards, because we need to redimension
    'the number of points.  That can only be done if the number of points
    'is the last dimension in an array.
    ReDim ServoOut(1 To 3, 0 To TotalPts)
    
    'Determine which second stage servo filter is being used
    If frmInput.optExp.Value = True Then
        ExpEnable = True
    Else
        ExpEnable = False
    End If
    
    'Initialize stage 1
    For L = 1 To SFL1
        SFIL1(L) = 0#
    Next L
    
    'Initialize stage 2
    For L = 1 To SFL2
        SFIL2(L) = 0#
    Next L
    
    For k = 1 To 3
        'Set servo output to 0 at time 0
        ServoOut(k, 0) = 0#
        Sout = 1#
        I = 0

        'If the second stage is exponential, then we need a weight factor
        If ExpEnable Then
        
            TotalWeight = 0#
        
            For J = 1 To SFL2
                Weight(J) = Exp(-J / SFL2)
                TotalWeight = TotalWeight + Weight(J)
            Next J
        
        Else
        
            TotalWeight = SFL2
        
            For J = 1 To SFL2
                Weight(J) = 1#
            Next J
    
        End If
    
        ReDim Preserve ServoOut(1 To 3, 0 To TotalPts * SFL1 + SFL2)
        'Loop until all servo filters are clear
        Do Until I = TotalPts * SFL1 + SFL2
            I = I + 1
            Out1In2 = 0#
        
            'Move filter 1 values to the next step
            For L = SFL1 To 2 Step -1
                SFIL1(L) = SFIL1(L - 1)
                Out1In2 = Out1In2 + SFIL1(L)
            Next L
        
            'When the delay causes a longer time, add 0s to the filter
            If I / SFL1 > TotalPts Then
                SFIL1(1) = 0
            Else
                'Determine which ITP input is used for each millisecond
                'Must go backwards to catch the right input
                For M = TotalPts To 1 Step -1
                    If I / SFL1 < M Then
                        SFIL1(1) = ControlOut(M - 1, k) / (itp * 1000)
                    End If
                Next M
            End If
        
            'Redefine filter 1 output
            Out1In2 = Out1In2 + SFIL1(1)
            Out1In2 = Out1In2 / SFL1
            ServoOut(k, I) = 0#
    
            'Move filter 2 values to next step
        
            For L = SFL2 To 1 Step -1
                If L <> 1 Then
                    SFIL2(L) = SFIL2(L - 1)
                Else
                    SFIL2(L) = Out1In2
                End If
            
                ServoOut(k, I) = ServoOut(k, I) + Weight(L) * SFIL2(L)
            Next L
        
            ServoOut(k, I) = ServoOut(k, I) / TotalWeight
            Sout = Abs(ServoOut(k, I))
            If k = 1 Then
                count = I
            End If
        Loop
    Next k
    
'Transpose the ServoOut array to be the same as the other arrays.
    ReDim ServoOutTranspose(0 To count, 1 To 3)
    
'---Transpose ServoOut and save the contents to a temporary array
    For I = 0 To count
        For J = 1 To 3
            ServoOutTranspose(I, J) = ServoOut(J, I)
        Next J
    Next I
    
'---Redimension ServoOut (You lose all data, that is the reason for the temp)
    ReDim ServoOut(0 To count, 1 To 3)
    
'---Resave ServoOut with its original data transposed.
    For I = 0 To count
        For J = 1 To 3
            ServoOut(I, J) = ServoOutTranspose(I, J)
        Next J
    Next I
    
End Sub

