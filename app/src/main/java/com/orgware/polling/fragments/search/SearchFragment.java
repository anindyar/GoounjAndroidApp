package com.orgware.polling.fragments.search;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orgware.polling.R;
import com.orgware.polling.adapters.SearchListAdapter;
import com.orgware.polling.fragments.BaseFragment;
import com.orgware.polling.interfaces.RestApiListener;
import com.orgware.polling.network.RestApiProcessor;
import com.orgware.polling.pojo.Search_SurveyPoll;
import com.orgware.polling.utils.Methodutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 4/2/16.
 */
public class SearchFragment extends BaseFragment implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    private List<Search_SurveyPoll> mSearchSurvey;
    private SearchListAdapter mSearchListAdapter;
    private RecyclerView mSearchRecyclerView;
    private int mTypeOfPage;
    private RelativeLayout mNoPoll;

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNoPoll = (RelativeLayout) view.findViewById(R.id.layout_no_poll_error);
        mSearchRecyclerView = (RecyclerView) view.findViewById(R.id.searchlist);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSearchSurvey = new ArrayList<>();
        mSearchListAdapter = new SearchListAdapter(act, mSearchSurvey, 1);
        mSearchListAdapter.setOnItemClickListener(this);
        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(act));
        mSearchRecyclerView.setAdapter(mSearchListAdapter);

        if (mSearchSurvey.size() == 0) {
            mNoPoll.setVisibility(View.VISIBLE);
            mSearchRecyclerView.setVisibility(View.GONE);
        } else {
            mNoPoll.setVisibility(View.GONE);
            mSearchRecyclerView.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_searchview, menu);
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
        super.onCreateOptionsMenu(menu, inflater);
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
        try {
            searchSurvey(query, getArguments().getString(TYPE).equals(SURVEY) ? true : false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void searchSurvey(String value, boolean survey) throws Exception {
        mSearchSurvey.clear();
        JSONObject object = new JSONObject();
        object.put(searchString, value);
        if (!survey)
            object.put(userId, preferences.getString(USER_ID, ""));

        RestApiProcessor mProcessor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.POST,
                BASE_URL + (survey ? SEARCH_SURVEY : SEARCH_POLL), true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {

                saveTimeLineResponse(response);
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

                for (int i = 0; i < responseArray.length(); i++) {
                    JSONObject objectPolls = responseArray.optJSONObject(i);
                    if (objectPolls.optString("isAnswered").equals("0")) {
                        mSearchSurvey.add(new Search_SurveyPoll(objectPolls.optInt("pollId"), splitFromString("" + objectPolls.optString("endDate")), splitFromString("" + objectPolls.optString("startDate")),
                                objectPolls.optString("createdUserName"), objectPolls.optString("pollName")));
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
                act.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
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
        return false;
    }
}
