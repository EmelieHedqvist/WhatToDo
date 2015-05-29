package com.emehed.emeliehedqvist.whattodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by edvin on 22/05/15.
 */
public class NoResultActivity extends Activity {
    //Sets up the view for when no results have been found

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_result);
    }
    @Override
    public void onBackPressed() {
        //This override method is used to take the user back to the main view when pressing the back button
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void noResultSettingsView(View view) {
        //A click on the cog wheel takes the user to the setting view
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);

    }
}
