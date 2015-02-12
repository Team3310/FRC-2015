Attribute VB_Name = "P50_Output"
Option Explicit

Dim XL As excel.Application
Dim xlSheet As excel.Worksheet
Dim xlSheet2 As excel.Worksheet
Dim xlSheet3 As excel.Worksheet
Dim xlSheet4 As excel.Worksheet
Dim xlSheetForces As excel.Worksheet
Dim Profile As excel.Chart

Dim q As Integer

Sub P50_OUTPUT(JntPos() As Double, JntAcc() As Double, JntVel() As Double, _
               CartPos() As Double, CartVel() As Double, CartAcc() As Double, _
               RealPts As Integer, OutPts As Integer, AveJS() As Double, _
               MinJS() As Double, MaxJS() As Double, MaxPS, MinPS, AvePS, _
               NumPaths As Integer, CalcDynamics As Boolean, _
               Torque() As Double, Tmin() As Double, Tmax() As Double, _
               Trms() As Double, Tweighted() As Double, Force() As Double, _
               WantTorque As Boolean, WantForce As Boolean)
                
Dim I As Integer, J As Integer, L As Integer, K As Integer
Dim irow As Integer
Dim icol As Integer
Dim RowCount As Integer
Dim OutputRange As Object, SummaryOutput As Object
Dim SideHeader As Object, HistoryHeader As Object
Dim Weighted As Object

Dim TorqueHistoryHeader As Object, TorqueSideHeader As Object
Dim TorqueOutputRange As Object, TorqueSummaryHeader As Object
Dim TorqueSummaryOutput As Object, TorqueTimeValues As Object

Dim JntPosValues(1 To 3) As Object
Dim JntSpeedValues(1 To 3) As Object
Dim JntAccValues(1 To 3) As Object
Dim CartPosValues(1 To 3) As Object
Dim CartSpeedValue As Object
Dim CartAccValue As Object

Dim TimeValues As Object

Dim Punit As String, Sunit As String, Aunit As String



frmInput.lblInfo.Caption = "Starting Excel"

On Error Resume Next
Set XL = GetObject(, "Excel.application")

If Err <> 0 Then
    Set XL = New excel.Application
End If

'Turn off the Excel alerts
XL.DisplayAlerts = False

frmInput.lblInfo.Caption = "Adding a Workbook"
XL.Workbooks.Add

q = XL.Workbooks.count


Set xlSheet = XL.Workbooks(q).Sheets(1)
'Initialize variables
irow = 10
icol = 1


frmInput.lblInfo.Caption = "Creating Headers"


xlSheet.Cells(8, 1) = "Time"
xlSheet.Cells(8, 2) = "J1"
xlSheet.Cells(8, 3) = "J2"
xlSheet.Cells(8, 4) = "J3"
xlSheet.Cells(8, 5) = "J1 Speed"
xlSheet.Cells(8, 6) = "J2 Speed"
xlSheet.Cells(8, 7) = "J3 Speed"
xlSheet.Cells(8, 8) = "X"
xlSheet.Cells(8, 9) = "Y"
xlSheet.Cells(8, 10) = "Z"
xlSheet.Cells(8, 11) = "Path Speed"


'No longer needed, but keeping for reference
'    With xlSheet
'        .Range(.Cells(7, 2), .Cells(7, 4)).Merge
'        .Range(.Cells(7, 2), .Cells(7, 4)).Font.Bold = True
'        .Range(.Cells(7, 2), .Cells(7, 4)).HorizontalAlignment = xlCenter
'        .Range(.Cells(7, 5), .Cells(7, 7)).Merge
'        .Range(.Cells(7, 5), .Cells(7, 7)).Font.Bold = True
'        .Range(.Cells(7, 5), .Cells(7, 7)).HorizontalAlignment = xlCenter
'        .Range(.Cells(7, 8), .Cells(7, 10)).Merge
'        .Range(.Cells(7, 8), .Cells(7, 10)).Font.Bold = True
'        .Range(.Cells(7, 8), .Cells(7, 10)).HorizontalAlignment = xlCenter
'        .Range(.Cells(7, 11), .Cells(7, 12)).Merge
'        .Range(.Cells(7, 11), .Cells(7, 12)).HorizontalAlignment = xlCenter
'        .Range(.Cells(7, 11), .Cells(7, 12)).Font.Bold = True
'    End With
    
