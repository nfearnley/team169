package team169.robots;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import team169.RobotBrain;
import team169.constants.BroadcastChannel;

public class SoldierSwarm extends RobotBrain
{
    int attack = 0;
    int leaderId = 0;
    boolean isLeader = false;
    MapLocation leaderLoc = null;
    MapLocation targetLoc = null;
    MapLocation pathLoc = null;

    public SoldierSwarm(RobotController rc)
    {
        super(rc);
    }

    @Override
    public void init() throws GameActionException
    {

    }

    @Override
    public void run() throws GameActionException
    {
        receiveBroadcasts();
        checkIsLeader();
        if (isLeader)
        {
            moveTowards(targetLoc);
        }
        else
        {
            moveTowards(leaderLoc);
        }
    }

    public void moveTowards(MapLocation target)
    {
        if (rc.getLocation().equals(target))
        {
            return;
        }
        
        Direction enemyDir = rc.getLocation().directionTo(target);
        
        Direction newDir = Direction.NONE;
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
        
        if (newDir == Direction.NONE)
        {
            return;
        }

        MapLocation loc = rc.getLocation().add(newDir);
        if (loc == pathLoc)
        {
            return;
        }
        
        try
        {
            if (rc.senseMine(loc) != null)
            {
                rc.defuseMine(loc);
            } else
            {
                rc.move(newDir);
            }
        }
        catch (GameActionException e)
        {
            debug_error(e, "moveTowards");
        }
    }
    
    private void checkIsLeader()
    {
        isLeader = rc.getRobot().getID() == leaderId;
    }

    private void receiveBroadcasts()
    {
        try
        {
            attack = rc.readBroadcast(BroadcastChannel.ATTACK);
            leaderId = rc.readBroadcast(BroadcastChannel.LEADER_ID);
            int leaderLocX = rc.readBroadcast(BroadcastChannel.LEADER_X);
            int leaderLocY = rc.readBroadcast(BroadcastChannel.LEADER_Y);
            leaderLoc = new MapLocation(leaderLocX, leaderLocY);
            int targetLocX = rc.readBroadcast(BroadcastChannel.TARGET_X);
            int targetLocY = rc.readBroadcast(BroadcastChannel.TARGET_Y);
            targetLoc = new MapLocation(targetLocX, targetLocY);
            int pathLocX = rc.readBroadcast(BroadcastChannel.PATH_X);
            int pathLocY = rc.readBroadcast(BroadcastChannel.PATH_Y);
            pathLoc = new MapLocation(pathLocX, pathLocY);
        } catch (GameActionException e)
        {
            debug_error(e, "receiveBroadcasts");
        }
        debug_setIndicator();
    }
    
    private void debug_setIndicator()
    {
        rc.setIndicatorString(0, leaderLoc.toString() + " -> " + targetLoc.toString());
        rc.setIndicatorString(2, (attack == 1 ? "Attack" : "Follow") + " : " + leaderId);
    }

}
