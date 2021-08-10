package com.example.guardian.views.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.guardian.R;
import com.example.guardian.adapters.SitesAdapter;
import com.example.guardian.adapters.TicketsAdapter;
import com.example.guardian.databases.SqliteClass;
import com.example.guardian.models.SitesModel;
import com.example.guardian.models.TicketModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import static com.example.guardian.config.ConstValue.getActivity;

public class MyTicketsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView empty;
    Context context;
    TicketsAdapter adapter;
    CardView row_ticket;
    ArrayList<TicketModel>tickets ;

    SearchView editsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);
        //getSupportActionBar().hide();
        getSupportActionBar().setTitle("Mis Tickets");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=this;

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_ticket);
        tickets = SqliteClass.getInstance(context).databasehelp.ticketSql.getAllTicket();
        empty = (TextView) findViewById(R.id.tv_empty);
        getList(tickets);

        editsearch = (SearchView) findViewById(R.id.searchView_ticket);
        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                ArrayList<TicketModel> filteredValues = new ArrayList<TicketModel>(tickets);
                for (TicketModel value : tickets) {
                    if (!value.getNumOperacion().toLowerCase().contains(newText.toLowerCase())) {
                        filteredValues.remove(value);
                    }
                }
                getList(filteredValues);
                return false;
            }
        });

        empty = (TextView) findViewById(R.id.tv_empty);

        if(tickets.size()>0){
            empty.setVisibility(recyclerView.GONE);
        }else {
            empty.setVisibility(recyclerView.VISIBLE);
        }

    }

    public void getList(ArrayList<TicketModel>list){
        if(list.size()>0){
            empty.setVisibility(recyclerView.GONE);
            adapter = new TicketsAdapter(context, list);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }else {
            empty.setVisibility(recyclerView.VISIBLE);
        }
    }

    public void resetSearch() {
        tickets = SqliteClass.getInstance(getActivity()).databasehelp.ticketSql.getAllTicket();
        getList(tickets);
    }

    @Override
    public void onResume() {
        super.onResume();
        tickets = SqliteClass.getInstance(getActivity()).databasehelp.ticketSql.getAllTicket();
        getList(tickets);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent = new Intent(MyTicketsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
