package com.example.clment.doubleauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.orangegangsters.lollipin.lib.managers.AppLock;
import com.github.orangegangsters.lollipin.lib.managers.AppLockActivity;
import com.github.orangegangsters.lollipin.lib.managers.LockManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Clément on 28/05/2018.
 */

public class SignInActivity extends Activity implements View.OnClickListener{

    private String url = "http://authenticator-efrei.azurewebsites.net/api/auth/token";
    //private String url="http://httpbin.org/post";
    //private String url="http://localhost:8080/api/users/token";

    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        this.findViewById(R.id.validation).setOnClickListener(this);


    }

    /*private String getMACaddr(){
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();

        Log.d("MAC",address);
        return address;
    }*/

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.validation:
                this.checkPassword();
                break;
        }
    }

    public void launchNewActivity(){
        Intent intent2 = new Intent(SignInActivity.this, MainActivity.class);
        intent2.putExtra("token",token);
        startActivity(intent2);

    }

    private void checkPassword(){
        String username=((EditText)this.findViewById(R.id.username)).getText().toString();
        String password=((EditText)this.findViewById(R.id.password)).getText().toString();


        this.makeJsonObjReq(username,password);
    }

    
    private void makeJsonObjReq(String username,String password) {
        RequestQueue queue = Volley.newRequestQueue(this);


        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("usernameOrEmail", username);
        postParam.put("password", password);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            token=(String)response.getString("accessToken");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", response.toString());
                        launchNewActivity();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response","requete d'authentification user");
                Log.d("Error.Response", error.toString());
                Toast.makeText(getApplicationContext(), "Erreur d'authentification", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders(){
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }



        };
        queue.add(jsonObjReq);

        }


}
