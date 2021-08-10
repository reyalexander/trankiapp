package com.example.guardian.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.guardian.R;
import com.example.guardian.databases.SqliteClass;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

public class PositionClass {
    private static LocationManager locManager;

    Context context;
    public PositionClass(Context context) {
        this.context=context;
    }

    /** busca la mejor posición actual
     * return@ Location Best **/
    public static Location getLastBestLocation(Context _context) {
        /** Fuente
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
