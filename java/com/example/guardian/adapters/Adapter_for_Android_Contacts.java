package com.example.guardian.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guardian.R;
import com.example.guardian.config.ConstValue;
import com.example.guardian.models.ContactCelModel;
import com.example.guardian.models.ContactModel;
import com.example.guardian.utils.ConnectionDetector;
import com.example.guardian.utils.Protocol;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Adapter_for_Android_Contacts extends BaseAdapter {
    TextView contact_by_cel;
    ArrayList<ContactCelModel> items;
    Context context;
    Protocol protocol;
    ConnectionDetector cd;

    public Adapter_for_Android_Contacts(ArrayList<ContactCelModel> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //View view=View.inflate(context, R.layout.row_item_contact_by_cel,null);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.row_item_contact_by_cel, null);
        }

        TextView tv_contact_by_cel_name, tv_contact_by_cel_phone;
        LinearLayout contac=(LinearLayout) convertView.findViewById(R.id.ln_detail_contact);
        tv_contact_by_cel_name = (TextView) convertView.findViewById(R.id.tv_contact_by_cel_name);
        tv_contact_by_cel_phone = (TextView) convertView.findViewById(R.id.tv_contact_by_cel_number);

        tv_contact_by_cel_name.setText(items.get(position).getName_cont());
        tv_contact_by_cel_phone.setText(items.get(position).getCelular_cont());
        SharedPreferences sharedPref = context.getSharedPreferences("login_preferences",Context.MODE_PRIVATE);

        protocol = new Protocol();
        cd = new ConnectionDetector(context);

        contac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cd.isConnectingToInternet()){
                    new MaterialAlertDialogBuilder(context)
                            .setTitle("Tranki")
                            .setMessage("¿Enviar invitación a "+items.get(position).getName_cont()+"?")
                            .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String url =  ConstValue.SEND_INVITATION;

                                    JSONObject jsonObjectSend = new JSONObject();
                                    try {

                                        jsonObjectSend.put("to_customer_id",String.valueOf(items.get(position).getId_cont()));
                                        jsonObjectSend.put("from_customer_id",sharedPref.getString("customer_id",""));

                                        protocol.sendWorkPostRequest(context,jsonObjectSend,url);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    //SmsManager sms = SmsManager.getDefault();
                                    //sms.sendTextMessage(items.get(position).getCelular_cont(), null, "Te invito a usar Tranki, Descargala en http://Tranki.com" , null, null);
                                }
                            })
                            .setNegativeButton("Cancelar", null)
                            .show();
                }
                else {
                    Toast.makeText(context,"El equipo no tiene conexión a internet",Toast.LENGTH_LONG).show();
                }


            }
        });

        return convertView;
    }
}
