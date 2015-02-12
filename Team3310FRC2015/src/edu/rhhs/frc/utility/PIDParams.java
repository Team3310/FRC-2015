package edu.rhhs.frc.utility;

import edu.wpi.first.wpilibj.CANTalon;

public class PIDParams {
    public double kP = 0;
    public double kI = 0;
    public double kD = 0;
    public double kF = 0; 
    public int iZone = 0;
    public double rampRatePID = 0.0;

    public PIDParams() {	
    }
    
    public PIDParams(double kP, double kI, double kD, double kF, int iZone, double rampRatePID) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF; 
        this.iZone = iZone;
        this.rampRatePID = rampRatePID;
    }
    
    public void setTalonPID(CANTalon motorController, int profile) {
    	motorController.setPID(kP, kI, kD, kF, iZone, rampRatePID, profile); 
    }

}
