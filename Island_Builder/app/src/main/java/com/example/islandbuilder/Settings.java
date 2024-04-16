package com.example.islandbuilder;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.islandbuilder.IslandSchema.SettingsTable;

import static android.content.ContentValues.TAG;


/*The Settings class stores all the settings in a database. All changes update the database so that the game can be resumed with the stored settings.
The data is also stored in the class fields of the settings object to make it quicker to retrieve the data, since it will need to be accessed
throughout the game, but only changed at the start of the game.
 */
public class Settings {
    private static int nextId = 0;      //Will only be use an updated value if adding more rows of settings to the table. (Not implemented or required at this point in time)

    private int id, mapWidth, mapHeight, initMoney, familySize, shopSize, salary, serviceCost, houseCost, commercialCost, roadCost;
    private double taxRate;
    private String name;

    private SQLiteDatabase db; //The database where the data is stored

    public Settings(SQLiteDatabase db, boolean continuing)
    {
        this.db = db;
        if (!continuing)
        {
            setDefaultSettings();
            nextId++;
        }
        readDatabase();
    }

    //et the class variables from the table
    private void readDatabase()
    {
        CursorWrapper cursor = new CursorWrapper(db.query(SettingsTable.NAME, null, null, null, null, null, null));
        cursor.moveToFirst();

        id = cursor.getInt(cursor.getColumnIndex(SettingsTable.Cols.ID));
        name = cursor.getString(cursor.getColumnIndex(SettingsTable.Cols.NAME));
        mapWidth = cursor.getInt(cursor.getColumnIndex(SettingsTable.Cols.MAPWIDTH));
        mapHeight = cursor.getInt(cursor.getColumnIndex(SettingsTable.Cols.MAPHEIGHT));
        initMoney = cursor.getInt(cursor.getColumnIndex(SettingsTable.Cols.INITMONEY));
        familySize = cursor.getInt(cursor.getColumnIndex(SettingsTable.Cols.FAMSIZE));
        shopSize = cursor.getInt(cursor.getColumnIndex(SettingsTable.Cols.SHOPSIZE));
        salary = cursor.getInt(cursor.getColumnIndex(SettingsTable.Cols.SALARY));
        taxRate = cursor.getDouble(cursor.getColumnIndex(SettingsTable.Cols.TAXRATE));
        serviceCost = cursor.getInt(cursor.getColumnIndex(SettingsTable.Cols.SERVICECOST));
        houseCost = cursor.getInt(cursor.getColumnIndex(SettingsTable.Cols.HOUSECOST));
        commercialCost = cursor.getInt(cursor.getColumnIndex(SettingsTable.Cols.COMMERCIALCOST));
        roadCost = cursor.getInt(cursor.getColumnIndex(SettingsTable.Cols.ROADCOST));

        cursor.close();
    }

    public void setDefaultSettings()
    {
        //First remove any data that may be there by deleting table...
        db.delete(SettingsTable.NAME, null, null);

        ContentValues cv = new ContentValues();

        cv.put(SettingsTable.Cols.ID, nextId);
        cv.put(SettingsTable.Cols.NAME, "My Island");
        cv.put(SettingsTable.Cols.MAPWIDTH, 50);
        cv.put(SettingsTable.Cols.MAPHEIGHT, 10);
        cv.put(SettingsTable.Cols.INITMONEY, 1000);
        cv.put(SettingsTable.Cols.FAMSIZE, 4);
        cv.put(SettingsTable.Cols.SHOPSIZE, 6);
        cv.put(SettingsTable.Cols.SALARY, 10);
        cv.put(SettingsTable.Cols.TAXRATE, 0.3);
        cv.put(SettingsTable.Cols.SERVICECOST, 2);
        cv.put(SettingsTable.Cols.HOUSECOST, 100);
        cv.put(SettingsTable.Cols.COMMERCIALCOST, 500);
        cv.put(SettingsTable.Cols.ROADCOST, 20);

        db.insert(SettingsTable.NAME, null, cv);
    }

    private int convertToInteger(String value)
    {
        try{
            return Integer.parseInt(value);
        } catch (NumberFormatException e) { throw new IllegalArgumentException(); }
    }