xlSheet.Cells(9, 1) = "seconds"
xlSheet.Cells(9, 2) = "deg"
xlSheet.Cells(9, 3) = "deg"
xlSheet.Cells(9, 4) = "deg"
xlSheet.Cells(9, 5) = "deg/s"
xlSheet.Cells(9, 6) = "deg/s"
xlSheet.Cells(9, 7) = "deg/s"
xlSheet.Cells(9, 8) = "mm"
xlSheet.Cells(9, 9) = "mm"
xlSheet.Cells(9, 10) = "mm"
xlSheet.Cells(9, 11) = "mm/s"


    
'Sets header for Max/Ave Area
xlSheet.Cells(3, 1) = "J1"
xlSheet.Cells(4, 1) = "J2"
xlSheet.Cells(5, 1) = "J3"
xlSheet.Cells(6, 1) = "Path"


xlSheet.Cells(1, 2) = "Min Speed"
xlSheet.Cells(2, 2) = "deg/sec"
xlSheet.Cells(1, 3) = "Max Speed"
xlSheet.Cells(2, 3) = "deg/sec"
xlSheet.Cells(1, 4) = "Ave Speed"
xlSheet.Cells(2, 4) = "deg/sec"
    


'Sets the Top Area
Set Weighted = xlSheet.Range(xlSheet.Cells(1, 2), xlSheet.Cells(2, 7))
'Sets the Left Side of the top area
Set SideHeader = xlSheet.Range(xlSheet.Cells(3, 1), xlSheet.Cells(6, 1))
'Sets the header area
Set HistoryHeader = xlSheet.Range(xlSheet.Cells(8, 1), xlSheet.Cells(9, 17))

'Sets the summary data output area
Set SummaryOutput = xlSheet.Range(xlSheet.Cells(3, 2), xlSheet.Cells(6, 7))

'Sets the time column
Set TimeValues = xlSheet.Range(xlSheet.Cells(10, 1), _
                    xlSheet.Cells(10 + RealPts / OutPts, 1))

'Sets the output area
Set OutputRange = xlSheet.Range(xlSheet.Cells(10, 2), _
                    xlSheet.Cells(10 + RealPts / OutPts, 17))

'Set position locations for x,y,z and J1,J2,J3
For J = 1 To 3
    Set JntPosValues(J) = xlSheet.Range(xlSheet.Cells(10, J + 1), _
                          xlSheet.Cells(10 + RealPts / OutPts, J + 1))
    Set JntSpeedValues(J) = xlSheet.Range(xlSheet.Cells(10, J + 4), _
                          xlSheet.Cells(10 + RealPts / OutPts, J + 4))
    Set CartPosValues(J) = xlSheet.Range(xlSheet.Cells(10, J + 7), _
                           xlSheet.Cells(10 + RealPts / OutPts, J + 7))
Next J

Set CartSpeedValue = xlSheet.Range(xlSheet.Cells(10, 11), _
                       xlSheet.Cells(10 + RealPts / OutPts, 11))

    
frmInput.lblInfo.Caption = "Printing Output to Excel"

'Output Max,Min, and Ave Joint Speeds
For J = 1 To 3
    xlSheet.Cells(2 + J, 2) = MinJS(J)
    xlSheet.Cells(2 + J, 3) = MaxJS(J)
    xlSheet.Cells(2 + J, 4) = AveJS(J)
Next J

'Output Max, Min, & Ave Path Speeds
xlSheet.Cells(6, 2) = MinPS
xlSheet.Cells(6, 3) = MaxPS
xlSheet.Cells(6, 4) = AvePS
        
'Output time history data

