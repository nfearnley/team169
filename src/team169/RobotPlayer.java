package team169;

import team169.robots.HqSwarm;
import team169.robots.SoldierSwarm;
import battlecode.common.Clock;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class RobotPlayer
{
    static int round = 0;
    
    public static void run(RobotController rc)
    {
        RobotBrain rb = null;
        switch (rc.getType())
        {
        case HQ:
            rb = new HqSwarm(rc);
            break;
        case SOLDIER:
            rb = new SoldierSwarm(rc);
            break;
        case ARTILLERY:
            break;
        case GENERATOR:
            break;
        case MEDBAY:
            break;
        case SHIELDS:
            break;
        case SUPPLIER:
            break;
        default:
            break;
        }

        if (rb != null)
        {
            try
            {
                rb.init();
            } catch (Exception e)
            {
                RobotBrain.debug_error(e, "init");
            }

            while (true)
            {
                try
                {
                    debug_endRound(rc);
                    rc.yield();
                    debug_startRound();
                    if (rc.isActive())
                    {
                        rb.run();
                    }
                } catch (Exception e)
                {
                    RobotBrain.debug_error(e, "run");
                }

            }
        }
    }
    
    private static void debug_startRound()
    {
        round = Clock.getRoundNum();
    }
    
    private static void debug_endRound(RobotController rc)
    {
        if (rc.getType() == RobotType.HQ)
        {
            int endRound = Clock.getRoundNum();
            int cycleDuration = endRound - round;
            int bytecodesUsed = Clock.getBytecodeNum();
            System.out.println("Cycle duration: "+ cycleDuration + " @ " + bytecodesUsed + " bytecodes used, " + (rc.isActive() ? "Active" : "Inactive"));
        }
    }
}
