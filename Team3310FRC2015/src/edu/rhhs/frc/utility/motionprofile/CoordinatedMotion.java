package edu.rhhs.frc.utility.motionprofile;

import edu.rhhs.frc.utility.motionprofile.MotionProfile.ProfileMode;

public class CoordinatedMotion {

	// -------------------------------------------------------------------------
	// | Sub CoMotion:
	// |     INPUTS  - Npts, dJoint, VpathIn, JointSpeed, Tacc1, Tacc2, IsLinear
	// |     OUTPUTS - ActualPathSpeed, T4, PathLength, Tcnt
	// |
	// |  Description:
	// |          Takes input parameters and determines each joint speed
	// |      in order to end up at the final point at the same time.  For
	// |      linear moves, simply calculates T4 based on path length.
	// |      Coordinated motion is automatically achieved for linear move.
	// -------------------------------------------------------------------------

	public class CoMotionOutput {
		public double[][] Tseg;
		public double[] PathLength;
		public double[][] ActualPathSpeed;
		public double[][] Tcnt;
		
		public CoMotionOutput(int Npts) {
			Tseg = new double[Npts][4];
		    PathLength = new double[Npts]; 
			ActualPathSpeed = new double[Npts][4];
			Tcnt= new double[Npts][4];
		}
	}

    public CoMotionOutput coMotion(int Npts, double[][] dJoint, double[] VpathIn, double[] JointSpeedPercent, 
	             double[] JointSpeed, double[] Tacc1, double[] Tacc2, 
	             ProfileMode profileMode, double[] CNT, 
	             double CartAcc1, double CartAcc2, double itp) {

    	CoMotionOutput out = new CoMotionOutput(Npts);
    	
	    if ( profileMode != ProfileMode.CartesianInputLinearMotion ) {
	    
			double[] Tmax = new double[Npts];
			double[][] Ttemp = new double[Npts][4];
		    
	        // Calculate each joint speed based on percent speed			
	        for (int i = 0; i < Npts; i++) {
	            Tmax[i] = 0.0;	            
	            
	            for (int j = 0; j < 4; j++) {
	                out.ActualPathSpeed[i][j] = JointSpeedPercent[i] / 100.0 * JointSpeed[j];
	                out.Tseg[i][j] = Math.abs(dJoint[i][j]) / out.ActualPathSpeed[i][j];
	                out.Tseg[i][j] = Math.ceil(out.Tseg[i][j] / itp);
	                
	                
	                // Determines the itp number for the end condition
	                if ( i < Npts - 1) {
	                    out.Tcnt[i][j] = Math.round((1 - Math.sqrt(CNT[i] / 100.0)) * (Tacc1[j] + Tacc2[j]));
	                } 
	                else {
	                    out.Tcnt[i][j] = Tacc1[j] + Tacc2[j];
	                }
	                
	                
	                if ( Math.abs(dJoint[i][j]) > 0 ) {
	                    Ttemp[i][j] = out.Tseg[i][j] + out.Tcnt[i][j];
	                } 
	                else {
	                    Ttemp[i][j] = out.Tseg[i][j];
	                }
	            
	                if ( Ttemp[i][j] > Tmax[i] ) {
	                    Tmax[i] = Ttemp[i][j];
	                }
	                
	            }
	            
	            // Use longest joint time for every joint speed
	            for (int j = 0; j < 4; j++) {
	                 // T4 is in integration points, NOT seconds
	                out.Tseg[i][j] = Tmax[i] - out.Tcnt[i][j];
	                if ( out.Tseg[i][j] != 0 ) {
	                    out.ActualPathSpeed[i][j] = dJoint[i][j] / (out.Tseg[i][j] * itp);
	                } 
	                else {
	                    out.ActualPathSpeed[i][j] = 0.0;
	                }
	            }
	        }
	    } 
	    else {
	        
	        // Calculate linear speed based on Input Speed
	        for (int i = 0; i < Npts; i++) {
		        for (int j = 0; j < 3; j++) {
	               out.ActualPathSpeed[i][j] = VpathIn[i];
	            }
	        }
	        
	        // Calculate T4 for linear moves
	        for (int i = 0; i < Npts; i++) {
	            out.PathLength[i] = Math.sqrt(dJoint[i][0]*dJoint[i][0] + dJoint[i][1]*dJoint[i][1] + dJoint[i][2]*dJoint[i][2]);
	    		for (int j = 0; j < 3; j++) {
	                out.Tseg[i][j] = Math.abs(out.PathLength[i] / out.ActualPathSpeed[i][j]);
	                out.Tseg[i][j] = Math.ceil(out.Tseg[i][j] / itp);
	                if ( i < Npts - 1 ) {
	                    out.Tcnt[i][j] = Math.round((1 - Math.sqrt(CNT[i] / 100.0)) * (CartAcc1 + CartAcc2));
	                } else {
	                    out.Tcnt[i][j] = CartAcc1 + CartAcc2;
	                }
	            }
	        }
	        	        
			// Calculate each joint speed based on percent speed			
	        for (int i = 0; i < Npts; i++) {
                out.Tseg[i][3] = out.Tseg[i][0];
                out.Tcnt[i][3] = out.Tcnt[i][0];

                if ( out.Tseg[i][3] != 0 ) {
                    out.ActualPathSpeed[i][3] = dJoint[i][3] / (out.Tseg[i][3] * itp);
                } 
                else {
                    out.ActualPathSpeed[i][3] = 0.0;
                }
	        }
	    }
	    
	    return out;
	}

    
	public class CoMotionFilterOutput {
		public int NumITPs;
		public double[][] PosFilter;
		public double[][] dDisStep;
		
