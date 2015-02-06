Attribute VB_Name = "charting"
Option Explicit
'Code to create a path profile chart
Sub Chart_1(TimeValues As Object, SpeedValues() As Object, _
            AccValues() As Object, xlSheet As Object, _
            XL As Object, q As Integer, islinear As Boolean, J As Integer)

Dim Profile As Chart
Dim Sunit As String, Aunit As String

If islinear = True Then
    Sunit = "mm/sec"
    Aunit = "mm/sec^2"
Else
    Sunit = "°/sec"
    Aunit = "rad/sec^2"
End If

'Add a chart to the current workbook
    XL.Workbooks(q).Charts.Add before:=XL.Workbooks(q).Sheets(J)
    
'Set an Object variable for the created charts
Set Profile = XL.Workbooks(q).Charts(J)

XL.Workbooks(q).Sheets(J).Name = "Vel & Acc Chart " & J
'With command to set chart options
With Profile
    .ChartType = xlXYScatterLinesNoMarkers
    .Location Where:=xlLocationAsNewSheet
    .SeriesCollection.NewSeries
    .SeriesCollection(1).XValues = TimeValues
    .SeriesCollection(1).Values = SpeedValues(J)
    .SeriesCollection(1).Name = xlSheet.Cells(8, 3 * J)
    .SeriesCollection.NewSeries
    .SeriesCollection(2).XValues = TimeValues
    .SeriesCollection(2).Values = AccValues(J)
    .SeriesCollection(2).AxisGroup = 2
    .SeriesCollection(2).Name = xlSheet.Cells(8, 3 * J + 1)
    .Axes(xlValue).HasTitle = True
    .Axes(xlValue).AxisTitle.Characters.Text = "Speed " & Sunit
    .Axes(xlValue).MajorTickMark = xlInside
    .Axes(xlValue).MinorTickMark = xlInside
    '.Axes(xlValue).HasMajorGridlines = False
    .Axes(xlCategory).HasTitle = True
    .Axes(xlCategory).AxisTitle.Characters.Text = "Time (sec)"
    .Axes(xlValue, xlSecondary).MajorTickMark = xlInside
    .Axes(xlValue, xlSecondary).MinorTickMark = xlInside
    '.Axes(xlValue, xlSecondary).HasMajorGridlines = False
    .Axes(xlValue, xlSecondary).HasTitle = True
    .Axes(xlValue, xlSecondary).AxisTitle.Characters.Text = "Acceleration " & Aunit
    .Axes(xlValue, xlSecondary).TickLabels.NumberFormat = "0.0"
    .Axes(xlValue).TickLabels.NumberFormat = "0.0"
    .Axes(xlCategory).TickLabels.NumberFormat = "0.0"
'        .SeriesCollection.NewSeries
'        .SeriesCollection(3).XValues = TimeValues
'        .SeriesCollection(3).Values = PosValues
'        .SeriesCollection(3).Border.ColorIndex = 1
'        .SeriesCollection(3).Name = xlSheet.Cells(4, 4)
    .PlotArea.Left = 60
    .PlotArea.Height = 350
    .PlotArea.Width = 480
    .HasTitle = True
    .PlotArea.Interior.ColorIndex = xlNone
    .PlotArea.Border.LineStyle = xlNone
    
    .ChartArea.AutoScaleFont = True
    .ChartArea.Font.Name = "Century Schoolbook"
    .ChartArea.Font.Size = 10
    .HasLegend = True
    .Legend.Position = xlLegendPositionRight
    .Legend.Border.LineStyle = xlNone
    
    
End With
If islinear = False Then
    Profile.ChartTitle.Text = "J" & J & " Speed & Accel. vs. Time"
End If

If islinear = True Then
    If J = 1 Then
        Profile.ChartTitle.Text = "X Speed & Accel. vs. Time"
    ElseIf J = 2 Then
        Profile.ChartTitle.Text = "Y Speed & Accel. vs. Time"
    ElseIf J = 3 Then
        Profile.ChartTitle.Text = "Z Speed & Accel. vs. Time"
    End If
End If

End Sub

Sub Chart_2(TimeValues As Object, PosValues() As Object, _
            xlSheet As Object, _
            XL As Object, q As Integer, islinear As Boolean)

Dim Profile2 As Chart

Dim Punit As String, J As Integer

If islinear = True Then
    Punit = "mm"
    
Else
    Punit = "°"
    
End If
'Add a chart to the current workbook
    XL.Workbooks(q).Charts.Add before:=XL.Workbooks(q).Sheets(4)
    
'Set an Object variable for the created charts
Set Profile2 = XL.Workbooks(q).Charts(4)
XL.Workbooks(q).Sheets(4).Name = "Pos Chart"

For J = 1 To 3
'With command to set chart options
    With Profile2
        .ChartType = xlXYScatterLinesNoMarkers
        .Location Where:=xlLocationAsNewSheet
        .SeriesCollection.NewSeries
        .SeriesCollection(J).XValues = TimeValues
        .SeriesCollection(J).Values = PosValues(J)
        .SeriesCollection(J).Name = xlSheet.Cells(7, 3 * J - 1)
        
        .Axes(xlValue).HasTitle = True
        .Axes(xlValue).AxisTitle.Characters.Text = "Position " & Punit
        .Axes(xlValue).MajorTickMark = xlInside
        .Axes(xlValue).MinorTickMark = xlInside
        .Axes(xlValue).HasMajorGridlines = False
        .Axes(xlCategory).HasTitle = True
        .Axes(xlCategory).AxisTitle.Characters.Text = "Time (sec)"
        .Axes(xlValue).TickLabels.NumberFormat = "0.0"
        .Axes(xlCategory).TickLabels.NumberFormat = "0.0"
    '        .SeriesCollection.NewSeries
    '        .SeriesCollection(3).XValues = TimeValues
    '        .SeriesCollection(3).Values = PosValues
    '        .SeriesCollection(3).Border.ColorIndex = 1
    '        .SeriesCollection(3).Name = xlSheet.Cells(4, 4)
        .PlotArea.Left = 60
        .PlotArea.Height = 350
        .PlotArea.Width = 480
        .HasTitle = True
        .PlotArea.Interior.ColorIndex = xlNone
        .PlotArea.Border.LineStyle = xlNone
        
        .ChartArea.AutoScaleFont = True
        .ChartArea.Font.Name = "Century Schoolbook"
        .ChartArea.Font.Size = 10
        .HasLegend = True
        .Legend.Position = xlLegendPositionRight
        .Legend.Border.LineStyle = xlNone
        
        
    End With
Next J
        Profile2.ChartTitle.Text = " Position vs. Time"
   

End Sub

