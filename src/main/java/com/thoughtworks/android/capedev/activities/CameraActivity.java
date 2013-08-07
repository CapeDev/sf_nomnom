package com.thoughtworks.android.capedev.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.thoughtworks.android.capedev.constants.Domain;
import net.iharder.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class CameraActivity extends NavigableActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView foodPicture;

    private EditText restaurantName;
    private EditText foodName;

    private Tracker tracker;

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        tracker = new Tracker(context);

        setContentView(R.layout.camera);
        foodPicture = (ImageView) this.findViewById(R.id.foodImage);
        restaurantName = (EditText) this.findViewById(R.id.restaurantName);
        foodName = (EditText) this.findViewById(R.id.foodName);

        Button submitButton = (Button) this.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                if (foodName.getText().length() == 0) {
                    toast = Toast.makeText(context, "Enter the name of the food!", Toast.LENGTH_SHORT);
                } else if (restaurantName.getText().length() == 0) {
                    toast = Toast.makeText(context, "Enter the name of the restaurant!", Toast.LENGTH_SHORT);
                }
                else {
                    tracker.getLocation();

                    Bitmap photo = ((BitmapDrawable) foodPicture.getDrawable()).getBitmap();

                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 90, bao);
                    byte[] ba = bao.toByteArray();
                    String ba1 = Base64.encodeBytes(ba);

                    List nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("name", foodName.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(tracker.getLongitude())));
                    nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(tracker.getLatitude())));
                    nameValuePairs.add(new BasicNameValuePair("restaurant", restaurantName.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("image", ba1));
                    tracker.stopTracking();

                    new PostFoodItem().execute(nameValuePairs);

                    toast = Toast.makeText(getApplicationContext(), "Submitted!", Toast.LENGTH_SHORT);
                }
                toast.show();
            }
        });


        Button photoButton = (Button) this.findViewById(R.id.startCamera);
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            foodPicture.setImageBitmap(photo);
        }
    }

    private class PostFoodItem extends AsyncTask<List<NameValuePair>, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(List<NameValuePair>... nameValuePairs) {
            for (List<NameValuePair> nameValuePair : nameValuePairs) {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(Domain.SERVER_URL + "/add");

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));

                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                } catch (Exception e) {
                    Log.i("Submitting picture", e.getStackTrace().toString());
                }
            }

            return true;
        }
    }
}
