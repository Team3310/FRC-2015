package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.CANTalon.StatusFrameRate;
import edu.wpi.first.wpilibj.command.Command;

public class BinGrabberDeployAngle extends Command 
{
	private double speed;
	private double positionDeg;
	private double timeoutMs;
	
	public BinGrabberDeployAngle(double speed, double positionDeg, double timeoutMs) {
		this.speed = speed;
		this.positionDeg = positionDeg;
		this.timeoutMs = timeoutMs;
		requires(RobotMain.binGrabber);
	}
	
	@Override
	protected void initialize() {
		RobotMain.binGrabber.setStatusFrameRate(StatusFrameRate.AnalogTempVbat, 10);
		RobotMain.binGrabber.setSpeed(speed, speed);
		this.setTimeout(timeoutMs/1000.0);
	}

	@Override
	protected void execute() {
		if (RobotMain.binGrabber.getLeftPositionDeg() > positionDeg) {
			RobotMain.binGrabber.setLeftSpeed(0.0);
		}
		if (RobotMain.binGrabber.getRightPositionDeg() > positionDeg) {
			RobotMain.binGrabber.setRightSpeed(0.0);
		}
	}

	@Override
	protected boolean isFinished() {
		return this.isTimedOut() || 
				(RobotMain.binGrabber.getLeftPositionDeg() > positionDeg && 
				RobotMain.binGrabber.getRightPositionDeg() > positionDeg);
	}

	@Override
	protected void end() {
		RobotMain.binGrabber.setSpeed(0.0, 0.0);
		RobotMain.binGrabber.setStatusFrameRate(StatusFrameRate.AnalogTempVbat, 100);
	}

	@Override
	protected void interrupted() {
		end();
	}
}
