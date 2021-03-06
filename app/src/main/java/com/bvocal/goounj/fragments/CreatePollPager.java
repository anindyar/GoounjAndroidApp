package com.bvocal.goounj.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bvocal.goounj.R;
import com.bvocal.goounj.adapters.PollPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 10/22/15.
 */
public class CreatePollPager extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener, CompoundButton.OnCheckedChangeListener {
    RadioButton mTabOpinion, mTabQuick, mTabSurvey, mTabSocial;
    ViewPager mViewPager;
    PollPagerAdapter mAdapter;
    List<Fragment> mFragmentList;
    RadioGroup mRadioGroup;
    int mPollType, mMyPoll;

    @Override
    public void setTitle() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPollType = getArguments().getInt("page_type");
        mMyPoll = getArguments().getInt("myPoll");

//<<<<<<< HEAD
////        makeToast("" + mMyPoll);
//=======
//>>>>>>> 14d03947a80bfeb2ed2e2a6b031bbaf1c06ed5e7
        if (mPollType == 1) {

            mFragmentList = new ArrayList<>();
            mFragmentList.clear();
            mFragmentList.add(setPageFrg(new PollOpinion(),
                    mPollType, mMyPoll));
            mFragmentList.add(setPageFrg(new PollQuick(),
                    mPollType, mMyPoll));
            mFragmentList.add(setPageFrg(new PollSurvey(),
                    mPollType, mMyPoll));
            mFragmentList.add(setPageFrg(new PollSocial(),
                    mPollType, mMyPoll));
        } else {
            mFragmentList = new ArrayList<>();
            mFragmentList.clear();
            mFragmentList.add(setPageFrg(new PollOpinion(),
                    mPollType, mMyPoll));
            mFragmentList.add(setPageFrg(new PollQuick(),
                    mPollType, mMyPoll));
            mFragmentList.add(setPageFrg(new PollSurvey(),
                    mPollType, mMyPoll));
        }

        mAdapter = new PollPagerAdapter(
                getChildFragmentManager(), mFragmentList);
    }

    private Fragment setPageFrg(Fragment frg, int mPollType, int myPoll) {
        Bundle bundle = new Bundle();
        bundle.putInt("page_type", mPollType);
        bundle.putInt("myPoll", myPoll);
        frg.setArguments(bundle);
        return frg;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_poll_pager, container, false);
        mRadioGroup = (RadioGroup) v.findViewById(R.id.tab_radio);
        mTabOpinion = (RadioButton) v.findViewById(R.id.tab_opinion);
        mTabQuick = (RadioButton) v.findViewById(R.id.tab_quick);
        mTabSurvey = (RadioButton) v.findViewById(R.id.tab_survey);
        mTabSocial = (RadioButton) v.findViewById(R.id.tab_social);
        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setOnPageChangeListener(this);
        mTabOpinion.setOnCheckedChangeListener(this);
        mTabQuick.setOnCheckedChangeListener(this);
        mTabSurvey.setOnCheckedChangeListener(this);
        mTabSocial.setOnCheckedChangeListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewPager.setAdapter(mAdapter);
        if (mPollType == 1)
            mTabSocial.setVisibility(View.VISIBLE);
        else
            mTabSocial.setVisibility(View.GONE);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {
//        enableTabOnPoll(position);
        enablePosition(position, mPollType);

    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void enablePosition(int position, int mPollType) {
        switch (mPollType) {
            case 1:
                if (position == 0)
                    mTabOpinion.setChecked(true);
                else if (position == 1)
                    mTabQuick.setChecked(true);
                else if (position == 2)
                    mTabSurvey.setChecked(true);
                else if (position == 3)
                    mTabSocial.setChecked(true);
                break;
            case 2:
                if (position == 0)
                    mTabOpinion.setChecked(true);
                else if (position == 1)
                    mTabQuick.setChecked(true);
                else if (position == 2)
                    mTabSurvey.setChecked(true);
                break;

        }
    }

    private void enableTabOnPoll(int position) {
        switch (position) {
            case 0:
                mTabOpinion.setChecked(true);
//                ((HomeActivity) act).mPageTitle.setText("Opinion Poll");
                break;
            case 1:
                mTabQuick.setChecked(true);
//                ((HomeActivity) act).mPageTitle.setText("Quick Poll");
                break;
            case 2:
                mTabSurvey.setChecked(true);
//                ((HomeActivity) act).mPageTitle.setText("Survey Poll");
                break;
            case 3:
                mTabSocial.setChecked(true);
//                ((HomeActivity) act).mPageTitle.setText("Social Poll");
                break;
        }
    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mTabOpinion.isChecked()) {
            mViewPager.setCurrentItem(0);
            mTabOpinion.setTextColor(getResources().getColor(android.R.color.white));
        } else
            mTabOpinion.setTextColor(getResources().getColor(android.R.color.black));
        if (mTabQuick.isChecked()) {
            mTabQuick.setTextColor(getResources().getColor(android.R.color.white));
            mViewPager.setCurrentItem(1);
        } else
            mTabQuick.setTextColor(getResources().getColor(android.R.color.black));
        if (mTabSurvey.isChecked()) {
            mTabSurvey.setTextColor(getResources().getColor(android.R.color.white));
            mViewPager.setCurrentItem(2);
        } else
            mTabSurvey.setTextColor(getResources().getColor(android.R.color.black));
        if (mTabSocial.isChecked()) {
            mTabSocial.setTextColor(getResources().getColor(android.R.color.white));
            mViewPager.setCurrentItem(3);
        } else
            mTabSocial.setTextColor(getResources().getColor(android.R.color.black));
    }

}
