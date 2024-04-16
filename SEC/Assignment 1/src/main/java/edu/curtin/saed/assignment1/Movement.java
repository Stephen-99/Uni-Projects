package edu.curtin.saed.assignment1;

import javafx.application.Platform;

public class Movement implements Runnable
{
    @SuppressWarnings("PMD") //constants should be all CAPS
    private final int TOTALTIME = 400;
    @SuppressWarnings("PMD")
    private final int STEPS = 20;
    @SuppressWarnings("PMD")
    private final double TOL = 1 / (double)STEPS / 1000;

    private int robotId;
    private double xInc, yInc, startX, endX, startY, endY;
    private GridManager gm;
    private JFXArena gui;

    //assumes end position is only 1 away from start and only in 1 direction.
    public Movement(GridManager gm, JFXArena gui, int robotId, double startX, double startY, double endX, double endY)
    {
        this.gm = gm;
        this.gui = gui;
        this.robotId = robotId;
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        
        //Either both of the xpos or both of the ypos should be the same
        if (Math.abs(startX - endX) < TOL)
        {
            xInc = 0;
            yInc = (1 / (double)STEPS) * (endY - startY); //multiply by difference to give the direction
        }
        else
        {
            yInc = 0;
            xInc = (1 / (double)STEPS) * (endX - startX); //multiply by difference to give the direction
        }
    }

    @Override
    public void run() 
    {
        try
        {
            for (int ii = 0; ii < STEPS; ii++)
            {
                Platform.runLater(() -> 
                { 
                    gui.moveRobot(robotId, xInc, yInc);
                });
                
                Thread.sleep(TOTALTIME/STEPS);
            }
            gm.robotFinishedMove(robotId, (int)startX, (int)endX, (int)startY, (int)endY);
        }
        catch (InterruptedException e)
        {

        }
        
    }
    
}
