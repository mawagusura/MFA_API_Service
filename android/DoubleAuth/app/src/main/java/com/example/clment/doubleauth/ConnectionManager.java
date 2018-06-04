package com.example.clment.doubleauth;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Clément on 04/06/2018.
 */

public class ConnectionManager {
    static RequestQueue queue;


    public static RequestQueue getContext(Context context) {

        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        return queue;
    }


}
