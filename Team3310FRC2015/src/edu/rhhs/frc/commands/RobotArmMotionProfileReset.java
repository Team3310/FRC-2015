package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class RobotArmMotionProfileReset extends Command 
{
	public RobotArmMotionProfileReset() {
		requires(RobotMain.robotArm);
	}
	
	@Override
	protected void initialize() {
		RobotMain.robotArm.disableControlLoop();
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