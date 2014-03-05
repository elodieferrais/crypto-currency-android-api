package com.eferrais.coins;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eferrais.api.baseclient.CryptoAddressClient;
import com.eferrais.api.baseclient.CryptoCallback;
import com.eferrais.api.model.Account;

import java.util.List;

public class MainActivity extends Activity {

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(), PlaceholderFragment.class.getSimpleName())
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_example) {
            AccountCreationDialog accountCreationDialog = new AccountCreationDialog(this);
            accountCreationDialog.setAccountCreationDialogListener((AccountCreationDialogListener) getFragmentManager().findFragmentByTag(PlaceholderFragment.class.getSimpleName()));
            accountCreationDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements AccountCreationDialogListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private BalanceListAdapter adapter;
        private CryptoAddressClient client;
        private AccountsManager accountsManager;
        private CryptoCallback<Account> callback = new CryptoCallback<Account>() {
            @Override
            public void onResult(Account result, Error error) {
                Log.d("ELODIE", "onResult balance:" + String.valueOf(result.balance));
                if (result == null) {
                    return;
                }
                if (error != null) {
                    //TODO Alert dialog
                    return;
                }

                accountsManager.getAccount(result.address).setBalance(result.balance);
                accountsManager.commit();
                adapter.notifyDataSetChanged();
                Log.d("ELODIE", "notifyDataSetChanged");
            }

            ;

        };

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            accountsManager = new AccountsManager(getActivity());

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ListView listView = (ListView) rootView.findViewById(R.id.fragment_main_listview);
            adapter = new BalanceListAdapter(getActivity(), R.layout.cell_account);
            listView.setAdapter(adapter);
            updateCoins(accountsManager.getAccounts());
            return rootView;
        }

        public void updateCoins(List<Account> accounts) {
            if (client == null) {
                client = new CryptoAddressClient(getActivity());
            }
            for (Account account : accounts) {
                client.getBalance(account.address, account.coinType, callback);
            }
        }

        @Override
        public void onSuccessAccountCreation(List<Account> accounts) {
            Log.d("ELODIE", "onSuccessAccountCreation");
            if (adapter != null) {
                Log.d("ELODIE", "notifyDataSetChanged");
                adapter.notifyDataSetChanged();
                Log.d("ELODIE", "updateCoinsBalance");
                updateCoins(accounts);
            }
        }

        @Override
        public void onCancelAccountCreation() {
        }
    }
}
