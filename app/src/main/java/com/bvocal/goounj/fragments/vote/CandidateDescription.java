package com.bvocal.goounj.fragments.vote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bvocal.goounj.R;
import com.bvocal.goounj.fragments.BaseFragment;

/**
 * Created by nandagopal on 19/1/16.
 */
public class CandidateDescription extends BaseFragment implements View.OnClickListener {

    private TextView mCandidateDescription;
    private Button mVote;
    private String mDesc;

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mDesc = getArguments().getString("can_desc");
//            makeToast("" + getArguments().getString("can_desc"));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        mCandidateDescription.setText("" + mDesc);
        (mVote = (Button) view.findViewById(R.id.btnVote)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
