package com.example.covid19_utec.symptoms;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.covid19_utec.BaseSymptomActivity;
import com.example.covid19_utec.LocationService;
import com.example.covid19_utec.MainActivity;
import com.example.covid19_utec.R;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;

public class CoughActivity extends BaseSymptomActivity {
    private static final String TAG = "CoughtActivity";
    private String document;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cough);
        Intent intent= getIntent();
        document = intent.getStringExtra("document");
        type = intent.getStringExtra("type");
        setVariables(ShiverActivity.class);
        answers = new ArrayList<>();
        setOnCLickListeners();
        callPermissions();
    }

    private void startLocationService(){
        if(!isLocationServiceRunning()){
            Intent serviceIntent = new Intent(this, LocationService.class);
            serviceIntent.putExtra("document",document);
            serviceIntent.putExtra("type",type);
//        this.startService(serviceIntent);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){

                CoughActivity.this.startForegroundService(serviceIntent);
            }else{
                startService(serviceIntent);
            }
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("com.codingwithmitch.googledirectionstest.services.LocationService".equals(service.service.getClassName())) {
                Log.d(TAG, "isLocationServiceRunning: location service is already running.");
                return true;
            }
        }
        Log.d(TAG, "isLocationServiceRunning: location service is not running.");
        return false;
    }

    public void callPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        Permissions.check(this/*context*/, permissions, "Local permission are required", new Permissions.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning"), new PermissionHandler() {
            @Override
            public void onGranted() {
                startLocationService();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                callPermissions();
            }
        });
    }
}
