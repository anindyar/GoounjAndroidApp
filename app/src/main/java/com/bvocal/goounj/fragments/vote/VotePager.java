package com.bvocal.goounj.fragments.vote;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.bvocal.goounj.R;
import com.bvocal.goounj.adapters.PollPagerAdapter;
import com.bvocal.goounj.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 11/1/16.
 */
public class VotePager extends BaseFragment implements ViewPager.OnPageChangeListener, CompoundButton.OnCheckedChangeListener {

    List<Fragment> mFragmentList = new ArrayList<>();
    PollPagerAdapter mPagerAdapter;
    ViewPager mViewPager;
    RadioButton mCurrentVoteRadio, mHistoryVoteRadio;

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentList.clear();
        mFragmentList.add(setPagerFragment(new CurrentVote(),
                mFragmentList.size()));
        mFragmentList.add(setPagerFragment(new HistoryVote(),
                mFragmentList.size()));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vote_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        (mViewPager = (ViewPager) view.findViewById(R.id.pager_vote)).addOnPageChangeListener(this);
        ((mCurrentVoteRadio = (RadioButton) view.findViewById(R.id.radio_current_vote))).setOnCheckedChangeListener(this);
        ((mHistoryVoteRadio = (RadioButton) view.findViewById(R.id.radio_history_vote))).setOnCheckedChangeListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCurrentVoteRadio.setChecked(true);
        mPagerAdapter = new PollPagerAdapter(getChildFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mPagerAdapter);
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
        if (position == 0)
            mCurrentVoteRadio.setChecked(true);
        else
            mHistoryVoteRadio.setChecked(true);
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

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mCurrentVoteRadio.isChecked()) {
            mViewPager.setCurrentItem(0);
            mCurrentVoteRadio.setTextColor(Color.WHITE);
        } else
            mCurrentVoteRadio.setTextColor(Color.BLACK);
        if (mHistoryVoteRadio.isChecked()) {
            mViewPager.setCurrentItem(1);
            mHistoryVoteRadio.setTextColor(Color.WHITE);
        } else
            mHistoryVoteRadio.setTextColor(Color.BLACK);
    }
}
