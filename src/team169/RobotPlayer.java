package team169;

import team169.robots.HqSwarm;
import team169.robots.SoldierSwarm;
import team169.robots.SoldierYield;
import battlecode.common.RobotController;

public class RobotPlayer
{
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
                e.printStackTrace();
            }

            while (true)
            {
                try
                {
                    if (rc.isActive())
                    {
                        rb.run();
                    }
                    rc.yield();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }
    }

}
