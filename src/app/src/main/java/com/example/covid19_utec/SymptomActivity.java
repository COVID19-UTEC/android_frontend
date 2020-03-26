package com.example.covid19_utec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SymptomActivity extends AppCompatActivity {
    private Button yesButton1;
    private Button noButton1;
    private Button yesButton2;
    private Button noButton2;
    private TextView sendButton;
    private ImageView sendImage;
    private EditText temperature;
    private RadioButtonView groupRadio1;
    private RadioButtonView groupRadio2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);
        yesButton1 = findViewById(R.id.si_button1);
        yesButton2 = findViewById(R.id.si_button2);
        noButton1 = findViewById(R.id.no_button1);
        noButton2 = findViewById(R.id.no_button2);
        sendButton = findViewById(R.id.send);
        sendImage = findViewById(R.id.sendImage);
        temperature = findViewById(R.id.temperature);

        groupRadio1 = new RadioButtonView(yesButton1,noButton1,this);
        groupRadio2 = new RadioButtonView(yesButton2,noButton2,this);

        yesButton1.setOnClickListener((View v) -> {
            groupRadio1.onButtonClick(yesButton1);
        });

        noButton1.setOnClickListener((View v) ->{
            groupRadio1.onButtonClick(noButton1);
        });

        yesButton2.setOnClickListener((View v) -> {
            groupRadio2.onButtonClick(yesButton2);
        });

        noButton2.setOnClickListener((View v) ->{
            groupRadio2.onButtonClick(noButton2);
        });



    }

    public void sendData(View view){
        Integer temp =Integer.parseInt(temperature.getText().toString());
        boolean tos = groupRadio1.getSelectedButton();
        boolean problemas_respiratorios= groupRadio2.getSelectedButton();

        Toast.makeText(this, tos+";"+problemas_respiratorios+";"+temp, Toast.LENGTH_SHORT).show();

    }

}
