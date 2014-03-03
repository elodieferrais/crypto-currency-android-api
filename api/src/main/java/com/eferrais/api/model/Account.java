package com.eferrais.api.model;

import com.eferrais.api.manager.CryptoManager;

import java.io.Serializable;

/**
 * Created by elodieferrais on 3/1/14.
 */
public class Account implements Serializable {
    final public String address;
    public Double balance;
    final public CryptoManager.CRYPTO_TYPE coinType;

    public Account(String address, Double balance, CryptoManager.CRYPTO_TYPE coinType) {
        this.address = address;
        this.balance = balance;
        this.coinType = coinType;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
