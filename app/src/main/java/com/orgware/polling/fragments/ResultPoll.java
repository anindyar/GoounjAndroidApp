package com.orgware.polling.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orgware.polling.HomeActivity;
import com.orgware.polling.R;
import com.orgware.polling.adapters.ChoicesListviewAdapter;
import com.orgware.polling.interfaces.RestApiListener;
import com.orgware.polling.network.RestApiProcessor;
import com.orgware.polling.pojo.ChoicesItem;
import com.orgware.polling.pojo.CurrentPollItem;
import com.orgware.polling.utils.CircularProg;
import com.orgware.polling.utils.Methodutils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by nandagopal on 15/11/15.
 */
public class ResultPoll extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
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
    FrameLayout mFrameLayoutOne, mFrameLayoutTwo, mFrameLayoutThree;
    LinearLayout mLayoutOne, mLayoutTwo, mLayoutThree, mLayoutSubmit;
    RelativeLayout mBackToLayout;

    private CircularProg mQtsOneProgressOne, mQtsOneProgressTwo, mQtsOneProgressTh, mQtsTwoProgressOne, mQtsTwoProgressTwo, mQtsTwoProgressTh,
            mQtsThProgressOne, mQtsThProgressTwo, mQtsThProgressTh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ((HomeActivity) act).mSearchPollsTxt.setVisibility(View.GONE);
        qtsSize = preferences.getInt(RESULT_QUESTION_SIZE, 0);
        if (qtsSize == 1)
            setQtsOneContent();
        else if (qtsSize == 2)
            setQtsTwoContent();
        else if (qtsSize == 3)
            setQtsThreeContent();
        else
            Log.e("Result", "test");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result_poll, container, false);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        (mChoiceListviewOne = (ListView) v.findViewById(R.id.choicesListviewOne)).setOnItemClickListener(this);
        (mChoiceListviewTwo = (ListView) v.findViewById(R.id.choicesListviewTwo)).setOnItemClickListener(this);
        (mChoiceListviewThree = (ListView) v.findViewById(R.id.choicesListviewThree)).setOnItemClickListener(this);
        mBackToLayout = (RelativeLayout) v.findViewById(R.id.layout_statics_back);
        mBackToLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.getSupportFragmentManager().popBackStack(1, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                makeToast("Test");
            }
        });
//        (mResultBack = (ImageView) v.findViewById(R.id.resultBack)).setOnClickListener(this);
        mFrameLayoutOne = (FrameLayout) v.findViewById(R.id.layout_progress_one);
        mFrameLayoutTwo = (FrameLayout) v.findViewById(R.id.layout_progress_two);
        mFrameLayoutThree = (FrameLayout) v.findViewById(R.id.layout_progress_three);
        (mLayoutSubmit = (LinearLayout) v.findViewById(R.id.layout_survey_detail_submit)).setOnClickListener(this);
        (mBtnSubmit = (LinearLayout) v.findViewById(R.id.layout_survey_detail_submit)).setOnClickListener(this);
        mLayoutQtsOne = (LinearLayout) v.findViewById(R.id.layout_survey_detail_qts_one);
        mLayoutQtsTwo = (LinearLayout) v.findViewById(R.id.layout_survey_detail_qts_two);
        mLayoutQtsThree = (LinearLayout) v.findViewById(R.id.layout_survey_detail_qts_three);
        mQuestionTitleOne = (TextView) v.findViewById(R.id.qts_survey_detail_one);
        mQuestionTitleTwo = (TextView) v.findViewById(R.id.qts_survey_detail_two);
        mQuestionTitleThree = (TextView) v.findViewById(R.id.qts_survey_detail_three);
        mLayoutSubmit.setVisibility(View.GONE);
        mFrameLayoutOne.setVisibility(View.VISIBLE);
        mFrameLayoutTwo.setVisibility(View.VISIBLE);
        mFrameLayoutThree.setVisibility(View.VISIBLE);
        initCircularProgressBar(v);
    }

    @Override
    public void setTitle() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivity) act).openSearch.setVisibility(View.GONE);
