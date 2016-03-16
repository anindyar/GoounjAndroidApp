package com.bvocal.goounj.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bvocal.goounj.R;
import com.bvocal.goounj.adapters.SocialTrendsAdapter;
import com.bvocal.goounj.pojo.SocialTrendsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 22/10/15.
 */
public class PollSocial extends BaseFragment implements View.OnClickListener {

    RecyclerView mRecyclerView;
    List<SocialTrendsItem> mSocialTrendsItemList;
    SocialTrendsAdapter mAdapter;

    @Override
    public void setTitle() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSocialTrendsItemList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mSocialTrendsItemList.add(new SocialTrendsItem("Lorem ipsum dolor sit amet,consectetur adipiscing edit.", R.drawable.ic_ad_withus));
        }
        mAdapter = new SocialTrendsAdapter(act, mSocialTrendsItemList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_social_trends, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.social_recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(act));
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }
}
