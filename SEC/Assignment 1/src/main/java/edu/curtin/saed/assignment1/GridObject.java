package edu.curtin.saed.assignment1;

//Represents an object that is in the grid. It will have a position.
public class GridObject 
{
    private Position pos;
    
    public GridObject(double x, double y)
    {
        pos = new Position(x, y);
    }

    public GridObject(Position pos)
    {
        this.pos = pos;
    }

    //expose position methods for easiser use.
    public double getX()
    {
        return pos.getX();
    }

    public double getY()
    {
        return pos.getY();
    }

    public void increment(double xAmt, double yAmt)
    {
        pos.increment(xAmt, yAmt);
    }

}
