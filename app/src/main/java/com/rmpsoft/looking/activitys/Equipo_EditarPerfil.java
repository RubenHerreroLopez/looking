package com.rmpsoft.looking.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rmpsoft.looking.R;
import com.rmpsoft.looking.utils.Toast_Manager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class Equipo_EditarPerfil extends AppCompatActivity {

    ImageView perfil;
    EditText et_equipo, et_deporte, et_municipio, et_dia;
    ImageButton btn_perfil, btn_equipo, btn_equipoOK, btn_deporte, btn_deporteOK, btn_municipio, btn_municipioOK, btn_dia, btn_diaOK;
    Button btn_descartar, btn_confirmar;

    FirebaseAuth firebaseauth;
    FirebaseUser firebaseuser;
    FirebaseFirestore firestore;
    StorageReference storage;
    ProgressDialog progressDialog;

    Bitmap bitmap = null;

    String equipo;
    String deporte;
    String municipio;
    String dia;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipo__editar_perfil);

        firebaseauth = FirebaseAuth.getInstance();
        firebaseuser = firebaseauth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        uid = firebaseuser.getUid();
        storage = FirebaseStorage.getInstance().getReference().child(uid);
        progressDialog = new ProgressDialog(Equipo_EditarPerfil.this);

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle(" ");
        actionbar.setIcon(R.drawable.ic_actionbar_logo);

        perfil = findViewById(R.id.EqEdit_image_perfil);
        et_equipo = findViewById(R.id.EqEdit_et_equipo);
        et_deporte = findViewById(R.id.EqEdit_et_deporte);
        et_municipio = findViewById(R.id.EqEdit_et_municipio);
        et_dia = findViewById(R.id.EqEdit_et_dia);

        btn_perfil = findViewById(R.id.EqEdit_btn_edit_IP);
        btn_equipo = findViewById(R.id.EqEdit_btn_edit_EQ);
        btn_equipoOK = findViewById(R.id.EqEdit_btn_OK_EQ);
        btn_deporte = findViewById(R.id.EqEdit_btn_edit_DP);
        btn_deporteOK = findViewById(R.id.EqEdit_btn_OK_DP);
        btn_municipio = findViewById(R.id.EqEdit_btn_edit_MC);
        btn_municipioOK = findViewById(R.id.EqEdit_btn_OK_MC);
        btn_dia = findViewById(R.id.EqEdit_btn_edit_DIA);
        btn_diaOK = findViewById(R.id.EqEdit_btn_OK_DIA);
        btn_descartar = findViewById(R.id.EqEdit_btn_descartar);
        btn_confirmar = findViewById(R.id.EqEdit_btn_confirmar);

        getData();

        btn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(Equipo_EditarPerfil.this);
            }
        });

        btn_equipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_equipo.setEnabled(true);
                btn_equipo.setVisibility(View.GONE);
                btn_equipoOK.setVisibility(View.VISIBLE);

            }
        });

        btn_equipoOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_equipo.setEnabled(false);
                btn_equipoOK.setVisibility(View.GONE);
                btn_equipo.setVisibility(View.VISIBLE);
            }
        });

        btn_deporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_deporte.setEnabled(true);
                btn_deporte.setVisibility(View.GONE);
                btn_deporteOK.setVisibility(View.VISIBLE);
            }
        });

        btn_deporteOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_deporte.setEnabled(false);
                btn_deporteOK.setVisibility(View.GONE);
                btn_deporte.setVisibility(View.VISIBLE);
            }
        });

        btn_municipio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_municipio.setEnabled(true);
                btn_municipio.setVisibility(View.GONE);
                btn_municipioOK.setVisibility(View.VISIBLE);
            }
        });

        btn_municipioOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_municipio.setEnabled(false);
                btn_municipioOK.setVisibility(View.GONE);
                btn_municipio.setVisibility(View.VISIBLE);
            }
        });

        btn_dia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_dia.setEnabled(true);
                btn_dia.setVisibility(View.GONE);
                btn_diaOK.setVisibility(View.VISIBLE);
            }
        });

        btn_diaOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_dia.setEnabled(false);
                btn_diaOK.setVisibility(View.GONE);
                btn_dia.setVisibility(View.VISIBLE);
            }
        });

        btn_descartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Equipo_EditarPerfil.this, Equipo_Home.class));
            }
        });

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
                startActivity(new Intent(Equipo_EditarPerfil.this, Equipo_Home.class));
                finish();
            }
        });

    }

    /* Método para la subida de la imagen */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageuri = CropImage.getPickImageResultUri(Equipo_EditarPerfil.this, data);

            /* Recortando imagen */
            CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON)
                    .setRequestedSize(480, 480)
                    .setAspectRatio(1,1).start(Equipo_EditarPerfil.this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK) {
                Uri resulturi = result.getUri();

                File url = new File(resulturi.getPath());

                /* Comprimiendo imagen */
                try {
                    bitmap = new Compressor(Equipo_EditarPerfil.this).setMaxWidth(480).setMaxHeight(480).setQuality(90).compressToBitmap(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                final byte[] bitmap_byte = byteArrayOutputStream.toByteArray();

                final String nombreArchivo = uid + "comprimido.jpg";

                progressDialog.setTitle("Subiendo foto...");
                progressDialog.setMessage("Por favor, espere. Esta \nacción puede llevar unos segundos");
                progressDialog.show();

                final StorageReference reference = storage.child(nombreArchivo);
                UploadTask upload = reference.putBytes(bitmap_byte);

                /* Subiendo la imagen a Storage */
                Task<Uri> uirTask = upload.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }
                        return reference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri uploaduri = task.getResult();

                        Map<String, Object> values = new HashMap<>();
                        values.put("image", uploaduri.toString());

                        firestore.collection("Equipos").document(uid).update(values);

                        progressDialog.dismiss();
                        Toast_Manager.showToast(Equipo_EditarPerfil.this, "Imagen subida con éxito");

                    }
                });

            }
        }
    }

    /* Método que actualiza los datos introducidos por el usuario */
    public void updateData() {
        progressDialog.setTitle("Actualizando datos...");
        progressDialog.setMessage("Por favor, espere. Esta \nacción puede llevar unos segundos");
        progressDialog.show();

        equipo = et_equipo.getText().toString();
        deporte = et_deporte.getText().toString();
        municipio = et_municipio.getText().toString();
        dia = et_dia.getText().toString();

        Map<String, Object> values = new HashMap<>();

        values.put("equipo", equipo);
        values.put("deporte", deporte);
        values.put("horario", dia);
        values.put("municipio", municipio);

        firestore.collection("Equipos").document(uid).update(values);
        progressDialog.dismiss();
        Toast_Manager.showToast(Equipo_EditarPerfil.this, "Datos actualizados con éxito");
    }

    /* Método que obtiene los datos del usuario y los asigna a los EditText */
    public void getData () {
        String uid = firebaseuser.getUid();
        firestore.collection("Equipos").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    equipo = documentSnapshot.getString("equipo");
                    deporte = documentSnapshot.getString("deporte");
                    municipio = documentSnapshot.getString("municipio");
                    dia = documentSnapshot.getString("horario");

                    et_equipo.setEnabled(false);
                    et_equipo.setText(equipo);
                    et_deporte.setEnabled(false);
                    et_deporte.setText(deporte);
                    et_municipio.setEnabled(false);
                    et_municipio.setText(municipio);
                    et_dia.setEnabled(false);
                    et_dia.setText(dia);
                }
            }
        });
    }

}