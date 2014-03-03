package com.eferrais.coins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eferrais.api.model.Account;

import java.util.Iterator;
import java.util.List;

/**
 * Created by elodieferrais on 3/1/14.
 */
public class BalanceListAdapter extends ArrayAdapter<Account> {

    private Context context;
    private int resourceId;
    private LayoutInflater inflater;


    public BalanceListAdapter(Context context, int resource, List<Account> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourceId = resource;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }
        TextView address = (TextView) view.findViewById(R.id.cell_account_address_textview);
        TextView type = (TextView) view.findViewById(R.id.cell_account_type_textview);
        TextView balance = (TextView) view.findViewById(R.id.cell_account_balance_textview);
        TextView dollars = (TextView) view.findViewById(R.id.cell_account_dollars_textview);
        Account account = getItem(position);

        address.setText(account.address);
        type.setText(account.coinType.getLabel());
        balance.setText(String.valueOf(account.balance));

        return view;
    }

    public int getPosition(String address) {
        for(int i = 0; i < getCount(); i++) {
            Account account = getItem(i);
            if (address.equals(account.address)) {
                return i;
            }
        }
        return -1;
    }
}
