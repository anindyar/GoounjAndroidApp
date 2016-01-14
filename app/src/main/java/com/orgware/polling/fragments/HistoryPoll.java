package com.orgware.polling.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orgware.polling.HomeActivity;
import com.orgware.polling.R;
import com.orgware.polling.adapters.CurrentPollAdapter;
import com.orgware.polling.adapters.HistoryPollAdapter;
import com.orgware.polling.fragments.BaseFragment;
import com.orgware.polling.interfaces.RestApiListener;
import com.orgware.polling.network.NetworkHelper;
import com.orgware.polling.network.RestApiProcessor;
import com.orgware.polling.pojo.CurrentPollItem;
import com.orgware.polling.utils.Methodutils;
import com.orgware.polling.utils.SuperSwipeRefreshLayout;

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
    ProgressDialog mProgress;
    CurrentPollAdapter mAdapter;
    int limit = 15;
    RelativeLayout mPollNoError, mPollError;
    private SuperSwipeRefreshLayout swipeRefreshLayout;
    // Header View
    private ProgressBar progressBar;
    private TextView textView;
    private ImageView imageView;
    // Footer View
    private ProgressBar footerProgressBar;
    private TextView footerTextView;
    private ImageView footerImageView;

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgress = new ProgressDialog(act);
        mProgress.setMessage("Loading");
        mProgress.setCancelable(false);
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

        mHistoryPollList = (RecyclerView) v.findViewById(R.id.currentPollListview);
        mPollNoError = (RelativeLayout) v.findViewById(R.id.layout_no_poll_error);
        mPollError = (RelativeLayout) v.findViewById(R.id.layout_poll_error);
        swipeRefreshLayout = (SuperSwipeRefreshLayout) v.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setHeaderViewBackgroundColor(getResources().getColor(R.color.ash_bg));
        swipeRefreshLayout.setHeaderView(createHeaderView());// add headerView
        swipeRefreshLayout.setFooterView(null);
        swipeRefreshLayout.setTargetScrollWithLayout(true);
        swipeRefreshLayout
                .setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {

                    @Override
                    public void onRefresh() {
                        textView.setText("Pull Down To Refresh");
                        imageView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        if (NetworkHelper.checkActiveInternet(act)) {
                            getPollForCreatedUser(BASE_URL + SHOW_POLL_FOR_AUDIENCE);
                            progressBar.setVisibility(View.GONE);
                        } else
                            Methodutils.messageWithTitle(act, "No Internet connection", "Please check your internet connection", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    act.getSupportFragmentManager().popBackStack();
                                    return;
                                }
                            });
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onPullDistance(int distance) {
                        // pull distance
                    }

                    @Override
                    public void onPullEnable(boolean enable) {
                        textView.setText(enable ? "Release To Refresh" : "Pull Down To Refresh");
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setRotation(enable ? 180 : 0);
                    }
                });
        mHistoryPollList.setLayoutManager(new LinearLayoutManager(act));
        ((HomeActivity) act).mSearchPollsTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    List<CurrentPollItem> filteredTitles = new ArrayList<>();
                    for (int i = 0; i < itemList.size(); i++) {
                        if (itemList.get(i).mCurrentPollTitle.toString().toLowerCase().contains(s) ||
                                itemList.get(i).mCurrentPollTitle.toString().toUpperCase().contains(s) ||
                                itemList.get(i).mCurrentPollTitle.toString().contains(s)) {
                            filteredTitles.add(itemList.get(i));
                        }
                    }
                    mAdapter = new CurrentPollAdapter(act, filteredTitles, 2);
                    mHistoryPollList.setAdapter(mAdapter);
