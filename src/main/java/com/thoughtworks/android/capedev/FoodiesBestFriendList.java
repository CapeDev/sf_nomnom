package com.thoughtworks.android.capedev;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import java.util.ArrayList;

public class FoodiesBestFriendList extends Activity {

    Button button;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list);

        addListenerOnButton();

        ListView listView = (ListView) findViewById(R.id.list_of_food_items);

        ArrayList<FoodItem> foodItems = new ArrayList<FoodItem>();
        foodItems.add(new FoodItem("baklava", 1));
        foodItems.add(new FoodItem("lamb chops", 2));
        foodItems.add(new FoodItem("moussaka", 3));

        FoodItemListAdapter adapter = new FoodItemListAdapter(this, foodItems);

        listView.setAdapter(adapter);
    }

    private void addListenerOnButton() {
        final Context context = this;

        button = (Button) findViewById(R.id.back_button);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RestaurantList.class);
                startActivity(intent);
            }
        });
    }
}
