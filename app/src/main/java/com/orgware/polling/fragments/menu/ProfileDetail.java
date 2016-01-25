package com.orgware.polling.fragments.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orgware.polling.MainHomeActivity;
import com.orgware.polling.MenuDetailActivity;
import com.orgware.polling.R;
import com.orgware.polling.fragments.BaseFragment;

/**
 * Created by nandagopal on 21/1/16.
 */
public class ProfileDetail extends BaseFragment {
    TextView mProfileName;

    @Override
    public void setTitle() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProfileName = (TextView) view.findViewById(R.id.profile_text_detail);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProfileName.setText("" + preferences.getString(USERNAME, ""));
        ((MenuDetailActivity) act).setTitle("Profile");
    }
}
