package com.bvocal.goounj.activities.results;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bvocal.goounj.BaseActivity;
import com.bvocal.goounj.R;
import com.bvocal.goounj.fragments.poll.ResultPollNew;
import com.bvocal.goounj.fragments.vote.ResultVote;

public class ResultActivity extends BaseActivity {

    int mPollID;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_current_poll_detail_home);

        mPollID = getIntent().getExtras().getInt("poll_id");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setContentInsetsAbsolute(0, 0);
        mToolbar.setTitleTextAppearance(this, R.style.MyTitleTextApperance);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setTitle("Poll");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            Bundle bundle = new Bundle();
            bundle.putInt(PAGER_COUNT, mPollID);
            Fragment fragment = new ResultPollNew();
            fragment.setArguments(bundle);

            setNewFragment(fragment, "Pager_Activity", false);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
