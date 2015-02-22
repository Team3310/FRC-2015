package edu.rhhs.frc;

import edu.rhhs.frc.commands.BinGrabberClawPosition;
import edu.rhhs.frc.commands.BinGrabberDeployAndDrive;
import edu.rhhs.frc.commands.BinGrabberDeployAndGo;
import edu.rhhs.frc.commands.BinGrabberDeployAngle;
import edu.rhhs.frc.commands.BinGrabberDeployTimed;
import edu.rhhs.frc.commands.BinGrabberPivotLockPosition;
import edu.rhhs.frc.commands.BinGrabberPositionPID;
import edu.rhhs.frc.commands.BinGrabberSetSpeed;
import edu.rhhs.frc.commands.DriveTrainPositionControl;
import edu.rhhs.frc.commands.DriveTrainPositionHoldOff;
import edu.rhhs.frc.commands.DriveTrainPositionHoldOn;
import edu.rhhs.frc.commands.DriveTrainSpeedTimeout;
import edu.rhhs.frc.commands.DriveTrainVelocityControl;
import edu.rhhs.frc.commands.RobotArmMotionProfilePause;
import edu.rhhs.frc.commands.RobotArmMotionProfileResume;
import edu.rhhs.frc.commands.RobotArmMotionProfileStart;
import edu.rhhs.frc.commands.ToteGrabberAutoClose;
import edu.rhhs.frc.commands.ToteGrabberPosition;
import edu.rhhs.frc.commands.robotarm.RobotArmCommandList;
import edu.rhhs.frc.commands.robotarm.RobotArmMotionProfilePath;
import edu.rhhs.frc.controller.XboxController;
import edu.rhhs.frc.subsystems.BinGrabber;
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

	private XboxController m_xboxController1;
	private XboxController m_xboxController2;
	private Joystick m_joystick1;
    private Joystick m_joystick2;
	
	private OI() {
		m_xboxController1 = new XboxController(RobotMap.XBOX_1_USB_ID);
		m_xboxController2 = new XboxController(RobotMap.XBOX_2_USB_ID);
        m_joystick1 = new Joystick(RobotMap.JOYSTICK_1_USB_ID);
        m_joystick2 = new Joystick(RobotMap.JOYSTICK_2_USB_ID);
	    
        JoystickButton moveBinGrabberUp = new JoystickButton(m_xboxController1.getJoyStick(), XboxController.A_BUTTON);
        moveBinGrabberUp.whenPressed(new BinGrabberSetSpeed(0.3));
        moveBinGrabberUp.whenReleased(new BinGrabberSetSpeed(0));
        
        JoystickButton moveBinGrabberDown = new JoystickButton(m_xboxController1.getJoyStick(), XboxController.Y_BUTTON);
        moveBinGrabberDown.whenPressed(new BinGrabberSetSpeed(-0.3));
        moveBinGrabberDown.whenReleased(new BinGrabberSetSpeed(0));
	    
        InternalButton binGrabberDeployTimed = new InternalButton();
        binGrabberDeployTimed.whenReleased(new BinGrabberDeployTimed(1.0, 300));
		SmartDashboard.putData("Bin Grabber Deploy Timed", binGrabberDeployTimed);
		
        InternalButton binGrabberDeployAngle = new InternalButton();
        binGrabberDeployAngle.whenReleased(new BinGrabberDeployAngle(1.0, BinGrabber.DEPLOYED_POSITION_TIMED_DEG, 300));
		SmartDashboard.putData("Bin Grabber Deploy Angle Limit", binGrabberDeployAngle);
		
		InternalButton binGrabberDeployPID = new InternalButton();
        binGrabberDeployPID.whenReleased(new BinGrabberPositionPID(BinGrabber.DEPLOYED_POSITION_DEG, BinGrabber.DEPLOYED_POSITION_DEG, 1, 1000));
		SmartDashboard.putData("Bin Grabber Deploy Position PID", binGrabberDeployPID);
		
		InternalButton binGrabberStowedPID = new InternalButton();
        binGrabberStowedPID.whenReleased(new BinGrabberPositionPID(BinGrabber.STOWED_POSITION_DEG, BinGrabber.STOWED_POSITION_DEG, 1, 1000));
		SmartDashboard.putData("Bin Grabber Stowed Position PID", binGrabberStowedPID);
		
		InternalButton binGrabberDragPID = new InternalButton();
        binGrabberDragPID.whenReleased(new BinGrabberPositionPID(BinGrabber.DRAG_BIN_POSITION_DEG, BinGrabber.DRAG_BIN_POSITION_DEG, 1, 1000));
		SmartDashboard.putData("Bin Grabber Drag Position PID", binGrabberDragPID);

		InternalButton binGrabberDeployAndGo = new InternalButton();
		binGrabberDeployAndGo.whenReleased(new BinGrabberDeployAndGo());
		SmartDashboard.putData("Bin Grabber Deploy and Go", binGrabberDeployAndGo);
		
		InternalButton binGrabberDeployAndDrive = new InternalButton();
		binGrabberDeployAndDrive.whenReleased(new BinGrabberDeployAndDrive(60, 60, 200, 2));
		SmartDashboard.putData("Bin Grabber Deploy and Drive", binGrabberDeployAndDrive);

		InternalButton drivetrainTestSpeed = new InternalButton();
		drivetrainTestSpeed.whenReleased(new DriveTrainSpeedTimeout(1, 3));
		SmartDashboard.putData("DriveTrain Test Speed", drivetrainTestSpeed);		

		InternalButton drivetrainTestVel = new InternalButton();
		drivetrainTestVel.whenReleased(new DriveTrainVelocityControl(1500, 1500, 1, 2));
		SmartDashboard.putData("DriveTrain Test Velocity PID", drivetrainTestVel);		

		InternalButton drivetrainTestPosition = new InternalButton();
		drivetrainTestPosition.whenReleased(new DriveTrainPositionControl(60, 60, 1, 4));
		SmartDashboard.putData("DriveTrain Test Position PID", drivetrainTestPosition);		

		InternalButton toteGrabberOpen = new InternalButton();
		toteGrabberOpen.whenPressed(new ToteGrabberPosition(RobotArm.ToteGrabberPosition.OPEN));
		SmartDashboard.putData("Tote Grabber Open", toteGrabberOpen);

		InternalButton toteGrabberClosed = new InternalButton();
		toteGrabberClosed.whenPressed(new ToteGrabberPosition(RobotArm.ToteGrabberPosition.CLOSED));
		SmartDashboard.putData("Tote Grabber Closed", toteGrabberClosed);

		InternalButton toteGrabberAuto = new InternalButton();
		toteGrabberAuto.whenReleased(new ToteGrabberAutoClose());
		SmartDashboard.putData("Tote Grabber Auto Close", toteGrabberAuto);
		
		InternalButton binGrabberClawOpen = new InternalButton();
		binGrabberClawOpen.whenPressed(new BinGrabberClawPosition(BinGrabber.BinGrabberState.EXTENDED));
		SmartDashboard.putData("Bin Grabber Claw Open", binGrabberClawOpen);
		
		InternalButton binGrabberClawClosed = new InternalButton();
		binGrabberClawClosed.whenPressed(new BinGrabberClawPosition(BinGrabber.BinGrabberState.RETRACTED));
		SmartDashboard.putData("Bin Grabber Claw Closed", binGrabberClawClosed);
		
		InternalButton binGrabberPivotLock = new InternalButton();
		binGrabberPivotLock.whenPressed(new BinGrabberPivotLockPosition(BinGrabber.BinGrabberState.EXTENDED));
		SmartDashboard.putData("Bin Grabber Pivot Lock", binGrabberPivotLock);
		
		InternalButton binGrabberPivotUnlock = new InternalButton();
		binGrabberPivotUnlock.whenPressed(new BinGrabberPivotLockPosition(BinGrabber.BinGrabberState.RETRACTED));
		SmartDashboard.putData("Bin Grabber Pivot Unlock", binGrabberPivotUnlock);
		
    	WaypointList waypoints0 = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
    	waypoints0.addWaypoint(RobotArm.J1_MASTER_ANGLE_DEG, RobotArm.J2_MASTER_ANGLE_DEG, RobotArm.J3_MASTER_ANGLE_DEG, RobotArm.J4_MASTER_ANGLE_DEG);
    	waypoints0.addWaypoint(0, 0, 0, 0);
    	RobotArmCommandList commandList0 = new RobotArmCommandList();
		commandList0.add(new RobotArmMotionProfilePath(waypoints0));

		InternalButton motionProfileStartMasterTo0 = new InternalButton();
		motionProfileStartMasterTo0.whenPressed(new RobotArmMotionProfileStart(commandList0));
		SmartDashboard.putData("Motion Profile Start Master to 0", motionProfileStartMasterTo0);
		
		// J1 motion Profile
    	WaypointList waypointsJ1A = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
    	waypointsJ1A.addWaypoint(0, 0, -60, 0);
    	waypointsJ1A.addWaypoint(-140, 0, -60, 0);
    	RobotArmCommandList commandListJ1A = new RobotArmCommandList();
		commandListJ1A.add(new RobotArmMotionProfilePath(waypointsJ1A));

		InternalButton motionProfileJ1A = new InternalButton();
		motionProfileJ1A.whenPressed(new RobotArmMotionProfileStart(commandListJ1A));
		SmartDashboard.putData("Motion Profile Start J1 A", motionProfileJ1A);
		
    	WaypointList waypointsJ1B = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
    	waypointsJ1B.addWaypoint(-140, 0, -60, 0);
    	waypointsJ1B.addWaypoint(40, 0, -60, 0);
    	RobotArmCommandList commandListJ1B = new RobotArmCommandList();
		commandListJ1B.add(new RobotArmMotionProfilePath(waypointsJ1B));

		InternalButton motionProfileJ1B = new InternalButton();
		motionProfileJ1B.whenPressed(new RobotArmMotionProfileStart(commandListJ1B));
		SmartDashboard.putData("Motion Profile Start J1 B", motionProfileJ1B);

    	WaypointList waypointsJ1C = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
    	waypointsJ1C.addWaypoint(40, 0, -60, 0);
    	waypointsJ1C.addWaypoint(0, 0, -60, 0);
    	RobotArmCommandList commandListJ1C = new RobotArmCommandList();
		commandListJ1C.add(new RobotArmMotionProfilePath(waypointsJ1C));

		InternalButton motionProfileJ1C = new InternalButton();
		motionProfileJ1C.whenPressed(new RobotArmMotionProfileStart(commandListJ1C));
		SmartDashboard.putData("Motion Profile Start J1 C", motionProfileJ1C);

		// J2 motion Profile
    	WaypointList waypointsJ2A = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
    	waypointsJ2A.addWaypoint(RobotArm.J1_MASTER_ANGLE_DEG, RobotArm.J2_MASTER_ANGLE_DEG, RobotArm.J3_MASTER_ANGLE_DEG, RobotArm.J4_MASTER_ANGLE_DEG);
    	waypointsJ2A.addWaypoint(0, 80, -80, 0);
    	RobotArmCommandList commandListJ2A = new RobotArmCommandList();
		commandListJ2A.add(new RobotArmMotionProfilePath(waypointsJ2A));

		InternalButton motionProfileJ2A = new InternalButton();
		motionProfileJ2A.whenPressed(new RobotArmMotionProfileStart(commandListJ2A));
		SmartDashboard.putData("Motion Profile Start J2 A", motionProfileJ2A);
		
    	WaypointList waypointsJ2B = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
    	waypointsJ2B.addWaypoint(0, 80, -80, 0);
    	waypointsJ2B.addWaypoint(0, 0, 0, 0);
    	RobotArmCommandList commandListJ2B = new RobotArmCommandList();
		commandListJ2B.add(new RobotArmMotionProfilePath(waypointsJ2B));

		InternalButton motionProfileJ2B = new InternalButton();
		motionProfileJ2B.whenPressed(new RobotArmMotionProfileStart(commandListJ2B));
		SmartDashboard.putData("Motion Profile Start J2 B", motionProfileJ2B);

    	WaypointList waypointsJ2C = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
    	waypointsJ2C.addWaypoint(0, 0, 0, 0);
    	waypointsJ2C.addWaypoint(0, 80, -80, 0);
    	RobotArmCommandList commandListJ2C = new RobotArmCommandList();
		commandListJ2C.add(new RobotArmMotionProfilePath(waypointsJ2C));

		InternalButton motionProfileJ2C = new InternalButton();
		motionProfileJ2C.whenPressed(new RobotArmMotionProfileStart(commandListJ2C));
		SmartDashboard.putData("Motion Profile Start J2 C", motionProfileJ2C);		
		
		// J3 motion Profile
    	WaypointList waypointsJ3A = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
    	waypointsJ3A.addWaypoint(0, 0, 0, 0);
    	waypointsJ3A.addWaypoint(0, 0, -45, 0);
    	RobotArmCommandList commandListJ3A = new RobotArmCommandList();
		commandListJ3A.add(new RobotArmMotionProfilePath(waypointsJ3A));

		InternalButton motionProfileJ3A = new InternalButton();
		motionProfileJ3A.whenPressed(new RobotArmMotionProfileStart(commandListJ3A));
		SmartDashboard.putData("Motion Profile Start J3 A", motionProfileJ3A);
		
    	WaypointList waypointsJ3B = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
    	waypointsJ3B.addWaypoint(0, 0, -45, 0);
    	waypointsJ3B.addWaypoint(0, 0, 0, 0);
    	RobotArmCommandList commandListJ3B = new RobotArmCommandList();
		commandListJ3B.add(new RobotArmMotionProfilePath(waypointsJ3B));

		InternalButton motionProfileJ3B = new InternalButton();
		motionProfileJ3B.whenPressed(new RobotArmMotionProfileStart(commandListJ3B));
		SmartDashboard.putData("Motion Profile Start J3 B", motionProfileJ3B);

    	WaypointList waypointsJ3C = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
    	waypointsJ3C.addWaypoint(0, 0, 0, 0);
    	waypointsJ3C.addWaypoint(0, 0, 0, 0);
    	RobotArmCommandList commandListJ3C = new RobotArmCommandList();
		commandListJ3C.add(new RobotArmMotionProfilePath(waypointsJ3C));

		InternalButton motionProfileJ3C = new InternalButton();
		motionProfileJ3C.whenPressed(new RobotArmMotionProfileStart(commandListJ3C));
		SmartDashboard.putData("Motion Profile Start J3 C", motionProfileJ3C);		
		
		// Master to human station Profile
    	WaypointList waypointsM2H = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
    	waypointsM2H.addWaypoint(RobotArm.J1_MASTER_ANGLE_DEG, RobotArm.J2_MASTER_ANGLE_DEG, RobotArm.J3_MASTER_ANGLE_DEG, RobotArm.J4_MASTER_ANGLE_DEG);
    	waypointsM2H.addWaypoint(0, 89,  -101.7,   0);
    	waypointsM2H.addWaypoint(-127, 89,  -101.7,   0);
    	waypointsM2H.addWaypoint(-127, 79.8, -42.69, 0);
    	RobotArmCommandList commandListM2H = new RobotArmCommandList();
    	commandListM2H.add(new RobotArmMotionProfilePath(waypointsM2H));

		InternalButton motionProfileM2H = new InternalButton();
		motionProfileM2H.whenPressed(new RobotArmMotionProfileStart(commandListM2H));
		SmartDashboard.putData("Motion Profile Master To Human", motionProfileM2H);
		
		// Human station to stack Profile
    	WaypointList waypointsH2S = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
    	waypointsH2S.addWaypoint(-127, 79.8, -42.69, 0);
    	waypointsH2S.addWaypoint(-127, 68,  -101.7,   0);
    	waypointsH2S.addWaypoint(  55, 68,  -101.7, -55);
    	waypointsH2S.addWaypoint(  55, 94.7, -65.6, -55);
    	RobotArmCommandList commandListH2S = new RobotArmCommandList();
    	commandListH2S.add(new RobotArmMotionProfilePath(waypointsH2S));

		InternalButton motionProfileH2S = new InternalButton();
		motionProfileH2S.whenPressed(new RobotArmMotionProfileStart(commandListH2S));
		SmartDashboard.putData("Motion Profile Human To Stack", motionProfileH2S);
		
		// Pause resume
		InternalButton motionProfilePause = new InternalButton();
		motionProfilePause.whenPressed(new RobotArmMotionProfilePause());
		SmartDashboard.putData("Motion Profile Pause", motionProfilePause);
		
		InternalButton motionProfileResume = new InternalButton();
		motionProfileResume.whenPressed(new RobotArmMotionProfileResume());
		SmartDashboard.putData("Motion Profile Resume", motionProfileResume);
		
		InternalButton driveTrainHoldOn = new InternalButton();
		driveTrainHoldOn.whenPressed(new DriveTrainPositionHoldOn());
		SmartDashboard.putData("Drivetrain Hold On", driveTrainHoldOn);
		
		InternalButton driveTrainHoldOff = new InternalButton();
		driveTrainHoldOff.whenPressed(new DriveTrainPositionHoldOff());
		SmartDashboard.putData("Drivetrain Hold Off", driveTrainHoldOff);
	}
	
	public static OI getInstance() {
		if(instance == null) {
			instance = new OI();
		}
		return instance;
	}
	
    public Joystick getJoystick1() {
        return m_joystick1;
    }
    
    public Joystick getJoystick2() {
        return m_joystick2;
    }
    
	public XboxController getXBoxController1() {
        return m_xboxController1;
    }

	public XboxController getXBoxController2() {
        return m_xboxController2;
    }
}

