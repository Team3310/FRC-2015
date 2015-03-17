package edu.rhhs.frc;

import edu.rhhs.frc.buttons.XBoxDPadTriggerButton;
import edu.rhhs.frc.buttons.XBoxTriggerButton;
import edu.rhhs.frc.commands.AutonGet3TotesTip3BinsNew;
import edu.rhhs.frc.commands.AutonTurnToHumanPosition;
import edu.rhhs.frc.commands.BinGrabberClawPosition;
import edu.rhhs.frc.commands.BinGrabberDeployAndGoPID;
import edu.rhhs.frc.commands.BinGrabberPivotLockPosition;
import edu.rhhs.frc.commands.BinGrabberPositionDownPID;
import edu.rhhs.frc.commands.BinGrabberPositionStowedPID;
import edu.rhhs.frc.commands.BinGrabberSetLeftSpeed;
import edu.rhhs.frc.commands.BinGrabberSetRightSpeed;
import edu.rhhs.frc.commands.BinGrabberStopPID;
import edu.rhhs.frc.commands.DriveTrainGyroTurn;
import edu.rhhs.frc.commands.DriveTrainMotionProfileStraight;
import edu.rhhs.frc.commands.DriveTrainMotionProfileTurn;
import edu.rhhs.frc.commands.DriveTrainPositionControl;
import edu.rhhs.frc.commands.DriveTrainPositionHoldOn;
import edu.rhhs.frc.commands.DriveTrainStopPID;
import edu.rhhs.frc.commands.DriveTrainToteSledPosition;
import edu.rhhs.frc.commands.RobotArmMotionProfileCurrentToPosition;
import edu.rhhs.frc.commands.RobotArmMotionProfileNext;
import edu.rhhs.frc.commands.RobotArmMotionProfilePause;
import edu.rhhs.frc.commands.RobotArmMotionProfileReset;
import edu.rhhs.frc.commands.RobotArmMotionProfileResume;
import edu.rhhs.frc.commands.RobotArmMotionProfileStart;
import edu.rhhs.frc.commands.RobotArmSetHome;
import edu.rhhs.frc.commands.ToteGrabberPosition;
import edu.rhhs.frc.commands.robotarm.HumanLoadCommandListGenerator;
import edu.rhhs.frc.commands.robotarm.RobotArmCommandList;
import edu.rhhs.frc.commands.robotarm.RobotArmMotionProfileCurrentToPath;
import edu.rhhs.frc.commands.robotarm.RobotArmMotionProfileJ1ToZero;
import edu.rhhs.frc.controller.XboxController;
import edu.rhhs.frc.subsystems.BinGrabber;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.RobotArm;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.WaypointList;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI 
{	
	private static OI instance;

	private XboxController m_drivetrainController;
	private XboxController m_robotArmController;
//	private Joystick m_joystick1;
//    private Joystick m_joystick2;
	
	private OI() {
		m_drivetrainController = new XboxController(RobotMap.XBOX_1_USB_ID);
		m_robotArmController = new XboxController(RobotMap.XBOX_2_USB_ID);
//		m_joystick1 = new Joystick(RobotMap.JOYSTICK_1_USB_ID);
//		m_joystick2 = new Joystick(RobotMap.JOYSTICK_2_USB_ID);
//	    
//		// Joystick controller
//        JoystickButton toteSledUpJoystick = new JoystickButton(m_joystick1, 3);
//        toteSledUpJoystick.whenPressed(new DriveTrainToteSledPosition(DriveTrain.ToteSledPosition.UP));
//
//        JoystickButton toteSledDownJoystick = new JoystickButton(m_joystick1, 2);
//        toteSledDownJoystick.whenPressed(new DriveTrainToteSledPosition(DriveTrain.ToteSledPosition.DOWN));
//  
//        JoystickButton driveTrainHoldOnJoystick = new JoystickButton(m_joystick1, 5);
//        driveTrainHoldOnJoystick.whenPressed(new DriveTrainPositionHoldOn());
//
//        JoystickButton driveTrainHoldOffJoystick = new JoystickButton(m_joystick1, 4);
//        driveTrainHoldOffJoystick.whenPressed(new DriveTrainStopPID());

		// Drivetrain XBox controller
//        JoystickButton binGrabberStowPID = new JoystickButton(m_drivetrainController.getJoyStick(), XboxController.Y_BUTTON);
//        binGrabberStowPID.whenPressed(new BinGrabberPositionStowedPID());
        
//        JoystickButton binGrabberCancelPID = new JoystickButton(m_drivetrainController.getJoyStick(), XboxController.X_BUTTON);
//        binGrabberCancelPID.whenPressed(new BinGrabberStopPID());
	    
//        JoystickButton binGrabberDragPID = new JoystickButton(m_drivetrainController.getJoyStick(), XboxController.B_BUTTON);
//        binGrabberDragPID.whenPressed(new BinGrabberPositionDownPID(BinGrabber.DRAG_BIN_POSITION_DEG, BinGrabber.DRAG_BIN_POSITION_DEG));
 
        JoystickButton toteSledUp = new JoystickButton(m_drivetrainController.getJoyStick(), XboxController.Y_BUTTON);
        toteSledUp.whenPressed(new DriveTrainToteSledPosition(DriveTrain.ToteSledPosition.UP));

        JoystickButton toteSledDown = new JoystickButton(m_drivetrainController.getJoyStick(), XboxController.A_BUTTON);
        toteSledDown.whenPressed(new DriveTrainToteSledPosition(DriveTrain.ToteSledPosition.DOWN));
        
        JoystickButton driveTrainHoldOnButton = new JoystickButton(m_drivetrainController.getJoyStick(), XboxController.B_BUTTON);
        driveTrainHoldOnButton.whenPressed(new DriveTrainPositionHoldOn());

        JoystickButton driveTrainHoldOffButton = new JoystickButton(m_drivetrainController.getJoyStick(), XboxController.X_BUTTON);
        driveTrainHoldOffButton.whenPressed(new DriveTrainStopPID());

        JoystickButton binGrabberRightUpManual = new JoystickButton(m_drivetrainController.getJoyStick(), XboxController.RIGHT_BUMPER_BUTTON);
        binGrabberRightUpManual.whenPressed(new BinGrabberSetRightSpeed(-0.3));
        binGrabberRightUpManual.whenReleased(new BinGrabberSetRightSpeed(0));

        JoystickButton binGrabberLeftUpManual = new JoystickButton(m_drivetrainController.getJoyStick(), XboxController.LEFT_BUMPER_BUTTON);
        binGrabberLeftUpManual.whenPressed(new BinGrabberSetLeftSpeed(-0.3));
        binGrabberLeftUpManual.whenReleased(new BinGrabberSetLeftSpeed(0));
        
        XBoxTriggerButton binGrabberRightDownManual = new XBoxTriggerButton(m_drivetrainController, XBoxTriggerButton.RIGHT_TRIGGER);
        binGrabberRightDownManual.whenPressed(new BinGrabberSetRightSpeed(0.3));
        binGrabberRightDownManual.whenReleased(new BinGrabberSetRightSpeed(0.0));

        XBoxTriggerButton binGrabberLeftDownManual = new XBoxTriggerButton(m_drivetrainController, XBoxTriggerButton.LEFT_TRIGGER);
        binGrabberLeftDownManual.whenPressed(new BinGrabberSetLeftSpeed(0.3));
        binGrabberLeftDownManual.whenReleased(new BinGrabberSetLeftSpeed(0.0));

        JoystickButton binGrabberPivotLockExtend = new JoystickButton(m_drivetrainController.getJoyStick(), XboxController.START_BUTTON);
        binGrabberPivotLockExtend.whenPressed(new BinGrabberPivotLockPosition(BinGrabber.BinGrabberState.EXTENDED));

        JoystickButton binGrabberPivotLockRelease = new JoystickButton(m_drivetrainController.getJoyStick(), XboxController.BACK_BUTTON);
        binGrabberPivotLockRelease.whenPressed(new BinGrabberPivotLockPosition(BinGrabber.BinGrabberState.RETRACTED));

        XBoxDPadTriggerButton binGrabberClawOpen = new XBoxDPadTriggerButton(m_drivetrainController, XBoxDPadTriggerButton.UP);
        binGrabberClawOpen.whenPressed(new BinGrabberClawPosition(BinGrabber.BinGrabberState.EXTENDED));

        XBoxDPadTriggerButton binGrabberClawClose = new XBoxDPadTriggerButton(m_drivetrainController, XBoxDPadTriggerButton.DOWN);
        binGrabberClawClose.whenPressed(new BinGrabberClawPosition(BinGrabber.BinGrabberState.RETRACTED));    
        
        XBoxDPadTriggerButton driveTrainHoldOn = new XBoxDPadTriggerButton(m_drivetrainController, XBoxDPadTriggerButton.RIGHT);
        driveTrainHoldOn.whenPressed(new DriveTrainPositionHoldOn());

        XBoxDPadTriggerButton driveTrainHoldOff = new XBoxDPadTriggerButton(m_drivetrainController, XBoxDPadTriggerButton.LEFT);
        driveTrainHoldOff.whenPressed(new DriveTrainStopPID());
        
        // Robot arm controller
        JoystickButton motionProfilePause = new JoystickButton(m_robotArmController.getJoyStick(), XboxController.Y_BUTTON);
        motionProfilePause.whenPressed(new RobotArmMotionProfilePause());

        JoystickButton motionProfileResume = new JoystickButton(m_robotArmController.getJoyStick(), XboxController.A_BUTTON);
        motionProfileResume.whenPressed(new RobotArmMotionProfileResume());
 
        JoystickButton motionProfileNext = new JoystickButton(m_robotArmController.getJoyStick(), XboxController.B_BUTTON);
        motionProfileNext.whenPressed(new RobotArmMotionProfileNext());

        JoystickButton motionProfileStart = new JoystickButton(m_robotArmController.getJoyStick(), XboxController.X_BUTTON);
        motionProfileStart.whenPressed(new RobotArmMotionProfileStart(RobotMain.commandListGenerator.getCommandList()));

    	WaypointList waypointsCurrentToHome = new WaypointList(MotionProfile.ProfileMode.CartesianInputJointMotion);
    	waypointsCurrentToHome.addWaypoint(HumanLoadCommandListGenerator.DEFAULT_HOME_COORD);
    	RobotArmCommandList commandListCurrentToHome = new RobotArmCommandList();
    	commandListCurrentToHome.add(new RobotArmMotionProfileCurrentToPath(waypointsCurrentToHome));

        JoystickButton motionProfileGoHome = new JoystickButton(m_robotArmController.getJoyStick(), XboxController.START_BUTTON);
        motionProfileGoHome.whenPressed(new RobotArmMotionProfileStart(commandListCurrentToHome));

        JoystickButton resetHome = new JoystickButton(m_robotArmController.getJoyStick(), XboxController.BACK_BUTTON);
        resetHome.whenPressed(new RobotArmSetHome());

        JoystickButton toteGrabberOpen = new JoystickButton(m_robotArmController.getJoyStick(), XboxController.LEFT_BUMPER_BUTTON);
        toteGrabberOpen.whenPressed(new ToteGrabberPosition(RobotArm.ToteGrabberPosition.OPEN));

        JoystickButton toteGrabberClose = new JoystickButton(m_robotArmController.getJoyStick(), XboxController.RIGHT_BUMPER_BUTTON);
        toteGrabberClose.whenPressed(new ToteGrabberPosition(RobotArm.ToteGrabberPosition.CLOSE));

        XBoxDPadTriggerButton moveToSixStackLoadHeight = new XBoxDPadTriggerButton(m_robotArmController, XBoxDPadTriggerButton.UP);
        moveToSixStackLoadHeight.whenPressed(new RobotArmMotionProfileCurrentToPosition(36, 0, 78, MotionProfile.ProfileMode.CartesianInputJointMotion));
        
        XBoxDPadTriggerButton moveToSixStackReleaseHeight = new XBoxDPadTriggerButton(m_robotArmController, XBoxDPadTriggerButton.DOWN);
        moveToSixStackReleaseHeight.whenPressed(new RobotArmMotionProfileCurrentToPosition(36, 0, 6, MotionProfile.ProfileMode.CartesianInputJointMotion));
        
    	RobotArmCommandList commandListJ1ToZero = new RobotArmCommandList();
    	commandListJ1ToZero.add(new RobotArmMotionProfileJ1ToZero());

        XBoxDPadTriggerButton j1ToZero = new XBoxDPadTriggerButton(m_robotArmController, XBoxDPadTriggerButton.RIGHT);
        j1ToZero.whenPressed(new RobotArmMotionProfileStart(commandListJ1ToZero));
       
        // Testing
//        InternalButton binGrabberDeployTimedTest = new InternalButton();
//        binGrabberDeployTimedTest.whenReleased(new BinGrabberDeployTimed(1.0, 300));
//		SmartDashboard.putData("Bin Grabber Deploy Timed", binGrabberDeployTimedTest);
//		
//        InternalButton binGrabberDeployAngleTest = new InternalButton();
//        binGrabberDeployAngleTest.whenReleased(new BinGrabberDeployAngle(1.0, BinGrabber.DEPLOYED_POSITION_DRIVETRAIN_ENGAGE_DEG, 300));
//		SmartDashboard.putData("Bin Grabber Deploy Angle Limit", binGrabberDeployAngleTest);
//		
//		InternalButton binGrabberDeployPIDTest = new InternalButton();
//        binGrabberDeployPIDTest.whenReleased(new BinGrabberPositionDownPID(BinGrabber.DEPLOYED_POSITION_DEG, BinGrabber.DEPLOYED_POSITION_DEG));
//		SmartDashboard.putData("Bin Grabber Deploy Position PID", binGrabberDeployPIDTest);
//		
		InternalButton binGrabberStowedPIDTest = new InternalButton();
        binGrabberStowedPIDTest.whenReleased(new BinGrabberPositionStowedPID());
		SmartDashboard.putData("Bin Grabber Stowed Position PID", binGrabberStowedPIDTest);
		
		InternalButton binGrabberStopPIDTest = new InternalButton();
		binGrabberStopPIDTest.whenReleased(new BinGrabberStopPID());
		SmartDashboard.putData("Bin Grabber Cancel PID", binGrabberStopPIDTest);
		
		InternalButton binGrabberDragPIDTest = new InternalButton();
        binGrabberDragPIDTest.whenReleased(new BinGrabberPositionDownPID(BinGrabber.DRAG_BIN_POSITION_DEG, BinGrabber.DRAG_BIN_POSITION_DEG));
		SmartDashboard.putData("Bin Grabber Drag Position PID", binGrabberDragPIDTest);

//		InternalButton binGrabberDeployAndGoTest = new InternalButton();
//		binGrabberDeployAndGoTest.whenReleased(new BinGrabberDeployAndGo());
//		SmartDashboard.putData("Bin Grabber Deploy and Go", binGrabberDeployAndGoTest);
//		
		InternalButton binGrabberDeployAndGoPIDTest = new InternalButton();
		binGrabberDeployAndGoPIDTest.whenReleased(new BinGrabberDeployAndGoPID());
		SmartDashboard.putData("Bin Grabber Deploy and Go PID", binGrabberDeployAndGoPIDTest);
		
//		InternalButton binGrabberDeployAndDriveTest = new InternalButton();
//		binGrabberDeployAndDriveTest.whenReleased(new BinGrabberDeployAndDrive(60, 60, 10, 1));
//		SmartDashboard.putData("Bin Grabber Deploy and Drive", binGrabberDeployAndDriveTest);
//
//		InternalButton drivetrainTestSpeedTest = new InternalButton();
//		drivetrainTestSpeedTest.whenReleased(new DriveTrainSpeedTimeout(1, 3));
//		SmartDashboard.putData("DriveTrain Test Speed", drivetrainTestSpeedTest);		
//
//		InternalButton drivetrainTestVelTest = new InternalButton();
//		drivetrainTestVelTest.whenReleased(new DriveTrainVelocityControl(1500, 1500, 1, 2));
//		SmartDashboard.putData("DriveTrain Test Velocity PID", drivetrainTestVelTest);		
//
		InternalButton drivetrainTestPositionTest = new InternalButton();
		drivetrainTestPositionTest.whenReleased(new DriveTrainPositionControl(60, 60, true, 60));
		SmartDashboard.putData("DriveTrain Test Position PID", drivetrainTestPositionTest);		

		InternalButton toteGrabberOpenTest = new InternalButton();
		toteGrabberOpenTest.whenPressed(new ToteGrabberPosition(RobotArm.ToteGrabberPosition.OPEN));
		SmartDashboard.putData("Tote Grabber Open", toteGrabberOpenTest);

		InternalButton toteGrabberClosedTest = new InternalButton();
		toteGrabberClosedTest.whenPressed(new ToteGrabberPosition(RobotArm.ToteGrabberPosition.CLOSE));
		SmartDashboard.putData("Tote Grabber Closed", toteGrabberClosedTest);

//		InternalButton toteGrabberAutoTest = new InternalButton();
//		toteGrabberAutoTest.whenReleased(new ToteGrabberAutoClose());
//		SmartDashboard.putData("Tote Grabber Auto Close", toteGrabberAutoTest);
//		
		InternalButton binGrabberClawOpenTest = new InternalButton();
		binGrabberClawOpenTest.whenPressed(new BinGrabberClawPosition(BinGrabber.BinGrabberState.EXTENDED));
		SmartDashboard.putData("Bin Grabber Claw Open", binGrabberClawOpenTest);
		
		InternalButton binGrabberClawClosedTest = new InternalButton();
		binGrabberClawClosedTest.whenPressed(new BinGrabberClawPosition(BinGrabber.BinGrabberState.RETRACTED));
		SmartDashboard.putData("Bin Grabber Claw Closed", binGrabberClawClosedTest);
		
		InternalButton binGrabberPivotLockTest = new InternalButton();
		binGrabberPivotLockTest.whenPressed(new BinGrabberPivotLockPosition(BinGrabber.BinGrabberState.EXTENDED));
		SmartDashboard.putData("Bin Grabber Pivot Lock", binGrabberPivotLockTest);
		
		InternalButton binGrabberPivotUnlockTest = new InternalButton();
		binGrabberPivotUnlockTest.whenPressed(new BinGrabberPivotLockPosition(BinGrabber.BinGrabberState.RETRACTED));
		SmartDashboard.putData("Bin Grabber Pivot Unlock", binGrabberPivotUnlockTest);
		
		InternalButton driveStraightTest = new InternalButton();
		driveStraightTest.whenPressed(new DriveTrainMotionProfileStraight(60, 60));
		SmartDashboard.putData("Drive Straight Test", driveStraightTest);
		
		InternalButton driveTurnRightTest = new InternalButton();
		driveTurnRightTest.whenPressed(new DriveTrainMotionProfileTurn(-45, 60, false));
		SmartDashboard.putData("Drive Turn -45 Test", driveTurnRightTest);		
		
		InternalButton driveTurnLeftTest = new InternalButton();
		driveTurnLeftTest.whenPressed(new DriveTrainMotionProfileTurn(45, 60, false));
		SmartDashboard.putData("Drive Turn 45 Test", driveTurnLeftTest);
		
		InternalButton driveTankTurnLeftTest = new InternalButton();
		driveTankTurnLeftTest.whenPressed(new DriveTrainMotionProfileTurn(45, 60, true));
		SmartDashboard.putData("Drive Tank Turn 45 Test", driveTankTurnLeftTest);
		
		InternalButton driveTankTurnRightTest = new InternalButton();
		driveTankTurnRightTest.whenPressed(new DriveTrainMotionProfileTurn(-45, 60, true));
		SmartDashboard.putData("Drive Tank Turn -45 Test", driveTankTurnRightTest);
		
		InternalButton driveGyroTurnLeftTest = new InternalButton();
		driveGyroTurnLeftTest.whenPressed(new DriveTrainGyroTurn(20, 1, 0.6));
		SmartDashboard.putData("Drive Gyro Turn  45 Test", driveGyroTurnLeftTest);
		
		InternalButton driveGyroTurnRightTest = new InternalButton();
		driveGyroTurnRightTest.whenPressed(new DriveTrainGyroTurn(-20, 1, 0.6));
		SmartDashboard.putData("Drive Gyro Turn -45 Test", driveGyroTurnRightTest);

		//    	WaypointList waypoints0 = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
//    	waypoints0.addWaypoint(RobotArm.J1_MASTER_ANGLE_DEG, RobotArm.J2_MASTER_ANGLE_DEG, RobotArm.J3_MASTER_ANGLE_DEG, RobotArm.J4_MASTER_ANGLE_DEG);
//    	waypoints0.addWaypoint(0, 0, 0, 0);
//    	RobotArmCommandList commandList0 = new RobotArmCommandList();
//		commandList0.add(new RobotArmMotionProfilePath(waypoints0));
//
//		InternalButton motionProfileStartMasterTo0 = new InternalButton();
//		motionProfileStartMasterTo0.whenPressed(new RobotArmMotionProfileStart(commandList0));
//		SmartDashboard.putData("Motion Profile Start Master to 0", motionProfileStartMasterTo0);
//		
//		// J1 motion Profile
//    	WaypointList waypointsJ1A = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
//    	waypointsJ1A.addWaypoint(0, 0, -60, 0);
//    	waypointsJ1A.addWaypoint(-140, 0, -60, 0);
//    	RobotArmCommandList commandListJ1A = new RobotArmCommandList();
//		commandListJ1A.add(new RobotArmMotionProfilePath(waypointsJ1A));
//
//		InternalButton motionProfileJ1A = new InternalButton();
//		motionProfileJ1A.whenPressed(new RobotArmMotionProfileStart(commandListJ1A));
//		SmartDashboard.putData("Motion Profile Start J1 A", motionProfileJ1A);
//		
//    	WaypointList waypointsJ1B = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
//    	waypointsJ1B.addWaypoint(-140, 0, -60, 0);
//    	waypointsJ1B.addWaypoint(40, 0, -60, 0);
//    	RobotArmCommandList commandListJ1B = new RobotArmCommandList();
//		commandListJ1B.add(new RobotArmMotionProfilePath(waypointsJ1B));
//
//		InternalButton motionProfileJ1B = new InternalButton();
//		motionProfileJ1B.whenPressed(new RobotArmMotionProfileStart(commandListJ1B));
//		SmartDashboard.putData("Motion Profile Start J1 B", motionProfileJ1B);
//
//    	WaypointList waypointsJ1C = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
//    	waypointsJ1C.addWaypoint(40, 0, -60, 0);
//    	waypointsJ1C.addWaypoint(0, 0, -60, 0);
//    	RobotArmCommandList commandListJ1C = new RobotArmCommandList();
//		commandListJ1C.add(new RobotArmMotionProfilePath(waypointsJ1C));
//
//		InternalButton motionProfileJ1C = new InternalButton();
//		motionProfileJ1C.whenPressed(new RobotArmMotionProfileStart(commandListJ1C));
//		SmartDashboard.putData("Motion Profile Start J1 C", motionProfileJ1C);
//
//		// J2 motion Profile
//    	WaypointList waypointsJ2A = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
//    	waypointsJ2A.addWaypoint(RobotArm.J1_MASTER_ANGLE_DEG, RobotArm.J2_MASTER_ANGLE_DEG, RobotArm.J3_MASTER_ANGLE_DEG, RobotArm.J4_MASTER_ANGLE_DEG);
//    	waypointsJ2A.addWaypoint(0, 42, -28, 0);
//    	RobotArmCommandList commandListJ2A = new RobotArmCommandList();
//		commandListJ2A.add(new RobotArmMotionProfilePath(waypointsJ2A));
//
//		InternalButton motionProfileJ2A = new InternalButton();
//		motionProfileJ2A.whenPressed(new RobotArmMotionProfileStart(commandListJ2A));
//		SmartDashboard.putData("Motion Profile Start J2 A", motionProfileJ2A);
//		
//    	WaypointList waypointsJ2B = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
//    	waypointsJ2B.addWaypoint(0, 42, -28, 0);
//    	waypointsJ2B.addWaypoint(0, 0, 0, 0);
//    	RobotArmCommandList commandListJ2B = new RobotArmCommandList();
//		commandListJ2B.add(new RobotArmMotionProfilePath(waypointsJ2B));
//
//		InternalButton motionProfileJ2B = new InternalButton();
//		motionProfileJ2B.whenPressed(new RobotArmMotionProfileStart(commandListJ2B));
//		SmartDashboard.putData("Motion Profile Start J2 B", motionProfileJ2B);
//
//    	WaypointList waypointsJ2C = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
//    	waypointsJ2C.addWaypoint(0, 0, 0, 0);
//    	waypointsJ2C.addWaypoint(0, 42, -28, 0);
//    	RobotArmCommandList commandListJ2C = new RobotArmCommandList();
//		commandListJ2C.add(new RobotArmMotionProfilePath(waypointsJ2C));
//
//		InternalButton motionProfileJ2C = new InternalButton();
//		motionProfileJ2C.whenPressed(new RobotArmMotionProfileStart(commandListJ2C));
//		SmartDashboard.putData("Motion Profile Start J2 C", motionProfileJ2C);		
//		
//		// J3 motion Profile
//    	WaypointList waypointsJ3A = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
//    	waypointsJ3A.addWaypoint(0, 0, 0, 0);
//    	waypointsJ3A.addWaypoint(0, 0, -45, 0);
//    	RobotArmCommandList commandListJ3A = new RobotArmCommandList();
//		commandListJ3A.add(new RobotArmMotionProfilePath(waypointsJ3A));
//
//		InternalButton motionProfileJ3A = new InternalButton();
//		motionProfileJ3A.whenPressed(new RobotArmMotionProfileStart(commandListJ3A));
//		SmartDashboard.putData("Motion Profile Start J3 A", motionProfileJ3A);
//		
//    	WaypointList waypointsJ3B = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
//    	waypointsJ3B.addWaypoint(0, 0, -45, 0);
//    	waypointsJ3B.addWaypoint(0, 0, 0, 0);
//    	RobotArmCommandList commandListJ3B = new RobotArmCommandList();
//		commandListJ3B.add(new RobotArmMotionProfilePath(waypointsJ3B));
//
//		InternalButton motionProfileJ3B = new InternalButton();
//		motionProfileJ3B.whenPressed(new RobotArmMotionProfileStart(commandListJ3B));
//		SmartDashboard.putData("Motion Profile Start J3 B", motionProfileJ3B);
//
//    	WaypointList waypointsJ3C = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
//    	waypointsJ3C.addWaypoint(0, 0, 0, 0);
//    	waypointsJ3C.addWaypoint(0, 0, 0, 0);
//    	RobotArmCommandList commandListJ3C = new RobotArmCommandList();
//		commandListJ3C.add(new RobotArmMotionProfilePath(waypointsJ3C));
//
//		InternalButton motionProfileJ3C = new InternalButton();
//		motionProfileJ3C.whenPressed(new RobotArmMotionProfileStart(commandListJ3C));
//		SmartDashboard.putData("Motion Profile Start J3 C", motionProfileJ3C);		
//		
//		// Master to human station Profile
//    	WaypointList waypointsM2H = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
//    	waypointsM2H.addWaypoint(RobotArm.J1_MASTER_ANGLE_DEG, RobotArm.J2_MASTER_ANGLE_DEG, RobotArm.J3_MASTER_ANGLE_DEG, RobotArm.J4_MASTER_ANGLE_DEG);
//    	waypointsM2H.addWaypoint(0, 89,  -101.7,   0);
//    	waypointsM2H.addWaypoint(-129, 89,  -101.7,   0);
//    	waypointsM2H.addWaypoint(-129, 73, -42.69, 0);
//    	RobotArmCommandList commandListM2H = new RobotArmCommandList();
//    	commandListM2H.add(new RobotArmMotionProfilePath(waypointsM2H));
//
//		InternalButton motionProfileM2H = new InternalButton();
//		motionProfileM2H.whenPressed(new RobotArmMotionProfileStart(commandListM2H));
//		SmartDashboard.putData("Motion Profile Master To Human", motionProfileM2H);
//		
//		// Master to human station Profile
//    	WaypointList waypointsM2HCart = new WaypointList(MotionProfile.ProfileMode.CartesianInputJointMotion);
//    	waypointsM2HCart.addWaypoint(RobotArm.X_MASTER_POSITION_IN, RobotArm.Y_MASTER_POSITION_IN, RobotArm.Z_MASTER_POSITION_IN, RobotArm.GAMMA_MASTER_ANGLE_DEG);
//    	waypointsM2HCart.addWaypoint(30.9, 0.0, 11.8, 0.0);
//    	waypointsM2HCart.addWaypoint(HumanLoadCommandListGenerator.HUMAN_LOAD_START_COORD);
//    	RobotArmCommandList commandListM2HCart = new RobotArmCommandList();
//    	commandListM2HCart.add(new RobotArmMotionProfilePath(waypointsM2HCart));
//
//		InternalButton motionProfileM2HCart = new InternalButton();
//		motionProfileM2HCart.whenPressed(new RobotArmMotionProfileStart(commandListM2HCart));
//		SmartDashboard.putData("Motion Profile Master To Human Cartesian", motionProfileM2HCart);
//
//		// human station to stack Profile
//    	WaypointList waypointsH2SCart = new WaypointList(MotionProfile.ProfileMode.CartesianInputJointMotion);
//    	waypointsH2SCart.addWaypoint(HumanLoadCommandListGenerator.HUMAN_LOAD_START_COORD);
//    	waypointsH2SCart.addWaypoint(HumanLoadCommandListGenerator.HUMAN_LOAD_FINISH_COORD);
//    	waypointsH2SCart.addWaypoint(29, -18, 15, 0.0);
//    	waypointsH2SCart.addWaypoint(HumanLoadCommandListGenerator.LEFT_POSITION_BUILD_STACK_RELEASE_COORD);
//    	RobotArmCommandList commandListH2SCart = new RobotArmCommandList();
//    	commandListH2SCart.add(new RobotArmMotionProfilePath(waypointsH2SCart));
//
//		InternalButton motionProfileH2SCart = new InternalButton();
//		motionProfileH2SCart.whenPressed(new RobotArmMotionProfileStart(commandListH2SCart));
//		SmartDashboard.putData("Motion Profile Human To Stack Cartesian", motionProfileH2SCart);
//
//		// Human station to stack Profile
//    	WaypointList waypointsH2S = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
//    	waypointsH2S.addWaypoint(-129, 73, -42.69, 0);
//    	waypointsH2S.addWaypoint(-129, 68,  -101.7,   0);
//    	waypointsH2S.addWaypoint(  55, 94.7, -65.6, -55);
//    	RobotArmCommandList commandListH2S = new RobotArmCommandList();
//    	commandListH2S.add(new RobotArmMotionProfilePath(waypointsH2S));
//
//		InternalButton motionProfileH2S = new InternalButton();
//		motionProfileH2S.whenPressed(new RobotArmMotionProfileStart(commandListH2S));
//		SmartDashboard.putData("Motion Profile Human To Stack", motionProfileH2S);
//		
//		// Stack 1 To Human Profile
//    	WaypointList waypointsS1ToH = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
//    	waypointsS1ToH.addWaypoint(  55, 94.7, -65.6, -55);
//    	waypointsS1ToH.addWaypoint(  70, 72.6, -73, -55);
//    	waypointsS1ToH.addWaypoint(-129, 89,  -101.7,   0);
//    	waypointsS1ToH.addWaypoint(-129, 73, -42.69, 0);
//    	RobotArmCommandList commandListS1ToH = new RobotArmCommandList();
//    	commandListS1ToH.add(new RobotArmMotionProfilePath(waypointsS1ToH));
//
//		InternalButton motionProfileS1ToH = new InternalButton();
//		motionProfileS1ToH.whenPressed(new RobotArmMotionProfileStart(commandListS1ToH));
//		SmartDashboard.putData("Motion Profile Stack 1 To Human", motionProfileS1ToH);
//		
//		// Human station to stack Profile
//    	WaypointList waypointsH2S2 = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
//    	waypointsH2S2.addWaypoint(-129, 73, -42.69, 0);
//    	waypointsH2S2.addWaypoint(-129, 68,  -101.7,   0);
//    	waypointsH2S2.addWaypoint(  58, 51, -55.0, -58);
//    	RobotArmCommandList commandListH2S2 = new RobotArmCommandList();
//    	commandListH2S2.add(new RobotArmMotionProfilePath(waypointsH2S2));
//
//		InternalButton motionProfileH2S2 = new InternalButton();
//		motionProfileH2S2.whenPressed(new RobotArmMotionProfileStart(commandListH2S2));
//		SmartDashboard.putData("Motion Profile Human To Stack 2", motionProfileH2S2);
		
		// Pause resume
		InternalButton motionProfilePauseTest = new InternalButton();
		motionProfilePauseTest.whenPressed(new RobotArmMotionProfilePause());
		SmartDashboard.putData("Motion Profile Pause", motionProfilePauseTest);
		
		InternalButton motionProfileResumeTest = new InternalButton();
		motionProfileResumeTest.whenPressed(new RobotArmMotionProfileResume());
		SmartDashboard.putData("Motion Profile Resume", motionProfileResumeTest);
		
		InternalButton motionProfileReset = new InternalButton();
		motionProfileReset.whenPressed(new RobotArmMotionProfileReset());
		SmartDashboard.putData("Motion Profile Reset", motionProfileReset);
		
        InternalButton motionProfileJ1Zero = new InternalButton();
		motionProfileJ1Zero.whenPressed(new RobotArmMotionProfileStart(commandListJ1ToZero));
		SmartDashboard.putData("Motion Profile J1 To Zero", motionProfileJ1Zero);
				
		InternalButton driveTrainHoldOnTest = new InternalButton();
		driveTrainHoldOnTest.whenPressed(new DriveTrainPositionHoldOn());
		SmartDashboard.putData("Drivetrain Hold On 1", driveTrainHoldOnTest);
		
		InternalButton driveTrainHoldOffTest = new InternalButton();
		driveTrainHoldOffTest.whenPressed(new DriveTrainStopPID());
		SmartDashboard.putData("Drivetrain Hold Off", driveTrainHoldOffTest);
		
		InternalButton autonGet3TotesTip3Bins = new InternalButton();
		autonGet3TotesTip3Bins.whenPressed(new AutonGet3TotesTip3BinsNew());
		SmartDashboard.putData("Autonomous 3 Totes Tip 3 Bins Test", autonGet3TotesTip3Bins);
		
		InternalButton autonTurnToHumanPosition = new InternalButton();
		autonTurnToHumanPosition.whenPressed(new AutonTurnToHumanPosition());
		SmartDashboard.putData("Autonomous Turn To Huamn Position", autonTurnToHumanPosition);
	}
	
	public static OI getInstance() {
		if(instance == null) {
			instance = new OI();
		}
		return instance;
	}
	
//    public Joystick getJoystick1() {
//        return m_joystick1;
//    }
//    
//    public Joystick getJoystick2() {
//        return m_joystick2;
//    }
//   
	public XboxController getDrivetrainController() {
        return m_drivetrainController;
    }

	public XboxController getRobotArmController() {
        return m_robotArmController;
    }
}