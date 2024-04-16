package com.example.islandbuilder;

import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

/**
 * Represents a possible structure to be placed on the map. A structure simply contains a drawable
 * int reference, a string label to be shown in the selector, a name editable by the user and a type.
 */
public class Structure implements Serializable
{
    private final int drawableId;

    // 0 = House, 1 = Road, 2 = Commercial and -1 = Invalid Structure.
    private int type;
    private String label, name;

    public Structure(int drawableId, String label)
    {
        this.drawableId = drawableId;
        this.label = label;
        this.name = label;
        if (label.equals("House")) {
            type = 0;
        } else if (label.equals("Road")) {
            type = 1;
        } else if (label.equals("Commercial")){
            type = 2;
        } else {
            type = -1;
        }
    }

    public int getDrawableId()
    {
        return drawableId;
    }

    public String getLabel()
    {
        return label;
    }

    public boolean isRoad()
    {
        return type == 1;
    }

    public boolean isHouse()
    {
        return type == 0;
    }

    public boolean isCommercial()
    {
        return type == 2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
