package com.rlr.proyectoorganiceitor.recyclerview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.rlr.proyectoorganiceitor.EventActivity;
import com.rlr.proyectoorganiceitor.R;
import com.rlr.proyectoorganiceitor.modelo.Contacto;
import com.rlr.proyectoorganiceitor.modelo.Evento;

import java.util.List;

public class Mi2Adaptador extends RecyclerView.Adapter<Mi2ViewHolder> {
    private List<Contacto> contactos;

    public Mi2Adaptador(List<Contacto> datos){
        contactos = datos;
    }
    @Override
    public Mi2ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.peque_layout,parent,false);
        return new Mi2ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder( Mi2ViewHolder holder, int position) {
        holder.BindHolder(contactos.get(position));

    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }
}
