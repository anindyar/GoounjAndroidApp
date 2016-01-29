package com.orgware.polling;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.orgware.polling.fragments.HomeDashboard;
import com.orgware.polling.fragments.ResultPoll;
import com.orgware.polling.utils.Methodutils;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainHomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public Menu mMenu;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mBarDrawerToggle;
    private TextView mUserName, mUserAmount;
    private CircleImageView mProfileImage;
    private ImageView mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        editor.putBoolean(WELCOME_SCREEN, false).putString(OTP_VALUE, "").commit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setTitleTextAppearance(this, R.style.MyTitleTextApperance);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideSoftInput();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                hideSoftInput();

            }
        };

        mDrawerLayout.setDrawerListener(mBarDrawerToggle);
        mBarDrawerToggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        (mLogout = (ImageView) findViewById(R.id.menu_logout)).setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        addHeader();
        setNewFragment(new HomeDashboard(), "Dashboard", false);
    }

    private void addHeader() {
        View view = LayoutInflater.from(this).inflate(R.layout.nav_header_profile, null);
        navigationView.addHeaderView(view);
        mUserName = (TextView) view.findViewById(R.id.username);
        mUserAmount = (TextView) view
                .findViewById(R.id.user_amount);
        mProfileImage = (CircleImageView) view.findViewById(R.id.profile_image);
        mUserName.setText("" + preferences.getString(USERNAME, "NA"));

//        Picasso.with(this)
//                .load("")
//                .into(mProfileImage);
//        mUserName.setText(preferences.getString(AppConstants.first_name, "") + "" + preferences.getString(AppConstants.last_name, ""));
//        mEmailId.setText(preferences.getString("email", ""));
//        File cacheDir = GraphicsUtil.getCacheFolder(this);
//        File cacheFile = new File(cacheDir, AppConstants.profile_picture_thump_url + ".jpg");

//        if (cacheFile.exists()) {
//            Picasso.with(getApplicationContext())
//                    .load(cacheFile)
//                    .placeholder(R.drawable.ic_profile)
//                    .into(mProfileImage);
//        } else {
//            GraphicsUtil.WriteProfileImage(getApplicationContext());
//            Picasso.with(getApplicationContext())
//                    .load(preferences.getString(AppConstants.profile_picture_thump_url, ""))
//                    .placeholder(mProfileImage.getDrawable())
//                    .into(mProfileImage);
//
//
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options, mMenu);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_home:
                setNewFragment(new HomeDashboard(), "Home", true);
                break;
            case R.id.menu_settings:
                setMenuintent(6);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        }

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_content).getClass() == ResultPoll.class) {
            getSupportFragmentManager().popBackStack(2, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_content).getClass() == HomeDashboard.class) {
            Methodutils.message2btn(MainHomeActivity.this, "Are you sure to exit?", "Yes", "No", new View.OnClickListener() {
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
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                setMenuintent(1);
                break;
            case R.id.nav_timeline:
                setMenuintent(2);
                break;
            case R.id.nav_changenumber:
                setMenuintent(3);
                break;
            case R.id.nav_wallet:
                setMenuintent(4);
                break;
            case R.id.nav_aboutus:
                setMenuintent(5);
                break;
            case R.id.nav_settings:
                setMenuintent(6);
                break;
        }
//        mDrawerLayout.closeDrawers();
        return false;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_logout:
                Methodutils.message2btn(MainHomeActivity.this, "Are you sure to logout?", "Confirm", "Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preferences.edit().clear().commit();
                        finish();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        return;
                    }
                });
                break;
        }
    }
}
