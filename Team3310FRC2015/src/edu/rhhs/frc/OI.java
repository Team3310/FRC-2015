package edu.rhhs.frc;

import edu.rhhs.frc.commands.BinGrabberClawPosition;
import edu.rhhs.frc.commands.BinGrabberDeployAndGo;
import edu.rhhs.frc.commands.BinGrabberDeployAngle;
import edu.rhhs.frc.commands.BinGrabberDeployTimed;
import edu.rhhs.frc.commands.BinGrabberPivotLockPosition;
import edu.rhhs.frc.commands.BinGrabberPositionPID;
import edu.rhhs.frc.commands.BinGrabberSetSpeed;
import edu.rhhs.frc.commands.DriveTrainSpeedTimeout;
import edu.rhhs.frc.commands.DriveTrainVelocityControl;
import edu.rhhs.frc.commands.RobotArmMotionProfilePause;
import edu.rhhs.frc.commands.RobotArmMotionProfileResume;
import edu.rhhs.frc.commands.RobotArmMotionProfileStart;
import edu.rhhs.frc.commands.ToteGrabberAutoClose;
import edu.rhhs.frc.commands.ToteGrabberPosition;
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

		InternalButton drivetrainTestSpeed = new InternalButton();
		drivetrainTestSpeed.whenReleased(new DriveTrainSpeedTimeout(1, 3));
		SmartDashboard.putData("DriveTrain Test Speed", drivetrainTestSpeed);		

		InternalButton drivetrainTestVel = new InternalButton();
		drivetrainTestVel.whenReleased(new DriveTrainVelocityControl(1500, 1500, 1, 2));
		SmartDashboard.putData("DriveTrain Test Velocity PID", drivetrainTestVel);		

		InternalButton drivetrainTestPosition = new InternalButton();
		drivetrainTestPosition.whenReleased(new DriveTrainVelocityControl(60, 60, 1, 4));
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
		
    	WaypointList waypoints1 = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
    	waypoints1.addWaypoint(0, 0, 0, 0);
    	waypoints1.addWaypoint(-45, 0, 0, 0);

		InternalButton motionProfileStart045 = new InternalButton();
		motionProfileStart045.whenPressed(new RobotArmMotionProfileStart(waypoints1));
		SmartDashboard.putData("Motion Profile Start 0 to -45", motionProfileStart045);
		
    	WaypointList waypoints2 = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
    	waypoints2.addWaypoint(-45, 0, 0, 0);
    	waypoints2.addWaypoint(45, 0, 0, 0);

		InternalButton motionProfileStart4545 = new InternalButton();
		motionProfileStart4545.whenPressed(new RobotArmMotionProfileStart(waypoints2));
		SmartDashboard.putData("Motion Profile Start -45 to 45", motionProfileStart4545);

    	WaypointList waypoints3 = new WaypointList(MotionProfile.ProfileMode.JointInputJointMotion);
    	waypoints3.addWaypoint(45, 0, 0, 0);
    	waypoints3.addWaypoint(0, 0, 0, 0);

		InternalButton motionProfileStart450 = new InternalButton();
		motionProfileStart450.whenPressed(new RobotArmMotionProfileStart(waypoints3));
		SmartDashboard.putData("Motion Profile Start 45 to 0", motionProfileStart450);

		InternalButton motionProfilePause = new InternalButton();
		motionProfilePause.whenPressed(new RobotArmMotionProfilePause());
		SmartDashboard.putData("Motion Profile Pause", motionProfilePause);
		
		InternalButton motionProfileResume = new InternalButton();
		motionProfileResume.whenPressed(new RobotArmMotionProfileResume());
		SmartDashboard.putData("Motion Profile Resume", motionProfileResume);
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