For I = 0 To RealPts Step OutPts

    xlSheet.Cells(irow, icol) = I / 1000#
    
    For J = 1 To 3
        xlSheet.Cells(irow, icol + J) = JntPos(I, J)
        xlSheet.Cells(irow, icol + J + 3) = JntVel(I, J)
        xlSheet.Cells(irow, icol + J + 6) = CartPos(I, J)
    Next J
    
    'Output path velocity
    xlSheet.Cells(irow, 11) = CartVel(I, 4)
    
    irow = irow + 1
    
Next I


'Output Joint Torques and RMS Torques
If CalcDynamics = True Then

    Set xlSheet2 = XL.Workbooks(q).Sheets(2)
    irow = 10
    icol = 1
    
    'We will always output Max and RMS Torques
    'Sets header for Max/RMS Area
    xlSheet2.Cells(3, 1) = "J1"
    xlSheet2.Cells(4, 1) = "J2"
    xlSheet2.Cells(5, 1) = "J3"
      
    xlSheet2.Cells(1, 2) = "Min Torque"
    xlSheet2.Cells(2, 2) = "N-m"
    xlSheet2.Cells(1, 3) = "Max Torque"
    xlSheet2.Cells(2, 3) = "N-m"
    xlSheet2.Cells(1, 4) = "RMS Torque"
    xlSheet2.Cells(2, 4) = "N-m"
    xlSheet2.Cells(1, 5) = "10/3 Torque"
    xlSheet2.Cells(2, 5) = "N-m"
    
    'Sets the Top Area
    Set TorqueSummaryHeader = xlSheet2.Range(xlSheet2.Cells(1, 2), _
                              xlSheet2.Cells(2, 5))
    'Sets the Left Side of the top area
    Set TorqueSideHeader = xlSheet2.Range(xlSheet2.Cells(3, 1), _
                           xlSheet2.Cells(6, 1))
    'Sets the header area
    Set TorqueHistoryHeader = xlSheet2.Range(xlSheet2.Cells(8, 1), _
                              xlSheet2.Cells(9, 7))
    
    'Sets the summary data output area
    Set TorqueSummaryOutput = xlSheet2.Range(xlSheet2.Cells(3, 2), _
                              xlSheet2.Cells(6, 5))
    
    'Sets the time column
    Set TimeValues = xlSheet.Range(xlSheet.Cells(10, 1), _
                        xlSheet.Cells(10 + RealPts / OutPts, 1))
                        
    'Sets the output area
    Set TorqueOutputRange = xlSheet2.Range(xlSheet2.Cells(10, 2), _
                            xlSheet2.Cells(10 + RealPts / OutPts, 7))
    
    For J = 1 To 3
        xlSheet2.Cells(J + 2, 2) = Tmin(J)
        xlSheet2.Cells(J + 2, 3) = Tmax(J)
        xlSheet2.Cells(J + 2, 4) = Trms(J)
        xlSheet2.Cells(J + 2, 5) = Tweighted(J)
    Next J
    
    
    'We will only output the following if requested by user
    
    If WantTorque = True Then
    
        'Set Header for Output Area
        xlSheet2.Cells(8, 1) = "Time"
        xlSheet2.Cells(8, 2) = "J1 Torque"
        xlSheet2.Cells(8, 3) = "J2 Torque"
        xlSheet2.Cells(8, 4) = "J3 Torque"
        xlSheet2.Cells(8, 5) = "J4 Torque"
        xlSheet2.Cells(8, 6) = "J5 Torque"
        xlSheet2.Cells(8, 7) = "J6 Torque"
        
        xlSheet2.Cells(9, 2) = "N-m"
        xlSheet2.Cells(9, 3) = "N-m"
        xlSheet2.Cells(9, 4) = "N-m"
        xlSheet2.Cells(9, 5) = "N-m"
        xlSheet2.Cells(9, 6) = "N-m"
        xlSheet2.Cells(9, 7) = "N-m"
        
        
        For I = 0 To RealPts Step OutPts
            For J = 1 To 6
                xlSheet2.Cells(irow, icol + J) = Torque(I, J, 3)
            Next J
            
            xlSheet2.Cells(irow, icol) = I / 1000#
            irow = irow + 1
        Next I
        
        With TorqueOutputRange
            .HorizontalAlignment = xlCenter
            .VerticalAlignment = xlCenter
            .WrapText = False
            .Orientation = xlHorizontal
            .NumberFormat = "0.000"
        End With
    
        With TorqueHistoryHeader
            .Font.Bold = True
            .HorizontalAlignment = xlCenter
            .VerticalAlignment = xlCenter
            .WrapText = False
            .Orientation = xlHorizontal
            .NumberFormat = "0.000"
            .Columns.AutoFit
        End With
        
    End If
    
    With TorqueSummaryHeader
        .Font.Bold = True
        .HorizontalAlignment = xlCenter
        .VerticalAlignment = xlCenter
        .WrapText = False
        .Orientation = xlHorizontal
        .Columns.AutoFit
    End With
    
    With TorqueSummaryOutput
        .HorizontalAlignment = xlCenter
        .VerticalAlignment = xlCenter
        .WrapText = False
        .Orientation = xlHorizontal
        .NumberFormat = "0.00"
    End With
    
    With TorqueSideHeader
        .Font.Bold = True
        .HorizontalAlignment = xlRight
        .VerticalAlignment = xlCenter
        .WrapText = False
        .Orientation = xlHorizontal
    End With
    
    xlSheet2.Columns.AutoFit
    xlSheet2.Name = "Dynamics Output"