//        qtsSize = preferences.getInt(RESULT_QUESTION_SIZE, 0);
        if (qtsSize == 1) {
//            setQtsOneContent();
            mChoiceListviewOne.setAdapter(mAdapterOne);
            mQuestionTitleOne.setText(mQuestionOne);
            mLayoutQtsTwo.setVisibility(View.GONE);
            mLayoutQtsThree.setVisibility(View.GONE);
            mQtsOneProgressOne.setProgressWithAnimation(0, 2000);
            mQtsOneProgressTwo.setProgressWithAnimation(72, 1500);
            mQtsOneProgressTh.setProgressWithAnimation(-89, 3000);
        } else if (qtsSize == 2) {
//            setQtsTwoContent();
            mChoiceListviewOne.setAdapter(mAdapterOne);
            mChoiceListviewTwo.setAdapter(mAdapterTwo);
            mQuestionTitleOne.setText(mQuestionOne);
            mQuestionTitleTwo.setText(mQuestionTwo);
            mLayoutQtsThree.setVisibility(View.GONE);
            mQtsOneProgressOne.setProgressWithAnimation(-60, 2000);
            mQtsOneProgressTwo.setProgressWithAnimation(72, 1500);
            mQtsOneProgressTh.setProgressWithAnimation(-89, 3000);
            mQtsTwoProgressOne.setProgressWithAnimation(-90, 2000);
            mQtsTwoProgressTwo.setProgressWithAnimation(85, 1500);
            mQtsTwoProgressTh.setProgressWithAnimation(-54, 3000);
        } else if (qtsSize == 3) {
//            setQtsThreeContent();
            mChoiceListviewOne.setAdapter(mAdapterOne);
            mChoiceListviewTwo.setAdapter(mAdapterTwo);
            mChoiceListviewThree.setAdapter(mAdapterThree);
            mQuestionTitleOne.setText(mQuestionOne);
            mQuestionTitleTwo.setText(mQuestionTwo);
            mQuestionTitleThree.setText(mQuestionThree);
            mQtsOneProgressOne.setProgressWithAnimation(-60, 2000);
            mQtsOneProgressTwo.setProgressWithAnimation(72, 1500);
            mQtsOneProgressTh.setProgressWithAnimation(-89, 3000);
            mQtsTwoProgressOne.setProgressWithAnimation(-90, 2000);
            mQtsTwoProgressTwo.setProgressWithAnimation(85, 1500);
            mQtsTwoProgressTh.setProgressWithAnimation(-54, 3000);
            mQtsThProgressOne.setProgressWithAnimation(-57, 2000);
            mQtsThProgressTwo.setProgressWithAnimation(79, 1500);
            mQtsThProgressTh.setProgressWithAnimation(-51, 3000);
        }

    }

    private void initCircularProgressBar(View v) {
        mQtsOneProgressOne = (CircularProg) v.findViewById(R.id.first_circular_progressbar);
        mQtsOneProgressOne.setProgressBarWidth(20.0f);
        mQtsOneProgressOne.setColor(getResources().getColor(R.color.bg));
        mQtsOneProgressOne.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mQtsOneProgressTwo = (CircularProg) v.findViewById(R.id.second_circular_progressbar);
        mQtsOneProgressTwo.setColor(getResources().getColor(R.color.tab_social));
        mQtsOneProgressTwo.setProgressBarWidth(20.0f);
        mQtsOneProgressTwo.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mQtsOneProgressTh = (CircularProg) v.findViewById(R.id.third_circular_progressbar);
        mQtsOneProgressTh.setColor(getResources().getColor(R.color.tab_opinion));
        mQtsOneProgressTh.setProgressBarWidth(20.0f);
        mQtsOneProgressTh.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mQtsTwoProgressOne = (CircularProg) v.findViewById(R.id.qts_two_first_circular_progressbar);
        mQtsTwoProgressOne.setProgressBarWidth(20.0f);
        mQtsTwoProgressOne.setColor(getResources().getColor(R.color.bg));
        mQtsTwoProgressOne.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mQtsTwoProgressTwo = (CircularProg) v.findViewById(R.id.qts_two_second_circular_progressbar);
        mQtsTwoProgressTwo.setColor(getResources().getColor(R.color.tab_social));
        mQtsTwoProgressTwo.setProgressBarWidth(20.0f);
        mQtsTwoProgressTwo.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mQtsTwoProgressTh = (CircularProg) v.findViewById(R.id.qts_two_third_circular_progressbar);
        mQtsTwoProgressTh.setColor(getResources().getColor(R.color.tab_opinion));
        mQtsTwoProgressTh.setProgressBarWidth(20.0f);
        mQtsTwoProgressTh.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mQtsThProgressOne = (CircularProg) v.findViewById(R.id.qts_th_first_circular_progressbar);
        mQtsThProgressOne.setProgressBarWidth(20.0f);
        mQtsThProgressOne.setColor(getResources().getColor(R.color.bg));
        mQtsThProgressOne.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mQtsThProgressTwo = (CircularProg) v.findViewById(R.id.qts_th_second_circular_progressbar);
        mQtsThProgressTwo.setColor(getResources().getColor(R.color.tab_social));
        mQtsThProgressTwo.setProgressBarWidth(20.0f);
        mQtsThProgressTwo.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mQtsThProgressTh = (CircularProg) v.findViewById(R.id.qts_th_third_circular_progressbar);
        mQtsThProgressTh.setColor(getResources().getColor(R.color.tab_opinion));
        mQtsThProgressTh.setProgressBarWidth(20.0f);
        mQtsThProgressTh.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void setQtsOneContent() {
        try {
            mChoiceArrayOne = new JSONArray("" + preferences.getString("RES_CHOICE_0", ""));
            mQuestionOne = preferences.getString("RES_QUESTION_0", "");
            itemListOne.clear();
            for (int i = 0; i < mChoiceArrayOne.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayOne.optJSONObject(i);
                itemListOne.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("resultCount")));
                Log.e("Qts One", "" + itemListOne.get(i).mChoiceName);
            }
            mAdapterOne = new ChoicesListviewAdapter(act, R.layout.item_choices, itemListOne, 2);
//            editor.putInt(RES_COUNT_0, itemListOne.get(0).mChoiceOptionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setQtsTwoContent() {
        try {
            mChoiceArrayOne = new JSONArray("" + preferences.getString("RES_CHOICE_0", ""));
            mChoiceArrayTwo = new JSONArray("" + preferences.getString("RES_CHOICE_1", ""));
            mQuestionOne = preferences.getString("RES_QUESTION_0", "");
            mQuestionTwo = preferences.getString("RES_QUESTION_1", "");
            itemListOne.clear();
            itemListTwo.clear();
            for (int i = 0; i < mChoiceArrayOne.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayOne.optJSONObject(i);
                itemListOne.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("resultCount")));
                Log.e("Qts One", "" + itemListOne.get(i).mChoiceName);
            }
            for (int i = 0; i < mChoiceArrayTwo.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayTwo.optJSONObject(i);
                itemListTwo.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("resultCount")));
                Log.e("Qts One", "" + itemListTwo.get(i).mChoiceName);
            }
            mAdapterOne = new ChoicesListviewAdapter(act, android.R.layout.simple_list_item_single_choice, itemListOne, 2);
            mAdapterTwo = new ChoicesListviewAdapter(act, android.R.layout.simple_list_item_single_choice, itemListTwo, 2);
