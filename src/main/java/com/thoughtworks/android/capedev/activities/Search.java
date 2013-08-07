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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Search extends NavigableActivity {

    private SearchView searchBar;
    private ListView resultsList;

    private SearchResultsListAdapter searchResultsAdapter;
    private ArrayList<SearchResult> results = new ArrayList<SearchResult>();


    private float latitude = (float) 37.76313;
    private float longitude = (float) -122.42398;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.search);

        searchBar = (SearchView) findViewById(R.id.search_bar);
        int searchBarId = searchBar.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView searchText = (TextView) findViewById(searchBarId);
        searchText.setTextColor(Color.WHITE);

        searchResultsAdapter = new SearchResultsListAdapter(this, results);
        resultsList = (ListView) findViewById(R.id.results_list);
        resultsList.setAdapter(searchResultsAdapter);

        final TextView latitudeTextView = (TextView) findViewById(R.id.latitude);
        latitudeTextView.setTextColor(Color.RED);
        latitudeTextView.setText("Latitiude:    " + String.valueOf(latitude));

        final TextView longitudeTextView = (TextView) findViewById(R.id.longitude);
        longitudeTextView.setTextColor(Color.RED);
        longitudeTextView.setText("Longitude    " + String.valueOf(longitude));

        Button locator = (Button) findViewById(R.id.locator_button);
        locator.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        latitudeTextView.setText("Latitiude:    " + String.valueOf(location.getLatitude()));
                        longitudeTextView.setText("Longitude    " + String.valueOf(location.getLongitude()));
                        locationManager.removeUpdates(this);
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                    public void onProviderEnabled(String provider) {}

                    public void onProviderDisabled(String provider) {}
                  };

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        });

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchTerm) {
                results.clear();
                searchResultsAdapter.notifyDataSetChanged();

                final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                String currentLatitude = (location!=null) ? String.valueOf(location.getLatitude()) : String.valueOf(latitude);
                String currentLongitude = (location!=null) ? String.valueOf(location.getLongitude()) : String.valueOf(longitude);
                String query = null;
                try {
                    query = URLEncoder.encode(searchTerm, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String requestParameters = String.format("search_term=%s&latitude=%s&longitude=%s", query, currentLatitude , currentLongitude);
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

            String restaurantName = "";
            String dishName = "";
            String picture = "";
            String distance = "";

            for (int i=0; i < jsonArray.length(); i++){
                try {
                    JSONObject resultsJson = jsonArray.getJSONObject(i);
                    dishName = resultsJson.getString("name");
                    restaurantName = resultsJson.getString("restaurant");
                    picture = resultsJson.getString("picture");
                    distance = resultsJson.getString("distance_away");

                    Log.d("distance", distance);
                    results.add(new SearchResult(dishName, restaurantName, picture, distance));
                    searchResultsAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
