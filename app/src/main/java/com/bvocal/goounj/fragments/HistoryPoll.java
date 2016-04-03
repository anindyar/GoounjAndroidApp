package com.bvocal.goounj.fragments;

import android.content.Intent;
import android.os.Bundle;
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
import com.bvocal.goounj.adapters.CurrentPollAdapter;
import com.bvocal.goounj.fragments.poll.ResultPollNew;
import com.bvocal.goounj.interfaces.RestApiListener;
import com.bvocal.goounj.network.NetworkHelper;
import com.bvocal.goounj.network.RestApiProcessor;
import com.bvocal.goounj.pojo.CurrentPollItem;
import com.bvocal.goounj.activities.poll.CurrentPollDetailActivity;
import com.bvocal.goounj.utils.Methodutils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 26/10/15.
 */
public class HistoryPoll extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    RecyclerView mHistoryPollList;
    //    HistoryPollAdapter mAdapter;
    List<CurrentPollItem> itemList;
    CurrentPollAdapter mAdapter;
    int limit = 15;
    RelativeLayout mPollNoError, mPollError;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mLowerLimit = 0, mUpperLimit = 10;

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemList = new ArrayList<>();

//        for (int i = 0; i < 10; i++) {
//            itemList.add(new CurrentPollItem("Poll title", "Tester", "30/04/1991", "30/04/1991"));
//        }
//
//        mAdapter = new HistoryPollAdapter(act, itemList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_poll_listview, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh);
        mHistoryPollList = (RecyclerView) v.findViewById(R.id.currentPollListview);
        mPollNoError = (RelativeLayout) v.findViewById(R.id.layout_no_poll_error);
        mPollError = (RelativeLayout) v.findViewById(R.id.layout_poll_error);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.home_bg, R.color.bg, R.color.tab_opinion, R.color.tab_quick, R.color.tab_social, R.color.tab_survey);
        mHistoryPollList.setLayoutManager(new LinearLayoutManager(act));
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            if (NetworkHelper.checkActiveInternet(act))
                getPollForCreatedUser(mLowerLimit, mUpperLimit, BASE_URL + SHOW_POLL_FOR_AUDIENCE);
            else
                Methodutils.messageWithTitle(act, "No Internet connection", "Please check your internet connection", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        act.getSupportFragmentManager().popBackStack();
                        return;
                    }
                });
            if (itemList.size() == 0) {
                mHistoryPollList.setVisibility(View.GONE);
                mPollNoError.setVisibility(View.VISIBLE);
            } else {
                mHistoryPollList.setVisibility(View.VISIBLE);
                mPollNoError.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
//        itemList.clear();
//        if (NetworkHelper.checkActiveInternet(act))
//            getPollForCreatedUser(mLowerLimit, mUpperLimit, BASE_URL + SHOW_POLL_FOR_AUDIENCE);
//        else
//            Methodutils.messageWithTitle(act, "No Internet connection", "Please check your internet connection", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    act.getSupportFragmentManager().popBackStack();
//                    return;
//                }
//            });
//        if (itemList.size() == 0) {
//            mHistoryPollList.setVisibility(View.GONE);
//            mPollNoError.setVisibility(View.VISIBLE);
//        } else {
//            mHistoryPollList.setVisibility(View.VISIBLE);
//            mPollNoError.setVisibility(View.GONE);
//        }


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkHelper.checkActiveInternet(act))
                    getPollForCreatedUser(mLowerLimit, mUpperLimit, BASE_URL + SHOW_POLL_FOR_AUDIENCE);
                else
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


    private String showPollParams(int mLowerLimit, int mUpperLimit) {
        JSONObject mShowPollOnject = new JSONObject();
        try {
            mShowPollOnject.put(USER_ID, "" + preferences.getString(USER_ID, ""));
            mShowPollOnject.put("lowerLimit", mLowerLimit);
            mShowPollOnject.put("upperLimit", mUpperLimit);
            mShowPollOnject.put("isAnswered", "2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mShowPollOnject.toString();
    }

    private void getPollForCreatedUser(final int mLowerLimit, final int mUpperLimit, String url) {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.POST, url, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
//                mHistoryPollList.setVisibility(View.VISIBLE);
//                mPollError.setVisibility(View.GONE);
                if (response.equals("[]"))
                    makeToast("No records found");
                else
                    try {
                        showPollList(response);
                        if (itemList.size() == 0) {
                            mHistoryPollList.setVisibility(View.GONE);
                            mPollNoError.setVisibility(View.VISIBLE);
                        } else {
                            mHistoryPollList.setVisibility(View.VISIBLE);
                            mPollNoError.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onRequestFailed(Exception e) {
                mHistoryPollList.setVisibility(View.GONE);
                mPollError.setVisibility(View.VISIBLE);
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
        processor.execute(showPollParams(mLowerLimit, mUpperLimit).toString());
    }

    public void showPollList(String response) {
        try {
            JSONArray objectArray = new JSONArray(response);

            for (int i = 0; i < objectArray.length(); i++) {
                JSONObject objectPolls = objectArray.optJSONObject(i);
                Log.e("Array Values", "" + i);
                if (objectPolls.optString("isAnswered").equals("1")) {
                    itemList.add(new CurrentPollItem(objectPolls.optInt("pollId"), splitFromString("" + objectPolls.optString("startDate")), splitFromString("" + objectPolls.optString("endDate")),
                            objectPolls.optString("pollName"), objectPolls.optInt("isBoost"), objectPolls.optString("createdUserName")));
                }
            }
            refreshCurrentPollListview();
            mSwipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshCurrentPollListview() {
        if (itemList.size() > 0) {
            mHistoryPollList.setVisibility(View.VISIBLE);
            mPollNoError.setVisibility(View.GONE);
            mPollError.setVisibility(View.GONE);
            mAdapter = new CurrentPollAdapter(act, itemList, 2);
            mAdapter.setOnItemClickListener(this);
            mHistoryPollList.setAdapter(mAdapter);
            mLowerLimit = mLowerLimit + 10;
        } else {
            mLowerLimit = 0;
            mHistoryPollList.setVisibility(View.GONE);
            mPollNoError.setVisibility(View.VISIBLE);
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
        editor.putInt(POLL_ID, itemList.get(position).currentPollId).putString(POLL_NAME, "" + itemList.get(position).mCurrentPollTitle).putString(CURRENT_CREATED_USER_NAME, "" + itemList.get(position).mCreatedUserName).commit();
//        ((HomeActivity) act).setNewFragment(new ResultPoll(), "Current Poll Pager", true);
        startActivity(new Intent(act, CurrentPollDetailActivity.class).putExtra("poll_id", itemList.get(position).currentPollId).putExtra("poll_type", 2));
        Log.e("Poll Id", "" + itemList.get(position).currentPollId);
//        try {
//            getResultPollForCreatedUser(BASE_URL + RESULT_URL + itemList.get(position).currentPollId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void getResultPollForCreatedUser(String url) {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.GET, url, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
//                mHistoryPollList.setVisibility(View.VISIBLE);
//                mPollError.setVisibility(View.GONE);
                if (response.equals("[]"))
                    makeToast("No Records Found");
                else
                    try {
                        showResultPollList(response);
                        ((MainHomeActivity) act).setNewFragment(new ResultPollNew(), "", true);
//                        ((HomeActivity) act).mSearchPollsTxt.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onRequestFailed(Exception e) {
//                mHistoryPollList.setVisibility(View.GONE);
//                mPollError.setVisibility(View.VISIBLE);
                if (e == null) {
                    Log.e("Error", "" + e.getMessage());
                    Methodutils.message(act, "Internal Server Error. Requested Action Failed", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            act.getSupportFragmentManager().popBackStack();
                        }
                    });
                } else {
                    if (e.getMessage() == null)
                        Methodutils.message(act, "No Records Found", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                act.getSupportFragmentManager().popBackStack();
                            }
                        });
                    else
                        Methodutils.message(act, "Try again, Failed to connect to server", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                act.getSupportFragmentManager().popBackStack();
                            }
                        });

                }
            }
        });
        processor.execute();
    }

    public void showResultPollList(String response) {
        try {
            JSONObject mPollDetailObject = new JSONObject(response);
            JSONArray mQuestionsArray = mPollDetailObject.optJSONArray(ANS_QUESTIONLIST);
            editor.putInt(RESULT_QUESTION_SIZE, mQuestionsArray.length()).commit();
            for (int j = 0; j < mQuestionsArray.length(); j++) {
                JSONObject mQuestions = mQuestionsArray.optJSONObject(j);
                JSONArray mChoicesArray = mQuestions.optJSONArray(CHOICES);
                Log.e("Choice Array - " + j, "" + mChoicesArray.toString());
                editor.putString("RES_QUESTION_" + j, "" + mQuestions.optString(QUESTION)).
                        putString("RES_CHOICE_" + j, "" + mChoicesArray.toString()).commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
//        Methodutils.showListSearch(act, itemList, mHistoryPollList);
    }

    /**
     * Called when the user submits the query. This could be due to a key press on the
     * keyboard or due to pressing a submit button.
     * The listener can override the standard behavior by returning true
     * to indicate that it has handled the submit request. Otherwise return false to
     * let the SearchView handle the submission by launching any associated intent.
     *
     * @param query the query text that is to be submitted
     * @return true if the query has been handled by the listener, false to let the
     * SearchView perform the default action.
     */


    private List<CurrentPollItem> filter(List<CurrentPollItem> models, String query) {
        query = query.toLowerCase();

        final List<CurrentPollItem> filteredModelList = new ArrayList<>();
        for (CurrentPollItem model : models) {
            final String text = model.mCurrentPollTitle.toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
