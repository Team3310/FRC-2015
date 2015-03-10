package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class DriveWithJoystick extends Command 
{
	public DriveWithJoystick() {
		requires(RobotMain.driveTrain);
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		RobotMain.driveTrain.driveWithJoystick();
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