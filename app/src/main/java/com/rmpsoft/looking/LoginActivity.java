package com.rmpsoft.looking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rmpsoft.looking.activitys.Equipo_Home;
import com.rmpsoft.looking.activitys.Equipo_Registro;
import com.rmpsoft.looking.activitys.User_Home;
import com.rmpsoft.looking.activitys.User_Registro;
import com.rmpsoft.looking.utils.Toast_Manager;

public class LoginActivity extends AppCompatActivity {

    EditText et_correo, et_pass;
    Button btn_login, btn_registro;
    Spinner spinner_seleccion;
    String seleccion;

    private FirebaseAuth firebaseauth;
    private ProgressDialog progressdialog;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setTitle(" ");

        et_correo = findViewById(R.id.UsuarioLogin_et_usuario);
        et_pass = findViewById(R.id.UsuarioLogin_et_pass);
        btn_login = findViewById(R.id.UsuarioLogin_btn_login);
        btn_registro = findViewById(R.id.UsuarioLogin_btn_registro);
        spinner_seleccion = findViewById(R.id.Login_spinner);

        firebaseauth = FirebaseAuth.getInstance();
        progressdialog = new ProgressDialog(LoginActivity.this);
        dialog = new Dialog(LoginActivity.this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccion = spinner_seleccion.getSelectedItem().toString();
                if(seleccion.equals("Jugador")) {

                    String correo = et_correo.getText().toString();
                    String pass = et_pass.getText().toString();

                    if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                        et_correo.setError("Introduzca un correo válido");
                        et_correo.setFocusable(true);
                    } else if (pass.length() < 6) {
                        et_pass.setError("La contraseña debe ser mayor a seis caracteres");
                        et_pass.setFocusable(true);
                    } else {
                        loginUser(correo, pass);
                    }

                } else if (seleccion.equals("Equipo")) {

                    String correo = et_correo.getText().toString();
                    String pass = et_pass.getText().toString();

                    if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                        et_correo.setError("Introduzca un correo válido");
                        et_correo.setFocusable(true);
                    } else if (pass.length() < 6) {
                        et_pass.setError("La contraseña debe ser mayor a seis caracteres");
                        et_pass.setFocusable(true);
                    } else {
                        loginUser(correo, pass);
                    }

                } else {
                    Toast_Manager.showToast(LoginActivity.this, "Debes seleccionar una categoria");
                }
            }
        });

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccion = spinner_seleccion.getSelectedItem().toString();
                if(seleccion.equals("Jugador")) {
                    startActivity(new Intent(LoginActivity.this, User_Registro.class));
                } else if (seleccion.equals("Equipo")) {
                    startActivity(new Intent(LoginActivity.this, Equipo_Registro.class));
                } else {
                    Toast_Manager.showToast(LoginActivity.this, "Debes seleccionar una categoria");
                }
            }
        });
    }

    private void loginUser(String correo, String pass) {

            progressdialog.setCancelable(false);
            progressdialog.show();
            firebaseauth.signInWithEmailAndPassword(correo, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressdialog.dismiss();
                        if (seleccion.equals("Jugador")) {
                            startActivity(new Intent(LoginActivity.this, User_Home.class));
                            finish();
                        } else if (seleccion.equals("Equipo")) {
                            startActivity(new Intent(LoginActivity.this, Equipo_Home.class));
                            finish();
                        }

                    } else {
                        progressdialog.dismiss();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogNoInicio ();
                        //showToast("Correo o contraseña incorrectos");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressdialog.dismiss();
                    //showToast(e.getMessage());
                }
            });

        }

    private void dialogNoInicio () {
        Button btn_ok;

        dialog.setContentView(R.layout.dialog_no_session);
        btn_ok = dialog.findViewById(R.id.DialogNoInicio_btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

}