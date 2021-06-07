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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rmpsoft.looking.R;
import com.rmpsoft.looking.model.Chat;
import com.rmpsoft.looking.utils.Toast_Manager;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class User_EditPerfil extends AppCompatActivity {

    EditText et_nombre, et_apellido, et_edad;
    ImageButton btn_perfil, btn_nombre, btn_nombreOK, btn_apellido, btn_apellidoOK, btn_edad, btn_edadOK;
    ImageView image_perfil;
    Button btn_descartar, btn_confirmar;

    FirebaseAuth firebaseauth;
    FirebaseUser firebaseuser;
    FirebaseFirestore firestore;
    StorageReference storage;
    ProgressDialog progressDialog;

    Bitmap bitmap = null;
    Uri uploaduri;

    String nombre, apellido, edad, uid, uriImagePerfil;

    ArrayList<String> idChatPath = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_perfil);

        firebaseauth = FirebaseAuth.getInstance();
        firebaseuser = firebaseauth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        uid = firebaseuser.getUid();
        storage = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(User_EditPerfil.this);

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle(" ");
        actionbar.setIcon(R.drawable.ic_actionbar_logo);

        et_nombre = findViewById(R.id.UserEdit_et_nombre);
        et_apellido = findViewById(R.id.UserEdit_et_apellido);
        et_edad = findViewById(R.id.UserEdit_et_edad);
        image_perfil = findViewById(R.id.UserEdit_image_perfil);

        btn_perfil = findViewById(R.id.UserEdit_btn_edit_IP);
        btn_nombre = findViewById(R.id.UserEdit_btn_edit_NOM);
        btn_nombreOK = findViewById(R.id.UserEdit_btn_OK_NOM);
        btn_apellido = findViewById(R.id.UserEdit_btn_edit_APE);
        btn_apellidoOK = findViewById(R.id.UserEdit_btn_OK_APE);
        btn_edad = findViewById(R.id.UserEdit_btn_edit_ED);
        btn_edadOK = findViewById(R.id.UserEdit_btn_OK_ED);
        btn_descartar = findViewById(R.id.UserEdit_btn_descartar);
        btn_confirmar = findViewById(R.id.UserEdit_btn_confirmar);

        getData();

        btn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(User_EditPerfil.this);
            }
        });

        btn_nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_nombre.setEnabled(true);
                btn_nombre.setVisibility(View.GONE);
                btn_nombreOK.setVisibility(View.VISIBLE);
            }
        });

        btn_nombreOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_nombre.setEnabled(false);
                btn_nombreOK.setVisibility(View.GONE);
                btn_nombre.setVisibility(View.VISIBLE);
            }
        });

        btn_apellido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_apellido.setEnabled(true);
                btn_apellido.setVisibility(View.GONE);
                btn_apellidoOK.setVisibility(View.VISIBLE);
            }
        });

        btn_apellidoOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_apellido.setEnabled(false);
                btn_apellidoOK.setVisibility(View.GONE);
                btn_apellido.setVisibility(View.VISIBLE);
            }
        });

        btn_edad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_edad.setEnabled(true);
                btn_edad.setVisibility(View.GONE);
                btn_edadOK.setVisibility(View.VISIBLE);
            }
        });

        btn_edadOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_edad.setEnabled(false);
                btn_edadOK.setVisibility(View.GONE);
                btn_edad.setVisibility(View.VISIBLE);
            }
        });

        btn_descartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_EditPerfil.this, User_Home.class));
            }
        });

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
                startActivity(new Intent(User_EditPerfil.this, User_Home.class));
                finish();
            }
        });

    }
    /* Método para la subida de la imagen */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageuri = CropImage.getPickImageResultUri(User_EditPerfil.this, data);

            /* Recortando imagen */
            CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON)
                    .setRequestedSize(480, 480)
                    .setAspectRatio(1,1).start(User_EditPerfil.this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK) {
                Uri resulturi = result.getUri();

                File url = new File(resulturi.getPath());

                /* Comprimiendo imagen */
                try {
                    bitmap = new Compressor(User_EditPerfil.this).setMaxWidth(480).setMaxHeight(480).setQuality(90).compressToBitmap(url);
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
                        uploaduri = task.getResult();

                        Map<String, Object> values = new HashMap<>();
                        values.put("image", uploaduri.toString());

                        firestore.collection("Usuarios").document(uid).update(values);

                        progressDialog.dismiss();
                        Toast_Manager.showToast(User_EditPerfil.this, "Imagen subida con éxito");

                        updateImageData();
                        getData();

                    }
                });

            }
        }
    }

    /* Actualiza el path de la imagen en los chats correspondientes */
    public void updateImageData() {

        Map<String, Object> chatValue = new HashMap<>();
        chatValue.put("userImage", uploaduri.toString());

        firestore.collection("Chat").whereEqualTo("idUser", uid).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<DocumentSnapshot> listDocuments = querySnapshot.getDocuments();
                        if(!listDocuments.isEmpty()) {
                            for (DocumentSnapshot document : listDocuments) {
                                String dato = document.toObject(Chat.class).getChatPath();
                                idChatPath.add(dato);
                            }
                            for (String id : idChatPath) firestore.collection("Chat").document(id).update(chatValue);
                        }
                    }
                });
             }

    /* Método que actualiza los datos introducidos por el usuario */
    public void updateData() {
        progressDialog.setTitle("Actualizando datos...");
        progressDialog.setMessage("Por favor, espere. Esta \nacción puede llevar unos segundos");
        progressDialog.show();

        nombre = et_nombre.getText().toString();
        apellido = et_apellido.getText().toString();
        edad = et_edad.getText().toString();

        Map<String, Object> values = new HashMap<>();

        values.put("nombre", nombre);
        values.put("apellido", apellido);
        values.put("edad", edad);

        firestore.collection("Usuarios").document(uid).update(values);
        progressDialog.dismiss();
        Toast_Manager.showToast(User_EditPerfil.this, "Datos actualizados con éxito");
    }

    /* Método que obtiene los datos del usuario y los asigna a los EditText */
    public void getData () {
        String uid = firebaseuser.getUid();
        firestore.collection("Usuarios").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    nombre = documentSnapshot.getString("nombre");
                    apellido = documentSnapshot.getString("apellido");
                    edad = documentSnapshot.getString("edad");
                    uriImagePerfil = documentSnapshot.getString("image");

                    try {
                        Picasso.get().load(uriImagePerfil).placeholder(R.drawable.ic_perfil_user).into(image_perfil);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_perfil_user).into(image_perfil);
                    }

                    et_nombre.setEnabled(false);
                    et_nombre.setText(nombre);
                    et_apellido.setEnabled(false);
                    et_apellido.setText(apellido);
                    et_edad.setEnabled(false);
                    et_edad.setText(edad);
                }
            }
        });
    }

}