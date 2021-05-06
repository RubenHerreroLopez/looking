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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rmpsoft.looking.LoginActivity;
import com.rmpsoft.looking.R;
import com.rmpsoft.looking.utils.Toast_Manager;

import java.util.HashMap;

public class Equipo_Registro extends AppCompatActivity {

    EditText et_correo, et_equipo, et_deporte, et_municipio, et_liga, et_pass;
    Button btn_registro;
    FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipo__registro);

        et_correo = findViewById(R.id.EquipoRegistro_et_correo);
        et_equipo = findViewById(R.id.EquipoRegistro_et_equipo);
        et_deporte = findViewById(R.id.EquipoRegistro_et_deporte);
        et_municipio = findViewById(R.id.EquipoRegistro_et_municipio);
        et_liga = findViewById(R.id.EquipoRegistro_et_liga);
        et_pass = findViewById(R.id.EquipoRegistro_et_pass);
        btn_registro = findViewById(R.id.EquipoRegistro_btn_registro);

        firebaseauth = FirebaseAuth.getInstance();

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String correo = et_correo.getText().toString();
                String pass = et_pass.getText().toString();

                /* Validamos el formato del correo y la contraseña */
                if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                    et_correo.setError("Introduzca un correo válido");
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

                    assert user != null;
                    String uid = user.getUid();
                    String correo = et_correo.getText().toString();
                    String pass = et_pass.getText().toString();
                    String equipo = et_equipo.getText().toString();
                    String municipio = et_municipio.getText().toString();
                    String liga = et_liga.getText().toString();
                    String deporte = et_deporte.getText().toString();

                    HashMap<Object, String> datosEquipo = new HashMap<>();

                    datosEquipo.put("uid", uid);
                    datosEquipo.put("correo", correo);
                    datosEquipo.put("pass", pass);
                    datosEquipo.put("equipo", equipo);
                    datosEquipo.put("municipio", municipio);
                    datosEquipo.put("liga", liga);
                    datosEquipo.put("deporte", deporte);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("DATABASE_LOOKING");
                    reference.child(uid).setValue(datosEquipo);
                    Toast_Manager.showToast(Equipo_Registro.this,"El registro se realizó correctamente");

                    startActivity(new Intent(Equipo_Registro.this, LoginActivity.class));

                } else {
                    Toast_Manager.showToast(Equipo_Registro.this, "No se pudo realizar el registro");
                }
            }
        });
    }

}
