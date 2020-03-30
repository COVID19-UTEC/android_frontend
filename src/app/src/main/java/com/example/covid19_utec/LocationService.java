package com.example.covid19_utec;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class LocationService extends Service {
    public static final String CHANNEL_ID = "exampleServiceChannel";
    private static final String TAG = "LocationService";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private String document;
    private String type;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Example Service")
                    .setContentText("")
                    .build();

            startForeground(1, notification);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        document = intent.getStringExtra("document");
        type = intent.getStringExtra("type");
        requestLocationUpdates();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void requestLocationUpdates() {
        boolean finePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED;
        boolean coarsePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED;
        if (finePermission && coarsePermission) {
            fusedLocationProviderClient = new FusedLocationProviderClient(this);
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setFastestInterval(2000);
            locationRequest.setInterval(360000);
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    //locationTv.setText("Latitude: "+ locationResult.getLastLocation().getLatitude()+"\nLongitude: "+locationResult.getLastLocation().getLongitude());
                    sendUserLocation(locationResult.getLastLocation());
                }
            }, Looper.myLooper());
        }

    }


    private void sendUserLocation(Location location) {
        try {
            Log.e(TAG, "Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude());
            Toast.makeText(this, "Latitude: "+ location.getLatitude()+"\nLongitude: "+location.getLongitude(), Toast.LENGTH_LONG).show();
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("document", document);
            data.put("type", type);
            data.put("latitude", 12);
            data.put("longitude", 12);
            JSONObject postData;
            postData = new JSONObject(data);
            volleySend(postData);
        } catch (Exception e) {
            Log.e(TAG, "Error");
            stopSelf();
        }
    }

    private void volleySend(JSONObject postData) {
        Log.i("VOLLEY", postData.toString());
        String urlUbication = "http://107.180.91.147:3031/ubication/report";
        RequestQueue mQueue = Volley.newRequestQueue(getBaseContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlUbication, postData,
                response -> {},
                error -> {});
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000, 2, 1));
        mQueue.add(jsonObjectRequest);
    }
}