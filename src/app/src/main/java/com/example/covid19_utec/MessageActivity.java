package com.example.covid19_utec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {
    TextView scoreView;
    TextView messageView;
    String message;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        scoreView = findViewById(R.id.result);
        messageView = findViewById(R.id.mensaje);
        Intent intent = getIntent();
        message = intent.getStringExtra("message");
        score = intent.getIntExtra("score",0);
        ArrayList<Boolean> answers = (ArrayList<Boolean>) intent.getSerializableExtra("answers");
        scoreView.setText(Integer.toString(score));
        messageView.setText(message);
    }
}
