package com.orgware.polling.fragments.vote;

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

import com.orgware.polling.MainHomeActivity;
import com.orgware.polling.R;
import com.orgware.polling.adapters.CurrentPollAdapter;
import com.orgware.polling.adapters.VoteListAdapter;
import com.orgware.polling.fragments.BaseFragment;
import com.orgware.polling.interfaces.RestApiListener;
import com.orgware.polling.network.NetworkHelper;
import com.orgware.polling.network.RestApiProcessor;
import com.orgware.polling.pojo.CurrentPollItem;
import com.orgware.polling.utils.Methodutils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 11/1/16.
 */
public class CurrentVote extends BaseFragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private List<CurrentPollItem> mVoteList = new ArrayList<>();
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
            mVoteList.add(new CurrentPollItem(i, "13-10-2016", "13-10-2016", "Title " + i, 1, "Nanda - " + i, "10-01-2016"));
        }
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

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
//                if (NetworkHelper.checkActiveInternet(act))
//                    getPollForCreatedUser(BASE_URL + SHOW_POLL_FOR_AUDIENCE, true);
//                else
//                    Methodutils.messageWithTitle(act, "No Internet connection", "Please check your internet connection", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            act.getSupportFragmentManager().popBackStack();
//                            return;
//                        }
//                    });
            }
        });
    }


    private void getPollForCreatedUser(String url, boolean pullDownType) {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.POST, url, pullDownType, true, new RestApiListener<String>() {
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
                makeToast("Failed to connect to server");
            }
        });
        processor.execute(showPollParams().toString());
    }

    private String showPollParams() {
        JSONObject mShowPollOnject = new JSONObject();
        try {
            mShowPollOnject.put(USER_ID, "" + preferences.getString(USER_ID, ""));
            mShowPollOnject.put(POLL_LIMIT, 10);
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
//            JSONObject object = new JSONObject(response);
//            JSONArray objectArray = object.optJSONArray(response);
            JSONArray objectArray = new JSONArray(response);
            mVoteList.clear();
            if (preferences.getInt(DASHBOARD_ID, 0) == 0) {
                for (int i = 0; i < objectArray.length(); i++) {
                    JSONObject objectPolls = objectArray.optJSONObject(i);
                    Log.e("Array Values", "" + i);
                    if (objectPolls.optString("isAnswered").equals("0")) {
                        mVoteList.add(new CurrentPollItem(objectPolls.optInt("pollId"), splitFromString("" + objectPolls.optString("startDate")), splitFromString("" + objectPolls.optString("endDate")),
                                objectPolls.optString("pollName"), objectPolls.optInt("isBoost"), objectPolls.optString("createdUserName")));
                    }
                }
            }
            if (preferences.getInt(DASHBOARD_ID, 0) == 1) {
                for (int i = 0; i < objectArray.length(); i++) {
                    JSONObject objectPolls = objectArray.optJSONObject(i);
                    Log.e("Array Values", "" + i);
                    mVoteList.add(new CurrentPollItem(objectPolls.optInt("pollId"), splitFromString("" + objectPolls.optString("startDate")), splitFromString("" + objectPolls.optString("endDate")),
                            objectPolls.optString("pollName"), objectPolls.optInt("isBoost"), objectPolls.optString("createdUserName")));
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
        ((MainHomeActivity) act).setNewFragment(setPagerFragment(new CurrentVoteDetail(), "PAGER", 1, "PAGER_VALUE", "test"), "Vote Detail", true);
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
