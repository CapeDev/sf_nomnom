package com.thoughtworks.android.capedev.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.thoughtworks.android.capedev.R;

public class NavigableActivity extends Activity {
    @Override public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case R.id.add:
                intent = new Intent(this, CameraActivity.class);
                startActivity(intent);
                return true;
            case R.id.search:
                intent = new Intent(this, Search.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }
}
