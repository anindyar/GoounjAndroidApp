package com.bvocal.goounj.fragments.vote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bvocal.goounj.R;
import com.bvocal.goounj.fragments.BaseFragment;

/**
 * Created by nandagopal on 19/1/16.
 */
public class CandidateDescription extends BaseFragment {

    TextView mCandidateDescription;

    @Override
    public void setTitle() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_candidate_description, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCandidateDescription = (TextView) view.findViewById(R.id.candidate_detail_description);
    }
}
