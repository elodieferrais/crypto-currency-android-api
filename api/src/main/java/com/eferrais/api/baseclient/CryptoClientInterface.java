package com.eferrais.api.baseclient;

import com.eferrais.api.model.Account;

/**
 * Created by elodieferrais on 3/1/14.
 */
public interface CryptoClientInterface {
    public void getBalance(final String address, final CryptoCallback<Account> callback);
}
