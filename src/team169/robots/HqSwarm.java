package team169.robots;

import team169.RobotBrain;
import team169.constants.BroadcastChannel;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameObject;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;

public class HqSwarm extends RobotBrain
{
    Direction enemyDir;
    MapLocation enemyHqLoc;
    Robot leader = null;
    Robot target = null;
    MapLocation leaderLoc;
    MapLocation targetLoc;
    int leaderId = 0;

    public HqSwarm(RobotController rc)
    {
        super(rc);
    }

    public void init() throws GameActionException
    {
        MapLocation hqLoc = rc.getLocation();
        enemyDir = hqLoc.directionTo(rc.senseEnemyHQLocation());
        enemyHqLoc = rc.senseEnemyHQLocation();
        leaderLoc = rc.getLocation();
        targetLoc = enemyHqLoc;
    }

    public void run() throws GameActionException
    {
        spawnInCircle();

        getLeader();
        getTarget();
        bubbleLeader();
        BroadcastCommands();
    }

    private void spawnInCircle()
    {
        Direction spawnDir = enemyDir.rotateRight();
        while ((!rc.canMove(spawnDir)) && (spawnDir != enemyDir))
        {
            spawnDir = spawnDir.rotateRight();
        }
        if (rc.canMove(spawnDir))
        {
            try
            {
                rc.spawn(spawnDir);
            } catch (GameActionException e)
            {
                debug_error(e, "spawnInCircle");
            }
        }
    }

    private void BroadcastCommands()
    {
        int attack;

        if (leaderLoc.distanceSquaredTo(targetLoc) < 3)
        {
            attack = 1;
        } else
        {
            attack = 0;
        }

        MapLocation pathLoc = getPath(leaderLoc, targetLoc);

        try
        {
            rc.broadcast(BroadcastChannel.ATTACK, attack);
            rc.broadcast(BroadcastChannel.LEADER_ID, leaderId);
            rc.broadcast(BroadcastChannel.LEADER_X, leaderLoc.x);
            rc.broadcast(BroadcastChannel.LEADER_Y, leaderLoc.y);
            rc.broadcast(BroadcastChannel.TARGET_X, targetLoc.x);
            rc.broadcast(BroadcastChannel.TARGET_Y, targetLoc.y);
            rc.broadcast(BroadcastChannel.PATH_X, pathLoc.x);
            rc.broadcast(BroadcastChannel.PATH_Y, pathLoc.y);
        } catch (GameActionException e)
        {
            debug_error(e, "BroadcastCommands");
        }
    }

    private MapLocation getPath(MapLocation from, MapLocation to)
    {
        return from.add(from.directionTo(to));
    }

    private void getLeader() throws GameActionException
    {
        if (!isRobotAlive(leader))
        {
            leader = getNextRobot(rc.getTeam(), leaderLoc);
        }

        if (isRobotAlive(leader))
        {
            leaderLoc = getRobotLoc(leader);
            leaderId = leader.getID();
        } else
        {
            leaderLoc = rc.getLocation();
            leaderId = 0;
        }
    }

    private void bubbleLeader()
    {
        try
        {   
            while (!leaderLoc.isAdjacentTo(targetLoc))
            {
                GameObject object = rc.senseObjectAtLocation(getPath(leaderLoc, targetLoc));
                Robot robot;
                if (object instanceof Robot)
                {
                    robot = (Robot) object;
                }
                else
                {
                    break;
                }
                
                if (robot.getTeam() != rc.getTeam())
                {
                    break;
                }
                
                if (rc.senseRobotInfo(robot).type != RobotType.SOLDIER)
                {
                    break;
                }

                leader = robot;
                leaderLoc = getRobotLoc(leader);
            }
            
        }
        catch (GameActionException e)
        {
            debug_error(e, "bubbleLeader");
        }
    }

    private void getTarget() throws GameActionException
    {
        if (!isRobotAlive(target))
        {
            target = getNextRobot(rc.getTeam().opponent(), leaderLoc);
        }

        if (isRobotAlive(target))
        {
            targetLoc = getRobotLoc(target);
        } else
        {
            targetLoc = rc.senseEnemyHQLocation();
        }
    }

    private MapLocation getRobotLoc(Robot robot) throws GameActionException
    {
        return rc.senseRobotInfo(robot).location;
    }

    private boolean isRobotAlive(Robot robot)
    {
        boolean isAlive = false;

        if (robot != null)
        {
            try
            {
                rc.senseRobotInfo(robot);
                isAlive = true;
            } catch (GameActionException e)
            {
            }
        }

        return isAlive;
    }

    // Get the closest robot to 'loc' that is on 'team'
    private Robot getNextRobot(Team team, MapLocation loc)
    {
        Robot[] robots = rc.senseNearbyGameObjects(Robot.class, loc, 100000,
                team);
        Robot nextRobot = null;
        for (Robot robot : robots)
        {
            try
            {
                RobotInfo robotInfo = rc.senseRobotInfo(robot);
                if (robotInfo.type == RobotType.SOLDIER)
                {
                    nextRobot = robot;
                    break;
                }
            } catch (GameActionException e)
            {
                debug_error(e, "getNextRobot");
            }
        }

        return nextRobot;
    }

}
