package com.example.guardian.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.guardian.views.activitys.LoginActivity;
import com.example.guardian.views.activitys.RegisterActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Protocol {
    public JSONObject sendJsonData(String STRURL, List<NameValuePair> nameValuePairs){
        JSONObject objReturn = null;
        String errorString = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(STRURL);
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            //httppost.setHeader("","");
            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                InputStream is = r_entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                String json = sb.toString();
                if (json.startsWith("ï»¿")) { json = json.substring(3); }
                objReturn = new JSONObject(json);
            } else {
                objReturn = new JSONObject("{token:'empty'");
            }
        } catch (ClientProtocolException e) {
            errorString = e.toString();
        } catch (IOException e) {
            errorString = e.toString();
        }catch (JSONException e) {
            errorString = e.toString();
        }
        if(errorString!=null){
            try {
                objReturn = new JSONObject("{error:'"+errorString+"'}");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return objReturn;
    }

    public JSONObject sendJson(String STRURL, List<NameValuePair> nameValuePairs){
        JSONObject objReturn = null;
        String errorString = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(STRURL);
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            //httppost.setHeader("","");
            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            //if (statusCode == 200) {
                // Server response
                InputStream is = r_entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                String json = sb.toString();
                if (json.startsWith("ï»¿")) { json = json.substring(3); }
                objReturn = new JSONObject(json);
            //} else if(){
              //  objReturn = new JSONObject("{token:'empty'");
            //}
        } catch (ClientProtocolException e) {
            errorString = e.toString();
        } catch (IOException e) {
            errorString = e.toString();
        }catch (JSONException e) {
            errorString = e.toString();
        }
        if(errorString!=null){
            try {
                objReturn = new JSONObject("{error:'"+errorString+"'}");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return objReturn;
    }

    public static JSONObject postJson(String url, JSONObject postData){
        BufferedReader reader = null;
        String result="";
        JSONObject objReturn = null;
        HttpURLConnection httpURLConnection =null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setRequestMethod("POST");
            //httpURLConnection.setRequestProperty ("Authorization", ""); /** Aqui va al token **/
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept","application/json");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            httpURLConnection.connect();

            DataOutputStream os = new DataOutputStream(httpURLConnection.getOutputStream());
            os.writeBytes(postData.toString());
            os.flush();

            InputStream stream = httpURLConnection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);

            }

            os.close();
            stream.close();
            result = buffer.toString();
            objReturn = new JSONObject(result);

            //ConstValue.setResponse(String.valueOf(httpURLConnection.getResponseCode()));

            Log.i("STATUS", String.valueOf(httpURLConnection.getResponseCode()));
            Log.i("MSG" , httpURLConnection.getResponseMessage());

            httpURLConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objReturn;

    }

    public String getJson(String url){
        JSONObject jsonResult= null;
        String result="";

        HttpURLConnection httpURLConnection = null;
        try {
            URL u = new URL(url);
            httpURLConnection = (HttpURLConnection) u.openConnection();
            httpURLConnection.setRequestMethod("GET");
            //httpURLConnection.setRequestProperty("Authorization", ""); /** Aqui va al token **/
            httpURLConnection.setRequestProperty("Content-length", "0");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setAllowUserInteraction(false);
            httpURLConnection.connect();
            int status = httpURLConnection.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();

            result = sb.toString();
            //jsonResult = new JSONObject(result);


        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    public void putJson(String url,JSONObject putData) {
        BufferedReader reader = null;
        JSONObject objReturn = new JSONObject();
        HttpURLConnection httpURLConnection =null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setRequestMethod("PUT");
            //httpURLConnection.setRequestProperty("Authorization", ""); /** Aqui va al token **/
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept","application/json");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            httpURLConnection.connect();

            DataOutputStream os = new DataOutputStream(httpURLConnection.getOutputStream());
            os.writeBytes(putData.toString());
            os.flush();

            InputStream stream = httpURLConnection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);

            }

            os.close();
            stream.close();
            String json = buffer.toString();
            objReturn = new JSONObject(json);

            //ConstValue.setResponse(String.valueOf(httpURLConnection.getResponseCode()));

            Log.i("STATUS", String.valueOf(httpURLConnection.getResponseCode()));
            Log.i("MSG" , httpURLConnection.getResponseMessage());

            httpURLConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void sendWorkPostRequest(final Context context, JSONObject json, String URL) {

        final RequestQueue requestQueue;
        try {
            requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonBody = json;
            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("VOLLEY SUCCESS", response.toString());
                    Toast.makeText(context,"Acción realizada con éxito",Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("VOLLEY ERROR", error.toString());
                    //Log.i("VOLLEY ERROR", ""+error.networkResponse.statusCode);
                    //Toast.makeText(context,"La acción fallo intente mas tarde",Toast.LENGTH_LONG).show();
                    //Toast.makeText(context,"error",Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    SharedPreferences sharedPref = context.getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
                    headers.put("Authorization", "token " + sharedPref.getString("token",""));//put your token here
                    return headers;
                }
            };
            requestQueue.add(jsonOblect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();
    }

    public static void sendAlert(final Context context, JSONObject json, String URL) {

        final RequestQueue requestQueue;
        try {
            requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonBody = json;
            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("VOLLEY SUCCESS", response.toString());
                    Toast.makeText(context,"Alerta Generada con Exito",Toast.LENGTH_LONG).show();
                    //Utils.addNotificationAlert(context,"user");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("VOLLEY ERROR", error.toString());
                    Toast.makeText(context,"Alerta no Generada",Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    SharedPreferences sharedPref = context.getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
                    headers.put("Authorization", "token " + sharedPref.getString("token",""));//put your token here
                    return headers;
                }
            };
            requestQueue.add(jsonOblect);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();
    }

    public static void sendUser(final Context context, Activity activity, JSONObject json, String URL) {

        final RequestQueue requestQueue;
        try {
            requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonBody = json;
            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("VOLLEY SUCCESS", response.toString());
                    Intent intent = new Intent(activity, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
                    //context.finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("VOLLEY ERROR", error.toString());
                    Toast.makeText(context,"error",Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    //SharedPreferences sharedPref = context.getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
                    //headers.put("Authorization", "token " + sharedPref.getString("token",""));//put your token here
                    return headers;
                }
            };
            requestQueue.add(jsonOblect);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();

    }

    public String getJsonCustomer(String url,String token){
        JSONObject jsonResult= null;
        String result="";
        HttpURLConnection httpURLConnection = null;
        try {
            URL u = new URL(url);
            httpURLConnection = (HttpURLConnection) u.openConnection();
            httpURLConnection.setRequestMethod("GET");

            if(!token.equals("default")){
                httpURLConnection.setRequestProperty("Authorization", "token "+token); /** Aqui va al token **/
            }

            httpURLConnection.setRequestProperty("Content-length", "0");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setAllowUserInteraction(false);
            httpURLConnection.connect();
            //int status = httpURLConnection.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();

            result = sb.toString();

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }
}