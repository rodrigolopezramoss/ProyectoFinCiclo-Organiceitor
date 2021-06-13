package com.rlr.proyectoorganiceitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rlr.proyectoorganiceitor.modelo.Evento;
import com.rlr.proyectoorganiceitor.recyclerview.MiAdaptador;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth fbauth;
    private FirebaseDatabase fbdbase;
    private DatabaseReference mDatabase;

    private RecyclerView rv;
    private MiAdaptador adaptador;

    private Spinner spiFil;
    private ArrayAdapter<String> adapFiltro;

    private String idioma;

    private List<Evento> eventos;

    private Button btnAniadir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbauth = FirebaseAuth.getInstance();
        fbdbase = FirebaseDatabase.getInstance();

        btnAniadir = findViewById(R.id.btnAdd);

        eventos = new ArrayList<>();
        mDatabase = fbdbase.getReference();

        idioma = Locale.getDefault().getLanguage();

        spiFil = findViewById(R.id.spinFiltro);

        if (idioma.equals("es")){
            List<String> filtros = new ArrayList<String>(){{
                add("Todos");
                add("Mis eventos");
                add("Grandes");
                add("Medianos");
                add("Pequeños");
            }};
            adapFiltro = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,filtros);
        }else if (idioma.equals("en")){
            List<String> filtros = new ArrayList<String>(){{
                add("All");
                add("My events");
                add("Large");
                add("Medium");
                add("Small");
            }};
            adapFiltro = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,filtros);
        }

        spiFil.setAdapter(adapFiltro);

        spiFil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filtro = spiFil.getSelectedItem().toString();
                loadEventos(filtro);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rv = findViewById(R.id.recycler);

        rv.setLayoutManager( new LinearLayoutManager(this));


        btnAniadir.setOnClickListener(v -> {
            Intent i = new Intent(this,AddActivity.class);
            startActivity(i);
        });
    }

    private void loadEventos(String filtro) {
        mDatabase.child("eventos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    eventos.clear();
                    for (DataSnapshot ds: snapshot.getChildren()){
                        String usid = ds.child("userId").getValue().toString();
                        String nom = ds.child("nombre").getValue().toString();
                        String tip = ds.child("tipo").getValue().toString();
                        String cat = ds.child("categoría").getValue().toString();
                        String fec = ds.child("fecha").getValue().toString();
                        String des = ds.child("descripcion").getValue().toString();
                        String im = ds.child("imagen").getValue().toString();
                        String co = ds.child("contratacion").getValue().toString();
                        String pu = ds.child("publicidad").getValue().toString();
                        Boolean con = false;
                        Boolean pub = false;

                        if(co.equals("true")){
                            con = true;
                        }
                        if(pu.equals("true")){
                            pub = true;
                        }
                        String uid = fbauth.getUid();
                        switch (filtro){
                            case "Todos":
                                eventos.add(new Evento(usid,nom,tip,cat,fec,des,im,con,pub));
                                break;
                            case "All":
                                eventos.add(new Evento(usid,nom,tip,cat,fec,des,im,con,pub));
                                break;
                            case "Mis eventos":
                                if (usid.equals(uid)){
                                    eventos.add(new Evento(usid,nom,tip,cat,fec,des,im,con,pub));
                                }
                                break;
                            case "My events":
                                if (usid.equals(uid)){
                                    eventos.add(new Evento(usid,nom,tip,cat,fec,des,im,con,pub));
                                }
                                break;
                            case "Grandes":
                                if (tip.equals("Grande")){
                                    eventos.add(new Evento(usid,nom,tip,cat,fec,des,im,con,pub));
                                }
                                break;
                            case "Large":
                                if (tip.equals("Grande")){
                                    eventos.add(new Evento(usid,nom,tip,cat,fec,des,im,con,pub));
                                }
                                break;
                            case "Medianos":
                                if (tip.equals("Mediano")){
                                    eventos.add(new Evento(usid,nom,tip,cat,fec,des,im,con,pub));
                                }
                                break;
                            case "Medium":
                                if (tip.equals("Mediano")){
                                    eventos.add(new Evento(usid,nom,tip,cat,fec,des,im,con,pub));
                                }
                                break;
                            case "Pequeños":
                                if (tip.equals("Pequeño")){
                                    eventos.add(new Evento(usid,nom,tip,cat,fec,des,im,con,pub));
                                }
                                break;
                            case "Small":
                                if (tip.equals("Pequeño")){
                                    eventos.add(new Evento(usid,nom,tip,cat,fec,des,im,con,pub));
                                }
                                break;
                        }

                    }
                    adaptador = new MiAdaptador(eventos);
                    rv.setAdapter(adaptador);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu) ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.atras:
                MaterialAlertDialogBuilder madb = new MaterialAlertDialogBuilder(this) ;
                madb.setTitle(R.string.lab_cerrar)
                        .setMessage(R.string.lab_preCerrar)
                        .setNegativeButton(R.string.lab_negacion, (dialog, which) -> {})
                        .setPositiveButton(R.string.lab_afirmacion, (dialog, which) ->
                        {
                            fbauth.signOut();
                            finish();
                        })
                        .create()
                        .show();
                break;
            default :
                return super.onOptionsItemSelected(item);
        }

        return true ;
    }

    @Override
    public void onBackPressed() {
    }
}