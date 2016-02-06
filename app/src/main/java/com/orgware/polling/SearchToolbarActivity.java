package com.orgware.polling;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orgware.polling.BaseActivity;
import com.orgware.polling.R;
import com.orgware.polling.adapters.SearchListAdapter;
import com.orgware.polling.fragments.CurrentPollPager;
import com.orgware.polling.fragments.poll.ResultPollNew;
import com.orgware.polling.interfaces.RestApiListener;
import com.orgware.polling.network.RestApiProcessor;
import com.orgware.polling.pojo.CurrentPollItem;
import com.orgware.polling.pojo.Search_SurveyPoll;
import com.orgware.polling.pojo.TimeLine;
import com.orgware.polling.pollactivities.CurrentPollDetailActivity;
import com.orgware.polling.utils.Methodutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 2/2/16.
 */
public class SearchToolbarActivity extends BaseActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {


    private List<Search_SurveyPoll> mSearchSurvey;
    private SearchListAdapter mSearchListAdapter;
    private RecyclerView mSearchRecyclerView;
    private int mTypeOfPage;
    private RelativeLayout mNoPoll;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_searchview);
        mSearchSurvey = new ArrayList<>();
        mNoPoll = (RelativeLayout) findViewById(R.id.layout_no_poll_error);
        mSearchListAdapter = new SearchListAdapter(activity, mSearchSurvey, 1);
        mSearchListAdapter.setOnItemClickListener(this);
        mSearchRecyclerView = (RecyclerView) findViewById(R.id.searchlist);
        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mSearchRecyclerView.setAdapter(mSearchListAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setTitleTextAppearance(this, R.style.MyTitleTextApperance);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            activity.setTitle(getIntent().getExtras().getString(TYPE).equals(SURVEY) ? "Search Survey" : "Search Poll");
            mTypeOfPage = getIntent().getExtras().getInt("PAGE_TYPE");
            Log.e("Search Type", "" + mTypeOfPage);
        }

        if (mSearchSurvey.size() == 0) {
            mNoPoll.setVisibility(View.VISIBLE);
            mSearchRecyclerView.setVisibility(View.GONE);
        } else {
            mNoPoll.setVisibility(View.GONE);
            mSearchRecyclerView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_searchview, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);
        searchView.onActionViewExpanded();
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
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            searchSurvey(query, getIntent().getExtras().getString(TYPE).equals(SURVEY) ? true : false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return true;
    }

