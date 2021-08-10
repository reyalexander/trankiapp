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

public class FormSitesActivity extends AppCompatActivity  {
    EditText et_site_name, et_site_dire,et_site_correo, et_site_ruc,et_site_horario;
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
        getSupportActionBar().setTitle("Nuevo Sitio");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /** Datos del layout **/
        et_site_name= (EditText) findViewById(R.id.et_site_name);
        et_site_dire= (EditText) findViewById(R.id.et_site_direccion);
        et_site_correo= (EditText) findViewById(R.id.et_site_correo);
        et_site_ruc= (EditText) findViewById(R.id.et_site_ruc);
        et_site_horario= (EditText) findViewById(R.id.et_site_horario);

        /** Boton de enviar **/
        fltBtn_ok = (FloatingActionButton) findViewById(R.id.btn_validate);
        fltBtn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Validacion de campos vacios **/
                if(et_site_name.getText().length()>0 && et_site_dire.getText().length()>0 && et_site_correo.getText().length()>0 && et_site_ruc.getText().length()>0 && et_site_horario.getText().length()>0 ){
                    SharedPreferences sharedPref = getSharedPreferences("Detail_New_Store",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("name", et_site_name.getText().toString());
                    editor.putString("direccion", et_site_dire.getText().toString());
                    editor.putString("correo", et_site_correo.getText().toString());
                    editor.putString("ruc", et_site_ruc.getText().toString());
                    editor.putString("horario", et_site_horario.getText().toString());
                    editor.apply();

                    Intent intent = new Intent(FormSitesActivity.this, SearchSiteMapActivity.class);
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
        Intent intent = new Intent(FormSitesActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent = new Intent(FormSitesActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
