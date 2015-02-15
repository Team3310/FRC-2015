package edu.rhhs.frc.utility;

import edu.wpi.first.wpilibj.CANTalon;

public class CANTalonAnalogPID extends CANTalonEncoderPID {
	
	private int zeroPositionRaw;
	
	public CANTalonAnalogPID(int deviceNumber, double sensorToOutputGearRatio, double offsetAngleDeg, double initAngleDeg, double minAngleDeg, double maxAngleDeg, int zeroPositionRaw) {
		super(deviceNumber, sensorToOutputGearRatio, offsetAngleDeg, initAngleDeg, minAngleDeg, maxAngleDeg);
		this.zeroPositionRaw = zeroPositionRaw;
	}

	public CANTalonAnalogPID(int deviceNumber, int controlPeriodMs, double sensorToOutputGearRatio, double offsetAngleDeg, double initAngleDeg, double minAngleDeg, double maxAngleDeg, int zeroPositionRaw) {
		super(deviceNumber, controlPeriodMs, sensorToOutputGearRatio, offsetAngleDeg, initAngleDeg, minAngleDeg, maxAngleDeg);
		this.zeroPositionRaw = zeroPositionRaw;
	}
	
	@Override
	public void inititializeSensorPosition() {
	}
	
	@Override
	public double getPositionDeg() {
		return RobotUtility.convertAnalogPositionToDeg(this.getAnalogInRaw(), zeroPositionRaw, sensorToOutputGearRatio) - offsetAngleDeg;
	}
	
	@Override
	public double getVelocityDegPerSec() {
		return RobotUtility.convertAnalogVelocityToDegPerSec(this.getSpeed(), sensorToOutputGearRatio);
	}
	
	@Override
	public void setPIDPositionDeg(double positionCommandDeg) {
		if (positionCommandDeg > maxAngleDeg) {
			positionCommandDeg = maxAngleDeg;
		}
		if (positionCommandDeg < minAngleDeg) {
			positionCommandDeg = minAngleDeg;
		}
		
		this.set(RobotUtility.convertDegToAnalogPosition(positionCommandDeg + offsetAngleDeg, zeroPositionRaw, sensorToOutputGearRatio));
	}

	@Override
	public void setPIDVelocityDegPerSec(double velocityCommandDegPerSec) {
		if ((getPositionDeg() > maxAngleDeg && velocityCommandDegPerSec > 0) ||
			(getPositionDeg() < minAngleDeg && velocityCommandDegPerSec < 0)) {
			if (this.getControlMode() == CANTalon.ControlMode.Speed) {
				setControlMode(RobotUtility.ControlMode.POSITION);
			}
			setPIDPositionDeg(getPositionDeg());
			return;
		}
		
		this.set(RobotUtility.convertDegPerSecToAnalogVelocity(velocityCommandDegPerSec, sensorToOutputGearRatio));
	}
}
