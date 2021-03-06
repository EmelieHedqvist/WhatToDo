package com.emehed.emeliehedqvist.whattodo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;


public class SettingActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar sb;
    private int rangeValue;
    private TextView tv;


    //This class sets up a slider for the user to choose search radius
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //This is to set up the slider
        sb = (SeekBar)findViewById(R.id.seekBar);
        sb.setOnSeekBarChangeListener(this);
        SharedPreferences settings = getSharedPreferences("values",
                Context.MODE_PRIVATE);
        //Pre set radius is 1000 m
        int radius = settings.getInt("radius", 1000);
        sb.setProgress(radius);
        this.rangeValue = radius;




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
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
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        rangeValue = i;
        tv = (TextView)findViewById(R.id.distance);
        tv.setText(rangeValue + " m");
        SharedPreferences settings = getSharedPreferences("values",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("radius", rangeValue);
        editor.commit();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        rangeValue = rangeValue;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //When the slider is released, the user is sent back to the main view
        rangeValue = rangeValue;
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
