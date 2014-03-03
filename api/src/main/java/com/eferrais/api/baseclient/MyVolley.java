package com.eferrais.api.baseclient;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by elodieferrais on 3/1/14.
 */
public class MyVolley {
    private static RequestQueue requestQueue;

    private MyVolley() {
    }

    public static RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }
}

