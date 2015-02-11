package edu.rhhs.frc.utility;

public class P50Post {
		
		// NOTE: 4th array spot on Vel & Acc is for magnitude on a linear move
		  
		public int Npts;
		public double AvePathVel ;
		public double MaxPathVel;
		public double MinPathVel;
		public double[][] JointAccel;
		public double [][] JointVel;
		public double [][] CartVel;
		public double [][] CartAccel;
		public double [] MaxJVel = new double[3];
		public double [] MinJVel = new double[3];
		public double [] AveJVel = new double[3];
		  
		public P50Post(int Npts) {
			this.Npts = Npts;
			JointAccel = new double[Npts+1][3];
			JointVel = new double[Npts+1][3];
			CartVel = new double[Npts+1][4];
			CartAccel = new double[Npts+1][4];
		}

		public void VelAccPos(double[][] JointPos, double[][] CartPos) {
		  // Calculate X, Y, & Z components of Cartesian Speed & Acceleration
		  for (int j = 0; j < 3; j++) {
		  
		      // Determine the Start values for V and A
		      CartVel[0][j] = 0.0;
		      CartAccel[0][j] = 0.0;
		      
		      // Determine Cartesian V and Accel at each millisecond using
		      // numerical methods
		      for (int i = 0; i < Npts; i++) {
		          CartVel[i][j] = (CartPos[i][j] - CartPos[i-1][j]) / 0.001;
		          CartAccel[i][j] = (CartVel[i][j] - CartVel[i-1][j]) / 0.001;
		      }
		  }
		
		  // Calculate Magnitude of Cartesian Speed & Acceleration
		    for (int i = 0; i < Npts; i++) {
			    CartVel[i][4] = Math.sqrt(CartVel[i][1]*CartVel[i][1] + CartVel[i][2]*CartVel[i][2] + 
			                        	  CartVel[i][3]*CartVel[i][3]);
			    CartAccel[i][4] = Math.sqrt(CartAccel[i][1]*CartAccel[i][1] + CartAccel[i][2]*CartAccel[i][2] + 
		                           			CartAccel[i][3]*CartAccel[i][3]);
		    }
		    
		    
		  // Calculate J1, J2, and J3 Speed & Acceleration
		  for (int j = 0; j < 3; j++) {
		      
		      // Determine the Start values for V and A
		      JointVel[0][j] = 0.0;
		      JointAccel[0][j] = 0.0;
		      
		      // Determine Joint V and Accel at each millisecond using
		      // numerical methods
		      for (int i = 0; i < Npts; i++) {
		          JointVel[i][j] = (JointPos[i][j] - JointPos[i-1][j]) / 0.001;
		          JointAccel[i][j] = (JointVel[i][j] - JointVel[i-1][j]) / 0.001;
		      }
		  }
		  
		  // Determine Max, Min, and Ave joint speeds and path speeds
		  // Initialize parameters
		  for (int j = 0; j < 3; j++) {
		      MaxJVel[j] = JointVel[0][j];
		      MinJVel[j] = JointVel[0][j];
		      AveJVel[j] = 0.0;
		  }
		  AvePathVel = 0.0;
		  MaxPathVel = CartVel[0][4];
		  MinPathVel = CartVel[0][4];
		  for (int i = 0; i < Npts; i++) {
		      for (int j = 0; j < 3; j++) {
		      
		          // Determine maximum joint velocity
		          if ( JointVel[i][j] > MaxJVel[j] ) {
		              MaxJVel[j] = JointVel[i][j];
		          }
		  
		          // Determine minimum joint velocity
		          if ( JointVel[i][j] < MinJVel[j] ) {
		              MinJVel[j] = JointVel[i][j];
		          }
		          
		      }
		      
		      // Determine maximum path speed
		      if ( CartVel[i][4] > MaxPathVel ) {
		          MaxPathVel = CartVel[i][4];
		      }
		      
		      // Determine minimum path speed
		      if ( CartVel[i][4] < MinPathVel ) {
		          MinPathVel = CartVel[i][4];
		      }
		  }
		  
		  // Calculate Average joint and path speeds
		  for (int i = 0; i < Npts + 1; i++) {
		      
		      // Sum each joint velocity value
		      for (int j = 0; j < 3; j++) {
		          AveJVel[j] = AveJVel[j] + Math.abs(JointVel[i][j]);
		      }
		      
		      // Sum each path velocity value
		      AvePathVel = AvePathVel + Math.abs(CartVel[i][4]);
		  }
		  
		  // Calculate the average speed
		  for (int j = 0; j < 3; j++) {
		      AveJVel[j] = AveJVel[j] / Npts;
		  }
		  
		  AvePathVel = AvePathVel / Npts;
		  
		          
		  // Convert all joint angles to degrees and all distances to mm.
		  for (int i = 0; i < Npts + 1; i++) {
		      for (int j = 0; j < 3; j++) {
		          CartPos[i][j] = CartPos[i][j] * 1000.0;           		// mm
		          CartVel[i][j] = CartVel[i][j] * 1000.0;           		// mm/s
		          CartAccel[i][j] = CartAccel[i][j] * 1000.0;      		 	// mm/s^2
		          JointPos[i][j] = JointPos[i][j] * 180.0 / Math.PI;     	// deg
		          JointVel[i][j] = JointVel[i][j] * 180.0 / Math.PI;     	// deg/s
		          JointAccel[i][j] = JointAccel[i][j] * 180.0 / Math.PI; 	// deg/s^2
		      }
		      
		      CartVel[i][4] = CartVel[i][4] * 1000.0;               		// mm/s
		      CartAccel[i][4] = CartAccel[i][4] * 1000.0;          			// mm/s^2
		      
		  }
		  
		  for (int j = 0; j < 3; j++) {
		      AveJVel[j] = AveJVel[j] * 180.0 / Math.PI;           			// deg/s
		      MaxJVel[j] = MaxJVel[j] * 180.0 / Math.PI;                  	// deg/s
		      MinJVel[j] = MinJVel[j] * 180.0 / Math.PI;                  	// deg/s
		  }
		  
		  MaxPathVel = MaxPathVel * 1000.0;                         		// mm/s
		  MinPathVel = MinPathVel * 1000.0;                         		// mm/s
		  AvePathVel = AvePathVel * 1000.0;                         		// mm/s
	}
}
