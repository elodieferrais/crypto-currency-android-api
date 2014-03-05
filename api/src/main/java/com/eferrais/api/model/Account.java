package com.eferrais.api.model;

import com.eferrais.api.converstionrate.CryptoType;

import java.io.Serializable;

/**
 * Created by elodieferrais on 3/1/14.
 */
public class Account implements Serializable {
    final public String address;
    public Double balance = Double.NaN;
    final public CryptoType coinType;

    public Account(String address, Double balance, CryptoType coinType) {
        this.address = address;
        setBalance(balance);
        this.coinType = coinType;
    }

    public void setBalance(Double balance) {
        if (balance != null) {
            this.balance = balance;
        }
    }
}
