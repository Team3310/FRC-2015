package edu.rhhs.frc.utility.motionprofile;

public class Kinematics {
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

	public class IKINOutput {
		double[][] userAngles;
		boolean errorFlag;
		
		public IKINOutput(int NumPoints) {
	        userAngles = new double[NumPoints + 1][4];
		    errorFlag = false;
		}
	}
	
	public IKINOutput getInstanceIKINOutput(int NumPoints) {
		return new IKINOutput(NumPoints);
	}
	
	public IKINOutput iKIN(double[][] Pos, int NumPoints, double[] L, double[] D, boolean ElbowUp, boolean Front) {
	                       	    
		IKINOutput out = new IKINOutput(NumPoints);
		
		double Kappa, px1, py1, px1py1sqrd, gamma;
	    double C1, S1;
	    double Den, ThetaT, T2Num;
	    double[] Theta = new double[4];    
	    
	    // Loop through each ITP and calculate the IKIN
	    for (int i = 0; i < NumPoints + 1; i++) {
	    
	    	gamma = Pos[i][3];
	        if (Front == true) { // Front reach solution
	            if (isZero(Pos[i][0]) && isZero(Pos[i][1])) {
	                Theta[0] = 0;
	            } 
	            else {
	                Theta[0] = Math.atan2(Pos[i][1] - L[5] * Math.sin(gamma), Pos[i][0] - L[5] * Math.cos(gamma));
	            }
	            
	        } 
	        else { // Back reach solution
	            if (Pos[i][0] == 0 && Pos[i][1] == 0 ) {
	                Theta[0] = Math.PI;
	            } 
	            else {
	                Theta[0] = Math.atan2(Pos[i][1] - L[5] * Math.sin(gamma), Pos[i][0] - L[5] * Math.cos(gamma)) + Math.PI;
	            }
	        }
	    
	        // --Define cos and sin of theta1 
	        C1 = Math.cos(Theta[0]);
	        S1 = Math.sin(Theta[0]);

	        // Where Theta[2]=asin(Kappa)
	        Theta[3] = Pos[i][3] - Theta[0];
	        px1 = Pos[i][0] * C1 + Pos[i][1] * S1 - L[3] - L[5] * Math.cos(Theta[3]);
	        py1 = D[0] - Pos[i][2] + D[4];
	        px1py1sqrd = px1*px1 + py1*py1;
	        Kappa = (px1py1sqrd - L[1]*L[1] - L[2]*L[2]) / (2 * L[1] * L[2]);
	                
	        // Check to see if position is reachable & Calculate Theta3
	        if (Math.abs(Kappa) > 1 ) {
	            System.out.println("Position at time = " + NumPoints * 0.001 + " seconds is unreachable");
	            out.errorFlag = true;
	            return out;
	        } 
	        else if (Math.abs(Kappa) == 1) {
	            Theta[2] = -Math.acos(Kappa);
	        } 
	    	else if (ElbowUp == true) {
	            Theta[2] = -Math.acos(Kappa);
	        } 
	    	else {
	            Theta[2] = Math.PI + Math.acos(Kappa);
	        }
	        
	        // Calculate Theta2	        
	        // --Calculate the denominator in the S2 and C2 equations
	        Den = 2.0 * L[1] * Math.sqrt(px1py1sqrd); 
	        	                
	        // --Determine if in Singularity
	        if (isZero(Den)) {
	        	System.out.println("Position at time = " + NumPoints * 0.001 + " seconds causes singularity");
	            out.errorFlag = true;
	            return null;
	        } 
	        else {
	            ThetaT = Math.atan2(-py1, px1);
	            T2Num = L[1]*L[1] - L[2]*L[2] + px1py1sqrd;
	        }
	        
	        // --Calculate Theta2
	        Theta[1] = -ThetaT - Math.acos(T2Num/Den);
	        
	        // Calculate User Angles and reallocate them to pos(i,?)
	        out.userAngles[i][0] = Theta[0];
	        out.userAngles[i][1]  = Math.PI / 2 + Theta[1];
	        out.userAngles[i][2] = Theta[2] -Theta[1];
	        out.userAngles[i][3] = Theta[3];
	    }
	       
	    out.errorFlag = false;

	    return out;
	}

	// This subroutine takes the Joint(User) angles and converts them to Kinematic
	// angles.  It is necessary to do this to calculate Newton-Euler Dynamics.

	public static double[][] userToKin(double[][] JointPos, int NumPoints) {
	    
	    for (int i = 0; i < NumPoints; i++) { 
	        // Theta1 and J1 are the same
	        
	        // Theta2 = 90 - J2
	        JointPos[i][1] = Math.PI / 2 - JointPos[i][1]; // JointPos(I,2) is now Theta2
	        
	        // Theta3 = J3 - Theta2 + 90
	        JointPos[i][2] = JointPos[i][2] - JointPos[i][1] + Math.PI / 2;
	    }
	    
	    return JointPos;
	}
	
	public static boolean isZero(double value) {
		return Math.abs(value) < 3 * Double.MIN_VALUE;
	}

	// Subroutine KinToUser:
	// 
	// This subroutine takes the Kinematic angles and converts them to Joint
	// (User) angles. JointPos() starts as Theta(I) and Leaves as J(I)

	public static double[][] kinToUser(double[][] JointPos, int NumPoints) {
	    
	    for (int i = 0; i < NumPoints; i++) {
	        // Theta1 and J1 are the same
	        
	        //  J2 = 90 - Theta2
	        JointPos[i][1] = Math.PI / 2 - JointPos[i][1];
	        
	        //  J3 = Theta3 - J2
	        JointPos[i][2] = JointPos[i][2] - JointPos[i][1];
	    }
	    
	    return JointPos;
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

	public static double[][] fKIN(double[][] Pos, int NumPoints, double[] L, double[] D) {
	        
	    double[][] CartPos = new double[NumPoints + 1][4];
	    
	    for (int i = 0; i < NumPoints + 1; i++) {
	    
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
	    	

	    	double gamma = Pos[i][0] + Pos[i][3];
	    	double j2j3Term = L[3] + L[2] * Math.cos(Pos[i][2]) + L[1] * Math.sin(Pos[i][1]);
	        CartPos[i][0] = Math.cos(gamma) * L[5] + Math.cos(Pos[i][0]) * j2j3Term;
	        
	        // Y position
	        CartPos[i][1] = Math.sin(gamma) * L[5] + Math.sin(Pos[i][0]) * j2j3Term;
	                        
	        // Z position
	        CartPos[i][2] = D[0] + D[4] + L[1] * Math.cos(Pos[i][1]) + L[2] * Math.sin(Pos[i][2]);
	        CartPos[i][3] = gamma;
	    }  
	    
	    return CartPos;
	}
	
}
