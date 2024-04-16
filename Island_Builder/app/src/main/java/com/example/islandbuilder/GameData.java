package com.example.islandbuilder;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.content.ContentValues.TAG;


public class GameData {

    //For database purposes, number is random
    public static final int GAME_DATA_ID = 7494;
    public static final String RESUME_TEXT = "Found game in progress.";

    private Settings settings;
    private MapData map;
    private int money, gameTime, numResidential, numCommercial;
    private boolean continuing;

    private static GameData instance = null;

    private SQLiteDatabase db; //The database where the data is stored

    public static GameData getInstance(Context con)
    {
        if (instance == null)
        {
            instance = new GameData(con);
        }
        return instance;
    }

    public GameData(Context con)
    {
        db = new IslandDbHelper(con.getApplicationContext()).getWritableDatabase();

        Cursor testCursor = db.rawQuery("SELECT count(*) FROM " + IslandSchema.GameDataTable.NAME, null);
        testCursor.moveToFirst();
        int itemCount = testCursor.getInt(0);
        if (!(itemCount > 0))
        {
            Log.i(TAG, "GameData: NO DB CREATING GAMEDATA: ");
            //No data in the table, use the defaults
            setDefaults();
        }
        else {
            Log.i(TAG, "GameData DATABASE ALREADY EXISTED");
            displayResumePopUp(RESUME_TEXT, con);
        }
    }

    private void setDefaults() {
        continuing = false;

        settings = new Settings(db, continuing);
        map = new  MapData(settings.getMapWidth(), settings.getMapHeight(), db, continuing);
        money = settings.getInitMoney();
        gameTime = 0;
        numResidential = 0;
        numCommercial = 0;

        //deleting old values from the db
        db.delete(IslandSchema.GameDataTable.NAME, null, null);
        ContentValues cv = new ContentValues();

        cv.put(IslandSchema.GameDataTable.Cols.ID, GAME_DATA_ID);
        cv.put(IslandSchema.GameDataTable.Cols.MONEY, money);
        cv.put(IslandSchema.GameDataTable.Cols.GAME_TIME, 0);
        cv.put(IslandSchema.GameDataTable.Cols.NUM_RESIDENTIAL, 0);
        cv.put(IslandSchema.GameDataTable.Cols.NUM__COMMERCIAL, 0);

        db.insert(IslandSchema.GameDataTable.NAME, null, cv);
    }

    private void readDb() {
        continuing = true;

        CursorWrapper cursor = new CursorWrapper(db.query(IslandSchema.GameDataTable.NAME, null, null, null, null, null, null));
        cursor.moveToFirst();

        money = cursor.getInt(cursor.getColumnIndex(IslandSchema.GameDataTable.Cols.MONEY));
        gameTime = cursor.getInt(cursor.getColumnIndex(IslandSchema.GameDataTable.Cols.GAME_TIME));
        numResidential = cursor.getInt(cursor.getColumnIndex(IslandSchema.GameDataTable.Cols.NUM_RESIDENTIAL));
        numCommercial = cursor.getInt(cursor.getColumnIndex(IslandSchema.GameDataTable.Cols.NUM__COMMERCIAL));

        //Map and settings take care of reading from db themselves
        settings = new Settings(db, continuing);
        map = new  MapData(settings.getMapWidth(), settings.getMapHeight(), db, continuing);

        cursor.close();
    }

    public Settings getSettings() {
        return settings;
    }

    public MapData getMap() {
        return map;
    }

    public int getMoney() {
        return money;
    }

    public int getGameTime() {
        return gameTime;
    }

    public int getNumResidential() {
        return numResidential;
    }

    public int getNumCommercial() {
        return numCommercial;
    }

    public boolean isContinuing() {
        return continuing;
    }

    public void increaseNumResidential(int amount) {
        numResidential += amount;

        ContentValues cv = new ContentValues();
        cv.put(IslandSchema.GameDataTable.Cols.NUM_RESIDENTIAL, numResidential);

        db.update(IslandSchema.GameDataTable.NAME, cv, IslandSchema.GameDataTable.Cols.ID + " = ?", new String[]{String.valueOf(GAME_DATA_ID)});
    }

    public void increaseNumCommercial(int amount) {
        numCommercial += amount;

        ContentValues cv = new ContentValues();
        cv.put(IslandSchema.GameDataTable.Cols.NUM__COMMERCIAL, numCommercial);

        db.update(IslandSchema.GameDataTable.NAME, cv, IslandSchema.GameDataTable.Cols.ID + " = ?", new String[]{String.valueOf(GAME_DATA_ID)});
    }

    public void nextDay() {
        gameTime++;

        ContentValues cv = new ContentValues();
        cv.put(IslandSchema.GameDataTable.Cols.GAME_TIME, gameTime);

        db.update(IslandSchema.GameDataTable.NAME, cv, IslandSchema.GameDataTable.Cols.ID + " = ?", new String[]{String.valueOf(GAME_DATA_ID)});
    }

    /*Changes player Money, use -ve amount to subtract money.
        Returns true if a transact5ion was successful. */
    public boolean transaction(int amount)
    {
        boolean returnVal = true;
        if (money + amount >= 0) {
            money += amount;

            ContentValues cv = new ContentValues();
            cv.put(IslandSchema.GameDataTable.Cols.MONEY, money);

            db.update(IslandSchema.GameDataTable.NAME, cv, IslandSchema.GameDataTable.Cols.ID + " = ?", new String[]{String.valueOf(GAME_DATA_ID)});
        } else {
            returnVal = false;
        }

        return returnVal;
    }

    //this is a useful method put here for central access for all other classes
    //Displays a simple popUp with provided text
    public static void displayPopUp(String msg, Context context)
    {
        final Dialog popUp = new Dialog(context);
        popUp.setContentView(R.layout.pop_up);

        TextView txt = popUp.findViewById(R.id.text_box);
        txt.setText(msg);

        Button ok = popUp.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
            }
        });
        popUp.show();
    }

    //Variation of above for asking if they want to resume or start a new game
    public void displayResumePopUp(String msg, Context context)
    {
        final Dialog popUp = new Dialog(context);
        popUp.setContentView(R.layout.resume_pop_up);

        TextView txt = popUp.findViewById(R.id.text_box);
        txt.setText(msg);

        Button continueGame = popUp.findViewById(R.id.continue_game);
        Button startNew = popUp.findViewById(R.id.start_new);


        continueGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
                readDb();
            }
        });

        startNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
                setDefaults();
            }
        });

        //Otherwise would close when they click outside the pop-up
        popUp.setCanceledOnTouchOutside(false);
        popUp.show();
    }

    //If InitMoney updated, money will need to be updated.
    public void resetMoney() {
        money = settings.getInitMoney();
    }

    //If MapWidth and/or MapHeight get changed, the map needs to be re-created accordingly.
    public void updateMap() {
        if ((map.WIDTH != settings.getMapWidth()) || map.HEIGHT != settings.getMapHeight()) {
            map = new  MapData(settings.getMapWidth(), settings.getMapHeight(), db, continuing);
        }
    }
}


