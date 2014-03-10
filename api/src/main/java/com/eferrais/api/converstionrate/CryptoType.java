package com.eferrais.api.converstionrate;

import android.content.Context;

import com.eferrais.api.baseclient.CryptoChangeClient;
import com.eferrais.api.bitcoin.BitcoinClient;
import com.eferrais.api.dogecoin.DogeClient;
import com.eferrais.api.helper.SharedPreferencesCryptoHelper;
import com.eferrais.api.litecoin.LitecoinClient;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by elodieferrais on 3/2/14.
 */

public enum CryptoType implements Serializable {
    BITCOIN(BitcoinClient.class, "Bitcoin", null, "BTC"),
    LITECOIN(LitecoinClient.class, "Litecoin", "http://data.bter.com/api/1/ticker/ltc_btc", "LTC"),
    DOGECOIN(DogeClient.class, "Dogecoin", "http://data.bter.com/api/1/ticker/doge_btc", "DOGE");

    /**
     * speficic client class
     */
    private Class coinClass;
    /**
     * Crypto currency label meaningful for user display
     */
    private String label;
    /**
     * Crypto Unity
     */
    private String abbreviation;
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
     * Last time change rate (crypto to BTC)  has been updated
     */
    private Calendar lastFetchBTC;
    /**
     * Fetch
     * Last time change rate (BTC to USD)  has been updated
     */
    private Calendar lastFetchUSD;
    private CryptoChangeClient client;
    private SharedPreferencesCryptoHelper sharedPreferencesCryptoHelper;
    private boolean hasBeenRestored = false;
    private Double changeRateBTCToUSD = Double.NaN;
    private static final int VALID_INTERVAL = 1000 * 60 * 5;


    CryptoType(Class coinClass, String label, String url, String abbreviation) {
        this.coinClass = coinClass;
        this.label = label;
        this.url = url;
        this.abbreviation = abbreviation;
    }

    public void setChangeRateToBTC(Double change, Context context) {
        synchronized (this) {
            if (!hasBeenRestored) {
                restoreData(context);
            }
        }
        if (change != null && !change.equals(Double.NaN)) {
            this.changeRateToBTC = change;
            lastFetchBTC = Calendar.getInstance();
        }

    }

    public void setChangeRateBTCToUSD(Double change, Context context) {
        synchronized (this) {
            if (!hasBeenRestored) {
                restoreData(context);
            }
        }
        if (change != null && !change.equals(Double.NaN)) {
            this.changeRateBTCToUSD = change;
            lastFetchUSD = Calendar.getInstance();
        }

    }

    public Class getCoinClass() {
        return coinClass;
    }

    public String getLabel() {
        return label;
    }

    public String getUrl() {
        return url;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Asynchronous method which fetch the value from the server if it is obsolete
     *
     * @param callback
     * @param context
     */
    public void getChangeRateToBTC(final ChangeRateCallback callback, final Context context) {
        synchronized (this) {
            if (!hasBeenRestored) {
                restoreData(context);
            }
        }

        if (this.equals(BITCOIN)) {
            changeRateToBTC = 1d;
            callback.onResult(changeRateToBTC);
            return;
        }
        if (changeRateToBTC == Double.NaN || lastFetchBTC == null
                || Calendar.getInstance().getTimeInMillis() - lastFetchBTC.getTimeInMillis() > VALID_INTERVAL) {
            getClient(context).getChangeRateToBTC(this, new ChangeRateCallback() {
                @Override
                public void onResult(Double result) {
                    setChangeRateToBTC(result, context);
                    if (sharedPreferencesCryptoHelper == null) {
                        sharedPreferencesCryptoHelper = new SharedPreferencesCryptoHelper(context);
                    }
                    sharedPreferencesCryptoHelper.saveCrypto(CryptoType.this);
                    callback.onResult(changeRateToBTC);
                }
            });
        } else {
            callback.onResult(changeRateToBTC);
        }
    }


    /**
     * Synchronous method
     *
     * @return the last convertion rate saved
     */
    public Double getChangeRateToBTC() {
        if (this.equals(BITCOIN)) {
            changeRateToBTC = 1d;
        }
        return changeRateToBTC;
    }

    public void getChangeRateBTCToUSD(final ChangeRateCallback callback, final Context context) {
        synchronized (this) {
            if (!hasBeenRestored) {
                restoreData(context);
            }
        }
        if (changeRateBTCToUSD == Double.NaN || lastFetchUSD == null
                || Calendar.getInstance().getTimeInMillis() - lastFetchUSD.getTimeInMillis() > VALID_INTERVAL) {
            getClient(context).getChangeRateBTCToUSD(new ChangeRateCallback() {
                @Override
                public void onResult(Double result) {
                    setChangeRateBTCToUSD(result, context);
                    if (sharedPreferencesCryptoHelper == null) {
                        sharedPreferencesCryptoHelper = new SharedPreferencesCryptoHelper(context);
                    }
                    sharedPreferencesCryptoHelper.saveCrypto(CryptoType.this);
                    callback.onResult(changeRateBTCToUSD);
                }
            });
        } else {
            callback.onResult(changeRateBTCToUSD);
        }
    }

    public Double getChangeRateBTCToUSD() {
        return changeRateBTCToUSD;
    }

    private CryptoChangeClient getClient(Context context) {
        if (client == null) {
            client = new CryptoChangeClient(context);
        }
        return client;
    }
    private void restoreData(Context context) {
        if (sharedPreferencesCryptoHelper == null) {
            sharedPreferencesCryptoHelper = new SharedPreferencesCryptoHelper(context);
        }
        sharedPreferencesCryptoHelper.updateCryptos();
        hasBeenRestored = true;
    }
}

