package com.orgware.polling;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.orgware.polling.adapters.MenuAdapter;
import com.orgware.polling.fragments.CircleProgressTest;
import com.orgware.polling.fragments.HomeDashboard;
import com.orgware.polling.fragments.ResultPoll;
import com.orgware.polling.interfaces.Appinterface;
import com.orgware.polling.utils.Methodutils;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

public class HomeActivity extends BaseActivity implements Appinterface, View.OnClickListener, AdapterView.OnItemClickListener {

    public ImageView openMenu, openHome, openSettings, openSearch, openClose;
    public TextView mPageTitle;
    public EditText mSearchPollsTxt;
    MenuDrawer mSlidingMenu;
    RecyclerView mRecyclerView;
    MenuAdapter menuAdapter;
    int count = 0;
    /*Maintains the menu drawer state listener whether menu state is changed or slided or not*/
    MenuDrawer.OnDrawerStateChangeListener listener = new MenuDrawer.OnDrawerStateChangeListener() {

        @Override
        public void onDrawerStateChange(int oldState, int newState) {
            findViewById(R.id.fragment_content).clearFocus();
        }

        @Override
        public void onDrawerSlide(float openRatio, int offsetPixels) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editor.putBoolean(WELCOME_SCREEN, false).putString(OTP_VALUE, "").commit();

        if (isScreenXlarge) {
            mSlidingMenu = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY, Position.LEFT);
            mSlidingMenu.setDropShadowEnabled(false);
        } else {
            mSlidingMenu = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY, Position.LEFT);
            mSlidingMenu.setDropShadowEnabled(false);
        }

        mSlidingMenu.setContentView(R.layout.activity_home);
        mSlidingMenu.setMenuView(R.layout.main_menu);
        mSlidingMenu.setMenuSize(menuWidth);
        mSlidingMenu.setOnDrawerStateChangeListener(listener);

        mRecyclerView = (RecyclerView) findViewById(R.id.menu_recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuAdapter = new MenuAdapter(this, Methodutils.setMenuName());
        menuAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(menuAdapter);
        openMenu = (ImageView) findViewById(R.id.imgHomeMenu);
        openHome = (ImageView) findViewById(R.id.imgHomeHome);
        mSearchPollsTxt = (EditText) findViewById(R.id.edittext_search_poll);
        (openSearch = (ImageView) findViewById(R.id.img_searchPoll)).setOnClickListener(this);
        (openClose = (ImageView) findViewById(R.id.img_closePoll)).setOnClickListener(this);
        openHome.setVisibility(View.GONE);
        openSearch.setVisibility(View.GONE);
        openClose.setVisibility(View.GONE);
        mPageTitle = (TextView) findViewById(R.id.pageTitle);
        openSettings = (ImageView) findViewById(R.id.imgHomeSettings);
        openMenu.setOnClickListener(this);
        openHome.setOnClickListener(this);
        openSettings.setOnClickListener(this);
        setNewFragment(new HomeDashboard(), "Home", false);
    }

    /*This method is used to call the new Fragment and sets into the Frame Layout */
    public void setNewFragment(Fragment fragment, String title,
                               boolean addbackstack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.setCustomAnimations(R.anim.slideout2, R.anim.slidein2,
                R.anim.slidein, R.anim.slideout);

        transaction.replace(R.id.fragment_content, fragment);
        if (addbackstack)
            transaction.addToBackStack(title);
        transaction.commit();

    }

    /*This method is used to call the new Fragment and sets into the Frame Layout */
    public void setNewFragmentWithArgument(Fragment fragment, String title, String argument,
                                           boolean addbackstack) {
        Bundle bundleArgument = new Bundle();
        bundleArgument.putString(PAGE_ARGUMENT, argument);
        fragment.setArguments(bundleArgument);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.setCustomAnimations(R.anim.slideout2, R.anim.slidein2,
                R.anim.slidein, R.anim.slideout);

        transaction.replace(R.id.fragment_content, fragment);
        if (addbackstack)
            transaction.addToBackStack(title);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        if (mSlidingMenu.isMenuVisible()) {
            mSlidingMenu.closeMenu();
            return;
        }
        count = getSupportFragmentManager().getBackStackEntryCount();
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_content).getClass() == ResultPoll.class) {
            getSupportFragmentManager().popBackStack(2, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_content).getClass() == HomeDashboard.class) {
            Methodutils.message2btn(HomeActivity.this, "Are you sure to exit?", "Yes", "No", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    return;
                }
            });
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgHomeMenu:
                if (!mSlidingMenu.isMenuVisible())
                    mSlidingMenu.openMenu();
                break;
            case R.id.imgHomeHome:
                setNewFragment(new HomeDashboard(), "Home", true);
                break;
            case R.id.imgHomeSettings:
//                setNewFragment(new CircleProgressTest(), "PROG", true);
                break;
            case R.id.img_searchPoll:
                openClose.setVisibility(View.VISIBLE);
                openSearch.setVisibility(View.GONE);
                mSearchPollsTxt.setVisibility(View.VISIBLE);
                mSearchPollsTxt.setText("");
                mPageTitle.setVisibility(View.GONE);
                break;
            case R.id.img_closePoll:
                openClose.setVisibility(View.GONE);
                openSearch.setVisibility(View.VISIBLE);
                mSearchPollsTxt.setVisibility(View.GONE);
                mPageTitle.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mSlidingMenu.isMenuVisible())
            mSlidingMenu.closeMenu();
    }
}
