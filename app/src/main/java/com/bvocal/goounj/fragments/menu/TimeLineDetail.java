package com.bvocal.goounj.fragments.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bvocal.goounj.MenuDetailActivity;
import com.bvocal.goounj.R;
import com.bvocal.goounj.adapters.TimeLineAdapter;
import com.bvocal.goounj.fragments.BaseFragment;
import com.bvocal.goounj.interfaces.RestApiListener;
import com.bvocal.goounj.network.RestApiProcessor;
import com.bvocal.goounj.pojo.TimeLine;
import com.bvocal.goounj.activities.poll.CurrentPollDetailActivity;
import com.bvocal.goounj.utils.Methodutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 21/1/16.
 */
public class TimeLineDetail extends BaseFragment implements AdapterView.OnItemClickListener {

    RecyclerView mTimeLineRecycler;
    TimeLineAdapter mAdapter;
    List<TimeLine> mTimeLineItems = new ArrayList<>();
    private TextView mNoRecordsFound;

    //    http://192.168.10.124:3000/users/v1/timeline/3
    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTimeLineItems.clear();
        mAdapter = new TimeLineAdapter(act, mTimeLineItems);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_timeline, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNoRecordsFound = (TextView) view.findViewById(R.id.no_records_found);
        mTimeLineRecycler = (RecyclerView) view.findViewById(R.id.timelineRecyclerview);
        mTimeLineRecycler.setLayoutManager(new LinearLayoutManager(act));
        mTimeLineRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MenuDetailActivity) act).setTitle("TimeLine");
        try {
            getTimeLinePage(BASE_URL + TIMELINE + preferences.getString(USER_ID, "3"));
//            getTimeLinePage(BASE_URL + TIMELINE + "3");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getTimeLinePage(String url) throws Exception {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.GET,
                url, true, new RestApiListener<String>() {
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
        processor.execute();
    }

    private void saveTimeLineResponse(String result) {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<TimeLine>>() {
            }.getType();
            JSONArray responseArray = new JSONArray(result);
            if (responseArray != null && responseArray.length() > 0) {
//                List<TimeLine> list = gson.fromJson(responseArray.toString(), listType);
//                mTimeLineItems.addAll(list);
                mTimeLineItems.clear();
                for (int i = 0; i < responseArray.length(); i++) {
                    JSONObject objectPolls = responseArray.optJSONObject(i);
                    mTimeLineItems.add(new TimeLine(objectPolls.optString("username"), objectPolls.optString("date"), objectPolls.optString("pollName"),
                            objectPolls.optString("createdUser"), objectPolls.optInt("pollId")));
                }

            } else {
                mNoRecordsFound.setVisibility(View.VISIBLE);
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
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
        editor.putInt(POLL_ID, mTimeLineItems.get(position).pollId).putString(POLL_NAME, "" + mTimeLineItems.get(position).pollName).
                putString(CURRENT_CREATED_USER_NAME, "" + mTimeLineItems.get(position).pollId).commit();
        startActivity(new Intent(act, CurrentPollDetailActivity.class).putExtra("poll_id", mTimeLineItems.get(position).pollId).putExtra("poll_type", 3));
    }
}
