package edu.curtin.saed.assignment1;


public class GridSquare extends GridObject
{
    private enum State
    {
        EMPTY,
        ROBOT,
        WALL,
        ROBOTANDWALL,
        CITADEL,
        ROBOTANDCITADEL,
        OUTOFBOUNDS
    }

    private State gridState;

    public GridSquare(int x, int y)
    {
        super(x, y);
        gridState = State.EMPTY;
    }

    //Alternatice constructor to create an out of Bounds gridSquare
    public GridSquare()
    {
        super(-1, -1);
        gridState = State.OUTOFBOUNDS;
    }

    //This feels bad like revealing the inner state a bit, but it makes a bunch of other code much simpler
    //It also isn't totally revealing the state, it's basically just saying if the square is availible for something to be placed
    public boolean isEmpty()
    {
        return gridState == State.EMPTY;
    }

    public void placeCitadel()
    {
        if (gridState == State.EMPTY)
        {
            gridState = State.CITADEL;
        }
    }

    public void placeWall()
    {
        if (gridState == State.EMPTY)
        {
            gridState = State.WALL;
        }  
    }

    public void addRobot()
    {
        if (gridState == State.EMPTY)
        {
            gridState = State.ROBOT;
        }
        else if (gridState == State.WALL)
        {
            gridState = State.ROBOTANDWALL;
        }
        else if (gridState == State.CITADEL)
        {
            gridState = State.ROBOTANDCITADEL;
        }
    }
    
    //Checks if it's ok for a robot to move here.
    public boolean canAcceptRobot()
    {
        return (gridState == State.EMPTY) || (gridState == State.WALL) || (gridState == State.CITADEL);
    }

    public boolean wallCollision()
    {
        return gridState == State.ROBOTANDWALL;
    }

    public boolean isCitadel()
    {
        return gridState == State.CITADEL || gridState == State.ROBOTANDCITADEL;
    }

    public void clearSquare()
    {
        if (gridState == State.ROBOT || gridState == State.ROBOTANDWALL)
        {
            gridState = State.EMPTY;
        }
    }
}
