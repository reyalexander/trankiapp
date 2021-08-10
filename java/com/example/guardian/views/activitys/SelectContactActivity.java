package com.example.guardian.views.activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.provider.ContactsContract;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;


import com.example.guardian.R;
import com.example.guardian.adapters.Adapter_for_Android_Contacts;
import com.example.guardian.adapters.SitesAdapter;
import com.example.guardian.config.ConstValue;
import com.example.guardian.databases.SqliteClass;
import com.example.guardian.models.ContactCelModel;
import com.example.guardian.models.ContactModel;
import com.example.guardian.models.SitesModel;
import com.example.guardian.utils.Protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SelectContactActivity extends AppCompatActivity {
    ListView listContacts;

    ProgressDialog dialog;

    ArrayList<ContactCelModel> contactModels;
    Adapter_for_Android_Contacts adapter;
    Context context;
    Protocol protocol;
    boolean existsContacts =true;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        getSupportActionBar().setTitle("Buscar Contactos");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=this;
        protocol=new Protocol();
        //getSupportActionBar().hide();
        listContacts=(ListView) findViewById(R.id.lst_contacts);

        contactModels = new ArrayList<ContactCelModel>();

        new contactsTask().execute(true);

        //contactModels=SqliteClass.getInstance(context).databasehelp.contacByCeltSql.getAllContactsByCel();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SelectContactActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.trim().isEmpty()) {
                    resetSearch();
                    return false;
                }
                ArrayList<ContactCelModel> filteredValues = new ArrayList<ContactCelModel>(contactModels);
                for (ContactCelModel value : contactModels) {
                    if (!value.getName_cont().toLowerCase().contains(newText.toLowerCase())) {
                        filteredValues.remove(value);
                    }
                }
                //getList(filteredValues);
                if (existsContacts){
                    getList(filteredValues);
                } else {
                    getContactsCel(filteredValues);
                }

                return false;
            }
        });
        searchView.setQueryHint("Buscar...");
        return true;
    }
    public void resetSearch() {
        //contactModels = SqliteClass.getInstance(context).databasehelp.shipmentSql.getAllShipment();
        if (existsContacts){
            getList(contactModels);
        } else {
            getContactsCel(contactModels);
        }
        adapter = new Adapter_for_Android_Contacts( contactModels,this);
        listContacts.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            Intent intent = new Intent(SelectContactActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<ContactCelModel> getContactsCel(ArrayList<ContactCelModel> list){

        Cursor cursor_Android_Contacts = null;
        ContentResolver contentResolver = getContentResolver();
        try {
            cursor_Android_Contacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        } catch (Exception ex) {
            Log.e("Error on contact", ex.getMessage());
        }

        if (cursor_Android_Contacts.getCount() > 0) {

            while (cursor_Android_Contacts.moveToNext()) {
                ContactCelModel android_contact = new ContactCelModel();
                String contact_id = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts._ID));
                String contact_display_name = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                android_contact.setName_cont(contact_display_name);

                int hasPhoneNumber = Integer.parseInt(cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                            , null
                            , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                            , new String[]{contact_id}
                            , null);

                    while (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        android_contact.setCelular_cont(phoneNumber);

                    }
                    phoneCursor.close();
                }
                list.add(android_contact);
            }
        }
        adapter = new Adapter_for_Android_Contacts( list,this);
        listContacts.setAdapter(adapter);
        return list;
    }

    public void getList(ArrayList<ContactCelModel>list){
        adapter = new Adapter_for_Android_Contacts( list,this);
        listContacts.setAdapter(adapter);

    }

    class contactsTask extends AsyncTask<Boolean, Void, String> {

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(SelectContactActivity.this, "", getString(R.string.action_loading), true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Boolean... booleans) {

            String url = ConstValue.GET_ALL_CUSTOMERS;
            SharedPreferences sharedPref = getSharedPreferences("login_preferences",Context.MODE_PRIVATE);

            System.out.println("Customers"+protocol.getJsonCustomer(url,sharedPref.getString("token","")));
            ContactCelModel contact ;
            try {
                JSONArray jsonArrayCustomers =new  JSONArray(protocol.getJsonCustomer(url,sharedPref.getString("token","")));
                for (int i = 0 ;  i < jsonArrayCustomers.length() ; i++) {
                    contact = new ContactCelModel();
                    JSONObject jsonObjectContacto = new JSONObject(jsonArrayCustomers.get(i).toString());
                    contact.setId_cont(jsonObjectContacto.getInt("CustomerId"));
                    contact.setName_cont(
                            jsonObjectContacto.getString("FirstName") + " " + jsonObjectContacto.getString("Surname")
                    );
                    contact.setCelular_cont(jsonObjectContacto.getString("CellPhone"));
                    contactModels.add(contact);
                }
                //contactModels

                Collections.sort(contactModels, new Comparator<ContactCelModel>() {
                    public int compare(ContactCelModel obj1, ContactCelModel obj2) {
                        return obj1.getName_cont().compareTo(obj2.getName_cont());
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if(contactModels.size()>0){
                getList(contactModels);
            }else {
                getContactsCel(contactModels);
                existsContacts=false;
            }
            super.onPostExecute(s);
        }
    }

}
