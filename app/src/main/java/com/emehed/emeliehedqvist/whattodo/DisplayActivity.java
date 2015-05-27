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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

//This class implements two interfaces,
//First, AsyncResponse is implemented for enabling a call to the method processFinished in this class, when a thread,
//away from the UI thread, is finished in the class PlaceFinder
//LocationListener is implemented to enable for this class to access the coordinates of the users location in the getLocation() method
public class DisplayActivity extends Activity implements AsyncResponse, LocationListener{
    //latitude & longitude = users location, pLat & pLong = location of found place,
    // distance = calculated distance (km) between user and found place, calculated in method calcDist()
    double latitude, longitude, pLat, pLong, distance;

    int radius; //Stores the search radius chosen by the user
    String keyword; //Stores the keyword chosen via button pressed on main screen 'bar, restaurant, activity or random (mystery box)'
    WPlace recommendedPlace; //Stores an instance of the class WPlace, received via AsyncResponse from PlaceFinder
    TextView name, address, rating, dist; //Text views in activity_display
    ImageView type; //Image view in activity_display

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        type = (ImageView)findViewById(R.id.type);
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        dist = (TextView) findViewById(R.id.distance);

        getLocation();

        Intent intent = getIntent();
        keyword = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        SharedPreferences settings = getSharedPreferences("values",
                Context.MODE_PRIVATE);
        radius = settings.getInt("radius", 0);
        setTypeLogo();

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
    public void setTypeLogo(){
        if(keyword.equals("bar")){
            type.setImageResource(R.drawable.drink);
        }
        else if(keyword.equals("restaurant")){
            type.setImageResource(R.drawable.eat);
        }
        else if(keyword.equals("activity")){
            type.setImageResource(R.drawable.activities);
        }
        else{
            type.setImageResource(R.drawable.random);
        }

    }

    @Override
    public void processFinish(WPlace place) {


        if(place!=null) {
        recommendedPlace = place;
        pLat = recommendedPlace.lat;
        pLong = recommendedPlace.lng;
        calcDist();

        name = (TextView)findViewById(R.id.name);
        name.setText(recommendedPlace.name);

        address = (TextView)findViewById(R.id.address);
        address.setText(recommendedPlace.address);

        rating = (TextView)findViewById(R.id.ratingText);
        rating.setText(recommendedPlace.rating);

        dist = (TextView)findViewById(R.id.distance);
        dist.setText(Double.toString(distance) + " km");

        }
        else  {
            Intent i = new Intent(getApplicationContext(), NoResultActivity.class);
            startActivity(i);
        }
    }

    public void mapView(View view) {
        Intent m = new Intent(getApplicationContext(), MapsActivity.class);
        String lat = Double.toString(pLat);
        String lng = Double.toString(pLong);
        String name = recommendedPlace.name;
        String address = recommendedPlace.address;
        String[] list = new String[]{lat,lng, name, address};
        m.putExtra("pos",list);
        startActivity(m);
    }
    public void calcDist(){
        Location myLocation = new Location("MyLocation");

        myLocation.setLatitude(latitude);
        myLocation.setLongitude(longitude);

        Location pLocation = new Location("PlaceLocation");

        pLocation.setLatitude(pLat);
        pLocation.setLongitude(pLong);
        //Rounds to two decimals and changes meter to km
        distance = (Math.round((myLocation.distanceTo(pLocation)/1000)*100.0)/100.0);
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
