package com.orgware.polling.pollactivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
            setPollPage(mCreateType);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            if (mCreateType == 1) {
//                bundle.putInt("page_type", 1);
//                bundle.putInt("mypoll", 2);
//                Fragment fragment = new CreatePollPager();
//                fragment.setArguments(bundle);
//                setNewFragment(fragment, "Create Poll Pager", false);
//                getSupportActionBar().setTitle("Poll");
//            } else {
//                bundle.putInt("page_type", 2);
//                bundle.putInt("", 2);
//                Fragment fragment = new CreatePollPager();
//                fragment.setArguments(bundle);
//                setNewFragment(setPagerFragment(new CreatePollPager(), 2), "Create Poll Pager", false);
//                getSupportActionBar().setTitle("Survey");
//            }
//        } catch (Exception e) {
//
//        }

    }

    private void setPollPage(int mCreateType) throws Exception {
        switch (mCreateType) {
            case 1:
                Bundle bundle = new Bundle();
                bundle.putInt("page_type", 1);
                bundle.putInt("mypoll", 0);
                Fragment fragmentPoll = new CreatePollPager();
                fragmentPoll.setArguments(bundle);
                setNewFragment(fragmentPoll, "Create Poll Pager", false);
                getSupportActionBar().setTitle("Poll");
                break;
            case 2:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("page_type", 2);
                bundle1.putInt("myPoll", 0);
                Fragment fragmentSurvey = new CreatePollPager();
                fragmentSurvey.setArguments(bundle1);
                setNewFragment(fragmentSurvey, "Create Poll Pager", false);
                getSupportActionBar().setTitle("Survey");
                break;
            case 3:
                Bundle bundle3 = new Bundle();
                bundle3.putInt("page_type", 1);
                bundle3.putInt("myPoll", 1);
                Fragment fragmentmyPoll = new CreatePollPager();
                fragmentmyPoll.setArguments(bundle3);
                setNewFragment(fragmentmyPoll, "Create Poll Pager", false);
                getSupportActionBar().setTitle("My Poll");
                break;
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
