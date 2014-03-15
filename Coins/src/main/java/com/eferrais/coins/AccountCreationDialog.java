package com.eferrais.coins;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.eferrais.api.converstionrate.CryptoType;
import com.eferrais.api.model.Account;

/**
 * Created by elodieferrais on 3/2/14.
 */
public class AccountCreationDialog {
    private Context context;
    private EditText addressEditText;
    private NumberPicker coinsTypePicker;
    private AlertDialog dialog;
    private CryptoType[] coinsType;
    private String[] coinsTypeValue;
    private AccountsManager accountsManager;
    private AccountCreationDialogListener listener;

    public AccountCreationDialog(Context context) {
        this.context = context;
        accountsManager = new AccountsManager(context);
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
        coinsTypePicker.setValue(1);

        //np.setWrapSelectorWheel(false);

        //Building dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (addressEditText.getText() != null && !addressEditText.getText().toString().trim().isEmpty()) {
                    dialog.dismiss();
                    Account account = new Account(addressEditText.getText().toString(), null, getCoinsType()[coinsTypePicker.getValue()]);
                    saveNewAccount(account);
                    if (listener != null) {
                        listener.onSuccessAccountCreation(accountsManager.getAccounts());
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                listener.onCancelAccountCreation();
            }
        });
        dialog = builder.create();
    }

    public void setAccountCreationDialogListener(AccountCreationDialogListener listener) {
        this.listener = listener;
    }

    private void saveNewAccount(Account account) {
        if (accountsManager.isAlreadySaved(account.address)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Already saved");
            builder.setMessage("You cannot save two times the same address account");
            builder.create().show();
        } else {
            accountsManager.addAccount(account);
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
            coinsType = CryptoType.values();
        }
        coinsTypeValue = new String[coinsType.length];
        for (int j = 0; j < coinsType.length; j++) {
            coinsTypeValue[j] = coinsType[j].getLabel();
        }
        return coinsTypeValue;

    }

    private CryptoType[] getCoinsType() {
        if (coinsType != null) {
            return coinsType;
        }
        coinsType = CryptoType.values();
        return coinsType;
    }

    public void setPrefilledAddressValue(String address) {
        addressEditText.setText(address);
    }
}
