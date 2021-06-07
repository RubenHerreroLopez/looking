package com.rmpsoft.looking.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.rmpsoft.looking.ChatActivity;
import com.rmpsoft.looking.ChatListActivity;
import com.rmpsoft.looking.LoginActivity;
import com.rmpsoft.looking.R;
import com.rmpsoft.looking.adapter.AdvicesUserAdapter;
import com.rmpsoft.looking.model.Advice;
import com.rmpsoft.looking.model.Chat;
import com.rmpsoft.looking.utils.Toast_Manager;
import com.squareup.picasso.Picasso;

import java.text.Normalizer;

public class User_Home extends AppCompatActivity {

    FirebaseAuth firebaseauth;
    FirebaseUser firebaseuser;
    FirebaseFirestore firestore;

    TextView tv_nombreUser, ll_tv_contacto, ll_tv_equipo, ll_tv_descripcion;
    ImageButton btn_ll_chat, btn_ll_cerrar;
    FloatingActionButton fab_filter;
    ImageView image_perfil;

    String idUser, firstUserName, lastUserName, userName, uriImagePerfil, idTeam, teamName, uriTeamPerfil;
    String TIPO_USUARIO = "usuario";

    Boolean sesionIniciada = false;

    RecyclerView rv_Anuncios;
    AdvicesUserAdapter anunciosAdapter;

    LinearLayout ll_anuncioSeleccionado;
    Advice adviceSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle(" ");
        actionbar.setIcon(R.drawable.ic_actionbar_logo);

        firebaseauth = FirebaseAuth.getInstance();
        firebaseuser = firebaseauth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        idUser = firebaseuser.getUid();

        tv_nombreUser = findViewById(R.id.UserHome_tv_user);
        fab_filter = findViewById(R.id.UserHome_fab_filter);
        image_perfil = findViewById(R.id.UserHome_image_perfil);
        rv_Anuncios = findViewById(R.id.UserHome_rv_anuncios);
        rv_Anuncios.setLayoutManager(new LinearLayoutManager(this));

        ll_anuncioSeleccionado = findViewById(R.id.UserHome_ll_anuncioSelected);
        ll_tv_contacto = findViewById(R.id.UserHome_ll_tv_contacto);
        ll_tv_equipo = findViewById(R.id.UserHome_ll_tv_equipo);
        ll_tv_descripcion = findViewById(R.id.UserHome_ll_tv_descripcion);
        btn_ll_chat = findViewById(R.id.UserHome_ll_btn_chat);
        btn_ll_cerrar = findViewById(R.id.UserHome_ll_btn_cerrar);

