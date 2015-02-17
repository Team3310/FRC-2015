package edu.rhhs.frc.utility.motionprofile;

public class ProfileOutput { 
	public int numPoints;

	public double [][] jointPos;
	public double [][] jointVel;
	public double [][] jointAccel;
	public double [][] cartPos;
	public double [][] cartVel;
	public double [][] cartAccel;

	public double [] minJVel = new double[4];
	public double [] maxJVel = new double[4];
	public double [] averageJVel = new double[4];

	public double avePathVel ;
	public double maxPathVel;
	public double minPathVel;
	
	public ProfileOutput(int Npts) {
		this.numPoints = Npts;
		jointVel = new double[Npts+1][4];
		jointAccel = new double[Npts+1][4];
		cartVel = new double[Npts+1][4];
		cartAccel = new double[Npts+1][4];
	}
	
	public void output(int outPts) {
		System.out.println("Time (sec), J1 (deg), J2 (deg), J3 (deg), J4 (deg), J1 Speed (deg/sec), J2 Speed (deg/sec), J3 Speed (deg/sec), J4 Speed (deg/sec), X (mm), Y (mm), Z (mm), Path Speed (mm/s) ");
		for (int i = 0; i < numPoints; i+=outPts) {
			System.out.println(i/1000.0 + "," + 
					jointPos[i][0] + "," + jointPos[i][1] + "," + jointPos[i][2]  + "," + jointPos[i][3] + "," + 
					jointVel[i][0] + "," + jointVel[i][1] + "," + jointVel[i][2]  + "," + jointVel[i][3] + "," + 
					cartPos[i][0]  + "," + cartPos[i][1]  + "," + cartPos[i][2]   + "," + cartVel[i][3]);
		}
	}		
}
