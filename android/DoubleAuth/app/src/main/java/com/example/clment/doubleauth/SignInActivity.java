package com.example.clment.doubleauth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
                startActivity(intent);
                break;
        }
    }

    private void getDBPassword(){
    }
}
