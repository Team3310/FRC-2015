package edu.rhhs.frc.utility.motionprofile;

import edu.rhhs.frc.utility.motionprofile.MotionProfile.ProfileMode;

public class Filter {
	/*
	***********************************************************************************
	* Subroutine Filter
	*
	* OBJECTIVES:
	*   (1) Takes a step input and returns a second order curve output.
	*
	*
	* REVISIONS:
	*   AUTHOR              DESCRIPTION                                          DATE
	*    (1) PDC    - Initial Release                                            03 MAR 98
	*    (2) PDC    - Changed the Filter Length Problem for Short Motion.        28 MAR 98
	*    (3) PDC    - Changed to take any input and filter it.                   16 AUG 00
	*
	************************************************************************************ 
	*/

	public static double[][] filter(ProfileMode profileMode, double[] Tacc1, double[] Tacc2, double CartAcc1,
			double CartAcc2, int NumITPs, double[][] PosIn) {

	    double[] Fil1 = null;
	    double[] Fil2 = null;
	    double Out1In2;
	    int FL1 = 0;
	    int FL2 = 0;
	    
	    double[][] Out2 = new double[NumITPs + 1][4];
	    
	    if (profileMode == ProfileMode.CartesianInputLinearMotion) {
	        FL1 = (int)Math.round(CartAcc1);
	        FL2 = (int)Math.round(CartAcc2);
	        Fil1 = new double[FL1]; 
	        Fil2 = new double[FL2];
	    }
	    
	    for (int k = 0; k < 4; k++) {
	            
	        if (profileMode != ProfileMode.CartesianInputLinearMotion == true || (profileMode == ProfileMode.CartesianInputLinearMotion == true && k == 3)) {
	            FL1 = (int)Math.round(Tacc1[k]);
	            FL2 = (int)Math.round(Tacc2[k]);
		        Fil1 = new double[FL1]; 
		        Fil2 = new double[FL2];
	        }
	    
	        // -------INITIALIZE FILTERS TO ZERO---------
	        for (int i = 0; i < FL1; i++) { 
	            Fil1[i] = 0.0;
	        }
	    
	        for (int i = 0; i < FL2; i++) { 
	            Fil2[i] = 0.0;
	        }
	             
	        Out2[0][k] = 0.0;
	        	        
	        // LOOP UNTIL FILTERS ARE CLEAR (NumITPs)
	        for (int i = 0; i < NumITPs; i++) {
	            Out1In2 = 0.0;
	            // MOVE FILTER 1 FILTER VALUES TO THE NEXT STEP
	            for (int j = FL1-1; j > 0; j--) {
	                Fil1[j] = Fil1[j - 1];
	                Out1In2 = Out1In2 + Fil1[j];
	        	}
	            
	            // ADD DeltaPos TO FILTER 1'S FIRST STEP
	            Fil1[0] = PosIn[i + 1][k] - PosIn[i][k];
	            
	            // DEFINE THE OUTPUT FROM FILTER 1
	            Out1In2 = Out1In2 + Fil1[0];
	            Out1In2 = Out1In2 / FL1;
	            Out2[i + 1][k] = 0.0;
	        
	            // MOVE FILTER 2 FILTER VALUES TO THE NEXT STEP
	            for (int j = FL2-1; j > 0; j--) {
	                Fil2[j] = Fil2[j - 1];
	                Out2[i + 1][k] = Out2[i + 1][k] + Fil2[j];
	            }
	            
	            // FILTER 2 1st value set to FILTER 1 out
	            Fil2[0] = Out1In2;
	            Out2[i + 1][k] = Out2[i + 1][k] + Fil2[0];
	            Out2[i + 1][k] = Out2[i + 1][k] / FL2;	            
	        }
	    }

	    return Out2;
	}

	public class ServoFilterOutput {
		public double[][] ServoOut;
		int pointCount;
		
		public ServoFilterOutput() {
		}
	}
	
