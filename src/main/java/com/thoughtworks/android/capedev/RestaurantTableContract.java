package com.thoughtworks.android.capedev;

import android.provider.BaseColumns;

public class RestaurantTableContract {

    public static abstract class Restaurant implements BaseColumns{
        public static final String TABLE_NAME = "restaurants";
        public static final String COLUMN_NAME_RESTAURANT_NAME = "name";
    }

    private RestaurantTableContract(){};
}