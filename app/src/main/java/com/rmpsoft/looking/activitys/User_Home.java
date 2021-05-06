package com.rmpsoft.looking.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rmpsoft.looking.LoginActivity;
import com.rmpsoft.looking.R;
import com.rmpsoft.looking.utils.Toast_Manager;

import java.util.Objects;

public class User_Home extends AppCompatActivity {

    FirebaseAuth firebaseauth;
    FirebaseUser firebaseuser;
    FirebaseFirestore firestore;

    Button btn_cerrar;
    TextView tv_nombreUser;

    String nombreUsuario;
    String apellidoUsuario;
    String nombreCompleto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__home);

        firebaseauth = FirebaseAuth.getInstance();
        firebaseuser = firebaseauth.getCurrentUser();

        //Poner de titulo de actionbar el nombre de usuario

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setTitle("Home");
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        firebaseauth = FirebaseAuth.getInstance();
        firebaseuser = firebaseauth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        btn_cerrar = findViewById(R.id.UserHome_btn_cerrarSesion);
        tv_nombreUser = findViewById(R.id.UserHome_tv_user);

        btn_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    @Override
    protected void onStart() {
        verificarInicioSesion();
        getDataUser();
        super.onStart();
    }

    private void verificarInicioSesion() {
        if (firebaseuser != null) {
            Toast_Manager.showToast(this, "Se ha iniciado sesión correctamente");
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void signOut() {
        firebaseauth.signOut();
        Toast_Manager.showToast(this, "Has cerrado sesion");
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /* Este método obtiene el nombre y apellidos del usuario actual y los asigna al TextView */
    private void getDataUser() {
        String uid = firebaseuser.getUid();

        firestore.collection("Usuarios").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    nombreUsuario = documentSnapshot.getString("nombre");
                    apellidoUsuario = documentSnapshot.getString("apellido");
                    nombreCompleto = " " + nombreUsuario + " " + apellidoUsuario + " ";
                    tv_nombreUser.setText(nombreCompleto);
                }
            }
        });
    }
}