package edu.rhhs.frc.commands.robotarm;

import edu.rhhs.frc.RobotMain;

public class RobotArmSetXLock extends RobotArmCommand 
{
	private boolean m_position;

	public RobotArmSetXLock(boolean position) {
    	m_position = position;
    }
	
    protected void initialize() {
    	RobotMain.robotArm.setXLock(m_position);
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}