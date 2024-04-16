package com.example.islandbuilder;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

/**
 * Represents a single grid square in the map. Each map element has both terrain and an optional
 * structure.
 *
 * The terrain comes in four pieces, as if each grid square was further divided into its own tiny
 * 2x2 grid (north-west, north-east, south-west and south-east). Each piece of the terrain is
 * represented as an int, which is actually a drawable reference. That is, if you have both an
 * ImageView and a MapElement, you can do this:
 *
 * ImageView iv = ...;
 * MapElement me = ...;
 * iv.setImageResource(me.getNorthWest());
 *
 * This will cause the ImageView to display the grid square's north-western terrain image,
 * whatever it is.
 *
 * (The terrain is broken up like this because there are a lot of possible combinations of terrain
 * images for each grid square. If we had a single terrain image for each square, we'd need to
 * manually combine all the possible combinations of images, and we'd get a small explosion of
 * image files.)
 *
 * Meanwhile, the structure is something we want to display over the top of the terrain. Each
 * MapElement has either zero or one Structure} objects. For each grid square, we can also change
 * which structure is built on it.
 */
public class MapElement
{
    private boolean buildable;
    private final int terrainNorthWest;
    private final int terrainSouthWest;
    private final int terrainNorthEast;
    private final int terrainSouthEast;
    private int id;
    private Structure structure;
    private Bitmap image;

    //Literally here just because spec asks for it.
    private String ownerName;

    private SQLiteDatabase db; //The database where the data is stored

    //Needs id so that the database can be updated when adding ro removing a structure.
    public MapElement(boolean buildable, int northWest, int northEast,
                      int southWest, int southEast, Structure structure, int id, SQLiteDatabase db)
    {
        this.db = db;
        this.buildable = buildable;
        this.terrainNorthWest = northWest;
        this.terrainNorthEast = northEast;
        this.terrainSouthWest = southWest;
        this.terrainSouthEast = southEast;
        this.structure = structure;
        this.id = id;
        image = null;
        ownerName = "System";
    }

    public boolean isBuildable()
    {
        return buildable;
    }

    public int getNorthWest()
    {
        return terrainNorthWest;
    }

    public int getSouthWest()
    {
        return terrainSouthWest;
    }

    public int getNorthEast()
    {
        return terrainNorthEast;
    }

    public int getSouthEast()
    {
        return terrainSouthEast;
    }

    public Structure getStructure()
    {
        return structure;
    }

    public void setStructure(Structure structure)
    {
        if (structure != null) {
            this.structure = structure;
            buildable = false;

            ContentValues cv = new ContentValues();
            cv.put(IslandSchema.MapTable.Cols.BUILDABLE, false);
            cv.put(IslandSchema.MapTable.Cols.DRAWABLEID, structure.getDrawableId());
            cv.put(IslandSchema.MapTable.Cols.LABEL, structure.getLabel());
            cv.put(IslandSchema.MapTable.Cols.STRUCTNAME, structure.getName());

            db.update(IslandSchema.MapTable.NAME, cv, IslandSchema.MapTable.Cols.ID + " = ?", new String[]{String.valueOf(id)});
        } else {
            removeStructure();
        }
    }

    public void removeStructure()
    {
        if (structure != null) {
            structure = null;
            buildable = true;
            ContentValues cv = new ContentValues();
            cv.put(IslandSchema.MapTable.Cols.BUILDABLE, true);
            cv.put(IslandSchema.MapTable.Cols.DRAWABLEID, (Integer)null);
            cv.put(IslandSchema.MapTable.Cols.LABEL, (String)null);
            cv.put(IslandSchema.MapTable.Cols.STRUCTNAME, (String)null);

            db.update(IslandSchema.MapTable.NAME, cv, IslandSchema.MapTable.Cols.ID + " = ?", new String[]{String.valueOf(id)});
        }
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
