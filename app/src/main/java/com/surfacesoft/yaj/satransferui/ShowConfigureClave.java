package com.surfacesoft.yaj.satransferui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class ShowConfigureClave extends Activity {

    // Base de datos Helper
    StatusSQLiteHelper dbH = new StatusSQLiteHelper(this, "Status", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_configure_clave);
    }

    public void nextSlide(View v) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        String clave = ((EditText) findViewById(R.id.etClaveConfigure)).getText().toString().trim();

        if (clave.length() > 0) {
            if (clave.length() != 4) {
                Toast.makeText(this, "El código debe tener 4 digitos", Toast.LENGTH_SHORT).show();
            } else {
                //  Updating clave in Status DB
                SQLiteDatabase dbStatus = dbH.getWritableDatabase();
                dbStatus.execSQL("UPDATE Status SET " +
                        dbH.clavetransf + "='" + Cryptography.encryptIt(clave) + "' WHERE " + dbH.id + "=1");

                // Updating Shared Preferences
                editor.putBoolean("pref_configured", true);
                editor.commit();
                this.finish();
            }
        } else {
            this.finish();
        }
    }
}
