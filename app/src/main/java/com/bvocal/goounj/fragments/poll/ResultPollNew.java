package com.bvocal.goounj.fragments.poll;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bvocal.goounj.R;
import com.bvocal.goounj.activities.results.ResultActivity;
import com.bvocal.goounj.adapters.ChoicesListviewAdapter;
import com.bvocal.goounj.fragments.BaseFragment;
import com.bvocal.goounj.interfaces.RestApiListener;
import com.bvocal.goounj.network.RestApiProcessor;
import com.bvocal.goounj.pojo.ChoicesItem;
import com.bvocal.goounj.activities.poll.CurrentPollDetailActivity;
import com.bvocal.goounj.utils.Methodutils;
import com.bvocal.goounj.utils.piegraph.PieGraph;
import com.bvocal.goounj.utils.piegraph.PieSlice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 3/2/16.
 */
public class ResultPollNew extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    ListView mChoiceListviewOne, mChoiceListviewTwo, mChoiceListviewThree;
    ChoicesListviewAdapter mAdapterOne, mAdapterTwo, mAdapterThree;
    List<ChoicesItem> itemListOne = new ArrayList<>();
    List<ChoicesItem> itemListTwo = new ArrayList<>();
    List<ChoicesItem> itemListThree = new ArrayList<>();
    int qtsSize, mQtsIdOne, mQtsIdTwo, mQtsIdThree;
    String mQuestionOne, mQuestionTwo, mQuestionThree;
    JSONArray mChoiceArrayOne, mChoiceArrayTwo, mChoiceArrayThree, mSubmitValuesArray;
    LinearLayout mLayoutQtsOne, mLayoutQtsTwo, mLayoutQtsThree, mBtnSubmit;
    TextView mQuestionTitleOne, mQuestionTitleTwo, mQuestionTitleThree;
    LinearLayout mLayoutOne, mLayoutTwo, mLayoutThree, mLayoutSubmit;
    RelativeLayout mBackToLayout;
    Resources resources;
    PieGraph mPieOne, mPieTwo, mPieThree;
    int mPollId;
    private int mTotalCountOne, mTotalCountTwo, mTotalCountThree;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPollId = getArguments().getInt(PAGER_COUNT);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result_poll, container, false);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        resources = getResources();
        mPieOne = (PieGraph) v.findViewById(R.id.piegraphOne);
        mPieTwo = (PieGraph) v.findViewById(R.id.piegraphTwo);
        mPieThree = (PieGraph) v.findViewById(R.id.piegraphThree);
        (mChoiceListviewOne = (ListView) v.findViewById(R.id.choicesListviewOne)).setOnItemClickListener(this);
        (mChoiceListviewTwo = (ListView) v.findViewById(R.id.choicesListviewTwo)).setOnItemClickListener(this);
        (mChoiceListviewThree = (ListView) v.findViewById(R.id.choicesListviewThree)).setOnItemClickListener(this);
        mBackToLayout = (RelativeLayout) v.findViewById(R.id.layout_statics_back);
        (mLayoutSubmit = (LinearLayout) v.findViewById(R.id.layout_survey_detail_submit)).setOnClickListener(this);
        (mBtnSubmit = (LinearLayout) v.findViewById(R.id.layout_survey_detail_submit)).setOnClickListener(this);
        mLayoutQtsOne = (LinearLayout) v.findViewById(R.id.layout_survey_detail_qts_one);
        mLayoutQtsTwo = (LinearLayout) v.findViewById(R.id.layout_survey_detail_qts_two);
        mLayoutQtsThree = (LinearLayout) v.findViewById(R.id.layout_survey_detail_qts_three);
        mQuestionTitleOne = (TextView) v.findViewById(R.id.qts_survey_detail_one);
        mQuestionTitleTwo = (TextView) v.findViewById(R.id.qts_survey_detail_two);
        mQuestionTitleThree = (TextView) v.findViewById(R.id.qts_survey_detail_three);
        mLayoutSubmit.setVisibility(View.GONE);
    }

    @Override
    public void setTitle() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            if (preferences.getInt(DASHBOARD_ID, 0) == 0) {
                ((ResultActivity) act).getSupportActionBar().setTitle("Poll");
                getResultPollForCreatedUser(BASE_URL + RESULT_URL + mPollId);
            } else {
                ((ResultActivity) act).getSupportActionBar().setTitle("Survey");
                getResultPollForCreatedUser(BASE_URL + SURVEY_RESULT_URL + mPollId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setQtsOneContent(PieGraph pieGraphOne) {
        try {
            PieSlice slice;
            mChoiceArrayOne = new JSONArray("" + preferences.getString("RES_CHOICE_0", ""));
            mQuestionOne = preferences.getString("RES_QUESTION_0", "");
            itemListOne.clear();
            for (int i = 0; i < mChoiceArrayOne.length(); i++) {
                final JSONObject mChoiceObject = mChoiceArrayOne.optJSONObject(i);
                itemListOne.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("resultCount"), mChoiceObject.optString("percentage")));
                mTotalCountOne = mTotalCountOne + itemListOne.get(i).mChoiceOptionId;
                Log.e("Qts One", "" + itemListOne.get(i).mChoiceName + " - One1 -" + mTotalCountOne);
                if (!mChoiceObject.optString("percentage").equals("0 %")) {
                    slice = new PieSlice();
                    slice.setColor(resources.getColor(Methodutils.mResultColor[i]));
//                testCount = ((itemList.get(i).mChoiceOptionId * 100) / mTotal);
                    String percentValue = mChoiceObject.optString("percentage");
                    percentValue = percentValue.replace("%", "");
                    slice.setValue(Float.parseFloat(percentValue));
                    slice.setTitle("first");
                    pieGraphOne.addSlice(slice);
                }
            }
            editor.putInt("mTotalCountOne", mChoiceArrayOne.length()).putInt("mTotalOne", mTotalCountOne).commit();
//            editor.putInt(RES_COUNT_0, itemListOne.get(0).mChoiceOptionId);
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_app_icon);
            pieGraphOne.setBackgroundBitmap(b);
            pieGraphOne.setInnerCircleRatio(128);
            pieGraphOne.setPadding(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setQtsTwoContent(PieGraph pieGraphOne, PieGraph pieGraphTwo) {
        try {
            PieSlice slice;
            mChoiceArrayOne = new JSONArray("" + preferences.getString("RES_CHOICE_0", ""));
            mChoiceArrayTwo = new JSONArray("" + preferences.getString("RES_CHOICE_1", ""));
            mQuestionOne = preferences.getString("RES_QUESTION_0", "");
            mQuestionTwo = preferences.getString("RES_QUESTION_1", "");
            itemListOne.clear();
            itemListTwo.clear();
            for (int i = 0; i < mChoiceArrayOne.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayOne.optJSONObject(i);
                itemListOne.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("resultCount"), mChoiceObject.optString("percentage")));
                mTotalCountOne = mTotalCountOne + itemListOne.get(i).mChoiceOptionId;
                Log.e("Qts One", "" + itemListOne.get(i).mChoiceName + " - One2 -" + mTotalCountOne);
                if (!mChoiceObject.optString("percentage").equals("0 %")) {
                    slice = new PieSlice();
                    slice.setColor(resources.getColor(Methodutils.mResultColor[i]));
//                testCount = ((itemList.get(i).mChoiceOptionId * 100) / mTotal);
                    String percentValue = mChoiceObject.optString("percentage");
                    percentValue = percentValue.replace("%", "");
                    slice.setValue(Float.parseFloat(percentValue));
                    slice.setTitle("first");
                    pieGraphOne.addSlice(slice);
                }
            }
            for (int i = 0; i < mChoiceArrayTwo.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayTwo.optJSONObject(i);
                itemListTwo.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("resultCount"), mChoiceObject.optString("percentage")));
                mTotalCountTwo = mTotalCountTwo + itemListTwo.get(i).mChoiceOptionId;
                Log.e("Qts One", "" + itemListTwo.get(i).mChoiceName + " - Two2 - " + mTotalCountTwo);
                if (!mChoiceObject.optString("percentage").equals("0 %")) {
                    slice = new PieSlice();
                    slice.setColor(resources.getColor(Methodutils.mResultColor[i]));
//                testCount = ((itemList.get(i).mChoiceOptionId * 100) / mTotal);
                    String percentValue = mChoiceObject.optString("percentage");
                    percentValue = percentValue.replace("%", "");
                    slice.setValue(Float.parseFloat(percentValue));
                    slice.setTitle("first");
                    pieGraphTwo.addSlice(slice);
                }
            }
            editor.putInt("mTotalCountOne", mChoiceArrayOne.length()).putInt("mTotalOne", mTotalCountOne).
                    putInt("mTotalCountTwo", mChoiceArrayTwo.length()).putInt("mTotalTwo", mTotalCountTwo).commit();
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_app_icon);
            pieGraphOne.setBackgroundBitmap(b);
            pieGraphOne.setInnerCircleRatio(128);
            pieGraphOne.setPadding(2);
            pieGraphTwo.setBackgroundBitmap(b);
            pieGraphTwo.setInnerCircleRatio(128);
            pieGraphTwo.setPadding(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setQtsThreeContent(PieGraph pieGraphOne, PieGraph pieGraphTwo, PieGraph pieGraphThree) {
        try {
            PieSlice slice;
            mChoiceArrayOne = new JSONArray("" + preferences.getString("RES_CHOICE_0", ""));
            mChoiceArrayTwo = new JSONArray("" + preferences.getString("RES_CHOICE_1", ""));
            mChoiceArrayThree = new JSONArray("" + preferences.getString("RES_CHOICE_2", ""));
            mQuestionOne = preferences.getString("RES_QUESTION_0", "");
            mQuestionTwo = preferences.getString("RES_QUESTION_1", "");
            mQuestionThree = preferences.getString("RES_QUESTION_2", "");
            itemListOne.clear();
            itemListTwo.clear();
            itemListThree.clear();
            for (int i = 0; i < mChoiceArrayOne.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayOne.optJSONObject(i);
                itemListOne.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("resultCount"), mChoiceObject.optString("percentage")));
                mTotalCountOne = mTotalCountOne + itemListOne.get(i).mChoiceOptionId;
                Log.e("Qts One", "" + itemListOne.get(i).mChoiceName + " - One3 -" + mTotalCountOne);
                if (!mChoiceObject.optString("percentage").equals("0 %")) {
                    slice = new PieSlice();
                    slice.setColor(resources.getColor(Methodutils.mResultColor[i]));
//                testCount = ((itemList.get(i).mChoiceOptionId * 100) / mTotal);
                    String percentValue = mChoiceObject.optString("percentage");
                    percentValue = percentValue.replace("%", "");
                    slice.setValue(Float.parseFloat(percentValue));
                    slice.setTitle("first");
                    pieGraphOne.addSlice(slice);
                }
            }
            for (int i = 0; i < mChoiceArrayTwo.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayTwo.optJSONObject(i);
                itemListTwo.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("resultCount"), mChoiceObject.optString("percentage")));
                mTotalCountTwo = mTotalCountTwo + itemListTwo.get(i).mChoiceOptionId;
                Log.e("Qts One", "" + itemListTwo.get(i).mChoiceName + " - Two3 -" + mTotalCountThree);
                if (!mChoiceObject.optString("percentage").equals("0 %")) {
                    slice = new PieSlice();
                    slice.setColor(resources.getColor(Methodutils.mResultColor[i]));
//                testCount = ((itemList.get(i).mChoiceOptionId * 100) / mTotal);
                    String percentValue = mChoiceObject.optString("percentage");
                    percentValue = percentValue.replace("%", "");
                    slice.setValue(Float.parseFloat(percentValue));
                    slice.setTitle("first");
                    pieGraphTwo.addSlice(slice);
                }
            }
            for (int i = 0; i < mChoiceArrayThree.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayThree.optJSONObject(i);
                itemListThree.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("resultCount"), mChoiceObject.optString("percentage")));
                mTotalCountThree = mTotalCountThree + itemListThree.get(i).mChoiceOptionId;
                Log.e("Qts One", "" + itemListThree.get(i).mChoiceName + " - Three3 -" + mTotalCountThree);
                if (!mChoiceObject.optString("percentage").equals("0 %")) {
                    slice = new PieSlice();
                    slice.setColor(resources.getColor(Methodutils.mResultColor[i]));
//                testCount = ((itemList.get(i).mChoiceOptionId * 100) / mTotal);
                    String percentValue = mChoiceObject.optString("percentage");
                    percentValue = percentValue.replace("%", "");
                    slice.setValue(Float.parseFloat(percentValue));
                    slice.setTitle("first");
                    pieGraphThree.addSlice(slice);
                }
            }
