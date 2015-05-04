package com.emehed.emeliehedqvist.whattodo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar sb;
    private TextView tv;
    private int rangeValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sb = (SeekBar)findViewById(R.id.seekBar);
        sb.setOnSeekBarChangeListener(this);
        tv = (TextView)findViewById(R.id.range);

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

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        rangeValue = i;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        rangeValue = 10;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        tv.setText("The range is: " + rangeValue);
    }
}
