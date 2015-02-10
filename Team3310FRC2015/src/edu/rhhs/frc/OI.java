package edu.rhhs.frc;

import edu.rhhs.frc.commands.FireBinGrabber;
import edu.rhhs.frc.commands.MoveBinGrabber;
import edu.rhhs.frc.commands.TalonTestVelocityControl;
import edu.rhhs.frc.controller.XboxController;
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
	    
	    JoystickButton fireBinGrabber = new JoystickButton(m_xboxController.getJoyStick(), XboxController.B_BUTTON);
        fireBinGrabber.whenPressed(new FireBinGrabber(.4, 10));
        
        JoystickButton moveBinGrabberUp = new JoystickButton(m_xboxController.getJoyStick(), XboxController.A_BUTTON);
        moveBinGrabberUp.whenPressed(new MoveBinGrabber(1.0));
        moveBinGrabberUp.whenReleased(new MoveBinGrabber(0));
        
        JoystickButton moveBinGrabberDown = new JoystickButton(m_xboxController.getJoyStick(), XboxController.Y_BUTTON);
        moveBinGrabberDown.whenPressed(new MoveBinGrabber(-0.5));
        moveBinGrabberDown.whenReleased(new MoveBinGrabber(0));
	    
        InternalButton talonTest = new InternalButton();
        talonTest.whenReleased(new TalonTestVelocityControl(1000,1000, 1, 4));
		SmartDashboard.putData("Talon Test", talonTest);
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

