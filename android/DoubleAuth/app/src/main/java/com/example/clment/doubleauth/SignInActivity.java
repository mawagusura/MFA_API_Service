package com.example.clment.doubleauth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by Clément on 28/05/2018.
 */

public class SignInActivity extends Activity implements View.OnClickListener{

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

    private void getDBPassword(){
    }

    private void checkPassword(){
        String username=((EditText)this.findViewById(R.id.username)).getText().toString();
        String password=((EditText)this.findViewById(R.id.password)).getText().toString();
    }

}
