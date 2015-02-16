package edu.rhhs.frc.utility.motionprofile;

import java.util.ArrayList;

public class WaypointList {

	private ArrayList<double[]> coordinates = new ArrayList<double[]>();
	
	public WaypointList() {
		
	}

	public WaypointList(ArrayList<double[]> pointCoordinates) {
		this.coordinates = pointCoordinates;
	}
	
	public void addWaypoint(double[] coordinate) {
		coordinates.add(coordinate);
	}
	
	public void addWaypoint(double x, double y, double z) {
		coordinates.add(new double[] {x, y, z});
	}
	
	public double[][] getCoordinates() {
		double[][] output = new double[coordinates.size()][3];
		for (int i = 0; i < coordinates.size(); i++) {
			output[i] = coordinates.get(i);
		}
		
		return output;
	}
}
