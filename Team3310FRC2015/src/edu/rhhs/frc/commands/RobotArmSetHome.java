package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class RobotArmSetHome extends Command 
{
	public RobotArmSetHome() {
		requires(RobotMain.robotArm);
	}
	
	@Override
	protected void initialize() {
		RobotMain.commandListGenerator.setHome(RobotMain.robotArm.getJointAngles());
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
