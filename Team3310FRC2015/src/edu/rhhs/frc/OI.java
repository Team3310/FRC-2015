package edu.rhhs.frc;

import edu.rhhs.frc.commands.BinGrabberClawPosition;
import edu.rhhs.frc.commands.BinGrabberDeployAngle;
import edu.rhhs.frc.commands.BinGrabberDeployTimed;
import edu.rhhs.frc.commands.BinGrabberPivotLockPosition;
import edu.rhhs.frc.commands.BinGrabberPositionPID;
import edu.rhhs.frc.commands.BinGrabberSetSpeed;
import edu.rhhs.frc.commands.TalonTestVelocityControl;
import edu.rhhs.frc.commands.ToteGrabberAutoClose;
import edu.rhhs.frc.commands.ToteGrabberPosition;
import edu.rhhs.frc.controller.XboxController;
import edu.rhhs.frc.subsystems.BinGrabber;
import edu.rhhs.frc.subsystems.RobotArm;
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

	private XboxController m_xboxController;
	private Joystick m_joystick1;
    private Joystick m_joystick2;
	
	private OI() {
		m_xboxController = new XboxController(RobotMap.XBOX_USB_ID);
        m_joystick1 = new Joystick(RobotMap.JOYSTICK_1_USB_ID);
        m_joystick2 = new Joystick(RobotMap.JOYSTICK_2_USB_ID);
	    
        JoystickButton moveBinGrabberUp = new JoystickButton(m_xboxController.getJoyStick(), XboxController.A_BUTTON);
        moveBinGrabberUp.whenPressed(new BinGrabberSetSpeed(1.0));
        moveBinGrabberUp.whenReleased(new BinGrabberSetSpeed(0));
        
        JoystickButton moveBinGrabberDown = new JoystickButton(m_xboxController.getJoyStick(), XboxController.Y_BUTTON);
        moveBinGrabberDown.whenPressed(new BinGrabberSetSpeed(-0.5));
        moveBinGrabberDown.whenReleased(new BinGrabberSetSpeed(0));
	    
        InternalButton binGrabberDeployTimed = new InternalButton();
        binGrabberDeployTimed.whenReleased(new BinGrabberDeployTimed(1, 100));
		SmartDashboard.putData("Bin Grabber Deploy Timed", binGrabberDeployTimed);
		
        InternalButton binGrabberDeployAngle = new InternalButton();
        binGrabberDeployAngle.whenReleased(new BinGrabberDeployAngle(1.0, BinGrabber.DEPLOYED_POSITION_TIMED_DEG, 300));
		SmartDashboard.putData("Bin Grabber Deploy Angle", binGrabberDeployAngle);
		
		InternalButton binGrabberDeployPID = new InternalButton();
        binGrabberDeployPID.whenReleased(new BinGrabberPositionPID(BinGrabber.DEPLOYED_POSITION_DEG, BinGrabber.DEPLOYED_POSITION_DEG, 1, 4));
		SmartDashboard.putData("Talon Test Position_Deployed", binGrabberDeployPID);
		
		InternalButton binGrabberStowedPID = new InternalButton();
        binGrabberStowedPID.whenReleased(new BinGrabberPositionPID(BinGrabber.STOWED_POSITION_DEG, BinGrabber.STOWED_POSITION_DEG, 1, 4));
		SmartDashboard.putData("Bin Grabber Position_Stowed", binGrabberStowedPID);
		
		InternalButton binGrabberDragPID = new InternalButton();
        binGrabberDragPID.whenReleased(new BinGrabberPositionPID(BinGrabber.DRAG_BIN_POSITION_DEG, BinGrabber.DRAG_BIN_POSITION_DEG, 1, 4));
		SmartDashboard.putData("Bin Grabber Position_Drag", binGrabberDragPID);

		InternalButton talonTestVel = new InternalButton();
        talonTestVel.whenReleased(new TalonTestVelocityControl(1000,1000, 1, 4));
		SmartDashboard.putData("Talon Test Velocity", talonTestVel);		

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
    
	public XboxController getXBoxController() {
        return m_xboxController;
    }
}