//            editor.putInt(RES_COUNT_0, itemListTwo.get(0).mChoiceOptionId);
//            editor.putInt(RES_COUNT_1, itemListTwo.get(1).mChoiceOptionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setQtsThreeContent() {
        try {
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
                itemListOne.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("resultCount")));
                Log.e("Qts One", "" + itemListOne.get(i).mChoiceName);
            }
            for (int i = 0; i < mChoiceArrayTwo.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayTwo.optJSONObject(i);
                itemListTwo.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("resultCount")));
                Log.e("Qts One", "" + itemListTwo.get(i).mChoiceName);
            }
            for (int i = 0; i < mChoiceArrayThree.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayThree.optJSONObject(i);
                itemListThree.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("resultCount")));
                Log.e("Qts One", "" + itemListThree.get(i).mChoiceName);
            }
            mAdapterOne = new ChoicesListviewAdapter(act, android.R.layout.simple_list_item_single_choice, itemListOne, 2);
            mAdapterTwo = new ChoicesListviewAdapter(act, android.R.layout.simple_list_item_single_choice, itemListTwo, 2);
            mAdapterThree = new ChoicesListviewAdapter(act, android.R.layout.simple_list_item_single_choice, itemListThree, 2);
//            editor.putInt(RES_COUNT_0, itemListThree.get(0).mChoiceOptionId);
//            editor.putInt(RES_COUNT_1, itemListThree.get(1).mChoiceOptionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
