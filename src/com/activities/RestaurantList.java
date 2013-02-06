package com.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class RestaurantList extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.restaurant_list);

        ListView listView = (ListView) findViewById(R.id.list_of_restaurants);

        ArrayList<RestaurantItem> restaurants = new ArrayList<RestaurantItem>();
        restaurants.add(new RestaurantItem("kokkari"));
        restaurants.add(new RestaurantItem("pizza orgasmica"));
        restaurants.add(new RestaurantItem("palominos"));
        restaurants.add(new RestaurantItem("Boccadillos"));

        RestaurantListAdapter adapter = new RestaurantListAdapter(this, restaurants);

        listView.setAdapter(adapter);
        final Context context = this;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, FoodiesBestFriendList.class);
                startActivity(intent);
            }
        });
    }
}
