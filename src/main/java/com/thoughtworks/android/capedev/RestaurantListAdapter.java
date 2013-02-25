package com.thoughtworks.android.capedev;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class RestaurantListAdapter extends BaseAdapter {
    private static ArrayList<RestaurantItem> restaurantsList;
    private LayoutInflater l_Inflater;

    public RestaurantListAdapter(Context context, ArrayList<RestaurantItem> restaurantsList){
        this.restaurantsList = restaurantsList;
        l_Inflater = LayoutInflater.from(context);
    }

    @Override public int getCount() {
        return restaurantsList.size();
    }

    @Override public Object getItem(int position) {
        return restaurantsList.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
           view = l_Inflater.inflate(R.layout.restaurant_item_row, null);
           holder = new ViewHolder();
           holder.restaurantName = (TextView) view.findViewById(R.id.restaurant_name);
           view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.restaurantName.setText(restaurantsList.get(position).getName());
        return view;
    }

    static class ViewHolder {
      TextView restaurantName;
     }
}
