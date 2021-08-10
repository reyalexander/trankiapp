package com.example.guardian.views.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

//import androidx.fragment.app.Fragment;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.guardian.R;
import com.example.guardian.views.CommonDialog.Dialogs;
import com.example.guardian.views.activitys.MainActivity;
import com.example.guardian.views.activitys.MyTicketsActivity;

@SuppressLint("NewApi")
public class ConfigFragment extends Fragment {
    LinearLayout lnl_logout,lnl_ticket;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_config, container, false);
        lnl_logout=(LinearLayout) rootView.findViewById(R.id.lnl_logout);
        lnl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.showLogoutDialog(getActivity(),getActivity());
            }
        });

        lnl_ticket = (LinearLayout) rootView.findViewById(R.id.lnl_ticket);
        lnl_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyTicketsActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

}
