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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rmpsoft.looking.LoginActivity;
import com.rmpsoft.looking.R;
import com.rmpsoft.looking.utils.Toast_Manager;

import java.text.Normalizer;
import java.util.HashMap;

public class Equipo_Registro extends AppCompatActivity {

    EditText et_correo, et_equipo, et_deporte, et_municipio, et_liga, et_dia, et_pass;
    Button btn_registro;
    FirebaseAuth firebaseauth;
    FirebaseFirestore firestore;
    private ProgressDialog progressdialog;

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
        et_dia = findViewById(R.id.EquipoRegistro_et_dia);
        et_pass = findViewById(R.id.EquipoRegistro_et_pass);
        btn_registro = findViewById(R.id.EquipoRegistro_btn_registro);

        firebaseauth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressdialog = new ProgressDialog(Equipo_Registro.this);

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String correo = et_correo.getText().toString();
                String pass = et_pass.getText().toString();
                String equipo = et_equipo.getText().toString();
                String deporte = et_deporte.getText().toString();
                String municipio = et_municipio.getText().toString();
                String liga = et_liga.getText().toString();
                String dia = et_dia.getText().toString();

                /* Validamos el formato del correo y la contraseña así como que los campos están rellenados*/
                if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                    et_correo.setError("Introduzca un correo válido");
                    et_correo.setFocusable(true);
                } else if (pass.length()<6) {
                    et_pass.setError("La contraseña debe ser mayor a seis caracteres");
                    et_pass.setFocusable(true);
                } else if (equipo.isEmpty()) {
                    et_equipo.setError("El campo es obligatorio");
                    et_equipo.setFocusable(true);
                } else if (deporte.isEmpty()) {
                    et_deporte.setError("El campo es obligatorio");
                    et_deporte.setFocusable(true);
                } else if (municipio.isEmpty()) {
                    et_municipio.setError("El campo es obligatorio");
                    et_municipio.setFocusable(true);
                } else if (liga.isEmpty()) {
                    et_liga.setError("El campo es obligatorio");
                    et_liga.setFocusable(true);
                } else if (dia.isEmpty()) {
                    et_dia.setError("El campo es obligatorio");
                    et_dia.setFocusable(true);
                } else {
                    registrar(correo, pass);
                }
            }
        });
    }

    /* Método para registrar los equipos en la BBDD */
    private void registrar (String correo, String pass) {

        progressdialog.setCancelable(false);
        progressdialog.show();

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
                    String municipio = formatoString(et_municipio.getText().toString());
                    String liga = et_liga.getText().toString();
                    String deporte = formatoString(et_deporte.getText().toString());
                    String dia = formatoString(et_dia.getText().toString());

                    HashMap<Object, String> datosEquipo = new HashMap<>();

                    datosEquipo.put("uid", uid);
                    datosEquipo.put("correo", correo);
                    datosEquipo.put("pass", pass);
                    datosEquipo.put("equipo", equipo);
                    datosEquipo.put("municipio", municipio);
                    datosEquipo.put("liga", liga);
                    datosEquipo.put("deporte", deporte);
                    datosEquipo.put("dia", dia);

                    firestore.collection("Equipos").document(uid).set(datosEquipo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast_Manager.showToast(Equipo_Registro.this,"El registro se realizó correctamente");
                            startActivity(new Intent(Equipo_Registro.this, LoginActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast_Manager.showToast(Equipo_Registro.this, "No se pudo realizar el registro");
                        }
                    });

                } else {
                    progressdialog.dismiss();
                    Toast_Manager.showToast(Equipo_Registro.this, "No se pudo realizar el registro");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast_Manager.showToast(Equipo_Registro.this, e.getMessage());
            }
        });
    }

    /* Este método recoge un String y cambia la primera letra a mayusculas y las siguientes en minusculas.
     * Elimina las tíldes y los diacríticos.
     * Devuele un String con el resultado */
    private static String formatoString (String textoUsuario) {

        String textoMinusculas = textoUsuario.toLowerCase();
        char[] arr = textoMinusculas.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        String textoMayuscula = String.valueOf(arr);
        textoMayuscula = Normalizer.normalize(textoMayuscula, Normalizer.Form.NFD);
        return textoMayuscula.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }
}
