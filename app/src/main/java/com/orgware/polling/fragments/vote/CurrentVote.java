package com.orgware.polling.fragments.vote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.orgware.polling.HomeActivity;
import com.orgware.polling.MainHomeActivity;
import com.orgware.polling.R;
import com.orgware.polling.adapters.CurrentPollAdapter;
import com.orgware.polling.adapters.VoteListAdapter;
import com.orgware.polling.fragments.BaseFragment;
import com.orgware.polling.pojo.CurrentPollItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 11/1/16.
 */
public class CurrentVote extends BaseFragment implements AdapterView.OnItemClickListener {

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
        mVoteAdapter = new VoteListAdapter(act, mVoteList, 1);
        mVoteRecyclerView.setAdapter(mVoteAdapter);
        mVoteAdapter.setOnItemClickListener(this);
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((MainHomeActivity) act).setNewFragment(setPagerFragment(new CurrentVoteDetail(), "PAGER", 1, "PAGER_VALUE", "test"), "Vote Detail", true);
    }
}
