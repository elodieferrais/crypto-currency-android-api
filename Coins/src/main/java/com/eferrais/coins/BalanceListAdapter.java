package com.eferrais.coins;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eferrais.api.converstionrate.ChangeRateCallback;
import com.eferrais.api.model.Account;

import java.util.List;

/**
 * Created by elodieferrais on 3/1/14.
 */
public class BalanceListAdapter extends BaseAdapter {

    private Context context;
    private int resourceId;
    private LayoutInflater inflater;
    final private AccountsManager accountsManager;


    public BalanceListAdapter(Context context, int resource) {
        this.context = context;
        this.resourceId = resource;
        accountsManager = new AccountsManager(context);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return accountsManager.getAccounts().size();
    }

    @Override
    public Account getItem(int position) {
        return accountsManager.getAccounts().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }
        final TextView address = (TextView) view.findViewById(R.id.cell_account_address_textview);
        final TextView type = (TextView) view.findViewById(R.id.cell_account_type_textview);
        final TextView balance = (TextView) view.findViewById(R.id.cell_account_balance_textview);
        final TextView dollars = (TextView) view.findViewById(R.id.cell_account_dollars_textview);
        Account account = getItem(position);

        address.setText(account.address);
        type.setText(account.coinType.getLabel());
        balance.setText(String.valueOf(account.balance));
        Log.d("ELODIE", "UI before getting change rate: account balance:" + String.valueOf(account.balance));
        account.coinType.getChangeRateToBTC(new ChangeRateCallback() {
            @Override
            public void onResult(Double result) {
                Log.d("ELODIE", "UI: account balance:" + String.valueOf(getItem(position).balance));
                Log.d("ELODIE", "UI: account result:" + String.valueOf(result));

                Double valueInBTC = result*getItem(position).balance;
                if (!valueInBTC.equals(Double.NaN)) {
                    dollars.setText(String.valueOf(result*getItem(position).balance));
                }
            }
        }, context);

        return view;
    }
}
