package team169.robots;

import team169.RobotBrain;
import team169.constants.BroadcastChannel;
import team169.constants.IndicatorString;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import battlecode.engine.instrumenter.lang.System;

public class HqSwarm extends RobotBrain
{
    Direction enemyDir;
    Robot leader = null;
    Robot target = null;
    MapLocation leaderLoc = null;
    MapLocation targetLoc = null;
    MapLocation pathLoc = null;
    int leaderId = 0;

    public HqSwarm(RobotController rc)
    {
        super(rc);
    }

    public void init() throws GameActionException
    {
        MapLocation hqLoc = rc.getLocation();
        enemyDir = hqLoc.directionTo(rc.senseEnemyHQLocation());
    }

    public void run()
    {
        spawnInCircle();

        getLeaderLoc();
        getTargetLoc();
        getPathLoc();
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
                e.printStackTrace();
                rc.addMatchObservation("UhOh");
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
            e.printStackTrace();
        }
    }

    private void getLeaderLoc()
    {

        RobotInfo leaderInfo = getLeaderInfo();
        if (leaderInfo != null)
        {
            leaderLoc = leaderInfo.location;
        } else
        {
            leaderLoc = rc.getLocation();
        }
    }

    private void getTargetLoc()
    {
        RobotInfo targetInfo = getTargetInfo();
        if (targetInfo != null)
        {
            targetLoc = targetInfo.location;
        } else
        {
            targetLoc = rc.senseEnemyHQLocation();
        }
    }

    private void getPathLoc()
    {
        pathLoc = leaderLoc.add(leaderLoc.directionTo(targetLoc));
    }

    private RobotInfo getLeaderInfo()
    {
        RobotInfo robotInfo = null;

        if (leader != null)
        {
            try
            {
                robotInfo = rc.senseRobotInfo(leader);
            } catch (GameActionException e)
            {
                leader = null;
            }
        }

        if (leader == null)
        {
            getNextLeader();
        }

        if (robotInfo == null && leader != null)
        {
            try
            {
                robotInfo = rc.senseRobotInfo(leader);
            } catch (GameActionException e)
            {
            }
        }

        return robotInfo;
    }

    private RobotInfo getTargetInfo()
    {
        RobotInfo robotInfo = null;

        if (target != null)
        {
            try
            {
                robotInfo = rc.senseRobotInfo(target);
            } catch (GameActionException e)
            {
                target = null;
            }
        }

        if (target == null)
        {
            getNextTarget();
        }

        if (robotInfo == null && target != null)
        {
            try
            {
                robotInfo = rc.senseRobotInfo(target);
            } catch (GameActionException e)
            {
            }
        }

        return robotInfo;
    }

    private void getNextLeader()
    {
        leader = getNextRobot(rc.getTeam());

        if (leader != null)
        {
            leaderId = leader.getID();
        } else
        {
            leaderId = 0;
        }
    }

    private void getNextTarget()
    {
        target = getNextRobot(rc.getTeam().opponent());
    }

    private Robot getNextRobot(Team team)
    {
        Robot[] robots = rc.senseNearbyGameObjects(Robot.class,
                new MapLocation(0, 0), 100000, team);
        Robot nextRobot = null;
        for (Robot robot : robots)
        {
            try
            {
                RobotInfo robotInfo = rc.senseRobotInfo(robot);
                if (robotInfo.type == RobotType.SOLDIER)
                {
                    nextRobot = robot;
                }
            } catch (GameActionException e)
            {
                e.printStackTrace();
            }
        }

        return nextRobot;
    }

}
