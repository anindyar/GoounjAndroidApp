package com.orgware.polling.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orgware.polling.HomeActivity;
import com.orgware.polling.R;
import com.orgware.polling.adapters.ContactGridviewAdapter;
import com.orgware.polling.adapters.CurrentPollAdapter;
import com.orgware.polling.database.GoounjDatabase;
import com.orgware.polling.interfaces.RestApiListener;
import com.orgware.polling.network.NetworkHelper;
import com.orgware.polling.network.RestApiProcessor;
import com.orgware.polling.pojo.ContactItem;
import com.orgware.polling.pojo.CurrentPollItem;
import com.orgware.polling.utils.Methodutils;
import com.orgware.polling.utils.SuperSwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by nandagopal on 26/10/15.
 */
public class CurrentPoll extends BaseFragment implements AdapterView.OnItemClickListener {

    RecyclerView mCurrentPollList;
    CurrentPollAdapter mAdapter;
    List<CurrentPollItem> itemList;
    List<CurrentPollItem> singleItemList;
    int limit = 15, dashboardId;
    GoounjDatabase db;
    boolean currentPollService;
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
//        ((HomeActivity)act).mPageTitle.setText("Current Poll");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new GoounjDatabase(act);
        itemList = new ArrayList<>();
        singleItemList = new ArrayList<>();
        dashboardId = preferences.getInt(DASHBOARD_ID, 0);
        Log.e("DashBoard", "" + dashboardId);
//        splitFromString("2015-10-31T09:16:02.000Z");
//        if (dashboardId == 0)
//            ((HomeActivity) act).mSearchPollsTxt.setText("Poll");
//        else
//            ((HomeActivity) act).mSearchPollsTxt.setText("Survey");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_poll_listview, container, false);
        mCurrentPollList = (RecyclerView) v.findViewById(R.id.currentPollListview);
        swipeRefreshLayout = (SuperSwipeRefreshLayout) v.findViewById(R.id.swipe_refresh);
        mCurrentPollList.setLayoutManager(new LinearLayoutManager(act));
        mPollNoError = (RelativeLayout) v.findViewById(R.id.layout_no_poll_error);
        mPollError = (RelativeLayout) v.findViewById(R.id.layout_poll_error);
//        db = new GoounjDatabase(act);
        currentPollService = preferences.getBoolean(CURRENT_POLLDB, true);
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
                            getPollForCreatedUser(BASE_URL + SHOW_POLL_FOR_AUDIENCE, false);
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
                    mAdapter = new CurrentPollAdapter(act, filteredTitles, 1);
                    mCurrentPollList.setAdapter(mAdapter);
//                    mAdapter = new ContactGridviewAdapter(act, filteredTitles);
//                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter = new CurrentPollAdapter(act, itemList, 1);
                    mCurrentPollList.setAdapter(mAdapter);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivity) act).mPageTitle.setText("Poll");
        ((HomeActivity) act).mPageTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_poll_logo, 0, 0, 0);
        ((HomeActivity) act).openSearch.setVisibility(View.VISIBLE);
        if (preferences.getInt(DASHBOARD_ID, 0) == 1) {
//            ((HomeActivity) act).mPageTitle.setText("Survey Poll");
            ((HomeActivity) act).openHome.setVisibility(View.VISIBLE);
        } else {
//            ((HomeActivity) act).mPageTitle.setText("Current Poll");
            ((HomeActivity) act).openHome.setVisibility(View.VISIBLE);
        }
        if (NetworkHelper.checkActiveInternet(act))
            getPollForCreatedUser(BASE_URL + SHOW_POLL_FOR_AUDIENCE, true);
        else
            Methodutils.messageWithTitle(act, "No Internet connection", "Please check your internet connection", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    act.getSupportFragmentManager().popBackStack();
                    return;
                }
            });

