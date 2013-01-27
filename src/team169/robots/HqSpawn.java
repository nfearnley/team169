package team169.robots;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import team169.RobotBrain;
import team169.constants.IndicatorString;

public class HqSpawn extends RobotBrain
{
    Direction enemyDir;

    public HqSpawn(RobotController rc)
    {
        super(rc);
    }

    @Override
    public void init() throws GameActionException
    {
        MapLocation hqLoc = rc.getLocation();
        enemyDir = hqLoc.directionTo(rc.senseEnemyHQLocation());
        rc.setIndicatorString(IndicatorString.ROBOT_TYPE, "HQ");
    }

    @Override
    public void run() throws GameActionException
    {
        Direction spawnDir = enemyDir;
        while ((!rc.canMove(spawnDir)) && (spawnDir != enemyDir))
        {
            spawnDir = spawnDir.rotateRight();
        }
        if (rc.canMove(spawnDir))
        {
            rc.spawn(spawnDir);
        }
    }
}
