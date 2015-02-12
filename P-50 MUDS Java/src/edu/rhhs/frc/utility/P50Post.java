package edu.rhhs.frc.utility;

import com.sun.security.auth.module.JndiLoginModule;

public class P50Post {
		
		// NOTE: 4th array spot on Vel & Acc is for magnitude on a linear move
		 
		public class P50PostOutput { 
			public int Npts;

			public double [][] JointPos;
			public double [][] JointVel;
			public double [][] JointAccel;
			public double [][] CartPos;
			public double [][] CartVel;
			public double [][] CartAccel;

			public double [] MinJVel = new double[3];
			public double [] MaxJVel = new double[3];
			public double [] AveJVel = new double[3];

			public double AvePathVel ;
			public double MaxPathVel;
			public double MinPathVel;
			
			public P50PostOutput(int Npts) {
				this.Npts = Npts;
				JointVel = new double[Npts+1][3];
				JointAccel = new double[Npts+1][3];
				CartVel = new double[Npts+1][4];
				CartAccel = new double[Npts+1][4];
			}
		}
		
		public void output(P50PostOutput out, int outPts) {
			System.out.println("Time (sec), J1 (deg), J2 (deg), J3 (deg), J1 Speed (deg/sec), J2 Speed (deg/sec), J3 Speed (deg/sec), X (mm), Y (mm), Z (mm), Path Speed (mm/s) ");
			for (int i = 0; i < out.Npts; i+=outPts) {
				System.out.println(i/1000.0 + "," + 
						out.JointPos[i][0] + "," + out.JointPos[i][1] + "," + out.JointPos[i][2] + "," + 
						out.JointVel[i][0] + "," + out.JointVel[i][1] + "," + out.JointVel[i][2] + "," + 
						out.CartPos[i][0]  + "," + out.CartPos[i][1]  + "," + out.CartPos[i][2]  + "," + out.CartVel[i][3]);
			}
		}
		
