package com.orgware.polling.pollactivities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.orgware.polling.BaseActivity;
import com.orgware.polling.R;
import com.orgware.polling.fragments.CreatePollPager;

/**
 * Created by nandagopal on 5/2/16.
 */
public class PollCreateActivity extends BaseActivity {

    Toolbar mToolbar;

    int mCreateType;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        mCreateType = getIntent().getExtras().getInt("create_type");

        setContentView(R.layout.activity_current_poll_detail_home);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setContentInsetsAbsolute(0, 0);
        mToolbar.setTitleTextAppearance(this, R.style.MyTitleTextApperance);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Poll");


        try {
            if (mCreateType == 1) {
                setNewFragment(setPagerFragment(new CreatePollPager(), 1), "Create Poll Pager", false);
                getSupportActionBar().setTitle("Poll");
            } else {
                setNewFragment(setPagerFragment(new CreatePollPager(), 2), "Create Poll Pager", false);
                getSupportActionBar().setTitle("Survey");
            }
        } catch (Exception e) {

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
