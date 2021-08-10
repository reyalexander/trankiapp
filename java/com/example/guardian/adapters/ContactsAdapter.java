package com.example.guardian.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guardian.R;
import com.example.guardian.config.ConstValue;
import com.example.guardian.databases.SqliteClass;
import com.example.guardian.models.ContactModel;
import com.example.guardian.views.activitys.DetailContactActivity;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    Context context;
    List<ContactModel> items;
    View.OnClickListener listener;

    public ContactsAdapter(Context context, ArrayList<ContactModel> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.row_item_contact, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // Set item views based on your views and data model

        holder.bind(items.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Display info " + position, Toast.LENGTH_SHORT).show();
                ConstValue.setIdContact(items.get(position).getId_cont());
                Intent i = new Intent(context, DetailContactActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name_cont,tv_correo_cont,tv_cel_cont,tv_state_conta;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name_cont = (TextView) itemView.findViewById(R.id.tv_name_contac);
            tv_cel_cont = (TextView) itemView.findViewById(R.id.tv_cel_contact);
            tv_state_conta = (TextView) itemView.findViewById(R.id.tv_state_contact);

        }

        public void bind(ContactModel model){
            //textShipment.setText("Nro. " + model.getShipmentNumber());
            if(model.getState_cont().equals("0")){
                tv_state_conta.setText(R.string.state_ok);
                tv_state_conta.setTextColor(Color.GREEN);
            } else if(model.getState_cont().equals("1")){
                tv_state_conta.setText(R.string.state_alert);
                tv_state_conta.setTextColor(Color.RED);
            }
            tv_name_cont.setText(model.getName_cont());
            tv_cel_cont.setText(model.getCelular_cont());

        }

    }
}
