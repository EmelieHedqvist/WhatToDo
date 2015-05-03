package com.example.henriktaljedal.test;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements LocationListener{

    double latitude = 0;
    double longitude = 0;
    String keyword = "";
    int radius;
    Place recommendedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getMyLocation();


        whenPushedButton();
    }

    public void whenPushedButton(){
        keyword = "bar";
        radius = 1000;

        SearcherDummy sd = new SearcherDummy();
        recommendedPlace = sd.search(keyword, latitude, longitude, radius);

        String reference = recommendedPlace.reference;
        String name = recommendedPlace.name;
        String address = recommendedPlace.address;
        String rating = recommendedPlace.rating;
        String phone = recommendedPlace.phone;
        Boolean isOpen = recommendedPlace.isOpen;

        Toast.makeText(getApplicationContext(), name + " " + address + " " + rating , Toast.LENGTH_LONG).show();
    }

    public void getMyLocation(){
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
