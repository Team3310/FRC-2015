package edu.rhhs.frc.utility.motionprofile;

import java.util.ArrayList;

public class WaypointList {

	private ArrayList<double[]> coordinates = new ArrayList<double[]>();
	
	public WaypointList() {
		
	}

	public void addWaypoint(double x, double y, double z, double toolAngleDeg) {
		coordinates.add(new double[] {x, y, z, toolAngleDeg});
	}
	
	public double[][] getWaypoints() {
		double[][] output = new double[coordinates.size()][3];
		for (int i = 0; i < coordinates.size(); i++) {
			output[i] = coordinates.get(i);
		}
		
		return output;
	}
	
}
