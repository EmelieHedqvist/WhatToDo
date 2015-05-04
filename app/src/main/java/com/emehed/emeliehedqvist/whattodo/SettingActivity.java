package com.emehed.emeliehedqvist.whattodo;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


public class SettingActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar sb;
    private int rangeValue;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sb = (SeekBar)findViewById(R.id.seekBar);
        sb.setOnSeekBarChangeListener(this);
        this.rangeValue = 0;
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

    public int getRangeValue(){
        return this.rangeValue;
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        rangeValue = i;
        tv = (TextView)findViewById(R.id.distance);
        tv.setText(rangeValue + " m");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        rangeValue = rangeValue;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        rangeValue = rangeValue;
    }
}
