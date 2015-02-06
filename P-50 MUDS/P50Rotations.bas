Attribute VB_Name = "Rotations"
Sub RotateForward(A() As Double, D() As Double, Alpha() As Double, _
                   Theta() As Double, Rfor() As Double, J As Integer)

    
    
    ReDim Preserve Rfor(1 To 3, 1 To 3, 1 To 3)
    
    If J < 3 Then
    
        Rfor(J, 1, 1) = Cos(Theta(J, I))
        'Enter other rotations
    End If
    
End Sub
