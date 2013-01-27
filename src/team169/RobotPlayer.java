package team169;

import team169.robots.HqSwarm;
import team169.robots.SoldierSwarm;
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
                System.out.println("Init: Exception should not have gotten this far");
                e.printStackTrace();
            }

            while (true)
            {
                try
                {
                    rc.yield();
                    if (rc.isActive())
                    {
                        rb.run();
                    }
                } catch (Exception e)
                {
                    System.out.println("Run: Exception should not have gotten this far");
                    e.printStackTrace();
                }

            }
        }
    }

}
