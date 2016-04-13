package com.bvocal.goounj.activities.results;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.bvocal.goounj.BaseActivity;
import com.bvocal.goounj.R;
import com.bvocal.goounj.fragments.poll.ResultPollNew;
import com.bvocal.goounj.fragments.vote.ResultVote;

public class ResultActivity extends BaseActivity {

    int mResultType;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_current_poll_detail_home);

        mResultType = getIntent().getExtras().getInt("result_type");

        try {
            setResultPage(mResultType);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setResultPage(int pageType) throws Exception {
        switch (pageType) {
            case 0:
                getSupportActionBar().setTitle("Poll");
                setNewFragment(setPagerFragment(new ResultPollNew(), getIntent().getExtras().getInt("poll_id")), "Pager_Activity", false);
                break;
            case 1:
                getSupportActionBar().setTitle("Vote");
                Fragment fragmentVoteResult = new ResultVote();
                fragmentVoteResult.setArguments(getIntent().getExtras());
                setNewFragment(fragmentVoteResult, "", false);
                break;
        }
    }

}
