package edu.curtin.saed.assignment1;

import java.util.*;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GridManager 
{
    private Integer nextRobotId = 1;
    private int gridWidth, gridHeight, citadelX, citadelY;
    private GridSquare[][] grid;
    private Random rand;
    private JFXArena gui;
    private MovementManager movementManager;
    private ScoreManager scoreManager;
    private WallManager wallManager;
    private Stage stage;
    private TextArea logger;
    private Map<Integer, Robot> robots; //GUI has a separate copy, since it can't afford to block if another thread is changing it.
    private Map<Integer, Wall> walls; //GUI has a separate copy, since it can't afford to block if another thread is changing it.
    private Object mutex; //For locking any access to the gridSquares in the grid. This means we can make decisions based on the state of the 'gameboard' without other threads changing it.

    public GridManager(int gridWidth, int gridHeight, JFXArena gui, MovementManager movementManager, ScoreManager scoreManager, Stage stage, TextArea logger)
    {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.gui = gui;
        this.movementManager = movementManager;
        this.scoreManager = scoreManager;
        this.stage = stage;
        this.logger = logger;
    }

    public void setupGrid(WallManager wallManager)
    {
        this.wallManager = wallManager;
        citadelX = (gridWidth - 1) / 2;
        citadelY = (gridHeight - 1) / 2;
        rand = new Random();
        robots = new HashMap<>();
        walls = new HashMap<>();
        mutex = new Object();

        grid = new GridSquare[gridWidth][gridHeight];
        for (int ii = 0; ii < gridWidth; ii++)
        {
            for (int jj = 0; jj < gridHeight; jj++)
            {
                grid[ii][jj] = new GridSquare(ii, jj);
            }
        }
        grid[citadelX][citadelY].placeCitadel();
    }

    public void addRobot()
    {
        synchronized(mutex)
        {
            //First check which corners are availible, then randomly pick one.
            ArrayList<GridSquare> availibleCorners = new ArrayList<>(Arrays.asList(grid[0][0], grid[gridWidth-1][0], grid[0][gridHeight-1], grid[gridWidth-1][gridHeight-1]));
            availibleCorners.removeIf(corner -> !corner.isEmpty()); //Remove corners where we can't place a robot
            if (!availibleCorners.isEmpty())
            {
                GridSquare gs = availibleCorners.get(rand.nextInt(availibleCorners.size()));
                gs.addRobot();
                
                final int curRoboID = nextRobotId; //final so it doesn't change by the time gui runs it.
                Platform.runLater(() ->
                {
                    logger.appendText("Added robot_" + curRoboID + " at: " + gs.getX() + ", " + gs.getY() + "\n");
                });

                int roboId = nextRobotId;
                Platform.runLater(() ->
                {
                    //The gui needs to get the position before a robot is created and a move request might occur
                    gui.addRobot(roboId, new Position(gs.getX(), gs.getY()));
                    Robot robot = new Robot(roboId, (int)gs.getX(), (int)gs.getY(), this);
                    robots.put(roboId, robot);
                });
                nextRobotId++;
            }
        }
    }

    public void killRobots()
    {
        for (Map.Entry<Integer, Robot> robo : robots.entrySet())
        {
            robo.getValue().killRobot();
        }
    }

    public boolean tryAddWall(int id, Position pos)
    {
        int x = (int)pos.getX();
        int y = (int)pos.getY();

        //can't be in corners or CITADEL
        if (x == citadelX && y == citadelY)
        {
            return false;
        }
        if (x == 0 && y == 0)
        {
            return false;
        }
        if (x == gridWidth-1 && y == 0)
        {
            return false;
        }
        if (x == 0 && y == gridHeight-1)
        {
            return false;
        }
        if (x == gridWidth-1 && y == gridHeight-1)
        {
            return false;
        }

        synchronized(mutex)
        {
            if (!grid[x][y].isEmpty())
            {
                return false;
            }

            Wall wall = new Wall(pos, id);
            walls.put(id, wall);
            grid[x][y].placeWall();
            Platform.runLater(() ->
            {
                gui.addWall(id, pos);
            });
        }
        return true;
    }

    public void moveRobot(int roboId) throws InterruptedException
    {
        synchronized(mutex)
        {
            Robot robo = robots.get(roboId);
            int x = (int)robo.getX();
            int y = (int)robo.getY();

            //Get the valid moving spaces
            GridSquare citadelVert = getNeighbouringSquare(x, y, 1, true);
            GridSquare citadelHorz = getNeighbouringSquare(x, y, 1, false);
            GridSquare awayVert = getNeighbouringSquare(x, y, -1, true);
            GridSquare awayHorz = getNeighbouringSquare(x, y, -1, false);

            //For keeping track of which position was moved to.
            Position movedTo = new Position(-1, -1);

            //75% chance of moving towards citadel.
            boolean towardsCitadel = rand.nextDouble() > 0.25;
            boolean verticalMvmt = rand.nextBoolean();

            //If CitadelY == y or CitadelX == x, there are 3 options for away from citadel, and only 1 for towards
            if (citadelY == y)
            {
                if (towardsCitadel)
                {
                    if (!tryMove(citadelHorz, movedTo))
                    {
                        //No valid movement towards citadel, try move away
                        if (!tryMove(awayVert, awayHorz, verticalMvmt, movedTo))
                        {
                            tryMove(citadelVert, movedTo);
                        }
                    }
                }
                else
                {
                    if (!tryMove(awayVert, awayHorz, verticalMvmt, movedTo))
                    {
                        if (!tryMove(citadelVert, movedTo))
                        {
                            //No valid movement away from citadel, try move towards
                            tryMove(citadelHorz, movedTo);
                        }
                    }
                }
            }
            else if (citadelX == x)
            {
                if (towardsCitadel)
                {
                    if (!tryMove(citadelVert, movedTo))
                    {
                        //No valid movement towards citadel, try move away
                        if (!tryMove(awayVert, awayHorz, verticalMvmt, movedTo))
                        {
                            tryMove(citadelHorz, movedTo);
                        }
                    }
                }
                else
                {
                    if (!tryMove(awayVert, awayHorz, verticalMvmt, movedTo))
                    {
                        if (!tryMove(citadelHorz, movedTo))
                        {
                            //No valid movement away from citadel, try move towards
                            tryMove(citadelVert, movedTo);
                        }
                    }
                }
            }
            else if (towardsCitadel)
            {
                if (!tryMove(citadelVert, citadelHorz, verticalMvmt, movedTo))
                {
                    //No valid movement towards citadel, try move away
                    tryMove(awayVert, awayHorz, verticalMvmt, movedTo);
                }
            }
            else
            {
                if (!tryMove(awayVert, awayHorz, verticalMvmt, movedTo))
                {
                    //No valid movement away from citadel, try move towards
                    tryMove(citadelVert, citadelHorz, verticalMvmt, movedTo);
                }
            }

            //If there was not a valid move space
            if (movedTo.getX() == -1)
            {
                robo.finishedMoving();
            }
            else
            {
                movementManager.submitMovement(new Movement(this, gui, roboId, x, y, movedTo.getX(), movedTo.getY()));
            }
        }
    }

    public void robotFinishedMove(int roboId, int startX, int endX, int startY, int endY)
    {
        synchronized(mutex)
        {
            if (grid[endX][endY].isCitadel())
            {    
               //trigger game over
               Platform.runLater(() ->
               {
                    //The on close handler will take care of cleaning up all the threads
                    stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
               });
            }
            if (grid[endX][endY].wallCollision())
            {
                //Find the offending wall
                Wall wall = null;
                for (Map.Entry<Integer, Wall> wallEntry : walls.entrySet())
                {
                    wall = wallEntry.getValue();
                    if ((wall.getX() == endX) && (wall.getY() == endY))
                    {
                        break;
                    }
                }
                final int wallId = wall.getId(); //final so I can pass it to the gui

                //remove the robot and clear the grid squares
                Robot robo = robots.get(roboId);
                robo.killRobot();
                robots.remove(roboId);
                scoreManager.robotDied();
                grid[endX][endY].clearSquare();
                grid[startX][startY].clearSquare();

                if (wall.takeDamage())
                {
                    //wall still stands.
                    Platform.runLater(() ->
                    {
                        logger.appendText("Wall at: " + endX + ", " + endY + " took damage!\n");
                    });
                    grid[endX][endY].placeWall();
                }
                else
                {
                    //wall couldn't take damage, it is too damaged and needs to be removed
                    Platform.runLater(() ->
                    {
                        logger.appendText("Wall at: " + endX + ", " + endY + " was destroyed!!\n");
                    });
                    walls.remove(wallId);
                    wallManager.wallRemoved();
                }

                Platform.runLater(() -> 
                {
                    gui.removeRobot(roboId);
                    gui.wallChanged(wallId);
                });
            }
            else
            {
                //No collision. Clear old square, and update robot position
                grid[startX][startY].clearSquare();
                Robot robo = robots.get(roboId);
                robo.increment(endX-startX, endY-startY);
                robo.finishedMoving();
            }
        }
    }

    private boolean tryMove(GridSquare vertOpt, GridSquare horzOpt, boolean vertical, Position movedTo)
    {
        if (vertical)
        {
            if (!tryMove(vertOpt, movedTo))
            {
                if (!tryMove(horzOpt, movedTo))
                {
                    return false;
                }
            }
        }
        else
        {
            if (!tryMove(horzOpt, movedTo))
            {
                if (!tryMove(vertOpt, movedTo))
                {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean tryMove(GridSquare square, Position movedTo)
    {
        if (square.canAcceptRobot())
        {
            square.addRobot();
            movedTo.setXY(square.getX(), square.getY());
            return true;
        }
        return false;
    }

    //towards the citadel should be 1 for towards and -1 for away
    private GridSquare getNeighbouringSquare(int x, int y, int towardsCitadel, boolean vertical)
    {
        int difFromCitadel;
        if (vertical)
        {
            difFromCitadel = citadelY-y;
            if (citadelY == y)
            {
                difFromCitadel = 1; //Otherwise we get 0 and the current square as a valid movement.
            }

            int idx = (int)(y + Math.signum(difFromCitadel) * towardsCitadel);
            if (idx < 0 || idx >= gridHeight)
            {
                //out of bounds!
                return new GridSquare();
            }
            return grid[x][idx];
        }
        else
        {
            difFromCitadel = citadelX-x;
            if (citadelX == x)
            {
                difFromCitadel = 1; //Otherwise we get 0 and the curretn square as a valid movement.
            }

            int idx = (int)(x + Math.signum(difFromCitadel) * towardsCitadel);
            if (idx < 0 || idx >= gridWidth)
            {
                //out of bounds!
                return new GridSquare();
            }
            return grid[idx][y];
        }  
    }
}
