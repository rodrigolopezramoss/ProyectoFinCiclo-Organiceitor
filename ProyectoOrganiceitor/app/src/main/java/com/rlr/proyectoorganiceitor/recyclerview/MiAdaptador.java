package com.rlr.proyectoorganiceitor.recyclerview;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rlr.proyectoorganiceitor.AddActivity;
import com.rlr.proyectoorganiceitor.EventActivity;
import com.rlr.proyectoorganiceitor.R;
import com.rlr.proyectoorganiceitor.modelo.Evento;

import java.io.Serializable;
import java.util.List;

public class MiAdaptador extends RecyclerView.Adapter<MiViewHolder> {

    private List<Evento> lista;

    public MiAdaptador(List<Evento> datos){
        lista = datos;
    }

    @Override
    public MiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                                   .inflate(R.layout.card_layout,parent,false);
        return new MiViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull MiViewHolder holder, int position) {
        final Evento item = lista.get(position);

        holder.BindHolder(lista.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), EventActivity.class);
                i.putExtra("itemDetail",  item);
                holder.itemView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
