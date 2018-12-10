package com.myweb.final_bnk48;

import android.app.Activity;
import android.os.Build;
import android.os.StrictMode;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MySQLConnect {
    private final Activity main;
    private List<String> list;
    private String URL ="http://192.168.2.40/" , GET_URL = "BNK48/get_post.php" , SENT_URL = "BNK48/sent_post.php";

    public MySQLConnect() {
        main = null;
    }

    public MySQLConnect(Activity mainA){
        main = mainA;
        list = new ArrayList<String>();
    }

    public List<String> getData() {
        String url = URL + GET_URL;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
                //Toast.makeText(main, list.get(0), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(main, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(main.getApplicationContext());
        requestQueue.add(stringRequest);

        return list;
    }

    public void showJSON(String response){
        String ticket_id = "";
        String name = "";
        String gender = "";
        String date = "";
        String ticket_amount = "";
        String ticket_price = "";
        String ticket_total = "";


        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i=0; i<result.length(); i++){
                JSONObject collectData = result.getJSONObject(i);
                ticket_id = collectData.getString("ticket_id");
                name = collectData.getString("name");
                gender = collectData.getString("gender");
                date = collectData.getString("date");
                ticket_amount = collectData.getString("ticket_amount");
                ticket_price = collectData.getString("ticket_price");
                ticket_total = collectData.getString("ticket_total");
                //แบบรวมitemเดียว
                list.add(ticket_id+"\n"+name+"\n"+gender+"\n"+date+"\n"+
                        ticket_amount+"\n"+ticket_price+"\n"+ticket_total);
                //แบบแยกitemเ
                /*
                list.add(ticket_id);
                list.add(name);
                list.add(gender);
                list.add(date);
                list.add(ticket_amount);
                list.add(ticket_price);
                list.add(ticket_total);*/
            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void sentData(String name, String gender, String date, String ticket_amount, String ticket_price, String ticket_total){
        StrictMode.enableDefaults();
        if (Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("name", name));
            nameValuePairs.add(new BasicNameValuePair("gender", gender));
            nameValuePairs.add(new BasicNameValuePair("date", date));
            nameValuePairs.add(new BasicNameValuePair("ticket_amount", ticket_amount));
            nameValuePairs.add(new BasicNameValuePair("ticket_price", ticket_price));
            nameValuePairs.add(new BasicNameValuePair("ticket_total", ticket_total));
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL + SENT_URL);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            httpClient.execute(httpPost);

            Toast.makeText(main,"Completed.", Toast.LENGTH_LONG).show();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }catch (ClientProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
