package com.orgware.polling.fragments.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orgware.polling.MainHomeActivity;
import com.orgware.polling.MenuDetailActivity;
import com.orgware.polling.R;
import com.orgware.polling.adapters.TimeLineAdapter;
import com.orgware.polling.fragments.BaseFragment;
import com.orgware.polling.fragments.CurrentPoll;
import com.orgware.polling.fragments.HomeDashboard;
import com.orgware.polling.fragments.SurveyDetail;
import com.orgware.polling.interfaces.RestApiListener;
import com.orgware.polling.network.RestApiProcessor;
import com.orgware.polling.pojo.TimeLineItem;
import com.orgware.polling.utils.Methodutils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 21/1/16.
 */
public class TimeLineDetail extends BaseFragment {

    RecyclerView mTimeLineRecycler;
    TimeLineAdapter mAdapter;
    List<TimeLineItem> mTimeLineItems = new ArrayList<>();

    //    http://192.168.10.124:3000/users/v1/timeline/3
    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTimeLineItems.clear();
        String me;
        for (int i = 0; i < 10; i++) {
            if (i == 3 || i == 5 || i == 9)
                me = "me";
            else
                me = "nanda";
            mTimeLineItems.add(new TimeLineItem("Jan 22", "Title", "Creator", me));
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_timeline, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTimeLineRecycler = (RecyclerView) view.findViewById(R.id.timelineRecyclerview);
        mTimeLineRecycler.setLayoutManager(new LinearLayoutManager(act));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MenuDetailActivity) act).setTitle("TimeLine");

        try {
            getTimeLinePage(BASE_URL + TIMELINE + preferences.getString(USER_ID, ""));
            refreshRecyclerView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTimeLinePage(String url) throws Exception {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.GET, url, true, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                saveTimeLineResponse(response);
            }

            @Override
            public void onRequestFailed(Exception e) {
                Methodutils.messageWithTitle(act, "Failed", "" + e.getMessage(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        act.finish();
                    }
                });
            }
        });
        processor.execute();
    }

    private void saveTimeLineResponse(String result) {
        try {
            String username;
            JSONArray mTimeLineArray = new JSONArray(result);
            for (int j = 0; j < mTimeLineArray.length(); j++) {
                JSONObject mQuestions = mTimeLineArray.optJSONObject(j);
                if (mQuestions.optString("username").equals("" + mQuestions.optString("createdUser")))
                    username = "You";
                else
                    username = "" + mQuestions.optString("createdUser");
                mTimeLineItems.add(new TimeLineItem(splitDateFromString("" + mQuestions.optString("date")), mQuestions.optString("pollName"), "" + username));
            }
//            refreshRecyclerView();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshRecyclerView() throws Exception {
        mAdapter = new TimeLineAdapter(act, mTimeLineItems);
        mTimeLineRecycler.setAdapter(mAdapter);
    }

}
