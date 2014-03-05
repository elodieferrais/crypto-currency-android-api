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
public class AccountsManager {
    private static ArrayList<Account> accounts = new ArrayList<Account>();
    private static boolean hasPreferencesChanged = true;
    private static SharedPreferences sharedPreferences;
    private String MY_PREFERENCES = "com.eferrais.coins:PREFERENCES";
    private String MY_PREFERENCES_ACOUNTS = "com.eferrais.coins:PREFERENCES:ACOUNTS";

    public AccountsManager(Context context) {
        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, context.MODE_PRIVATE);
    }

    /**
     * Reset old data on disk and those accounts instead
     * @param accounts
     */
    public void saveAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
        commit();
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
                Log.e(AccountsManager.class.getName(), "The following accounts have not been de-serialized: " + accountsSerialized, e);
            } catch (ClassNotFoundException e) {
                Log.e(AccountsManager.class.getName(), "The following accounts have not been de-serialized: " + accountsSerialized, e);

            }
        }
        return accounts;
    }

    ;

    public void addAccount(Account account) {
        if (isAlreadySaved(account.address)) {
            return;
        }
        getAccounts().add(account);
        saveAccounts(accounts);
    }

    /**
     *
     * @param address
     * @return true if this account exist otherwise return false;
     */
    public boolean isAlreadySaved(String address) {
        for (Account account : getAccounts()) {
            if (account.address.equals(address)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param address
     * @return account with this specific address if it exists, otherwise return null;
     */
    public Account getAccount(String address) {
        for(int i = 0; i < getAccounts().size(); i++) {
            Account account = getAccounts().get(i);
            if (address.equals(account.address)) {
                return account;
            }
        }
        return null;
    }

    /**
     * Save on the disk the accounts that as been updates in the account manager so far
     */
    public void commit() {
        try {
            sharedPreferences.edit().putString(MY_PREFERENCES_ACOUNTS, SerializableHelper.toString(accounts)).commit();
        } catch (IOException e) {
            Log.e(AccountsManager.class.getName(), "The following accounts have not been saved: " + accounts.toString(), e);
        }
        hasPreferencesChanged = true;
    }
}
