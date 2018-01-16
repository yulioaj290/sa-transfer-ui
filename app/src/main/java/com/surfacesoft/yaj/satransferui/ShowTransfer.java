package com.surfacesoft.yaj.satransferui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class ShowTransfer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_transfer);
    }

    public void nextSlide(View v) {
        Intent intNext = new Intent(ShowTransfer.this, ShowClave.class);
        startActivity(intNext);
        this.finish();
    }
}
