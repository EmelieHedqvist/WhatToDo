package com.emehed.emeliehedqvist.whattodo;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity{


    double latitude = 0;
    double longitude = 0;
    String keyword = "";
    int radius;
    Place recommendedPlace;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void switchLayout(View view){
        setContentView(R.layout.activity_display);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void createChoice(View view){
        View button = view;

        if (button == findViewById(R.id.bar)){
            keyword = "bar";
        } /*else if (button == findViewById(R.id.restaurant)){
            keyword = "restaurant";
        } else if (button == findViewById(R.id.nightClub)){
            keyword = "night_club";
        }*/

        setContentView(R.layout.activity_display);
        radius = 1000;
        SearcherDummy sd = new SearcherDummy();
        recommendedPlace = sd.search(keyword, latitude, longitude, radius);
        tv = (TextView)findViewById(R.id.name);
        tv.setText(recommendedPlace.name);
    }

    public void settingView(View view){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);

        //TextView dis = (TextView)findViewById(R.id.distance);
    }



}
