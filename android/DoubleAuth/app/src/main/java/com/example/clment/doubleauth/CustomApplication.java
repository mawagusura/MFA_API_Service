package com.example.clment.doubleauth;

import android.app.Application;

import com.github.orangegangsters.lollipin.lib.managers.LockManager;

public class CustomApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, CustomPinActivity.class);
        lockManager.getAppLock().setLogoId(R.drawable.security_lock);
    }
}
