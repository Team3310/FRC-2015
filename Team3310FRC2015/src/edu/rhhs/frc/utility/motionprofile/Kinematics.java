package edu.rhhs.frc.utility.motionprofile;

public class Kinematics 
{
	/*---------------------------------------------------------------------------
	 * Subroutine IKIN
	 *    The P-50 Kinematics Routines assume no J2 or J3 offset
	 * 
	 * Inputs:
	 *    [0] Pos() - X,Y,Z position data
	 *    [1] Numpoints - Number of ITPs in entire path
	 *    [2] L() - Arm Length Data
	 *    (4) ElbowUp - To determine the configuration of the outer arm
	 *    (5) Front - To determine the configuration of the turret
	 * 
	 * Outputs:
	 *    [0] ErrorFlag - To notify the Main code that the position is unreachable
	 *    [1] userAngles() - J1, J2, J3 position data
	 * ----------------------------------------------------------------------------*/

	public class IKinematicsOutput 
	{
		double[][] userAngles;
		boolean errorFlag;

		public IKinematicsOutput(int numPoints) {
			userAngles = new double[numPoints + 1][4]; // 4 Array Elements in the second dimension due to arm angles
			errorFlag = false;
		}
	}

	public IKinematicsOutput getInstanceIKINOutput(int numPoints) {
		return new IKinematicsOutput(numPoints);
	}

	public IKinematicsOutput inverseKinematics(double[][] pos, int numPoints, double[] armLengths, double[] d, boolean elbowUp, boolean front) {

		IKinematicsOutput out = new IKinematicsOutput(numPoints);

		double kappa, px1, py1, px1py1sqrd/*, gamma*/;
		double c1, s1;
		double den, thetaT, t2Num;
		double[] theta = new double[4];    

		// Loop through each ITP and calculate the IKIN
		for (int i = 0; i < numPoints + 1; i++) {
			//gamma = pos[i][3]; - For J4, but removed.
			if (front) { // Front reach solution
				if (isZero(pos[i][0]) && isZero(pos[i][1])) {
					theta[0] = 0;
				}
				else {					
//					Theta[0] = Math.atan2(Pos[i][1] - L[5] * Math.sin(gamma), Pos[i][0] - L[5] * Math.cos(gamma));  // 4 axis robot					
					theta[0] = Math.atan2(pos[i][1], pos[i][0]);  // 3 axis robot
				}

			} 
			else { // Back reach solution
				if (pos[i][0] == 0 && pos[i][1] == 0 ) {
					theta[0] = Math.PI;
				} 
				else {					
//					Theta[0] = Math.atan2(Pos[i][1] - L[5] * Math.sin(gamma), Pos[i][0] - L[5] * Math.cos(gamma)) + Math.PI;  // 4 axis robot					
					theta[0] = Math.atan2(pos[i][1], pos[i][0]) + Math.PI;  // 3 axis robot
				}
			}

			// Define cos and sin of theta1 
			c1 = Math.cos(theta[0]);
			s1 = Math.sin(theta[0]);

			// Where Theta[2]=asin(Kappa)
//			Theta[3] = Pos[i][3] - Theta[0];  // 4 axis
//			px1 = Pos[i][0] * C1 + Pos[i][1] * S1 - L[3] - L[5] * Math.cos(Theta[3]);   // 4 axis
			px1 = pos[i][0] * c1 + pos[i][1] * s1 - armLengths[3] - armLengths[5];  // 3 axis
//			py1 = D[0] - Pos[i][2] + D[4];
			py1 = d[0] - pos[i][2];
			px1py1sqrd = px1*px1 + py1*py1;
			kappa = (px1py1sqrd - armLengths[1]*armLengths[1] - armLengths[2]*armLengths[2]) / (2 * armLengths[1] * armLengths[2]);

			// Check to see if position is reachable & Calculate Theta3
			if (Math.abs(kappa) > 1 ) {
				System.out.println("Position = " +  pos[i][0] + ", " +  pos[i][1]  + ", " +  pos[i][2]+ " at time = " + i * 0.001 + " seconds is unreachable");
				out.errorFlag = true;
				return out;
			} 
			else if (Math.abs(kappa) == 1) {
				theta[2] = -Math.acos(kappa);
			} 
			else if (elbowUp) {
				theta[2] = -Math.acos(kappa);
			} 
			else {
				theta[2] = Math.PI + Math.acos(kappa);
			}

			// Calculate Theta2	        
			// --Calculate the denominator in the S2 and C2 equations
			den = 2.0 * armLengths[1] * Math.sqrt(px1py1sqrd); 

			// --Determine if in Singularity
			if (isZero(den)) {
				System.out.println("Position at time = " + numPoints * 0.001 + " seconds causes singularity");
				out.errorFlag = true;
				return null;
			} 
			else {
				thetaT = Math.atan2(-py1, px1);
				t2Num = armLengths[1]*armLengths[1] - armLengths[2]*armLengths[2] + px1py1sqrd;
			}

			// --Calculate Theta2
			theta[1] = -thetaT - Math.acos(t2Num/den);

			// Calculate User Angles and reallocate them to pos(i,?)
			out.userAngles[i][0] = theta[0];
			out.userAngles[i][1]  = Math.PI / 2 + theta[1];
			out.userAngles[i][2] = theta[2] - theta[1];
//			out.userAngles[i][3] = Theta[3];   // 4 axis
			out.userAngles[i][3] = 0;
		}

		out.errorFlag = false;

		return out;
	}

