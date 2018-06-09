package com.example.clment.doubleauth;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.orangegangsters.lollipin.lib.PinActivity;
import com.github.orangegangsters.lollipin.lib.managers.AppLock;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends PinActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_ENABLE = 11;
    private String url="http://httpbin.org/post";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getPassword();//Demande à l'utilisateur de rentrer son MDP

        this.findViewById(R.id.button_enable_pin).setOnClickListener(this);
        this.findViewById(R.id.button_change_pin).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, CustomPinActivity.class);

        switch (v.getId()) {
            case R.id.button_enable_pin://Creation du code Pin
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
                startActivityForResult(intent, REQUEST_CODE_ENABLE);
                break;
            case R.id.button_change_pin:// changement du code pin
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.CHANGE_PIN);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_ENABLE:
                //affichage d'un msg pop up
                Toast.makeText(this, "PinCode enabled", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void getPassword(){
        if(this.isTokenValid("token")) {
            String mdp = this.askAPIForPassword("token");
            CustomApplication customApplication = new CustomApplication();
            customApplication.setPassword(mdp);
        }
        else{
            this.finish();
        }


    }

    private String askAPIForPassword(String token){
        RequestQueue queue = Volley.newRequestQueue(this);


        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("token", token);

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

        return"9999"; //a changer
    }

    private boolean isTokenValid(String token){
        return true;
    }


}
