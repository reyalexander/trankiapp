package com.example.guardian.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;

public class Position {
    private static LocationManager locManager;

    Context context;
    public Position(Context context) {
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