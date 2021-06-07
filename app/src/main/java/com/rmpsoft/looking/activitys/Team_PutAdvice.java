package com.rmpsoft.looking.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rmpsoft.looking.R;
import com.rmpsoft.looking.model.Advice;
import com.rmpsoft.looking.utils.Toast_Manager;

import java.math.BigInteger;
import java.text.Normalizer;
import java.util.Random;

public class Team_PutAdvice extends AppCompatActivity {

    EditText et_contacto, et_posicion, et_descripcion;
    Button btn_descartar, btn_publicar;

    FirebaseUser firebaseuser;
    FirebaseAuth firebaseauth;
    FirebaseFirestore firestore;
    private ProgressDialog progressdialog;

    private String equipo, deporte, municipio, categoria, imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_put_advice);

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle(" ");
        actionbar.setIcon(R.drawable.ic_actionbar_logo);

        et_contacto = findViewById(R.id.PonerAnuncio_et_contacto);
        et_posicion = findViewById(R.id.PonerAnuncio_et_posicion);
        et_descripcion = findViewById(R.id.PonerAunncio_et_descripcion);
        btn_descartar = findViewById(R.id.PonerAnuncio_btn_descartar);
        btn_publicar = findViewById(R.id.PonerAnuncio_btn_publicar);

        firebaseauth = FirebaseAuth.getInstance();
        firebaseuser = firebaseauth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        progressdialog = new ProgressDialog(Team_PutAdvice.this);

        btn_descartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Team_PutAdvice.this, Team_Home.class));
            }
        });

        btn_publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String contacto = formatoString(et_contacto.getText().toString());
                String posicion = formatoString(et_posicion.getText().toString());
                String descripcion = formatoString(et_descripcion.getText().toString());

                /* Validamos que los campos están rellenados */
                if (contacto.isEmpty()) {
                    et_contacto.setError("Debes rellenar el campo");
                    et_contacto.setFocusable(true);
                }
                if (posicion.isEmpty()) {
                    et_posicion.setError("Debes rellenar el campo");
                    et_posicion.setFocusable(true);
                }
                if (descripcion.isEmpty()) {
                    et_descripcion.setError("Debes rellenar el campo");
                    et_descripcion.setFocusable(true);
                }

                publicarAnuncio(contacto, posicion, descripcion);
            }
        });
    }

    @Override
    protected void onStart() {
        getDataUser();
        super.onStart();
    }

    /* Método que guarda el anuncio en la bbdd */
    private void publicarAnuncio(String contacto, String posicion, String descripcion) {
        progressdialog.setCancelable(false);
        progressdialog.show();

        String uid = firebaseuser.getUid();
        Random randomuid = new Random();
        String uidadvice = new BigInteger(100, randomuid).toString(32);

        Advice newAdvice = new Advice();
        newAdvice.setUidcontacto(uid);
        newAdvice.setUidadvice(uidadvice);
        newAdvice.setEquipo(equipo);
        newAdvice.setDeporte(deporte);
        newAdvice.setMunicipio(municipio);
        newAdvice.setPosicion(posicion);
        newAdvice.setContacto(contacto);
        newAdvice.setDescripcion(descripcion);
        newAdvice.setCategoria(categoria);
        newAdvice.setImagen(imagen);

        firestore.collection("Anuncios").document(uidadvice).set(newAdvice).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast_Manager.showToast(Team_PutAdvice.this, "El anuncio se publicó correctamente");
                startActivity(new Intent(Team_PutAdvice.this, Team_Home.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast_Manager.showToast(Team_PutAdvice.this, "No se pudo publicar el anuncio");
            }
        });
        progressdialog.dismiss();
    }

    /* Este método obtiene datos del equipo para completar la publicacion*/
    private void getDataUser() {
        String uid = firebaseuser.getUid();

        firestore.collection("Equipos").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    equipo = documentSnapshot.getString("equipo");
                    deporte = documentSnapshot.getString("deporte");
                    municipio = documentSnapshot.getString("municipio");
                    categoria = documentSnapshot.getString("categoria");
                    imagen = documentSnapshot.getString("image");
                }
            }
        });
    }

    private static String formatoString (String textoUsuario) {

        String textoMinusculas = textoUsuario.toLowerCase();
        char[] arr = textoMinusculas.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        String textoMayuscula = String.valueOf(arr);
        textoMayuscula = Normalizer.normalize(textoMayuscula, Normalizer.Form.NFD);
        return textoMayuscula.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }
}