    private Double convertToDouble(String value)
    {
        try{
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {throw new IllegalArgumentException();}
    }

    //Setters change both the objects version and the database version.
    //They all take String arguments allowing them to be treated similarly in a for loop in the
    // SettingsActivity
    public int getId()
    {
        return id;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(String mapWidthStr) {
        int mapWidth = convertToInteger(mapWidthStr);
        if (mapWidth > 0) {
            this.mapWidth = mapWidth;

            ContentValues cv = new ContentValues();
            cv.put(SettingsTable.Cols.MAPWIDTH, mapWidth);

            db.update(SettingsTable.NAME, cv, SettingsTable.Cols.ID + " = ?", new String[]{String.valueOf(id)});
        } else { throw new IllegalArgumentException(); }
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(String mapHeightStr) {
        int mapHeight = convertToInteger(mapHeightStr);
        if (mapHeight > 0) {
            this.mapHeight = mapHeight;
            ContentValues cv = new ContentValues();
            cv.put(SettingsTable.Cols.MAPHEIGHT, mapHeight);

            db.update(SettingsTable.NAME, cv, SettingsTable.Cols.ID + " = ?", new String[]{String.valueOf(id)});
        } else { throw new IllegalArgumentException();}
    }

    public int getInitMoney() {
        return initMoney;
    }

    public void setInitMoney(String initMoneyStr) {
        int initMoney = convertToInteger(initMoneyStr);
        if (initMoney > 0) {
            this.initMoney = initMoney;
            ContentValues cv = new ContentValues();
            cv.put(SettingsTable.Cols.INITMONEY, initMoney);

            db.update(SettingsTable.NAME, cv, SettingsTable.Cols.ID + " = ?", new String[]{String.valueOf(id)});
        } else { throw new IllegalArgumentException();}
    }

    public int getFamilySize() {
        return familySize;
    }

    public void setFamilySize(String familySizeStr) throws IllegalArgumentException{
        int familySize = convertToInteger(familySizeStr);
        //Family of 1 is possible.
        if (familySize > 0) {
            this.familySize = familySize;
            ContentValues cv = new ContentValues();
            cv.put(SettingsTable.Cols.FAMSIZE, familySize);

            db.update(SettingsTable.NAME, cv, SettingsTable.Cols.ID + " = ?", new String[]{String.valueOf(id)});
        } else { throw new IllegalArgumentException();}
    }

    public int getShopSize() {
        return shopSize;
    }

    public void setShopSize(String shopSizeStr) {
        int shopSize = convertToInteger(shopSizeStr);
        if (shopSize > 0) {
            this.shopSize = shopSize;
            ContentValues cv = new ContentValues();
            cv.put(SettingsTable.Cols.SHOPSIZE, shopSize);

            db.update(SettingsTable.NAME, cv, SettingsTable.Cols.ID + " = ?", new String[]{String.valueOf(id)});
        } else { throw new IllegalArgumentException();}
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(String salaryStr) {
        int salary = convertToInteger(salaryStr);
        if (salary > 0) {
            this.salary = salary;
            ContentValues cv = new ContentValues();
            cv.put(SettingsTable.Cols.SALARY, salary);

            db.update(SettingsTable.NAME, cv, SettingsTable.Cols.ID + " = ?", new String[]{String.valueOf(id)});
        } else { throw new IllegalArgumentException();}
    }

    public int getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(String serviceCostStr) {
        int serviceCost = convertToInteger(serviceCostStr);
        if (serviceCost > 0) {
            this.serviceCost = serviceCost;
            ContentValues cv = new ContentValues();
            cv.put(SettingsTable.Cols.SERVICECOST, serviceCost);

            db.update(SettingsTable.NAME, cv, SettingsTable.Cols.ID + " = ?", new String[]{String.valueOf(id)});
        } else { throw new IllegalArgumentException();}
    }

    public int getHouseCost() {
        return houseCost;
    }

    public void setHouseCost(String houseCostStr) {
        int houseCost = convertToInteger(houseCostStr);
        if (houseCost > 0) {
            this.houseCost = houseCost;
            ContentValues cv = new ContentValues();
            cv.put(SettingsTable.Cols.HOUSECOST, houseCost);

            db.update(SettingsTable.NAME, cv, SettingsTable.Cols.ID + " = ?", new String[]{String.valueOf(id)});
        } else { throw new IllegalArgumentException();}
    }

    public int getCommercialCost() {
        return commercialCost;
    }

    public void setCommercialCost(String commercialCostStr) {
        int commercialCost = convertToInteger(commercialCostStr);
        if (commercialCost > 0) {
            this.commercialCost = commercialCost;
            ContentValues cv = new ContentValues();
            cv.put(SettingsTable.Cols.COMMERCIALCOST, commercialCost);

            db.update(SettingsTable.NAME, cv, SettingsTable.Cols.ID + " = ?", new String[]{String.valueOf(id)});
        } else { throw new IllegalArgumentException();}
    }

    public int getRoadCost() {
        return roadCost;
    }

    public void setRoadCost(String roadCostStr) {
        int roadCost = convertToInteger(roadCostStr);
        if (roadCost > 0) {
            this.roadCost = roadCost;
            ContentValues cv = new ContentValues();
            cv.put(SettingsTable.Cols.ROADCOST, roadCost);

            db.update(SettingsTable.NAME, cv, SettingsTable.Cols.ID + " = ?", new String[]{String.valueOf(id)});
        } else {
            throw new IllegalArgumentException();
        }
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRateStr) {
        Double taxRate = convertToDouble(taxRateStr);
        if (taxRate > 0 && taxRate < 1) {
            this.taxRate = taxRate;
            ContentValues cv = new ContentValues();
            cv.put(SettingsTable.Cols.TAXRATE, taxRate);

            db.update(SettingsTable.NAME, cv, SettingsTable.Cols.ID + " = ?", new String[]{String.valueOf(id)});
        } else { throw new IllegalArgumentException();}
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        ContentValues cv = new ContentValues();
        cv.put(SettingsTable.Cols.NAME, name);

        db.update(SettingsTable.NAME, cv, SettingsTable.Cols.ID + " = ?", new String[]{String.valueOf(id)});
    }
}
