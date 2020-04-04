package com.example.covid19_utec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    ArrayList<String> mensajes;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        mensajes = new ArrayList<>();
        for (int i =0;i<50;i++){
            mensajes.add("Mensaje "+i+" ");
        }

        NotificationAdapter adapter = new NotificationAdapter(mensajes);
        recycler.setAdapter(adapter);
    }
}
