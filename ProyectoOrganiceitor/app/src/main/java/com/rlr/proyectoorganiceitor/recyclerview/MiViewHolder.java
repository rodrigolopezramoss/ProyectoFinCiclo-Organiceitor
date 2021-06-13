package com.rlr.proyectoorganiceitor.recyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rlr.proyectoorganiceitor.R;
import com.rlr.proyectoorganiceitor.modelo.Evento;

import java.util.Locale;

public class MiViewHolder extends RecyclerView.ViewHolder {
    TextView txtNom,txtUsu,txtFec;

    private FirebaseDatabase fbdbase;
    private DatabaseReference mDatabase;

    public MiViewHolder(@NonNull View itemView) {
        super(itemView);

        txtNom = itemView.findViewById(R.id.txtNomEve);
        txtUsu = itemView.findViewById(R.id.txtNomUsu);
        txtFec = itemView.findViewById(R.id.txtFec);

        fbdbase = FirebaseDatabase.getInstance();
        mDatabase = fbdbase.getReference();


    }

    public void BindHolder(Evento evento) {

        txtNom.setText(evento.getNombre());
        txtFec.setText(evento.getFecha());

        mDatabase.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){
                        String usid = ds.child("uid").getValue().toString();
                        String usu = ds.child("nombre").getValue().toString();
                        String idioma = Locale.getDefault().getLanguage();
                        if (usid.equals(evento.getUserId())){
                            if (idioma.equals("es")){
                                txtUsu.setText("Hecho por " + usu);
                            }else if (idioma.equals("en")){
                                txtUsu.setText("Made by " + usu);
                            }

                        }

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
