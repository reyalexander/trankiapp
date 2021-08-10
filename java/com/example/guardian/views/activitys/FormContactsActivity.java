package com.example.guardian.views.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.guardian.R;
import com.example.guardian.databases.SqliteClass;
import com.example.guardian.models.SitesModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class FormContactsActivity extends AppCompatActivity  {
    EditText et_contact_name, et_contact_dire,et_contact_correo, et_contact_dni;
    FloatingActionButton fltBtn_ok;
    SitesModel site ;
    public MapView map;
    private LocationManager locationManager;
    //MyItemizedOverlay myItemizedOverlay = null;
    Drawable marker;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_sites);
        getSupportActionBar().setTitle("Nuevo Contacto");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /** Datos del layout **/
        et_contact_name= (EditText) findViewById(R.id.et_contact_name);
        et_contact_dire= (EditText) findViewById(R.id.et_contact_direccion);
        et_contact_correo= (EditText) findViewById(R.id.et_contact_correo);
        et_contact_dni= (EditText) findViewById(R.id.et_contact_dni);

        /** Boton de enviar **/
        fltBtn_ok = (FloatingActionButton) findViewById(R.id.btn_validate);
        fltBtn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Validacion de campos vacios **/
                if(et_contact_name.getText().length()>0 && et_contact_dire.getText().length()>0 && et_contact_correo.getText().length()>0 && et_contact_dni.getText().length()>0 ){
                    SharedPreferences sharedPref = getSharedPreferences("Detail_New_Contact",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("name", et_contact_name.getText().toString());
                    editor.putString("direccion", et_contact_dire.getText().toString());
                    editor.putString("correo", et_contact_correo.getText().toString());
                    editor.putString("dni", et_contact_dni.getText().toString());
                    editor.apply();

                    Intent intent = new Intent(FormContactsActivity.this, SearchSiteMapActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Debe llenar los campos",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FormContactsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent = new Intent(FormContactsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
