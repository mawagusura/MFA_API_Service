package com.example.clment.doubleauth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Clément on 28/05/2018.
 */

public class SignInActivity extends Activity implements View.OnClickListener{

    //String url = "http://authenticator-efrei.azurewebsites.net/api/auth/login";
    String url="http://httpbin.org/post";
    //String url="http://localhost:8080/api/auth/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        this.findViewById(R.id.validation).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);

        switch (v.getId()) {
            case R.id.validation:
                this.checkPassword();
                //lancement du MainActivity (demande de code Pin)
                startActivity(intent);
                break;
        }
    }

    private void checkPassword(){
        String username=((EditText)this.findViewById(R.id.username)).getText().toString();
        String password=((EditText)this.findViewById(R.id.password)).getText().toString();

        //this.sendLogsToApi(username,password);
        this.makeJsonObjReq(username,password);
    }

    private void sendLogsToApi(String username,String password){

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("usernameOrEmail", "test");
                params.put("password","testefrei");
                return params;
            }
        };
        queue.add(postRequest);
    }
    
    private void makeJsonObjReq(String username,String password) {
        RequestQueue queue = Volley.newRequestQueue(this);


        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("usernameOrEmail", "test");
        postParam.put("password", "testefrei");


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", "Error: " + error.getMessage());
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
