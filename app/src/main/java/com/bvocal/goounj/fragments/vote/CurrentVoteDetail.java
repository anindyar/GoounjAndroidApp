package com.bvocal.goounj.fragments.vote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bvocal.goounj.R;
import com.bvocal.goounj.adapters.PollPagerAdapter;
import com.bvocal.goounj.fragments.BaseFragment;
import com.bvocal.goounj.pojo.CandidateItem;
import com.bvocal.goounj.views.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 12/1/16.
 */
public class CurrentVoteDetail extends BaseFragment {
    int id;
    int electionId;
    private String electionName, associationName, startDate, endDate;
    private TextView txtPollTitle, txtPollCreatedBy, txtPollStartDate, txtPollEndDate, txtSelfNominationDate;

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getArguments() != null) {
                Bundle bundle = getArguments().getBundle("vote_bundle");
                electionId = bundle.getInt("electionId");
                electionName = bundle.getString("electionName");
                associationName = bundle.getString("associationName");
                startDate = bundle.getString("startDate");
                endDate = bundle.getString("endDate");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_vote_detail, container, false);
    }

    @Override
    public void onViewCreated(View itemView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(itemView, savedInstanceState);
        txtPollTitle = (TextView) itemView.findViewById(R.id.poll_title);
        txtPollCreatedBy = (TextView) itemView.findViewById(R.id.poll_createdBy);
        txtPollStartDate = (TextView) itemView.findViewById(R.id.poll_startDate);
        txtPollEndDate = (TextView) itemView.findViewById(R.id.poll_endDate);
        setNewFragment(setPagerFragment(new CandidateDetail(), electionId), R.id.fragment_content_candidate, "Candidate", false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            txtPollTitle.setText("" + electionName);
            txtPollCreatedBy.setText("Created By: " + associationName);
            txtPollStartDate.setText("" + startDate);
            txtPollEndDate.setText("" + endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
