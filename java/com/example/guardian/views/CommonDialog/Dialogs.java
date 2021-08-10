package com.example.guardian.views.CommonDialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.guardian.config.ConstValue;
import com.example.guardian.databases.SqliteClass;
import com.example.guardian.views.activitys.LoginActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class Dialogs {
    public static void showLogoutDialog(final Activity activity, final Context context){
        new MaterialAlertDialogBuilder(activity)
                .setTitle("Salir")
                .setMessage("¿Está seguro de salir de la aplicación?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SqliteClass.getInstance(context).databasehelp.siteSql.deleteSites();
                        SqliteClass.getInstance(context).databasehelp.contactSql.deleteContact();
                        SqliteClass.getInstance(context).databasehelp.contacByCeltSql.deleteContact();

                        SharedPreferences sharedPref = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("logueado", "inactive");
                        editor.apply();
                        activity.finish();
                        Intent i = new Intent(activity, LoginActivity.class);
                        activity.startActivity(i);
                    }
                })
                .show();
    }
}
