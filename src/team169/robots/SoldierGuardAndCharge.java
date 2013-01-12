package team169.robots;

import team169.RobotBrain;
import team169.constants.IndicatorString;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class SoldierGuardAndCharge extends RobotBrain
{
    boolean isGuard = false;

    public SoldierGuardAndCharge(RobotController rc)
    {
        super(rc);
    }

    public void init() throws GameActionException
    {
        MapLocation hqLoc = rc.senseHQLocation();
        Direction enemyDir = hqLoc.directionTo(rc.senseEnemyHQLocation());
        MapLocation launchLoc = hqLoc.add(enemyDir);
        if (!(rc.getLocation().equals(launchLoc)))
        {
            isGuard = true;
        }
        rc.setIndicatorString(IndicatorString.ROBOT_TYPE, isGuard ? "Guard" : "Soldier");
    }

    public void run() throws GameActionException
    {
        if (isGuard)
        {
            return;
        }

        if (rc.getTeamPower() < 10)
        {
            return;
        }

        Direction enemyDir = rc.getLocation().directionTo(
                rc.senseEnemyHQLocation());
        Direction newDir = null;
        if (rc.canMove(enemyDir))
        {
            newDir = enemyDir;
        } else if (rc.canMove(enemyDir.rotateRight()))
        {
            newDir = enemyDir.rotateRight();
        } else if (rc.canMove(enemyDir.rotateLeft()))
        {
            newDir = enemyDir.rotateLeft();
        }
        MapLocation loc = rc.getLocation().add(newDir);

        if (newDir == null)
        {
            return;
        }

        if (rc.senseMine(loc) != null)
        {
            rc.defuseMine(loc);
        } else
        {
            rc.move(newDir);
        }
    }
}
