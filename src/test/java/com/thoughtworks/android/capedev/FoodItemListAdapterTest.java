package com.thoughtworks.android.capedev;

import com.thoughtworks.android.capedev.domain.SearchResult;
import com.xtremelabs.robolectric.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class FoodItemListAdapterTest {

    @Test
    public void shouldBeAnExampleTest(){
        ArrayList<SearchResult> searchResults = new ArrayList<SearchResult>(10);
        assertEquals(searchResults.size(), 0);
    }
}
