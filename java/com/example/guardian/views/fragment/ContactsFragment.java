package com.example.guardian.views.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

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
import com.example.guardian.adapters.ContactsAdapter;
import com.example.guardian.config.ConstValue;
import com.example.guardian.databases.SqliteClass;
import com.example.guardian.models.ContactModel;
import com.example.guardian.models.SitesModel;
import com.example.guardian.views.activitys.FormContactsActivity;
import com.example.guardian.views.activitys.SelectContactActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class ContactsFragment extends Fragment {
    FloatingActionButton fltbtn_add_contact;
    RecyclerView recyclerView;
    ArrayList<ContactModel> sites;
    TextView empty;
    Context context;
    ContactsAdapter adapter;
    SearchView editsearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_contacts, container, false);
        context= getActivity();

        fltbtn_add_contact = (FloatingActionButton)rootView.findViewById(R.id.fltbtn_add_contact);
        fltbtn_add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FormContactsActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                Toast.makeText(context,"Agregar un contacto",Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        sites=SqliteClass.getInstance(getActivity()).databasehelp.contactSql.getAllContacts();

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
                ArrayList<ContactModel> filteredValues = new ArrayList<ContactModel>(sites);
                for (ContactModel value : sites) {
                    if (!value.getName_cont().toLowerCase().contains(newText.toLowerCase())) {
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
        sites= SqliteClass.getInstance(getActivity()).databasehelp.contactSql.getAllContacts();
        adapter = new ContactsAdapter(context, sites);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void getList(ArrayList<ContactModel>list){
        if(list.size()>0){
            empty.setVisibility(recyclerView.GONE);
            adapter = new ContactsAdapter(context, list);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }else {
            empty.setVisibility(recyclerView.VISIBLE);
        }
    }

    public void resetSearch() {
        sites= SqliteClass.getInstance(getActivity()).databasehelp.contactSql.getAllContacts();
        getList(sites);
    }

}
