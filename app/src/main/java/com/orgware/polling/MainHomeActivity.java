package com.orgware.polling;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.orgware.polling.fragments.HomeDashboard;
import com.squareup.picasso.Picasso;

import java.io.File;

public class MainHomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mBarDrawerToggle;
    private TextView mUserName, mEmailId;
    private ImageView mProfileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
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
        navigationView.setItemIconTintList(null);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_profile));
        navigationView.getMenu().getItem(0).setChecked(true);
        addHeader();
        setNewFragment(new HomeDashboard(), "Dashboard", false);
    }

    private void addHeader() {
        View view = LayoutInflater.from(this).inflate(R.layout.nav_header_profile, null);
        navigationView.addHeaderView(view);
        mUserName = (TextView) view.findViewById(R.id.name);
        mEmailId = (TextView) view
                .findViewById(R.id.emailid);
        mProfileImage = (ImageView) view.findViewById(R.id.profile_image);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.nav_profile) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;

    }
}
