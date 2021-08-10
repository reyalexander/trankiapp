package com.example.guardian.views.activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.guardian.R;
import com.example.guardian.config.ConstValue;
import com.example.guardian.databases.SqliteClass;
import com.example.guardian.models.SitesModel;
import com.example.guardian.utils.Protocol;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchSiteMapActivity extends AppCompatActivity implements LocationListener {
    public MapView mapView;
    private LocationManager locationManager;
    // MyItemizedOverlay myItemizedOverlay = null;
    Drawable marker;
    FloatingActionButton fltBtn_ok;
    SitesModel site ;
    private static LocationManager locManager;
    boolean haySite=false;
    ItemizedIconOverlay<OverlayItem> items = null;
    private Vibrator vibrator;

    Protocol protocol;

    List<Address> address;
    Geocoder geo;
    Context context;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_site_map);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=this;

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mapView = (MapView) this.findViewById(R.id.map_search);
        mapView.setUseDataConnection(false);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.setUseDataConnection(false);
        mapView.setFocusable(true);
        mapView.setFocusableInTouchMode(true);

        mapView.getController().setZoom(16); // set initial zoom-level, depends
        Location location = getLastBestLocation(SearchSiteMapActivity.this);
        mapView.getController().animateTo(new GeoPoint(location));
        mapView.getController().setCenter(new GeoPoint(location));


        Toast.makeText(getBaseContext(),"Dar un Click prolongado en el mapa donde quiere guardar",Toast.LENGTH_LONG).show();

        /** Para compartir los datos de la vista del formulario **/
        final SharedPreferences sharedPref = getSharedPreferences("Detail_New_Store",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        /** Obtiene la location donde se da click
         * Fuente : https://github.com/osmdroid/osmdroid/issues/295
         * **/

        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {

                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                //Toast.makeText(getBaseContext()," Posición actualizada ",Toast.LENGTH_LONG).show();

                DecimalFormat df= new DecimalFormat("#.000000");
                editor.putString("latitud", String.valueOf(df.format(p.getLatitude())));
                editor.putString("longitud", String.valueOf(df.format(p.getLongitude())));
                editor.apply();
                if (vibrator.hasVibrator()) {//Si tiene vibrado
                    long tiempo = 100; //en milisegundos
                    vibrator.vibrate(tiempo);
                }
                ArrayList<OverlayItem> markers = new ArrayList<>();
                OverlayItem item = new OverlayItem("", "", p);
                item.setMarker(ContextCompat.getDrawable(SearchSiteMapActivity.this, R.drawable.ic_shop));
                markers.add(item);
                geo=new Geocoder(SearchSiteMapActivity.this,Locale.getDefault());

                try {
                    address=geo.getFromLocation(p.getLatitude(),p.getLongitude(),1);
                    String county = address.get(0).getCountryName();
                    String area = address.get(0).getSubLocality();
                    String city = address.get(0).getLocality();
                    String state = address.get(0).getAdminArea();
                    String countryCode = address.get(0).getCountryCode();
                    String postalCode = address.get(0).getPostalCode();
                    editor.putString("postal", postalCode);
                    editor.putString("distrito", state);
                    editor.apply();

                    Toast.makeText(getBaseContext(),""+county + " -- " +
                            area+" -- " +
                            city+" -- " +
                            state+" -- " +
                            countryCode+" -- " +
                            postalCode,Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (items == null) {
                    items = new ItemizedIconOverlay<>(SearchSiteMapActivity.this, markers, null);
                    mapView.getOverlays().add(items);
                    mapView.invalidate();
                } else {
                    mapView.getOverlays().remove(items);
                    mapView.invalidate();
                    items = new ItemizedIconOverlay<>(SearchSiteMapActivity.this, markers, null);
                    mapView.getOverlays().add(items);
                }
                haySite=true;

                return false;
            }
        };


        MapEventsOverlay OverlayEvents = new MapEventsOverlay(getBaseContext(), mReceive);
        mapView.getOverlays().add(OverlayEvents);


        /** Agregar aqui recien a la BD **/
        fltBtn_ok = (FloatingActionButton) findViewById(R.id.btn_validate_map);
        fltBtn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(haySite){

                    site= new SitesModel(1,
                            sharedPref.getString("name","inactive"),
                            sharedPref.getString("latitud","default"),
                            sharedPref.getString("longitud","default"),
                            sharedPref.getString("direccion","inactive"),
                            sharedPref.getString("correo","inactive"),
                            sharedPref.getString("ruc","inactive"),
                            sharedPref.getString("horario","inactive"),
                            "0");

                    JSONObject objsite = new JSONObject();
                    try {
                        protocol=new Protocol();
                        int lastSite=Integer.parseInt(sharedPref.getString("lastsite","10000"));
                        objsite.put("AddressId",lastSite+1);
                        objsite.put("RazonSocial",sharedPref.getString("name","inactive"));
                        objsite.put("Correo",sharedPref.getString("correo","inactive"));
                        objsite.put("RUC",sharedPref.getString("ruc","inactive"));
                        objsite.put("Address1",sharedPref.getString("direccion","inactive"));
                        objsite.put("Address2",sharedPref.getString("direccion","inactive"));
                        objsite.put("District",sharedPref.getString("distrito","inactive"));
                        objsite.put("PostalCode",sharedPref.getString("postal","inactive"));
                        objsite.put("GPSLocation","https://www.google.com/maps/@" + sharedPref.getString("latitud","default") + ","+sharedPref.getString("longitud","default")+",19z");
                        objsite.put("Latitud",Double.parseDouble(sharedPref.getString("latitud","default")));
                        objsite.put("Longitud",Double.parseDouble(sharedPref.getString("longitud","default")));
                        objsite.put("is_active",true);
                        objsite.put("CityId",1);
                        //JSONObject result=protocol.postJson(ConstValue.POST_ADDRESS,objsite);
                        Log.i("JSON for SEND: ", objsite.toString());
                        protocol.sendWorkPostRequest(context,objsite,ConstValue.POST_ADDRESS);
                        //Log.i("Post new Site ", result.toString());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    SqliteClass.getInstance(getApplicationContext()).databasehelp.siteSql.addSite(site);
                    Intent intent = new Intent(SearchSiteMapActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getBaseContext(),"Tiene que dar un Click prolongado en el mapa donde se ubica su negocio.",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        //mapView.getOverlays().add(myItemizedOverlay);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SearchSiteMapActivity.this, FormSitesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent = new Intent(SearchSiteMapActivity.this, FormSitesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /** busca la mejor posición actual
     * return@ Location Best **/
    public static Location getLastBestLocation(Context _context) {
        /** Fuente ::
         * https://www.programcreek.com/java-api-examples/?code=Arman92/Mapsforge-OsmDroid-GraphHopper/Mapsforge-OsmDroid-GraphHopper-master/app/src/main/java/com/arman/osmdroidmapsforge/map/MFMapView.java
         * **/
        if (locManager == null)
            locManager = (LocationManager)_context.getSystemService(Context.LOCATION_SERVICE);

        Location bestResult = null;

        float bestAccuracy = Float.MAX_VALUE;

        // Iterate through all the providers on the system, keeping
        // note of the most accurate result within the acceptable time limit.
        // If no result is found within maxTime, return the newest Location.
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