'---If they want the force and moment data, then dump it to sheet 3
    If WantForce = True Then
        Set xlSheetForces = XL.Workbooks(q).Worksheets.Add(, xlSheet2)
        
        xlSheetForces.Name = "Force & Moment Output"
        
        xlSheetForces.Cells(2, 1) = "Time"
        xlSheetForces.Cells(1, 2) = "Axis 1"
        xlSheetForces.Cells(1, 8) = "Axis 2"
        xlSheetForces.Cells(1, 14) = "Axis 3"
        xlSheetForces.Cells(1, 20) = "Axis 4"
        xlSheetForces.Cells(1, 26) = "Axis 5"
        xlSheetForces.Cells(1, 32) = "Axis 6"
        
        With xlSheetForces
            .Range(xlSheetForces.Cells(1, 2), _
                                xlSheetForces.Cells(1, 7)).Merge
            .Range(xlSheetForces.Cells(1, 2), _
                                xlSheetForces.Cells(1, 7)).HorizontalAlignment = xlCenter
            .Range(xlSheetForces.Cells(1, 2), _
                                xlSheetForces.Cells(1, 7)).Font.Bold = True
                            
            .Range(xlSheetForces.Cells(1, 8), _
                                xlSheetForces.Cells(1, 13)).Merge
            .Range(xlSheetForces.Cells(1, 8), _
                                xlSheetForces.Cells(1, 13)).HorizontalAlignment = xlCenter
            .Range(xlSheetForces.Cells(1, 8), _
                                xlSheetForces.Cells(1, 13)).Font.Bold = True
            
            .Range(xlSheetForces.Cells(1, 14), _
                                xlSheetForces.Cells(1, 19)).Merge
            .Range(xlSheetForces.Cells(1, 14), _
                                xlSheetForces.Cells(1, 19)).HorizontalAlignment = xlCenter
            .Range(xlSheetForces.Cells(1, 14), _
                                xlSheetForces.Cells(1, 19)).Font.Bold = True
                                
            .Range(xlSheetForces.Cells(1, 20), _
                                xlSheetForces.Cells(1, 25)).Merge
            .Range(xlSheetForces.Cells(1, 20), _
                                xlSheetForces.Cells(1, 25)).HorizontalAlignment = xlCenter
            .Range(xlSheetForces.Cells(1, 20), _
                                xlSheetForces.Cells(1, 25)).Font.Bold = True
                                
            
            .Range(xlSheetForces.Cells(1, 26), _
                                xlSheetForces.Cells(1, 31)).Merge
            .Range(xlSheetForces.Cells(1, 26), _
                                xlSheetForces.Cells(1, 31)).HorizontalAlignment = xlCenter
            .Range(xlSheetForces.Cells(1, 26), _
                                xlSheetForces.Cells(1, 31)).Font.Bold = True
                                
                                
            .Range(xlSheetForces.Cells(1, 32), _
                                xlSheetForces.Cells(1, 37)).Merge
            .Range(xlSheetForces.Cells(1, 32), _
                                xlSheetForces.Cells(1, 37)).HorizontalAlignment = xlCenter
            .Range(xlSheetForces.Cells(1, 32), _
                                xlSheetForces.Cells(1, 37)).Font.Bold = True
        End With
        
        For I = 1 To 6
            xlSheetForces.Cells(2, 6 * I - 4) = "Fx"
            xlSheetForces.Cells(2, 6 * I - 3) = "Fy"
            xlSheetForces.Cells(2, 6 * I - 2) = "Fz"
            xlSheetForces.Cells(2, 6 * I - 1) = "Mx"
            xlSheetForces.Cells(2, 6 * I) = "My"
            xlSheetForces.Cells(2, 6 * I + 1) = "Mz"
        Next I
        
        xlSheetForces.Cells(3, 1) = "sec"
        
        For I = 1 To 36
            xlSheetForces.Cells(3, I + 1) = "N"
        Next I
        
        J = 0
        
        For I = 0 To RealPts Step OutPts
            xlSheetForces.Cells(4 + J, 1) = I / 1000#
            For K = 1 To 6
                    xlSheetForces.Cells(4 + J, 6 * K - 4) = Force(I, K, 1)
                    xlSheetForces.Cells(4 + J, 6 * K - 3) = Force(I, K, 2)
                    xlSheetForces.Cells(4 + J, 6 * K - 2) = Force(I, K, 3)
                    xlSheetForces.Cells(4 + J, 6 * K - 1) = Torque(I, K, 1)
                    xlSheetForces.Cells(4 + J, 6 * K) = Torque(I, K, 2)
                    xlSheetForces.Cells(4 + J, 6 * K + 1) = Torque(I, K, 3)
            Next K

            J = J + 1
        Next I
    End If
