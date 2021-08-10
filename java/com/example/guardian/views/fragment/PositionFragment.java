package com.example.guardian.views.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

//import androidx.fragment.app.Fragment;
import android.app.Fragment;

import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.guardian.R;
import com.example.guardian.config.ConstValue;
import com.example.guardian.databases.SqliteClass;
import com.example.guardian.models.ContactModel;
import com.example.guardian.models.SitesModel;
import com.example.guardian.utils.Position;
import com.example.guardian.utils.PositionClass;
import com.example.guardian.utils.Protocol;
import com.example.guardian.utils.Utils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import tech.gusavila92.websocketclient.WebSocketClient;
import static com.example.guardian.utils.PositionClass.getLastBestLocation;

public class PositionFragment extends Fragment {

    public static MapView map;
    public static Marker startMarkersite;
    public static Marker contactMarker ;
    public MyLocationNewOverlay mLocationOverlay;
    LocationManager locationManager;
    ArrayList<SitesModel>sitesModels;
    public static ArrayList<ContactModel>contactModels;
    Protocol protocol;
    public static GeoPoint myPoint;
    ImageButton btn_myposotion;
    public static Context context;
    Position position;

    LocationManager locManager;
    LocationListener locListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_position, container, false);

        actualizarPosicion();
        //createWebSocketClient();
        context= getActivity();
        protocol = new Protocol();
        position =  new Position(context);
        btn_myposotion = (ImageButton) rootView.findViewById(R.id.ic_center_map);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { }

        Context ctx = getActivity();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = (MapView) rootView.findViewById(R.id.map);

        startMarkersite = new Marker(map);

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getController().setZoom(15);

        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getActivity()),map);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableFollowLocation();
        map.getOverlays().add(mLocationOverlay);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("location_actual", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Por usar  futuro
        btn_myposotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sitesModels = SqliteClass.getInstance(getActivity()).databasehelp.siteSql.getAllSites();

        return rootView;
    }


    @Override
    public void onStop() {
        Toast.makeText(getActivity(),"on stop",Toast.LENGTH_LONG).show();
        locManager.removeUpdates(locListener);
        super.onStop();
    }

    public static class positionapTask extends AsyncTask<String, Void, String> {
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {

            contactMarker = new Marker(map);
            //contactMarker.setIcon(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_person,null));
            map.getOverlays().clear(); // limpia los icones anteriores

            SharedPreferences sharedPref = context.getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
            map.getOverlays().remove(startMarkersite);
            myPoint = new GeoPoint(
                    Double.parseDouble(sharedPref.getString("myLatitud","0.0000")),
                    Double.parseDouble(sharedPref.getString("myLongitu","0.0000")));

            /** icono del q se loguea **/
            startMarkersite = new Marker(map);
            startMarkersite.setPosition(myPoint);
            startMarkersite.setIcon(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_my_location, null));
            startMarkersite.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            startMarkersite.setTitle(sharedPref.getString("user_first_name",""));
            map.getOverlays().add(startMarkersite);

            contactModels = SqliteClass.getInstance(context).databasehelp.contactSql.getAllContacts();
            for(int i=0;i<contactModels.size();i++){
                double lat= Double.parseDouble(contactModels.get(i).getPos_lati_cont());
                double longi= Double.parseDouble(contactModels.get(i).getPos_longi_cont());

                myPoint = new GeoPoint(lat, longi);
                contactMarker.setPosition(myPoint);
                contactMarker.setIcon(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_person,null));
                contactMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                contactMarker.setTitle(contactModels.get(i).getName_cont());
                contactMarker.setSnippet(contactModels.get(i).getCelular_cont());
                map.getOverlays().add(contactMarker);

                map.invalidate();
            }
            return null;
        }
    }

    public void onResume(){
        super.onResume();
        map.onResume();
    }

    public void onPause(){
        super.onPause();
        map.onPause();
    }

    /** Para un metodo mas preciso **/
    @SuppressLint("MissingPermission")
    private void actualizarPosicion() {

        locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        /** Consulta si el gps esta activo y si no pide activarlo **/
        if(!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertNoGps();
        }

        locListener = new LocationListener() {
            //map = (MapView) rootView.findViewById(R.id.map);

            @Override
            public void onLocationChanged(Location location) {

                map.getController().setCenter(new GeoPoint(location.getLatitude(), location.getLongitude()));
                map.getController().animateTo(new GeoPoint(location.getLatitude(), location.getLongitude()));

                if(location ==null){

                }
                else{
                    SharedPreferences sharedPref = context.getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putString("myLatitud",String.valueOf(location.getLatitude()));
                    editor.putString("myLongitu",String.valueOf(location.getLongitude()));
                    editor.apply();
                    JSONObject jsonObjectTrack =  new JSONObject();
                    try {
                        jsonObjectTrack.put("device",Integer.parseInt(sharedPref.getString("id_device","6")));
                        jsonObjectTrack.put("timestamp",Utils.getFechaNum()+" "+Utils.getHoraNum());
                        jsonObjectTrack.put("latitude",String.valueOf(location.getLatitude()));
                        jsonObjectTrack.put("longitude",String.valueOf(location.getLongitude()));
                        jsonObjectTrack.put("course",11);
                        jsonObjectTrack.put("satellites",11);

                        protocol.sendWorkPostRequest(context,jsonObjectTrack,ConstValue.POST_TRACK);

                        new positionapTask().execute("");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //Toast.makeText(ConstValue.getContext(), "Cambios en proveedor " + provider + " Estado-->" + status, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProviderEnabled(String provider) {
                //Toast.makeText(ConstValue.getContext(), "habilitado " + provider, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                //Toast.makeText(ConstValue.getContext(), "deshabilitado " + provider, Toast.LENGTH_LONG).show();
            }

        };

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        Dexter.withContext(getActivity())
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
        if(locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locListener);

        } else if(locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locListener);

        }
    }

    /** Es requerido para oobtener la ubicacion en tiempo real **/
    private void AlertNoGps() {
        new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Tranki")
                .setMessage("El sistema GPS esta desactivado, ¿Desea activarlo, para una mejor precisión en la ubicación?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Activar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }).show();
    }
}
