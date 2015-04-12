package edu.rhhs.frc.commands.robotarm;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.RobotArm;

public class RobotArmToteGrabberSetPosition extends RobotArmCommand 
{
	private RobotArm.ToteGrabberPosition m_position;

	public RobotArmToteGrabberSetPosition(RobotArm.ToteGrabberPosition position) {
    	m_position = position;
    }
	
	public RobotArmToteGrabberSetPosition(RobotArmCommandType givenType) {
    	m_commandType = givenType;
    }

    protected void initialize() {
    	setTimeout(0.15);
    	RobotMain.robotArm.setToteGrabberPosition(m_position);
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return isTimedOut();
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}