End If

'Format the output cells.

frmInput.lblInfo.Caption = "Formatting Output"

With OutputRange
    .HorizontalAlignment = xlCenter
    .VerticalAlignment = xlCenter
    .WrapText = False
    .Orientation = xlHorizontal
    .NumberFormat = "0.00"
End With

With HistoryHeader
    .Font.Bold = True
    .HorizontalAlignment = xlCenter
    .VerticalAlignment = xlCenter
    .WrapText = False
    .Orientation = xlHorizontal
    .NumberFormat = "0.00"
    .Columns.AutoFit
End With

With Weighted
    .Font.Bold = True
    .HorizontalAlignment = xlCenter
    .VerticalAlignment = xlCenter
    .WrapText = False
    .Orientation = xlHorizontal
    .Columns.AutoFit
End With

With SummaryOutput
    .HorizontalAlignment = xlCenter
    .VerticalAlignment = xlCenter
    .WrapText = False
    .Orientation = xlHorizontal
    .NumberFormat = "0.00"
End With

With SideHeader
    .Font.Bold = True
    .HorizontalAlignment = xlRight
    .VerticalAlignment = xlCenter
    .WrapText = False
    .Orientation = xlHorizontal
End With

xlSheet.Columns.AutoFit


'frmInput.lblInfo.Caption = "Creating Profile Charts."
'
'For J = 1 To 3
'
'    Call charting.Chart_1(TimeValues, SpeedValues, AccValues, _
'    xlSheet, XL, q, islinear, J)
'
'Next J
'
''For J = 1 To 3
'    Call charting.Chart_2(TimeValues, PosValues, xlSheet, XL, q, islinear)
''Next J

'XL.Sheets(XL.Sheets.count).Delete


XL.DisplayAlerts = True

Call Sheet3_Output(NumPaths)

If CalcDynamics = True Then
    Call Dynamics_Input_Output
End If

XL.Visible = True
Set XL = Nothing

frmInput.Show

End Sub


