package com.example.guardian.config;

import android.app.Activity;
import android.content.Context;

public class ConstValue {
    public static String BASE_URL = "http://tranki.transcending.io/api/"; //Production
    //public static String BASE_URL = "http://192.168.0.5:8080/"; //QUA
    //authentication/login/
    public static String AUTH_LOGIN = BASE_URL + "authentication/login/";
    public static String REGISTER_USER = BASE_URL + "authentication/create-user/";
    public static String GET_CUSTOMERS = BASE_URL + "customers/customer-relationships/";
    public static String GET_ALL_CUSTOMERS = BASE_URL + "customers/customers/";
    public static String SEND_INVITATION = BASE_URL + "customers/send_solicitude/";

    public static String GET_ADDRESS = BASE_URL + "address/";
    public static String POST_ADDRESS = BASE_URL + "address/";
    public static String POST_ALERT_OUTOPIC = BASE_URL + "devices/devices/register_outtopicevent/";
    public static String PUT_OUTOPIC = BASE_URL + "devices/devices/update_outtopicevent/";
    public static String POST_ALERT_MULTITOPIC = BASE_URL + "devices/devices/register_multitopicevent/";
    public static String POST_LAST_POSITION = BASE_URL + "devices/devices/last_position_device/"; //json id_device
    public static String GET_LAST_POSITIONS = BASE_URL + "devices/devices/last_position_devices/"; //json id_device
    public static String POST_TRACK = BASE_URL + "devices/devices/insert_tracks/";
    public static String GET_TICKETS = BASE_URL + "devices/devices/send_report/";
    public static Context context;
    public static Activity activity;

    public static Context getContext() {
        return context;
    }
    public static void setContext(Context context) {
        ConstValue.context = context;
    }

    public static Activity getActivity() { return activity; }
    public static void setActivity(Activity activity) { ConstValue.activity = activity; }

    public static int idSite;
    public static int getIdSite() { return idSite; }
    public static void setIdSite(int idSite) { ConstValue.idSite = idSite; }

    public static int idContact;
    public static int getIdContact() { return idContact; }
    public static void setIdContact(int idContact) { ConstValue.idContact = idContact; }
}
