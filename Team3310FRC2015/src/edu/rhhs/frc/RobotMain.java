
package edu.rhhs.frc;

import com.kauailabs.nav6.frc.IMUAdvanced;

import edu.rhhs.frc.commands.BinGrabberDeployAndGo;
import edu.rhhs.frc.commands.BinGrabberDeployAndGoPID;
import edu.rhhs.frc.subsystems.BinGrabber;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.RobotArm;
import edu.rhhs.frc.utility.CANTalonEncoderPID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CANTalon.StatusFrameRate;
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

    private SerialPort serial_port;
    //IMU imu;  // Alternatively, use IMUAdvanced for advanced features
    public IMUAdvanced imu;
    private boolean first_iteration;
    
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
   	
        m_autonomousChooser = new SendableChooser();
        m_autonomousChooser.addDefault("BinGrabberDeployAndGo", 	new BinGrabberDeployAndGo());
        m_autonomousChooser.addObject ("BinGrabberDeployAndGoPID", 	new BinGrabberDeployAndGoPID());
        SmartDashboard.putData("Autonomous Mode", m_autonomousChooser);

        try {
	    	serial_port = new SerialPort(57600,SerialPort.Port.kMXP);
			
			// You can add a second parameter to modify the 
			// update rate (in hz) from 4 to 100.  The default is 100.
			// If you need to minimize CPU load, you can set it to a
			// lower value, as shown here, depending upon your needs.
			
			// You can also use the IMUAdvanced class for advanced
			// features.
			
			byte update_rate_hz = 50;
			//imu = new IMU(serial_port,update_rate_hz);
			imu = new IMUAdvanced(serial_port,update_rate_hz);
    	} catch( Exception ex ) {
    		
    	}
        first_iteration = true;
 
        updateStatus();
        System.out.println("\nRobot code successfully enabled!");
    }
	
	public void disabledPeriodic() {
        boolean is_calibrating = imu.isCalibrating();
        if ( first_iteration && !is_calibrating ) {
            Timer.delay( 0.3 );
            imu.zeroYaw();
            first_iteration = false;
        	RobotMain.binGrabber.setStatusFrameRate(StatusFrameRate.AnalogTempVbat, 10);
        }

        // instantiate the command used for the autonomous period
    	m_autonomousCommand = (Command)m_autonomousChooser.getSelected();
    	
		// This is for an issue when the robotRIO boots up sometimes Talons don't get enabled
		driveTrain.keepAlive();
		binGrabber.keepAlive();
		robotArm.keepAlive();
		Scheduler.getInstance().run();
        updateStatus();
	}

    public void autonomousInit() {
        m_autonomousCommand.start();
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
        driveTrain.teleopInit();
        binGrabber.teleopInit();
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
    
    public IMUAdvanced getIMU() {
    	return imu;
    }
    
    public void updateStatus() {
    	try {
    		long currentTime = System.nanoTime();
    		SmartDashboard.putNumber("Main loop time (ms)", (currentTime - m_loopTime) / 1000000.0);
    		SmartDashboard.putNumber("IMU Yaw (deg)", imu.getYaw());
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