Sub Sheet3_Output(NumPts As Integer)
'
'This sub prints the data from the input form
'to sheet 2 of the Excel Workbook
'By:  Don Bartlett
'Latest Revision: 11-3-99
'       Revised for P-50 MUDS on 12/8/00 by PDC
'
    Dim TitleRange As Object
    Dim ValueRange As Object
    Dim UnitsRange As Object
    Dim PointRange As Object
    Dim LengthRange As Object
    Dim I As Integer, J As Integer
    
    
    On Error GoTo 0

    
    
    'Set xlSheet3 = XL.Workbooks(q).Sheets(XL.Sheets.count)
    Set xlSheet3 = XL.Workbooks(q).Sheets.Add(, xlSheet)
    
    With xlSheet3
        .Cells(1, 1) = "INPUT DATA"
        .Cells(2, 2) = frmInput.lblX_J1.Caption
        .Cells(2, 3) = frmInput.lblY_J2.Caption
        .Cells(2, 4) = frmInput.lblZ_J3.Caption
        .Cells(2, 5) = frmInput.lblSpeed.Caption
        .Cells(2, 6) = frmInput.Label4.Caption
        .Cells(3, 2) = frmInput.lblUnitsDis(0)
        .Cells(3, 3) = frmInput.lblUnitsDis(1)
        .Cells(3, 4) = frmInput.lblUnitsDis(2)
        .Cells(3, 5) = frmInput.lblUnitsVel
        '.Cells(3, 6) = frmInput.Label5
        
        .Range("A1").Font.Bold = True
        .Range("A1").HorizontalAlignment = xlLeft
        .Name = "Motion Input"
        .PageSetup.PrintGridlines = True
        .PageSetup.LeftFooter = "File:  &F"
        .PageSetup.RightFooter = "Printed:  &D &T"
    End With
    
    For I = 0 To NumPts
        With xlSheet3
            .Cells(4 + I, 1) = "Point " & I
            .Cells(4 + I, 2) = frmInput.txtPx(I).Text
            .Cells(4 + I, 3) = frmInput.txtPy(I).Text
            .Cells(4 + I, 4) = frmInput.txtPz(I).Text
        End With
        
        If I > 0 Then
            xlSheet3.Cells(4 + I, 5) = frmInput.txtSpeed(I).Text
            If I < NumPts Then
                xlSheet3.Cells(4 + I, 6) = frmInput.txtEnd(I).Text
            End If
        End If
            
    Next I
    
    If frmInput.optLinear = False Then
        With xlSheet3
            .Cells(6 + NumPts, 1) = frmInput.lblJNTAcc1.Caption
            .Cells(6 + NumPts, 5) = "milliseconds"
            .Cells(7 + NumPts, 1) = frmInput.lblJNTAcc2.Caption
            .Cells(7 + NumPts, 5) = "milliseconds"
            .Cells(8 + NumPts, 1) = frmInput.lblJNTSpeed.Caption
            .Cells(8 + NumPts, 5) = frmInput.lblJNTSpeedUnits
        End With
        
        For J = 1 To 3
            With xlSheet3
                .Cells(6 + NumPts, J + 1) = frmInput.txtJNTAcc1(J).Text
                .Cells(7 + NumPts, J + 1) = frmInput.txtJNTAcc2(J).Text
                .Cells(8 + NumPts, J + 1) = frmInput.txtJNTSpeed(J).Text
            End With
        Next J
        xlSheet3.Range(xlSheet3.Cells(6 + NumPts, 2), _
        xlSheet3.Cells(8 + NumPts, 5)).HorizontalAlignment = xlCenter
        
        xlSheet3.Range(xlSheet3.Cells(6 + NumPts, 2), _
        xlSheet3.Cells(8 + NumPts, 5)).NumberFormat = "0"
        
    Else
        With xlSheet3
            .Cells(6 + NumPts, 1) = frmInput.lblCartAcc1.Caption
            .Cells(6 + NumPts, 2) = frmInput.txtCartAcc1.Text
            .Cells(6 + NumPts, 3) = "milliseconds"
            .Cells(7 + NumPts, 1) = frmInput.lblCartAcc2.Caption
            .Cells(7 + NumPts, 2) = frmInput.txtCartAcc2.Text
            .Cells(7 + NumPts, 3) = "milliseconds"
        End With
        
        xlSheet3.Range(xlSheet3.Cells(6 + NumPts, 2), _
        xlSheet3.Cells(7 + NumPts, 2)).HorizontalAlignment = xlCenter
        
        xlSheet3.Range(xlSheet3.Cells(6 + NumPts, 2), _
        xlSheet3.Cells(7 + NumPts, 2)).NumberFormat = "0"
    End If
    
    With xlSheet3
        .Cells(10 + NumPts, 1) = "Update Rate:"
        .Cells(10 + NumPts, 2) = frmInput.txtUpdate
        .Cells(10 + NumPts, 3) = "milliseconds"
        .Cells(11 + NumPts, 1) = "Output Rate:"
        .Cells(11 + NumPts, 2) = frmInput.txtOutput
        .Cells(11 + NumPts, 3) = "milliseconds"
    End With
    
    xlSheet3.Range(xlSheet3.Cells(10 + NumPts, 2), _
        xlSheet3.Cells(12 + NumPts, 2)).HorizontalAlignment = xlCenter
        
        xlSheet3.Range(xlSheet3.Cells(10 + NumPts, 2), _
        xlSheet3.Cells(12 + NumPts, 2)).NumberFormat = "0"
    
    
    If frmInput.optExp = True Then
        With xlSheet3
            .Cells(12 + NumPts, 1) = "Exp. Decay:"
            .Cells(12 + NumPts, 2) = CDbl(frmInput.txtExp.Text)
            .Cells(12 + NumPts, 3) = "milliseconds"
        End With
        Set PointRange = xlSheet3.Range(xlSheet3.Cells(4, 1), _
                         xlSheet3.Cells(NumPts + 12, 1))
    Else
        xlSheet3.Cells(12 + NumPts, 1) = "Double Linear Servo Filter"
        Set PointRange = xlSheet3.Range(xlSheet3.Cells(4, 1), _
                         xlSheet3.Cells(NumPts + 11, 1))
    End If
    
    xlSheet3.Cells(14 + NumPts, 1) = "Inner Arm Length"
    xlSheet3.Cells(14 + NumPts, 2) = frmDynamics.txtLength(2).Text
    xlSheet3.Cells(14 + NumPts, 3) = "mm"
    
    xlSheet3.Cells(15 + NumPts, 1) = "Outer Arm Length"
    xlSheet3.Cells(15 + NumPts, 2) = frmDynamics.txtLength(3).Text
    xlSheet3.Cells(15 + NumPts, 3) = "mm"

    Set LengthRange = xlSheet3.Range(xlSheet3.Cells(14 + NumPts, 1), _
                      xlSheet3.Cells(15 + NumPts, 3))
    
    xlSheet3.Range("A1:F1").Merge
    xlSheet3.Range("A1:F1").HorizontalAlignment = xlCenter
    
    With PointRange
        .Font.Bold = True
        .HorizontalAlignment = xlRight
        .Columns.AutoFit
    End With
    
    With LengthRange
        .Font.Bold = False
        .HorizontalAlignment = xlRight
        .NumberFormat = "0.00"
    End With
    
    Set TitleRange = xlSheet3.Range("B2:F3")

    With TitleRange
        .Font.Bold = True
        .HorizontalAlignment = xlCenter
    End With

    Set ValueRange = xlSheet3.Range(xlSheet3.Cells(4, 2), _
                     xlSheet3.Cells(4 + NumPts, 5))

    With ValueRange
        .HorizontalAlignment = xlCenter
        .NumberFormat = "0.00"
        .Columns.AutoFit
    End With

    Set UnitsRange = xlSheet3.Range(xlSheet3.Cells(4, 6), _
                     xlSheet3.Cells(4 + NumPts, 6))

    With UnitsRange
        .HorizontalAlignment = xlCenter
    End With

    With xlSheet
        .Name = "Motion Output"
        .PageSetup.PrintGridlines = True
        .PageSetup.LeftFooter = "File:  &F"
        .PageSetup.RightFooter = "Printed:  &D &T"
    End With
    
