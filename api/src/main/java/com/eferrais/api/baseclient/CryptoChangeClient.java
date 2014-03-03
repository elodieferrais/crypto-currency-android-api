package com.eferrais.api.baseclient;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.eferrais.api.manager.CryptoManager;

import org.json.JSONObject;

/**
 * Created by elodieferrais on 3/2/14.
 */
public class CryptoChangeClient {
    private Context context;

    public CryptoChangeClient(android.content.Context context) {
        this.context = context;
    }

    public void getChangeRateToBTC(final CryptoManager.CRYPTO_TYPE fromCoin, final Callback callback) {
        if (fromCoin.getUrl() == null) {
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, fromCoin.getUrl(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && jsonObject.has("avg")) {
                    fromCoin.setChangeRateToBTC(jsonObject.optDouble("avg"));
                    callback.onResult(true);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(CryptoChangeClient.class.getName(), volleyError.getMessage(),volleyError);
            }
        });
        MyVolley.getRequestQueue(context).add(request);
    }
}
