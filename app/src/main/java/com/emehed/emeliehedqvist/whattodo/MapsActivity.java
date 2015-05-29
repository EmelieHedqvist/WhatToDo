package com.emehed.emeliehedqvist.whattodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends Activity implements OnMapReadyCallback {
    double lat, lng;
    String name, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Code from Google to set up a map
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        //String array received to set lat, lng and present information about place alias and address
        String[] pos = intent.getStringArrayExtra("pos");
        lat = Double.parseDouble(pos[0]);
        lng = Double.parseDouble(pos[1]);
        name = pos[2];
        address = pos[3];

    }



    @Override
    public void onMapReady(GoogleMap map) {

        LatLng pos = new LatLng(lat, lng);

        map.setMyLocationEnabled(true);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 13));
        //Puts a marker on the map on the found place location, presenting place alias and address
        map.addMarker(new MarkerOptions()
                .title(name)
                .snippet(address)
                .position(pos));


    }


}
