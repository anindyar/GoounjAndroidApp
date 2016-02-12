package com.orgware.polling.pollactivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.orgware.polling.BaseActivity;
import com.orgware.polling.R;
import com.orgware.polling.fragments.BaseFragment;
import com.orgware.polling.fragments.CurrentPollPager;
import com.orgware.polling.fragments.HistoryPoll;
import com.orgware.polling.fragments.SurveyDetail;
import com.orgware.polling.fragments.poll.ResultPollNew;

/**
 * Created by nandagopal on 5/2/16.
 */
public class CurrentPollDetailActivity extends BaseActivity {


    Toolbar mToolbar;
    int mPollId, mPollType;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        bundle = new Bundle();
        mPollId = getIntent().getExtras().getInt("poll_id");
        mPollType = getIntent().getExtras().getInt("poll_type");

        setContentView(R.layout.activity_current_poll_detail_home);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setContentInsetsAbsolute(0, 0);
        mToolbar.setTitleTextAppearance(this, R.style.MyTitleTextApperance);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            setFragType(mPollType);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setFragType(int mPollType) throws Exception {
        switch (mPollType) {
            case 1:
                getSupportActionBar().setTitle("Poll");
                bundle.putInt("page", 1);
                bundle.putInt("poll_id", mPollId);
                Fragment fragmentCurrentPoll = new CurrentPollPager();
                fragmentCurrentPoll.setArguments(bundle);
                setNewFragment(fragmentCurrentPoll, "Pager_Activity", false);
                break;
            case 2:
                getSupportActionBar().setTitle("Poll");
                setNewFragment(setPagerFragment(new ResultPollNew(), mPollId), "Pager_Activity", false);
                break;
            case 3:
                getSupportActionBar().setTitle("Poll");
                bundle.putInt("page", 2);
                bundle.putInt("poll_id", mPollId);
                Fragment fragmentMyPoll = new CurrentPollPager();
                fragmentMyPoll.setArguments(bundle);
                setNewFragment(fragmentMyPoll, "Pager_Activity", false);
                break;
            case 4:
                getSupportActionBar().setTitle("Survey");
                bundle.putInt("page", 1);
                bundle.putInt("poll_id", mPollId);
                Fragment fragmentCurrentSurvey = new SurveyDetail();
                fragmentCurrentSurvey.setArguments(bundle);
                setNewFragment(fragmentCurrentSurvey, "Pager_Activity", false);
                break;
            case 5:
                getSupportActionBar().setTitle("Survey");
                bundle.putInt("page", 2);
                bundle.putInt("poll_id", mPollId);
                Fragment fragmentMySurvey = new SurveyDetail();
                fragmentMySurvey.setArguments(bundle);
                setNewFragment(fragmentMySurvey, "Pager_Activity", false);
                break;
        }
    }

    private void showEdit() {

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
