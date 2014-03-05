package com.eferrais.api.baseclient;

import android.content.Context;

import com.eferrais.api.converstionrate.CryptoType;
import com.eferrais.api.model.Account;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by elodieferrais on 3/1/14.
 */
public class CryptoAddressClient {

    private Context context;

    public CryptoAddressClient(Context context) {
        this.context = context;
    }

    public void getBalance(String address, CryptoType type, CryptoCallback<Account> callback) {
        Class coinClass = type.getCoinClass();
        Constructor<?> ctor;
        try {
            ctor = coinClass.getConstructor(Context.class);
            CryptoClientInterface coinClient = (CryptoClientInterface) ctor.newInstance(new Object[]{this.context});
            coinClient.getBalance(address, callback);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
