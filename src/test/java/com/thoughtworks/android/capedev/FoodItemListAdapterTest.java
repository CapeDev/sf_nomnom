package com.thoughtworks.android.capedev;

import com.thoughtworks.android.capedev.adapters.FoodItemListAdapter;
import com.thoughtworks.android.capedev.domain.FoodItem;
import com.xtremelabs.robolectric.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class FoodItemListAdapterTest {

    @Test
    public void shouldReturnCount(){
        ArrayList<FoodItem> foods = new ArrayList<FoodItem>(10);
        foods.add(new FoodItem("baklava", 1));
        foods.add(new FoodItem("tiramisu", 2));

        FoodItemListAdapter adapter = new FoodItemListAdapter(Robolectric.application.getApplicationContext(), foods);

        assertEquals(adapter.getCount(), 2);
    }
}
