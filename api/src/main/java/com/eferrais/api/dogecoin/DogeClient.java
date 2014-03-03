package com.eferrais.api.dogecoin;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eferrais.api.baseclient.BaseCryptoClient;
import com.eferrais.api.baseclient.CryptoCallback;
import com.eferrais.api.baseclient.CryptoClientInterface;
import com.eferrais.api.baseclient.MyVolley;
import com.eferrais.api.manager.CryptoManager;
import com.eferrais.api.model.Account;

/**
 * Created by elodieferrais on 3/1/14.
 */
public class DogeClient extends BaseCryptoClient implements CryptoClientInterface {
    public DogeClient(Context context) {
        super(context);
    }

    @Override
    public void getBalance(final String address, final CryptoCallback<Account> callback) {
        String url = "https://dogechain.info/chain/Dogecoin/q/addressbalance/" + address;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result == null) {
                    callback.onResult(null, null);
                } else {
                    try {
                        Double balance = Double.valueOf(result);
                        Account account = new Account(address, balance, CryptoManager.CRYPTO_TYPE.DOGECOIN);
                        callback.onResult(account, null);
                    } catch (Exception e) {
                        callback.onResult(null, new Error(e.getMessage()));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onResult(null, new Error(volleyError.getMessage()));
            }
        });
        MyVolley.getRequestQueue(context).add(request);
    }
}
