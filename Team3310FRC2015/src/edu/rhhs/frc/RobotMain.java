package edu.rhhs.frc;

import edu.rhhs.frc.commands.AutonGet3TotesTip3BinsNew;
import edu.rhhs.frc.commands.AutonTurnToHumanPosition;
import edu.rhhs.frc.commands.BinGrabberDeployAndGoPID;
import edu.rhhs.frc.commands.DriveTrainPositionControl;
import edu.rhhs.frc.commands.RobotArmMotionProfileStart;
import edu.rhhs.frc.commands.robotarm.HumanLoadCommandListGenerator;
import edu.rhhs.frc.commands.robotarm.HumanLoadCommandListGenerator.StackPriority;
import edu.rhhs.frc.commands.robotarm.HumanLoadCommandListGeneratorOptimal;
import edu.rhhs.frc.commands.robotarm.RobotArmCommandList;
import edu.rhhs.frc.commands.robotarm.RobotArmMotionProfileCurrentToPath;
import edu.rhhs.frc.subsystems.BinGrabber;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.RobotArm;
import edu.rhhs.frc.utility.CANTalonEncoderPID;
import edu.rhhs.frc.utility.motionprofile.MotionProfile;
import edu.rhhs.frc.utility.motionprofile.WaypointList;
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
	public static final HumanLoadCommandListGeneratorOptimal commandListGenerator = new HumanLoadCommandListGeneratorOptimal();

	private Command m_autonomousCommand;
	private SendableChooser m_autonomousChooser;
