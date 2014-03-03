package com.eferrais.coins;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.eferrais.api.helper.SerializableHelper;
import com.eferrais.api.model.Account;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by elodieferrais on 3/1/14.
 */
public class SharedPreferencesHelper {
    private static ArrayList<Account> accounts = new ArrayList<Account>();
    private static boolean hasPreferencesChanged = true;
    private static SharedPreferences sharedPreferences;
    private String MY_PREFERENCES = "com.eferrais.api:PREFERENCES";
    private String MY_PREFERENCES_ACOUNTS = "com.eferrais.api:PREFERENCES:ACOUNTS";

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, context.MODE_PRIVATE);
    }

    public void saveAccounts(ArrayList<Account> accounts) {
        try {
            sharedPreferences.edit().putString(MY_PREFERENCES_ACOUNTS, SerializableHelper.toString(accounts)).commit();
        } catch (IOException e) {
            Log.e(SharedPreferencesHelper.class.getName(), "The following accounts have not been saved: " + accounts.toString(), e);
        }
        hasPreferencesChanged = true;
    }

    public ArrayList<Account> getAccounts() {
        if (hasPreferencesChanged) {
            hasPreferencesChanged = false;
            String accountsSerialized = sharedPreferences.getString(MY_PREFERENCES_ACOUNTS, null);
            try {
                if (accountsSerialized == null) {
                    accounts.clear();
                } else {
                    accounts = (ArrayList<Account>) SerializableHelper.fromString(accountsSerialized);
                }
            } catch (IOException e) {
                Log.e(SharedPreferencesHelper.class.getName(), "The following accounts have not been de-serialized: " + accountsSerialized, e);
            } catch (ClassNotFoundException e) {
                Log.e(SharedPreferencesHelper.class.getName(), "The following accounts have not been de-serialized: " + accountsSerialized, e);

            }
        }
        return accounts;
    }

    ;

    public void addAccount(Account account) {
        if (isAlreadySaved(account.address)) {
            return;
        }
        ArrayList<Account> accounts = getAccounts();
        accounts.add(account);
        saveAccounts(accounts);
        hasPreferencesChanged = true;
    }

    public boolean isAlreadySaved(String address) {
        for (Account account : getAccounts()) {
            if (account.address.equals(address)) {
                return true;
            }
        }
        return false;
    }
}