	public ServoFilterOutput servoFilter(double itp, double Ted, double[][] ControlOut, int TotalPts, boolean ExpEnable) {
	    
		ServoFilterOutput out = new ServoFilterOutput();
		
	    int SFL1 = (int)Math.round(itp * 1000);
	    int SFL2 = (int)Math.round(Ted * 1000);
	    double[] SFIL1 = new double[SFL1];
	    double[] SFIL2 = new double[SFL2];
	    double[] Weight = new double[SFL2];
	    double Sout;
	    double TotalWeight;
	    double Out1In2;

	    out.ServoOut = new double[4][TotalPts * SFL1 + SFL2 + 1];
	    
	    /* ServoOut is dimensioned backwards, because we need to redimension
	    'the number of points.  That can only be done if the number of points
	    'is the last dimension in an array */
	    
	    // Initialize stage 1
//	    for (int i = 0; i < SFL1; i++) {
//	        SFIL1[i] = 0.0;
//	    }
	    
	    // Initialize stage 2
//	    for (int i = 0; i < SFL2; i++) {
//	        SFIL2[i] = 0.0;
//	    }
	    
	    for (int k = 0; k < 4; k++) {
	        // Set servo output to 0 at time 0
	        out.ServoOut[k][0] = 0.0;
	        Sout = 1.0;

	        // If the second stage is exponential, then we need a weight factor
	        if (ExpEnable) {	        
	            TotalWeight = 0.0;
	        
	            for (int j = 0; j < SFL2; j++) {
	                Weight[j] = Math.exp(-(double)(j+1) / (double)SFL2);
	                TotalWeight = TotalWeight + Weight[j];
	            }
	        } 
	        else {
	            TotalWeight = SFL2;
	        
	            for (int j = 0; j < SFL2; j++) {
	                Weight[j] = 1.0;
	            }
	        }
	    	        
	        // Loop until all servo filters are clear
	        int i = 0;
	        while (i < TotalPts * SFL1 + SFL2) {
	            i = i + 1;
	            Out1In2 = 0.0;
	        
	            // Move filter 1 values to the next step
	            for (int L = SFL1 - 1; L > 0; L--) {
	                SFIL1[L] = SFIL1[L - 1];
	                Out1In2 = Out1In2 + SFIL1[L];
	            }
	        
	            // When the delay causes a longer time, add 0s to the filter
	            if (i / SFL1 > TotalPts) {
	                SFIL1[0] = 0.0;
	            } 
	            else {
	                // Determine which ITP input is used for each millisecond
	                // Must go backwards to catch the right input
	                for (int M = TotalPts - 1; M >= 0; M--) {
	                    if (i / SFL1 < (M + 1)) {
	                        SFIL1[0] = ControlOut[M][k] / (itp * 1000);
	                    }
	                }
	            }
	        
	            // Redefine filter 1 output
	            Out1In2 = Out1In2 + SFIL1[0];
	            Out1In2 = Out1In2 / SFL1;
	            out.ServoOut[k][i] = 0.0;
	    
	            // Move filter 2 values to next step
	        
	            for (int L = SFL2-1; L >= 0; L--) {
	                if (L != 0) {
	                    SFIL2[L] = SFIL2[L - 1];
	                } 
	                else {
	                    SFIL2[L] = Out1In2;
	                }
	            
	                out.ServoOut[k][i] = out.ServoOut[k][i] + Weight[L] * SFIL2[L];
	            }
	        
	            out.ServoOut[k][i] = out.ServoOut[k][i] / TotalWeight;
	            Sout = Math.abs(out.ServoOut[k][i]);
	            if (k == 0) {
	                out.pointCount = i;
	            }
	    	}
	    }
	    
	    // Transpose the ServoOut array to be the same as the other arrays.
	    double[][] ServoOutTranspose = new double[out.pointCount + 1][4];
	    
	    // ---Transpose ServoOut and save the contents to a temporary array
	    for (int i = 0;  i < out.pointCount + 1; i++) {
	        for (int j = 0; j < 4; j++) {
	            ServoOutTranspose[i][j] = out.ServoOut[j][i];
	        }
	    }
	    
	    // ---save ServoOut with its original data transposed.
	    out.ServoOut = ServoOutTranspose;
	    
	    return out;
	}
}
