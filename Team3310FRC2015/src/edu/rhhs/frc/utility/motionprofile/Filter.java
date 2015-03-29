package edu.rhhs.frc.utility.motionprofile;

import edu.rhhs.frc.utility.motionprofile.MotionProfile.ProfileMode;

public class Filter 
{
	/*
	 *******************************************************************************************
	 * Subroutine Filter                                                                       *
	 *                                                                                         *
	 * OBJECTIVES:                                                                             *
	 *   (1) Takes a step input and returns a second order curve output.                       *
	 *                                                                                         *
	 *                                                                                         *
	 * REVISIONS:                                                                              *
	 *   AUTHOR              DESCRIPTION                                          DATE         *
	 *    (1) PDC    - Initial Release                                            03 MAR 98    *
	 *    (2) PDC    - Changed the Filter Length Problem for Short Motion.        28 MAR 98    *
	 *    (3) PDC    - Changed to take any input and filter it.                   16 AUG 00    *
	 *                                                                                         *
	 *******************************************************************************************
	 */

	public static double[][] filter(ProfileMode profileMode, double[] jointAccels, double[] jointDecels, double cartAccels,
			double cartDecels, int numITPs, double[][] posIn) {

		double[] fil1 = null;
		double[] fil2 = null;
		double out1In2;
		int fL1 = 0;
		int fL2 = 0;

		double[][] out2 = new double[numITPs + 1][4];

		if (profileMode == ProfileMode.CartesianInputLinearMotion) {
			fL1 = (int)Math.round(cartAccels);
			fL2 = (int)Math.round(cartDecels);
			fil1 = new double[fL1]; 
			fil2 = new double[fL2];
		}

		for (int k = 0; k < 4; k++) {

			if (profileMode != ProfileMode.CartesianInputLinearMotion == true || (profileMode == ProfileMode.CartesianInputLinearMotion == true && k == 3)) {
				fL1 = (int)Math.round(jointAccels[k]);
				fL2 = (int)Math.round(jointDecels[k]);
				fil1 = new double[fL1]; 
				fil2 = new double[fL2];
			}

			// -------INITIALIZE FILTERS TO ZERO---------
			for (int i = 0; i < fL1; i++) { 
				fil1[i] = 0.0;
			}

			for (int i = 0; i < fL2; i++) { 
				fil2[i] = 0.0;
			}

			out2[0][k] = 0.0;

			// LOOP UNTIL FILTERS ARE CLEAR (NumITPs)
			for (int i = 0; i < numITPs; i++) {
				out1In2 = 0.0;
				// MOVE FILTER 1 FILTER VALUES TO THE NEXT STEP
				for (int j = fL1-1; j > 0; j--) {
					fil1[j] = fil1[j - 1];
					out1In2 = out1In2 + fil1[j];
				}

				// ADD DeltaPos TO FILTER 1'S FIRST STEP
				fil1[0] = posIn[i + 1][k] - posIn[i][k];

				// DEFINE THE OUTPUT FROM FILTER 1
				out1In2 = out1In2 + fil1[0];
				out1In2 = out1In2 / fL1;
				out2[i + 1][k] = 0.0;

				// MOVE FILTER 2 FILTER VALUES TO THE NEXT STEP
				for (int j = fL2-1; j > 0; j--) {
					fil2[j] = fil2[j - 1];
					out2[i + 1][k] = out2[i + 1][k] + fil2[j];
				}

				// FILTER 2 1st value set to FILTER 1 out
				fil2[0] = out1In2;
				out2[i + 1][k] = out2[i + 1][k] + fil2[0];
				out2[i + 1][k] = out2[i + 1][k] / fL2;	            
			}
		}

		return out2;
	}

	public class ServoFilterOutput {
		public double[][] servoOut;
		int pointCount;

		public ServoFilterOutput() {
		}

