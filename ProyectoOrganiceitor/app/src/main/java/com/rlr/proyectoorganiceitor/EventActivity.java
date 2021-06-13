package com.rlr.proyectoorganiceitor;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.rlr.proyectoorganiceitor.modelo.Evento;

import java.util.Locale;

public class EventActivity extends AppCompatActivity {

    private FirebaseAuth fbauth;
    private FirebaseDatabase fbdbase;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    private ImageView imgEvent;
    private ImageButton btnImg;

    private TextView txtNom,txtTip,txtCat,txtDes;
    private Button btnCon,btnPub,btnBorrar;
    private Evento ev;
    private String univoco;
    private String cate;
    private String acti;

    private String idioma;

    private static final int GALLERY_INTENT = 1;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        txtNom = findViewById(R.id.txtNom);
        txtTip = findViewById(R.id.txtTip);
        txtCat = findViewById(R.id.txtCat);
        txtDes = findViewById(R.id.txtDescri);

        fbdbase = FirebaseDatabase.getInstance();
        mDatabase = fbdbase.getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        imgEvent = findViewById(R.id.imgEvent);

        btnCon = findViewById(R.id.btnContra);
        btnPub = findViewById(R.id.btnPubli);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnImg = findViewById(R.id.imgBtnEvent);

        fbauth = FirebaseAuth.getInstance();

        mProgressDialog = new ProgressDialog(this);

        idioma = Locale.getDefault().getLanguage();

        initValues();


        btnCon.setOnClickListener(v -> doIr(1));
        btnPub.setOnClickListener(v -> doIr(2));

    }

    private void doIr(int in) {
        if (in == 1){
            acti = "contratacion";
        } else if (in == 2){
            acti = "publicidad";
        }
        int permiso = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permiso == PackageManager.PERMISSION_GRANTED){

            Intent i = new Intent(EventActivity.this, ContraActivity.class);
            i.putExtra("evenCate", cate);
            i.putExtra("evenActi", acti);
            startActivity(i);
        }else if(permiso == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
        }
    }

    private void initValues() {
        ev = (Evento) getIntent().getExtras().getSerializable("itemDetail");

        String uid = fbauth.getUid();
        String img = ev.getImagen();

        String tipo = ev.getTipo();

        univoco = ev.getNombre().concat(uid);
        cate = ev.getCategoría();

        txtNom.setText(ev.getNombre());

        if (idioma.equals("en")){
            switch (tipo){
                case "Grande":
                    txtTip.setText("Large");
                    break;
                case "Mediano":
                    txtTip.setText("Medium");
                    break;
                case "Pequeño":
                    txtTip.setText("Small");
                    break;

            }
            switch (cate){
                case "Benéfico":
                    txtCat.setText("Charity");
                    break;
                case "Charla":
                    txtCat.setText("Talk");
                    break;
                case "Concierto":
                    txtCat.setText("Concert");
                    break;
                case "Conferencia":
                    txtCat.setText("Conference");
                    break;
                case "Deportivo":
                    txtCat.setText("Sporting");
                    break;
                case "Fiesta":
                    txtCat.setText("Party");
                    break;
                case "Feria gastronómica":
                    txtCat.setText("Gastronomic fair");
                    break;
                case "Feria de videojuegos":
                    txtCat.setText("Videogame fair");
                    break;

            }
        }
        txtDes.setText(ev.getDescripcion());

        if (ev.getUserId().equals(uid)){
            imgEvent.setVisibility(View.GONE);
            if (!ev.isContratacion()){
                btnCon.setVisibility(View.GONE);
            }
            if (!ev.isPublicidad()){
                btnPub.setVisibility(View.GONE);
            }
        } else if (!ev.getUserId().equals(uid)){
            btnImg.setVisibility(View.GONE);
            btnCon.setVisibility(View.GONE);
            btnPub.setVisibility(View.GONE);
            btnBorrar.setVisibility(View.GONE);
        }

        Glide.with(EventActivity.this)
                .load(img)
                .fitCenter()
                .centerCrop()
                .into(imgEvent);

        Glide.with(EventActivity.this)
                .load(img)
                .fitCenter()
                .centerCrop()
                .into(btnImg);


        btnBorrar.setOnClickListener(v -> doBorrado());
        btnImg.setOnClickListener(v -> doSubir());

    }

    private void doSubir() {
        int permiso = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permiso == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityIfNeeded(intent,GALLERY_INTENT);
        }else if(permiso == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK){

        }
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            mProgressDialog.setTitle("Subiendo...");
            mProgressDialog.setMessage("Subiendo imagen");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            Uri uri = data.getData();

            StorageReference filepath = mStorage.child("fotos").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();


                    String descFoto = taskSnapshot.getStorage().getName();
                    mStorage.child("fotos/"+descFoto).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(EventActivity.this)
                                    .load(uri.toString())
                                    .fitCenter()
                                    .centerCrop()
                                    .into(btnImg);

                            mDatabase.child("eventos").child(univoco).child("imagen")
                                    .setValue(uri.toString());
                        }
                    });
                }
            });


        }
    }

    private void doBorrado() {
        MaterialAlertDialogBuilder madb = new MaterialAlertDialogBuilder(this) ;
        madb.setTitle(R.string.lab_borrar)
                .setMessage(R.string.lab_preBorrar)
                .setNegativeButton(R.string.lab_negacion, (dialog, which) -> {})
                .setPositiveButton(R.string.lab_afirmacion, (dialog, which) ->
                {
                    String id = ev.getNombre().concat(ev.getUserId());
                    mDatabase.child("eventos").child(id).removeValue();
                    finish();
                })
                .create()
                .show();

    }
}