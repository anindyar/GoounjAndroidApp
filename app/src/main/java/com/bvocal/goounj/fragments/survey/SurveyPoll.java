package com.bvocal.goounj.fragments.survey;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bvocal.goounj.MainHomeActivity;
import com.bvocal.goounj.R;
import com.bvocal.goounj.adapters.CurrentPollAdapter;
import com.bvocal.goounj.database.GoounjDatabase;
import com.bvocal.goounj.fragments.BaseFragment;
import com.bvocal.goounj.fragments.CreatePollPager;
import com.bvocal.goounj.fragments.SurveyDetail;
import com.bvocal.goounj.interfaces.RestApiListener;
import com.bvocal.goounj.network.NetworkHelper;
import com.bvocal.goounj.network.RestApiProcessor;
import com.bvocal.goounj.pojo.CurrentPollItem;
import com.bvocal.goounj.pollactivities.CurrentPollDetailActivity;
import com.bvocal.goounj.utils.Methodutils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 26/10/15.
 */
public class SurveyPoll extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    RecyclerView mCurrentPollList;
    CurrentPollAdapter mAdapter;
    List<CurrentPollItem> itemList;
    List<CurrentPollItem> singleItemList;
    int limit = 15, dashboardId;
    GoounjDatabase db;
    boolean currentPollService;
    RelativeLayout mPollNoError, mPollError;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;
    private int mLowerLimit = 0, mUpperLimit = 10;
    //    private SuperSwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    // Header View
    private LinearLayout mLayoutCreate;
    private Button mBtnCreate;
    private ProgressBar progressBar;
    private TextView textView;
    private ImageView imageView;
    // Footer View
    private ProgressBar footerProgressBar;
    private TextView footerTextView;
    private ImageView footerImageView;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;

    @Override
    public void setTitle() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        db = new GoounjDatabase(act);
        itemList = new ArrayList<>();
        singleItemList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_survey_poll, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mLayoutManager = new LinearLayoutManager(act);
        (mBtnCreate = (Button) v.findViewById(R.id.btnCreate)).setOnClickListener(this);
        (mLayoutCreate = (LinearLayout) v.findViewById(R.id.layout_create)).setOnClickListener(this);
        mCurrentPollList = (RecyclerView) v.findViewById(R.id.currentPollListview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh);
        mCurrentPollList.setLayoutManager(mLayoutManager);
        mPollNoError = (RelativeLayout) v.findViewById(R.id.layout_no_poll_error);
        mPollError = (RelativeLayout) v.findViewById(R.id.layout_poll_error);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.home_bg, R.color.bg, R.color.tab_opinion, R.color.tab_quick, R.color.tab_social, R.color.tab_survey);
        currentPollService = preferences.getBoolean(CURRENT_POLLDB, true);
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser && isResumed()) {
//            if (NetworkHelper.checkActiveInternet(act))
//                getPollForCreatedUser(mLowerLimit, mUpperLimit, BASE_URL + SHOW_SURVEY_LIST, true);
//            else
//                Methodutils.messageWithTitle(act, "No Internet connection", "Please check your internet connection", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        act.getSupportFragmentManager().popBackStack();
//                        return;
//                    }
//                });
//        }
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainHomeActivity) act).setTitle("Survey");
        itemList.clear();
        if (NetworkHelper.checkActiveInternet(act))
            getPollForCreatedUser(mLowerLimit, mUpperLimit, BASE_URL + SHOW_SURVEY_LIST, true);
        else
            Methodutils.messageWithTitle(act, "No Internet connection", "Please check your internet connection", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    act.getSupportFragmentManager().popBackStack();
                    return;
                }
            });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkHelper.checkActiveInternet(act))
                    getPollForCreatedUser(mLowerLimit, mUpperLimit, BASE_URL + SHOW_SURVEY_LIST, true);
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

//        mSwipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                if (NetworkHelper.checkActiveInternet(act))
//                    getPollForCreatedUser(mLowerLimit, mUpperLimit, BASE_URL + SHOW_POLL_FOR_AUDIENCE, true);
//                else
//                    Methodutils.messageWithTitle(act, "No Internet connection", "Please check your internet connection", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            act.getSupportFragmentManager().popBackStack();
//                            return;
//                        }
//                    });
//            }
//        });

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
        editor.putInt(POLL_ID, itemList.get(position).currentPollId).putString(POLL_NAME, "" + itemList.get(position).mCurrentPollTitle).putString(CURRENT_CREATED_USER_NAME, "" + itemList.get(position).mCreatedUserName).commit();