xlSheet3.Columns.AutoFit

End Sub


'This subroutine takes the user input for dynamics(Mass, Inertia, etc) and
'Writes it to another sheet

Sub Dynamics_Input_Output()

Dim TopHeader As excel.Range
Dim SideHeader As excel.Range
Dim InputOutput As excel.Range

Dim J As Integer

On Error GoTo 0

Set xlSheet4 = XL.Workbooks(q).Worksheets.Add(, xlSheet3)

xlSheet4.Cells(1, 1) = "Dynamics User Input"

With xlSheet4
    .Range(xlSheet4.Cells(1, 1), xlSheet4.Cells(1, 5)).Merge
    .Range(xlSheet4.Cells(1, 1), xlSheet4.Cells(1, 5)).HorizontalAlignment _
     = xlCenter
    .Range(xlSheet4.Cells(1, 1), xlSheet4.Cells(1, 5)).Font.Bold = True
    .Name = "Dynamics Input"
End With

xlSheet4.Cells(2, 2) = "Turret"
xlSheet4.Cells(2, 3) = "Inner Arm"
xlSheet4.Cells(2, 4) = "Outer Arm"
xlSheet4.Cells(2, 5) = "Payload"

Set TopHeader = xlSheet4.Range(xlSheet4.Cells(2, 2), _
                xlSheet4.Cells(2, 5))

