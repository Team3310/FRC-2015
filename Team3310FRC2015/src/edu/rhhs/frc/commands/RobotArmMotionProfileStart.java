package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.commands.robotarm.RobotArmCommandList;
import edu.rhhs.frc.commands.robotarm.RobotArmMotionProfilePath;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.WaypointList;
import edu.wpi.first.wpilibj.command.Command;

public class RobotArmMotionProfileStart extends Command 
{
	private RobotArmCommandList commandList;
	
	public RobotArmMotionProfileStart(RobotArmCommandList commandList) {
		requires(RobotMain.robotArm);
		this.commandList = commandList;
	}
	
	@Override
	protected void initialize() {
		RobotMain.robotArm.startRobotArmCommandList(commandList);
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return RobotMain.robotArm.isControlLoopEnabled();
	}

	@Override
	protected void end() {
		
	}

	@Override
	protected void interrupted() {
		
	}
}
