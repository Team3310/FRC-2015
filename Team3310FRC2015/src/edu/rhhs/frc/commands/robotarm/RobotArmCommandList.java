package edu.rhhs.frc.commands.robotarm;

import java.util.ArrayList;
import edu.rhhs.frc.commands.robotarm.RobotArmCommand.RobotArmCommandType;
import edu.rhhs.frc.utility.motionprofile.MotionProfile.ProfileMode;
import edu.rhhs.frc.utility.motionprofile.WaypointList;

public class RobotArmCommandList extends ArrayList<RobotArmCommand> 
{
	private static final long serialVersionUID = 6784216752255634190L;

	int currentIndex;

	public RobotArmCommandList() {
		currentIndex = 0;
	}

	public RobotArmCommand getCurrent() {
		return this.get(currentIndex);
	}

	public RobotArmCommand getNext() {
		return this.get(currentIndex++);
	}

	public RobotArmCommand getPrev(RobotArmCommandType type) {
		for(int i = currentIndex; i < this.size(); i++) {
			if(get(i).getCommandType() != type) {
				continue;
			}
			if(getNext().getCommandType() == type) {
				return get(i);
			}
		}
		return null;
	}

	public RobotArmCommand getNext(RobotArmCommandType type) {
		for(int i = currentIndex; i < this.size(); i++) {
			if(get(i).getCommandType() != type) {
				continue;
			}
			if(getNext().getCommandType() == type) {
				return get(i);
			}
		}
		return null;
	}

	public static void main(String[] args) {
		RobotArmCommandList list = new RobotArmCommandList();
		RobotArmMotionProfileJ1ToZero a = new RobotArmMotionProfileJ1ToZero();
		a.setCommandType(RobotArmCommandType.MIDDLE_STACK);
		list.add(a);
		WaypointList wpl = new WaypointList(ProfileMode.CartesianInputJointMotion);
		wpl.addWaypoint(0, 0, 0, 0);
		RobotArmMotionProfilePath arm = new RobotArmMotionProfilePath(wpl);
		arm.setCommandType(RobotArmCommandType.BEGIN_CYCLE);
		list.add(arm);
		System.out.println(list.getCurrent().getClass().getSimpleName());
		System.out.println(list.getNext(RobotArmCommandType.BEGIN_CYCLE).getClass().getSimpleName());
	}
}