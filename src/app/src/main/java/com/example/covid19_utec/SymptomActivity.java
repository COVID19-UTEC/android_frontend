package com.example.covid19_utec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SymptomActivity extends AppCompatActivity {

    private static final String TAG = "SymptomActivity";
    String message;
    int score;
    PagerTabStrip pagerTabStrip;
    ViewPager vpPager;
    FragmentPagerAdapter adapterViewPager;
    protected String document;
    protected String type;
    protected ArrayList<Boolean> answers;
    private ArrayList<String> questions;
    private static ArrayList<SymptomFragment> fragments;
    Map<String,Object> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);
        Intent intent= getIntent();
        document = intent.getStringExtra("document");
        type = intent.getStringExtra("type");
        questions = intent.getStringArrayListExtra("questions");
        initializeArrays();
        pagerTabStrip = findViewById(R.id.pager_header);
        pagerTabStrip.setTextColor(Color.BLACK);
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        callPermissions();
    }

    public void initializeArrays(){
        fragments = new ArrayList<>();
        Boolean visibleSendButton,visibleGoNextButton,visibleGoBackButton;
        visibleGoBackButton=visibleGoNextButton=true;
        visibleSendButton = false;
        for( int i = 0; i<questions.size();i++){
            if(i==0)
                visibleGoBackButton=false;
            if(i== questions.size()-1) {
                visibleGoNextButton=false;
                visibleSendButton=true;
            }
            fragments.add(SymptomFragment.newInstance(questions.get(i),i,visibleGoBackButton,visibleGoNextButton,visibleSendButton));
            visibleGoBackButton=visibleGoNextButton=true;
        }
    }

    public void sendData(){
        answers = new ArrayList<>();
        for(SymptomFragment b :fragments ){
            answers.add(b.getResult());
        }
        data = new HashMap<>();
        data.put("document",document);
        data.put("type",type);
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

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = fragments.size();

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

        }
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return "Pregunta" + (position+1);
        }

    }

    public void setCurrentItem (int item, boolean smoothScroll) {
        vpPager.setCurrentItem(item, smoothScroll);
    }

    public void startMessageActivity(){
        Intent intent = new Intent(this, MessageActivity.class);
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

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){

                SymptomActivity.this.startForegroundService(serviceIntent);
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