//        getPollDetailPage(BASE_URL + SHOW_POLL_URL + itemList.get(position).currentPollId);
        startActivity(new Intent(act, CurrentPollDetailActivity.class).putExtra("poll_id", itemList.get(position).currentPollId).putExtra("poll_type", 4));
        Log.e("Poll Id", "" + itemList.get(position).currentPollId);
    }


    private String showPollParams(int mLowerLimit, int mUpperLimit) {
        JSONObject mShowPollOnject = new JSONObject();
        try {
            mShowPollOnject.put("lowerLimit", mLowerLimit);
            mShowPollOnject.put("upperLimit", mUpperLimit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mShowPollOnject.toString();
    }

    private void getPollForCreatedUser(int mLowerLimit, int mUpperLimit, String url, boolean pullDownType) {
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
                mCurrentPollList.setVisibility(View.GONE);
                mPollNoError.setVisibility(View.GONE);
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
//            JSONObject object = new JSONObject(response);
//            JSONArray objectArray = object.optJSONArray(response);
            JSONArray objectArray = new JSONArray(response);
            for (int i = 0; i < objectArray.length(); i++) {
                JSONObject objectPolls = objectArray.optJSONObject(i);
                Log.e("Array Values", "" + i);
                itemList.add(new CurrentPollItem(objectPolls.optInt("pollId"), splitFromString("" + objectPolls.optString("startDate")), splitFromString("" + objectPolls.optString("endDate")),
                        objectPolls.optString("pollName"), objectPolls.optInt("isBoost"), objectPolls.optString("createdUserName")));
            }
            refreshCurrentPollListview();
            mSwipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshCurrentPollListview() {
        if (itemList.size() > 0) {
            mCurrentPollList.setVisibility(View.VISIBLE);
            mPollNoError.setVisibility(View.GONE);
            mPollError.setVisibility(View.GONE);
            mAdapter = new CurrentPollAdapter(act, itemList, 1);
            mAdapter.setOnItemClickListener(this);
            mCurrentPollList.setAdapter(mAdapter);
            mLowerLimit = mLowerLimit + 10;
        } else {
            mLowerLimit = 0;
            mCurrentPollList.setVisibility(View.GONE);
            mPollNoError.setVisibility(View.VISIBLE);
        }
    }

    private void getPollDetailPage(String url) {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.GET, url, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                savePollDetailResponse(response);
            }

            @Override
            public void onRequestFailed(Exception e) {
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
        processor.execute();
    }

    private void savePollDetailResponse(String result) {
        try {
            JSONObject mPollDetailObject = new JSONObject(result);
            JSONArray mQuestionsArray = mPollDetailObject.optJSONArray(ANS_QUESTIONLIST);
            editor.putInt(QUESTION_SIZE, mQuestionsArray.length()).commit();
            if (mQuestionsArray.length() != 0)
                for (int j = 0; j < mQuestionsArray.length(); j++) {
                    JSONObject mQuestions = mQuestionsArray.optJSONObject(j);
                    JSONArray mChoicesArray = mQuestions.optJSONArray(CHOICES);
                    Log.e("Choice Array - " + j, "" + mChoicesArray.toString());

                    editor.putString("QUESTION_SIZE_" + j, "" + mQuestions.optString(QUESTION)).putInt("QUESTION_ID_" + j, mQuestions.optInt(QUESTION_ID)).
                            putString("CHOICE_" + j, "" + mChoicesArray.toString()).commit();
                }
//                setQuestions(mQuestionsArray.length(), mQuestionsArray);
            else
                makeToast("Sorry,No Questions to answer!");

            ((MainHomeActivity) act).setNewFragment(new SurveyDetail(), "Pager", true);
            ((MainHomeActivity) act).setTitle("Survey");

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
//        Methodutils.showListSearch(act, itemList, mCurrentPollList);
        if (v.getId() == R.id.layout_create) {
            ((MainHomeActivity) act).setNewFragment(setPagerFragment(new CreatePollPager(), 2), "Create Poll Pager", true);
        }
        if (v.getId() == R.id.btnCreate) {
            ((MainHomeActivity) act).setNewFragment(setPagerFragment(new CreatePollPager(), 2), "Create Poll Pager", true);
        }
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
