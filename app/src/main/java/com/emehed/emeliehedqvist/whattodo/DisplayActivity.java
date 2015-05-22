package com.emehed.emeliehedqvist.whattodo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class DisplayActivity extends Activity implements AsyncResponse, LocationListener{

    double latitude = 0;
    double longitude = 0;
    int radius = 1000;
    String keyword = "";
    WPlace recommendedPlace;
    TextView name;
    TextView phoneNumber;
    TextView address;
    TextView rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        name = (TextView) findViewById(R.id.name);
        //phoneNumber = (TextView) findViewById(R.id.phonenumber);
        address = (TextView) findViewById(R.id.address);

        getLocation();

        Intent intent = getIntent();
        keyword = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        SharedPreferences settings = getSharedPreferences("values",
                Context.MODE_PRIVATE);
        radius = settings.getInt("radius", 0);




         /*else if (button == findViewById(R.id.restaurant)){
            keyword = "restaurant";
        } else if (button == findViewById(R.id.nightClub)){
            keyword = "night_club";
        }*/

        PlaceFinder pf = new PlaceFinder();
        PlaceFinder.DownloadWebpage dw = pf.search(keyword, latitude, longitude, radius);
        dw.delegate = this;

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

    @Override
    public void processFinish(WPlace place) {

        recommendedPlace = place;

        name = (TextView)findViewById(R.id.name);
        name.setText(recommendedPlace.name);

        //phoneNumber = (TextView)findViewById(R.id.phonenumber);
        //phoneNumber.setText(recommendedPlace.phone);

        address = (TextView)findViewById(R.id.address);
        address.setText(recommendedPlace.address);

        rating = (TextView)findViewById(R.id.ratingText);
        rating.setText(recommendedPlace.rating);

    }

    public void mapView(View view) {
       Intent intent = new Intent(this, NoResultActivity.class);
       startActivity(intent);
    }

    public void getLocation(){
        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location From GPS
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(provider, 20000, 0, this);
    }
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
