package com.eferrais.api.manager;

import android.content.Context;
import android.util.Log;

import com.eferrais.api.baseclient.Callback;
import com.eferrais.api.baseclient.CryptoChangeClient;
import com.eferrais.api.bitcoin.BitcoinClient;
import com.eferrais.api.dogecoin.DogeClient;
import com.eferrais.api.litecoin.LitecoinClient;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by elodieferrais on 3/2/14.
 */
public class CryptoManager {

    private Context context;
    private CryptoChangeClient client;

    public enum CRYPTO_TYPE implements Serializable{
        BITCOIN(BitcoinClient.class, "Bitcoin", null),
        LITECOIN(LitecoinClient.class, "Litecoin","http://data.bter.com/api/1/ticker/ltc_btc"),
        DOGECOIN(DogeClient.class, "Dogecoin", "http://data.bter.com/api/1/ticker/doge_btc");

        /**
         * speficic client class
         */
        private Class coinClass;
        /**
         * Crypto currency label meaningful for user display
         */
        private String label;
        /**
         * change is the change rate from the crypto-currency to BTC if it's not BITCOIN
         */
        private Double changeRateToBTC = Double.NaN;
        /**
         * Url used to get the change rate
         */
        private String url;
        /**
         * Fetch
         * Last time change rate has been
         */
        private Date lastFetch;


        CRYPTO_TYPE(Class coinClass, String label, String url) {
            this.coinClass = coinClass;
            this.label = label;
            this.url = url;
        }

        public void setChangeRateToBTC(Double change) {
            this.changeRateToBTC = change;
        }
        public void getChangeRateToUSD() {

        }

        public Class getCoinClass() {
            return coinClass;
        }

        public String getLabel() {
            return label;
        }

        public void getChangeRateToBTC(ChangeRateCallback callback) {
            if (this.equals(BITCOIN)) {
                callback.onResult(1d);
            }
            if (changeRateToBTC == Double.NaN || lastFetch == null ) {
                //TODO
            } else {
                callback.onResult(changeRateToBTC);
            }
        }

        public String getUrl() {
            return url;
        }
    }

    public CryptoManager(Context context) {
        this.context = context;
        this.client = new CryptoChangeClient(this.context);
    }

    public void synchronyze() {
        final CRYPTO_TYPE[] keys = CRYPTO_TYPE.values();
        for (int i = 0; i < keys.length; i++) {
            final CRYPTO_TYPE key = keys[i];
            client.getChangeRateToBTC(key, new Callback() {
                @Override
                public void onResult(boolean success) {
                    Log.d(CryptoManager.class.getName(), "SUCCESS to synchronise the" + key.getLabel() + " change rate");
                }
            });
        }

    }


}
