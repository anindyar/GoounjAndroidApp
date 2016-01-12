package com.orgware.polling.fragments.vote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orgware.polling.R;
import com.orgware.polling.adapters.VoteListAdapter;
import com.orgware.polling.fragments.BaseFragment;
import com.orgware.polling.pojo.CurrentPollItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 11/1/16.
 */
public class HistoryVote extends BaseFragment {
    List<CurrentPollItem> mVoteList = new ArrayList<>();
    RecyclerView mVoteRecyclerView;
    VoteListAdapter mVoteAdapter;

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVoteList.clear();
        for (int i = 0; i < 10; i++) {
            mVoteList.add(new CurrentPollItem(i, "13-10-2016", "13-10-2016", "Title " + i, 1, "Nanda - " + i, "10-01-2016"));
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mVoteAdapter = new VoteListAdapter(act, mVoteList, 2);
        mVoteRecyclerView.setAdapter(mVoteAdapter);
    }
}
