package edu.curtin.saed.assignment1;

import java.util.concurrent.ArrayBlockingQueue;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextArea;

public class WallManager 
{
    public static final int MAX_WALLS = 10;
    public StringProperty wallsInQueueText;
    private ArrayBlockingQueue<Position> wallQueue;
    private int nextWallId, numWalls, numQueued;
    private GridManager gm;
    private TextArea logger;
    private Object mutex;
    private Thread wallThread;

    public WallManager(GridManager gm, TextArea logger)
    {
        this.gm = gm;
        this.logger = logger;
        nextWallId = 1;
        numWalls = 0;
        numQueued = 0;
        wallsInQueueText  = new SimpleStringProperty();
        setWallsInQueueText(0);
        mutex = new Object(); //For updating numWalls
        wallQueue = new ArrayBlockingQueue<>(MAX_WALLS);
        wallThread = new Thread(this::placeWall , "wallThread");
        wallThread.start();
    }
    
    //so that we can run create Wall in a new thread passing in arguments.
    public class WallCreator implements Runnable 
    {
        private int x, y;

        public WallCreator(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public void run() 
        {
            createWall(x, y);
        }
    }

    public void createWall(int x, int y)
    {
        if (numWalls < MAX_WALLS)
        {
            Position pos = new Position(x, y);
            if (wallQueue.offer(pos))
            {
                synchronized(mutex)
                {
                    numWalls++;
                    numQueued++;
                    setWallsInQueueText(numQueued);
                }
            }
        }
    }

    public void wallRemoved()
    {
        synchronized(mutex)
        {
            numWalls--;
        }
    }

    public void noMoreWalls()
    {
        wallThread.interrupt();
    }

    private void placeWall()
    {
        try
        {
            while (true)
            {
                Thread.sleep(2000);
                Position pos = wallQueue.take();
                synchronized(mutex)
                {
                    numQueued--;
                    setWallsInQueueText(numQueued);
                }
                while (!gm.tryAddWall(nextWallId, pos))
                {
                    synchronized(mutex)
                    {
                        numWalls--;
                    }
                    pos = wallQueue.take();

                    synchronized(mutex)
                    {
                        numQueued--;
                        setWallsInQueueText(numQueued);
                    }
                }
                final int x = (int)pos.getX(); //both final so they don't change when gui uses them
                final int y = (int)pos.getY();
                Platform.runLater(() ->
                {
                    logger.appendText("Added a wall at: " + x + ", " + y + "\n");
                });
                nextWallId++;
            }
        }
        catch (InterruptedException e)
        {

        }
    }
        
    private void setWallsInQueueText(int numInQueue)
    {
        //This triggers a GUI update so needs to happen on the GUI thread.
        Platform.runLater(() ->
        {
            wallsInQueueText.set("Number of walls in queue: " + numInQueue);    
        });
    }
}
