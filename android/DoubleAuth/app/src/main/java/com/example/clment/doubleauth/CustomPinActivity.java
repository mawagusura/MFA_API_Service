package com.example.clment.doubleauth;

import android.content.res.Resources;
import android.graphics.Typeface;

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

    }

    @Override
    public int getPinLength() {
        return 4;
    }
}
