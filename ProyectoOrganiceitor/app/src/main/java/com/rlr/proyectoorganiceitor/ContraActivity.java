package com.rlr.proyectoorganiceitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rlr.proyectoorganiceitor.modelo.Contacto;
import com.rlr.proyectoorganiceitor.recyclerview.Mi2Adaptador;
import com.rlr.proyectoorganiceitor.recyclerview.MiAdaptador;

import java.util.ArrayList;
import java.util.List;

public class ContraActivity extends AppCompatActivity {

    private FirebaseAuth fbauth;
    private FirebaseDatabase fbdbase;
    private DatabaseReference mDatabase;

    private List<Contacto> contactos;
    private RecyclerView rv;
    private Mi2Adaptador adaptador;

    private String cat;
    private String act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contra);

        fbauth = FirebaseAuth.getInstance();
        fbdbase = FirebaseDatabase.getInstance();
        mDatabase = fbdbase.getReference();

        contactos = new ArrayList<>();

        cat = (String) getIntent().getExtras().getSerializable("evenCate");
        act = (String) getIntent().getExtras().getSerializable("evenActi");

        loadContactos(cat,act);

        rv = findViewById(R.id.recyCon);

        rv.setLayoutManager( new LinearLayoutManager(this));
    }

    private void loadContactos(String cat, String act) {
        mDatabase.child(act).child(cat).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    contactos.clear();
                    for (DataSnapshot ds: snapshot.getChildren()){
                        String nom = ds.child("nombre").getValue().toString();
                        String num = ds.child("numero").getValue().toString();

                        contactos.add(new Contacto(nom,num));
                    }
                    adaptador = new Mi2Adaptador(contactos);
                    rv.setAdapter(adaptador);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}