package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.commands.robotarm.RobotArmCommandList;
import edu.wpi.first.wpilibj.command.Command;

public class RobotArmMotionProfileNext extends Command 
{
	public RobotArmMotionProfileNext(RobotArmCommandList commandList) {
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
