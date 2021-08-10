package com.example.guardian.views.activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.guardian.R;
import com.example.guardian.config.ConstValue;
import com.example.guardian.utils.ConnectionDetector;
import com.example.guardian.utils.Protocol;
import com.example.guardian.utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    Context context = this;
    EditText et_number_phone,et_fisrt_name,et_second_name,et_surname,et_second_surname,et_username,et_password, et_dni,et_correo,et_direccion,et_serial_number_device,et_company_id;
    String number,first_name,second_name,surname,second_surname,username,password,dni,correo,direccion,serial_number_device,company_id;
    Button btn_register;
    Protocol protocol;
    ConnectionDetector cd;
/**
 * https://appsnipp.com/free-login-registration-xml-design-for-android/
 * **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        getSupportActionBar().hide();
        //getSupportActionBar().setTitle("Mis Tickets");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        protocol = new Protocol();
        cd = new ConnectionDetector(context);

        et_number_phone = (EditText) findViewById(R.id.et_number_phone);
        et_number_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>9){
                    et_number_phone.setError("Número invalido");
                }
            }
        });
        et_fisrt_name = (EditText) findViewById(R.id.et_first_name);
        et_second_name = (EditText) findViewById(R.id.et_second_name);
        et_surname = (EditText) findViewById(R.id.et_surname);
        et_second_surname = (EditText) findViewById(R.id.et_second_surname);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_dni = (EditText) findViewById(R.id.et_dni);
        et_dni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>8){
                    et_dni.setError("Número DNI invalido");
                }
            }
        });
        et_correo = (EditText) findViewById(R.id.et_email);
        et_direccion = (EditText) findViewById(R.id.et_direccion);
        et_company_id = (EditText) findViewById(R.id.et_company_id);
        et_serial_number_device = (EditText) findViewById(R.id.et_serial_number_device);
        et_serial_number_device.setText("ESP8266Client-1007");
        btn_register = (Button) findViewById(R.id.bt_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cd.isConnectingToInternet()){
                    if(et_serial_number_device.getText().length()>=2){
                        new registerTask().execute(true);
                    }else {
                        Toast.makeText(context,"Es necesario el código del dispositivo",Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(context,"El equipo no tiene conexión a internet",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    class registerTask extends AsyncTask<Boolean, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            number = et_number_phone.getText().toString();
            username = et_username.getText().toString();
            first_name = et_fisrt_name.getText().toString();
            second_name = et_second_name.getText().toString();
            surname = et_surname.getText().toString();
            second_surname = et_second_surname.getText().toString();
            password = et_password.getText().toString();
            dni = et_dni.getText().toString();
            correo = et_correo.getText().toString();
            direccion=et_direccion.getText().toString();
            company_id = et_company_id.getText().toString();
            serial_number_device =et_serial_number_device.getText().toString();
            validar();
        }
        public boolean validar(){
            boolean retorno = true;
            if(number.isEmpty()){
                et_number_phone.setError("Numero no puede estar vacio");
                retorno = false;
            }
            if(username.isEmpty()){
                et_username.setError("Usuario no puede estar vacio");
                retorno = false;
            }
            if(first_name.isEmpty()){
                et_fisrt_name.setError("Primer Nombre no puede estar vacio");
                retorno = false;
            }
            if(surname.isEmpty()){
                et_surname.setError("Apellido Paterno no puede estar vacio");
                retorno = false;
            }
            if(second_surname.isEmpty()){
                et_second_surname.setError("Apellido Materno no puede estar vacio");
                retorno = false;
            }
            if(password.isEmpty()){
                et_password.setError("Contraseña no puede estar vacia");
                retorno = false;
            }
            if(dni.isEmpty()){
                et_dni.setError("DNI no puede estar vacio");
                retorno = false;
            }
            if(direccion.isEmpty()){
                et_direccion.setError("Dirección no puede estar vacio");
                retorno = false;
            }
            if(correo.isEmpty()){
                et_correo.setError("Correo no puede estar vacio");
                retorno = false;
            }
            if(company_id.isEmpty()){
                et_company_id.setError("Compañia ID no puede estar vacio");
                retorno = false;
            }
            if(serial_number_device.isEmpty()){
                et_serial_number_device.setError("Codigo de Dispositivo no puede estar vacio");
                retorno = false;
            }
            return retorno;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(validar() == false){
                Toast.makeText(context,"Datos Incompletos", Toast.LENGTH_LONG).show();
            }else {
                AlertDialog.Builder myBuild = new AlertDialog.Builder(RegisterActivity.this);
                myBuild.setMessage("\t       Registro Exitoso!\n Revise su Correo Electronico");
                myBuild.setTitle("         Confirmación");
                myBuild.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }
                });

                AlertDialog dialog = myBuild.create();
                dialog.show();
            }
            /*
            if(s == "okey"){
                Toast.makeText(context,"Registro de datos correctos",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(context,"Error en el registro de datos",Toast.LENGTH_LONG).show();
            }
            */
        }

        @SuppressLint("MissingPermission")
        @Override
        protected String doInBackground(Boolean... booleans) {

            JSONObject jsonObjectUser = new JSONObject();
            try {
                jsonObjectUser.put("username",username);
                jsonObjectUser.put("password",password);
                jsonObjectUser.put("email",correo);
                jsonObjectUser.put("first_name",first_name);
                jsonObjectUser.put("second_name",second_name);
                jsonObjectUser.put("surname",surname);
                jsonObjectUser.put("second_surname",second_surname);
                jsonObjectUser.put("cellphone",number);
                jsonObjectUser.put("address",direccion);
                jsonObjectUser.put("serialnumber",serial_number_device);
                jsonObjectUser.put("dni",dni);
                jsonObjectUser.put("company_id",company_id);
                String myIMEI = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                jsonObjectUser.put("imei",myIMEI);
                Log.i("SEND",jsonObjectUser.toString());
                protocol.sendUser(context,RegisterActivity.this,jsonObjectUser,ConstValue.REGISTER_USER);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
