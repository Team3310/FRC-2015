package edu.rhhs.frc.utility;

public class CoordinatedMotion {

	// -------------------------------------------------------------------------
	// | Sub CoMotion:
	// |     INPUTS  - Npts, dJoint, VpathIn, JointSpeed, Tacc1, Tacc2, IsLinear
	// |     OUTPUTS - ActualPathSpeed, T4, PathLength
	// |
	// |  Description:
	// |          Takes input parameters and determines each joint speed
	// |      in order to end up at the final point at the same time.  For
	// |      linear moves, simply calculates T4 based on path length.
	// |      Coordinated motion is automatically achieved for linear move.
	// -------------------------------------------------------------------------


	public double[][] coMotion(int Npts, double[][] dJoint, double[] VpathIn, 
	             double[] JointSpeed, double[] Tacc1, double[] Tacc2, 
	             boolean islinear, double[] CNT, 
	             double CartAcc1, double CartAcc2, double itp, 
	             double[][] T4, double[] PathLength) {

		double[][] Ttemp = new double[Npts][3];
		double[] Tmax = new double[Npts];
		double[][] ActualPathSpeed = new double[Npts][3];
		double[][] Tcnt= new double[Npts][3];
	    
	    for (int j = 0; j < 3; j++) {
	        Tacc1[j] = Math.ceil(Tacc1[j] / itp);
	        Tacc2[j] = Math.ceil(Tacc2[j] / itp);
	    }
	    
	    // Convert cartesian acceleration filters to integration points
	    CartAcc1 = Math.ceil(CartAcc1 / itp);
	    CartAcc2 = Math.ceil(CartAcc2 / itp);
	    
	    if ( islinear == false ) {
	    
	        // Calculate each joint speed based on percent speed
	        
	        for (int i = 0; i < Npts; i++) {
	            Tmax[i] = 0.0;	            
	            
	            for (int j = 0; j < 3; j++) {
	                ActualPathSpeed[i][j] = VpathIn[i] / 100.0 * JointSpeed[j];
	                T4[i][j] = Math.abs(dJoint[i][j]) / ActualPathSpeed[i][j];
	                T4[i][j] = Math.ceil(T4[i][j] / itp);
	                
	                
	                // Determines the itp number for the end condition
	                if ( i < Npts ) {
	                    Tcnt[i][j] = Math.round((1 - Math.sqrt(CNT[i] / 100.0)) * (Tacc1[j] + Tacc2[j]));
	                } 
	                else {
	                    Tcnt[i][j] = Tacc1[j] + Tacc2[j];
	                }
	                
	                
	                if ( Math.abs(dJoint[i][j]) > 0 ) {
	                    Ttemp[i][j] = T4[i][j] + Tcnt[i][j];
	                } 
	                else {
	                    Ttemp[i][j] = T4[i][j];
	                }
	            
	                if ( Ttemp[i][j] > Tmax[i] ) {
	                    Tmax[i] = Ttemp[i][j];
	                }
	                
	            }
	            
	            // Use longest joint time for every joint speed
	            for (int j = 0; j < 3; j++) {
	                 // T4 is in integration points, NOT seconds
	                T4[i][j] = Tmax[i] - Tcnt[i][j];
	                if ( T4[i][j] != 0 ) {
	                    ActualPathSpeed[i][j] = dJoint[i][j] / (T4[i][j] * itp);
	                } 
	                else {
	                    ActualPathSpeed[i][j] = 0.0;
	                }
	            }
	        }
	    } 
	    else {
	        
	        // Calculate linear speed based on Input Speed
	        for (int i = 0; i < Npts; i++) {
		        for (int j = 0; j < 3; j++) {
	               ActualPathSpeed[i][j] = VpathIn[i];
	            }
	        }
	        
	        // Calculate T4 for linear moves
        for (int i = 0; i < Npts; i++) {
	            PathLength[i] = Math.sqrt(dJoint[i][1]*dJoint[i][1] + dJoint[i][2]*dJoint[i][2] + dJoint[i][3]*dJoint[i][3]);
	    		for (int j = 0; j < 3; j++) {
	                T4[i][j] = Math.abs(PathLength[i] / ActualPathSpeed[i][j]);
	                T4[i][j] = Math.ceil(T4[i][j] / itp);
	                if ( i < Npts ) {
	                    Tcnt[i][j] = Math.round((1 - Math.sqrt(CNT[i] / 100.0)) * (CartAcc1 + CartAcc2));
	                } else {
	                    Tcnt[i][j] = CartAcc1 + CartAcc2;
	                }
	            }
	        }
	    }
	    
	    return Tcnt;
	}

	public static double[][] filterInput(double[][] dDis, double[][] T4, double[][] Tcnt, 
			double[][] Ptaught, int NumPaths, 
			double[][] PosFilterIn, int NumITPs, double[][] dDisStep) {
	                
	    int Counter1;
	    dDisStep = new double[NumPaths][3];
	    
	    NumITPs = 0;
	    for (int i = 0; i < NumPaths; i++) {
	        NumITPs = (int)(NumITPs + Math.round(T4[i][1] + Tcnt[i][1]));
	    }
	    
	    PosFilterIn = new double[NumITPs][3];
	    // Calculate the input position vs. time for the entire path
	    // NOTE: Each axis NumITPs should be the same, that is why we can have a constant
        for (int j = 0; j < 3; j++) {
	        int k = 0;
	        NumITPs = 0;
	        Counter1 = 0;
	        // Set the position at time 0 to the 1st taught position
	        PosFilterIn[k][j] = Ptaught[k][j];
	        k = 1;
	        for (int i = 0; i < NumPaths; i++) {
	            Counter1 = (int)(NumITPs + Math.round(T4[i][j]));
	            NumITPs = (int)(Counter1 + Math.round(Tcnt[i][j]));
	            if ( T4[i][j] != 0.0 ) {
	                dDisStep[i][j] = dDis[i][j] / T4[i][j];
	            } 
	            else {
	                dDisStep[i][j] = 0.0;
	            }
	            
	            // The following loop determines the filter input position
	            // at each ITP. If using joint filter for linear motion,
	            // then IKIN can be called after this Subroutine.
	            
	            while( k < NumITPs) {
	                if ( k <= Counter1 ) {
	                    PosFilterIn[k][j] = PosFilterIn[k-1][j] + dDisStep[i][j];
	                    k = k + 1;
	                } 
	                else {
	                    PosFilterIn[k][j] = PosFilterIn[k-1][j];
	                    k = k + 1;
	                }
	            }
	        }
	    }
	    
	    return PosFilterIn;
	}


}
