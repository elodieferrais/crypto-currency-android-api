package com.eferrais.api.litecoin;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.eferrais.api.baseclient.BaseCryptoClient;
import com.eferrais.api.baseclient.CryptoCallback;
import com.eferrais.api.baseclient.CryptoClientInterface;
import com.eferrais.api.baseclient.MyVolley;
import com.eferrais.api.manager.CryptoManager;
import com.eferrais.api.model.Account;

import org.json.JSONObject;

/**
 * Created by elodieferrais on 3/1/14.
 */
public class LitecoinClient extends BaseCryptoClient implements CryptoClientInterface {
    public LitecoinClient(Context context) {
        super(context);
    }

    @Override
    public void getBalance(final String address, final CryptoCallback<Account> callback) {
        String url = "http://ltc.blockr.io/api/v1/address/info/" + address;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject == null) {
                    callback.onResult(null, null);
                } else {
                    Double balance = null;
                    if (jsonObject.has("data")) {
                        JSONObject data = jsonObject.optJSONObject("data");
                        if (data != null && data.has("balance")) {
                            balance = data.optDouble("balance");
                        }
                    }
                    Account account = new Account(address, balance, CryptoManager.CRYPTO_TYPE.LITECOIN);
                    callback.onResult(account, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onResult(null, new Error(volleyError.getMessage()));
            }
        }
        );
        MyVolley.getRequestQueue(context).add(request);
    }
}
