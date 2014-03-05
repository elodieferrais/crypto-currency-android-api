package com.eferrais.api.baseclient;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.eferrais.api.converstionrate.ChangeRateCallback;
import com.eferrais.api.converstionrate.CryptoType;

import org.json.JSONObject;

/**
 * Created by elodieferrais on 3/2/14.
 */
public class CryptoChangeClient {
    private Context context;

    public CryptoChangeClient(android.content.Context context) {
        this.context = context;
    }

    public void getChangeRateToBTC(final CryptoType fromCoin, final ChangeRateCallback callback) {
        if (fromCoin.getUrl() == null) {
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, fromCoin.getUrl(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && jsonObject.has("avg")) {
                    callback.onResult(jsonObject.optDouble("avg"));
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

    public void getChangeRateBTCToUSD(final ChangeRateCallback callback) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://coinbase.com/api/v1/prices/buy", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Double value = null;
                if (jsonObject != null && jsonObject.has("subtotal")) {
                    JSONObject subtotal = jsonObject.optJSONObject("subtotal");
                    if (subtotal != null && subtotal.has("amount")) {
                        value = subtotal.optDouble("amount");
                    }
                }
                callback.onResult(value);

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
