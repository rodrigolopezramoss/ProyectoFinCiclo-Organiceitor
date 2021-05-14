package com.rlr.proyectoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth fbauth;

    private TextInputEditText inEma,inPas;

    private Button btnEntrar, btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fbauth = FirebaseAuth.getInstance();

        inEma = findViewById(R.id.emailInput);
        inPas = findViewById(R.id.passInput);

        btnEntrar = findViewById(R.id.btn_login);
        btnRegistrar = findViewById(R.id.btn_register);

        btnEntrar.setOnClickListener(v -> doLogin());
        btnRegistrar.setOnClickListener(v ->
        {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });
    }

    private void doLogin() {
        String ema = convertir(inEma);
        String pas = convertir(inPas);

        if (ema.isEmpty()||pas.isEmpty()){
            Snackbar s = Snackbar.make(btnEntrar,"Usuario y contraseña obligatorios", LENGTH_LONG);
            s.show();
        }else {
            fbauth.signInWithEmailAndPassword(ema,pas)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Snackbar s = Snackbar.make(btnEntrar,"Usuario o contraseña incorrectos", LENGTH_LONG);
                            s.show();
                        }else {
                            Intent i = new Intent(this,MainActivity.class);
                            startActivity(i);
                        }

                    });

        }
    }

    private String convertir(TextInputEditText view) {
        return view.getText().toString().trim();
    }

    @Override
    public void onBackPressed() {
    }
}