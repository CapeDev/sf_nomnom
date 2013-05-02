package com.thoughtworks.android.capedev.activities;

import android.app.ListActivity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import com.thoughtworks.android.capedev.R;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class SearchWithLayout extends ListActivity {

    private SearchView searchBar;
    private ListView resultList;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> restaurants = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search);

        searchBar = (SearchView) findViewById(R.id.search_bar);
        int searchBarId = searchBar.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView searchText = (TextView) findViewById(searchBarId);
        searchText.setTextColor(Color.WHITE);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, restaurants);
        setListAdapter(adapter);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchTerm) {
                restaurants.clear();
                adapter.notifyDataSetChanged();
                new GetJson().execute("http://10.0.2.2:3000/search/" + searchTerm);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    public static JSONObject doGet(String url) {
        JSONObject json = null;
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
            //System.out.println(page);
            return new JSONObject(page);
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

    private class GetJson extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... urls) {
            int count = urls.length;
            Log.d("urls.length", String.valueOf(urls.length));

            JSONObject json = null;
            for (int i = 0; i < count; i++) {
                json = doGet(urls[i]);
            }
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            JSONArray json = null;

            try {
                json = jsonObject.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String restaurantName = "";
            String dishName = "";

            for (int i=0; i < json.length(); i++){

                try {
                    JSONObject resultsJson = json.getJSONObject(i);
                    dishName = resultsJson.getString("name");
                    restaurantName = resultsJson.getJSONObject("_links").getString("parent");

                    restaurants.add(dishName + " @ " + restaurantName );
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
