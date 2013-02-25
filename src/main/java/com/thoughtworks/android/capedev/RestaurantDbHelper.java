package com.thoughtworks.android.capedev;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.thoughtworks.android.capedev.RestaurantTableContract.*;

public class RestaurantDbHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Restaurants.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Restaurant.TABLE_NAME + " (" +
                    Restaurant._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    Restaurant.COLUMN_NAME_RESTAURANT_NAME + TEXT_TYPE +
                    " )";

    public RestaurantDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + Restaurant.TABLE_NAME);
        onCreate(database);
    }

    public void destroyDatabase(SQLiteDatabase database){
        database.execSQL("DROP TABLE IF EXISTS " + Restaurant.TABLE_NAME);
        onCreate(database);
    }
}
