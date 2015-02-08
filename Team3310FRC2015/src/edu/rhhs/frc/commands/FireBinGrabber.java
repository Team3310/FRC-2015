package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class FireBinGrabber extends Command 
{
	private double speed;
	private int milliseconds;
	
	public FireBinGrabber(double speed, int milliseconds) {
		this.speed = speed;
		this.milliseconds = milliseconds;
		requires(RobotMain.binGrabber);
	}
	
	@Override
	protected void initialize() {
		this.setTimeout((double) (milliseconds / 1000));
		RobotMain.binGrabber.setSpeed(speed);
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
		RobotMain.binGrabber.setSpeed(0.0);
	}

	@Override
	protected void interrupted() {
		end();
	}
}