//	private SendableChooser m_driveModeChooser;
	private SendableChooser m_robotArmControlModeChooser;
	private SendableChooser m_numStacksChooser;
	private SendableChooser m_numTotesPerStackChooser;
	private SendableChooser m_stackPriorityChooser;

	private long m_loopTime;
	private Integer numStacks = null;
	private Integer numTotesPerStack = null;
	private StackPriority stackPriority;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		try {
			m_loopTime = System.nanoTime();

			commandListGenerator.setStackPriority(StackPriority.VERTICAL);

//			m_driveModeChooser = new SendableChooser();
//			m_driveModeChooser.addObject ("XBox Arcade Left", 	new Integer(DriveTrain.CONTROLLER_XBOX_ARCADE_LEFT));
//			m_driveModeChooser.addObject ("XBox Arcade Right", 	new Integer(DriveTrain.CONTROLLER_XBOX_ARCADE_RIGHT));
//			m_driveModeChooser.addDefault("XBox Cheesy",		new Integer(DriveTrain.CONTROLLER_XBOX_CHEESY));
//			m_driveModeChooser.addObject ("Joystick Arcade", 	new Integer(DriveTrain.CONTROLLER_JOYSTICK_ARCADE));
//			m_driveModeChooser.addObject ("Joystick Cheesy", 	new Integer(DriveTrain.CONTROLLER_JOYSTICK_CHEESY));
//			m_driveModeChooser.addObject ("Joystick Tank",   	new Integer(DriveTrain.CONTROLLER_JOYSTICK_TANK));
//			SmartDashboard.putData("Drive Mode", m_driveModeChooser);            

			m_stackPriorityChooser = new SendableChooser();
			m_stackPriorityChooser.addDefault("Vertical Priority", StackPriority.VERTICAL);
			m_stackPriorityChooser.addObject ("Horizontal Priority", StackPriority.HORIZONTAL);
			SmartDashboard.putData("Stack Priority Chooser", m_stackPriorityChooser);

			m_numStacksChooser = new SendableChooser();
			m_numStacksChooser.addObject ("1 Stack", 	new Integer(1));
			m_numStacksChooser.addObject ("2 Stacks", 	new Integer(2));
			m_numStacksChooser.addDefault("3 Stacks", 	new Integer(3));
			SmartDashboard.putData("Num Stacks Chooser", m_numStacksChooser);

			m_numTotesPerStackChooser = new SendableChooser();
			m_numTotesPerStackChooser.addObject ("2 Totes", new Integer(2));
			m_numTotesPerStackChooser.addObject ("4 Totes", new Integer(4));
			m_numTotesPerStackChooser.addDefault("6 Totes", new Integer(6));
			SmartDashboard.putData("Num Totes Per Stack Chooser", m_numTotesPerStackChooser);

			m_robotArmControlModeChooser = new SendableChooser();
			m_robotArmControlModeChooser.addObject ("VBus Only", 				CANTalonEncoderPID.ControlMode.PERCENT_VBUS);
			m_robotArmControlModeChooser.addObject ("VBus Position Hold", 		CANTalonEncoderPID.ControlMode.VBUS_POSITION_HOLD);
			m_robotArmControlModeChooser.addObject ("Position Absolute", 		CANTalonEncoderPID.ControlMode.POSITION);
			m_robotArmControlModeChooser.addDefault("Position Incremental", 	CANTalonEncoderPID.ControlMode.POSITION_INCREMENTAL);
			m_robotArmControlModeChooser.addObject ("Velocity Position Hold", 	CANTalonEncoderPID.ControlMode.VELOCITY_POSITION_HOLD);
			SmartDashboard.putData("Robot Arm Mode", m_robotArmControlModeChooser);

			WaypointList waypointsCurrentToHome = new WaypointList(MotionProfile.ProfileMode.CartesianInputJointMotion);
			waypointsCurrentToHome.addWaypoint(HumanLoadCommandListGenerator.DEFAULT_HOME_COORD);
			RobotArmCommandList commandListCurrentToHome = new RobotArmCommandList();
			commandListCurrentToHome.add(new RobotArmMotionProfileCurrentToPath(waypointsCurrentToHome));

			m_autonomousChooser = new SendableChooser();
			m_autonomousChooser.addDefault("BinGrabberDeployAndGoPID", 	new BinGrabberDeployAndGoPID());
			m_autonomousChooser.addObject ("Move Arm To Home", 	new RobotArmMotionProfileStart(commandListCurrentToHome));
			m_autonomousChooser.addObject ("Do nothing", null);
			m_autonomousChooser.addObject ("Drive forward 24", new DriveTrainPositionControl(36, 36, true, 36));
			m_autonomousChooser.addObject ("3 tote stack knock bins over", new AutonGet3TotesTip3BinsNew());
			m_autonomousChooser.addObject ("Turn to Human Position", new AutonTurnToHumanPosition());
			SmartDashboard.putData("Autonomous Mode", m_autonomousChooser);
		} catch( Exception ex ) {

		}

		updateStatus();
	}

	public void disabledPeriodic() {
		// Calibrate the IMU
		driveTrain.calibrateIMU();

		// Update the command list generator
		numStacks = (Integer)m_numStacksChooser.getSelected();
		numTotesPerStack = (Integer)m_numTotesPerStackChooser.getSelected();
		stackPriority = (StackPriority)m_stackPriorityChooser.getSelected();

		// Get the command used for the autonomous period
		m_autonomousCommand = (Command)m_autonomousChooser.getSelected();

		// This is for an issue when the robotRIO boots up sometimes Talons don't get enabled
		driveTrain.keepAlive();
		binGrabber.keepAlive();
		robotArm.keepAlive();

		Scheduler.getInstance().run();
		updateStatus();
	}

	public void autonomousInit() {
		if (m_autonomousCommand != null) {
			m_autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	public void teleopInit() {
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
		binGrabber.teleopInit();
		driveTrain.teleopInit();
		robotArm.teleopInit();
//		driveTrain.setJoystickControllerMode(((Integer)m_driveModeChooser.getSelected()).intValue());
		driveTrain.setJoystickControllerMode(DriveTrain.CONTROLLER_JOYSTICK_CHEESY);
		robotArm.setControlMode((CANTalonEncoderPID.ControlMode)m_robotArmControlModeChooser.getSelected());
		commandListGenerator.setNumStacks(numStacks);
		commandListGenerator.setNumTotesPerStack(numTotesPerStack);
		commandListGenerator.calculate();
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
			SmartDashboard.putNumber("Num Stacks Verify", numStacks);
			SmartDashboard.putNumber("Num Totes Per Stack Verify", numTotesPerStack);
			SmartDashboard.putString("Stack Priority Verify", stackPriority == StackPriority.HORIZONTAL ? "Horizontal" : "Vertical");
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