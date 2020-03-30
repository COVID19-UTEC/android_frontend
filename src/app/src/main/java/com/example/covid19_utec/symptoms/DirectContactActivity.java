package com.example.covid19_utec.symptoms;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid19_utec.BaseSymptomActivity;
import com.example.covid19_utec.R;
import com.example.covid19_utec.MessageActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class DirectContactActivity extends BaseSymptomActivity {
    String message;
    int score;
    Map<String,Object> data;
    TextView sendView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_contact);
        sendView = findViewById(R.id.send);
        sendView.setText("Enviar ");
        setVariables(MessageActivity.class);
        setOnCLickListeners();
        getAnswers();
    }

    @Override
    public void sendData(View view) {
        boolean symptom = groupRadio.getSelectedButton();
        answers.add(symptom);
        data = new HashMap<>();
        data.put("document",document);
        data.put("type",type);
        data.put("result",answers);
        JSONObject postData =  new JSONObject(data);

        Log.i("VOLLEY", postData.toString());
        String urlSymptom = "http://107.180.91.147:3031/symptom/report";
        RequestQueue mQueue = Volley.newRequestQueue(getBaseContext());
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
        mQueue.add(jsonObjectRequest);
    }

    public void startMessageActivity(){
        Intent intent = new Intent(this, nextSymptom);

        intent.putExtra("document",document);
        intent.putExtra("type",type);
        intent.putExtra("message",message);
        intent.putExtra("score",score);
        intent.putExtra("answers",answers);
        startActivity(intent);
    }
}
