package edu.rhhs.frc.utility.motionprofile;

import java.util.ArrayList;

public class WaypointList {

	private ArrayList<double[]> coordinates = new ArrayList<double[]>();
	private MotionProfile.ProfileMode profileMode;
	
	public WaypointList(MotionProfile.ProfileMode profileMode) {
		this.profileMode = profileMode;
	}

	public void addWaypoint(double x, double y, double z, double toolAngleDeg) {
		coordinates.add(new double[] {x, y, z, toolAngleDeg});
	}
	
	public void addWaypoint(double[] xyztool) {
		coordinates.add(xyztool);
	}
	
	public void insertWaypoint(double[] xyztool, int index) {
		ArrayList<double[]> coordinatesTemp = new ArrayList<double[]>();
		
		for (int i = 0; i < coordinates.size(); i++) {
			if (i == index) {
				coordinatesTemp.add(xyztool);
			}
			coordinatesTemp.add(coordinates.get(i));
		}
		coordinates = coordinatesTemp;
	}
	
	public double[][] getWaypoints() {
		double[][] output = new double[coordinates.size()][3];
		for (int i = 0; i < coordinates.size(); i++) {
			output[i] = coordinates.get(i);
		}
		
		return output;
	}
	
	public MotionProfile.ProfileMode getProfileMode() {
		return profileMode;
	}
}
