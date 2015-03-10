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
	public static double[][] mMult(double[][] M1, double[][] M2) {
	   
	    int[] Element1 = new int[2];
	    int[] Element2 = new int[2];
	   
	    // Determine the number of elements in each dimension of each matrix
	    Element1[0] = M1.length;
	    Element1[1] = M1[0].length;
	    Element2[0] = M2.length;
	    Element2[1] = M2[0].length;
	   
	    if(Element1[1] != Element2[0]) {
	        System.out.println("Matrix 1 # of columns must equal Matrix 2 # of rows");
	        return null;
	    }
	    
	    // The new matrix has the same # of rows of M1 and the same # of columns as M2.
	    double[][] M12 = new double[Element1[0]][Element2[1]];
	    
	    // Initialize M12 matrix
	    for (int i = 0; i < Element1[0]; i++ ) {
	        for (int j = 0; j < Element2[1]; j++) {
	            M12[i][j] = 0;
	    	}
	    }
	   
	    // Multiply the matrices together
	    for (int i = 0; i < Element1[0]; i++ ) {
	        for (int j = 0; j < Element2[1]; j++) {
		        for (int k = 0; k < Element1[1]; k++) {
	                M12[i][j] = M12[i][j] + M1[i][k] * M2[k][j];
		        }
	        }
	    }
	    
	    return M12; 
	}

	// -------------------------------------------------------------------------
	// Sub MatrixVectorMult:
	// 
	// This subroutine takes a matrix and multiplies it by a vector.
	// The dimension of the vector must equal the # of Matrix columns.
	// 
	// -------------------------------------------------------------------------

	public static double[] mVecMult(double[][] Min, double[] Vin) {

	    int[] Element1 = new int[2];
	    int Element2;
	   
	    // Determine the number of elements in each dimension of each matrix
	    Element1[0] = Min.length;
	    Element1[1] = Min[0].length;
	    Element2 = Vin.length;
	    
	    if(Element1[1] != Element2) {
	        System.out.println("Matrix 1 # of columns must equal Matrix 2 # of rows");
	        return null;
	    }
	    
	    // The new matrix has the same # of rows of M1 and 1 column.
	    double[] Vout = new double[Element1[0]];
	    
	    // Initialize Vout matrix
	    for (int i = 0; i < Element1[0]; i++) {
	        Vout[i] = 0;
	    }
	   
	    // Multiply the matrices together
	    for (int i = 0; i < Element1[0]; i++) {
	        for (int k = 0; k < Element1[1]; k++) {
	            Vout[i] = Vout[i] + Min[i][k] * Vin[k];
			}
		}
	    
	    return Vout;
	}


	//  Returns the cross product of two 3-dimensional vectors.
	public static double[] crossProd(double[] V1, double[] V2) {

	    double[] V3 = new double[3];
	    
	    V3[0] = (V1[1] * V2[2] - V2[1] * V1[2]);
	    V3[1] = (V1[2] * V2[0] - V2[2] * V1[0]);
	    V3[2] = (V1[0] * V2[1] - V2[0] * V1[1]);
	   
	    return V3;   
	}

	public static double[] vecAdd(double[] V1, double[] V2) {
	    
	    double[] V3 = new double[3];
	    
	    for (int i = 0; i < 3; i++) {
	        V3[i] = V1[i] + V2[i];
	    }
	    
	    return V3;   
	}

	public static double[] vecSub(double[] V1, double[] V2) {
	    
	    double[] V3 = new double[3];
	    
	    for (int i = 0; i < 3; i++) {
	        V3[i] = V1[i] - V2[i];
	    }
	    
	    return V3;   
	}

	public static double[] mVecByScalar(double ScalarIn, double[] Vin) {

	    double[] V3 = new double[3];
	    
	    for (int i = 0; i < 3; i++) {
	        V3[i] = ScalarIn * Vin[i];
	    }
	   
	    return V3;   
	}
}