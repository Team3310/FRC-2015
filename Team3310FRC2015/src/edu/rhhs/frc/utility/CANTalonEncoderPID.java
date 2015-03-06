package edu.rhhs.frc.utility;

import edu.wpi.first.wpilibj.CANTalon;

public class CANTalonEncoderPID extends CANTalon {
	
	public static enum ControlMode {PERCENT_VBUS, VBUS_POSITION_HOLD, POSITION, POSITION_INCREMENTAL, VELOCITY, VELOCITY_POSITION_HOLD};

	public static final int POSITION_PROFILE = 0;
	public static final int VELOCITY_PROFILE = 1;
	public static final int POSITION_PROFILE_DOWN = 0;
	public static final int POSITION_PROFILE_UP = 1;

	protected double sensorToOutputGearRatio = 1.0;
	protected double offsetAngleDeg = 0.0;
	protected double initAngleDeg = 0.0;
	protected double minAngleDeg;
	protected double maxAngleDeg;
	protected ControlMode controlMode;
	
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
	
	public void setControlMode(ControlMode mode) {
		CANTalon.ControlMode talonMode = CANTalon.ControlMode.PercentVbus;
		if (mode == ControlMode.POSITION || mode == ControlMode.POSITION_INCREMENTAL) {
			this.setProfile(POSITION_PROFILE);
			talonMode = CANTalon.ControlMode.Position;
		}
		else if (mode == ControlMode.VELOCITY || mode == ControlMode.VELOCITY_POSITION_HOLD) {
			this.setProfile(VELOCITY_PROFILE);
			talonMode = CANTalon.ControlMode.Speed;
		}
		else if (mode == ControlMode.PERCENT_VBUS || mode == ControlMode.VBUS_POSITION_HOLD) {
			talonMode = CANTalon.ControlMode.PercentVbus;
		}
		controlMode = mode;
		this.changeControlMode(talonMode);
	}

	public void setControlMode(ControlMode mode, int profile) {
		CANTalon.ControlMode talonMode = CANTalon.ControlMode.PercentVbus;
		if (mode == ControlMode.POSITION || mode == ControlMode.POSITION_INCREMENTAL) {
			talonMode = CANTalon.ControlMode.Position;
		}
		else if (mode == ControlMode.VELOCITY || mode == ControlMode.VELOCITY_POSITION_HOLD) {
			talonMode = CANTalon.ControlMode.Speed;
		}
		else if (mode == ControlMode.PERCENT_VBUS || mode == ControlMode.VBUS_POSITION_HOLD) {
			talonMode = CANTalon.ControlMode.PercentVbus;
		}
		controlMode = mode;
		this.setProfile(profile);
		this.changeControlMode(talonMode);
	}

	public void setPIDParams(PIDParams params, int profile) {
		this.setPID(params.kP, params.kI, params.kD, params.kF, params.iZone, params.rampRatePID, profile); 
	}
	
	public void inititializeSensorPosition() {
		this.setPosition(RobotUtility.convertDegToEncoderPosition(initAngleDeg + offsetAngleDeg, sensorToOutputGearRatio));
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
	
	public double setPIDPositionInches(double positionCommandInches) {
		double talonInput = RobotUtility.convertInchesToEncoderPosition(positionCommandInches);
		this.set(talonInput);
		
		return talonInput;
	}

	public double setPIDPositiveNegativePositionDeg(double positionCommandDeg, PIDParams positiveParams, PIDParams negativeParams) {
		if (positionCommandDeg > getPositionDeg()) {
			setPIDParams(positiveParams, POSITION_PROFILE);
		}
		else {
			setPIDParams(negativeParams, POSITION_PROFILE);
		}
		
		return setPIDPositionDeg(positionCommandDeg);
	}

	public double setPIDPositionDeg(double positionCommandDeg, int profile) {
		this.setProfile(profile);
		if (positionCommandDeg > maxAngleDeg) {
			positionCommandDeg = maxAngleDeg;
		}
		if (positionCommandDeg < minAngleDeg) {
			positionCommandDeg = minAngleDeg;
		}
		
		double talonInput = RobotUtility.convertDegToEncoderPosition(positionCommandDeg + offsetAngleDeg, sensorToOutputGearRatio);
		this.set(talonInput);
		
		return talonInput;
	}

	public double setPIDPositionDeg(double positionCommandDeg) {
		if (positionCommandDeg > maxAngleDeg) {
			positionCommandDeg = maxAngleDeg;
		}
		if (positionCommandDeg < minAngleDeg) {
			positionCommandDeg = minAngleDeg;
		}
		
		double talonInput = RobotUtility.convertDegToEncoderPosition(positionCommandDeg + offsetAngleDeg, sensorToOutputGearRatio);
		this.set(talonInput);
		
		return talonInput;
	}

	public double setPIDPositionDegNoLimits(double positionCommandDeg) {
		double talonInput = RobotUtility.convertDegToEncoderPosition(positionCommandDeg + offsetAngleDeg, sensorToOutputGearRatio);
		this.set(talonInput);
		
		return talonInput;
	}

	public void setPIDVelocityDegPerSec(double velocityCommandDegPerSec) {
		if ((getPositionDeg() > maxAngleDeg && velocityCommandDegPerSec > 0) ||
			(getPositionDeg() < minAngleDeg && velocityCommandDegPerSec < 0)) {
			if (controlMode != ControlMode.POSITION) {
				setControlMode(ControlMode.POSITION);
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
			if (controlMode != ControlMode.POSITION) {
				setControlMode(ControlMode.POSITION);
			}
			this.setPIDPositionDeg(getPositionDeg());
		}
		else {
			if (controlMode != ControlMode.VELOCITY_POSITION_HOLD) {
				setControlMode(ControlMode.VELOCITY_POSITION_HOLD);
			}
			this.setPIDVelocityDegPerSec(velocityCommandDegPerSec);
		}
	}
	
	public void setStickInputVBus(double vBusCommand) {
		if (Math.abs(vBusCommand) < 0.2) {
			if (controlMode != ControlMode.POSITION) {
				setControlMode(ControlMode.POSITION);
			}
			this.setPIDPositionDeg(getPositionDeg());
		}
		else {
			if (controlMode != ControlMode.VBUS_POSITION_HOLD) {
				setControlMode(ControlMode.VBUS_POSITION_HOLD);
			}
			this.set(vBusCommand);
		}
	}
	
	public double setInitPosition() {
		setPIDPositionDeg(initAngleDeg);
		return initAngleDeg;
	}
}
