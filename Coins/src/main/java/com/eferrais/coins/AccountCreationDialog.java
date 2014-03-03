package com.eferrais.coins;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.eferrais.api.manager.CryptoManager;
import com.eferrais.api.model.Account;

/**
 * Created by elodieferrais on 3/2/14.
 */
public class AccountCreationDialog {
    private Context context;
    private EditText addressEditText;
    private NumberPicker coinsTypePicker;
    private AlertDialog dialog;
    private CryptoManager.CRYPTO_TYPE[] coinsType;
    private String[] coinsTypeValue;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public AccountCreationDialog(Context context) {
        this.context = context;
        sharedPreferencesHelper = new SharedPreferencesHelper(context);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_new_account, null);
        addressEditText = (EditText) layout.findViewById(R.id.dialog_new_account_address_edtitext);
        coinsTypePicker = (NumberPicker) layout.findViewById(R.id.dialog_new_account_type_picker);
        coinsTypePicker.setMaxValue(getCoinsTypeValue().length - 1);
        coinsTypePicker.setMinValue(0);
        coinsTypePicker.setDisplayedValues(getCoinsTypeValue());
        //np.setWrapSelectorWheel(false);

        //Building dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                saveNewAccount();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
    }

    private void saveNewAccount() {
        if (addressEditText.getText() != null && !addressEditText.getText().toString().trim().isEmpty()) {
            Account account = new Account(addressEditText.getText().toString(), null, getCoinsType()[coinsTypePicker.getValue()]);
            if (sharedPreferencesHelper.isAlreadySaved(account.address)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Already saved");
                builder.setMessage("You cannot save two times the same address account");
                builder.create().show();
            } else {
                sharedPreferencesHelper.addAccount(account);
            }
        }
    }

    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    private String[] getCoinsTypeValue() {
        if (coinsTypeValue != null) {
            return coinsTypeValue;
        }

        if (coinsType == null) {
            coinsType = CryptoManager.CRYPTO_TYPE.values();
        }
        coinsTypeValue = new String[coinsType.length];
        for (int j = 0; j < coinsType.length; j++) {
            coinsTypeValue[j] = coinsType[j].getLabel();
        }
        return coinsTypeValue;

    }

    private CryptoManager.CRYPTO_TYPE[] getCoinsType() {
        if (coinsType != null) {
            return coinsType;
        }
        coinsType = CryptoManager.CRYPTO_TYPE.values();
        return coinsType;
    }
}
