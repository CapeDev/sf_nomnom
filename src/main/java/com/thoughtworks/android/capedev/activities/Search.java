package com.thoughtworks.android.capedev.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.thoughtworks.android.capedev.R;
import com.thoughtworks.android.capedev.Tools.Tracker;
import com.thoughtworks.android.capedev.adapters.SearchResultsListAdapter;
import com.thoughtworks.android.capedev.constants.Domain;
import com.thoughtworks.android.capedev.domain.SearchResult;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Search extends NavigableActivity {

    private SearchView searchBar;
    private ListView resultsList;

    private SearchResultsListAdapter searchResultsAdapter;
    private ArrayList<SearchResult> results = new ArrayList<SearchResult>();

    private Context context;

    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        tracker = new Tracker(context);

        setContentView(R.layout.search);

        searchBar = (SearchView) findViewById(R.id.search_bar);
        int searchBarId = searchBar.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView searchText = (TextView) findViewById(searchBarId);
        searchText.setTextColor(Color.WHITE);

        searchResultsAdapter = new SearchResultsListAdapter(this, results);
        resultsList = (ListView) findViewById(R.id.results_list);
        resultsList.setAdapter(searchResultsAdapter);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchTerm) {
                results.clear();
                searchResultsAdapter.notifyDataSetChanged();

                tracker.getLocation();

                String query = null;
                try {
                    query = URLEncoder.encode(searchTerm, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String requestParameters = String.format("search_term=%s", query);
                Log.d("RequestParameters", requestParameters);

                Log.i("RequestUrl", Domain.SERVER_URL + "/search?" + requestParameters);

                new GetJson().execute(Domain.SERVER_URL + "/search?" + requestParameters);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


    }

    public static JSONArray doGet(String url) {
        JSONArray json = null;
        BufferedReader in = null;
        try {
            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "android");
            HttpGet request = new HttpGet();
            request.setHeader("Content-Type", "text/plain; charset=utf-8");
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";

            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            String page = sb.toString();

            return new JSONArray(page);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return json;
    }

    private class GetJson extends AsyncTask<String, Integer, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... urls) {
            int count = urls.length;
            Log.d("urls.length", String.valueOf(urls.length));

            JSONArray json = null;
            for (int i = 0; i < count; i++) {
                json = doGet(urls[i]);
            }
            return json;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            Toast toast;

            if (jsonArray.length() == 0){
                toast = Toast.makeText(context, "No results found!!", Toast.LENGTH_SHORT);
                toast.show();

                searchBar.setQuery("",false);

                return;
            }


            for (int i=0; i < jsonArray.length(); i++){
                try {
                    JSONObject resultsJson = jsonArray.getJSONObject(i);
                    String dishName = resultsJson.getString("name");
                    String restaurantName = resultsJson.getString("restaurant");
                    String picture = resultsJson.getString("picture");

                    double foodLatitude = resultsJson.getDouble("latitude");
                    double foodLongitude = resultsJson.getDouble("longitude");

                    float [] result = new float[1];
                    Location.distanceBetween(tracker.getLatitude(),tracker.getLongitude(),foodLatitude,foodLongitude, result);
                    double distanceInKm =  (result[0]/1000.0 * 0.621);
                    double distanceInMiles =  (Math.round(distanceInKm*100.0)/100.0);

                    results.add(new SearchResult(dishName, restaurantName, picture, Double.toString(distanceInMiles)));
                    searchResultsAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            tracker.stopTracking();
        }
    }
}
