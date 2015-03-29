package edu.rhhs.frc.utility.motionprofile;

public class MatrixMath 
{
	/*------------------------------------------------------------------------------
	* MatrixMult:
	* 
	* This subroutine multiplies two, 2-dimensional matrices together.
	* The number of elements can be any size (2x2, 4x4, 4x2, etc.)  This subroutine
	* does not multiply a matrix and a vector (3x3 multiplied with a 3x1).
	*
	* PLEASE NOTE: The rule for multiplying matrices together is:
	*                 -The number of columns of the 1st matrix must equal
	*                  the number of rows of the second matrix.
	*------------------------------------------------------------------------------*/
	public static double[][] matrixMult(double[][] matrix1, double[][] matrix2) {
	   
	    int[] element1 = new int[2];
	    int[] element2 = new int[2];
	   
	    // Determine the number of elements in each dimension of each matrix
	    element1[0] = matrix1.length;
	    element1[1] = matrix1[0].length;
	    element2[0] = matrix2.length;
	    element2[1] = matrix2[0].length;
	   
	    if(element1[1] != element2[0]) {
	        System.out.println("Matrix 1 # of columns must equal Matrix 2 # of rows");
	        return null;
	    }
	    
	    // The new matrix has the same # of rows of M1 and the same # of columns as M2.
	    double[][] toReturn = new double[element1[0]][element2[1]];
	    
	    // Initialize M12 matrix
	    for (int i = 0; i < element1[0]; i++ ) {
	        for (int j = 0; j < element2[1]; j++) {
	            toReturn[i][j] = 0;
	    	}
	    }
	   
	    // Multiply the matrices together
	    for (int i = 0; i < element1[0]; i++ ) {
	        for (int j = 0; j < element2[1]; j++) {
		        for (int k = 0; k < element1[1]; k++) {
	                toReturn[i][j] = toReturn[i][j] + matrix1[i][k] * matrix2[k][j];
		        }
	        }
	    }
	    
	    return toReturn; 
	}

	// -------------------------------------------------------------------------
	/**
	Sub MatrixVectorMult:
	<br></br>
	This subroutine takes a matrix and multiplies it by a vector.
	The dimension of the vector must equal the # of Matrix columns.
	*/

	public static double[] matrixVectorMult(double[][] matrix, double[] vector) {

	    int[] element1 = new int[2];
	    int element2;
	   
	    // Determine the number of elements in each dimension of each matrix
	    element1[0] = matrix.length;
	    element1[1] = matrix[0].length;
	    element2 = vector.length;
	    
	    if(element1[1] != element2) {
	        System.out.println("Matrix 1 # of columns must equal Matrix 2 # of rows");
	        return null;
	    }
	    
	    // The new matrix has the same # of rows of M1 and 1 column.
	    double[] vectorOut = new double[element1[0]];
	    
	    // Initialize Vout matrix
	    for (int i = 0; i < element1[0]; i++) {
	        vectorOut[i] = 0;
	    }
	   
	    // Multiply the matrices together
	    for (int i = 0; i < element1[0]; i++) {
	        for (int k = 0; k < element1[1]; k++) {
	            vectorOut[i] = vectorOut[i] + matrix[i][k] * vector[k];
			}
		}
	    
	    return vectorOut;
	}


	//  Returns the cross product of two 3-dimensional vectors.
	public static double[] crossProduct(double[] vector1, double[] vector2) {

	    double[] toReturn = new double[3];
	    
	    toReturn[0] = (vector1[1] * vector2[2] - vector2[1] * vector1[2]);
	    toReturn[1] = (vector1[2] * vector2[0] - vector2[2] * vector1[0]);
	    toReturn[2] = (vector1[0] * vector2[1] - vector2[0] * vector1[1]);
	   
	    return toReturn;   
	}

	public static double[] addVectors(double[] vector1, double[] vector2) {
	    
	    double[] toReturn = new double[3];
	    
	    for (int i = 0; i < 3; i++) {
	        toReturn[i] = vector1[i] + vector2[i];
	    }
	    
	    return toReturn;   
	}

	public static double[] subtractVectors(double[] vector1, double[] vector2) {
	    
	    double[] toReturn = new double[3];
	    
	    for (int i = 0; i < 3; i++) {
	        toReturn[i] = vector1[i] - vector2[i];
	    }
	    
	    return toReturn;   
	}

	public static double[] multiplyVectorByScalar(double scalar, double[] vector) {

	    double[] toReturn = new double[3];
	    
	    for (int i = 0; i < 3; i++) {
	        toReturn[i] = scalar * vector[i];
	    }
	   
	    return toReturn;   
	}
}