Attribute VB_Name = "MatrixMath"
'------------------------------------------------------------------------------
'Sub MatrixMult:
'
'This subroutine multiplies two, 2-dimensional matrices together.
'The number of elements can be any size (2x2, 4x4, 4x2, etc.)  This subroutine
'does not multiply a matrix and a vector (3x3 multiplied with a 3x1).
'
'PLEASE NOTE: The rule for multiplying matrices together is:
'                -The number of columns of the 1st matrix must equal
'                 the number of rows of the second matrix.
'------------------------------------------------------------------------------
Function MMult(M1() As Double, M2() As Double) As Double()
   
    Dim I As Long, J As Long, K As Long
    Dim Element1(1 To 2) As Long, Element2(1 To 2) As Long
    Dim M12() As Double
   
    'Determine the number of elements in each dimension of each matrix
    Element1(1) = UBound(M1, 1) - LBound(M1, 1) + 1
    Element1(2) = UBound(M1, 2) - LBound(M1, 2) + 1
    Element2(1) = UBound(M2, 1) - LBound(M2, 1) + 1
    Element2(2) = UBound(M2, 2) - LBound(M2, 2) + 1
   
    If Element1(2) <> Element2(1) Then
        MsgBox "Hey dummy!!! Matrix 1 # of columns must equal Matrix 2 # of rows", _
            , "Dunce"
            
        Exit Function
    End If
    
    'The new matrix has the same # of rows of M1 and the same # of columns as M2.
    ReDim M12(1 To Element1(1), 1 To Element2(2))
    
    'Initialize M12 matrix
    For I = 1 To Element1(1)
        For J = 1 To Element2(2)
            M12(I, J) = 0#
        Next J
    Next I
   
    'Multiply the matrices together
    For I = 1 To Element1(1)
        For J = 1 To Element2(2)
            For K = 1 To Element1(2)
                M12(I, J) = M12(I, J) + M1(I, K) * M2(K, J)
            Next K
        Next J
    Next I
    
    MMult = M12
    
End Function

'-------------------------------------------------------------------------
'Sub MatrixVectorMult:
'
'This subroutine takes a matrix and multiplies it by a vector.
'The dimension of the vector must equal the # of Matrix columns.
'
'-------------------------------------------------------------------------

Function MVecMult(Min() As Double, Vin() As Double) As Double()

    Dim I As Long, K As Long
    Dim Element1(1 To 2) As Long, Element2 As Long
    Dim Vout() As Double
   
    'Determine the number of elements in each dimension of each matrix
    Element1(1) = UBound(Min, 1) - LBound(Min, 1) + 1
    Element1(2) = UBound(Min, 2) - LBound(Min, 2) + 1
    Element2 = UBound(Vin) - LBound(Vin) + 1
    
    If Element1(2) <> Element2 Then
        MsgBox "Hey dummy!!! Matrix 1 # of columns must equal Matrix 2 # of rows", _
            , "Dunce"
            
        Exit Function
    End If
    
    'The new matrix has the same # of rows of M1 and 1 column.
    ReDim Vout(1 To Element1(1))
    
    'Initialize Vout matrix
    For I = 1 To Element1(1)
        Vout(I) = 0#
    Next I
   
    'Multiply the matrices together
    For I = 1 To Element1(1)
        For K = 1 To Element1(2)
            Vout(I) = Vout(I) + Min(I, K) * Vin(K)
        Next K
    Next I
    
    MVecMult = Vout
    
End Function


' Returns the cross product of two 3-dimensional vectors.
Function CrossProd(V1() As Double, V2() As Double) As Double()

    Dim V3(1 To 3) As Double
    
    V3(1) = (V1(2) * V2(3) - V2(2) * V1(3))
    V3(2) = (V1(3) * V2(1) - V2(3) * V1(1))
    V3(3) = (V1(1) * V2(2) - V2(1) * V1(2))
   
    CrossProd = V3
    
End Function

Function VecAdd(V1() As Double, V2() As Double) As Double()
    
    Dim V3(1 To 3) As Double
    Dim I As Integer
    
    For I = 1 To 3
        V3(I) = V1(I) + V2(I)
    Next I
    
    VecAdd = V3

End Function

Function VecSub(V1() As Double, V2() As Double) As Double()
    
    Dim V3(1 To 3) As Double
    Dim I As Integer
    
    For I = 1 To 3
        V3(I) = V1(I) - V2(I)
    Next I
    
    VecSub = V3

End Function

Function MVecByScalar(ScalarIn As Double, Vin() As Double) As Double()

    Dim Vtemp(1 To 3) As Double
    Dim I As Integer
    
    For I = 1 To 3
        Vtemp(I) = ScalarIn * Vin(I)
    Next I
    
    MVecByScalar = Vtemp
    
End Function
