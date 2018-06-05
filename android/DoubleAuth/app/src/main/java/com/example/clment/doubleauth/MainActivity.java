package com.example.clment.doubleauth;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.orangegangsters.lollipin.lib.PinActivity;
import com.github.orangegangsters.lollipin.lib.managers.AppLock;



public class MainActivity extends PinActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_ENABLE = 11;
    static String API_URL = "https://api-adresse.data.gouv.fr/search/?q=8+bd+du+port";


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
        this.askPasswordToAPI();

        CustomApplication customApplication = new CustomApplication();
        customApplication.setPassword();


    }


    public void askPasswordToAPI(){
        ConnectionManager.getContext(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.e("tag",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag","erreur");
            }
        });

        ConnectionManager.queue.add(stringRequest);
    }

}
