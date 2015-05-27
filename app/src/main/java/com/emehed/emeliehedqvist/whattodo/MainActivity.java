package com.emehed.emeliehedqvist.whattodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;



public class MainActivity extends Activity {
    //This constant is used as a key for the extra information which is sent together with intent
    public static final String EXTRA_MESSAGE = "com.emehed.emeliehedqvist.whattodo.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void createChoice(View view) {
        //When a button 'bar, restaurant, activity or mystery box' is pressed, this method is called
        //This method creates an instance of the class DisplayActivity, checks which button was pressed,
        //and sends this information to DisplayActivity as 'intent.putExtra'
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
        if (button == findViewById(R.id.randombutton)) {
            message = "random";
        }

        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);


    }
    //This method is called when the button settings (the cog wheel) on the main display is pushed
    //Intent is used to change view, via the Class SettingActivity
    public void settingView(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
    //This method is called when the button info on the main display is pushed
    //Intent is used to change view, via the Class InfoActivity
    public void infoView(View view) {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }
}

