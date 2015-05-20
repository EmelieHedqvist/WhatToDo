package com.emehed.emeliehedqvist.whattodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void switchLayout(View view) {
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

    public void createChoice(View view) {
        Intent intent = new Intent(this, DisplayActivity.class);
        View button = view;
        String message = "";
        if (button == findViewById(R.id.barbutton)) {
            message = "bar";
        }
        if (button == findViewById(R.id.resturantbutton)) {
            message = "restaurant";
        }
        if (button == findViewById(R.id.activitybutton)) {
            message = "activity";
        }
        else {
            message = "bar";
        }
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);


    }

    public void settingView(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    public void infoView(View view) {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

}

