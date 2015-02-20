
package edu.rhhs.frc;

import edu.rhhs.frc.subsystems.BinGrabber;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.RobotArm;
import edu.rhhs.frc.utility.CANTalonEncoderPID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain extends IterativeRobot 
{
	public static final BinGrabber binGrabber = new BinGrabber();
	public static final DriveTrain driveTrain = new DriveTrain();
	public static final RobotArm robotArm = new RobotArm();

    private Command m_autonomousCommand;
    private SendableChooser m_autonomousChooser;
    private SendableChooser m_driveModeChooser;
    private SendableChooser m_robotArmControlModeChooser;

    private long m_loopTime;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	 
        // instantiate the command used for the autonomous period
        //autonomousCommand = new ExampleCommand(0.5);
        m_loopTime = System.nanoTime();
        m_driveModeChooser = new SendableChooser();
    	m_driveModeChooser.addObject ("XBox Arcade Left", 	new Integer(DriveTrain.CONTROLLER_XBOX_ARCADE_LEFT));
    	m_driveModeChooser.addObject ("XBox Arcade Right", 	new Integer(DriveTrain.CONTROLLER_XBOX_ARCADE_RIGHT));
    	m_driveModeChooser.addDefault("XBox Cheesy",		new Integer(DriveTrain.CONTROLLER_XBOX_CHEESY));
    	m_driveModeChooser.addObject ("Joystick Arcade", 	new Integer(DriveTrain.CONTROLLER_JOYSTICK_ARCADE));
    	m_driveModeChooser.addObject ("Joystick Cheesy", 	new Integer(DriveTrain.CONTROLLER_JOYSTICK_CHEESY));
    	m_driveModeChooser.addObject ("Joystick Tank",   	new Integer(DriveTrain.CONTROLLER_JOYSTICK_TANK));
        SmartDashboard.putData("Drive Mode", m_driveModeChooser);            

        m_robotArmControlModeChooser = new SendableChooser();
    	m_robotArmControlModeChooser.addObject ("VBus Only", 				CANTalonEncoderPID.ControlMode.PERCENT_VBUS);
    	m_robotArmControlModeChooser.addObject ("VBus Position Hold", 		CANTalonEncoderPID.ControlMode.VBUS_POSITION_HOLD);
    	m_robotArmControlModeChooser.addObject ("Position Absolute", 		CANTalonEncoderPID.ControlMode.POSITION);
    	m_robotArmControlModeChooser.addDefault("Position Incremental", 	CANTalonEncoderPID.ControlMode.POSITION_INCREMENTAL);
    	m_robotArmControlModeChooser.addObject ("Velocity Position Hold", 	CANTalonEncoderPID.ControlMode.VELOCITY_POSITION_HOLD);
        SmartDashboard.putData("Robot Arm Mode", m_robotArmControlModeChooser);
   	
        updateStatus();
        System.out.println("\nRobot code successfully enabled!");
    }
	
	public void disabledPeriodic() {
		// This is for an issue when the robotRIO boots up sometimes Talons don't get enabled
		driveTrain.keepAlive();
		binGrabber.keepAlive();
		robotArm.keepAlive();
		Scheduler.getInstance().run();
        updateStatus();
	}

    public void autonomousInit() {
        // schedule the autonomous command (example)
        if (m_autonomousCommand != null) { 
        	m_autonomousCommand.start();
        }
        updateStatus();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        updateStatus();
   }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (m_autonomousCommand != null) {
        	m_autonomousCommand.cancel();
        }
        driveTrain.setJoystickControllerMode(((Integer)m_driveModeChooser.getSelected()).intValue());
        robotArm.setControlMode((CANTalonEncoderPID.ControlMode)m_robotArmControlModeChooser.getSelected());
        updateStatus();
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){
        updateStatus();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        updateStatus();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
        updateStatus();
    }
    
    public void updateStatus() {
    	try {
    		long currentTime = System.nanoTime();
    		SmartDashboard.putNumber("Main loop time (ms)", (currentTime - m_loopTime) / 1000000.0);
    		m_loopTime = currentTime;
    		driveTrain.updateStatus();
    		binGrabber.updateStatus();
    		robotArm.updateStatus();
    	}
    	catch (Exception e) {
    		// Do nothing... just don't want to stop the robot
    	}
    }
}
