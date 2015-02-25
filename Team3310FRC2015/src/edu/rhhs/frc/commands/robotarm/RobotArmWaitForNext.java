package edu.rhhs.frc.commands.robotarm;

import edu.rhhs.frc.RobotMain;

/**
 *
 */
public class RobotArmWaitForNext extends RobotArmCommand {

	public RobotArmWaitForNext() {
    }

    protected void initialize() {
		RobotMain.robotArm.setWaitForNext(true);
    }

    protected void execute() {
    }

    protected boolean isFinished() {
    	return !RobotMain.robotArm.isWaitForNext();
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
