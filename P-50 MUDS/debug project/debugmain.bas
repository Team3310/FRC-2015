Attribute VB_Name = "Module1"
Sub Main()

Dim M1(1 To 3, 1 To 3) As Double, M2(1 To 3, 1 To 3) As Double
Dim Mfinal(1 To 3, 1 To 3) As Double, Vfinal() As Double
Dim V1(1 To 3) As Double, V2(1 To 3) As Double

Dim I As Integer, J As Integer

For I = 1 To 3
    V1(I) = 2 * I
    V2(I) = I - 1
Next I

Vfinal() = VecAdd(CrossProd(V1, V2), CrossProd(V1, V2))



End Sub
'FUNCTIONS WORK!!!!!!!!! 12/8/00 - PDC

