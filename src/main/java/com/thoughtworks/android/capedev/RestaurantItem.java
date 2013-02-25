package com.thoughtworks.android.capedev;

public class RestaurantItem {
    private String restaurantName;

    public RestaurantItem(String restaurantName){
        this.restaurantName = restaurantName;
    }

    public String getName() {
        return restaurantName;
    }

    public void setName(String name) {
        this.restaurantName = restaurantName;
    }
}
