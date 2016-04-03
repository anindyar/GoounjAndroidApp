package com.bvocal.goounj.fragments.vote;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bvocal.goounj.R;
import com.bvocal.goounj.adapters.VoteListAdapter;
import com.bvocal.goounj.fragments.BaseFragment;
import com.bvocal.goounj.pojo.CurrentPollItem;
import com.bvocal.goounj.pojo.VoteItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 11/1/16.
 */
public class HistoryVote extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private List<VoteItem> mVoteList = new ArrayList<>();
    private RecyclerView mVoteRecyclerView;
    private VoteListAdapter mVoteAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RelativeLayout mNoVoteImage, mPageError;
    private Handler handler = new Handler();

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVoteList.clear();
        for (int i = 0; i < 10; i++) {
            mVoteList.add(new VoteItem(i, "Title " + i, "13-10-2016", "13-10-2016", 1, "10-01-2016", "Nanda - " + i));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_poll_listview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVoteRecyclerView = (RecyclerView) view.findViewById(R.id.currentPollListview);
        mVoteRecyclerView.setLayoutManager(new LinearLayoutManager(act));
        (mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh)).setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.home_bg, R.color.bg, R.color.tab_opinion, R.color.tab_quick, R.color.tab_social, R.color.tab_survey);
        mNoVoteImage = (RelativeLayout) view.findViewById(R.id.layout_no_list_error);
        mPageError = (RelativeLayout) view.findViewById(R.id.layout_error);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mVoteAdapter = new VoteListAdapter(act, mVoteList, 2);
        mVoteRecyclerView.setAdapter(mVoteAdapter);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
}
