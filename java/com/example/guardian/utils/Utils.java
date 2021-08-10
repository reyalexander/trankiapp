package com.example.guardian.utils;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.guardian.R;
import com.example.guardian.models.TicketModel;
import com.example.guardian.views.activitys.MyTicketsActivity;

import java.util.Calendar;

public class Utils {

    public static String obtenerNombreDeDispositivo() {
        String fabricante = Build.MANUFACTURER;
        String modelo = Build.MODEL;
        if (modelo.startsWith(fabricante)) {
            //return primeraLetraMayuscula(modelo);
            return (modelo);
        } else {
            return fabricante + " " + modelo;
        }
    }

    public static String getFecha(){
        String result="";
        final Calendar calendario = Calendar.getInstance();
        int yy = calendario.get(Calendar.YEAR);
        int mm = calendario.get(Calendar.MONTH);
        int dd = calendario.get(Calendar.DAY_OF_MONTH);
        String mes="";
        switch(mm){
            case 0:
            {
                mes="Ene.";
                break;
            }
            case 1:
            {
                mes="Feb.";
                break;
            }
            case 2:
            {
                mes="Mar.";
                break;
            }
            case 3:
            {
                mes="Abr.";
                break;
            }
            case 4:
            {
                mes="May.";
                break;
            }
            case 5:
            {
                mes="Jun.";
                break;
            }
            case 6:
            {
                mes="Jul.";
                break;
            }
            case 7:
            {
                mes="Ago.";
                break;
            }
            case 8:
            {
                mes="Sep.";
                break;
            }
            case 9:
            {
                mes="Oct.";
                break;
            }
            case 10:
            {
                mes="Nov.";
                break;
            }
            case 11:
            {
                mes="Dic.";
                break;
            }
            default:
            {
                mes="Error";
                break;
            }
        }
        result=mes+" "+ dd + " "  + yy;
        return result;
    }

    public static String getHora(){
        final Calendar c = Calendar.getInstance();

        //Variables para obtener la hora hora
        int hora = c.get(Calendar.HOUR_OF_DAY);
        final int minuto = c.get(Calendar.MINUTE);
        final int segundo = c.get(Calendar.SECOND);
        System.out.println(hora%12 + ":" + minuto + " " + ((hora>=12) ? "PM" : "AM"));
        String periodo = ((hora>=12) ? "PM" : "AM");
        if(hora%12 == 0){
            hora=1;
        }else {
            hora=hora%12;
        }
        return hora + ":" + minuto + " " + periodo;

    }

    public static String getHoraNum(){
        final Calendar c = Calendar.getInstance();
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minuto = c.get(Calendar.MINUTE);
        int segundo = c.get(Calendar.SECOND);

        return hora + ":" + minuto + ":" + segundo;

    }

    public static String getFechaNum(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        month++;

        return year + "-" + month + "-" + day;

    }

    public static void addNotificationAlert(Context context, String user) {
        Intent intent = new Intent(context, MyTicketsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Tranki";
            String description = "Connecttix - Transcending";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_alert)
                .setShowWhen(true)
                .setNumber(1)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setContentTitle("Contacto")
                //.setSubText("SubText")
                .setContentText(user+" ha generado una alerta")
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        builder.setVibrate(new long[] { 1000, 1000});
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }

    public static void addNotificationSolicitud(Context context,String user) {
        Intent intent = new Intent(context, MyTicketsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Tranki";
            String description = "Connecttix - Transcending";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_alert)
                .setShowWhen(true)
                .setNumber(1)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setContentTitle("Contacto")
                //.setSubText("SubText")
                .setContentText(user)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        builder.setVibrate(new long[] { 1000, 1000});
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(2, builder.build());
    }

    public static void enableLocation(Activity activity, Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,
                    Manifest.permission.SEND_SMS,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE}, 0);
            return;
        }
    }
}
