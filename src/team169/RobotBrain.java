package team169;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public abstract class RobotBrain
{
    public RobotController rc;

    public RobotBrain(RobotController rc)
    {
        this.rc = rc;
    }

    public abstract void init() throws GameActionException;

    public abstract void run() throws GameActionException;
    
    public static void debug_error(Exception e, String message)
    {
        System.err.println(message + ": error");
        e.printStackTrace();
    }

}
