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
import com.rmpsoft.looking.utils.Toast_Manager;

import java.util.HashMap;

public class Equipo_PonerAnuncio extends AppCompatActivity {

    EditText et_contacto, et_posicion, et_descripcion;
    Button btn_descartar, btn_publicar;

    FirebaseUser firebaseuser;
    FirebaseAuth firebaseauth;
    FirebaseFirestore firestore;
    private ProgressDialog progressdialog;

    private String equipo;
    private String deporte;
    private String municipio;
    private String posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipo__poner_anuncio);

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle(" ");
        actionbar.setIcon(R.drawable.ic_logo_actionbar);

        et_contacto = findViewById(R.id.PonerAnuncio_et_contacto);
        et_posicion = findViewById(R.id.PonerAnuncio_et_posicion);
        et_descripcion = findViewById(R.id.PonerAunncio_et_descripcion);
        btn_descartar = findViewById(R.id.PonerAnuncio_btn_descartar);
        btn_publicar = findViewById(R.id.PonerAnuncio_btn_publicar);

        firebaseauth = FirebaseAuth.getInstance();
        firebaseuser = firebaseauth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        progressdialog = new ProgressDialog(Equipo_PonerAnuncio.this);

        btn_descartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Equipo_PonerAnuncio.this, Equipo_Home.class));
            }
        });

        btn_publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String contacto = et_contacto.getText().toString();
                String posicion = et_posicion.getText().toString();
                String descripcion = et_descripcion.getText().toString();

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
        String uid = firebaseuser.getUid();

        progressdialog.setCancelable(false);
        progressdialog.show();

        HashMap<Object, String> anuncio = new HashMap<>();

        anuncio.put("equipo", equipo);
        anuncio.put("deporte", deporte);
        anuncio.put("municipio", municipio);
        anuncio.put("posicion", posicion);
        anuncio.put("contacto", contacto);
        anuncio.put("descripcion", descripcion);
        anuncio.put("cerrado", "false");
        anuncio.put("uidcontacto", uid);

        firestore.collection("Anuncios").document().set(anuncio).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast_Manager.showToast(Equipo_PonerAnuncio.this, "El anuncio se publicó correctamente");
                startActivity(new Intent(Equipo_PonerAnuncio.this, Equipo_Home.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast_Manager.showToast(Equipo_PonerAnuncio.this, "No se pudo publicar el anuncio");
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
                }
            }
        });
    }
}