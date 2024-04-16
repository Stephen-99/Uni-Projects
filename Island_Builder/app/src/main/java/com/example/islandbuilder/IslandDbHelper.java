package com.example.islandbuilder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.islandbuilder.IslandSchema.*;


public class IslandDbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "island.db";

    public IslandDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SettingsTable.NAME + "("
                + SettingsTable.Cols.ID + " INTEGER, "
                + SettingsTable.Cols.NAME + " TEXT, "
                + SettingsTable.Cols.MAPWIDTH + " INTEGER, "
                + SettingsTable.Cols.MAPHEIGHT + " INTEGER, "
                + SettingsTable.Cols.INITMONEY + " INTEGER, "
                + SettingsTable.Cols.FAMSIZE + " INTEGER, "
                + SettingsTable.Cols.SHOPSIZE + " INTEGER, "
                + SettingsTable.Cols.SALARY + " INTEGER, "
                + SettingsTable.Cols.TAXRATE + " REAL, "
                + SettingsTable.Cols.SERVICECOST + " INTEGER, "
                + SettingsTable.Cols.HOUSECOST + " INTEGER, "
                + SettingsTable.Cols.COMMERCIALCOST + " INTEGER, "
                + SettingsTable.Cols.ROADCOST + " INTEGER)"
        );

        db.execSQL("CREATE TABLE " + MapTable.NAME + "("
            //Buildable is a boolean, store 0 for false and 1 for true
                + MapTable.Cols.ID + " INTEGER, "
                + MapTable.Cols.BUILDABLE + " INTEGER, "
                + MapTable.Cols.NORTHEAST + " INTEGER, "
                + MapTable.Cols.NORTHWEST + " INTEGER, "
                + MapTable.Cols.SOUTHEAST + " INTEGER, "
                + MapTable.Cols.SOUTHWEST + " INTEGER, "
                + MapTable.Cols.DRAWABLEID + " INTEGER, "
                + MapTable.Cols.LABEL + " TEXT, "
                + MapTable.Cols.STRUCTNAME + " TEXT)"
        );

        db.execSQL("CREATE TABLE " + GameDataTable.NAME + "("
                + GameDataTable.Cols.ID + " INTEGER, "
                + GameDataTable.Cols.MONEY + " INTEGER, "
                + GameDataTable.Cols.GAME_TIME + " INTEGER, "
                + GameDataTable.Cols.NUM__COMMERCIAL + " INTEGER, "
                + GameDataTable.Cols.NUM_RESIDENTIAL + " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2) {
        db.execSQL("DROP TABLE IF EXISTS " + SettingsTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MapTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GameDataTable.NAME);
        onCreate(db);
    }

}



