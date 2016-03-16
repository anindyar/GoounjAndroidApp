package com.bvocal.goounj.fragments.vote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bvocal.goounj.R;
import com.bvocal.goounj.adapters.PollPagerAdapter;
import com.bvocal.goounj.fragments.BaseFragment;
import com.bvocal.goounj.views.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 12/1/16.
 */
public class CurrentVoteDetail extends BaseFragment implements ViewPager.OnPageChangeListener {

    CustomViewPager mViewPager;
    List<Fragment> mFragmentList = new ArrayList<>();
    PollPagerAdapter mAdapter;

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String value = getArguments().getString("PAGER_VALUE");
        makeToast("" + value);
        mFragmentList.add(setPagerFragment(new CandidateDetail(),
                mFragmentList.size()));
        mFragmentList.add(setPagerFragment(new CandidateDescription(),
                mFragmentList.size()));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_vote_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        (mViewPager = (CustomViewPager) view.findViewById(R.id.pagerCandidate)).addOnPageChangeListener(this);
        mViewPager.setPageEnabled(false);
//        setNewFragment(new PollOpinion(), R.id.fragment_content_candidate, "Candidate_List", true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new PollPagerAdapter(getChildFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mAdapter);
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
}
