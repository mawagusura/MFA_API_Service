package com.example.clment.doubleauth;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.orangegangsters.lollipin.lib.managers.LockManager;

import org.json.JSONObject;

public class CustomApplication extends Application {

    ApiQuery apiQuery;
    private RequestQueue mRequestQueue;




    public CustomApplication(){

        this.apiQuery=new ApiQuery();
    }


    @Override
    public void onCreate() {

        super.onCreate();

        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, CustomPinActivity.class);
        lockManager.getAppLock().setLogoId(R.drawable.security_lock);
    }

    /**
     * Permet de définir le mot de passe que l'utilisateur doit enregisrer
     */
    public void setPassword(){
        Log.e("etape","on set le password");

        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, CustomPinActivity.class);

        lockManager.getAppLock().setPasscode(apiQuery.getPassword());


    }
}
