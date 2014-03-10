package com.eferrais.coins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applidium.headerlistview.SectionAdapter;
import com.eferrais.api.converstionrate.ChangeRateCallback;
import com.eferrais.api.converstionrate.CryptoType;
import com.eferrais.api.model.Account;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by elodieferrais on 3/1/14.
 */
public class BalanceListAdapter extends SectionAdapter {

    private Context context;
    private int resourceId;
    private LayoutInflater inflater;
    final private AccountsManager accountsManager;
    private List<List<Account>> accountsByCryptoList = new ArrayList<List<Account>>();


    public BalanceListAdapter(Context context, int resource) {
        this.context = context;
        this.resourceId = resource;
        accountsManager = new AccountsManager(context);
        sortAccountByCrypto();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int numberOfSections() {
        return accountsByCryptoList.size();
    }

    @Override
    public int numberOfRows(int i) {
        return accountsByCryptoList.get(i).size();
    }

    @Override
    public boolean hasSectionHeaderView(int section) {
        return true;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.cell_account_section, parent, false);
        } else {
            view = convertView;
        }

        final TextView type = (TextView) view.findViewById(android.R.id.text1);
        final TextView value = (TextView) view.findViewById(android.R.id.text2);

        final CryptoType crypto = getRowItem(section, 0).coinType;
        type.setText(crypto.getLabel());
        crypto.getChangeRateToBTC(new ChangeRateCallback() {
            @Override
            public void onResult(final Double cryptoToBtc) {
                crypto.getChangeRateBTCToUSD(new ChangeRateCallback() {
                    @Override
                    public void onResult(Double btcToUsd) {
                        DecimalFormat df = new DecimalFormat("$ #.##");
                        value.setText(df.format(cryptoToBtc*btcToUsd));
                    }
                }, context);
            }
        }, context);

        return view;
    }

    @Override
    public View getRowView(int section, int row, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }
        final TextView address = (TextView) view.findViewById(R.id.cell_account_address_textview);
        final TextView balance = (TextView) view.findViewById(R.id.cell_account_balance_textview);
        final TextView dollars = (TextView) view.findViewById(R.id.cell_account_dollars_textview);
        final Account account = getRowItem(section, row);


        address.setText((account.address.length() > 20) ? account.address.substring(0, 20) : account.address);
        balance.setText(String.valueOf(account.balance) + " " + account.coinType.getAbbreviation());

        //Amount in dollars
        account.coinType.getChangeRateToBTC(new ChangeRateCallback() {
            @Override
            public void onResult(Double result) {
                final Double valueInBTC = result * account.balance;
                if (!valueInBTC.equals(Double.NaN)) {
                    account.coinType.getChangeRateBTCToUSD(new ChangeRateCallback() {
                        @Override
                        public void onResult(Double result) {
                            DecimalFormat df = new DecimalFormat("#.##");
                            dollars.setText("$ " + df.format(valueInBTC * result));
                        }
                    }, context);

                }
            }
        }, context);

        return view;
    }

    @Override
    public Account getRowItem(int i, int i2) {
        return accountsByCryptoList.get(i).get(i2);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        sortAccountByCrypto();
        super.notifyDataSetChanged();
    }

    private void sortAccountByCrypto() {
        Map<CryptoType, List<Account>> accountsByCryptoMap = new HashMap<CryptoType, List<Account>>();
        ArrayList<Account> accounts = accountsManager.getAccounts();
        for (Account account : accounts) {
            List<Account> accountByCurrentCrypto;
            if (accountsByCryptoMap.containsKey(account.coinType)) {
                accountByCurrentCrypto = accountsByCryptoMap.get(account.coinType);
            } else {
                accountByCurrentCrypto = new ArrayList<Account>();
            }
            accountByCurrentCrypto.add(account);
            accountsByCryptoMap.put(account.coinType, accountByCurrentCrypto);
        }
        accountsByCryptoList.clear();
        accountsByCryptoList.addAll(accountsByCryptoMap.values());
    }
}
