package com.eferrais.api.baseclient;

/**
 * Created by elodieferrais on 3/1/14.
 */
public interface CryptoCallback<T> {
    public void onResult(T result, Error error);
}
