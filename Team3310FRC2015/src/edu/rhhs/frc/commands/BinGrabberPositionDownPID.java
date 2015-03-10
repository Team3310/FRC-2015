package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.BinGrabber;
import edu.wpi.first.wpilibj.CANTalon.StatusFrameRate;
import edu.wpi.first.wpilibj.command.Command;

public class BinGrabberPositionDownPID extends Command 
{
	private double leftTargetDeg;
	private double rightTargetDeg;

    public BinGrabberPositionDownPID(double leftPositionDeg, double rightPositionDeg) {
        // Use requires() here to declare subsystem dependencies
    	this.leftTargetDeg = leftPositionDeg;
    	this.rightTargetDeg = rightPositionDeg;
        requires(RobotMain.binGrabber);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
		RobotMain.binGrabber.setStatusFrameRate(StatusFrameRate.AnalogTempVbat, 10);
    	RobotMain.binGrabber.startPositionDownPID(leftTargetDeg, rightTargetDeg, 0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
		return RobotMain.binGrabber.getLeftPositionDeg() > BinGrabber.DEPLOYED_POSITION_DRIVETRAIN_ENGAGE_DEG && 
				RobotMain.binGrabber.getRightPositionDeg() >  BinGrabber.DEPLOYED_POSITION_DRIVETRAIN_ENGAGE_DEG;
    }

    // Called once after isFinished returns true
    protected void end() {
		RobotMain.binGrabber.setStatusFrameRate(StatusFrameRate.AnalogTempVbat, 100);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}