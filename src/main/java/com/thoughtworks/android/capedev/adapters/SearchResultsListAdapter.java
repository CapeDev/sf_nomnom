package com.thoughtworks.android.capedev.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.thoughtworks.android.capedev.R;
import com.thoughtworks.android.capedev.domain.SearchResult;

import java.util.ArrayList;

public class SearchResultsListAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<SearchResult> searchResults;

    public SearchResultsListAdapter(Context context, ArrayList<SearchResult> results) {
        searchResults = results;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override public int getCount() {
        return searchResults.size();
    }

    @Override public Object getItem(int position) {
        return searchResults.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
           view = layoutInflater.inflate(R.layout.food_item_row, null);
           holder = new ViewHolder();
           holder.foodName = (TextView) view.findViewById(R.id.food_name);
           holder.restaurantName = (TextView) view.findViewById(R.id.restaurant_name);
           holder.foodImage = (WebView) view.findViewById(R.id.food_image);

           view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.foodName.setText(searchResults.get(position).getResultName());
        holder.restaurantName.setText(searchResults.get(position).getResultLocation());

        final String mimeType = "text/html";
        final String encoding = "utf-8";
        final String html = "<html><head><style>* {margin:0;padding:0;}</style></head><img height=\"80\" width=\"100\" src=\"" + searchResults.get(position).getGooglePlacesPictureUrl() + "\"/></html>";
        holder.foodImage.loadData(html, mimeType, encoding);

        return view;
    }

    static class ViewHolder {
      TextView foodName;
      TextView restaurantName;
      WebView foodImage;
     }
}
