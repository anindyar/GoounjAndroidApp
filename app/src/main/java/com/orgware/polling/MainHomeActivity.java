package com.orgware.polling;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.orgware.polling.fragments.PollOpinion;

import java.io.File;

/**
 * Created by nandagopal on 14/1/16.
 */
public class MainHomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mBarDrawerToggle;
    private TextView mUserName, mEmailId;
    private ImageView mProfileImage;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_adwithus));
        navigationView.getMenu().getItem(0).setChecked(true);
        addHeader();
    }

    private void addHeader() {


        View view = LayoutInflater.from(this).inflate(R.layout.nav_header_navigation_drawer, null);
        navigationView.addHeaderView(view);
        mUserName = (TextView) view.findViewById(R.id.name);
        mEmailId = (TextView) view
                .findViewById(R.id.emailid);
        mProfileImage = (ImageView) view.findViewById(R.id.profile_image);
//        mUserName.setText(preferences.getString(AppConstants.first_name, "") + "" + preferences.getString(AppConstants.last_name, ""));
//        mEmailId.setText(preferences.getString("email", ""));
//        File cacheDir = GraphicsUtil.getCacheFolder(this);
//        File cacheFile = new File(cacheDir, AppConstants.profile_picture_thump_url + ".jpg");
//
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_profile:
                setNewFragment(new PollOpinion(), "DashBoardFragment", false);
                break;
            case R.id.nav_aboutus:
                setNewFragment(new PollOpinion(), "Profile Wallet Fragment", false);
                break;
            case R.id.nav_timeline:
                setNewFragment(new PollOpinion(), "Profile Wallet Fragment", false);
                break;
            case R.id.nav_changenumber:
                break;
            case R.id.nav_adwithus:
                setNewFragment(new PollOpinion(), "Profile Wallet Fragment", false);
                break;
            case R.id.nav_settings:
                setNewFragment(new PollOpinion(), "AboutFragment", false);
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
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


}
