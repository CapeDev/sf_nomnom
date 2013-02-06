package com.pages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodItemListAdapter extends BaseAdapter {
    private static ArrayList<FoodItem> foodItemsList;
    private LayoutInflater l_Inflater;

    private Integer[] imgid = {
      R.drawable.baklava,
      R.drawable.lambchops,
      R.drawable.mousakka
      };

    public FoodItemListAdapter(Context context, ArrayList<FoodItem> foodItems){
        foodItemsList = foodItems;
        l_Inflater = LayoutInflater.from(context);
    }

    @Override public int getCount() {
        return foodItemsList.size();
    }

    @Override public Object getItem(int position) {
        return foodItemsList.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
           view = l_Inflater.inflate(R.layout.food_item_row, null);
           holder = new ViewHolder();
           holder.foodName = (TextView) view.findViewById(R.id.food_name);
           holder.foodImage = (ImageView) view.findViewById(R.id.food_image);

           view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.foodName.setText(foodItemsList.get(position).getName());
        holder.foodImage.setImageResource(imgid[foodItemsList.get(position).getImageNumber() - 1]);

        return view;
    }

    static class ViewHolder {
      TextView foodName;
      ImageView foodImage;
     }
}
