package com.example.covid19_utec;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolderNotification> {

    ArrayList<String> mensajes;

    public NotificationAdapter(ArrayList<String> mensajes){
        this.mensajes = mensajes;
    }
    @NonNull
    @Override
    public ViewHolderNotification onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,null,false);
        return new ViewHolderNotification(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNotification holder, int position) {
        holder.asignarDatos(mensajes.get(position));
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public class ViewHolderNotification extends RecyclerView.ViewHolder {

        TextView messageView;
        public ViewHolderNotification(@NonNull View itemView) {
            super(itemView);
            messageView = itemView.findViewById(R.id.message_content);
        }

        public void asignarDatos(String data) {
            messageView.setText(data);
        }
    }
}
