package edu.rhhs.frc;

import edu.rhhs.frc.commands.CommandBase;
import edu.rhhs.frc.commands.RunMotor;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	private static OI instance = null;
	
	//INSTANTIATE THINGS THAT WE USE THROUGHOUT ROBOT
	private OI() {
		
	}
	
	public static OI getInstance() {
		if(instance == null) instance = new OI();
		return instance;
	}
}

