package com.example.guardian.views.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.guardian.R;
import com.example.guardian.config.ConstValue;
import com.example.guardian.databases.SqliteClass;
import com.example.guardian.models.SitesModel;
import com.example.guardian.utils.PositionClass;

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

public class DetailSiteActivity extends AppCompatActivity {
    TextView tv_name,tv_direccion,tv_ruc, tv_horario,tv_state,tv_correo;
    public MapView map;
    SitesModel siteModel;
    Context context;
    private static LocationManager locManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_site);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context=this;

        tv_name = (TextView) findViewById(R.id.tv_name_detail);
        tv_direccion = (TextView) findViewById(R.id.tv_direccion_detail);
        tv_ruc = (TextView) findViewById(R.id.tv_ruc_detail);
        tv_horario = (TextView) findViewById(R.id.tv_horario_detail);
        tv_state = (TextView) findViewById(R.id.tv_state_detail);
        tv_correo = (TextView) findViewById(R.id.tv_correo_detail);

        String val = String.valueOf(ConstValue.getIdSite());
        siteModel = SqliteClass.getInstance(context).databasehelp.siteSql.getSite(String.valueOf(ConstValue.getIdSite()));
        tv_name.setText(siteModel.getName());
        tv_direccion.setText(siteModel.getDireccion());
        tv_ruc.setText(siteModel.getRUC());
        tv_horario.setText(siteModel.getHorario());
        if(siteModel.getState().equals("0")){
            tv_state.setText(R.string.state_ok);
            tv_state.setTextColor(Color.GREEN);
        }else if(siteModel.getState().equals("1")){
            tv_state.setText(R.string.state_alert);
            tv_state.setTextColor(Color.RED);
        }
        tv_correo.setText(siteModel.getCorreo());

        /** Area del mapa **/
        map = (MapView) findViewById(R.id.map_detail);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.getController().setZoom(17);
        GeoPoint startPoint = new GeoPoint(Double.parseDouble(siteModel.getPos_lati()),Double.parseDouble(siteModel.getPos_longi()));
        map.getController().setCenter(startPoint);
        Marker startMarker= new Marker(map);
        startMarker.setPosition(startPoint); //pone el marker en la posocion
        startMarker.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_shop,null));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle(siteModel.getName());
        startMarker.setSnippet(siteModel.getDireccion());
        map.getOverlays().add(startMarker);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            new routeMapTask().execute(true);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DetailSiteActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent = new Intent(DetailSiteActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /** Para dibujar una ruta entre 2 puntos en el mapa **/
    class routeMapTask extends AsyncTask<Boolean, Void, String> {
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(Boolean... booleans) {

            RoadManager roadManager = new OSRMRoadManager(DetailSiteActivity.this);
            GeoPoint startPoint = new GeoPoint(Double.parseDouble(siteModel.getPos_lati()),Double.parseDouble(siteModel.getPos_longi()));

            ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
            waypoints.add(startPoint);
            Location location = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
                location= PositionClass.getLastBestLocation(DetailSiteActivity.this);

            } else{
                // para versiones anteriores a android 6.0
                location= getLastBestLocationFragment(DetailSiteActivity.this);
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