		public P50PostOutput VelAccPos(int Npts, double[][] JointPos, double[][] CartPos) {
		
			P50PostOutput out = new P50PostOutput(Npts);
			
			// Calculate X, Y, & Z components of Cartesian Speed & Acceleration
			for (int j = 0; j < 3; j++) {
		  
		      // Determine the Start values for V and A
		      out.CartVel[0][j] = 0.0;
		      out.CartAccel[0][j] = 0.0;
		      
		      // Determine Cartesian V and Accel at each millisecond using
		      // numerical methods
		      for (int i = 1; i < Npts + 1; i++) {
		          out.CartVel[i][j] = (CartPos[i][j] - CartPos[i-1][j]) / 0.001;
		          out.CartAccel[i][j] = (out.CartVel[i][j] - out.CartVel[i-1][j]) / 0.001;
		      }
		  }
		
		  // Calculate Magnitude of Cartesian Speed & Acceleration
		    for (int i = 1; i < Npts + 1; i++) {
			    out.CartVel[i][3] = Math.sqrt(out.CartVel[i][0]*out.CartVel[i][0] + out.CartVel[i][1]*out.CartVel[i][1] + 
			                        	  out.CartVel[i][2]*out.CartVel[i][2]);
			    out.CartAccel[i][3] = Math.sqrt(out.CartAccel[i][0]*out.CartAccel[i][0] + out.CartAccel[i][1]*out.CartAccel[i][1] + 
		                           			out.CartAccel[i][2]*out.CartAccel[i][2]);
		    }
		    
		    
		  // Calculate J1, J2, and J3 Speed & Acceleration
		  for (int j = 0; j < 3; j++) {
		      
		      // Determine the Start values for V and A
		      out.JointVel[0][j] = 0.0;
		      out.JointAccel[0][j] = 0.0;
		      
		      // Determine Joint V and Accel at each millisecond using
		      // numerical methods
		      for (int i = 1; i < Npts + 1; i++) {
		          out.JointVel[i][j] = (JointPos[i][j] - JointPos[i-1][j]) / 0.001;
		          out.JointAccel[i][j] = (out.JointVel[i][j] - out.JointVel[i-1][j]) / 0.001;
		      }
		  }
		  
		  // Determine Max, Min, and Ave joint speeds and path speeds
		  // Initialize parameters
		  for (int j = 0; j < 3; j++) {
		      out.MaxJVel[j] = out.JointVel[0][j];
		      out.MinJVel[j] = out.JointVel[0][j];
		      out.AveJVel[j] = 0.0;
		  }
		  out.AvePathVel = 0.0;
		  out.MaxPathVel = out.CartVel[0][3];
		  out.MinPathVel = out.CartVel[0][3];
		  for (int i = 1; i < Npts + 1; i++) {
		      for (int j = 0; j < 3; j++) {
		      
		          // Determine maximum joint velocity
		          if ( out.JointVel[i][j] > out.MaxJVel[j] ) {
		              out.MaxJVel[j] = out.JointVel[i][j];
		          }
		  
		          // Determine minimum joint velocity
		          if ( out.JointVel[i][j] < out.MinJVel[j] ) {
		              out.MinJVel[j] = out.JointVel[i][j];
		          }
		          
		      }
		      
		      // Determine maximum path speed
		      if ( out.CartVel[i][3] > out.MaxPathVel ) {
		          out.MaxPathVel = out.CartVel[i][3];
		      }
		      
		      // Determine minimum path speed
		      if ( out.CartVel[i][3] < out.MinPathVel ) {
		          out.MinPathVel = out.CartVel[i][3];
		      }
		  }
		  
		  // Calculate Average joint and path speeds
		  for (int i = 0; i < Npts + 1; i++) {
		      
		      // Sum each joint velocity value
		      for (int j = 0; j < 3; j++) {
		          out.AveJVel[j] = out.AveJVel[j] + Math.abs(out.JointVel[i][j]);
		      }
		      
		      // Sum each path velocity value
		      out.AvePathVel = out.AvePathVel + Math.abs(out.CartVel[i][3]);
		  }
		  
		  // Calculate the average speed
		  for (int j = 0; j < 3; j++) {
		      out.AveJVel[j] = out.AveJVel[j] / Npts;
		  }
		  
		  out.AvePathVel = out.AvePathVel / Npts;
		  
		          
		  // Convert all joint angles to degrees and all distances to mm.
		  for (int i = 0; i < Npts + 1; i++) {
		      for (int j = 0; j < 3; j++) {
		          CartPos[i][j] = CartPos[i][j] * 1000.0;           		// mm
		          out.CartVel[i][j] = out.CartVel[i][j] * 1000.0;           		// mm/s
		          out.CartAccel[i][j] = out.CartAccel[i][j] * 1000.0;      		 	// mm/s^2
		          JointPos[i][j] = JointPos[i][j] * 180.0 / Math.PI;     	// deg
		          out.JointVel[i][j] = out.JointVel[i][j] * 180.0 / Math.PI;     	// deg/s
		          out.JointAccel[i][j] = out.JointAccel[i][j] * 180.0 / Math.PI; 	// deg/s^2
		      }
		      
		      out.CartVel[i][3] = out.CartVel[i][3] * 1000.0;               		// mm/s
		      out.CartAccel[i][3] = out.CartAccel[i][3] * 1000.0;          			// mm/s^2
		      
		  }
		  
		  for (int j = 0; j < 3; j++) {
		      out.AveJVel[j] = out.AveJVel[j] * 180.0 / Math.PI;           			// deg/s
		      out.MaxJVel[j] = out.MaxJVel[j] * 180.0 / Math.PI;                  	// deg/s
		      out.MinJVel[j] = out.MinJVel[j] * 180.0 / Math.PI;                  	// deg/s
		  }
		  
		  out.MaxPathVel = out.MaxPathVel * 1000.0;                         		// mm/s
		  out.MinPathVel = out.MinPathVel * 1000.0;                         		// mm/s
		  out.AvePathVel = out.AvePathVel * 1000.0;                         		// mm/s
		  
		  // Save the positions for convenience
		  out.JointPos = JointPos;
		  out.CartPos = CartPos;
		  
		  return out;
	}
}
