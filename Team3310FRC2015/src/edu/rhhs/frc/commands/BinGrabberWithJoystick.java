package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class BinGrabberWithJoystick extends Command 
{
	public BinGrabberWithJoystick() {
		requires(RobotMain.binGrabber);
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		RobotMain.binGrabber.controlWithJoystick();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		
	}

	@Override
	protected void interrupted() {
		
	}
}