	// This subroutine takes the Joint(User) angles and converts them to Kinematic
	// angles.  It is necessary to do this to calculate Newton-Euler Dynamics.

	public static double[][] userToKin(double[][] jointPos, int numPoints) {

		for (int i = 0; i < numPoints; i++) { 
			// Theta1 and J1 are the same

			// Theta2 = 90 - J2
			jointPos[i][1] = Math.PI / 2 - jointPos[i][1]; // JointPos(I,2) is now Theta2

			// Theta3 = J3 - Theta2 + 90
			jointPos[i][2] = jointPos[i][2] - jointPos[i][1] + Math.PI / 2;
		}

		return jointPos;
	}

	public static boolean isZero(double value) {
		return Math.abs(value) < 3 * Double.MIN_VALUE;
	}

	/** Subroutine KinToUser:
	 <br></br>
	 This subroutine takes the Kinematic angles and converts them to Joint
	 (User) angles. JointPos() starts as Theta(I) and Leaves as J(I)
	*/
	public static double[][] kinToUser(double[][] jointPos, int numPoints) {

		for (int i = 0; i < numPoints; i++) {
			// Theta1 and J1 are the same

			//  J2 = 90 - Theta2
			jointPos[i][1] = Math.PI / 2 - jointPos[i][1];

			//  J3 = Theta3 - J2
			jointPos[i][2] = jointPos[i][2] - jointPos[i][1];
		}

		return jointPos;
	}

	// ---------------------------------------------------------------------------
	// Subroutine FKIN
	// Inputs:
	//    [0] Pos() - Joint (J1, J2, J3) filter output data
	//    [1] Numpoints - Number of real points in entire path
	//    [2] L() - Arm Length Data
	//    (4) InputPos() - Joint input position
	//        - We only need InputPos(0,J), where J is 1,2,and 3
	//        - InputPos(0,J) is Taught point 0 in joint space.
	// 
	// Outputs:
	//    [0] CartPos() - X, Y, Z actual position data
	//    [1] Pos() - J1, J2, J3 actual position data
	// 
	// Remarks:
	//    [0] Pos() is enters as servo output, but exits as actual joint
	// ----------------------------------------------------------------------------

	public static double[][] forwardKinematics(double[][] pos, int numPoints, double[] l, double[] d) {
		double[][] cartPos = new double[numPoints + 1][4]; //Cartesian Position Array

		for (int i = 0; i < numPoints + 1; i++) {

			// -------THE FOLLOWING CODE HAS BEEN MOVED TO THE MAIN SUBASSEMBLY
			// -------RIGHT AFTER THE SERVO FILTER SUBROUTINE -----------------
			//         if I = 0 ) {
			//             for J = 1 To 3
			//                 Pos(I, J) = InputPos(I, J)
			//             Next J
			// 
			//         } else {
			// 
			//             for J = 1 To 3
			//                 Pos(I, J) = Pos(I - 1, J) + Pos(I, J)
			//             Next J
			// 
			//         }

			// We can start FKIN because the FKIN are solved with
			// user angles, not kinematic angles.
			// Start forward Kinematics
			// X position

			//	        CartPos[i][0] = Math.cos(Pos[i][0]) * (Math.cos(Pos[i][2]) * L[2] + 
			//	                        L[1] * Math.sin(Pos[i][1]));
			//	        
			//	        // Y position
			//	        CartPos[i][1] = Math.sin(Pos[i][0]) * (Math.cos(Pos[i][2]) * L[2] + 
			//	                        L[1] * Math.sin(Pos[i][1]));
			//	                        
			//	        // Z position
			//	        CartPos[i][2] = L[2] * Math.sin(Pos[i][2]) + L[1] * Math.cos(Pos[i][1]);
			//	        CartPos[i][3] = Pos[i][0] + Pos[i][3];

// 4 axis robot w/ grounding link
//			double gamma = Pos[i][0] + Pos[i][3];
//			double j2j3Term = L[3] + L[2] * Math.cos(Pos[i][2]) + L[1] * Math.sin(Pos[i][1]);
//			CartPos[i][0] = Math.cos(gamma) * L[5] + Math.cos(Pos[i][0]) * j2j3Term;
//
//			// Y position
//			CartPos[i][1] = Math.sin(gamma) * L[5] + Math.sin(Pos[i][0]) * j2j3Term;
//
//			// Z position
//			CartPos[i][2] = D[0] + D[4] + L[1] * Math.cos(Pos[i][1]) + L[2] * Math.sin(Pos[i][2]);
//			CartPos[i][3] = gamma;
			
			
			// Removed J4 so 3 axis robot w/ grounding link
			double j2j3Term = l[3] + l[2] * Math.cos(pos[i][2]) + l[1] * Math.sin(pos[i][1]) + l[5];
			
			// X position
			cartPos[i][0] = Math.cos(pos[i][0]) * j2j3Term;

			// Y position
			cartPos[i][1] = Math.sin(pos[i][0]) * j2j3Term;

			// Z position
			cartPos[i][2] = d[0] + l[1] * Math.cos(pos[i][1]) + l[2] * Math.sin(pos[i][2]);
			cartPos[i][3] = 0.0;
		}  
		return cartPos;
	}
}