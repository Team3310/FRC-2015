package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Command;

public class BinGrabberDeployTimed extends Command 
{
	private double speed;
	private int timeoutMs;
	
	public BinGrabberDeployTimed(double speed, int timeoutMs) {
		this.speed = speed;
		this.timeoutMs = timeoutMs;
		requires(RobotMain.binGrabber);
	}
	
	@Override
	protected void initialize() {
		RobotMain.binGrabber.setTalonControlMode(CANTalon.ControlMode.PercentVbus);
		RobotMain.binGrabber.setSpeed(speed, speed);
		this.setTimeout((double) (timeoutMs / 1000.0));
	}

	@Override
	protected void execute() {
		
	}

	@Override
	protected boolean isFinished() {
		return this.isTimedOut();
	}

	@Override
	protected void end() {
		RobotMain.binGrabber.setSpeed(0.0, 0.0);
	}

	@Override
	protected void interrupted() {
		end();
	}
}