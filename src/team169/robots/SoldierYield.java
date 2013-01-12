package team169.robots;

import team169.RobotBrain;
import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class SoldierYield extends RobotBrain {
	public SoldierYield(RobotController rc) {
		super(rc);
	}

	public void init() throws GameActionException {
	}

	public void run() throws GameActionException {
		rc.setIndicatorString(1, Integer.toString(Clock.getRoundNum()));
		rc.setIndicatorString(0, Integer.toString(Clock.getBytecodeNum()));
		rc.yield();
	}
}