//            editor.putInt("mTotalCountOne", mChoiceArrayOne.length()).putInt("mTotalCountTwo", mChoiceArrayTwo.length()).putInt("mTotalCountThree", mChoiceArrayThree.length()).commit();
            editor.putInt("mTotalCountOne", mChoiceArrayOne.length()).putInt("mTotalOne", mTotalCountOne).
                    putInt("mTotalCountTwo", mChoiceArrayTwo.length()).putInt("mTotalTwo", mTotalCountTwo).
                    putInt("mTotalCountThree", mChoiceArrayThree.length()).putInt("mTotalThree", mTotalCountThree).commit();
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_app_icon);
            pieGraphOne.setBackgroundBitmap(b);
            pieGraphOne.setInnerCircleRatio(128);
            pieGraphOne.setPadding(2);
            pieGraphTwo.setBackgroundBitmap(b);
            pieGraphTwo.setInnerCircleRatio(128);
            pieGraphTwo.setPadding(2);
            pieGraphThree.setBackgroundBitmap(b);
            pieGraphThree.setInnerCircleRatio(128);
            pieGraphThree.setPadding(2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public Animator.AnimatorListener getAnimationListener() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
            return new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.d("piefrag", "anim end");
                }

                @Override
                public void onAnimationCancel(Animator animation) {//you might want to call slice.setvalue(slice.getGoalValue)
                    Log.d("piefrag", "anim cancel");
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            };
        else return null;

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
        switch (view.getId()) {
            case R.id.choicesListviewOne:
                editor.putInt(OPTION_ONE, itemListOne.get(position).mChoiceOptionId).commit();
                break;
            case R.id.choicesListviewTwo:
                editor.putInt(OPTION_TWO, itemListOne.get(position).mChoiceOptionId).commit();
                break;
            case R.id.choicesListviewThree:
                editor.putInt(OPTION_THREE, itemListOne.get(position).mChoiceOptionId).commit();
                break;
        }
    }


    private void getResultPollForCreatedUser(String url) {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.GET, url, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                if (response.equals("[]")) {
                    makeToast("No Records Found");
                    act.getSupportFragmentManager().popBackStack();
                } else
                    try {
                        showResultPollList(response);
//                        ((CurrentPollDetailActivity) act).setNewFragment(new ResultPollNew(), "", true);
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

    public void showResultPollList(String response) {
        try {
            JSONObject mPollDetailObject = new JSONObject(response);
            JSONArray mQuestionsArray = mPollDetailObject.optJSONArray(ANS_QUESTIONLIST);
            qtsSize = mQuestionsArray.length();
            editor.putInt(RESULT_QUESTION_SIZE, mQuestionsArray.length()).commit();
            for (int j = 0; j < mQuestionsArray.length(); j++) {
                JSONObject mQuestions = mQuestionsArray.optJSONObject(j);
                JSONArray mChoicesArray = mQuestions.optJSONArray(CHOICES);
                Log.e("Choice Array - " + j, "" + mChoicesArray.toString());
                editor.putString("RES_QUESTION_" + j, "" + mQuestions.optString(QUESTION)).
                        putString("RES_CHOICE_" + j, "" + mChoicesArray.toString()).commit();
            }

            try {
                setValuesForViews(qtsSize);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setValuesForViews(int qtsSize) throws Exception {
        switch (qtsSize) {
            case 1:
                try {
                    setQtsOneContent(mPieOne);

                    mAdapterOne = new ChoicesListviewAdapter(act, R.layout.item_choices, itemListOne, 2);
                    mChoiceListviewOne.setAdapter(mAdapterOne);
                    mQuestionTitleOne.setText(mQuestionOne);
                    mLayoutQtsTwo.setVisibility(View.GONE);
                    mLayoutQtsThree.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    setQtsTwoContent(mPieOne, mPieTwo);
                    mAdapterOne = new ChoicesListviewAdapter(act, R.layout.item_choices, itemListOne, 2);
                    mAdapterTwo = new ChoicesListviewAdapter(act, R.layout.item_choices, itemListTwo, 2);
                    mChoiceListviewOne.setAdapter(mAdapterOne);
                    mChoiceListviewTwo.setAdapter(mAdapterTwo);
                    mQuestionTitleOne.setText(mQuestionOne);
                    mQuestionTitleTwo.setText(mQuestionTwo);
                    mLayoutQtsThree.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    setQtsThreeContent(mPieOne, mPieTwo, mPieThree);
                    mAdapterOne = new ChoicesListviewAdapter(act, R.layout.item_choices, itemListOne, 2);
                    mAdapterTwo = new ChoicesListviewAdapter(act, R.layout.item_choices, itemListTwo, 2);
                    mAdapterThree = new ChoicesListviewAdapter(act, R.layout.item_choices, itemListThree, 2);
                    mChoiceListviewOne.setAdapter(mAdapterOne);
                    mChoiceListviewTwo.setAdapter(mAdapterTwo);
                    mChoiceListviewThree.setAdapter(mAdapterThree);
                    mQuestionTitleOne.setText(mQuestionOne);
                    mQuestionTitleTwo.setText(mQuestionTwo);
                    mQuestionTitleThree.setText(mQuestionThree);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}

