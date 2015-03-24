package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainToteSledPosition extends Command 
{
	private DriveTrain.ToteSledPosition m_position;
	
	public DriveTrainToteSledPosition(DriveTrain.ToteSledPosition position) {
		m_position = position;
		requires(RobotMain.driveTrain);
	}

	@Override
	protected void initialize() {
		RobotMain.driveTrain.setToteSledPosition(m_position);
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