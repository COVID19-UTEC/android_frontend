package com.example.covid19_utec;

import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.telephony.TelephonyManager;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.covid19_utec.symptoms.CoughActivity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    InputStream is;
    HashMap<String,HashMap<String,ArrayList<String>>> data;
    Spinner deparmentsSpinner;
    Spinner provinciaSpinner;
    Spinner districtSpinner;
    String deparment="";
    String provincia="";
    String district="";
    List<String> departments;
    List<String> provincias;
    List<String> distritos;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            is = getAssets().open("newdata.json");
            data = new ObjectMapper().readValue(is, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final EditText editText = (EditText) findViewById(R.id.nDocumento);
        final TelephonyManager telephonyManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        deparmentsSpinner = findViewById(R.id.spinner_departments);
        provinciaSpinner = findViewById(R.id.spinner_provincia);
        districtSpinner = findViewById(R.id.spinner_district);
        ArrayAdapter<CharSequence> myadapter=  ArrayAdapter.createFromResource(this,R.array.opciones_login,R.layout.spinner_item);
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departments= new ArrayList<String>(data.keySet());
        ArrayAdapter <String> departmentAdapter= new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,departments);
        deparmentsSpinner.setAdapter(departmentAdapter);
        spinner.setAdapter(myadapter);

        deparmentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                deparment= departments.get(i);
                changeProvinceAdapter();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        provinciaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                provincia = provincias.get(i);
                changeDistrictAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(v -> {
            String type = spinner.getSelectedItem().toString();
            String document = editText.getText().toString();
            district = districtSpinner.getSelectedItem().toString();
            JSONObject postData = new JSONObject();
            try {
                postData.put("document", document);
                postData.put("type", type);
                postData.put("departamento",deparment);
                postData.put("provincia",provincia);
                postData.put("distrito",district);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("VOLLEY", postData.toString());
            String urlLogin = "http://107.180.91.147:3031/login/app";

            RequestQueue mQueue = Volley.newRequestQueue(getBaseContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,urlLogin, postData,
                response -> Log.i("VOLLEY", response.toString()),
                error -> {
                    if (error instanceof AuthFailureError) {
                        //Toast.makeText(getApplicationContext(), "Auth ERROR: " + error, Toast.LENGTH_SHORT ).show();
                    }
                    else {
                        //Toast.makeText(getApplicationContext(), "ERROR: " + error, Toast.LENGTH_SHORT ).show();
                        Log.e("TAG", error.getMessage(), error);
                    }
                });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000, 2, 1));
            mQueue.add(jsonObjectRequest);
            Intent myIntent = new Intent(MainActivity.this, CoughActivity.class);
            myIntent.putExtra("document",document);
            myIntent.putExtra("type",type);
            startActivity(myIntent);
        });
    }

    public void changeProvinceAdapter(){
        provincias = new ArrayList<String>(data.get(deparment).keySet());
        ArrayAdapter <String> provinceAdapter= new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,provincias);
        provinciaSpinner.setAdapter(provinceAdapter);
    }

    public void changeDistrictAdapter(){
        distritos = new ArrayList<String>(data.get(deparment).get(provincia));
        ArrayAdapter <String> districtAdapter= new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,distritos);
        districtSpinner.setAdapter(districtAdapter);
    }

}
