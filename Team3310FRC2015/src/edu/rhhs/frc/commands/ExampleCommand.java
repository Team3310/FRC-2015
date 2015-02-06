
package edu.rhhs.frc.commands;

import edu.wpi.first.wpilibj.command.Command;

import edu.rhhs.frc.Robot;

/**
 *
 */
public class ExampleCommand extends Command {

	private double speed;
	
    public ExampleCommand(double speed) {
    	this.speed = speed;
        // Use requires() here to declare subsystem dependencies
        requires(Robot.exampleSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.exampleSubsystem.setSpeed(speed);
    	this.setTimeout(8);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut() || Robot.exampleSubsystem.isDepressed();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.exampleSubsystem.setSpeed(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
