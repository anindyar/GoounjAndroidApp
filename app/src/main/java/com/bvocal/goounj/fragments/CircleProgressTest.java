package com.bvocal.goounj.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bvocal.goounj.R;
import com.bvocal.goounj.utils.CircularProg;

/**
 * Created by Nandagopal on 31-Oct-15.
 */
public class CircleProgressTest extends BaseFragment implements View.OnClickListener {

    int completed, total, result, mul;
    ScrollView mScrollView;
    RelativeLayout layout_statistics_back;
    TextView mQtsOne;
    RadioButton mChoiceRadioQtsOneOne, mChoiceRadioQtsOneTwo, mChoiceRadioQtsOneThree, mChoiceRadioQtsOneFour, mChoiceRadioQtsOneFive,
            mChoiceRadioQtsTwoOne, mChoiceRadioQtsTwoTwo, mChoiceRadioQtsTwoThree, mChoiceRadioQtsTwoFour, mChoiceRadioQtsTwoFive,
            mChoiceRadioQtsThOne, mChoiceRadioQtsThTwo, mChoiceRadioQtsThThree, mChoiceRadioQtsThFour, mChoiceRadioQtsThFive;
    LinearLayout mLayoutOne, mLayoutTwo, mLayoutThree;
    int qtsSize;
    private CircularProg mQtsOneProgressOne, mQtsOneProgressTwo, mQtsOneProgressTh, mQtsTwoProgressOne, mQtsTwoProgressTwo, mQtsTwoProgressTh,
            mQtsThProgressOne, mQtsThProgressTwo, mQtsThProgressTh;

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qtsSize = preferences.getInt(QUESTION_SIZE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.circle_prog_test, container, false);
        initCircularProgressBar(v);
        mLayoutOne = (LinearLayout) v.findViewById(R.id.result_qts_one);
        mLayoutTwo = (LinearLayout) v.findViewById(R.id.result_qts_two);
        mLayoutThree = (LinearLayout) v.findViewById(R.id.result_qts_th);
        mQtsOne = (TextView) v.findViewById(R.id.qts_detail_one);
        mScrollView.setVerticalScrollbarPosition(ScrollView.FOCUS_DOWN);
        mQtsOneProgressOne.setProgressWithAnimation(-75, 2000);
        mQtsOneProgressTwo.setProgressWithAnimation(85, 1500);
        mQtsOneProgressTh.setProgressWithAnimation(-95, 3000);
        mQtsTwoProgressOne.setProgressWithAnimation(-75, 2000);
        mQtsTwoProgressTwo.setProgressWithAnimation(85, 1500);
        mQtsTwoProgressTh.setProgressWithAnimation(-95, 3000);
        mQtsThProgressOne.setProgressWithAnimation(-75, 2000);
        mQtsThProgressTwo.setProgressWithAnimation(85, 1500);
        mQtsThProgressTh.setProgressWithAnimation(-95, 3000);
        mQtsOne.setText(preferences.getString(QUESTION_SIZE_0, ""));
        return v;
    }

    private void initCircularProgressBar(View v) {
        mScrollView = (ScrollView) v.findViewById(R.id.scrollViewStat);
        layout_statistics_back = (RelativeLayout) v.findViewById(R.id.layout_statics_back);
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
        layout_statistics_back.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_statics_back:
                act.getSupportFragmentManager().popBackStack(2, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
    }
}
