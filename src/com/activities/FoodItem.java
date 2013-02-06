package com.activities;

public class FoodItem {
    private String foodName;
    private int foodImage;

    public FoodItem(String foodName, int foodImage){
        this.foodName = foodName;
        this.foodImage = foodImage;
    }

    public String getName() {
        return foodName;
    }

    public void setName(String name) {
        this.foodName = foodName;
    }

    public int getImageNumber() {
        return foodImage;
    }

    public void setImageNumber(int imageNumber) {
        this.foodImage = foodImage;
    }
}
