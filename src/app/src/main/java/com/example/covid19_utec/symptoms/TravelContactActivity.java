package com.example.covid19_utec.symptoms;

import android.os.Bundle;

import com.example.covid19_utec.BaseSymptomActivity;
import com.example.covid19_utec.R;


public class TravelContactActivity extends BaseSymptomActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_contact);
        setVariables(DirectContactActivity.class);
        setOnCLickListeners();
        getAnswers();
    }
}
