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

        //Connects this class with TextViews and an ImageView in activity_display
        type = (ImageView)findViewById(R.id.type);
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        dist = (TextView) findViewById(R.id.distance);

        //Runs the method getLocation() that receives the users coordinates, and stores them in the double variables latitude and longitude
        getLocation();

        //Reads the message sent with the Intent from MainActivity, to receive information about which button that was pressed
        Intent intent = getIntent();
        keyword = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        SharedPreferences settings = getSharedPreferences("values",
                Context.MODE_PRIVATE);
        radius = settings.getInt("radius", 0);
        //setTypeLogo() is a method that presents right logo on top of the activity_display, according to which button pressed on main_activity
        setTypeLogo();
        //Creates an instance of the class PlaceFinder, which is the class that connects to, and receives data, from Google Web Services
        PlaceFinder pf = new PlaceFinder();
        //This call is to a sub class in PlaceFinder, DownloadWebpage, which runs on a different thread than the main UI, to receive data from Google Web Services on the Internet
        //When the process (on another thread than main UI) in the sub class DownloadWebpage is finished, a call via 'delegate'/ AsyncResponse is made to send
        //an instance of a WPlace object back to this class, DisplayActivity, to the method processFinished.
        PlaceFinder.DownloadWebpage dw = pf.search(keyword, latitude, longitude, radius);
        dw.delegate = this;

    }
    public void setTypeLogo(){
        //This method checks keyword and uses this to present a correct logo on top of the screen, i.e. glass/cutlery/activity/mystery box
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
    //processFinished is called when a different thread is finished in the sub
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
