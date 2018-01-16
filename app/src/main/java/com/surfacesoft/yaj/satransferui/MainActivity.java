package com.surfacesoft.yaj.satransferui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    Toolbar toolbar;

    // Sliding Tab Layout attributes
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"Transferir", "Clave", "Servicios"};
    int Numboftabs = 3;

    private static final int CODIGO_CONTACT = 1;
    private static final int CODIGO_SALDO = 2;
    private static final int CODIGO_TRANSFERIR = 3;
    private static final int CODIGO_CCLAVE = 4;
    private static final int CODIGO_ACTIVAR = 5;
    private static final int CODIGO_SHARE = 6;
    private static final int CODIGO_99 = 7;
    private static final int CODIGO_INCOGNITO = 8;
    private static final int CODIGO_UNLOCKER = 9;
    private static final int CODIGO_CPHONENUMBER = 10;

    // Base de datos Helper
    StatusSQLiteHelper dbH = new StatusSQLiteHelper(this, "Status", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setPadding(-5, 0, 0, 0);

        /*****   BEGIN - Sliding Tab Layout   ******/

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.color_blue);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        /*****   END - Sliding Tab Layout   ******/

        // Guardando datos por defecto en la BD
        this.storeBasicsPhoneData();

        // Store default Shared Preferences
        this.initPreferences();

        // Presentations of Paints Sliding
        this.beginPresentation();

        // Asking unloker
        this.requireUnlockApp(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings || id == R.id.activate_settings || id == R.id.configure_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean result = super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_settings).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            }
        });

        menu.findItem(R.id.activate_settings).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                MainActivity.this.startActivityForResult(new Intent(MainActivity.this, Activate.class), CODIGO_ACTIVAR);
                return true;
            }
        });

        menu.findItem(R.id.configure_settings).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, ConfigureClave.class));
                return true;
            }
        });

        menu.findItem(R.id.phone_settings).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //  Asking for basic data stored in DB
                ArrayList<String> dataDB = TelephonData.getBasicsPhoneDataFromDB(dbH);
                //  Requesting Phone number
                if (dataDB.get(0).isEmpty() || dataDB.get(0).length() != 8 || dataDB.get(0).charAt(0) != '5') {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, ConfigurePhoneNumber.class));
                    return true;
                }else{
                    Toast.makeText(getApplicationContext(), "El n\u00famero de m\u00f3vil ya est\u00e1 configurado", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });

        menu.findItem(R.id.help_settings).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, Help.class));
                return true;
            }
        });

        menu.findItem(R.id.about_settings).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, About.class));
                return true;
            }
        });
        return result;
    }

    public void initPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        if (!prefs.contains("pref_first_time")) {
            editor.putBoolean("pref_first_time", true);
        }
        if (!prefs.contains("pref_configured")) {
            editor.putBoolean("pref_configured", false);
        }
        if (!prefs.contains("pref_remember_clave")) {
            editor.putBoolean("pref_remember_clave", true);
        }
        if (!prefs.contains("pref_apply_protection")) {
            editor.putString("pref_apply_protection", "0");
        }
        if (!prefs.contains("pref_show_introduction")) {
            editor.putBoolean("pref_show_introduction", true);
        }
        if (!prefs.contains("pref_configured") || !prefs.contains("pref_remember_clave")
                || !prefs.contains("pref_apply_protection") || !prefs.contains("pref_show_introduction")) {
            editor.commit();
        }
    }

    public void beginPresentation() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean showPreset = prefs.getBoolean("pref_show_introduction", false);
        boolean configured = prefs.getBoolean("pref_configured", false);

        if (showPreset) {
            Intent inPSliding = new Intent(MainActivity.this, ShowWelcome.class);
            startActivity(inPSliding);
        } else if (!configured) {
            Intent intNext = new Intent(MainActivity.this, ShowConfigureClave.class);
            startActivity(intNext);
        }
    }

    public void requireUnlockApp(boolean isUnlokerInit) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        boolean configured = prefs.getBoolean("pref_configured", false);
        boolean rememberClave = prefs.getBoolean("pref_remember_clave", false);
        String applyProtection = prefs.getString("pref_apply_protection", "0");

        if (configured && rememberClave) {
            if (isUnlokerInit && applyProtection.equals("2")) {
                Intent unlockInt = new Intent(MainActivity.this, Locker.class);
                unlockInt.putExtra("transfer", false);
                startActivityForResult(unlockInt, CODIGO_UNLOCKER);
            }
            if (!isUnlokerInit && applyProtection.equals("1") && this.USSD_Validar_Transferir()) {
                Intent unlockInt = new Intent(MainActivity.this, Locker.class);
                unlockInt.putExtra("transfer", true);
                startActivityForResult(unlockInt, CODIGO_UNLOCKER);
            } else if (!isUnlokerInit) {
                this.USSD_Transferir();
            }
        } else if (!isUnlokerInit) {
            MainActivity.this.startActivity(new Intent(MainActivity.this, ConfigureClave.class));
            Toast.makeText(this, "Para usar este servicio debe configurar la clave", Toast.LENGTH_SHORT).show();
        }

    }

    public void storeBasicsPhoneData() {
        ArrayList<String> data = TelephonData.getBasicsPhoneData(this);
        SQLiteDatabase dbStatus = this.dbH.getWritableDatabase();
        if (dbStatus != null) {
            Cursor c = dbStatus.rawQuery("SELECT Count(*) FROM Status WHERE " + dbH.id + "=1", null);
            if (c.moveToFirst()) {
                if (c.getInt(0) <= 0) {
                    dbStatus.execSQL("INSERT INTO Status (" + dbH.id + ", " + dbH.telefono + ", " + dbH.imei + ", "
                            + dbH.clavetransf + ", " + dbH.codigoactiv + ") " +
                            "VALUES (1, '" + Cryptography.encryptIt(data.get(0)) + "'," +
                            "'" + Cryptography.encryptIt(data.get(1)) + "'," +
                            "'" + Cryptography.encryptIt("1234") + "','')");
                }
            } else {
                dbStatus.execSQL("INSERT INTO Status (" + dbH.id + ", " + dbH.telefono + ", " + dbH.imei + ", "
                        + dbH.clavetransf + ", " + dbH.codigoactiv + ") " +
                        "VALUES (1, '" + Cryptography.encryptIt(data.get(0)) + "'," +
                        "'" + Cryptography.encryptIt(data.get(1)) + "'," +
                        "'" + Cryptography.encryptIt("1234") + "','')");
            }
        }
        dbStatus.close();

        //  Asking for basic data stored in DB
        ArrayList<String> dataDB = TelephonData.getBasicsPhoneDataFromDB(dbH);
        //  Requesting Phone number
        if (dataDB.get(0).isEmpty() || dataDB.get(0).length() != 8 || dataDB.get(0).charAt(0) != '5') {
            startActivityForResult(new Intent(MainActivity.this, ConfigurePhoneNumber.class), CODIGO_CPHONENUMBER);
        }
    }

    public void getContacts(View v) {
        //Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, CODIGO_CONTACT);
    }

    public void Service_99(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, CODIGO_99);
    }

    public void Service_Saldo(View v) {
        String encodedNumSimbol = Uri.encode("#");
        String ussd = "*" + "222" + encodedNumSimbol;
        startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussd)));
    }

    public void Service_Charge(View v) {
        FragmentTransaction fm = this.getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            fm.remove(prev);
        }
        fm.addToBackStack(null);

        ChargeDialog dialogo = new ChargeDialog();
        dialogo.show(fm, "tagCharge");
    }

    public void Service_Charge_Action(String chargeCode) {
        if (chargeCode.length() == 12) {
            String encodedNumSimbol = Uri.encode("#");
            String ussd = "*" + "662" + "*" + chargeCode + encodedNumSimbol;
            startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussd)));
        } else {
            Toast.makeText(this, "Código incorrecto.", Toast.LENGTH_SHORT).show();
        }
    }

    public void Service_Saldo_Voice(View v) {
        String ussd = "*" + "666";
        startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussd)));
    }

    public void Service_Incognito(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, CODIGO_INCOGNITO);
    }

    public void Service_Cliente(View v) {
        String ussd = "*" + "2266";
        startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussd)));
    }

    public void Service_Bombero(View v) {
        String ussd = "105";
        startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussd)));
    }

    public void Service_PNR(View v) {
        String ussd = "106";
        startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussd)));
    }

    public void Service_Informacion(View v) {
        String ussd = "113";
        startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussd)));
    }

    public void unlock_USSD_Transferir(View v) {
        this.requireUnlockApp(false);
    }

    public void USSD_Transferir() {
        String beneficiario = ((EditText) findViewById(R.id.etContacto)).getText().toString();
        String clave = TelephonData.getClaveTransferencia(dbH);
        String monto = ((EditText) findViewById(R.id.etMonto)).getText().toString();

        String encodedNumSimbol = Uri.encode("#");

        if (this.USSD_Validar_Transferir()) {
            String ussd = "*234*1*" + beneficiario + "*" + clave + "*" + monto + encodedNumSimbol;
            startActivityForResult(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussd)), CODIGO_TRANSFERIR);
        }
    }

    public boolean USSD_Validar_Transferir() {
        String beneficiario = ((EditText) findViewById(R.id.etContacto)).getText().toString();
        String monto = ((EditText) findViewById(R.id.etMonto)).getText().toString();

        // Codigo de activacion en BD
        String codBD = TelephonData.getCodigoActivacionFromDB(dbH);
        // Codigo de activacion generado por el telefono
        String codGen = TelephonData.getCodigoActivacion(dbH);

        if (!codBD.equals("") && codBD.equals(codGen)) {
            if (beneficiario.trim().isEmpty() && monto.trim().isEmpty()) {
                Toast.makeText(this, "Introduzca datos por favor", Toast.LENGTH_SHORT).show();
                return false;
            } else if (beneficiario.trim().isEmpty() || monto.trim().isEmpty()) {
                Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                return false;
            } else if (beneficiario.length() != 8) {
                Toast.makeText(this, "El número del beneficiario debe tener 8 digitos", Toast.LENGTH_SHORT).show();
                return false;
            } else if (beneficiario.charAt(0) != '5') {
                Toast.makeText(this, "El número del beneficiario debe comenzar con 5", Toast.LENGTH_SHORT).show();
                return false;
            } else if (Double.valueOf(monto.trim()) < 0.01 || Double.valueOf(monto.trim()) > 2999.7) {
                Toast.makeText(this, "El monto debe estar entre 0.01 y 2999.7", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        } else {
            MainActivity.this.startActivityForResult(new Intent(MainActivity.this, Activate.class), CODIGO_ACTIVAR);
            Toast.makeText(this, "Necesita activar el servicio de transferencia de saldo", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void USSD_CClave(View v) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean configured = prefs.getBoolean("pref_configured", false);

        String encodedNumSimbol = Uri.encode("#");
        String claveActual = ((EditText) findViewById(R.id.etClaveActual)).getText().toString().trim();
        String claveNueva = ((EditText) findViewById(R.id.etClaveNueva)).getText().toString().trim();
        String RepetirClave = ((EditText) findViewById(R.id.etClaveRepetir)).getText().toString().trim();

        if (configured) {
            if (claveActual.isEmpty() && claveNueva.isEmpty() && RepetirClave.isEmpty()) {
                Toast.makeText(this, "Introduzca datos por favor.", Toast.LENGTH_SHORT).show();
            } else if (claveActual.isEmpty() || claveNueva.isEmpty() || RepetirClave.isEmpty()) {
                Toast.makeText(this, "Rellene todos los campos.", Toast.LENGTH_SHORT).show();
            } else if (claveActual.length() != 4 || claveNueva.length() != 4 || RepetirClave.length() != 4) {
                Toast.makeText(this, "Las claves deben poseer 4 dígitos.", Toast.LENGTH_SHORT).show();
            } else if (!claveActual.equals(TelephonData.getClaveTransferencia(dbH))) {
                Toast.makeText(this, "Clave actual incorrecta!", Toast.LENGTH_SHORT).show();
            } else if (!claveNueva.equals(RepetirClave)) {
                Toast.makeText(this, "Por favor, verifique bien la nueva Clave!", Toast.LENGTH_SHORT).show();
            } else if (claveActual.equals(claveNueva)) {
                Toast.makeText(this, "La nueva Clave no puede ser igual a la clave anterior!", Toast.LENGTH_SHORT).show();
            } else {

                //  Updating clave in Status DB
                SQLiteDatabase dbStatus = dbH.getWritableDatabase();
                dbStatus.execSQL("UPDATE Status SET " +
                        dbH.clavetransf + "='" + Cryptography.encryptIt(claveNueva) + "' WHERE " + dbH.id + "=1");

                //  Executing USSD code
                String ussd = "*234*2*" + claveActual + "*" + claveNueva + encodedNumSimbol;
                startActivityForResult(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussd)), CODIGO_CCLAVE);
            }
        } else {
            Toast.makeText(this, "Para usar este servicio debe configurar la clave", Toast.LENGTH_SHORT).show();
        }
    }

    public void shareApp(View v) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        startActivity(sendIntent);
    }

    public void setControlsContact(View v) {
//        ((TextView) findViewById(R.id.tvNumero)).setVisibility(View.INVISIBLE);
        ((EditText) findViewById(R.id.etContacto)).setVisibility(View.VISIBLE);
        ((EditText) findViewById(R.id.etContacto)).setEnabled(true);
    }

    public void helpTransfer(View v) {
        startActivity(new Intent(MainActivity.this, HelpTransfer.class));
    }

    public void helpServices(View v) {
        startActivity(new Intent(MainActivity.this, HelpServices.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CODIGO_CONTACT:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String telf = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String photo = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI));
//                        String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
//                        String email = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                        //Toast.makeText(this, "Contacto: " + name + " | Telefono: " + telf, Toast.LENGTH_SHORT).show();

                        //  Cleaning phone number
                        telf = TelephonData.cleanPhoneNumber(telf);

                        if (telf.length() != 8) {
                            Toast.makeText(this, "El número del beneficiario debe tener 8 digitos", Toast.LENGTH_LONG).show();
                        } else if (telf.charAt(0) != '5') {
                            Toast.makeText(this, "El número del beneficiario debe comenzar con 5", Toast.LENGTH_LONG).show();
                        } else {
                            //  asignando datos a la vista
                            ((TextView) findViewById(R.id.tvContactName)).setText(name);
                            ((EditText) findViewById(R.id.etContacto)).setText(telf);
//                        ((TextView) findViewById(R.id.tvNumero)).setText(telf);

                            try {
                                if (!photo.equals("") || !photo.isEmpty() || photo == null) {
                                    ((ImageButton) findViewById(R.id.ibContact)).setImageURI(Uri.parse(photo));
                                } else {
                                    ((ImageButton) findViewById(R.id.ibContact)).setImageResource(R.mipmap.ic_person_white_48dp);
                                }
                            } catch (NullPointerException e) {
                                ((ImageButton) findViewById(R.id.ibContact)).setImageResource(R.mipmap.ic_person_white_48dp);
                            }


                            //((TextView) findViewById(R.id.tvEmail)).setText(email);

                            //  Set control contacts
//                        ((TextView) findViewById(R.id.tvNumero)).setVisibility(View.VISIBLE);
//                        ((EditText) findViewById(R.id.etContacto)).setVisibility(View.INVISIBLE);
//                        ((EditText) findViewById(R.id.etContacto)).setEnabled(false);
                        }
                    }
                }
                break;
            case CODIGO_SALDO:
