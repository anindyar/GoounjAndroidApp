package com.bvocal.goounj.fragments.vote;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bvocal.goounj.R;
import com.bvocal.goounj.activities.poll.CurrentPollDetailActivity;
import com.bvocal.goounj.adapters.ChoicesListviewAdapter;
import com.bvocal.goounj.fragments.BaseFragment;
import com.bvocal.goounj.interfaces.RestApiListener;
import com.bvocal.goounj.network.RestApiProcessor;
import com.bvocal.goounj.pojo.ChoicesItem;
import com.bvocal.goounj.utils.Methodutils;
import com.bvocal.goounj.utils.bargraph.Bar;
import com.bvocal.goounj.utils.bargraph.BarGraph;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nanda on 05/04/16.
 */
public class ResultVote extends BaseFragment {
    List<ChoicesItem> mVoteList = new ArrayList<>();
    ArrayList<Bar> mBarList = new ArrayList<>();
    ChoicesListviewAdapter mAdapter;
    Resources resources;
    String electionName, associationName, startDate, endDate;
    int electionId;
    int value = 0;
    private ListView mListview;
    private BarGraph mBargraph;
    private int[] choiceImage = new int[]{R.drawable.bg_choice_circle_opinion, R.drawable.bg_choice_circle_quick, R.drawable.bg_choice_circle_feedback,
            R.drawable.bg_choice_circle_red, R.drawable.bg_circle_home_green, R.drawable.bg_circle_dark_grey};
    private TextView txtPollTitle, txtPollCreatedBy, txtPollStartDate, txtPollEndDate;

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getArguments() != null) {
                Bundle bundle = getArguments().getBundle("vote_bundle");
                electionId = bundle.getInt("electionId");
                electionName = bundle.getString("electionName");
                associationName = bundle.getString("associationName");
                startDate = bundle.getString("startDate");
                endDate = bundle.getString("endDate");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        mVoteList.clear();
//        for (int i = 0; i < 3; i++) {
//            if (i == 1)
//                mVoteList.add(new ChoicesItem("Test", i, "40"));
//            else
//                mVoteList.add(new ChoicesItem("Test", i, "20"));
//        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vote_result, container, false);
    }

    @Override
    public void onViewCreated(View itemView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(itemView, savedInstanceState);
        resources = getResources();
        txtPollTitle = (TextView) itemView.findViewById(R.id.poll_title);
        txtPollCreatedBy = (TextView) itemView.findViewById(R.id.poll_createdBy);
        txtPollStartDate = (TextView) itemView.findViewById(R.id.poll_startDate);
        txtPollEndDate = (TextView) itemView.findViewById(R.id.poll_endDate);
        mListview = (ListView) itemView.findViewById(R.id.voteChoiceListVieView);
        mBargraph = (BarGraph) itemView.findViewById(R.id.bargraph);

        txtPollTitle.setText("" + electionName);
        txtPollCreatedBy.setText("Created By: " + associationName);
        txtPollStartDate.setText("" + startDate);
        txtPollEndDate.setText("" + endDate);
//        for (int i = 0; i < mVoteList.size(); i++) {
//            ChoicesItem item = mVoteList.get(i);
//            Bar bar = new Bar();
//            bar.setColor(resources.getColor(Methodutils.mResultColor[i]));
//            bar.setName("");
//            bar.setValue(Integer.parseInt(item.mChoicePercent));
//            mBarList.add(bar);
//        }
//        mBargraph.setBars(mBarList);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            mVoteList.clear();
            pushRequestForOtp(BASE_URL + VOTE_RESULT + electionId, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showResult(String response) throws Exception {
        try {
            JSONArray objectArray = new JSONArray(response);
            for (int i = 0; i < objectArray.length(); i++) {
                JSONObject jsonObject = objectArray.optJSONObject(i);
                mVoteList.add(new ChoicesItem(jsonObject.optInt("candidateId"), jsonObject.optString("candidate"),
                        "" + jsonObject.optInt("votes")));
            }
            for (int i = 0; i < mVoteList.size(); i++) {
                value = value + Integer.parseInt(mVoteList.get(i).mChoicePercent);
            }
            for (int i = 0; i < mVoteList.size(); i++) {
                ChoicesItem item = mVoteList.get(i);
                Bar bar = new Bar();
                bar.setColor(resources.getColor(Methodutils.mResultColor[i]));
                bar.setName("");
                bar.setValue((Integer.parseInt(item.mChoicePercent) / value) * 100);
                mBarList.add(bar);
            }
            mBargraph.setBars(mBarList);
            mAdapter = new ChoicesListviewAdapter(act, R.layout.item_choices, mVoteList, 2);
            mListview.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pushRequestForOtp(String url, boolean pullDownType) {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.GET, url, pullDownType, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                Log.e("Poll List Response", "" + response.toString());
                if (response.equals("[]")) {
                    makeToast("No Records Found");
                    act.getSupportFragmentManager().popBackStack();
                } else
                    try {
                        showResult(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onRequestFailed(Exception e) {
                if (((CurrentPollDetailActivity) act).getSupportFragmentManager().getBackStackEntryCount() > 1)
                    act.getSupportFragmentManager().popBackStack();
                else
                    act.finish();
                makeToast("No results found for the requested poll");
            }
        });
        processor.execute();
    }

}
