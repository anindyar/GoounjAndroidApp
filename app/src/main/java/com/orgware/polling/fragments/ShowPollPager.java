package com.orgware.polling.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.orgware.polling.HomeActivity;
import com.orgware.polling.R;
import com.orgware.polling.adapters.GridTabAdapter;
import com.orgware.polling.adapters.PollPagerAdapter;
import com.orgware.polling.pojo.GridItems;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 26/10/15.
 */
public class ShowPollPager extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener, AdapterView.OnItemClickListener {
    ViewPager mViewPager;
    PollPagerAdapter mAdapter;
    List<Fragment> mFragmentList;
    GridView mPollGrid;
    List<GridItems> mGridList;
    GridTabAdapter mGridAdapter;
    LinearLayout mLayoutCreate;
    Button btnCreate;

    @Override
    public void setTitle() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ((HomeActivity) act).mPageTitle.setText("Current Poll");
        mFragmentList = new ArrayList<>();
        mFragmentList.clear();
        mGridList = new ArrayList<GridItems>();
        mGridList.clear();
        mGridList.add(new GridItems("Current Poll", R.drawable.ic_current_poll));
        mGridList.add(new GridItems("History", R.drawable.ic_history));

        mFragmentList.add(setPagerFragment(new CurrentPoll(),
                mFragmentList.size()));
        mFragmentList.add(setPagerFragment(new HistoryPoll(),
                mFragmentList.size()));

        mGridAdapter = new GridTabAdapter(act, R.layout.item_grid_show_poll, mGridList);

        mAdapter = new PollPagerAdapter(
                getChildFragmentManager(), mFragmentList);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_showpoll_pager, container, false);
        mPollGrid = (GridView) v.findViewById(R.id.gridShowPoll);
        mViewPager = (ViewPager) v.findViewById(R.id.pagerShowPoll);
        mLayoutCreate = (LinearLayout) v.findViewById(R.id.layout_create);
        btnCreate = (Button) v.findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(this);
        mLayoutCreate.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(this);
        mPollGrid.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivity) act).openHome.setVisibility(View.VISIBLE);
        ((HomeActivity) act).mPageTitle.setText("Poll");
        ((HomeActivity) act).mPageTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_poll_logo, 0, 0, 0);
        ((HomeActivity) act).openSearch.setVisibility(View.VISIBLE);
        ((HomeActivity) act).openClose.setVisibility(View.GONE);
        ((HomeActivity) act).mSearchPollsTxt.setVisibility(View.GONE);
        mPollGrid.setAdapter(mGridAdapter);
        mPollGrid.setItemChecked(0, true);
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
                ((HomeActivity) act).setNewFragment(new CreatePollPager(), "Create Poll Pager", true);
                break;
            case R.id.layout_create:
                ((HomeActivity) act).setNewFragment(new CreatePollPager(), "Create Poll Pager", true);
                break;
        }
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
        mPollGrid.setItemChecked(position, true);
//        if (position == 0)
//            ((HomeActivity) act).mPageTitle.setText("Current Poll");
//        else
//            ((HomeActivity) act).mPageTitle.setText("Poll History");
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
     * <p/>
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
}