//    private void setFragValues(int type) {
//        switch (type) {
//            case 0:
//                break;
//            case 1:
//                break;
//            case 2:
//                break;
//            case 3:
//                break;
//            case 4:
//                break;
//        }
//    }

    private void searchSurvey(String value, boolean survey) throws Exception {
        mSearchSurvey.clear();
        JSONObject object = new JSONObject();
        object.put(searchString, value);
        object.put(userId, preferences.getString(USER_ID, ""));

        RestApiProcessor mProcessor = new RestApiProcessor(this, RestApiProcessor.HttpMethod.POST,
                BASE_URL + (survey ? SEARCH_SURVEY : SEARCH_POLL), true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {

                saveTimeLineResponse(response);
            }

            @Override
            public void onRequestFailed(Exception e) {
                if (e instanceof SocketTimeoutException)
                    makeToast(activity, "Something went wrong");
                else
                    makeToast(activity, "No Surveys Found");

            }
        });
        mProcessor.execute(object.toString());
    }

    private void saveTimeLineResponse(String result) {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Search_SurveyPoll>>() {
            }.getType();
            JSONArray responseArray = new JSONArray(result);
            if (responseArray != null && responseArray.length() > 0) {
                List<Search_SurveyPoll> list = gson.fromJson(responseArray.toString(), listType);

                if (mTypeOfPage == 0) {
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject objectPolls = responseArray.optJSONObject(i);
                        if (objectPolls.optString("isAnswered").equals("0")) {
                            mSearchSurvey.add(new Search_SurveyPoll(objectPolls.optInt("pollId"), splitFromString("" + objectPolls.optString("endDate")), splitFromString("" + objectPolls.optString("startDate")),
                                    objectPolls.optString("createdUserName"), objectPolls.optString("pollName")));
                        }
                    }
                } else if (mTypeOfPage == 1) {
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject objectPolls = responseArray.optJSONObject(i);
                        if (objectPolls.optString("isAnswered").equals("1")) {
                            mSearchSurvey.add(new Search_SurveyPoll(objectPolls.optInt("pollId"), splitFromString("" + objectPolls.optString("endDate")), splitFromString("" + objectPolls.optString("startDate")),
                                    objectPolls.optString("createdUserName"), objectPolls.optString("pollName")));
                        }
                    }
                } else if (mTypeOfPage == 2) {
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject objectPolls = responseArray.optJSONObject(i);
                        if (objectPolls.optString("isAnswered").equals("2")) {
                            mSearchSurvey.add(new Search_SurveyPoll(objectPolls.optInt("pollId"), splitFromString("" + objectPolls.optString("endDate")), splitFromString("" + objectPolls.optString("startDate")),
                                    objectPolls.optString("createdUserName"), objectPolls.optString("pollName")));
                        }
                    }
                } else if (mTypeOfPage == 3) {
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject objectPolls = responseArray.optJSONObject(i);
                        if (objectPolls.optString("isSurvey").equals("1")) {
                            mSearchSurvey.add(new Search_SurveyPoll(objectPolls.optInt("pollId"), splitFromString("" + objectPolls.optString("endDate")), splitFromString("" + objectPolls.optString("startDate")),
                                    objectPolls.optString("createdUserName"), objectPolls.optString("pollName")));
                        }
                    }
                } else if (mTypeOfPage == 4) {
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject objectPolls = responseArray.optJSONObject(i);
                        if (objectPolls.optInt("createdUserId") == Integer.parseInt(preferences.getString(USER_ID, "0"))) {
                            mSearchSurvey.add(new Search_SurveyPoll(objectPolls.optInt("pollId"), splitFromString("" + objectPolls.optString("endDate")), splitFromString("" + objectPolls.optString("startDate")),
                                    objectPolls.optString("createdUserName"), objectPolls.optString("pollName")));
                        }
                    }
                }

