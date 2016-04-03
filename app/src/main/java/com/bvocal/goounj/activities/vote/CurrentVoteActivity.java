package com.bvocal.goounj.activities.vote;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bvocal.goounj.BaseActivity;
import com.bvocal.goounj.MainHomeActivity;
import com.bvocal.goounj.R;
import com.bvocal.goounj.fragments.vote.CurrentVoteDetail;
import com.bvocal.goounj.fragments.vote.SelfNominationForm;

/**
 * Created by Nanda on 02/04/16.
 */
public class CurrentVoteActivity extends BaseActivity {

    Toolbar mToolbar;
    int mElectionId;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        bundle = new Bundle();
        setContentView(R.layout.activity_current_poll_detail_home);
        mElectionId = getIntent().getExtras().getInt("electionId");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setContentInsetsAbsolute(0, 0);
        mToolbar.setTitleTextAppearance(this, R.style.MyTitleTextApperance);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            setVoteType(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setVoteType(int mVoteType) throws Exception {
        switch (mVoteType) {
            case 0:
                getSupportActionBar().setTitle("Vote");
                setNewFragment(setPagerFragment(new SelfNominationForm(), mElectionId), "Vote Detail", false);
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
