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
    List<TimeLineItem> timeLineItems = new ArrayList<>();

    //    http://192.168.10.124:3000/users/v1/timeline/3
    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String me;
        for (int i = 0; i < 10; i++) {
            if (i == 3 || i == 5 || i == 9)
                me = "me";
            else
                me = "nanda";
            timeLineItems.add(new TimeLineItem("Jan 22", "Title", "Creator", me));
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
        mAdapter = new TimeLineAdapter(act, timeLineItems);
        mTimeLineRecycler.setAdapter(mAdapter);
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
}