		public CoMotionFilterOutput(int NumPaths, double[][] Tseg, double[][] Tcnt) {
		    NumITPs = 0;
		    for (int i = 0; i < NumPaths; i++) {
		        NumITPs = (int)(NumITPs + Math.round(Tseg[i][0] + Tcnt[i][0]));
		    }
		    
		    PosFilter = new double[NumITPs + 1][4];
		    dDisStep = new double[NumPaths][4];	    
		}
	}
    
	public CoMotionFilterOutput filterInput(double[][] dDis, double[][] Tseg, double[][] Tcnt, double[][] Ptaught, int NumPaths) {
		
		CoMotionFilterOutput out = new CoMotionFilterOutput(NumPaths, Tseg, Tcnt);
	                
	    // Calculate the input position vs. time for the entire path
	    // NOTE: Each axis NumITPs should be the same, that is why we can have a constant
        for (int j = 0; j < 4; j++) {
	        int k = 0;
	        out.NumITPs = 0;
	        int Counter1 = 0;
	        // Set the position at time 0 to the 1st taught position
	        out.PosFilter[k][j] = Ptaught[k][j];
	        k = 1;
	        for (int i = 0; i < NumPaths; i++) {
	            Counter1 = (int)(out.NumITPs + Math.round(Tseg[i][j]));
	            out.NumITPs = (int)(Counter1 + Math.round(Tcnt[i][j]));
	            if ( Tseg[i][j] != 0.0 ) {
	                out.dDisStep[i][j] = dDis[i][j] / Tseg[i][j];
	            } 
	            else {
	                out.dDisStep[i][j] = 0.0;
	            }
	            
	            // The following loop determines the filter input position
	            // at each ITP. If using joint filter for linear motion,
	            // then IKIN can be called after this Subroutine.
	            
	            while( k < out.NumITPs + 1) {
	                if ( k <= Counter1 ) {
	                    out.PosFilter[k][j] = out.PosFilter[k-1][j] + out.dDisStep[i][j];
	                    k = k + 1;
	                } 
	                else {
	                    out.PosFilter[k][j] = out.PosFilter[k-1][j];
	                    k = k + 1;
	                }
	            }
	        }
	    }
	    
	    return out;
	}

}
