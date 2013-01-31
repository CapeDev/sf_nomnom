package com.example;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class FoodiesBestFriend extends Activity {

    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        addListenerOnButton();

//        TextView text = new TextView(this);
//        text.setText("Hello World!!!!!");
//        setContentView(text);
        ImageView bakalava_image = (ImageView) findViewById(R.id.baklava);
        bakalava_image.setImageResource(R.drawable.baklava);

        ImageView lambchops_image = (ImageView) findViewById(R.id.lambchops);
        lambchops_image.setImageResource(R.drawable.lambchops);

        ImageView mousakka_image = (ImageView) findViewById(R.id.mousakka);
        mousakka_image.setImageResource(R.drawable.mousakka);

    }

    private void addListenerOnButton() {
        final Context context = this;

        button = (Button) findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FoodiesBestFriendList.class);
                startActivity(intent);
            }
        });
    }
}
