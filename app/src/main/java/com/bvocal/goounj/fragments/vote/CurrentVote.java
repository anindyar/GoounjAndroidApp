package com.bvocal.goounj.fragments.vote;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.bvocal.goounj.MainHomeActivity;
import com.bvocal.goounj.R;
import com.bvocal.goounj.activities.poll.CurrentPollDetailActivity;
import com.bvocal.goounj.adapters.VoteListAdapter;
import com.bvocal.goounj.fragments.BaseFragment;
import com.bvocal.goounj.interfaces.Appinterface;
import com.bvocal.goounj.interfaces.RestApiListener;
import com.bvocal.goounj.network.NetworkHelper;
import com.bvocal.goounj.network.RestApiProcessor;
import com.bvocal.goounj.pojo.CurrentPollItem;
import com.bvocal.goounj.pojo.VoteItem;
import com.bvocal.goounj.utils.Methodutils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 11/1/16.
 */
public class CurrentVote extends BaseFragment implements Appinterface, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

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
        try {
            getVoteList("http://192.168.10.45:3000/" + ELECTION_LIST, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        for (int i = 0; i < 10; i++) {
////            mVoteList.add(new VoteItem(i, "13-10-2016", "13-10-2016", "Title " + i, 1, "Nanda - " + i, "10-01-2016"));
//            mVoteList.add(new VoteItem(i, "Title " + i, "13-10-2016", "13-10-2016", 1, "10-01-2016", "Nanda - " + i));
//        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_vote_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        (mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh)).setOnRefreshListener(this);
        mVoteRecyclerView = (RecyclerView) view.findViewById(R.id.vote_recyclerview);
        mVoteRecyclerView.setLayoutManager(new LinearLayoutManager(act));
        mSwipeRefreshLayout.setColorSchemeResources(R.color.home_bg, R.color.bg, R.color.tab_opinion, R.color.tab_quick, R.color.tab_social, R.color.tab_survey);
        mNoVoteImage = (RelativeLayout) view.findViewById(R.id.layout_no_list_error);
        mPageError = (RelativeLayout) view.findViewById(R.id.layout_error);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mVoteList.clear();
        mVoteAdapter = new VoteListAdapter(act, mVoteList, 1);
        mVoteRecyclerView.setAdapter(mVoteAdapter);
        mVoteAdapter.setOnItemClickListener(this);

        if (mVoteList.isEmpty()) {
            mVoteRecyclerView.setVisibility(View.GONE);
            mNoVoteImage.setVisibility(View.VISIBLE);
        } else {
            mVoteRecyclerView.setVisibility(View.VISIBLE);
            mNoVoteImage.setVisibility(View.GONE);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkHelper.checkActiveInternet(act)) {
                    try {
                        getVoteList("http://192.168.10.45:3000/" + ELECTION_LIST, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    Methodutils.messageWithTitle(act, "No Internet connection", "Please check your internet connection", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            act.getSupportFragmentManager().popBackStack();
                            return;
                        }
                    });
            }
        });
    }


    private void getVoteList(String url, boolean pullDownType) {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.POST, url, pullDownType, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                Log.e("Poll List Response", "" + response.toString());
                if (response.equals("[]"))
                    makeToast("No records found");
                try {
                    showPollList(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestFailed(Exception e) {
                mVoteRecyclerView.setVisibility(View.GONE);
                mNoVoteImage.setVisibility(View.GONE);
                mPageError.setVisibility(View.VISIBLE);
                if (e == null) {
                    Log.e("Error", "Exception is null");
                    Methodutils.message(act, "Internal Server Error. Requested Action Failed", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                } else {
                    Log.e("Error", "Exception is not null");
                    Methodutils.message(act, "" + e.getMessage(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                }
            }
        });
        processor.execute(showPollParams().toString());
    }

    private String showPollParams() {
        JSONObject mShowPollOnject = new JSONObject();
        try {
            mShowPollOnject.put(USER_ID, "" + preferences.getString(USER_ID, ""));
            mShowPollOnject.put("lowerLimit", 0);
            mShowPollOnject.put("upperLimit", 10);
            mShowPollOnject.put("isVoted", "2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mShowPollOnject.toString();
    }

    private void refreshCurrentPollListview() {
        if (mVoteList.size() > 0) {
            mVoteRecyclerView.setVisibility(View.VISIBLE);
            mNoVoteImage.setVisibility(View.GONE);
            mPageError.setVisibility(View.GONE);
            mVoteAdapter = new VoteListAdapter(act, mVoteList, 1);
            mVoteAdapter.setOnItemClickListener(this);
            mVoteRecyclerView.setAdapter(mVoteAdapter);
        } else {
            mVoteRecyclerView.setVisibility(View.GONE);
            mNoVoteImage.setVisibility(View.VISIBLE);
        }
    }

    public void showPollList(String response) {
        try {
            JSONArray objectArray = new JSONArray(response);
            mVoteList.clear();
            for (int i = 0; i < objectArray.length(); i++) {
                JSONObject objectPolls = objectArray.optJSONObject(i);
                Log.e("Array Values", "" + i);
                if (objectPolls.optString("isVoted").equals("1")) {
                    mVoteList.add(new VoteItem(objectPolls.optInt("electionId"), objectPolls.optString("electionName"),
                            objectPolls.optString("endDate"), objectPolls.optString("startDate"), objectPolls.optInt("isVoted"),
                            objectPolls.optString("nominationEndDate"), objectPolls.optString("associationName")));

                }
            }

            refreshCurrentPollListview();
            mSwipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
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
        VoteItem item = mVoteList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("electionId", item.electionId);
        bundle.putString("electionName", item.electionName);
        bundle.putString("endDate", item.endDate);
        bundle.putString("startDate", item.startDate);
        bundle.putInt("isVoted", item.isVoted);
        bundle.putString("nominationEndDate", item.nominationEndDate);
        bundle.putString("associationName", item.associationName);
//        editor.putInt(POLL_ID, mVoteList.get(position).currentPollId).putString(POLL_NAME, "" + mVoteList.get(position).mCurrentPollTitle).putString(CURRENT_CREATED_USER_NAME, "" + mVoteList.get(position).mCreatedUserName).commit();
        startActivity(new Intent(act, CurrentPollDetailActivity.class).putExtra("poll_id", item.electionId).putExtra("poll_type", 7).putExtra("vote_bundle", bundle));
        Log.e("Poll Id", "" + mVoteList.get(position).electionId);
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
//        if (NetworkHelper.checkActiveInternet(act))
//            getPollForCreatedUser(BASE_URL + SHOW_POLL_FOR_AUDIENCE, true);
//        else
//            Methodutils.messageWithTitle(act, "No Internet connection", "Please check your internet connection", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    act.getSupportFragmentManager().popBackStack();
//                    return;
//                }
//            });
    }
}
