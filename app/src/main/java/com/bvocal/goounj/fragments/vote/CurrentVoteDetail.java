package com.bvocal.goounj.fragments.vote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bvocal.goounj.R;
import com.bvocal.goounj.adapters.PollPagerAdapter;
import com.bvocal.goounj.fragments.BaseFragment;
import com.bvocal.goounj.views.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 12/1/16.
 */
public class CurrentVoteDetail extends BaseFragment {

    int id;

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments().getBundle("vote_bundle");
        id = bundle.getInt("electionId");
        makeToast("Bundle" + bundle.getString("electionName") + " - " + bundle.getInt("electionId"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_vote_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setNewFragment(setPagerFragment(new CandidateDetail(), id), R.id.fragment_content_candidate, "Candidate", false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
