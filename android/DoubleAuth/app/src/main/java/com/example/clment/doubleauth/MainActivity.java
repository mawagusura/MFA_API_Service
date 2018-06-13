package com.example.clment.doubleauth;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.orangegangsters.lollipin.lib.PinActivity;
import com.github.orangegangsters.lollipin.lib.managers.AppLock;
import com.github.orangegangsters.lollipin.lib.managers.LockManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final int REQUEST_CODE_ENABLE = 11;
    //private String url="http://httpbin.org/post";
    private String url="http://authenticator-efrei.azurewebsites.net/api/";
    private String websiteClicked="";
    public String token="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listview = (ListView) findViewById(R.id.listview);
        listview.setOnItemClickListener(this);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        //appel de l'API pour remplir la listView
        this.askAPIForWebsites();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("Response",(String) parent.getItemAtPosition(position));
        websiteClicked=(String) parent.getItemAtPosition(position);
        //demande du codePin
        Intent intent2 = new Intent(MainActivity.this, CustomPinActivity.class);
        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.getAppLock().addIgnoredActivity(CustomPinActivity.class);
        intent2.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
        startActivityForResult(intent2, REQUEST_CODE_ENABLE);
    }

    public void fillListView(String[] sites){
        ListView listview = (ListView) findViewById(R.id.listview);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,sites);
        listview.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Reponse","retour");
        String pinCode = data.getStringExtra("keyName");
        Log.d("Reponse",pinCode);
        this.sendSecondAuth(websiteClicked,pinCode);
    }

    private void askAPIForWebsites(){
        String urlWebsite=url+"users/websites/action-required";
        //String urlWebsite="http://httpbin.org/get?param1=hello";
        RequestQueue queue = Volley.newRequestQueue(this);


        Map<String, String> postParam= new HashMap<String, String>();


        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                urlWebsite, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response","success");
                        Log.d("Response", response.toString());
                        String[] sites=new String[response.length()];
                        try {
                            for(int i=0;i<response.length();i++) {
                                sites[i]=response.optJSONObject(i).getString("url");
                            }
                        } catch (JSONException e) {
                                e.printStackTrace();
                        }

                        fillListView(sites);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response","requete recuperation websites");
                Log.d("Error.Response", error.toString());
                Log.d("Error.Response", ""+error.networkResponse.statusCode);
                Toast.makeText(getApplicationContext(), "Récupération des sites impossible", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders(){
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization","Bearer "+token);
                return headers;
            }



        };
        queue.add(jsonObjReq);
    }

    private void sendSecondAuth(String website,String pinCode){
        String urlAuth=url+"users/websites/validate";

        RequestQueue queue = Volley.newRequestQueue(this);


        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("url", website);
        postParam.put("pinCode", pinCode);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlAuth, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Response", response.toString());
                        askAPIForWebsites();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response","validate le site");
                Log.d("Error.Response", error.toString());
                Log.d("Error.Response", ""+error.networkResponse.statusCode);
                Toast.makeText(getApplicationContext(), "Erreur d'authentification via pin code", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders(){
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization","Bearer "+token);
                return headers;
            }



        };
        queue.add(jsonObjReq);
    }


}
