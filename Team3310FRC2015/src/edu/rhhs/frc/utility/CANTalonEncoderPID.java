package edu.rhhs.frc.utility;

import edu.wpi.first.wpilibj.CANTalon;

public class CANTalonEncoderPID extends CANTalon {
	
	protected double sensorToOutputGearRatio = 1.0;
	protected double offsetAngleDeg = 0.0;
	protected double initAngleDeg = 0.0;
	protected double minAngleDeg;
	protected double maxAngleDeg;
	protected RobotUtility.ControlMode controlMode;
	
	public CANTalonEncoderPID(int deviceNumber) {
		super(deviceNumber);
	}

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
		else if (mode == RobotUtility.ControlMode.VELOCITY_POSITION_HOLD) {
			this.setProfile(RobotUtility.VELOCITY_PROFILE);
			talonMode = CANTalon.ControlMode.Speed;
		}
		else if (mode == RobotUtility.ControlMode.VELOCITY) {
			this.setProfile(RobotUtility.VELOCITY_PROFILE);
			talonMode = CANTalon.ControlMode.Speed;
		}
		else if (mode == RobotUtility.ControlMode.PERCENT_VBUS || mode == RobotUtility.ControlMode.VBUS_POSITION_HOLD) {
			talonMode = CANTalon.ControlMode.PercentVbus;
		}
		controlMode = mode;
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
	
	public double getPositionInches() {
		return RobotUtility.convertEncoderPositionToInches(this.getPosition(), sensorToOutputGearRatio);
	}
	
	public double getVelocityDegPerSec() {
		return RobotUtility.convertEncoderVelocityToDegPerSec(this.getSpeed(), sensorToOutputGearRatio);
	}
	
	public double getVelocityFtPerSec() {
		return RobotUtility.convertEncoderVelocityToFtPerSec(this.getSpeed(), sensorToOutputGearRatio);
	}
	
	public void setPIDPositionInches(double positionCommandInches) {
		this.set(RobotUtility.convertInchesToEncoderPosition(positionCommandInches));
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
			if (controlMode != RobotUtility.ControlMode.POSITION) {
				setControlMode(RobotUtility.ControlMode.POSITION);
			}
			setPIDPositionDeg(getPositionDeg());
			return;
		}
		
		this.set(RobotUtility.convertDegPerSecToEncoderVelocity(velocityCommandDegPerSec, sensorToOutputGearRatio));
	}
	
	public void setPIDVelocityDegPerSecNoLimits(double velocityCommandDegPerSec) {		
		this.set(RobotUtility.convertDegPerSecToEncoderVelocity(velocityCommandDegPerSec, sensorToOutputGearRatio));
	}
	
	public void setStickInputVelocityDegPerSec(double velocityCommandDegPerSec) {
		if (Math.abs(velocityCommandDegPerSec) < 5) {
			if (controlMode != RobotUtility.ControlMode.POSITION) {
				setControlMode(RobotUtility.ControlMode.POSITION);
			}
			this.setPIDPositionDeg(getPositionDeg());
		}
		else {
			if (controlMode != RobotUtility.ControlMode.VELOCITY_POSITION_HOLD) {
				setControlMode(RobotUtility.ControlMode.VELOCITY_POSITION_HOLD);
			}
			this.setPIDVelocityDegPerSec(velocityCommandDegPerSec);
		}
	}
	
	public void setStickInputVBus(double vBusCommand) {
		if (Math.abs(vBusCommand) < 0.2) {
			if (controlMode != RobotUtility.ControlMode.POSITION) {
				setControlMode(RobotUtility.ControlMode.POSITION);
			}
			this.setPIDPositionDeg(getPositionDeg());
		}
		else {
			if (controlMode != RobotUtility.ControlMode.VBUS_POSITION_HOLD) {
				setControlMode(RobotUtility.ControlMode.VBUS_POSITION_HOLD);
			}
			this.set(vBusCommand);
		}
	}
	
	public void setInitPosition() {
		setControlMode(RobotUtility.ControlMode.POSITION);
		setPIDPositionDeg(initAngleDeg);
	}
}
