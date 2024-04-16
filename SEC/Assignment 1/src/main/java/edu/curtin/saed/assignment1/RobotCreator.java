package edu.curtin.saed.assignment1;

public class RobotCreator 
{
    private Thread creatorThread;
    private GridManager gm;

    public RobotCreator(GridManager gm)
    {
        this.gm = gm;
        creatorThread = new Thread(this::createRobots, "roboCreatorThread");
        creatorThread.start();
    }

    public void stopCreating()
    {
        creatorThread.interrupt();
    }

    private void createRobots()
    {
        try
        {
            while (true)
            {
                Thread.sleep(1500);
                gm.addRobot();
            }
        }
        catch (InterruptedException e)
        {

        }
    }
}
