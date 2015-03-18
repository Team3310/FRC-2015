package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.commands.robotarm.HumanLoadCommandListGeneratorOptimal;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.wpi.first.wpilibj.command.Command;

public class RobotArmSetHome extends Command 
{
	public RobotArmSetHome() {
		requires(RobotMain.robotArm);
	}
	
	@Override
	protected void initialize() {
		MotionProfile motionProfile = new MotionProfile();
		double[] currentRobotPosition = motionProfile.calcForwardKinematicsDeg(RobotMain.robotArm.getJointAngles());
		RobotMain.commandListGenerator.updateWorldToRobotOffset(HumanLoadCommandListGeneratorOptimal.DEFAULT_HOME_COORD, currentRobotPosition, RobotMain.driveTrain.getYawAngleDeg());
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