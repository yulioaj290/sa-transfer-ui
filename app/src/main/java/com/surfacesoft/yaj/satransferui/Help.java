package com.surfacesoft.yaj.satransferui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


public class Help extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    public void closeWindows(View v){
        this.finish();
    }
}
