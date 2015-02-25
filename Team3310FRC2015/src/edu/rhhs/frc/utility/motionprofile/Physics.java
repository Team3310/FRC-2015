package edu.rhhs.frc.utility.motionprofile;


public class Physics {
		
		// NOTE: 4th array spot on Vel & Acc is for magnitude on a linear move
		 		
		public static ProfileOutput VelAccPos(int Npts, double[][] JointPos, double[][] CartPos) {
		
			ProfileOutput out = new ProfileOutput(Npts, true);
			
			// Calculate X, Y, & Z components of Cartesian Speed & Acceleration
			for (int j = 0; j < 3; j++) {
		  
		      // Determine the Start values for V and A
		      out.cartVel[0][j] = 0.0;
		      out.cartAccel[0][j] = 0.0;
		      
		      // Determine Cartesian V and Accel at each millisecond using
		      // numerical methods
		      for (int i = 1; i < Npts + 1; i++) {
		          out.cartVel[i][j] = (CartPos[i][j] - CartPos[i-1][j]) / 0.001;
		          out.cartAccel[i][j] = (out.cartVel[i][j] - out.cartVel[i-1][j]) / 0.001;
		      }
		  }
		
		  // Calculate Magnitude of Cartesian Speed & Acceleration
		    for (int i = 1; i < Npts + 1; i++) {
			    out.cartVel[i][3] = Math.sqrt(out.cartVel[i][0]*out.cartVel[i][0] + out.cartVel[i][1]*out.cartVel[i][1] + 
			                        	  out.cartVel[i][2]*out.cartVel[i][2]);
			    out.cartAccel[i][3] = Math.sqrt(out.cartAccel[i][0]*out.cartAccel[i][0] + out.cartAccel[i][1]*out.cartAccel[i][1] + 
		                           			out.cartAccel[i][2]*out.cartAccel[i][2]);
		    }
		    
		    
		  // Calculate J1, J2, J3, and J4 Speed & Acceleration
		  for (int j = 0; j < 4; j++) {
		      
		      // Determine the Start values for V and A
		      out.jointVel[0][j] = 0.0;
		      out.jointAccel[0][j] = 0.0;
		      
		      // Determine Joint V and Accel at each millisecond using
		      // numerical methods
		      for (int i = 1; i < Npts + 1; i++) {
		          out.jointVel[i][j] = (JointPos[i][j] - JointPos[i-1][j]) / 0.001;
		          out.jointAccel[i][j] = (out.jointVel[i][j] - out.jointVel[i-1][j]) / 0.001;
		      }
		  }
		  
		  // Determine Max, Min, and Ave joint speeds and path speeds
		  // Initialize parameters
		  for (int j = 0; j < 4; j++) {
		      out.maxJVel[j] = out.jointVel[0][j];
		      out.minJVel[j] = out.jointVel[0][j];
		      out.averageJVel[j] = 0.0;
		  }
		  out.avePathVel = 0.0;
		  out.maxPathVel = out.cartVel[0][3];
		  out.minPathVel = out.cartVel[0][3];
		  for (int i = 1; i < Npts + 1; i++) {
		      for (int j = 0; j < 3; j++) {
		      
		          // Determine maximum joint velocity
		          if ( out.jointVel[i][j] > out.maxJVel[j] ) {
		              out.maxJVel[j] = out.jointVel[i][j];
		          }
		  
		          // Determine minimum joint velocity
		          if ( out.jointVel[i][j] < out.minJVel[j] ) {
		              out.minJVel[j] = out.jointVel[i][j];
		          }
		          
		      }
		      
		      // Determine maximum path speed
		      if ( out.cartVel[i][3] > out.maxPathVel ) {
		          out.maxPathVel = out.cartVel[i][3];
		      }
		      
		      // Determine minimum path speed
		      if ( out.cartVel[i][3] < out.minPathVel ) {
		          out.minPathVel = out.cartVel[i][3];
		      }
		  }
		  
		  // Calculate Average joint and path speeds
		  for (int i = 0; i < Npts + 1; i++) {
		      
		      // Sum each joint velocity value
		      for (int j = 0; j < 4; j++) {
		          out.averageJVel[j] = out.averageJVel[j] + Math.abs(out.jointVel[i][j]);
		      }
		      
		      // Sum each path velocity value
		      out.avePathVel = out.avePathVel + Math.abs(out.cartVel[i][3]);
		  }
		  
		  // Calculate the average speed
		  for (int j = 0; j < 4; j++) {
		      out.averageJVel[j] = out.averageJVel[j] / Npts;
		  }
		  
		  out.avePathVel = out.avePathVel / Npts;
		  
		          
		  // Convert all joint angles to degrees and all distances to mm.
		  for (int i = 0; i < Npts + 1; i++) {
		      for (int j = 0; j < 4; j++) {
//		          CartPos[i][j] = CartPos[i][j] * 1000.0;           				// mm
//		          out.cartVel[i][j] = out.cartVel[i][j] * 1000.0;           		// mm/s
//		          out.cartAccel[i][j] = out.cartAccel[i][j] * 1000.0;      		 	// mm/s^2
		          JointPos[i][j] = JointPos[i][j] * 180.0 / Math.PI;     			// deg
		          out.jointVel[i][j] = out.jointVel[i][j] * 180.0 / Math.PI;     	// deg/s
		          out.jointAccel[i][j] = out.jointAccel[i][j] * 180.0 / Math.PI; 	// deg/s^2
		      }		      
		  }
		  
		  for (int j = 0; j < 4; j++) {
		      out.averageJVel[j] = out.averageJVel[j] * 180.0 / Math.PI;           	// deg/s
		      out.maxJVel[j] = out.maxJVel[j] * 180.0 / Math.PI;                  	// deg/s
		      out.minJVel[j] = out.minJVel[j] * 180.0 / Math.PI;                  	// deg/s
		  }
		  
//		  out.maxPathVel = out.maxPathVel * 1000.0;                         		// mm/s
//		  out.minPathVel = out.minPathVel * 1000.0;                         		// mm/s
//		  out.avePathVel = out.avePathVel * 1000.0;                         		// mm/s
		  
		  // Save the positions for convenience
		  out.jointPos = JointPos;
		  out.cartPos = CartPos;
		  
		  return out;
	}
}
