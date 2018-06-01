package com.example.clment.doubleauth;

import android.app.Application;
import android.util.Log;

import com.github.orangegangsters.lollipin.lib.managers.LockManager;

public class CustomApplication extends Application {



    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("init","customOnCreate");
        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, CustomPinActivity.class);
        lockManager.getAppLock().setLogoId(R.drawable.security_lock);
    }

    /**
     * Permet de définir le mot de passe que l'utilisateur doit enregisrer
     */
    public void setPassword(){
        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, CustomPinActivity.class);
        lockManager.getAppLock().setPasscode("8888");
        Log.e("0","setPass");
    }
}
