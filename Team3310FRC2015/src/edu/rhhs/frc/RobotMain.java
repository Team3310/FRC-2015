
package edu.rhhs.frc;

import edu.rhhs.frc.subsystems.BinGrabber;
import edu.rhhs.frc.subsystems.DriveTrain;
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

    private Command autonomousCommand;
    private SendableChooser driveModeChooser;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	 
        // instantiate the command used for the autonomous period
        //autonomousCommand = new ExampleCommand(0.5);
    	driveModeChooser = new SendableChooser();
    	driveModeChooser.addObject("XBox Arcade Left", new Integer(DriveTrain.CONTROLLER_XBOX_ARCADE_LEFT));
    	driveModeChooser.addObject("XBox Arcade Right", new Integer(DriveTrain.CONTROLLER_XBOX_ARCADE_RIGHT));
    	driveModeChooser.addDefault("XBox Cheesy", new Integer(DriveTrain.CONTROLLER_XBOX_CHEESY));
    	driveModeChooser.addObject("Joystick Arcade", new Integer(DriveTrain.CONTROLLER_JOYSTICK_ARCADE));
    	driveModeChooser.addObject("Joystick Cheesy", new Integer(DriveTrain.CONTROLLER_JOYSTICK_CHEESY));
    	driveModeChooser.addObject("Joystick Tank", new Integer(DriveTrain.CONTROLLER_JOYSTICK_TANK));
        SmartDashboard.putData("Drive Mode", driveModeChooser);
        
        System.out.println("\nRobot code successfully enabled!");
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

    public void autonomousInit() {
        // schedule the autonomous command (example)
        if (autonomousCommand != null) { 
        	autonomousCommand.start();
        }
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) {
        	autonomousCommand.cancel();
        }
        driveTrain.setDriveMode(((Integer)driveModeChooser.getSelected()).intValue());
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
