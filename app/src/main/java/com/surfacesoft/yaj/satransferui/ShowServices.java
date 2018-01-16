package com.surfacesoft.yaj.satransferui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;


public class ShowServices extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_services);
    }

    public void nextSlide(View v) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean configured = prefs.getBoolean("pref_configured", false);
        if (!configured) {
            Intent intNext = new Intent(ShowServices.this, ShowConfigureClave.class);
            startActivity(intNext);
        }
        this.finish();
    }
}
