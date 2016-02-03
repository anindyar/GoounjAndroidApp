package com.orgware.polling;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orgware.polling.BaseActivity;
import com.orgware.polling.R;
import com.orgware.polling.adapters.SearchListAdapter;
import com.orgware.polling.interfaces.RestApiListener;
import com.orgware.polling.network.RestApiProcessor;
import com.orgware.polling.pojo.Search_SurveyPoll;
import com.orgware.polling.pojo.TimeLine;

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
public class SearchToolbarActivity extends BaseActivity implements SearchView.OnQueryTextListener {


    private List<Search_SurveyPoll> mSearchSurvey;
    private SearchListAdapter mSearchListAdapter;
    private RecyclerView mSearchRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_searchview);
        mSearchSurvey = new ArrayList<>();
        mSearchListAdapter = new SearchListAdapter(activity, mSearchSurvey, 1);
        mSearchRecyclerView = (RecyclerView) findViewById(R.id.searchlist);
        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mSearchRecyclerView.setAdapter(mSearchListAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setTitleTextAppearance(this, R.style.MyTitleTextApperance);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().getExtras() != null)
            activity.setTitle(getIntent().getExtras().getString(TYPE).equals(SURVEY) ? "Search Survey" : "Search Poll");
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

    private void searchSurvey(String value, boolean survey) throws Exception {
        mSearchSurvey.clear();
        JSONObject object = new JSONObject();
        object.put(searchString, value);
        if (!survey)
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
                    Toast.makeText(SearchToolbarActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();


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
                mSearchSurvey.addAll(list);
            }
            mSearchListAdapter.notifyDataSetChanged();
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
}
