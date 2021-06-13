package com.rlr.proyectoorganiceitor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rlr.proyectoorganiceitor.modelo.Evento;
import com.rlr.proyectoorganiceitor.modelo.Usuario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    private FirebaseAuth fbauth;
    private FirebaseDatabase fbdbase;
    private DatabaseReference mDatabase;

    private DatePickerDialog.OnDateSetListener setListener;

    private Spinner spiTipo,spiCate;
    private ArrayAdapter<String> adapTipo, adapCate;

    private TextView txtFecha;
    private EditText editNombre,editDesc;
    private CheckBox checkContra, checkPubli;
    private Button btnCrear;

    private String idioma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        fbauth = FirebaseAuth.getInstance();


        editNombre = findViewById(R.id.edtNombre);
        txtFecha = findViewById(R.id.edtFecha);
        editDesc = findViewById(R.id.edtDesc);

        spiTipo = findViewById(R.id.spinTipo);
        spiCate = findViewById(R.id.spinCate);

        checkContra = findViewById(R.id.cbxContra);
        checkPubli = findViewById(R.id.cbxPubli);

        btnCrear = findViewById(R.id.btnCrear);

        idioma = Locale.getDefault().getLanguage();

        if (idioma.equals("es")){
            List<String> tipos = new ArrayList<String>(){{
                add("Grande");
                add("Mediano");
                add("Pequeño");
            }};
            List<String> categorias = new ArrayList<String>(){{
                add("Benéfico");
                add("Charla");
                add("Concierto");
                add("Conferencia");
                add("Deportivo");
                add("Fiesta");
                add("Feria gastronómica");
                add("Feria de videojuegos");
            }};

            adapTipo = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,tipos);
            adapCate = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,categorias);

        }else if (idioma.equals("en")){
            List<String> tipos = new ArrayList<String>(){{
                add("Large");
                add("Medium");
                add("Small");
            }};
            List<String> categorias = new ArrayList<String>(){{
                add("Charity");
                add("Concert");
                add("Conference");
                add("Gastronomic fair");
                add("Party");
                add("Sporting");
                add("Talk");
                add("Videogame fair");
            }};

            adapTipo = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,tipos);
            adapCate = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,categorias);

        }

        spiTipo.setAdapter(adapTipo);
        spiCate.setAdapter(adapCate);

        Calendar calendar = Calendar.getInstance();
        final int ano = calendar.get(Calendar.YEAR);
        final int mes = calendar.get(Calendar.MONTH);
        final int dia = calendar.get(Calendar.DAY_OF_MONTH);

        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,setListener,ano,mes,dia);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                txtFecha.setText(date);
            }
        };

        btnCrear.setOnClickListener(v -> doCreacion());

    }

    private void doCreacion() {
        String nombre = convertir(editNombre);
        String descri = convertir(editDesc);
        String tipo = spiTipo.getSelectedItem().toString();
        String categoria = spiCate.getSelectedItem().toString();

        String uid = fbauth.getUid();
        String fec = txtFecha.getText().toString();
        String univoco = nombre.concat(uid);
        Evento evn = new Evento();
        evn.setUserId(uid);
        evn.setNombre(nombre);
        switch(tipo){
            case "Large":
                evn.setTipo("Grande");
                break;
            case "Medium":
                evn.setTipo("Mediano");
                break;
            case "Small":
                evn.setTipo("Pequeño");
                break;
        }
        switch(categoria){
            case "Charity":
                evn.setCategoría("Benéfico");
                break;
            case "Talk":
                evn.setCategoría("Charla");
                break;
            case "Concert":
                evn.setCategoría("Concierto");
                break;
            case "Conference":
                evn.setCategoría("Conferencia");
                break;
            case "Sporting":
                evn.setCategoría("Deportivo");
                break;
            case "Party":
                evn.setCategoría("Fiesta");
                break;
            case "Gastronomic fair":
                evn.setCategoría("Feria gastronómica");
                break;
            case "Videogame fair":
                evn.setCategoría("Feria de videojuegos");
                break;
        }
        evn.setFecha(fec);
        evn.setDescripcion(descri);

        if(checkContra.isChecked()){
            evn.setContratacion(true);
        }
        if(checkPubli.isChecked()){
            evn.setPublicidad(true);
        }

        if (uid.isEmpty()||nombre.isEmpty()||tipo.isEmpty()||categoria.isEmpty()||descri.isEmpty()) {
            Snackbar s = Snackbar.make(btnCrear, R.string.lab_errorRe, BaseTransientBottomBar.LENGTH_LONG);
            s.show();
        } else if ((fec.equals("Seleccionar"))||(fec.equals("Select"))){
            Snackbar s = Snackbar.make(btnCrear, R.string.lab_errorFe, BaseTransientBottomBar.LENGTH_LONG);
            s.show();
        } else {
            fbdbase = FirebaseDatabase.getInstance();
            mDatabase = fbdbase.getReference();
            mDatabase.child("eventos").child(univoco)
                    .setValue(evn);
            //
            finish();
            return;
        }


    }

    private String convertir(EditText view) {
        return view.getText().toString().trim();
    }
}