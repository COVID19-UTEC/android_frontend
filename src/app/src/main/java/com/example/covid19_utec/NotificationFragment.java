package com.example.covid19_utec;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
    String title = "Notificaciones";
    ArrayList<String> mensajes;
    RecyclerView recycler;

    public NotificationFragment() {
        mensajes = new ArrayList<>();
        for (int i =0;i<50;i++){
            mensajes.add("Mensaje "+i+" ");
        }
    }

    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        ((HomeActivity)getActivity()).changeFragmentTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        recycler = view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        NotificationAdapter adapter = new NotificationAdapter(mensajes);
        recycler.setAdapter(adapter);
        return view;
    }
}
