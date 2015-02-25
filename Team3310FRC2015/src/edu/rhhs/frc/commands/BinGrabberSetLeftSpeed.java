package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class BinGrabberSetLeftSpeed extends Command
{
	private double speed;
	
	public BinGrabberSetLeftSpeed(double speed) {
		this.speed = speed;
		requires(RobotMain.binGrabber);
	}
	
	@Override
	protected void initialize() {
		RobotMain.binGrabber.setLeftSpeed(speed);
	}

	@Override
	protected void execute() {
		
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
		
	}

	@Override
	protected void interrupted() {
		
	}

}
