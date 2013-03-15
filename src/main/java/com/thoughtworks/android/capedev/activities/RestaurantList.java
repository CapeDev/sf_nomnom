package com.thoughtworks.android.capedev.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.thoughtworks.android.capedev.*;
import com.thoughtworks.android.capedev.adapters.RestaurantListAdapter;
import com.thoughtworks.android.capedev.domain.RestaurantItem;

import java.util.ArrayList;

public class RestaurantList extends Activity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_list);

        final Context context = this;
        RestaurantDbHelper mDbHelper = new RestaurantDbHelper(context);


        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDbHelper.destroyDatabase(db);

        Cursor cursor = GetNumberOfExistingRestaurants(mDbHelper);

        if (cursor.getCount() == 0){
            CreateRestaurantList(mDbHelper);
            cursor = GetNumberOfExistingRestaurants(mDbHelper);
        }

        ArrayList<RestaurantItem> restaurantsFromDatabase = new ArrayList<RestaurantItem>();

        if (cursor.moveToFirst()) {
            do {
                restaurantsFromDatabase.add(new RestaurantItem(cursor.getString(0)));
            } while (cursor.moveToNext());
        }

        ListView listView = (ListView) findViewById(R.id.list_of_restaurants);

        RestaurantListAdapter adapter = new RestaurantListAdapter(this, restaurantsFromDatabase);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, RestaurantDetails.class);
                startActivity(intent);
            }
        });
    }

    private Cursor GetNumberOfExistingRestaurants(RestaurantDbHelper mDbHelper) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        String[] requiredColumns = {
                RestaurantTableContract.Restaurant.COLUMN_NAME_RESTAURANT_NAME
        };

        Cursor cursor = database.query(
                RestaurantTableContract.Restaurant.TABLE_NAME,
                requiredColumns,
                null,null,null,null,null
                );

        return cursor;
    }

    private void CreateRestaurantList(RestaurantDbHelper mDbHelper) {
        String[] listOfRestaurants = new String[]{
                "Kokkari",
                "Pizza Orgasmica",
                "Palominos",
                "Bocadillos"
        };

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        for(String restaurant : listOfRestaurants){
            ContentValues values = new ContentValues();
            values.put(RestaurantTableContract.Restaurant.COLUMN_NAME_RESTAURANT_NAME, restaurant);
            db.insert(RestaurantTableContract.Restaurant.TABLE_NAME, null, values);
        }

        db.close();
    }
}
