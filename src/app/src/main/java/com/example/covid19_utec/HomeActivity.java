package com.example.covid19_utec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "SymptomActivity";
    String message;
    int score;
    private  String publicityId;
    protected String document;
    protected String type;
    protected ArrayList<Boolean> answers;
    private ArrayList<String> questions;
    private ReportFragment reportFragment;
    private NotificationFragment notificationFragment;
    private TextView title;

    Map<String,Object> data;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
                switch (item.getItemId()){
                    case R.id.navigation_report:
                        switchToFragmentReport();
                        break;
                    case R.id.navigation_notifications:
                        switchToFragmentNotification();
                        break;
                }
                return true;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent= getIntent();
        publicityId = intent.getStringExtra("publicityId");
        document = intent.getStringExtra("document");
        type = intent.getStringExtra("type");
        questions = intent.getStringArrayListExtra("questions");

        title = findViewById(R.id.fragment_title);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        reportFragment = new ReportFragment();
        notificationFragment = new NotificationFragment();
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,reportFragment).commit();
        getNotifications();
        callPermissions();
    }

    public void getNotifications(){
        String urlNotifications = "http://107.180.91.147:3031/notification/get-all";
        JSONObject postData = new JSONObject();
        try {
            postData.put("publicityId", publicityId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Notifications", postData.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlNotifications, postData,
                response -> {
                    if (response == null) {
                        Log.i("notifications", "null");
                    } else {
                        Log.i("notifications", response.toString());
                    }
                }, error -> Log.i("notifications", error.toString()));
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000, 2, 1));
        Volley.newRequestQueue(getBaseContext()).add(jsonObjectRequest);
    }
    public void changeFragmentTitle(String fragmentTitle){
        title.setText(fragmentTitle);
    }

    public ArrayList<String> getQuestions(){
        return questions;
    }
    public void switchToFragmentReport() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.nav_host_fragment, reportFragment).commit();
    }

    public void switchToFragmentNotification() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.nav_host_fragment, notificationFragment).commit();
    }


    public void sendData(ArrayList<Boolean> answers){
        this.answers =answers;
        data = new HashMap<>();
        data.put("publicityId",publicityId);
        data.put("result",answers);
        JSONObject postData =  new JSONObject(data);
        String urlSymptom = "http://107.180.91.147:3031/symptom/report";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlSymptom, postData, response -> {
            try {
                message = response.getString("message");
                score = response.getInt("score");
                startMessageActivity();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {});
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000, 2, 1));
        Volley.newRequestQueue(getBaseContext()).add(jsonObjectRequest);
    }


    public void startMessageActivity(){
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("publicityId",publicityId);
        intent.putExtra("document",document);
        intent.putExtra("type",type);
        intent.putExtra("message",message);
        intent.putExtra("score",score);
        intent.putExtra("answers",answers);
        startActivity(intent);
    }

    private void startLocationService(){
        if(!isLocationServiceRunning()){
            Intent serviceIntent = new Intent(this, LocationService.class);
            serviceIntent.putExtra("document",document);
            serviceIntent.putExtra("type",type);
            serviceIntent.putExtra("publicityId",publicityId);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){

                HomeActivity.this.startForegroundService(serviceIntent);
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