//                if (resultCode == Activity.RESULT_OK) {
//                Toast.makeText(this, "Result code: " + resultCode, Toast.LENGTH_SHORT).show();
//                }
                break;
            case CODIGO_TRANSFERIR:
//                Toast.makeText(this, "Transferencia realizada con éxito!", Toast.LENGTH_SHORT).show();
                break;
            case CODIGO_CCLAVE:
                Toast.makeText(this, "La Clave se cambió correctamente!", Toast.LENGTH_SHORT).show();
                break;
            case CODIGO_ACTIVAR:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "La aplicación se activó con éxito!!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case CODIGO_99:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String telf = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        String ussd = "*" + "99" + telf;
                        startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussd)));
                    }
                }
                break;
            case CODIGO_INCOGNITO:
                if (resultCode == Activity.RESULT_OK) {
                    String encodedNumSimbol = Uri.encode("#");
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String telf = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        String ussd = encodedNumSimbol + "31" + encodedNumSimbol + telf;
                        startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussd)));
                    }
                }
                break;
            case CODIGO_UNLOCKER:
                if (resultCode == Activity.RESULT_CANCELED) {
                    boolean transfer = data.getBooleanExtra("transfer", false);
                    if (!transfer) {
                        this.finish();
                    }
                } else if (resultCode == Activity.RESULT_OK) {
                    boolean transfer = data.getBooleanExtra("transfer", false);
                    if (transfer) {
                        this.USSD_Transferir();
                    }
                }
                break;
            case CODIGO_CPHONENUMBER:
                if (resultCode == Activity.RESULT_CANCELED) {
                    this.finish();
                }
                break;
        }
    }

}
