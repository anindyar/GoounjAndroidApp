package com.bvocal.goounj.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bvocal.goounj.R;
import com.bvocal.goounj.adapters.SearchListAdapter;
import com.bvocal.goounj.interfaces.RestApiListener;
import com.bvocal.goounj.network.RestApiProcessor;
import com.bvocal.goounj.pojo.Search_SurveyPoll;
import com.bvocal.goounj.utils.Methodutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 2/2/16.
 */
public class SearchList_SurveyPoll extends BaseFragment implements SearchView.OnQueryTextListener {

    private List<Search_SurveyPoll> mSearchSurvey;
    private SearchListAdapter mSearchListAdapter;
    private RecyclerView mSearchRecyclerView;

    @Override
    public void setTitle() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchSurvey = new ArrayList<>();
        mSearchListAdapter = new SearchListAdapter(getActivity(), mSearchSurvey, 1);
        if (getArguments() != null)
            getActivity().setTitle(getArguments().getString(TYPE).equals(SURVEY) ? "Search Survey" : "Search Poll");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_searchlist, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchRecyclerView = (RecyclerView) view.findViewById(R.id.searchlist);
        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSearchRecyclerView.setAdapter(mSearchListAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            searchSurvey(query, getArguments().getString(TYPE).equals(SURVEY) ? true : false);
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
        JSONObject object = new JSONObject();
        object.put(searchString, value);
        if (survey)
            object.put(userId, preferences.getString(USER_ID, ""));

        RestApiProcessor mProcessor = new RestApiProcessor(getActivity(), RestApiProcessor.HttpMethod.POST,
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
            mSearchSurvey.clear();
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
                getActivity().finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
