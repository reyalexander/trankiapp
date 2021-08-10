package com.example.guardian.views.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

//import androidx.fragment.app.Fragment;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guardian.R;
import com.example.guardian.adapters.SitesAdapter;
import com.example.guardian.config.ConstValue;
import com.example.guardian.databases.SqliteClass;
import com.example.guardian.models.SitesModel;
import com.example.guardian.views.activitys.FormSitesActivity;
import com.example.guardian.views.activitys.LoginActivity;
import com.example.guardian.views.activitys.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SitesFragment extends Fragment {
    FloatingActionButton fltbtn_add_site;
    RecyclerView recyclerView;
    ArrayList<SitesModel> sites;
    TextView empty;
    Context context;
    SitesAdapter adapter;
    SearchView editsearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_sites, container, false);
        context= getActivity();

        fltbtn_add_site = (FloatingActionButton)rootView.findViewById(R.id.fltbtn_add_site);
        fltbtn_add_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Agregar un sitio",Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MainActivity.this, FormSitesActivity.class);
                Intent intent = new Intent(getActivity(), FormSitesActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                //finish();
            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        sites= SqliteClass.getInstance(getActivity()).databasehelp.siteSql.getAllSites();

        editsearch = (SearchView) rootView.findViewById(R.id.searchView);
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
                ArrayList<SitesModel> filteredValues = new ArrayList<SitesModel>(sites);
                for (SitesModel value : sites) {
                    if (!value.getName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredValues.remove(value);
                    }
                }
                getList(filteredValues);
                return false;
            }
        });

        empty = (TextView) rootView.findViewById(R.id.tv_empty);

        if(sites.size()>0){
            empty.setVisibility(recyclerView.GONE);
        }else {
            empty.setVisibility(recyclerView.VISIBLE);
        }


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        sites= SqliteClass.getInstance(getActivity()).databasehelp.siteSql.getAllSites();
        adapter = new SitesAdapter(context, sites);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }



    public void getList(ArrayList<SitesModel>list){
        if(list.size()>0){
            empty.setVisibility(recyclerView.GONE);
            adapter = new SitesAdapter(context, list);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }else {
            empty.setVisibility(recyclerView.VISIBLE);
        }

    }

    public void resetSearch() {
        sites = SqliteClass.getInstance(getActivity()).databasehelp.siteSql.getAllSites();
        getList(sites);
    }

}
