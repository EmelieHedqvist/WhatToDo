package com.emehed.emeliehedqvist.whattodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by edvin on 22/05/15.
 */
public class NoResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_result);
    }

    public void noResultSettingsView(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);

    }
}
