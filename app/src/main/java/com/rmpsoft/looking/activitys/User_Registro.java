package com.rmpsoft.looking.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rmpsoft.looking.LoginActivity;
import com.rmpsoft.looking.Persona;
import com.rmpsoft.looking.R;
import com.rmpsoft.looking.utils.Toast_Manager;

import java.util.HashMap;

public class User_Registro extends AppCompatActivity {

    EditText et_correo, et_nombre, et_apellido, et_user, et_edad, et_pass;
    RadioButton rb_hombre, rb_mujer;
    Button btn_registo;
    FirebaseAuth firebaseauth;
    FirebaseFirestore firestore;
    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__registro);

        et_correo = findViewById(R.id.UsuarioRegistro_et_correo);
        et_nombre = findViewById(R.id.UsuarioRegistro_et_nombre);
        et_apellido = findViewById(R.id.UsuarioRegistro_et_apellidos);
        et_edad = findViewById(R.id.UsuarioRegistro_et_edad);
        et_user = findViewById(R.id.UsuarioRegistro_et_usuario);
        et_pass = findViewById(R.id.UsuarioRegistro_et_pass);
        rb_hombre = findViewById(R.id.UsuarioRegistro_rb_hombre);
        rb_mujer = findViewById(R.id.UsuarioRegistro_rb_mujer);
        btn_registo = findViewById(R.id.UsuarioRegistro_btn_registro);

        firebaseauth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressdialog = new ProgressDialog(User_Registro.this);

        btn_registo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = et_correo.getText().toString();
                String pass = et_pass.getText().toString();
                String nombre = et_nombre.getText().toString();
                String apellido = et_apellido.getText().toString();
                String edad = et_edad.getText().toString();
                String usuario = et_user.getText().toString();

                /* Validamos el formato del correo y la contraseña así como que los campos están rellenados*/
                if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                    et_correo.setError("Introduzca un correo válido");
                    et_correo.setFocusable(true);
                } else if (pass.length()<6) {
                    et_pass.setError("La contraseña debe ser mayor a seis caracteres");
                    et_pass.setFocusable(true);
                } else if (nombre.isEmpty()) {
                    et_nombre.setError("El campo es obligatorio");
                    et_nombre.setFocusable(true);
                } else if (apellido.isEmpty()) {
                    et_apellido.setError("El campo es obligatorio");
                    et_apellido.setFocusable(true);
                } else if (edad.isEmpty()) {
                    et_edad.setError("El campo es obligatorio");
                    et_edad.setFocusable(true);
                } else if (usuario.isEmpty()) {
                    et_user.setError("El campo es obligatorio");
                    et_user.setFocusable(true);
                }else {
                    registrar(correo, pass);
                }
            }
        });
    }

    private void registrar (String correo, String pass) {

        progressdialog.setCancelable(false);
        progressdialog.show();
        firebaseauth.createUserWithEmailAndPassword(correo, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    progressdialog.dismiss();
                    FirebaseUser firebaseuser = firebaseauth.getCurrentUser();
                    String sexo;

                    assert firebaseuser != null;
                    String uid = firebaseuser.getUid();
                    String correo = et_correo.getText().toString();
                    String pass = et_pass.getText().toString();
                    String nombre = et_nombre.getText().toString();
                    String apellido = et_apellido.getText().toString();
                    String edad = et_edad.getText().toString();
                    String usuario = et_user.getText().toString();

                    if (rb_hombre.isChecked()) {
                        sexo = rb_hombre.getText().toString();
                    } else {
                        sexo = rb_mujer.getText().toString();
                    }

                    HashMap<Object, String> datosUser = new HashMap<>();

                    datosUser.put("uid", uid);
                    datosUser.put("usuario", usuario);
                    datosUser.put("correo", correo);
                    datosUser.put("pass", pass);
                    datosUser.put("nombre", nombre);
                    datosUser.put("apellido", apellido);
                    datosUser.put("edad", edad);
                    datosUser.put("sexo", sexo);

                    Persona p = new Persona (uid, nombre, apellido);

                    firestore.collection("Usuarios").document(uid).set(datosUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast_Manager.showToast(User_Registro.this, "El registro se realizó correctamente");

                            startActivity(new Intent(User_Registro.this, LoginActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast_Manager.showToast(User_Registro.this, "No se pudo realizar el registro");
                        }
                    });
                    
                } else {
                    progressdialog.dismiss();
                    Toast_Manager.showToast(User_Registro.this, "No se pudo realizar el registro");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast_Manager.showToast(User_Registro.this, e.getMessage());
            }
        });
    }
}
