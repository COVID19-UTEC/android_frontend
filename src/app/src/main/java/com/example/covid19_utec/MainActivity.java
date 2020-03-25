package com.example.covid19_utec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final EditText editText = (EditText) findViewById(R.id.nDocumento);
        final TelephonyManager telephonyManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        ArrayAdapter<CharSequence> myadapter=  ArrayAdapter.createFromResource(this,R.array.opciones_login,R.layout.spinner_item);
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myadapter);
        spinner.setOnItemSelectedListener(this);
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = spinner.getSelectedItem().toString();
                String document = editText.getText().toString();
                String phone = "322";
                JSONObject postData = new JSONObject();
                try {
                    postData.put("document", document);
                    postData.put("type", type);
                    postData.put("phone", phone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i("VOLLEY", postData.toString());
                String urlLogin = "http://107.180.91.147:3031/login/app";

                RequestQueue mQueue = Volley.newRequestQueue(getBaseContext());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,urlLogin, postData,
                        new Response.Listener<JSONObject>(){
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("VOLLEY", response.toString());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof AuthFailureError) {
                                    Toast.makeText(getApplicationContext(), "Auth ERROR: " + error, Toast.LENGTH_SHORT ).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "ERROR: " + error, Toast.LENGTH_SHORT ).show();
                                    Log.e("TAG", error.getMessage(), error);
                                }
                            }
                        })

                {
                };
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(1000, 2, 1));
                mQueue.add(jsonObjectRequest);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void login(View view) {

    }
}
