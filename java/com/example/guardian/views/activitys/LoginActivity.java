package com.example.guardian.views.activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.guardian.R;
import com.example.guardian.config.ConstValue;
import com.example.guardian.databases.SqliteClass;
import com.example.guardian.models.ContactModel;
import com.example.guardian.models.SitesModel;
import com.example.guardian.utils.ConnectionDetector;
import com.example.guardian.utils.Protocol;
import com.example.guardian.utils.Utils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tech.gusavila92.apache.http.HttpResponse;

public class LoginActivity extends AppCompatActivity {
    private  final int REQUEST_ACCESS_FINE = 0;
    EditText et_user, et_pass;
    Button btLogin,btnRegister;
    Context context;
    Activity activity;
    ProgressDialog dialog;
    Protocol protocol;
    String username,password;
    ConnectionDetector cd;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        context=this;
        protocol=new Protocol();
        cd=new ConnectionDetector(context);

        Utils.enableLocation(LoginActivity.this,context);
        //Utils.enableMessage(LoginActivity.this,context);
        //Utils.enableReadStorage(LoginActivity.this,context);
        //Utils.enableWriteStorage(LoginActivity.this,context);
        //Utils.enableReadPhoneState(LoginActivity.this,context);
        //Utils.enableCallPhone(LoginActivity.this,context);

        /** Mantener sesión activa **/
        SharedPreferences sharedPref = getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
        String logueado=sharedPref.getString("logueado","inactive");
        if(logueado.equals("active")){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }

        et_user=(EditText) findViewById(R.id.et_username);
        et_pass=(EditText) findViewById(R.id.et_password);
        //et_user.setText("admin");
        //et_pass.setText("P4ssw0rd!");

        btLogin = (Button) findViewById(R.id.bt_login);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_user.getText().length()>0 && et_pass.getText().length()>0){
                    if (cd.isConnectingToInternet()) {
                        //new loginTask().execute(true);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(context,"Tranki : Su dispositivo no cuenta con conexión a internet.",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(context,"Tranki : Ingrese un usario y/o contraseña",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnRegister=(Button) findViewById(R.id.bt_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PhoneAuthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    class loginTask extends AsyncTask<Boolean, Void, String> {

        @Override
        protected void onPreExecute() {
            username=et_user.getText().toString();
            password=et_pass.getText().toString();
            dialog = ProgressDialog.show(LoginActivity.this, "", getString(R.string.action_loading), true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if(s.equals("okey")){
                /** Activar el estado de sessión main **/
                SharedPreferences sharedPref = getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("logueado", "active");
                editor.apply();

                /** Redireccionar a clase main **/
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(context,"Error al recuperar datos",Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Boolean... booleans) {
            String resultado="";
            try {
                /** Datos de prueba **/
                //State 1 ==> en alerta
                //State 0 ==> en no alerta

                JSONObject jsonLogin = new JSONObject();
                jsonLogin.put("username",username);
                jsonLogin.put("password",password);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("password", password));

                JSONObject result=protocol.sendJsonData(ConstValue.AUTH_LOGIN,nameValuePairs);
                /*
                if(result.getString("status").equals("error")){
                    Toast.makeText(context, "Debe validar su cuenta!!!", Toast.LENGTH_LONG).show();
                }
            */
                if(result.getString("token").equals(null)){
                    Toast.makeText(context,"Tranki : Error al recuperar datos del portal",Toast.LENGTH_LONG).show();
                    resultado="error";
                } else {
                    resultado="okey";
                    /** Guardando token **/
                    SharedPreferences sharedPref = context.getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("token", result.getString("token"));
                    editor.putString("user_id", result.getString("user_id"));
                    editor.putString("device_id", result.getString("id_device"));
                    editor.putString("customer_id", result.getString("customer_id"));
                    editor.putString("user_first_name", result.getString("user_first_name"));
                    editor.apply();

                    /** Address **/

                    /*
                    SharedPreferences sharedPrefStore = getSharedPreferences("Detail_New_Store",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorStore = sharedPref.edit();
                    String jsonGet=protocol.getJson(ConstValue.GET_ADDRESS);
                    Log.i("get Location JSON ", jsonGet);
                    JSONArray jsonarray = new JSONArray(protocol.getJson(ConstValue.GET_ADDRESS));
                    if(jsonarray.length()>0){
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);

                            SitesModel sitesModel=new SitesModel(1,
                                    obj.getString("RazonSocial"),
                                    obj.getString("Latitud"),
                                    obj.getString("Longitud"),
                                    obj.getString("Address1"),
                                    obj.getString("Correo"),
                                    obj.getString("RUC"),
                                    "Lun_Vie / 9-16",
                                    "0");

                            editorStore.putString("lastsite",obj.getString("AddressId"));
                            editorStore.apply();
                            SqliteClass.getInstance(context).databasehelp.siteSql.addSite(sitesModel);
                        }
                    }
                     */

                    String url = ConstValue.GET_CUSTOMERS+result.getString("user_id")+"/";
                    System.out.println("Customers"+protocol.getJsonCustomer(url,result.getString("token")));

                    JSONArray jsonArrayRelatinCustomer =new  JSONArray(protocol.getJsonCustomer(url,result.getString("token")));
                    for(int i=0; i<jsonArrayRelatinCustomer.length();i++){
                        JSONObject jsonObjectCustomer =  new JSONObject(jsonArrayRelatinCustomer.get(i).toString());
                        JSONObject jsonDataContac =  new JSONObject(jsonObjectCustomer.getString("contact"));
                        ContactModel contactModel = new ContactModel();
                        JSONArray jsonArrayDevice= new JSONArray(jsonDataContac.getString("devices"));
                        JSONObject jsonObjectDevice =  new JSONObject(jsonArrayDevice.get(0).toString());
                        JSONObject jsonObjectLast = new JSONObject(jsonObjectDevice.getString("last_position"));
                        contactModel.setPos_lati_cont(jsonObjectLast.getString("latitude"));
                        contactModel.setPos_longi_cont(jsonObjectLast.getString("longitude"));
                        contactModel.setState_cont(jsonObjectCustomer.getString("status"));
                        contactModel.setId_cont_api(jsonDataContac.getInt("CustomerId"));
                        contactModel.setDNI_cont(jsonDataContac.getString("Dni"));
                        contactModel.setAddrescont(jsonDataContac.getString("Address"));
                        contactModel.setName_cont(jsonDataContac.getString("FirstName") + " " + jsonDataContac.getString("SecondName"));
                        contactModel.setCelular_cont(jsonDataContac.getString("CellPhone"));
                        contactModel.setApelli_cont(jsonDataContac.getString("Surname")+ " " + jsonDataContac.getString("SecondSurname"));
                        SqliteClass.getInstance(context).databasehelp.contactSql.addContact(contactModel);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resultado;
        }

    }
}
