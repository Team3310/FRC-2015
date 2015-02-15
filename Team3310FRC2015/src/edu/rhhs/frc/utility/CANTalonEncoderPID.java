package edu.rhhs.frc.utility;

import edu.wpi.first.wpilibj.CANTalon;

public class CANTalonEncoderPID extends CANTalon {
	
	protected double sensorToOutputGearRatio;
	protected double offsetAngleDeg;
	protected double initAngleDeg;
	protected double minAngleDeg;
	protected double maxAngleDeg;
	
	public CANTalonEncoderPID(int deviceNumber, double sensorToOutputGearRatio, double offsetAngleDeg, double initAngleDeg, double minAngleDeg, double maxAngleDeg) {
		super(deviceNumber);
		this.sensorToOutputGearRatio = sensorToOutputGearRatio;
		this.offsetAngleDeg = offsetAngleDeg;
		this.initAngleDeg = initAngleDeg;
		this.minAngleDeg = minAngleDeg;
		this.maxAngleDeg = maxAngleDeg;
	}

	public CANTalonEncoderPID(int deviceNumber, int controlPeriodMs, double sensorToOutputGearRatio, double offsetAngleDeg, double initAngleDeg, double minAngleDeg, double maxAngleDeg) {
		super(deviceNumber, controlPeriodMs);
		this.sensorToOutputGearRatio = sensorToOutputGearRatio;
		this.offsetAngleDeg = offsetAngleDeg;
		this.initAngleDeg = initAngleDeg;
		this.minAngleDeg = minAngleDeg;
		this.maxAngleDeg = maxAngleDeg;
	}
	
	public void setControlMode(RobotUtility.ControlMode mode) {
		CANTalon.ControlMode talonMode = CANTalon.ControlMode.PercentVbus;
		if (mode == RobotUtility.ControlMode.POSITION) {
			this.setProfile(RobotUtility.POSITION_PROFILE);
			talonMode = CANTalon.ControlMode.Position;
		}
		else if (mode == RobotUtility.ControlMode.VELOCITY) {
			this.setProfile(RobotUtility.VELOCITY_PROFILE);
			talonMode = CANTalon.ControlMode.Speed;
		}
		else if (mode == RobotUtility.ControlMode.VBUS) {
			talonMode = CANTalon.ControlMode.PercentVbus;
		}
		
		this.changeControlMode(talonMode);
	}

	public void setPIDParams(PIDParams params, int profile) {
		this.setPID(params.kP, params.kI, params.kD, params.kF, params.iZone, params.rampRatePID, profile); 
	}
	
	public void inititializeSensorPosition() {
		this.setPosition(RobotUtility.convertDegToEncoderPosition(initAngleDeg, sensorToOutputGearRatio));
	}
	
	public double getPositionDeg() {
		return RobotUtility.convertEncoderPositionToDeg(this.getPosition(), sensorToOutputGearRatio) - offsetAngleDeg;
	}
	
	public double getVelocityDegPerSec() {
		return RobotUtility.convertEncoderVelocityToDegPerSec(this.getSpeed(), sensorToOutputGearRatio);
	}
	
	public void setPIDPositionDeg(double positionCommandDeg) {
		if (positionCommandDeg > maxAngleDeg) {
			positionCommandDeg = maxAngleDeg;
		}
		if (positionCommandDeg < minAngleDeg) {
			positionCommandDeg = minAngleDeg;
		}
		
		this.set(RobotUtility.convertDegToEncoderPosition(positionCommandDeg + offsetAngleDeg, sensorToOutputGearRatio));
	}

	public void setPIDVelocityDegPerSec(double velocityCommandDegPerSec) {
		if ((getPositionDeg() > maxAngleDeg && velocityCommandDegPerSec > 0) ||
			(getPositionDeg() < minAngleDeg && velocityCommandDegPerSec < 0)) {
			if (this.getControlMode() == CANTalon.ControlMode.Speed) {
				setControlMode(RobotUtility.ControlMode.POSITION);
			}
			setPIDPositionDeg(getPositionDeg());
			return;
		}
		
		this.set(RobotUtility.convertDegPerSecToEncoderVelocity(velocityCommandDegPerSec, sensorToOutputGearRatio));
	}
	
	public void setStickInputVelocityDegPerSec(double velocityCommandDegPerSec) {
		if (Math.abs(velocityCommandDegPerSec) < 5) {
			if (this.getControlMode() == CANTalon.ControlMode.Speed) {
				setControlMode(RobotUtility.ControlMode.POSITION);
			}
			this.setPIDPositionDeg(getPositionDeg());
		}
		else {
			if (this.getControlMode() == CANTalon.ControlMode.Position) {
				setControlMode(RobotUtility.ControlMode.VELOCITY);
			}
			this.setPIDVelocityDegPerSec(velocityCommandDegPerSec);
		}
	}
	
	public void setInitPosition() {
		setControlMode(RobotUtility.ControlMode.POSITION);
		setPIDPositionDeg(initAngleDeg);
	}
}
