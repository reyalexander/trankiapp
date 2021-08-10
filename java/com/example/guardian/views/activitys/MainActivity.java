package com.example.guardian.views.activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import android.app.Fragment;
import android.app.FragmentManager;
import android.widget.Toast;

import com.example.guardian.R;
import com.example.guardian.config.ConstValue;
import com.example.guardian.databases.SqliteClass;
import com.example.guardian.models.TicketModel;
import com.example.guardian.utils.ConnectionDetector;
import com.example.guardian.utils.PositionClass;
import com.example.guardian.utils.Protocol;
import com.example.guardian.utils.Utils;
import com.example.guardian.views.fragment.ConfigFragment;
import com.example.guardian.views.fragment.ContactsFragment;
import com.example.guardian.views.fragment.PositionFragment;
import com.example.guardian.views.fragment.SitesFragment;
import com.google.android.material.snackbar.Snackbar;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import tech.gusavila92.websocketclient.WebSocketClient;

public class MainActivity extends AppCompatActivity {
    boolean menu_=false;
    Context context=this;
    LinearLayout lnyt_person,lnyt_comunitaria;
    PositionClass positionClass;
    Location location;
    Protocol protocol;
    TicketModel ticketModel;
    ConnectionDetector cd;
    boolean msg = false;

    private WebSocketClient webSocketClient;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //BottomAppBar navView = findViewById(R.id.nav_view);

        protocol = new Protocol();
        getSupportActionBar().hide();
        positionClass =new PositionClass(context);

        cd = new ConnectionDetector(context);
        createWebSocketClient();

        SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.showIconOnly();
        spaceNavigationView.setCentreButtonRippleColor(ContextCompat.getColor(this, R.color.colorAccent));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_near_me_black_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_home_black_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_person_outline_black_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_settings_black_24dp));

        PositionFragment fragment=new PositionFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.sample_content_fragment, fragment)
                .commit();

        lnyt_person=(LinearLayout) findViewById(R.id.alert_person);
        lnyt_comunitaria=(LinearLayout) findViewById(R.id.alert_comunitaria);
        SharedPreferences sharedPref = context.getSharedPreferences("login_preferences",Context.MODE_PRIVATE);

        lnyt_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cd.isConnectingToInternet()){
                    location= positionClass.getLastBestLocation(context);
                    JSONObject jsonObjectEvent =new JSONObject();
                    try {
                        jsonObjectEvent.put("id_device",sharedPref.getString("device_id",""));
                        jsonObjectEvent.put("latitude",String.valueOf(location.getLatitude()));
                        jsonObjectEvent.put("longitude",String.valueOf(location.getLongitude()));
                        Log.i("JSON for SEND B F: ", jsonObjectEvent.toString());
                        protocol.sendAlert(context,jsonObjectEvent,ConstValue.POST_ALERT_OUTOPIC);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context,"Tranki, su dispositivo no tiene conexión a internet",Toast.LENGTH_LONG).show();
                }
            }
        });

        lnyt_comunitaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObjectEvent =new JSONObject();
                try {
                    jsonObjectEvent.put("id_device",sharedPref.getString("device_id",""));
                    jsonObjectEvent.put("latitude",String.valueOf(location.getLatitude()));
                    jsonObjectEvent.put("longitude",String.valueOf(location.getLongitude()));
                    Log.i("JSON for SEND B F: ", jsonObjectEvent.toString());
                    protocol.sendAlert(context,jsonObjectEvent,ConstValue.POST_ALERT_MULTITOPIC);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {

                //Toast.makeText(MainActivity.this,"onCentreButtonClick", Toast.LENGTH_SHORT).show();
                LinearLayout menu=(LinearLayout) findViewById(R.id.lyt_menu);

                if(menu.getVisibility()==View.VISIBLE){
                    menu.setVisibility(View.GONE);
                } else {
                    menu.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                Fragment fragment = null;
                switch (itemIndex){
                    case 0:
                        fragment = new PositionFragment();
                        break;
                    case 1:
                        fragment = new SitesFragment();
                        break;
                    case 2:
                        fragment = new ContactsFragment();
                        break;
                    case 3:
                        fragment = new ConfigFragment();
                        break;
                    default:
                        break;
                }
                FragmentManager fragmentManager = getFragmentManager();
                if (itemIndex==0) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.sample_content_fragment, fragment)
                            .commit();
                }else{
                    fragmentManager.beginTransaction()
                            .replace(R.id.sample_content_fragment, fragment)
                            .addToBackStack(null)
                            .commit();
                }
                //Toast.makeText(MainActivity.this, itemIndex + "ItemClick" + itemName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {}
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
            SharedPreferences sharedPref = getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
            Log.i("url_ws","ws://tranki.transcending.io/ws/socket/"+sharedPref.getString("user_id","1")+"/");
            String urix = sharedPref.getString("user_id","1");
            uri = new URI("ws://tranki.transcending.io/ws/socket/"+urix+"/");

        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                //Toast.makeText(context,"Conectado",Toast.LENGTH_LONG).show();
                //webSocketClient.send("connected  {\"device\":1,\"timestamp\":\"2020-07-30T18:10:46-05:00\",\"latitude\":\"-72.471577000\",\"longitude\":\"-16.220233000\",\"course\":11,\"satellites\":11}");
            }
            @Override
            public void onTextReceived(String s) {
                SharedPreferences sharedPref = context.getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
                Log.i("WebSocket", "Message received"+s);
                try {
                    /*
                    JSONObject jobj = new JSONObject(s.toString());
                    String st = jobj.getString("text");
                    */
                    JSONArray jsonarrayLocation = new JSONArray(s.toString());
                    JSONObject obj = jsonarrayLocation.getJSONObject(0);
                    /*
                    if(st != null){
                        ticketModel = new TicketModel(
                                1,
                                sharedPref.getString("device_id",""),
                                sharedPref.getString("case_id","1"),
                                Utils.getFecha(),
                                Utils.getHora(),
                                "Alex",
                                "986102683",
                                "",
                                0.0,
                                0.0,
                                ""
                        );
                        Utils.addNotificationAlert(context,sharedPref.getString("user","Tu dispositivo"));
                        SqliteClass.getInstance(context).databasehelp.ticketSql.addTicket(ticketModel);
                     */
                    if(obj.getString("state").equals("alert")){
                        //if(st.equals("alert")){
                        ticketModel = new TicketModel(
                                1,
                                String.valueOf(obj.getInt("device")),
                                String.valueOf(obj.getInt("case_id")),
                                obj.getString("fecha"),
                                obj.getString("hora"),
                                obj.getString("customer_name"),
                                obj.getString("customer_cellphone"),
                                "",
                                obj.getDouble("latitude"),
                                obj.getDouble("longitude"),
                                ""
                                );
                        Utils.addNotificationAlert(context,sharedPref.getString("user",""));
                        SqliteClass.getInstance(context).databasehelp.ticketSql.addTicket(ticketModel);

                    }else if(obj.getString("state").equals("position")){
                        Log.i("WebSocket", "Is position");
                        SqliteClass.getInstance(context).databasehelp.contactSql.updateLatiLongiContact(obj.getString("latitude"),obj.getString("longitude"),obj.getString("customer_id"));
                        PositionFragment.positionapTask rout= new PositionFragment.positionapTask();
                        rout.execute("");

                    }else if(obj.getString("state").equals("solicitude")){
                        Utils.addNotificationSolicitud(context,obj.getString("message"));
                    }else if(obj.getString("state").equals("alert")){
                        Toast.makeText(context, "ALERTA !!!!",Toast.LENGTH_SHORT);
                        System.out.println("Alert");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(context,"Mensaje recibido",Toast.LENGTH_LONG).show();
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
            @Override
            public void onBinaryReceived(byte[] data) {
            }
            @Override
            public void onPingReceived(byte[] data) {
            }
            @Override
            public void onPongReceived(byte[] data) {
            }
            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }
            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
                System.out.println("onCloseReceived");
            }
        };
        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

}
