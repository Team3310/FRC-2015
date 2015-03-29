package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class RobotArmSetXLock extends Command 
{
	private boolean m_position;
	
	public RobotArmSetXLock(boolean position) {
		m_position = position;
		requires(RobotMain.robotArm);
	}
	
	@Override
	protected void initialize() {
		RobotMain.robotArm.setXLock(m_position);
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