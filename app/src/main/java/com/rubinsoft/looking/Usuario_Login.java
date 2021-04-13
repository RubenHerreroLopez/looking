package com.rubinsoft.looking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Usuario_Login extends AppCompatActivity {

    EditText et_usuario, et_pass;
    Button btn_login, btn_registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario__login);

        et_usuario = findViewById(R.id.UsuarioLogin_et_usuario);
        et_pass = findViewById(R.id.UsuarioLogin_et_pass);
        btn_login = findViewById(R.id.UsuarioLogin_btn_login);
        btn_registro = findViewById(R.id.UsuarioLogin_btn_registro);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Usuario_Login.this, Usuario_Registro.class));
            }
        });
    }
}