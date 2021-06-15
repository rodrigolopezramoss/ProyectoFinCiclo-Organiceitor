package com.rlr.proyectoorganiceitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rlr.proyectoorganiceitor.modelo.Usuario;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth fbauth;
    private FirebaseDatabase fbdbase;

    private TextInputEditText nom,ema,pas;

    private Button btnRegistrar, btnVolver;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nom = findViewById(R.id.name);
        ema = findViewById(R.id.email);
        pas = findViewById(R.id.password);

        btnRegistrar = findViewById(R.id.btnRegistro);
        btnVolver = findViewById(R.id.btnVolver);

        btnRegistrar.setOnClickListener(v -> doRegistro());
        btnVolver.setOnClickListener(v -> doAtras());
    }

    private void doAtras() {
            finish() ;
    }

    private void doRegistro() {
        String nombre = convertir(nom);
        String email = convertir(ema);
        String password = convertir(pas);

        if (email.isEmpty()||password.isEmpty()||nombre.isEmpty()) {
            Snackbar s = Snackbar.make(btnRegistrar, R.string.lab_errorRe, BaseTransientBottomBar.LENGTH_LONG);
            s.show();
        } else {
            fbauth = FirebaseAuth.getInstance() ;
            fbauth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {


                            // Cogemos el UID del usuario
                            String uid = fbauth.getUid();

                            //
                            Usuario usr = new Usuario();
                            usr.setUid(uid);
                            usr.setNombre(nombre);
                            usr.setEventos(new ArrayList<>());

                            // Guardar en la base de datos, la información
                            // del usuario.
                            fbdbase = FirebaseDatabase.getInstance() ;
                            mDatabase = fbdbase.getReference();
                            mDatabase.child("usuarios").child(uid)
                                    .setValue(usr);
                            //

                        }
                    });
            finish();
        }
    }

    private String convertir(TextInputEditText view) {
        return view.getText().toString().trim();
    }
}