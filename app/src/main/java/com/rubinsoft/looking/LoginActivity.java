package com.rubinsoft.looking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText et_usuario, et_pass;
    Button btn_login, btn_registro;
    Spinner spinner_seleccion;
    String seleccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_usuario = findViewById(R.id.UsuarioLogin_et_usuario);
        et_pass = findViewById(R.id.UsuarioLogin_et_pass);
        btn_login = findViewById(R.id.UsuarioLogin_btn_login);
        btn_registro = findViewById(R.id.UsuarioLogin_btn_registro);
        spinner_seleccion = findViewById(R.id.Login_spinner);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccion = spinner_seleccion.getSelectedItem().toString();
                if(seleccion.equals("Jugador")) {

                } else if (seleccion.equals("Equipo")) {

                } else {
                    Toast.makeText(LoginActivity.this, "Debes seleccionar una categoría", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(LoginActivity.this, "Debes seleccionar una categoría", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}