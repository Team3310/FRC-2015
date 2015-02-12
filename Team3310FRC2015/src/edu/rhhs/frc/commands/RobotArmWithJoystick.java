package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class RobotArmWithJoystick extends Command 
{
	public RobotArmWithJoystick() {
		requires(RobotMain.robotArm);
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		RobotMain.robotArm.controlWithJoystick();
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
