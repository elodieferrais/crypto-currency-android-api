package com.eferrais.api.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.eferrais.api.converstionrate.CryptoType;

import java.io.IOException;

/**
 * Created by elodieferrais on 3/4/14.
 */
public class SharedPreferencesCryptoHelper {
    private static SharedPreferences sharedPreferences;
    private String MY_PREFERENCES = "com.eferrais.api:PREFERENCES";
    private String MY_PREFERENCES_CRYPTO = "com.eferrais.api:PREFERENCES:CRYPTO";

    public SharedPreferencesCryptoHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, context.MODE_PRIVATE);
    }

    public void saveCrypto(CryptoType cryptoType) {
        try {
            sharedPreferences.edit().putString(MY_PREFERENCES_CRYPTO, SerializableHelper.toString(cryptoType)).commit();
        } catch (IOException e) {
            Log.e(SharedPreferencesCryptoHelper.class.getName(), "The following crypto have not been saved: " + cryptoType.getLabel(), e);
        }
    }

    public void updateCryptos() {
            String cryptoSerialized = sharedPreferences.getString(MY_PREFERENCES_CRYPTO, null);
            try {
                if (cryptoSerialized != null) {
                    SerializableHelper.fromString(cryptoSerialized);
                }
                /**for(CryptoType crypo:CryptoType.values()) {
                    crypo.setChangeRateToBTC(cryptoType.getChangeRateToBTC());
                }

                CryptoType.BITCOIN.setChangeRateToBTC(cryptoType.getChangeRateToBTC());**/

            } catch (IOException e) {
                Log.e(SharedPreferencesCryptoHelper.class.getName(), "The following crypto type have not been de-serialized: " + cryptoSerialized, e);
            } catch (ClassNotFoundException e) {
                Log.e(SharedPreferencesCryptoHelper.class.getName(), "The following crypto type have not been de-serialized: " + cryptoSerialized, e);

            }

    }
}
