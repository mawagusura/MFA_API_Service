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

import java.util.HashMap;
import java.util.Map;

public class CustomApplication extends Application {

    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {

        super.onCreate();

        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, CustomPinActivity.class);
        lockManager.getAppLock().setLogoId(R.drawable.security_lock);

    }



}
