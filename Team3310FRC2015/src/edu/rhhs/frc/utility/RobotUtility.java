package edu.rhhs.frc.utility;

import edu.rhhs.frc.RobotMap;

public class RobotUtility 
{
	// MA3 Analog Encoder raw output value goes from 0 to 1024.
	public static double convertAnalogPositionToDeg(double analogValue, double zeroValue) {
		return (360.0 / 1024.0) * (analogValue - zeroValue);
	}
	
	public static double convertDegToAnalogPosition(double degValue, double zeroValue) {
		return (degValue * (1024.0 / 360.0)) + zeroValue;
	}

	public static double convertAnalogVelocityToDegPerSec(double analogValue) {
		return (360.0 / 1024.0) * analogValue * 10;
	}

	public static double convertDegPerSecToAnalogVelocity(double degPerSecValue) {
		return degPerSecValue * (1024.0 / 360.0) / 10.0;
	}

	// With gear ratio
	public static double convertAnalogPositionToDeg(double analogValue, double zeroValue, double gearRatio) {
		return convertAnalogPositionToDeg(analogValue, zeroValue) / gearRatio;
	}
	
	public static double convertDegToAnalogPosition(double degValue, double zeroValue, double gearRatio) {
		return convertDegToAnalogPosition(degValue * gearRatio, zeroValue) ;
	}

	public static double convertAnalogVelocityToDegPerSec(double analogValue, double gearRatio) {
		return convertAnalogVelocityToDegPerSec(analogValue) / gearRatio;
	}

	public static double convertDegPerSecToAnalogVelocity(double degPerSecValue, double gearRatio) {
		return convertDegPerSecToAnalogVelocity(degPerSecValue * gearRatio);
	}

	public static double convertEncoderPositionToDeg(double encoderValue) {
		return 360.0 * encoderValue / (4 * RobotMap.GRAYHILL_ENCODER_COUNT);
	}

	public static double convertDegToEncoderPosition(double degValue) {
		return (degValue * 4 * RobotMap.GRAYHILL_ENCODER_COUNT) / 360.0;
	}

	public static double convertEncoderVelocityToDegPerSec(double encoderValue) {
		return convertEncoderPositionToDeg(encoderValue) * 10;
	}

	public static double convertEncoderVelocityToFtPerSec(double encoderValue) {
		return convertEncoderVelocityToDegPerSec(encoderValue) * RobotMap.WHEEL_DIAMETER_IN * Math.PI / 12.0 / 360.0;
	}

	public static double convertDegPerSecToEncoderVelocity(double degPerSecValue) {
		return convertDegToEncoderPosition(degPerSecValue) / 10.0;
	}
	
	public static double convertEncoderPositionToDeg(double encoderValue, double gearRatio) {
		return convertEncoderPositionToDeg(encoderValue) / gearRatio;
	}

	public static double convertDegToEncoderPosition(double degValue, double gearRatio) {
		return convertDegToEncoderPosition(degValue * gearRatio);
	}

	public static double convertEncoderVelocityToDegPerSec(double encoderValue, double gearRatio) {
		return convertEncoderVelocityToDegPerSec(encoderValue) / gearRatio;
	}

	public static double convertDegPerSecToEncoderVelocity(double degPerSecValue, double gearRatio) {
		return convertDegPerSecToEncoderVelocity(degPerSecValue * gearRatio);
	}
	
}