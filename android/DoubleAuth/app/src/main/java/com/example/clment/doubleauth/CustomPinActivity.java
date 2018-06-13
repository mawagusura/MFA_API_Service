package com.example.clment.doubleauth;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;

import com.github.orangegangsters.lollipin.lib.managers.AppLockActivity;



public class CustomPinActivity extends AppLockActivity {



    @Override
    public void showForgotDialog() {

    }

    @Override
    public void onPinFailure(int attempts) {

    }

    @Override
    public void onPinSuccess(int attempts) {
        Log.d("Reponse",this.mPinCode);
        Log.e("etape","authentification réussie");
        Intent intent = new Intent();
        intent.putExtra("keyName", this.mPinCode);
        setResult(RESULT_OK, intent);
    }

    @Override
    public int getPinLength() {
        return 4;

    }



}
