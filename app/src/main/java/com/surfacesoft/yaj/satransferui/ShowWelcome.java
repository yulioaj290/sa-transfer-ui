package com.surfacesoft.yaj.satransferui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class ShowWelcome extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_welcome);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        boolean firstTime = prefs.getBoolean("pref_first_time", true);
        if (firstTime) {
            editor.putBoolean("pref_first_time", false);
            editor.putBoolean("pref_show_introduction", false);
            editor.commit();
        }
    }

    public void nextSlide(View v) {
        Intent intNext = new Intent(ShowWelcome.this, ShowTransfer.class);
        startActivity(intNext);
        this.finish();
    }
}
