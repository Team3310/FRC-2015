package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class RobotArmMotionProfileNext extends Command 
{
	public RobotArmMotionProfileNext() {
		requires(RobotMain.robotArm);
	}
	
	@Override
	protected void initialize() {
		RobotMain.robotArm.setWaitForNext(false);
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