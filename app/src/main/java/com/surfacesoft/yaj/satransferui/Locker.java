package com.surfacesoft.yaj.satransferui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Locker extends Activity {

    // Base de datos Helper
    StatusSQLiteHelper dbH = new StatusSQLiteHelper(this, "Status", null, 1);

    boolean transfer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locker);

        this.transfer = getIntent().getBooleanExtra("transfer", false);

        // Cancelando por defecto
        Intent data = new Intent();
        data.putExtra("transfer", this.transfer);
        setResult(Activity.RESULT_CANCELED, data);
    }

    public void closeWindows(View v) {
        Intent data = new Intent();
        data.putExtra("transfer", this.transfer);
        setResult(Activity.RESULT_CANCELED, data);
        this.finish();
    }

    public void unlockApp(View v) {
        String clave = ((EditText) findViewById(R.id.etLockerClave)).getText().toString().trim();

        String storedClave = TelephonData.getClaveTransferencia(dbH);

        if (clave.isEmpty() || clave.length() != 4) {
            Toast.makeText(this, "La clave debe tener 4 dígitos", Toast.LENGTH_SHORT).show();
        } else if (!clave.equals(storedClave)) {
            Toast.makeText(this, "Clave incorrecta!!!", Toast.LENGTH_SHORT).show();
        } else {
            Intent data = new Intent();
            data.putExtra("transfer", this.transfer);
            setResult(Activity.RESULT_OK, data);
            this.finish();
        }
    }
}
