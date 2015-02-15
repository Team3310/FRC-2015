package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.BinGrabber;
import edu.wpi.first.wpilibj.command.Command;

public class BinGrabberPivotLockPosition extends Command 
{
	private BinGrabber.BinGrabberState m_position;
	
	public BinGrabberPivotLockPosition(BinGrabber.BinGrabberState position) {
		m_position = position;
		requires(RobotMain.binGrabber);
	}

	@Override
	protected void initialize() {
		RobotMain.binGrabber.setPivotLockPosition(m_position);
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