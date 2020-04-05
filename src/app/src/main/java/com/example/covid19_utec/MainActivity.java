package com.example.covid19_utec;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    private static final String publicityId ="464a4v7vcq6s";
    private static final String TAG = "MainActivity";
    InputStream is;
    HashMap<String,HashMap<String,ArrayList<String>>> data;
    String document,type;
    Spinner spinner;
    EditText editText;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinner);
        editText = (EditText) findViewById(R.id.nDocumento);
        ArrayAdapter<CharSequence> myadapter=  ArrayAdapter.createFromResource(this,R.array.opciones_login,R.layout.spinner_item);
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myadapter);

        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(v -> {
            type = spinner.getSelectedItem().toString();
            document = editText.getText().toString();
            sendLoginData();
            getQuestionData();
        });

    }


    public void getQuestionData(){
        String urlQuestions = "http://107.180.91.147:3031/symptom/questions";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlQuestions, null, response -> {
            try {
                Log.i("questions", response.toString());
                JSONArray array = response.getJSONArray("questions");
                ArrayList<String> questions = new ArrayList<>();
                for(int i=0; i<array.length();++i)
                    questions.add(array.getString(i));
                Log.i("questions", questions.toString());
                startSymptomActivity(questions);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },null);
        Volley.newRequestQueue(getBaseContext()).add(request);

    }

    public void startSymptomActivity(ArrayList<String> questions){
        Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
        myIntent.putExtra("document",document);
        myIntent.putExtra("publicityId",publicityId);
        myIntent.putExtra("type",type);
        myIntent.putExtra("questions",questions);
        startActivity(myIntent);
    }

    public void sendLoginData(){
        JSONObject postData = new JSONObject();
        try {
            postData.put("publicityId",publicityId);
            postData.put("document", document);
            postData.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("VOLLEY", postData.toString());
        String urlLogin = "http://107.180.91.147:3031/login/app";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,urlLogin, postData,
                response -> Log.i("VOLLEY", response.toString()),
                null);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000, 2, 1));
        Volley.newRequestQueue(getBaseContext()).add(jsonObjectRequest);
    }

}
