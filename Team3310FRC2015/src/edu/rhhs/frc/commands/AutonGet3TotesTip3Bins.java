package edu.rhhs.frc.commands;

import edu.rhhs.frc.commands.robotarm.RobotArmCommandList;
import edu.rhhs.frc.commands.robotarm.RobotArmMotionProfileCurrentToPath;
import edu.rhhs.frc.subsystems.RobotArm;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.WaypointList;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonGet3TotesTip3Bins extends CommandGroup 
{
    public AutonGet3TotesTip3Bins() {
    	addSequential(new ToteGrabberPosition(RobotArm.ToteGrabberPosition.CLOSE));
    	
    	WaypointList waypointsLiftFirstTote = new WaypointList(MotionProfile.ProfileMode.CartesianInputJointMotion);
    	waypointsLiftFirstTote.addWaypoint(36, -20, 15, -20);
    	RobotArmCommandList commandListLiftFirstTote = new RobotArmCommandList();
    	commandListLiftFirstTote.add(new RobotArmMotionProfileCurrentToPath(waypointsLiftFirstTote));
    	
    	addSequential(new RobotArmMotionProfileStart(commandListLiftFirstTote));

    	addSequential(new DriveTrainMotionProfileStraight(28, 60));
    	addSequential(new DriveTrainMotionProfileTurn(-31.5, 60, false));
    	
    	WaypointList waypointsPrepareForSecondTote = new WaypointList(MotionProfile.ProfileMode.CartesianInputJointMotion);
    	waypointsPrepareForSecondTote.addWaypoint(36, 0, 15, 0);
    	RobotArmCommandList commandListPrepareForSecondTote = new RobotArmCommandList();
    	commandListPrepareForSecondTote.add(new RobotArmMotionProfileCurrentToPath(waypointsPrepareForSecondTote));
    	
    	addSequential(new RobotArmMotionProfileStart(commandListPrepareForSecondTote));
    	
    	addSequential(new DriveTrainMotionProfileStraight(48, 60));
    	addSequential(new ToteGrabberPosition(RobotArm.ToteGrabberPosition.OPEN));
    	
    	WaypointList waypointsLiftSecondTote = new WaypointList(MotionProfile.ProfileMode.CartesianInputJointMotion);
    	waypointsLiftSecondTote.addWaypoint(36, 0, 5, 0);
    	RobotArmCommandList commandListLiftSecondTote = new RobotArmCommandList();
    	commandListLiftSecondTote.add(new RobotArmMotionProfileCurrentToPath(waypointsLiftSecondTote));
    	
    	addSequential(new RobotArmMotionProfileStart(commandListLiftSecondTote));
    	
    	addSequential(new ToteGrabberPosition(RobotArm.ToteGrabberPosition.CLOSE));
    	
    	WaypointList waypointsMoveWithSecondTote = new WaypointList(MotionProfile.ProfileMode.CartesianInputJointMotion);
    	waypointsMoveWithSecondTote.addWaypoint(36, -20, 15, 0);
    	RobotArmCommandList commandListMoveWithSecondTote = new RobotArmCommandList();
    	commandListMoveWithSecondTote.add(new RobotArmMotionProfileCurrentToPath(waypointsMoveWithSecondTote));
    	
    	addSequential(new RobotArmMotionProfileStart(commandListMoveWithSecondTote));
    	
    	addSequential(new DriveTrainMotionProfileStraight(20, 60));
    	
        addSequential(new DriveTrainStopPID());
    }
}