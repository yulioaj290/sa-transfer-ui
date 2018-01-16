package com.surfacesoft.yaj.satransferui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class ShowClave extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_clave);
    }

    public void nextSlide(View v) {
        Intent intNext = new Intent(ShowClave.this, ShowServices.class);
        startActivity(intNext);
        this.finish();
    }
}