		public void output(int outPts) {
			System.out.println("Time (sec), J1 (deg), J2 (deg), J3 (deg), J4 (deg)");
			for (int i = 0; i < servoOut.length; i+=outPts) {
				System.out.println(i/1000.0 + "," + 
						servoOut[i][0] + "," + servoOut[i][1] + "," + servoOut[i][2]  + "," + servoOut[i][3] );
			}
		}		

	}

	public ServoFilterOutput servoFilter(double servoFilterOutputSec, double itp, double ted, double[][] controlOut, int totalPoints, boolean expEnable) {

		ServoFilterOutput out = new ServoFilterOutput();

		//	    int SFL1 = (int)Math.round(itp * 1000);
		//	    int SFL2 = (int)Math.round(Ted * 1000);
		//	    int SFL1 = (int)Math.round(itp * 100);
		//	    int SFL1 = 1;
		int SFL1 = (int) Math.round(itp / servoFilterOutputSec);
		int SFL2 = (int) Math.round(ted / servoFilterOutputSec);
		double[] sFil1 = new double[SFL1];
		double[] sFil2 = new double[SFL2];
		double[] weight = new double[SFL2];
		//double Sout;
		double totalWeight;
		double out1In2;

		out.servoOut = new double[4][totalPoints * SFL1 + SFL2 + 1];

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
			out.servoOut[k][0] = 0.0;
			//Sout = 1.0;

			// If the second stage is exponential, then we need a weight factor
			if (expEnable) {	        
				totalWeight = 0.0;

				for (int j = 0; j < SFL2; j++) {
					weight[j] = Math.exp(-(double)(j+1) / (double)SFL2);
					totalWeight = totalWeight + weight[j];
				}
			} 
			else {
				totalWeight = SFL2;

				for (int j = 0; j < SFL2; j++) {
					weight[j] = 1.0;
				}
			}

			// Loop until all servo filters are clear
			int i = 0;
			while (i < totalPoints * SFL1 + SFL2) {
				i = i + 1;
				out1In2 = 0.0;

				// Move filter 1 values to the next step
				for (int L = SFL1 - 1; L > 0; L--) {
					sFil1[L] = sFil1[L - 1];
					out1In2 = out1In2 + sFil1[L];
				}

				// When the delay causes a longer time, add 0s to the filter
				if (i / SFL1 > totalPoints) {
					sFil1[0] = 0.0;
				} 
				else {
					// Determine which ITP input is used for each millisecond
					// Must go backwards to catch the right input
					for (int M = totalPoints - 1; M >= 0; M--) {
						if (i / SFL1 < (M + 1)) {
							//	                        SFIL1[0] = ControlOut[M][k] / (itp * 1000);
							sFil1[0] = controlOut[M][k] / (itp / servoFilterOutputSec);
						}
					}
				}

				// Redefine filter 1 output
				out1In2 = out1In2 + sFil1[0];
				out1In2 = out1In2 / SFL1;
				out.servoOut[k][i] = 0.0;

				// Move filter 2 values to next step

				for (int L = SFL2-1; L >= 0; L--) {
					if (L != 0) {
						sFil2[L] = sFil2[L - 1];
					} 
					else {
						sFil2[L] = out1In2;
					}

					out.servoOut[k][i] = out.servoOut[k][i] + weight[L] * sFil2[L];
				}

				out.servoOut[k][i] = out.servoOut[k][i] / totalWeight;
				//Sout = Math.abs(out.servoOut[k][i]);
				if (k == 0) {
					out.pointCount = i;
				}
			}
		}

		// Transpose the ServoOut array to be the same as the other arrays.
		double[][] servoOutTranspose = new double[out.pointCount + 1][4];

		// ---Transpose ServoOut and save the contents to a temporary array
		for (int i = 0;  i < out.pointCount + 1; i++) {
			for (int j = 0; j < 4; j++) {
				servoOutTranspose[i][j] = out.servoOut[j][i];
			}
		}

		// ---save ServoOut with its original data transposed.
		out.servoOut = servoOutTranspose;

		return out;
	}
}