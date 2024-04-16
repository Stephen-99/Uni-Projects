package edu.curtin.saed.assignment1;

public class Wall extends GridObject
{
    private boolean damaged;
    private int id;

    public Wall(double xPos, double yPos, int id)
    {
        super(xPos, yPos);
        this.id = id;
        damaged = false;
    }

    public Wall(Position pos, int id)
    {
        super(pos);
        this.id = id;
        damaged = false;
    }

    public boolean isDamaged()
    {
        return damaged;
    }

    public int getId()
    {
        return id;
    }

    //will take damage. If it is already damaged, it can't take more damage and will return false.
    public boolean takeDamage()
    {
        if (damaged)
        {
            return false;
        }
        damaged = true;
        return true;
    }
}