//        getPollForCreatedUser("http://192.168.0.112:3000/polls/v1/pollList");
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
        getPollDetailPage(BASE_URL + SHOW_POLL_URL + itemList.get(position).currentPollId);
    }

    private View createFooterView() {
        View footerView = LayoutInflater.from(swipeRefreshLayout.getContext())
                .inflate(R.layout.layout_footer, null);
        footerProgressBar = (ProgressBar) footerView
                .findViewById(R.id.footer_pb_view);
        footerImageView = (ImageView) footerView
                .findViewById(R.id.footer_image_view);
        footerTextView = (TextView) footerView
                .findViewById(R.id.footer_text_view);
        footerProgressBar.setVisibility(View.GONE);
        footerImageView.setVisibility(View.VISIBLE);
        footerImageView.setImageResource(R.drawable.arrow_refresh);
        footerTextView.setText("Push");
        return footerView;
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

    private String showPollParams() {
        JSONObject mShowPollOnject = new JSONObject();
        try {
            mShowPollOnject.put(USER_ID, "" + preferences.getString(USER_ID, ""));
//            mShowPollOnject.put(USER_ID, "3");
            mShowPollOnject.put(POLL_LIMIT, limit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mShowPollOnject.toString();
    }

    private void getPollForCreatedUser(String url, boolean pullDownType) {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.POST, url, pullDownType, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                mCurrentPollList.setVisibility(View.VISIBLE);
                mPollError.setVisibility(View.GONE);
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
                mPollError.setVisibility(View.VISIBLE);
                makeToast("Failed to connect to server");
//                if (e == null) {
//                    Log.e("Error", "" + e.getMessage());
//                    makeToast("Failed to connect to server");
////                    Methodutils.message(act, "Internal Server Error. Requested Action Failed", new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            act.getSupportFragmentManager().popBackStack();
////                        }
////                    });
//                } else {
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
            if (preferences.getInt(DASHBOARD_ID, 0) == 0) {
                for (int i = 0; i < objectArray.length(); i++) {
                    JSONObject objectPolls = objectArray.optJSONObject(i);
                    Log.e("Array Values", "" + i);
                    if (objectPolls.optString("isAnswered").equals("0")) {
                        itemList.add(new CurrentPollItem(objectPolls.optInt("pollId"), splitFromString("" + objectPolls.optString("startDate")), splitFromString("" + objectPolls.optString("endDate")),
                                objectPolls.optString("pollName"), objectPolls.optInt("isBoost"), objectPolls.optString("createdUserName")));
                    }
                }
            }
            if (preferences.getInt(DASHBOARD_ID, 0) == 1) {
                for (int i = 0; i < objectArray.length(); i++) {
                    JSONObject objectPolls = objectArray.optJSONObject(i);
                    Log.e("Array Values", "" + i);
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
            mCurrentPollList.setVisibility(View.VISIBLE);
            mPollNoError.setVisibility(View.GONE);
            mPollError.setVisibility(View.GONE);
            mAdapter = new CurrentPollAdapter(act, itemList, 1);
            mAdapter.setOnItemClickListener(this);
            mCurrentPollList.setAdapter(mAdapter);
        } else {
            mCurrentPollList.setVisibility(View.GONE);
            mPollNoError.setVisibility(View.VISIBLE);
        }
    }

    private void getPollDetailPage(String url) {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.GET, url, true, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                savePollDetailResponse(response);
            }

            @Override
            public void onRequestFailed(Exception e) {
                Methodutils.messageWithTitle(act, "Failed", "" + e.getMessage(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        act.getSupportFragmentManager().popBackStack();
                    }
                });
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
            if (dashboardId == 0) {
                ((HomeActivity) act).setNewFragment(new CurrentPollPager(), "Pager", true);
                ((HomeActivity) act).mSearchPollsTxt.setText("Poll");
            } else if (dashboardId == 1) {
                ((HomeActivity) act).setNewFragment(new SurveyDetail(), "Pager", true);
                ((HomeActivity) act).mSearchPollsTxt.setText("Survey");
            } else
                Log.e("No DashBoard", "" + dashboardId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setQuestions(int questionLength, JSONArray mQuestionssArray) {
        if (questionLength == 1) {
            for (int j = 0; j < mQuestionssArray.length(); j++) {
                JSONObject mQuestions = mQuestionssArray.optJSONObject(j);
                JSONArray mChoicesArray = mQuestions.optJSONArray(CHOICES);
                Log.e("Choice Array - " + j, "" + mChoicesArray.toString());

                editor.putString("QUESTION_SIZE_" + j, "" + mQuestions.optString(QUESTION)).putInt("QUESTION_ID_" + j, mQuestions.optInt(QUESTION_ID)).
                        putString("CHOICE_" + j, "" + mChoicesArray.toString()).commit();
//                        .putInt(CHOICE_SIZE_1, mChoicesArray.length()).commit();
//                setChoices(j, mChoicesArray.length(), mChoicesArray);
            }
        }
        if (questionLength == 2) {
            for (int j = 0; j < mQuestionssArray.length(); j++) {
                JSONObject mQuestions = mQuestionssArray.optJSONObject(j);
                JSONArray mChoicesArray = mQuestions.optJSONArray(CHOICES);
                Log.e("Choice Array - " + j, "" + mChoicesArray.toString());
                editor.putString("QUESTION_SIZE_" + j, "" + mQuestions.optString(QUESTION)).putString("CHOICE_" + j, "" + mChoicesArray.toString()).commit();
                Log.e("QUESTION_SIZE_" + j, "" + preferences.getString("QUESTION_SIZE_" + j, ""));
//                setChoices(j, mChoicesArray.length(), mChoicesArray);
            }
        }
        if (questionLength == 3) {
            for (int j = 0; j < mQuestionssArray.length(); j++) {
                editor.putString("QUESTION_SIZE_" + j, "" + mQuestionssArray.optString(j)).commit();
                JSONObject mQuestions = mQuestionssArray.optJSONObject(j);
                JSONArray mChoicesArray = mQuestions.optJSONArray(CHOICES);
                editor.putString("QUESTION_SIZE_" + j, "" + mQuestions.optString(QUESTION)).putString("CHOICE_" + j, "" + mChoicesArray.toString()).commit();
                Log.e("Choice Array - " + j, "" + mChoicesArray.toString());
//                editor.putInt(QUESTION_SIZE, 1).putInt(CHOICE_1, mChoicesArray.length()).commit();
//                setChoices(j, mChoicesArray.length(), mChoicesArray);
            }
        }
    }

    private void setChoices(int i, int mChoicesLength, JSONArray mChoicesArray) {
        editor.putInt("CHOICE_SIZE_" + i, mChoicesLength).commit();
        Log.e("Choice Size - " + i, "" + mChoicesLength);
        if (mChoicesLength == 1) {
            for (int j = 0; j < mChoicesArray.length(); j++) {
                JSONObject mChoiceObject = mChoicesArray.optJSONObject(j);
                Log.e("Choice Object - " + i, "" + mChoiceObject.toString());
                editor.putString("CHOICE_" + i + "_" + j, "" + mChoiceObject.optString("choice")).
                        putInt("OPTION_" + i + "_" + j, mChoiceObject.optInt("optionId")).commit();

            }
        }
        if (mChoicesLength == 2) {
            for (int j = 0; j < mChoicesArray.length(); j++) {
//                editor.putString("CHOICE_" + i + "_" + j, "" + mChoicesArray.optString(j));
                JSONObject mChoiceObject = mChoicesArray.optJSONObject(j);
                Log.e("Choice Object - " + i, "" + mChoiceObject.toString());
                editor.putString("CHOICE_" + i + "_" + j, "" + mChoiceObject.optString("choice")).
                        putInt("OPTION_" + i + "_" + j, mChoiceObject.optInt("optionId")).commit();
            }
        }
        if (mChoicesLength == 3) {
            for (int j = 0; j < mChoicesArray.length(); j++) {
                Log.e("CHOICE_" + i + "_" + j, "" + mChoicesArray.optString(j));
//                editor.putString("CHOICE_" + i + "_" + j, "" + mChoicesArray.optString(j));
                JSONObject mChoiceObject = mChoicesArray.optJSONObject(j);
                Log.e("Choice Object - " + i, "" + mChoiceObject.toString());
                editor.putString("CHOICE_" + i + "_" + j, "" + mChoiceObject.optString("choice")).
                        putInt("OPTION_" + i + "_" + j, mChoiceObject.optInt("optionId")).commit();
            }
        }
        if (mChoicesLength == 4) {
            for (int j = 0; j < mChoicesArray.length(); j++) {
//                Log.e("" + j, "" + mChoicesArray.optString(j));
//                editor.putString("CHOICE_" + i + "_" + j, "" + mChoicesArray.optString(j));
                JSONObject mChoiceObject = mChoicesArray.optJSONObject(j);
                editor.putString("CHOICE_" + i + "_" + j, "" + mChoiceObject.optString("choice")).
                        putInt("OPTION_" + i + "_" + j, mChoiceObject.optInt("optionId")).commit();
            }
        }
        if (mChoicesLength == 5) {
            for (int j = 0; j < mChoicesArray.length(); j++) {
//                Log.e("" + j, "" + mChoicesArray.optString(j));
//                editor.putString("CHOICE_" + i + "_" + j, "" + mChoicesArray.optString(j));
                JSONObject mChoiceObject = mChoicesArray.optJSONObject(j);
                editor.putString("CHOICE_" + i + "_" + j, "" + mChoiceObject.optString("choice")).
                        putInt("OPTION_" + i + "_" + j, mChoiceObject.optInt("optionId")).commit();
            }
        }
    }

}