        fab_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrarBusqueda();
            }
        });

        btn_ll_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chatPath = idUser + "to" + idTeam;
                createChat(idUser, idTeam, firstUserName, teamName, uriImagePerfil, uriTeamPerfil, chatPath);

                Intent intent = new Intent(User_Home.this, ChatActivity.class);
                intent.putExtra("idSender", idUser);
                intent.putExtra("idReceiver", idTeam);
                intent.putExtra("chatPath", chatPath);
                intent.putExtra("receiverName", teamName);
                startActivity(intent);
            }
        });

        btn_ll_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_anuncioSeleccionado.setVisibility(View.GONE);
                adviceSelected = null;
            }
        });

        verificarInicioSesion();
        loadDataUser();
        getAllAdvices();
    }

    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_home, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.UserHome_ic_exit) {
            signOut();
        }

        if (id == R.id.UserHome_ic_edit) {
            startActivity(new Intent(User_Home.this, User_EditPerfil.class));
        }

        if (id == R.id.UserHome_ic_chat) {
            Intent intent = new Intent(User_Home.this, ChatListActivity.class);
            intent.putExtra("tipoUsuario", TIPO_USUARIO);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        anunciosAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        anunciosAdapter.stopListening();
    }

    /* Método que crea un nuevo chat al pulsar en el boton de chat de 'll_anuncioSeleccionado' */
    public void createChat(String idUser, String idTeam, String userName, String teamName, String userImage, String teamImage, String chatPath) {

        Chat newChat = new Chat(idUser, idTeam, userName, teamName, userImage, teamImage, chatPath);

        firestore.collection("Chat").document(chatPath).set(newChat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("request", "Chat create succesfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("request", "Chat doesn't create");
            }
        });
    }

    /* Verifica que un usuario ha iniciado sesión, de lo contrario, cierra la activity */
    private void verificarInicioSesion() {
        if (!sesionIniciada) {
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

    public void filtrarBusqueda() {

        AlertDialog.Builder builder = new AlertDialog.Builder(User_Home.this);
        View view = getLayoutInflater().inflate(R.layout.layout_advice_filter, null);

        EditText et_deporte = view.findViewById(R.id.UserHome_ll_filter_et_deporte);
        EditText et_municipio = view.findViewById(R.id.UserHome_ll_filter_et_municipio);
        EditText et_posicion = view.findViewById(R.id.UserHome_ll_filter_et_posicion);
        ImageButton btn_cerrar = view.findViewById(R.id.UserHome_ll_filter_btn_cerrar);
        Button btn_aplicar = view.findViewById(R.id.UserHome_ll_filter_btn_aplicar);

        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        btn_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_aplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean bool_deporte = true;
                boolean bool_municipio = true;
                boolean bool_posicion = true;

                String deporte = et_deporte.getText().toString();
                String municipio = et_municipio.getText().toString();
                String posicion = et_posicion.getText().toString();

                if (deporte.isEmpty()) {
                    bool_deporte = false;
                }
                if (municipio.isEmpty()) {
                    bool_municipio = false;
                }
                if (posicion.isEmpty()) {
                    bool_posicion = false;
                }

                if (!bool_deporte && !bool_municipio && !bool_posicion) {
                    getAllAdvices();

                } else if (bool_deporte && !bool_municipio && !bool_posicion) {
                    deporte = formatoString(et_deporte.getText().toString());
                    rv_Anuncios.removeAllViews();
                    getAdvicesBySport(deporte);
                    dialog.dismiss();
                } else if (!bool_deporte && bool_municipio && !bool_posicion) {
                    municipio = formatoString(et_municipio.getText().toString());
                    getAdvicesByCity(municipio);
                    dialog.dismiss();
                } else if (!bool_deporte && !bool_municipio && bool_posicion) {
                    posicion = formatoString(et_posicion.getText().toString());
                    getAdvicesByPosition(posicion);
                    dialog.dismiss();
                } else if (bool_deporte && bool_municipio && !bool_posicion) {
                    deporte = formatoString(et_deporte.getText().toString());
                    municipio = formatoString(et_municipio.getText().toString());
                    getAdvicesBySportByCity(deporte, municipio);
                    dialog.dismiss();
                } else if (bool_deporte && !bool_municipio && bool_posicion) {
                    deporte = formatoString(et_deporte.getText().toString());
                    posicion = formatoString(et_posicion.getText().toString());
                    getAdvicesBySportByPosition(deporte, posicion);
                    dialog.dismiss();
                } else if (!bool_deporte && bool_municipio && bool_posicion) {
                    municipio = formatoString(et_municipio.getText().toString());
                    posicion = formatoString(et_posicion.getText().toString());
                    getAdvicesByCityByPosition(municipio, posicion);
                    dialog.dismiss();
                } else if (bool_deporte && bool_municipio && bool_posicion) {
                    deporte = formatoString(et_deporte.getText().toString());
                    municipio = formatoString(et_municipio.getText().toString());
                    posicion = formatoString(et_posicion.getText().toString());
                    getAdvicesBySportByCityByPosition(deporte, municipio, posicion);
                    dialog.dismiss();
                }

            }
        });
    }

    /* Este método obtiene los datos de todos los anuncios y los muestra en el RecyclerView*/
    private void getAllAdvices() {

        Query query = firestore.collection("Anuncios");

        FirestoreRecyclerOptions<Advice> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Advice>()
                .setQuery(query, Advice.class).build();

        anunciosAdapter = new AdvicesUserAdapter(firestoreRecyclerOptions);
        anunciosAdapter.notifyDataSetChanged();

        rv_Anuncios.setAdapter(anunciosAdapter);

        anunciosAdapter.setOnItemClickListener(new AdvicesUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                adviceSelected = null;
                adviceSelected = documentSnapshot.toObject(Advice.class);

                ll_anuncioSeleccionado.setVisibility(View.VISIBLE);

                String id = documentSnapshot.getId();
                idTeam = adviceSelected.getUidcontacto();
                String contacto = adviceSelected.getContacto();
                teamName = adviceSelected.getEquipo();
                uriTeamPerfil = adviceSelected.getImagen();
                String descripcion = adviceSelected.getDescripcion();

                ll_tv_contacto.setText(contacto);
                ll_tv_equipo.setText(teamName);
                ll_tv_descripcion.setText(descripcion);
            }
        });

    }

    /* Los siguientes métodos obtienen los datos de los anuncios filtrados y los muestra en el RecyclerView */
    private void getAdvicesBySport(String deporte) {

        Query query = firestore.collection("Anuncios").whereEqualTo("deporte", deporte);

        FirestoreRecyclerOptions<Advice> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Advice>()
                .setQuery(query, Advice.class).build();

        anunciosAdapter = new AdvicesUserAdapter(firestoreRecyclerOptions);
        anunciosAdapter.notifyDataSetChanged();
        rv_Anuncios.setAdapter(anunciosAdapter);

        anunciosAdapter.setOnItemClickListener(new AdvicesUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                adviceSelected = null;
                adviceSelected = documentSnapshot.toObject(Advice.class);

                ll_anuncioSeleccionado.setVisibility(View.VISIBLE);

                String id = documentSnapshot.getId();
                idTeam = adviceSelected.getUidcontacto();
                String contacto = adviceSelected.getContacto();
                teamName = adviceSelected.getEquipo();
                uriTeamPerfil = adviceSelected.getImagen();
                String descripcion = adviceSelected.getDescripcion();

                ll_tv_contacto.setText(contacto);
                ll_tv_equipo.setText(teamName);
                ll_tv_descripcion.setText(descripcion);
            }
        });
    }

    private void getAdvicesByCity(String municipio) {

        Query query = firestore.collection("Anuncios").whereEqualTo("municipio", municipio);
        Log.d("respones", "Message 2");
        FirestoreRecyclerOptions<Advice> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Advice>()
                .setQuery(query, Advice.class).build();

        anunciosAdapter = new AdvicesUserAdapter(firestoreRecyclerOptions);
        anunciosAdapter.notifyDataSetChanged();
        rv_Anuncios.setAdapter(anunciosAdapter);

        anunciosAdapter.setOnItemClickListener(new AdvicesUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                adviceSelected = null;
                adviceSelected = documentSnapshot.toObject(Advice.class);

                ll_anuncioSeleccionado.setVisibility(View.VISIBLE);

                String id = documentSnapshot.getId();
                idTeam = adviceSelected.getUidcontacto();
                String contacto = adviceSelected.getContacto();
                teamName = adviceSelected.getEquipo();
                uriTeamPerfil = adviceSelected.getImagen();
                String descripcion = adviceSelected.getDescripcion();

                ll_tv_contacto.setText(contacto);
                ll_tv_equipo.setText(teamName);
                ll_tv_descripcion.setText(descripcion);
            }
        });
    }

    private void getAdvicesByPosition(String posicion) {

        Query query = firestore.collection("Anuncios").whereEqualTo("posicion", posicion);
        FirestoreRecyclerOptions<Advice> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Advice>()
                .setQuery(query, Advice.class).build();

        anunciosAdapter = new AdvicesUserAdapter(firestoreRecyclerOptions);
        anunciosAdapter.notifyDataSetChanged();
        rv_Anuncios.setAdapter(anunciosAdapter);

        anunciosAdapter.setOnItemClickListener(new AdvicesUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                adviceSelected = null;
                adviceSelected = documentSnapshot.toObject(Advice.class);

                ll_anuncioSeleccionado.setVisibility(View.VISIBLE);

                String id = documentSnapshot.getId();
                idTeam = adviceSelected.getUidcontacto();
                String contacto = adviceSelected.getContacto();
                teamName = adviceSelected.getEquipo();
                uriTeamPerfil = adviceSelected.getImagen();
                String descripcion = adviceSelected.getDescripcion();

                ll_tv_contacto.setText(contacto);
                ll_tv_equipo.setText(teamName);
                ll_tv_descripcion.setText(descripcion);
            }
        });
    }

    private void getAdvicesBySportByCity (String deporte, String municipio) {

        Query query = firestore.collection("Anuncios").whereEqualTo("deporte", deporte).whereEqualTo("municipio", municipio);
        FirestoreRecyclerOptions<Advice> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Advice>()
                .setQuery(query, Advice.class).build();

        anunciosAdapter = new AdvicesUserAdapter(firestoreRecyclerOptions);
        anunciosAdapter.notifyDataSetChanged();
        rv_Anuncios.setAdapter(anunciosAdapter);

        anunciosAdapter.setOnItemClickListener(new AdvicesUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                adviceSelected = null;
                adviceSelected = documentSnapshot.toObject(Advice.class);

                ll_anuncioSeleccionado.setVisibility(View.VISIBLE);

                String id = documentSnapshot.getId();
                idTeam = adviceSelected.getUidcontacto();
                String contacto = adviceSelected.getContacto();
                teamName = adviceSelected.getEquipo();
                uriTeamPerfil = adviceSelected.getImagen();
                String descripcion = adviceSelected.getDescripcion();

                ll_tv_contacto.setText(contacto);
                ll_tv_equipo.setText(teamName);
                ll_tv_descripcion.setText(descripcion);
            }
        });
    }

    private void getAdvicesBySportByPosition (String deporte, String posicion) {

        Query query = firestore.collection("Anuncios").whereEqualTo("deporte", deporte).whereEqualTo("posicion", posicion);
        FirestoreRecyclerOptions<Advice> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Advice>()
                .setQuery(query, Advice.class).build();

        anunciosAdapter = new AdvicesUserAdapter(firestoreRecyclerOptions);
        anunciosAdapter.notifyDataSetChanged();
        rv_Anuncios.setAdapter(anunciosAdapter);

        anunciosAdapter.setOnItemClickListener(new AdvicesUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                adviceSelected = null;
                adviceSelected = documentSnapshot.toObject(Advice.class);

                ll_anuncioSeleccionado.setVisibility(View.VISIBLE);

                String id = documentSnapshot.getId();
                idTeam = adviceSelected.getUidcontacto();
                String contacto = adviceSelected.getContacto();
                teamName = adviceSelected.getEquipo();
                uriTeamPerfil = adviceSelected.getImagen();
                String descripcion = adviceSelected.getDescripcion();

                ll_tv_contacto.setText(contacto);
                ll_tv_equipo.setText(teamName);
                ll_tv_descripcion.setText(descripcion);
            }
        });
    }

    private void getAdvicesByCityByPosition (String municipio, String posicion) {

        Query query = firestore.collection("Anuncios").whereEqualTo("municipio", municipio).whereEqualTo("posicion", posicion);
        FirestoreRecyclerOptions<Advice> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Advice>()
                .setQuery(query, Advice.class).build();

        anunciosAdapter = new AdvicesUserAdapter(firestoreRecyclerOptions);
        anunciosAdapter.notifyDataSetChanged();
        rv_Anuncios.setAdapter(anunciosAdapter);

        anunciosAdapter.setOnItemClickListener(new AdvicesUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                adviceSelected = null;
                adviceSelected = documentSnapshot.toObject(Advice.class);

                ll_anuncioSeleccionado.setVisibility(View.VISIBLE);

                String id = documentSnapshot.getId();
                idTeam = adviceSelected.getUidcontacto();
                String contacto = adviceSelected.getContacto();
                teamName = adviceSelected.getEquipo();
                uriTeamPerfil = adviceSelected.getImagen();
                String descripcion = adviceSelected.getDescripcion();

                ll_tv_contacto.setText(contacto);
                ll_tv_equipo.setText(teamName);
                ll_tv_descripcion.setText(descripcion);
            }
        });
    }

    private void getAdvicesBySportByCityByPosition (String deporte, String municipio, String posicion) {

        Query query = firestore.collection("Anuncios").whereEqualTo("deporte", deporte).whereEqualTo("municipio", municipio)
                .whereEqualTo("posicion", posicion);
        FirestoreRecyclerOptions<Advice> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Advice>()
                .setQuery(query, Advice.class).build();

        anunciosAdapter = new AdvicesUserAdapter(firestoreRecyclerOptions);
        anunciosAdapter.notifyDataSetChanged();
        rv_Anuncios.setAdapter(anunciosAdapter);

        anunciosAdapter.setOnItemClickListener(new AdvicesUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                adviceSelected = null;
                adviceSelected = documentSnapshot.toObject(Advice.class);

                ll_anuncioSeleccionado.setVisibility(View.VISIBLE);

                String id = documentSnapshot.getId();
                idTeam = adviceSelected.getUidcontacto();
                String contacto = adviceSelected.getContacto();
                teamName = adviceSelected.getEquipo();
                uriTeamPerfil = adviceSelected.getImagen();
                String descripcion = adviceSelected.getDescripcion();

                ll_tv_contacto.setText(contacto);
                ll_tv_equipo.setText(teamName);
                ll_tv_descripcion.setText(descripcion);
            }
        });
    }

    /* Este método obtiene el nombre y apellidos del usuario actual y los asigna al TextView */
    private void loadDataUser() {
        String uid = firebaseuser.getUid();

        firestore.collection("Usuarios").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    firstUserName = documentSnapshot.getString("nombre");
                    lastUserName = documentSnapshot.getString("apellido");
                    uriImagePerfil = documentSnapshot.getString("image");
                    userName = " " + firstUserName + " " + lastUserName + " ";
                    tv_nombreUser.setText(userName);

                    try {
                        Picasso.get().load(uriImagePerfil).placeholder(R.drawable.ic_perfil_user).into(image_perfil);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_perfil_user).into(image_perfil);
                    }
                } else {
                    Toast_Manager.showToast(User_Home.this, "No tienes cuenta de este tipo");
                    startActivity(new Intent(User_Home.this, LoginActivity.class));
                    finish();
                }
            }
        });
    }

    /* Este método transforma los String introducidos por el usuario en un formato estandar en la BBDD
       Elimina las tíldes y deja en mayúscula solo la primera letra de toda la cedana */
    private static String formatoString (String textoUsuario) {

        String textoMinusculas = textoUsuario.toLowerCase();
        char[] arr = textoMinusculas.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        String textoMayuscula = String.valueOf(arr);
        textoMayuscula = Normalizer.normalize(textoMayuscula, Normalizer.Form.NFD);
        return textoMayuscula.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }
}