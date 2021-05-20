package com.rmpsoft.looking.activitys;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.rmpsoft.looking.LoginActivity;
import com.rmpsoft.looking.R;
import com.rmpsoft.looking.adapter.AnunciosUserAdapter;
import com.rmpsoft.looking.model.Anuncio;
import com.rmpsoft.looking.utils.Toast_Manager;

public class User_Home extends AppCompatActivity {

    FirebaseAuth firebaseauth;
    FirebaseUser firebaseuser;
    FirebaseFirestore firestore;

    TextView tv_nombreUser;
    FloatingActionButton fab_filter;

    String nombreUsuario;
    String apellidoUsuario;
    String nombreCompleto;
    Boolean sesionIniciada = false;

    RecyclerView rv_Anuncios;
    AnunciosUserAdapter anunciosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__home);

        firebaseauth = FirebaseAuth.getInstance();
        firebaseuser = firebaseauth.getCurrentUser();

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle(" ");
        actionbar.setIcon(R.drawable.ic_logo_actionbar);

        firebaseauth = FirebaseAuth.getInstance();
        firebaseuser = firebaseauth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        tv_nombreUser = findViewById(R.id.UserHome_tv_user);
        fab_filter = findViewById(R.id.UserHome_fab_filter);
        rv_Anuncios = findViewById(R.id.UserHome_rv_anuncios);
        rv_Anuncios.setLayoutManager(new LinearLayoutManager(this));

        fab_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_Home.this, User_FiltroBusqueda.class));
            }
        });

        getAnuncios();
    }


    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_userhome, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.UserHome_ic_exit) {
            signOut();
        }

        if (id == R.id.UserHome_ic_edit) {
            startActivity(new Intent(User_Home.this, User_EditarPerfil.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        anunciosAdapter.startListening();
        verificarInicioSesion();
        getDataUser();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        anunciosAdapter.stopListening();
    }

    /* Verifica que un usuario ha iniciado sesión, de lo contrario, cierra la activity */
    private void verificarInicioSesion() {
        if (sesionIniciada) {

        } else {
            if (firebaseuser != null) {
                Toast_Manager.showToast(this, "Se ha iniciado sesión correctamente");
                sesionIniciada = true;
            } else {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        }
    }

    /* Método que cierra la sesión actual */
    private void signOut() {
        firebaseauth.signOut();
        Toast_Manager.showToast(this, "Has cerrado sesion");
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /* Este método obtiene los datos de los anuncios */
    private void getAnuncios() {
        Query query = firestore.collection("Anuncios");

        FirestoreRecyclerOptions<Anuncio> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Anuncio>()
                .setQuery(query, Anuncio.class).build();

        anunciosAdapter = new AnunciosUserAdapter(firestoreRecyclerOptions);
        anunciosAdapter.notifyDataSetChanged();
        rv_Anuncios.setAdapter(anunciosAdapter);

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