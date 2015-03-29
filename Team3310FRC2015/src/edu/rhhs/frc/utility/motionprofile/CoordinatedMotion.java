package edu.rhhs.frc.utility.motionprofile;

import edu.rhhs.frc.utility.motionprofile.MotionProfile.ProfileMode;

public class CoordinatedMotion 
{
	// -------------------------------------------------------------------------
	// | Sub CoMotion:
	// |     INPUTS  - numPoints, dJoint, vPathIn, jointSpeed, jointAccels, jointDecels, isLinear
	// |     OUTPUTS - actualPathSpeed, tSeg, pathLength, tCnt
	// |
	// |  Description:
	// |          Takes input parameters and determines each joint speed
	// |      in order to end up at the final point at the same time.  For
	// |      linear moves, simply calculates T4 based on path length.
	// |      Coordinated motion is automatically achieved for linear move.
	// -------------------------------------------------------------------------

	public class CoMotionOutput 
	{
		public double[][] tSeg;
		public double[] pathLength;
		public double[][] actualPathSpeed;
		public double[][] tCnt;

		public CoMotionOutput(int Npts) {
			tSeg = new double[Npts][4];
			pathLength = new double[Npts]; 
			actualPathSpeed = new double[Npts][4];
			tCnt= new double[Npts][4];
		}
	}

	public CoMotionOutput coMotion(int numPoints, double[][] dJoint, double[] vPathIn, double[] jointSpeedPercent, 
			double[] jointSpeed, double[] jointAccels, double[] jointDecels, 
			ProfileMode profileMode, double[] continuity, 
			double cartAccels, double cartDecels, double itp) {

		CoMotionOutput out = new CoMotionOutput(numPoints);

		if (profileMode != ProfileMode.CartesianInputLinearMotion) {

			double[] tMax = new double[numPoints];
			double[][] tTemp = new double[numPoints][4];

			// Calculate each joint speed based on percent speed			
			for (int i = 0; i < numPoints; i++) {
				tMax[i] = 0.0;	            

				for (int j = 0; j < 4; j++) {
					out.actualPathSpeed[i][j] = jointSpeedPercent[i] / 100.0 * jointSpeed[j];
					out.tSeg[i][j] = Math.abs(dJoint[i][j]) / out.actualPathSpeed[i][j];
					out.tSeg[i][j] = Math.ceil(out.tSeg[i][j] / itp);


					// Determines the itp number for the end condition
					if (i < numPoints - 1) {
						out.tCnt[i][j] = Math.round((1 - Math.sqrt(continuity[i] / 100.0)) * (jointAccels[j] + jointDecels[j]));
					} 
					else {
						out.tCnt[i][j] = jointAccels[j] + jointDecels[j];
					}


					if (Math.abs(dJoint[i][j]) > 0 ) {
						tTemp[i][j] = out.tSeg[i][j] + out.tCnt[i][j];
					} 
					else {
						tTemp[i][j] = out.tSeg[i][j];
					}

					if (tTemp[i][j] > tMax[i] ) {
						tMax[i] = tTemp[i][j];
					}

				}

				// Use longest joint time for every joint speed
				for (int j = 0; j < 4; j++) {
					// T4 is in integration points, NOT seconds
					out.tSeg[i][j] = tMax[i] - out.tCnt[i][j];
					if (out.tSeg[i][j] != 0) {
						out.actualPathSpeed[i][j] = dJoint[i][j] / (out.tSeg[i][j] * itp);
					} 
					else {
						out.actualPathSpeed[i][j] = 0.0;
					}
				}
			}
		} 
		else {

			// Calculate linear speed based on Input Speed
			for (int i = 0; i < numPoints; i++) {
				for (int j = 0; j < 3; j++) {
					out.actualPathSpeed[i][j] = vPathIn[i];
				}
			}

			// Calculate T4 for linear moves
			for (int i = 0; i < numPoints; i++) {
				out.pathLength[i] = Math.sqrt(dJoint[i][0]*dJoint[i][0] + dJoint[i][1]*dJoint[i][1] + dJoint[i][2]*dJoint[i][2]);
				for (int j = 0; j < 3; j++) {
					out.tSeg[i][j] = Math.abs(out.pathLength[i] / out.actualPathSpeed[i][j]);
					out.tSeg[i][j] = Math.ceil(out.tSeg[i][j] / itp);
					if (i < numPoints - 1) {
						out.tCnt[i][j] = Math.round((1 - Math.sqrt(continuity[i] / 100.0)) * (cartAccels + cartDecels));
					} else {
						out.tCnt[i][j] = cartAccels + cartDecels;
					}
				}
			}

			// Calculate each joint speed based on percent speed			
			for (int i = 0; i < numPoints; i++) {
				out.tSeg[i][3] = out.tSeg[i][0];
				out.tCnt[i][3] = out.tCnt[i][0];

				if (out.tSeg[i][3] != 0) {
					out.actualPathSpeed[i][3] = dJoint[i][3] / (out.tSeg[i][3] * itp);
				} 
				else {
					out.actualPathSpeed[i][3] = 0.0;
				}
			}
		}
		return out;
	}


	public class CoMotionFilterOutput 
	{
		public int numITPs;
		public double[][] posFilter;
		public double[][] deltaDisStep;

		public CoMotionFilterOutput(int numPaths, double[][] tSeg, double[][] tCnt) {
			numITPs = 0;
			for (int i = 0; i < numPaths; i++) {
				numITPs = (int)(numITPs + Math.round(tSeg[i][0] + tCnt[i][0]));
			}

			posFilter = new double[numITPs + 1][4];
			deltaDisStep = new double[numPaths][4];	    
		}
	}

	public CoMotionFilterOutput filterInput(double[][] deltaDis, double[][] tSeg, double[][] tCnt, double[][] pTaught, int numPaths) {
		CoMotionFilterOutput out = new CoMotionFilterOutput(numPaths, tSeg, tCnt);

		// Calculate the input position vs. time for the entire path
		// NOTE: Each axis numITPs should be the same, that is why we can have a constant
		for (int j = 0; j < 4; j++) {
			int k = 0;
			out.numITPs = 0;
			int counter1 = 0;
			// Set the position at time 0 to the 1st taught position
			out.posFilter[k][j] = pTaught[k][j];
			k = 1;
			for (int i = 0; i < numPaths; i++) {
				counter1 = (int)(out.numITPs + Math.round(tSeg[i][j]));
				out.numITPs = (int)(counter1 + Math.round(tCnt[i][j]));
				if (tSeg[i][j] != 0.0) {
					out.deltaDisStep[i][j] = deltaDis[i][j] / tSeg[i][j];
				} 
				else {
					out.deltaDisStep[i][j] = 0.0;
				}

				// The following loop determines the filter input position
				// at each ITP. If using joint filter for linear motion,
				// then IKIN can be called after this Subroutine.

				while(k < out.numITPs + 1) {
					if (k <= counter1) {
						out.posFilter[k][j] = out.posFilter[k-1][j] + out.deltaDisStep[i][j];
						k++;
					} 
					else {
						out.posFilter[k][j] = out.posFilter[k-1][j];
						k++;
					}
				}
			}
		}
		return out;
	}
}