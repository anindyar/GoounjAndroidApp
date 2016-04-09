package com.bvocal.goounj.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.bvocal.goounj.R;
import com.bvocal.goounj.adapters.PollPagerAdapter;
import com.bvocal.goounj.fragments.poll.MyPoll;
import com.bvocal.goounj.SearchToolbarActivity;
import com.bvocal.goounj.activities.poll.PollCreateActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 26/10/15.
 */
public class ShowPollPager extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener,
        AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {
    RadioButton mTabOpinion, mTabQuick, mTabSurvey;
    ViewPager mViewPager;
    PollPagerAdapter mAdapter;
    List<Fragment> mFragmentList;
    //    GridView mPollGrid;
//    List<GridItems> mGridList;
//    GridTabAdapter mGridAdapter;
    LinearLayout mLayoutCreate;
    Button btnCreate;

    @Override
    public void setTitle() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mFragmentList = new ArrayList<>();
        mFragmentList.clear();
        mFragmentList.add(setPagerFragment(new CurrentPoll(),
                mFragmentList.size()));
        mFragmentList.add(setPagerFragment(new HistoryPoll(),
                mFragmentList.size()));
        mFragmentList.add(setPagerFragment(new MyPoll(),
                mFragmentList.size()));


        mAdapter = new PollPagerAdapter(
                getChildFragmentManager(), mFragmentList);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search)
            startActivity(new Intent(getActivity(), SearchToolbarActivity.class).putExtra(TYPE, POLL).putExtra("PAGE_TYPE", mViewPager.getCurrentItem()));
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_showpoll_pager, container, false);
        mTabOpinion = (RadioButton) v.findViewById(R.id.tab_current);
        mTabQuick = (RadioButton) v.findViewById(R.id.tab_history);
        mTabSurvey = (RadioButton) v.findViewById(R.id.tab_mine);
//        mPollGrid = (GridView) v.findViewById(R.id.gridShowPoll);
        mViewPager = (ViewPager) v.findViewById(R.id.pagerShowPoll);
        mLayoutCreate = (LinearLayout) v.findViewById(R.id.layout_create);
        btnCreate = (Button) v.findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(this);
        mLayoutCreate.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(this);
//        mPollGrid.setOnItemClickListener(this);
        mTabOpinion.setOnCheckedChangeListener(this);
        mTabQuick.setOnCheckedChangeListener(this);
        mTabSurvey.setOnCheckedChangeListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        MenuItem menuItem = ((MainHomeActivity) act).mMenu.findItem(R.id.menu_group_three_search);
//        menuItem.setVisible(true);
        mTabOpinion.setChecked(true);
//        mPollGrid.setAdapter(mGridAdapter);
//        mPollGrid.setItemChecked(0, true);
        mViewPager.setAdapter(mAdapter);
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreate:
//                ((MainHomeActivity) act).setNewFragment(setPagerFragment(new CreatePollPager(), 1), "Create Poll Pager", true);
                startActivity(new Intent(act, PollCreateActivity.class).putExtra("create_type", 1));
                break;
            case R.id.layout_create:
//                ((MainHomeActivity) act).setNewFragment(setPagerFragment(new CreatePollPager(), 1), "Create Poll Pager", true);
                startActivity(new Intent(act, PollCreateActivity.class).putExtra("create_type", 1));
                break;
        }
    }

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.menu_group_three_search).setVisible(true);
//        super.onPrepareOptionsMenu(menu);
//    }

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
        enablePosition(position);
//        mPollGrid.setItemChecked(position, true);
//        if (position == 0)

//            ((HomeActivity) act).mPageTitle.setText("Current Poll");
//        else
//            ((HomeActivity) act).mPageTitle.setText("Poll HistoryVote");
    }

    public void enablePosition(int position) {
        if (position == 0)
            mTabOpinion.setChecked(true);
        else if (position == 1)
            mTabQuick.setChecked(true);
        else if (position == 2)
            mTabSurvey.setChecked(true);
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
        mViewPager.setCurrentItem(position);
    }

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
    }


}
