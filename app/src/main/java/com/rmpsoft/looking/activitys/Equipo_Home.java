package com.rmpsoft.looking.activitys;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rmpsoft.looking.LoginActivity;
import com.rmpsoft.looking.R;
import com.rmpsoft.looking.utils.Toast_Manager;

public class Equipo_Home extends AppCompatActivity {

    FirebaseAuth firebaseauth;
    FirebaseUser firebaseuser;
    FirebaseFirestore firestore;

    TextView tv_nombreEquipo;
    String nombreEquipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipo__home);

        firebaseauth = FirebaseAuth.getInstance();
        firebaseuser = firebaseauth.getCurrentUser();

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setTitle("Home");

        firebaseauth = FirebaseAuth.getInstance();
        firebaseuser = firebaseauth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        tv_nombreEquipo = findViewById(R.id.EquipoHome_tv_user);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equipohome, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.EquipoHome_menu_cerrarSesion) {
            signOut();
        }
        if (id == R.id.EquipoHome_menu_editarPerfil) {
            startActivity(new Intent(Equipo_Home.this, Equipo_EditarPerfil.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        verificarInicioSesion();
        getDataUser();
        super.onStart();
    }

    /* Verifica que un usuario ha iniciado sesión, de lo contrario, cierra la activity */
    private void verificarInicioSesion() {
        if (firebaseuser != null) {
            Toast_Manager.showToast(this, "Se ha iniciado sesión correctamente");
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    /* Método que cierra la sesión actual */
    private void signOut() {
        firebaseauth.signOut();
        Toast_Manager.showToast(this, "Has cerrado sesion");
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /* Este método obtiene el nombre del equipo actual y lo asigna al TextView */
    private void getDataUser() {
        String uid = firebaseuser.getUid();

        firestore.collection("Equipos").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    nombreEquipo = documentSnapshot.getString("equipo");
                    tv_nombreEquipo.setText(nombreEquipo);
                }
            }
        });
    }
}