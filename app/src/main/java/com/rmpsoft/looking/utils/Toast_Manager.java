package com.rmpsoft.looking.utils;

import android.content.Context;
import android.widget.Toast;

import com.rmpsoft.looking.activitys.User_Registro;

public class Toast_Manager {

    public static void showToast(Context context, String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }
}
