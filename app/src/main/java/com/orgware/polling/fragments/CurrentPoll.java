package com.orgware.polling.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orgware.polling.HomeActivity;
import com.orgware.polling.MainHomeActivity;
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
public class CurrentPoll extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, SearchView.OnQueryTextListener {

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
    private SuperSwipeRefreshLayout swipeRefreshLayout;
    // Header View
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
//        ((HomeActivity)act).mPageTitle.setText("Current Poll");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        db = new GoounjDatabase(act);
        itemList = new ArrayList<>();
        singleItemList = new ArrayList<>();
        dashboardId = preferences.getInt(DASHBOARD_ID, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_poll_listview, container, false);
        mLayoutManager = new LinearLayoutManager(act);
        mCurrentPollList = (RecyclerView) v.findViewById(R.id.currentPollListview);
        swipeRefreshLayout = (SuperSwipeRefreshLayout) v.findViewById(R.id.swipe_refresh);
        mCurrentPollList.setLayoutManager(mLayoutManager);
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
//        ((HomeActivity) act).mSearchPollsTxt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!s.toString().equals("")) {
//                    List<CurrentPollItem> filteredTitles = new ArrayList<>();
//                    for (int i = 0; i < itemList.size(); i++) {
//                        if (itemList.get(i).mCurrentPollTitle.toString().toLowerCase().contains(s) ||
//                                itemList.get(i).mCurrentPollTitle.toString().toUpperCase().contains(s) ||
//                                itemList.get(i).mCurrentPollTitle.toString().contains(s)) {
//                            filteredTitles.add(itemList.get(i));
//                        }
//                    }
//                    mAdapter = new CurrentPollAdapter(act, filteredTitles, 1);
//                    mCurrentPollList.setAdapter(mAdapter);
////                    mAdapter = new ContactGridviewAdapter(act, filteredTitles);
////                    mRecyclerView.setAdapter(mAdapter);
//                } else {
//                    mAdapter = new CurrentPollAdapter(act, itemList, 1);
//                    mCurrentPollList.setAdapter(mAdapter);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() > 0)
//                    Log.e("Search", "Yes");
//                else
//                    Log.e("Search", "No");
////                    makeToast("No records found");
//
//            }
//        });
        return v;
    }

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.menu_group_three_search).setVisible(true);
//        super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (dashboardId == 0) {
            ((MainHomeActivity) act).setTitle("Poll");
        } else {
            ((MainHomeActivity) act).setTitle("Survey");
        }
//        ((HomeActivity) act).mPageTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_poll_logo, 0, 0, 0);
//        ((HomeActivity) act).openSearch.setVisibility(View.VISIBLE);
//        ((HomeActivity) act).openSearch.setOnClickListener(this);
//        ((HomeActivity) act).openHome.setVisibility(View.VISIBLE);
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
        mCurrentPollList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             * <p>
             * This callback will also be called if visible item range changes after a layout
             * calculation. In that case, dx and dy will be 0.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx           The amount of horizontal scroll.
             * @param dy           The amount of vertical scroll.
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached

                    Log.i("Yaeye!", "end called");

                    // Do something
                    makeToast("End Called");

                    loading = true;
                }
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
                ((MainHomeActivity) act).setNewFragment(new CurrentPollPager(), "Pager", true);
//                ((MainHomeActivity) act).mSearchPollsTxt.setText("Poll");
                ((MainHomeActivity) act).setTitle("Poll");
            } else if (dashboardId == 1) {
                ((MainHomeActivity) act).setNewFragment(new SurveyDetail(), "Pager", true);
                ((MainHomeActivity) act).setTitle("Survey");
            } else
                Log.e("No DashBoard", "" + dashboardId);

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
        Methodutils.showListSearch(act, itemList, mCurrentPollList);
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
