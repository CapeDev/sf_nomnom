package com.thoughtworks.android.capedev;

import android.content.Context;
import com.xtremelabs.robolectric.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class FoodItemListAdapterTest {

    @Test
    public void shouldBeTrue(){
        assertTrue(true);
    }

    @Test
    public void shouldReturnCount(){
        ArrayList<FoodItem> foods = new ArrayList<FoodItem>(10);
        foods.add(new FoodItem("baklava", 1));
        foods.add(new FoodItem("tiramisu", 2));
        Context contextMock = mock(Context.class, Mockito.CALLS_REAL_METHODS); //for mocking abstract classes with mockito

        FoodItemListAdapter adapter = new FoodItemListAdapter(contextMock, foods);

        assertEquals(adapter.getCount(), 2);
    }
}