//                    mAdapter = new ContactGridviewAdapter(act, filteredTitles);
//                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter = new CurrentPollAdapter(act, itemList, 2);
                    mHistoryPollList.setAdapter(mAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                    Log.e("Search", "Yes");
                else
                    Log.e("Search", "No");
//                    makeToast("No records found");

            }
        });
        return v;
    }

    private View createHeaderView() {
        View headerView = LayoutInflater.from(swipeRefreshLayout.getContext())
                .inflate(R.layout.layout_head, null);
        progressBar = (ProgressBar) headerView.findViewById(R.id.pb_view);
        textView = (TextView) headerView.findViewById(R.id.text_view);
        textView.setText("Pull Down To Refresh");
        imageView = (ImageView) headerView.findViewById(R.id.image_view);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.arrow_refresh);
        progressBar.setVisibility(View.GONE);
        return headerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
//        ((HomeActivity) act).mPageTitle.setText("Poll HistoryVote");
        ((HomeActivity) act).openSearch.setOnClickListener(this);
        if (NetworkHelper.checkActiveInternet(act))
            getPollForCreatedUser(BASE_URL + SHOW_POLL_FOR_AUDIENCE);
        else
            Methodutils.messageWithTitle(act, "No Internet connection", "Please check your internet connection", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    act.getSupportFragmentManager().popBackStack();
                    return;
                }
            });
    }


    private String showPollParams() {
        JSONObject mShowPollOnject = new JSONObject();
        try {
            mShowPollOnject.put(USER_ID, "" + preferences.getString(USER_ID, ""));
            mShowPollOnject.put(POLL_LIMIT, limit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mShowPollOnject.toString();
    }

    private void getPollForCreatedUser(String url) {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.POST, url, true, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                mHistoryPollList.setVisibility(View.VISIBLE);
                mPollError.setVisibility(View.GONE);
                if (response.equals("[]"))
                    makeToast("No records found");
                else
                    try {
                        showPollList(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onRequestFailed(Exception e) {
                mHistoryPollList.setVisibility(View.GONE);
                mPollError.setVisibility(View.VISIBLE);
//                if (e == null)
//                    Methodutils.messageWithTitle(act, "Failed", "Internal Server Error. Requested Action Failed", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            act.getSupportFragmentManager().popBackStack();
//                        }
//                    });
//                else {
//                    if (e.getMessage() == null)
//                        Methodutils.message(act, "No Records Found", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                act.getSupportFragmentManager().popBackStack();
//                            }
//                        });
//                    else
//                        Methodutils.message(act, "Try again, Failed to connect to server", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                act.getSupportFragmentManager().popBackStack();
//                            }
//                        });
//
//                }
            }
        });
        processor.execute(showPollParams().toString());
    }

    public void showPollList(String response) {
        try {
//            JSONObject object = new JSONObject(response);
//            JSONArray objectArray = object.optJSONArray(response);
            JSONArray objectArray = new JSONArray(response);
            itemList.clear();
            for (int i = 0; i < objectArray.length(); i++) {
                JSONObject objectPolls = objectArray.optJSONObject(i);
                Log.e("Array Values", "" + i);
                if (objectPolls.optString("isAnswered").equals("1")) {
                    itemList.add(new CurrentPollItem(objectPolls.optInt("pollId"), splitFromString("" + objectPolls.optString("startDate")), splitFromString("" + objectPolls.optString("endDate")),
                            objectPolls.optString("pollName"), objectPolls.optInt("isBoost"), objectPolls.optString("createdUserName")));
                }
            }
            refreshCurrentPollListview();
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
        } else {
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
        try {
            getResultPollForCreatedUser(BASE_URL + RESULT_URL + itemList.get(position).currentPollId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getResultPollForCreatedUser(String url) {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.GET, url, true, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
//                mHistoryPollList.setVisibility(View.VISIBLE);
//                mPollError.setVisibility(View.GONE);
                if (response.equals("[]"))
                    makeToast("No Records Found");
                else
                    try {
                        showResultPollList(response);
                        ((HomeActivity) act).setNewFragment(new ResultPoll(), "", true);
                        ((HomeActivity) act).mSearchPollsTxt.setVisibility(View.GONE);
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
        Methodutils.showListSearch(act, itemList, mHistoryPollList);
    }
}
