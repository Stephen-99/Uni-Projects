package edu.curtin.saed.assignment1;

public class Position 
{
    private double x, y;
    
    public Position(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    //Getters
    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    //Setters
    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public void setXY(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public void incrementX(double amt)
    {
        x += amt;
    }

    public void incrementY(double amt)
    {
        y += amt;
    }

    public void increment(double xAmt, double yAmt)
    {
        x += xAmt;
        y += yAmt;
    }
}
