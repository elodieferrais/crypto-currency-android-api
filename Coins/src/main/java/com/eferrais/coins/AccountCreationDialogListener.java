package com.eferrais.coins;

import com.eferrais.api.model.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elodieferrais on 3/3/14.
 */
public interface AccountCreationDialogListener {
    public void onSuccessAccountCreation(List<Account> account);
    public void onCancelAccountCreation();
}
