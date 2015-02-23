package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.BinGrabber;
import edu.wpi.first.wpilibj.CANTalon.StatusFrameRate;

public class BinGrabberDeployAndDrive extends ExtraTimeoutCommand 
{
	private double leftTargetInches;
	private double rightTargetInches;
	private double errorInches;
	private double timeoutSeconds;

    public BinGrabberDeployAndDrive(double leftPositionInches, double rightPositionInches, double errorInches, double timeoutSeconds) {
        // Use requires() here to declare subsystem dependencies
    	this.leftTargetInches = leftPositionInches;
    	this.rightTargetInches = rightPositionInches;
    	this.errorInches = errorInches;
    	this.timeoutSeconds = timeoutSeconds;
        requires(RobotMain.driveTrain);
        requires(RobotMain.binGrabber);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	RobotMain.binGrabber.setStatusFrameRate(StatusFrameRate.AnalogTempVbat, 10);
    	RobotMain.binGrabber.setSpeed(1, 1);
    	setTimeout(1.0);
    	startExtraTimeout(timeoutSeconds);
    	RobotMain.driveTrain.startPIDPosition(leftTargetInches, rightTargetInches, errorInches);
    	//setTimeout(timeoutSeconds);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (RobotMain.binGrabber.getLeftPositionDeg() > BinGrabber.DEPLOYED_POSITION_DRIVETRAIN_ENGAGE_DEG) {
			RobotMain.binGrabber.setLeftSpeed(0.0);
		}
		if (RobotMain.binGrabber.getRightPositionDeg() > BinGrabber.DEPLOYED_POSITION_DRIVETRAIN_ENGAGE_DEG) {
			RobotMain.binGrabber.setRightSpeed(0.0);
		}
    	if(isTimedOut()) {
    		RobotMain.driveTrain.startPIDPosition(leftTargetInches, rightTargetInches, errorInches);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isExtraTimedOut() || (isTimedOut() && (RobotMain.driveTrain.isAtLeftTarget() || RobotMain.driveTrain.isAtRightTarget()));
    }

    // Called once after isFinished returns true
    protected void end() {
    	RobotMain.binGrabber.setSpeed(0.0, 0.0);
		RobotMain.binGrabber.setStatusFrameRate(StatusFrameRate.AnalogTempVbat, 100);
    	RobotMain.driveTrain.stopPID();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
