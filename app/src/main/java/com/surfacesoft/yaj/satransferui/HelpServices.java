package com.surfacesoft.yaj.satransferui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


public class HelpServices extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_services);
    }

    public void closeWindows(View v){
        this.finish();
    }
}