//                mSearchSurvey.addAll(list);
            }
            mSearchListAdapter.notifyDataSetChanged();
            if (mSearchSurvey.size() == 0) {
                mNoPoll.setVisibility(View.VISIBLE);
                mSearchRecyclerView.setVisibility(View.GONE);
            } else {
                mNoPoll.setVisibility(View.GONE);
                mSearchRecyclerView.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
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

        switch (mTypeOfPage) {
            case 0:
                editor.putInt(POLL_ID, mSearchSurvey.get(position).pollId).putString(POLL_NAME, "" + mSearchSurvey.get(position).pollName).
                        putString(CURRENT_CREATED_USER_NAME, "" + mSearchSurvey.get(position).createdUserName).commit();
                startActivity(new Intent(this, CurrentPollDetailActivity.class).putExtra("poll_id", mSearchSurvey.get(position).pollId).putExtra("poll_type", 1));
                Log.e("Poll Id", "" + mSearchSurvey.get(position).pollId);

                break;
            case 1:
                editor.putInt(POLL_ID, mSearchSurvey.get(position).pollId).putString(POLL_NAME, "" + mSearchSurvey.get(position).pollName).
                        putString(CURRENT_CREATED_USER_NAME, "" + mSearchSurvey.get(position).createdUserName).commit();
                startActivity(new Intent(this, CurrentPollDetailActivity.class).putExtra("poll_id", mSearchSurvey.get(position).pollId).putExtra("poll_type", 2));
                Log.e("Poll Id", "" + mSearchSurvey.get(position).pollId);
                break;
            case 2:
                editor.putInt(POLL_ID, mSearchSurvey.get(position).pollId).putString(POLL_NAME, "" + mSearchSurvey.get(position).pollName).
                        putString(CURRENT_CREATED_USER_NAME, "" + mSearchSurvey.get(position).createdUserName).commit();
                startActivity(new Intent(this, CurrentPollDetailActivity.class).putExtra("poll_id", mSearchSurvey.get(position).pollId).putExtra("poll_type", 3));
                break;
            case 3:
                editor.putInt(POLL_ID, mSearchSurvey.get(position).pollId).putString(POLL_NAME, "" + mSearchSurvey.get(position).pollName).
                        putString(CURRENT_CREATED_USER_NAME, "" + mSearchSurvey.get(position).createdUserName).commit();
//        getPollDetailPage(BASE_URL + SHOW_POLL_URL + itemList.get(position).currentPollId);
                startActivity(new Intent(activity, CurrentPollDetailActivity.class).putExtra("poll_id", mSearchSurvey.get(position).pollId).putExtra("poll_type", 4));
                Log.e("Poll Id", "" + mSearchSurvey.get(position).pollId);
                break;
            case 4:
                editor.putInt(POLL_ID, mSearchSurvey.get(position).pollId).putString(POLL_NAME, "" + mSearchSurvey.get(position).pollName).
                        putString(CURRENT_CREATED_USER_NAME, "" + mSearchSurvey.get(position).createdUserName).commit();
                startActivity(new Intent(activity, CurrentPollDetailActivity.class).putExtra("poll_id", mSearchSurvey.get(position).pollId).putExtra("poll_type", 5));
                Log.e("Poll Id", "" + mSearchSurvey.get(position).pollId);
                break;
        }

    }

    private void getPollDetailPage(String url) {
        RestApiProcessor processor = new RestApiProcessor(activity, RestApiProcessor.HttpMethod.GET, url, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                savePollDetailResponse(response);
            }

            @Override
            public void onRequestFailed(Exception e) {
                Methodutils.messageWithTitle(activity, "Failed", "" + e.getMessage(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        act.getSupportFragmentManager().popBackStack();
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
            if (mQuestionsArray.length() != 0) {
                for (int j = 0; j < mQuestionsArray.length(); j++) {
                    JSONObject mQuestions = mQuestionsArray.optJSONObject(j);
                    JSONArray mChoicesArray = mQuestions.optJSONArray(CHOICES);
                    Log.e("Choice Array - " + j, "" + mChoicesArray.toString());

                    editor.putString("QUESTION_SIZE_" + j, "" + mQuestions.optString(QUESTION)).putInt("QUESTION_ID_" + j, mQuestions.optInt(QUESTION_ID)).
                            putString("CHOICE_" + j, "" + mChoicesArray.toString()).commit();
                }
//                setQuestions(mQuestionsArray.length(), mQuestionsArray);
            } else
                makeToast(this, "Sorry,No Questions to answer!");

//            ((MainHomeActivity) act).setNewFragment(setPagerFragment(new CurrentPollPager(), 1), "Pager", true);
//            ((MainHomeActivity) act).setTitle("Poll");
            startActivity(new Intent(activity, SearchDetailActivity.class).putExtra("Search_Detail", 1));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getResultPollForCreatedUser(String url) {
        RestApiProcessor processor = new RestApiProcessor(activity, RestApiProcessor.HttpMethod.GET, url, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
//                mHistoryPollList.setVisibility(View.VISIBLE);
//                mPollError.setVisibility(View.GONE);
                if (response.equals("[]"))
                    makeToast(activity, "No Records Found");
                else
                    try {
                        showResultPollList(response);
//                        ((MainHomeActivity) activity).setNewFragment(new ResultPollNew(), "", true);
//                        ((HomeActivity) act).mSearchPollsTxt.setVisibility(View.GONE);
                        startActivity(new Intent(activity, SearchDetailActivity.class).putExtra("Search_Detail", 2));
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
                    Methodutils.message(activity, "Internal Server Error. Requested Action Failed", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.getSupportFragmentManager().popBackStack();
                        }
                    });
                } else {
                    if (e.getMessage() == null)
                        Methodutils.message(activity, "No Records Found", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                activity.getSupportFragmentManager().popBackStack();
                            }
                        });
                    else
                        Methodutils.message(activity, "Try again, Failed to connect to server", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                activity.getSupportFragmentManager().popBackStack();
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

}
