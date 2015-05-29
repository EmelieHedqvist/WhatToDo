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
    //processFinished is called when a different thread (away from the main UI thread)is finished in the sub class DownloadWebpage in the class PlaceFinder
    //processFinished is called when a place is found and randomly selected from Google Places Web Services, via PlaceFinder
    //then the information is stored in the variables in an instance of the class WPlace, which is sent to processFinished as in parameter
    @Override
    public void processFinish(WPlace place) {

            //First, a check is done to see if PlaceFinder could find information about a place
        if(place!=null) {
            //Sets the variable in this class to the WPlace instance sent in to this method, which contains information about the randomly selected place which was selected in PlaceFinder
        recommendedPlace = place;
            //Sets lat and lng for the place found
        pLat = recommendedPlace.lat;
        pLong = recommendedPlace.lng;
            //Call for a method that calculates the distans in km between the user and the place
        calcDist();

            //Presents information for the user
        name = (TextView)findViewById(R.id.name);
        name.setText(recommendedPlace.name);

        address = (TextView)findViewById(R.id.address);
        address.setText(recommendedPlace.address);

        rating = (TextView)findViewById(R.id.ratingText);
        rating.setText(recommendedPlace.rating);

        dist = (TextView)findViewById(R.id.distance);
        dist.setText(Double.toString(distance) + " km");

        }
        //If no place information was found, null was sent, and the user is sent to the 'no places found' view
        else  {

            Intent i = new Intent(getApplicationContext(), NoResultActivity.class);
            startActivity(i);
        }
    }
        //This method starts the map view, and it is called when the user presses the map button when a place is presented
    public void mapView(View view) {
        Intent m = new Intent(getApplicationContext(), MapsActivity.class);
        String lat = Double.toString(pLat);
        String lng = Double.toString(pLong);
        String name = recommendedPlace.name;
        String address = recommendedPlace.address;
        //An string array is used to send information via the intent, for place alias and address and lat and lng
        String[] list = new String[]{lat,lng, name, address};
        m.putExtra("pos",list);
        startActivity(m);
    }
    //This method calculates the distance between a place and the users position and stores the information in the double variable 'distance' which is used to present the distance for the user
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
    //This is used by the location listener to update lat and lng when the user changes location
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
    //Methods below have to be implemented when implementing LocationListener, but they are not used by the WhatToDo App
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
