package com.orgware.polling.fragments.vote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orgware.polling.R;
import com.orgware.polling.adapters.CandidateListAdapter;
import com.orgware.polling.fragments.BaseFragment;
import com.orgware.polling.pojo.CandidateItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 13/1/16.
 */
public class CandidateDetail extends BaseFragment {

    RecyclerView mCandidateRecyclerView;
    List<CandidateItem> itemList = new ArrayList<>();
    CandidateListAdapter mAdapter;

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < 10; i++) {
            itemList.add(new CandidateItem("Nanda", "Lorem ipsum is a sample content", R.drawable.ic_usericon_blue));
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_candidate_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCandidateRecyclerView = (RecyclerView) view.findViewById(R.id.candidate_list);
        mCandidateRecyclerView.setLayoutManager(new LinearLayoutManager(act));
        mAdapter = new CandidateListAdapter(act, itemList);
        mCandidateRecyclerView.setAdapter(mAdapter);
    }
}
