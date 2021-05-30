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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.rmpsoft.looking.adapter.AnunciosAdapter;
import com.rmpsoft.looking.model.Anuncio;
import com.rmpsoft.looking.utils.Toast_Manager;
import com.squareup.picasso.Picasso;

public class Equipo_Home extends AppCompatActivity {

    FirebaseAuth firebaseauth;
    FirebaseUser firebaseuser;
    FirebaseFirestore firestore;

    TextView tv_nombreEquipo, tv_municipioEquipo, ll_tv_contacto, ll_tv_posicion, ll_tv_descripcion;
    FloatingActionButton fab_add;
    ImageButton btn_ll_eliminar, btn_ll_cerrar;
    ImageView image_perfil;

    String nombreEquipo;
    String municipioEquipo;
    String uriImagePerfil;
    Boolean sesionIniciada = false;

    RecyclerView rv_Anuncios;
    AnunciosAdapter anunciosAdapter;

    LinearLayout ll_anuncioSeleccionado;
    Anuncio anuncioSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipo__home);

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle(" ");
        actionbar.setIcon(R.drawable.ic_actionbar_logo);

        firebaseauth = FirebaseAuth.getInstance();
        firebaseuser = firebaseauth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        tv_nombreEquipo = findViewById(R.id.EquipoHome_tv_user);
        tv_municipioEquipo = findViewById(R.id.EquipoHome_tv_municipio);
        image_perfil = findViewById(R.id.EquipoHome_image_perfil);
        fab_add = findViewById(R.id.EquipoHome_fab_add);

        rv_Anuncios = findViewById(R.id.EquipoHome_rv_anuncios);
        rv_Anuncios.setLayoutManager(new LinearLayoutManager(this));

        ll_anuncioSeleccionado = findViewById(R.id.EquipoHome_ll_anuncioSelected);
        ll_tv_contacto = findViewById(R.id.EquipoHome_ll_tv_contacto);
        ll_tv_posicion = findViewById(R.id.EquipoHome_ll_tv_posicion);
        ll_tv_descripcion = findViewById(R.id.EquipoHome_ll_tv_descripcion);
        btn_ll_eliminar = findViewById(R.id.EquipoHome_ll_btn_eliminar);
        btn_ll_cerrar = findViewById(R.id.EquipoHome_ll_btn_cerrar);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Equipo_Home.this, Equipo_PonerAnuncio.class));
            }
        });

        btn_ll_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_anuncioSeleccionado.setVisibility(View.GONE);
                anuncioSeleccionado = null; 
            }
        });

        getAnuncios();

    }

    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equipohome, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.EquipoHome_ic_exit) {
            signOut();
        }

        if (id == R.id.EquipoHome_ic_edit) {
            startActivity(new Intent(Equipo_Home.this, Equipo_EditarPerfil.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        anunciosAdapter.startListening();
        verificarInicioSesion();
        loadDataUser();
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
        sesionIniciada = false;
        firebaseauth.signOut();
        Toast_Manager.showToast(this, "Has cerrado sesion");
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /* Este método actualiza los datos de un anuncio */
    private void updateAnuncio() {

    }

    /* Este método obtiene los datos de los anuncios del equipo actual*/
    private void getAnuncios() {
        String uid = firebaseuser.getUid();
        Query query = firestore.collection("Anuncios").whereEqualTo("uidcontacto", uid );

        FirestoreRecyclerOptions<Anuncio> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Anuncio>()
                .setQuery(query, Anuncio.class).build();

        anunciosAdapter = new AnunciosAdapter(firestoreRecyclerOptions);
        anunciosAdapter.notifyDataSetChanged();

        rv_Anuncios.setAdapter(anunciosAdapter);

        anunciosAdapter.setOnItemClickListener(new AnunciosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                anuncioSeleccionado = null;
                anuncioSeleccionado = documentSnapshot.toObject(Anuncio.class);

                ll_anuncioSeleccionado.setVisibility(View.VISIBLE);

                String id = documentSnapshot.getId();
                String contacto = anuncioSeleccionado.getContacto();
                String posicion = anuncioSeleccionado.getPosicion();
                String descripcion = anuncioSeleccionado.getDescripcion();

                ll_tv_contacto.setText(contacto);
                ll_tv_posicion.setText(posicion);
                ll_tv_descripcion.setText(descripcion);

                btn_ll_eliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firestore.collection("Anuncios").document(id).delete();
                        ll_anuncioSeleccionado.setVisibility(View.GONE);
                        anuncioSeleccionado = null;
                    }
                });
            }
        });

    }

    /* Este método obtiene el nombre del equipo actual y lo asigna al TextView */
    private void loadDataUser() {
        String uid = firebaseuser.getUid();
        firestore.collection("Equipos").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    nombreEquipo = documentSnapshot.getString("equipo");
                    municipioEquipo = documentSnapshot.getString("municipio");
                    uriImagePerfil = documentSnapshot.getString("image");
                    tv_nombreEquipo.setText(nombreEquipo);
                    tv_municipioEquipo.setText(municipioEquipo);

                    try {
                        Picasso.get().load(uriImagePerfil).placeholder(R.drawable.ic_perfil_equipo).into(image_perfil);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_perfil_equipo).into(image_perfil);
                    }
                }
            }
        });
    }
}