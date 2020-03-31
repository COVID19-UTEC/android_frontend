package com.example.covid19_utec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class BaseSymptomActivity extends AppCompatActivity {
    Button yesButton;
    Button noButton;
    protected RadioButtonView groupRadio;
    protected Class nextSymptom;
    protected String document;
    protected String type;
    protected ArrayList<Boolean> answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setOnCLickListeners(){
        yesButton.setOnClickListener((View v) -> {
            groupRadio.onButtonClick(yesButton);
        });

        noButton.setOnClickListener((View v) ->{
            groupRadio.onButtonClick(noButton);
        });
    }

    public void setVariables(Class activityClass){
        yesButton = findViewById(R.id.si_button);
        noButton = findViewById(R.id.no_button);
        groupRadio = new RadioButtonView(yesButton,noButton,this);
        nextSymptom = activityClass;
    }


    public void sendData(View view){
        boolean symptom = groupRadio.getSelectedButton();
        //Toast.makeText(this, ""+symptom, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, nextSymptom);
        answers.add(symptom);
        intent.putExtra("answers", answers);
        intent.putExtra("document",document);
        intent.putExtra("type",type);
        startActivity(intent);
    }

    public void getAnswers(){
        Intent intent = getIntent();
        document = intent.getStringExtra("document");
        type = intent.getStringExtra("type");
        answers = (ArrayList<Boolean>) intent.getSerializableExtra("answers");
    }
}
