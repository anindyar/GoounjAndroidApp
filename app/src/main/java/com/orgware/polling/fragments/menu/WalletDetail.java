package com.orgware.polling.fragments.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orgware.polling.MenuDetailActivity;
import com.orgware.polling.R;
import com.orgware.polling.fragments.BaseFragment;

/**
 * Created by nandagopal on 21/1/16.
 */
public class WalletDetail extends BaseFragment {

    @Override
    public void setTitle() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_wallet, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MenuDetailActivity) act).setTitle("Wallet");
    }
}
