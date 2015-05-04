package com.emehed.emeliehedqvist.whattodo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class DisplayActivity extends AppCompatActivity {

    double latitude = 0;
    double longitude = 0;
    String keyword = "";
    int radius;
    Place recommendedPlace;
    TextView name;
    TextView phoneNumber;
    TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        if (message == "bar"){
            keyword = "bar";
        } /*else if (button == findViewById(R.id.restaurant)){
            keyword = "restaurant";
        } else if (button == findViewById(R.id.nightClub)){
            keyword = "night_club";
        }*/

        radius = 1000;
        SearcherDummy sd = new SearcherDummy();
        recommendedPlace = sd.search(keyword, latitude, longitude, radius);

        name = (TextView)findViewById(R.id.name);
        name.setText(recommendedPlace.name);

        phoneNumber = (TextView)findViewById(R.id.number);
        phoneNumber.setText(recommendedPlace.phone);

        address = (TextView)findViewById(R.id.address);
        address.setText(recommendedPlace.address);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
