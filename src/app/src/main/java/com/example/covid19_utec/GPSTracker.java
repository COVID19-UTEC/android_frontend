package com.example.covid19_utec;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class GPSTracker implements LocationListener {
    Context context;
    public GPSTracker(Context c) {
        context = c;
    }

    public Location getLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "Permiso de localizacion denegado", Toast.LENGTH_SHORT).show();
            return null;
        }
        long minMiliSecondsUpdate = 20000;
        float minMetersUpdate = 30;
        LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,minMiliSecondsUpdate, minMetersUpdate,this);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
        }else{
            Toast.makeText(context, "Por favor active el GPS", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

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
}
