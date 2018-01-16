package com.surfacesoft.yaj.satransferui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ConfigureClave extends Activity {

    // Base de datos Helper
    StatusSQLiteHelper dbH = new StatusSQLiteHelper(this, "Status", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_clave);

        //  Asking for the configured preference
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean configured = prefs.getBoolean("pref_configured", false);
        if(configured){
            TextView tvExplain = (TextView)findViewById(R.id.tvExplainConfigure2);
            TextView tvClave = (TextView)findViewById(R.id.tvClaveConfigure2);
            EditText etClave = (EditText)findViewById(R.id.etClaveConfigure2);
            Button btnConfigure = (Button)findViewById(R.id.btnClaveConfigure2);

            tvExplain.setText(R.string.txtExplain2Configured);
            tvClave.setVisibility(View.INVISIBLE);
            tvClave.setEnabled(false);
            etClave.setVisibility(View.INVISIBLE);
            etClave.setEnabled(false);
            btnConfigure.setText(R.string.btnClose);
            btnConfigure.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btnConfigure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    public void actionConfigureClave(View v){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        String clave = ((EditText) findViewById(R.id.etClaveConfigure2)).getText().toString().trim();

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

    public void closeWindows(View v){
        this.finish();
    }
}