xlSheet4.Cells(3, 1) = "Mass"
xlSheet4.Cells(4, 1) = "CGx"
xlSheet4.Cells(5, 1) = "CGy"
xlSheet4.Cells(6, 1) = "CGz"
xlSheet4.Cells(7, 1) = "Ixx"
xlSheet4.Cells(8, 1) = "Iyy"
xlSheet4.Cells(9, 1) = "Izz"

Set SideHeader = xlSheet4.Range(xlSheet4.Cells(3, 1), _
                 xlSheet4.Cells(9, 1))

For J = 1 To 4
    xlSheet4.Cells(3, J + 1) = frmDynamics.txtMass(J).Text
    xlSheet4.Cells(4, J + 1) = frmDynamics.txtCGx(J).Text
    xlSheet4.Cells(5, J + 1) = frmDynamics.txtCGy(J).Text
    xlSheet4.Cells(6, J + 1) = frmDynamics.txtCGz(J).Text
    xlSheet4.Cells(7, J + 1) = frmDynamics.txtIxx(J).Text
    xlSheet4.Cells(8, J + 1) = frmDynamics.txtIyy(J).Text
    xlSheet4.Cells(9, J + 1) = frmDynamics.txtIzz(J).Text
Next J

Set InputOutput = xlSheet4.Range(xlSheet4.Cells(3, 2), _
                  xlSheet4.Cells(9, 5))

xlSheet4.Cells(11, 1) = "Axis 3 grounded?"

If frmDynamics.chkGrounded.Value = Unchecked Then
    xlSheet4.Cells(11, 2) = "NO"
Else
    xlSheet4.Cells(11, 2) = "YES"
End If

xlSheet4.Cells(12, 1) = "Axis 1 Config:"

If frmDynamics.optBackReach.Value = True Then
    xlSheet4.Cells(12, 2) = "Back Reach"
Else
    xlSheet4.Cells(12, 2) = "Front Reach"
End If

xlSheet4.Cells(13, 1) = "Axis 3 Config:"

If frmDynamics.optElbowUp.Value = True Then
    xlSheet4.Cells(13, 2) = "Elbow Up"
Else
    xlSheet4.Cells(13, 2) = "Elbow Down"
End If

'Format The Output
With TopHeader
    .Font.Bold = True
    .HorizontalAlignment = xlCenter
    .VerticalAlignment = xlCenter
    .WrapText = False
    .Orientation = xlHorizontal
End With

With SideHeader
    .Font.Bold = True
    .HorizontalAlignment = xlRight
    .VerticalAlignment = xlCenter
    .WrapText = False
    .Orientation = xlHorizontal
End With

With InputOutput
    .Font.Bold = False
    .HorizontalAlignment = xlCenter
    .VerticalAlignment = xlCenter
    .WrapText = False
    .Orientation = xlHorizontal
    .NumberFormat = "0.00"
End With

xlSheet4.Columns.AutoFit



End Sub
