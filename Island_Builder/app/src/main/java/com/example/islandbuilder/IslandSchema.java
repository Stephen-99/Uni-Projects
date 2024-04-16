package com.example.islandbuilder;

public class IslandSchema {
    public static class SettingsTable
    {
        public static final String NAME = "settings";

        //Stores column names
        public static class Cols
        {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String MAPWIDTH = "map_width";
            public static final String MAPHEIGHT = "map_height";
            public static final String INITMONEY = "initial_money";
            public static final String FAMSIZE = "family_size";
            public static final String SHOPSIZE = "shop_size";
            public static final String SALARY = "salary";
            public static final String TAXRATE = "tax_rate";
            public static final String SERVICECOST = "service_cost";
            public static final String HOUSECOST = "house_cost";
            public static final String COMMERCIALCOST = "commercial_cost";
            public static final String ROADCOST = "road_cost";
        }
    }

    //Each row represents a MapElement
    public static class MapTable
    {
        public static final String NAME = "map";

        public static class Cols
        {
            //could add image here too...
            public static final String ID = "id";
            public static final String BUILDABLE = "buildable";
            public static final String NORTHWEST = "north_west";
            public static final String SOUTHWEST = "south_west";
            public static final String NORTHEAST = "north_east";
            public static final String SOUTHEAST = "south_east";

            //structure fields
            public static final String DRAWABLEID = "drawable_id";
            public static final String LABEL = "label";
            public static final String STRUCTNAME = "struct_name";
        }
    }

    public static class GameDataTable
    {
        public static final String NAME = "game_data";

        public static class Cols {
            public static final String ID = "id";
            public static final String MONEY = "money";
            public static final String GAME_TIME = "gameTime";
            public static final String NUM_RESIDENTIAL = "num_residential";
            public static final String NUM__COMMERCIAL = "num_commercial";
        }
    }
}
