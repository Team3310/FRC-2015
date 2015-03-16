package edu.rhhs.frc.commands;

import edu.rhhs.frc.commands.robotarm.RobotArmCommandList;
import edu.rhhs.frc.commands.robotarm.RobotArmMotionProfilePath;
import edu.rhhs.frc.subsystems.RobotArm;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.WaypointList;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class AutonGet3TotesTip3Bins extends CommandGroup 
{
    public AutonGet3TotesTip3Bins() {
    	// Start position clamp tote
    	addSequential(new ToteGrabberPosition(RobotArm.ToteGrabberPosition.CLOSE));
    	addSequential(new WaitCommand(1));
   	
    	// Move arm up, over and knock over first bin
    	WaypointList waypointsKnockOverFirstBin = new WaypointList(MotionProfile.ProfileMode.CartesianInputJointMotion);
    	waypointsKnockOverFirstBin.addWaypoint(32,   0,  8, 0);
    	waypointsKnockOverFirstBin.addWaypoint(32,   0, 25, 0);
    	waypointsKnockOverFirstBin.addWaypoint(26, -16, 25, 0);
    	waypointsKnockOverFirstBin.addWaypoint(41, -24, 25, 0);
    	waypointsKnockOverFirstBin.addWaypoint(50,   0, 25, 0);
    	waypointsKnockOverFirstBin.addWaypoint(32,   0, 25, 0);
    	RobotArmCommandList commandListKnockOverFirstTote = new RobotArmCommandList();
    	commandListKnockOverFirstTote.add(new RobotArmMotionProfilePath(waypointsKnockOverFirstBin)); 	
    	addSequential(new RobotArmMotionProfileStart(commandListKnockOverFirstTote));
  
    	// Move forward, turn, go forward to second bin
    	addSequential(new DriveTrainMotionProfileStraight(36, 60));
    	addSequential(new DriveTrainGyroTurn(-23, 1, 0.6));
    	addSequential(new DriveTrainMotionProfileStraight(48, 60));

    	// Move arm up, over and knock over second bin
    	WaypointList waypointsKnockOverSecondBin = new WaypointList(MotionProfile.ProfileMode.CartesianInputJointMotion);
    	waypointsKnockOverSecondBin.addWaypoint(32,   0, 25, 0);
    	waypointsKnockOverSecondBin.addWaypoint(29,  -9, 25, 0);
    	waypointsKnockOverSecondBin.addWaypoint(50, -24, 25, 0);
    	waypointsKnockOverSecondBin.addWaypoint(50,   0, 25, 0);
    	waypointsKnockOverSecondBin.addWaypoint(32,   0, 18, 0);
    	RobotArmCommandList commandListKnockOverSecondBin = new RobotArmCommandList();
    	commandListKnockOverSecondBin.add(new RobotArmMotionProfilePath(waypointsKnockOverSecondBin));	
    	addSequential(new RobotArmMotionProfileStart(commandListKnockOverSecondBin));   	

    	// Open grabber to release first tote while on top of second tote
    	addSequential(new ToteGrabberPosition(RobotArm.ToteGrabberPosition.OPEN));
       	addSequential(new WaitCommand(1));
    	addSequential(new ToteGrabberPosition(RobotArm.ToteGrabberPosition.CLOSE));
       	addSequential(new WaitCommand(1));
    	addSequential(new ToteGrabberPosition(RobotArm.ToteGrabberPosition.OPEN));
    	addSequential(new WaitCommand(1));
    	
    	// Move arm down to grab second tote
    	WaypointList waypointsPrepareToLiftSecondTote = new WaypointList(MotionProfile.ProfileMode.CartesianInputJointMotion);
    	waypointsPrepareToLiftSecondTote.addWaypoint(32, 0, 18, 0);
    	waypointsPrepareToLiftSecondTote.addWaypoint(32, 0, 8, 0);
    	RobotArmCommandList commandListLiftSecondTote = new RobotArmCommandList();
    	commandListLiftSecondTote.add(new RobotArmMotionProfilePath(waypointsPrepareToLiftSecondTote));	
    	addSequential(new RobotArmMotionProfileStart(commandListLiftSecondTote));
    	
    	// Close grabber on second tote
    	addSequential(new ToteGrabberPosition(RobotArm.ToteGrabberPosition.CLOSE));
    	addSequential(new WaitCommand(1));
    	
    	// Move arm to lift both totes
    	WaypointList waypointsMoveWithSecondTote = new WaypointList(MotionProfile.ProfileMode.CartesianInputJointMotion);
    	waypointsMoveWithSecondTote.addWaypoint(32, 0, 8, 0);
    	waypointsMoveWithSecondTote.addWaypoint(32, 0, 25, 0);
    	RobotArmCommandList commandListMoveWithSecondTote = new RobotArmCommandList();
    	commandListMoveWithSecondTote.add(new RobotArmMotionProfilePath(waypointsMoveWithSecondTote));
    	addSequential(new RobotArmMotionProfileStart(commandListMoveWithSecondTote));
    	
    	// Move forward to third bin
    	addSequential(new DriveTrainMotionProfileStraight(77, 60));

    	// Move arm up, over and knock over third bin
    	WaypointList waypointsKnockOverThirdBin = new WaypointList(MotionProfile.ProfileMode.CartesianInputJointMotion);
    	waypointsKnockOverThirdBin.addWaypoint(36, 0, 25, 0);
    	waypointsKnockOverThirdBin.addWaypoint(36, 0, 18, 0);
    	RobotArmCommandList commandListKnockOverThirdBin = new RobotArmCommandList();
    	commandListKnockOverThirdBin.add(new RobotArmMotionProfilePath(waypointsKnockOverThirdBin));	
    	addSequential(new RobotArmMotionProfileStart(commandListKnockOverThirdBin));   	

    	// Open grabber to release second tote while on top of third tote
    	addSequential(new ToteGrabberPosition(RobotArm.ToteGrabberPosition.OPEN));
    	addSequential(new WaitCommand(1));
    	
    	// Move arm down to grab third tote
    	WaypointList waypointsPrepareToLiftThirdTote = new WaypointList(MotionProfile.ProfileMode.CartesianInputJointMotion);
    	waypointsPrepareToLiftThirdTote.addWaypoint(36, 0, 18, 0);
    	waypointsPrepareToLiftThirdTote.addWaypoint(36, 0, 8, 0);
    	RobotArmCommandList commandListLiftThirdTote = new RobotArmCommandList();
    	commandListLiftThirdTote.add(new RobotArmMotionProfilePath(waypointsPrepareToLiftThirdTote));	
    	addSequential(new RobotArmMotionProfileStart(commandListLiftThirdTote));
    	
    	// Close grabber on third tote
    	addSequential(new ToteGrabberPosition(RobotArm.ToteGrabberPosition.CLOSE));
    	addSequential(new WaitCommand(1));

    	addSequential(new DriveTrainStopPID());
    }
}