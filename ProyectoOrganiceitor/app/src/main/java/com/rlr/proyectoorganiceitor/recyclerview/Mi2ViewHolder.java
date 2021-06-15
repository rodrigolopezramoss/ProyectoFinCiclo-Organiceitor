package com.rlr.proyectoorganiceitor.recyclerview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.rlr.proyectoorganiceitor.ContraActivity;
import com.rlr.proyectoorganiceitor.R;
import com.rlr.proyectoorganiceitor.modelo.Contacto;

import java.util.Locale;

public class Mi2ViewHolder extends RecyclerView.ViewHolder {

    TextView txtNombre;
    ImageView icLlamar;
    String idioma;

    public Mi2ViewHolder(View itemView) {
        super(itemView);

        txtNombre = itemView.findViewById(R.id.txtNomCon);
        icLlamar = itemView.findViewById(R.id.ic_llamar);
        idioma = Locale.getDefault().getLanguage();
    }

    public void BindHolder(Contacto contacto) {
        txtNombre.setText(contacto.getNombre());

        icLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idioma.equals("es")){
                    Snackbar.make(v, "Llamando al " + contacto.getNumContac() + ", " + contacto.getNombre(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else if(idioma.equals("en")){
                    Snackbar.make(v, "Calling " + contacto.getNumContac() + ", " + contacto.getNombre(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });


    }
}
