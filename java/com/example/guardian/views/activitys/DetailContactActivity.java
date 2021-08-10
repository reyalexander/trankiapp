package com.example.guardian.views.activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.guardian.R;
import com.example.guardian.config.ConstValue;
import com.example.guardian.databases.SqliteClass;
import com.example.guardian.models.ContactModel;
import com.example.guardian.utils.PositionClass;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class DetailContactActivity extends AppCompatActivity {
    public TextView tv_name_cont, tv_correo_cont, tv_cel_cont, tv_state_conta, tv_dni_con;
    public MapView map;
    Context context;
    ContactModel contactModel;
    CardView ideaCar_call, ideaCar_msg;
    private static LocationManager locManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        /** Area Texto **/
        tv_name_cont = (TextView) findViewById(R.id.tv_name_contac_detail);
        tv_cel_cont = (TextView) findViewById(R.id.tv_cel_contact_detail);
        tv_dni_con = (TextView) findViewById(R.id.tv_dni_contact_detail);
        tv_state_conta = (TextView) findViewById(R.id.tv_state_contact_detail);
        tv_correo_cont = (TextView) findViewById(R.id.tv_correo_contact_detail);

        contactModel = SqliteClass.getInstance(context).databasehelp.contactSql.getcontact(ConstValue.getIdContact());
        tv_name_cont.setText(contactModel.getName_cont());
        tv_cel_cont.setText(contactModel.getCelular_cont());
        tv_dni_con.setText(contactModel.getDNI_cont());
        if(contactModel.getState_cont().equals("0")){
            tv_state_conta.setText(R.string.state_ok);
            tv_state_conta.setTextColor(Color.GREEN);

        }else if(contactModel.getState_cont().equals("1")){
            tv_state_conta.setText(R.string.state_alert);
            tv_state_conta.setTextColor(Color.RED);
        }
        tv_correo_cont.setText(contactModel.getCorreo_cont());

        /** Area Llamadas y msjs **/
        ideaCar_call = (CardView) findViewById(R.id.ideaCard_call);
        ideaCar_msg = (CardView) findViewById(R.id.ideaCard_msg);

        ideaCar_call.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                /** Permiso para hacer llamada **/
                int accessCallPermission = ContextCompat.checkSelfPermission(DetailContactActivity.this,Manifest.permission.CALL_PHONE);
                if(accessCallPermission!= PackageManager.PERMISSION_GRANTED
                    //accessCallPermission!=PackageManager.PERMISSION_GRANTED &&
                    //accessMsgPermission!=PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(DetailContactActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE
                            }, 0);
                }

                new MaterialAlertDialogBuilder(context)
                        .setTitle("Tranki")
                        .setMessage("¿Llamar a "+tv_name_cont.getText().toString()+"?")
                        .setPositiveButton("Llamar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                Intent i = new Intent(Intent.ACTION_CALL);
                                i.setData(Uri.parse("tel:" + contactModel.getCelular_cont()));
                                startActivity(i);
                                }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();

            }
        });

        ideaCar_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                String phone = "1234567890";
                String text = "Hi from Stackoverflow.com";
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(phone, null, text , null, null);
                */
            }
        });

        /** Area del mapa **/
        map = (MapView) findViewById(R.id.map_detail_contact);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.getController().setZoom(17);
        GeoPoint startPoint = new GeoPoint(Double.parseDouble(contactModel.getPos_lati_cont()),Double.parseDouble(contactModel.getPos_longi_cont()));
        map.getController().setCenter(startPoint);
        Marker startMarker= new Marker(map);
        startMarker.setPosition(startPoint); //pone el marker en la posocion
        startMarker.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_person,null));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle(contactModel.getName_cont());
        startMarker.setSnippet(contactModel.getCelular_cont());
        map.getOverlays().add(startMarker);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            new routeMapTaskContact().execute(true);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DetailContactActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent = new Intent(DetailContactActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /** Para dibujar una ruta entre 2 puntos en el mapa **/
    class routeMapTaskContact extends AsyncTask<Boolean, Void, String> {
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(Boolean... booleans) {

            RoadManager roadManager = new OSRMRoadManager(DetailContactActivity.this);
            GeoPoint startPoint = new GeoPoint(Double.parseDouble(contactModel.getPos_lati_cont()),Double.parseDouble(contactModel.getPos_longi_cont()));

            ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
            waypoints.add(startPoint);
            Location location = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
                location= PositionClass.getLastBestLocation(DetailContactActivity.this);
            }

            GeoPoint endPoint = new GeoPoint(location.getLatitude(), location.getLongitude());

            waypoints.add(endPoint);
            Road road = roadManager.getRoad(waypoints);
            Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
            map.getOverlays().add(roadOverlay);
            map.invalidate();
            return null;
        }
    }
    public static Location getLastBestLocationFragment(Context _context) {
        /** Fuente ::
         * https://www.programcreek.com/java-api-examples/?code=Arman92/Mapsforge-OsmDroid-GraphHopper/Mapsforge-OsmDroid-GraphHopper-master/app/src/main/java/com/arman/osmdroidmapsforge/map/MFMapView.java
         * **/
        if (locManager == null)
            locManager = (LocationManager)_context.getSystemService(Context.LOCATION_SERVICE);

        Location bestResult = null;

        float bestAccuracy = Float.MAX_VALUE;

        List<String> matchingProviders = locManager.getAllProviders();
        for (String provider: matchingProviders) {
            @SuppressLint("MissingPermission") Location location = locManager.getLastKnownLocation(provider);
            if (location != null) {
                float accuracy = location.getAccuracy();
                if ( accuracy < bestAccuracy) {
                    bestResult = location;
                    bestAccuracy = accuracy;
                }
            }
        }

        return bestResult;
    }

}
