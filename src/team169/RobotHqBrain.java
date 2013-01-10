package team169;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.Upgrade;

public class RobotHqBrain extends RobotBrain
{
    public RobotHqBrain(RobotController rc)
    {
        super(rc);
    }

    Direction enemyDir;

    public void init() throws GameActionException
    {
        MapLocation hqLoc = rc.getLocation();
        enemyDir = hqLoc.directionTo(rc.senseEnemyHQLocation());
        rc.setIndicatorString(IndicatorString.ROBOT_TYPE, "HQ");
    }

    public void run() throws GameActionException
    {
        Direction spawnDir = enemyDir.rotateRight();
        while ((!rc.canMove(spawnDir)) && (spawnDir != enemyDir))
        {
            spawnDir = spawnDir.rotateRight();
        }
        if (rc.canMove(spawnDir))
        {
            rc.spawn(spawnDir);
        } else
        {
            rc.researchUpgrade(Upgrade.NUKE);
        }
    }

}
