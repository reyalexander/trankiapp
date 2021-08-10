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
import com.example.guardian.models.SitesModel;
import com.example.guardian.views.activitys.DetailSiteActivity;

import java.util.ArrayList;
import java.util.List;

public class SitesAdapter extends RecyclerView.Adapter<SitesAdapter.ViewHolder>{

    Context context;
    List<SitesModel> items;

    public SitesAdapter(Context context, ArrayList<SitesModel> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View siteView = inflater.inflate(R.layout.row_item_sites, parent, false);
        ViewHolder viewHolder = new ViewHolder(siteView);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        // Set item views based on your views and data model

        holder.bind(items.get(position));

        //String state = SqliteClass.getInstance(context).databasehelp.siteSql.getType(position+1);
        /*
        if(state.equals("0")){
            //holder.state.setImageResource(R.drawable.ic_assignment_black_24dp);
            holder.tv_state.setText(R.string.state_ok);
            //holder.tv_state.setTextColor(R.color.green);
            holder.tv_state.setTextColor(Color.GREEN);
        }
        else if(state.equals("1")){
            holder.tv_state.setText(R.string.state_alert);
            //holder.tv_state.setTextColor(R.color.red);
            holder.tv_state.setTextColor(Color.RED);

        }
*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Display info " + position, Toast.LENGTH_SHORT).show();
                ConstValue.setIdSite(items.get(position).getId());
                //items.get(position).getId();
                Intent i = new Intent(context, DetailSiteActivity.class);
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

        public TextView tv_name,tv_direccion,tv_ruc, tv_horario,tv_state,tv_correo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_direccion = (TextView) itemView.findViewById(R.id.tv_direccion);
            tv_state = (TextView) itemView.findViewById(R.id.tv_state);

        }

        public void bind(SitesModel model){
            //textShipment.setText("Nro. " + model.getShipmentNumber());
            if(model.getState().equals("1")){
                tv_state.setText(R.string.state_alert);
                tv_state.setTextColor(Color.RED);
            }else if (model.getState().equals("0")){
                tv_state.setText(R.string.state_ok);
                tv_state.setTextColor(Color.GREEN);
            }

            tv_name.setText(model.getName());
            tv_direccion.setText(model.getDireccion());
            //tv_state.setText(model.getState());
        }

    }
}
