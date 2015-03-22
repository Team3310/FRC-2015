package edu.rhhs.frc.commands;

import edu.rhhs.frc.commands.robotarm.HumanLoadCommandListGenerator;
import edu.rhhs.frc.commands.robotarm.RobotArmCommandList;
import edu.rhhs.frc.commands.robotarm.RobotArmMotionProfilePath;
import edu.rhhs.frc.subsystems.DriveTrain.ToteSledPosition;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.WaypointList;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class AutonTurnToHumanPosition extends CommandGroup 
{
	public AutonTurnToHumanPosition() {
		addSequential(new DriveTrainMotionProfileStraight(-32.7, 60));
		addSequential(new DriveTrainPositionHoldOn());

		// Start position clamp tote
		addSequential(new DriveTrainToteSledPosition(ToteSledPosition.DOWN));
		addSequential(new WaitCommand(4));

		// Move arm up, over and knock over first bin
		WaypointList armDown = new WaypointList(MotionProfile.ProfileMode.CartesianInputJointMotion);
		armDown.addWaypoint(0,   15, 47, 0);
		armDown.addWaypoint(16, 0, 48, 0);
		armDown.addWaypoint(HumanLoadCommandListGenerator.HOME_LOAD_COORD);
		RobotArmCommandList armDownCommandList = new RobotArmCommandList();
		armDownCommandList.add(new RobotArmMotionProfilePath(armDown, new double[] {120, 120, 120, 120}, new double[] {100, 100, 100}, new double[] {100, 100, 100})); 	
		addSequential(new RobotArmMotionProfileStart(armDownCommandList));

		addSequential(new DriveTrainStopPID());
	}
}