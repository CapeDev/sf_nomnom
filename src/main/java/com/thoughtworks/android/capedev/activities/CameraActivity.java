package com.thoughtworks.android.capedev.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.thoughtworks.android.capedev.R;
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
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        this.imageView = (ImageView) this.findViewById(R.id.imageView1);
        Button photoButton = (Button) this.findViewById(R.id.button1);
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
            imageView.setImageBitmap(photo);

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 90, bao);
            byte[] ba = bao.toByteArray();
            String ba1 = Base64.encodeBytes(ba);

            List nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("name", "bearclaw"));
            nameValuePairs.add(new BasicNameValuePair("longitude", "37.783448"));
            nameValuePairs.add(new BasicNameValuePair("latitude", "-122.417731"));
            nameValuePairs.add(new BasicNameValuePair("restaurant", "Coffee Shop"));
            nameValuePairs.add(new BasicNameValuePair("image", ba1));
            new PostFoodItem().execute(nameValuePairs);
        }
    }

    private class PostFoodItem extends AsyncTask<List<NameValuePair>, Integer, Integer> {

        @Override
        protected Integer doInBackground(List<NameValuePair>... nameValuePairs) {
            for (List<NameValuePair> nameValuePair : nameValuePairs){
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://10.0.2.2:3000/add");

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));

                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                } catch (Exception e) {
                    Log.i("Submitting picture", e.getStackTrace().toString());
                }
            }

            return 1;
        }
    }
}
