package com.rmpsoft.looking.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rmpsoft.looking.LoginActivity;
import com.rmpsoft.looking.R;

import java.util.HashMap;

public class User_Registro extends AppCompatActivity {

    EditText et_correo, et_nombre, et_apellido, et_user, et_edad, et_pass;
    RadioButton rb_hombre, rb_mujer;
    Button btn_registo;
    FirebaseAuth firebaseauth;

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

        btn_registo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = et_correo.getText().toString();
                String pass = et_pass.getText().toString();

                /* Validamos el formato del correo y la contraseña */
                if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                    et_correo.setError("Correo no válido");
                    et_correo.setFocusable(true);
                } else if (pass.length()<6) {
                    et_pass.setError("La contraseña debe ser mayor a seis caracteres");
                    et_pass.setFocusable(true);
                } else {
                    registrar(correo, pass);
                }
            }
        });

    }

    private void registrar (String correo, String pass) {

        firebaseauth.createUserWithEmailAndPassword(correo, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseauth.getCurrentUser();
                    String sexo;

                    assert user != null;
                    String uid = user.getUid();
                    String correo = et_correo.getText().toString();
                    String pass = et_pass.getText().toString();
                    String nombre = et_nombre.getText().toString();
                    String apellido = et_apellido.getText().toString();
                    String edad = et_edad.getText().toString();

                    if (rb_hombre.isChecked()) {
                        sexo = rb_hombre.getText().toString();
                    } else {
                        sexo = rb_mujer.getText().toString();
                    }

                    HashMap<Object, String> datosUser = new HashMap<>();

                    datosUser.put("uid", uid);
                    datosUser.put("correo", correo);
                    datosUser.put("pass", pass);
                    datosUser.put("nombre", nombre);
                    datosUser.put("apellido", apellido);
                    datosUser.put("edad", edad);
                    datosUser.put("sexo", sexo);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("DATABASE_LOOKING");
                    reference.child(uid).setValue(datosUser);
                    showToast("El registro se realizó correctamente");

                    startActivity(new Intent(User_Registro.this, LoginActivity.class));

                } else {
                    showToast("No se pudo realizar el registro");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast(e.getMessage());
            }
        });
    }

    private void showToast(String mensaje) {
        Toast.makeText(User_Registro.this, mensaje, Toast.LENGTH_SHORT).show();
        }
    }
