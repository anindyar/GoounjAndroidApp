package com.orgware.polling.fragments.vote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orgware.polling.R;
import com.orgware.polling.fragments.BaseFragment;

/**
 * Created by nandagopal on 12/1/16.
 */
public class CurrentVoteDetail extends BaseFragment {




    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String value = getArguments().getString("PAGER_VALUE");
        makeToast("" + value);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_vote_detail, container, false);
    }


}
