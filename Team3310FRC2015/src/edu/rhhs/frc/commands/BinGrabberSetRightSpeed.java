package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Command;

public class BinGrabberSetRightSpeed extends Command
{
	private double speed;
	
	public BinGrabberSetRightSpeed(double speed) {
		this.speed = speed;
		requires(RobotMain.binGrabber);
	}
	
	@Override
	protected void initialize() {
		RobotMain.binGrabber.setTalonControlMode(CANTalon.ControlMode.PercentVbus);
		RobotMain.binGrabber.setRightSpeed(speed);
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
