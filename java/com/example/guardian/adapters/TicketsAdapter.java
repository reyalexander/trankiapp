package com.example.guardian.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.guardian.R;
import com.example.guardian.config.ConstValue;
import com.example.guardian.databases.SqliteClass;
import com.example.guardian.models.TicketModel;
import com.example.guardian.utils.PositionClass;
import com.example.guardian.utils.Protocol;
import com.example.guardian.views.activitys.MyTicketsActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.ViewHolder> {

    Context context;
    List<TicketModel> items;
    Location location;
    PositionClass positionClass;
    Protocol protocol;
    public TicketsAdapter(Context context, List<TicketModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View siteView = inflater.inflate(R.layout.row_item_ticket, parent, false);
        ViewHolder viewHolder = new ViewHolder(siteView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TicketsAdapter.ViewHolder holder, final int position) {
        holder.bind(items.get(position));
        positionClass =new PositionClass(context);
        protocol = new Protocol();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(context)
                        .setTitle("Alerta")
                        .setIcon(R.drawable.ic_alert)
                        .setMessage("Esta apunto de cerrar un ticket, significa que el peligro ya paso ¿Esta seguro de realizar esta acción?")
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SqliteClass.getInstance(context).databasehelp.ticketSql.deleteTicketId(String.valueOf(items.get(position).getId()));
                                location= positionClass.getLastBestLocation(context);

                                JSONObject jsonObjectEvent =new JSONObject();
                                try {
                                    jsonObjectEvent.put("id_device",6);

                                    Log.i("JSON for SEND B F: ", jsonObjectEvent.toString());
                                    protocol.sendWorkPostRequest(context,jsonObjectEvent, ConstValue.PUT_OUTOPIC);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Intent intent = new Intent(context, MyTicketsActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                context.startActivity(intent);
                            }
                        }).show();
            }
        });

    }

    @Override
    public int getItemCount() { return items.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_cod_disp,tv_operacion,tv_direccion_ticket,tv_fecha,tv_hora,tv_user,tv_latitud,tv_longitud;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_cod_disp = (TextView) itemView.findViewById(R.id.tv_cod_disp);
            tv_operacion = (TextView) itemView.findViewById(R.id.tv_operacion);
            tv_direccion_ticket = (TextView) itemView.findViewById(R.id.tv_direccion_ticket);
            tv_user = (TextView) itemView.findViewById(R.id.tv_user);
            tv_fecha = (TextView) itemView.findViewById(R.id.tv_fecha);
            tv_hora = (TextView) itemView.findViewById(R.id.tv_hora);
            tv_latitud = (TextView) itemView.findViewById(R.id.tv_latitud);
            tv_longitud = (TextView) itemView.findViewById(R.id.tv_longitud);

        }

        public void bind(TicketModel model){
            tv_cod_disp.setText(model.getCodDispositivo());
            tv_operacion.setText(model.getNumOperacion());
            tv_user.setText(model.getCliNombre());
            tv_direccion_ticket.setText(model.getDireccion());
            tv_fecha.setText(model.getFecha());
            tv_hora.setText(model.getHora());
            tv_latitud.setText(Double.toString(model.getLatitud()));
            tv_longitud.setText(Double.toString(model.getLongitud()));
        }
    }
}
