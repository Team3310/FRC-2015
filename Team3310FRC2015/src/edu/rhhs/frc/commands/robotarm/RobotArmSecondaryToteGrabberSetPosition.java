package edu.rhhs.frc.commands.robotarm;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.RobotArm;

public class RobotArmSecondaryToteGrabberSetPosition extends RobotArmCommand 
{
	private RobotArm.ToteGrabberPosition m_position;

	public RobotArmSecondaryToteGrabberSetPosition(RobotArm.ToteGrabberPosition position) {
    	m_position = position;
    }
	
	public RobotArmSecondaryToteGrabberSetPosition(RobotArmCommandType givenType) {
    	m_commandType = givenType;
    }

    protected void initialize() {
    	setTimeout(0.15);
    	RobotMain.robotArm.setSecondaryToteGrabberPosition(m_position);
    }

    
    protected void execute() {
    }

    protected boolean isFinished() {
        return isTimedOut();
//    	return true;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}