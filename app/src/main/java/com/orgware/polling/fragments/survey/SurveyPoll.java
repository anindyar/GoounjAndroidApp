package com.orgware.polling.fragments.survey;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orgware.polling.MainHomeActivity;
import com.orgware.polling.R;
import com.orgware.polling.adapters.ContactGridviewAdapter;
import com.orgware.polling.adapters.CurrentPollAdapter;
import com.orgware.polling.database.GoounjDatabase;
import com.orgware.polling.fragments.BaseFragment;
import com.orgware.polling.fragments.CreatePollPager;
import com.orgware.polling.fragments.SurveyDetail;
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
public class SurveyPoll extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, SearchView.OnQueryTextListener {

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainHomeActivity) act).setTitle("Survey");
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
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
            }
        });

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


    private String showPollParams() {
        JSONObject mShowPollOnject = new JSONObject();
        try {
            mShowPollOnject.put(USER_ID, "" + preferences.getString(USER_ID, ""));
            mShowPollOnject.put("lowerLimit", 0);
            mShowPollOnject.put("upperLimit", 10);
            mShowPollOnject.put("isAnswered", "2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mShowPollOnject.toString();
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
                mCurrentPollList.setVisibility(View.GONE);
                mPollNoError.setVisibility(View.GONE);
                mPollError.setVisibility(View.VISIBLE);
                makeToast("Failed to connect to server");
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
            ((MainHomeActivity) act).setNewFragment(new CreatePollPager(), "Create Poll Pager", true);
        }
        if (v.getId() == R.id.btnCreate) {
            ((MainHomeActivity) act).setNewFragment(new CreatePollPager(), "Create Poll Pager", true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHint("Search");
        searchEditText.setBackgroundColor(Color.TRANSPARENT);
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        mAdapter.setFilter(itemList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
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
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Called when the query text is changed by the user.
     *
     * @param newText the new content of the query text field.
     * @return false if the SearchView should perform the default action of showing any
     * suggestions if available, true if the action was handled by the listener.
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        final List<CurrentPollItem> filteredModelList = filter(itemList, newText);
        mAdapter.setFilter(filteredModelList);
        return true;
    }

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