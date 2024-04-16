package edu.curtin.saed.assignment1;

import java.util.Random;

public class Robot extends GridObject
{
    private int delay, id;
    private Thread roboThread;
    private GridManager gm;
    private Object monitor; //To keep track of when the robot is moving and make sure we wait
    private boolean moving;

    public Robot(int id, int xPos, int yPos, GridManager gm)
    {
        super(xPos, yPos);
        this.id = id;
        this.gm = gm;
        monitor = new Object();
        moving = false;
        Random rand = new Random();
        delay = rand.nextInt(500, 2001);
        roboThread = new Thread(this::moveAfterDelay, "roboThread_" + id);
        roboThread.start();
    }

    public void killRobot()
    {
        roboThread.interrupt();
    }

    @SuppressWarnings("PMD") //It asks me to use notifyAll instead of notify, when I know for a fact there will only be 1 thread waiting
    public void finishedMoving()
    {
        synchronized(monitor)
        {
            moving = false;
            monitor.notify();
        }
    }

    private void moveAfterDelay()
    {
        try
        {
            while (true)
            {
                Thread.sleep(delay);
                synchronized (monitor)
                {
                    if (moving)
                    {
                        monitor.wait();
                    }
                    moving = true;
                    gm.moveRobot(id);
                }
            }
        }
        catch (InterruptedException e)
        {

        }
    }
}
