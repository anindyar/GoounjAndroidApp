package com.orgware.polling;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;

import com.orgware.polling.fragments.CurrentPollPager;
import com.orgware.polling.fragments.SurveyDetail;
import com.orgware.polling.fragments.menu.AboutUsDetail;
import com.orgware.polling.fragments.menu.ChangeNumberDetail;
import com.orgware.polling.fragments.menu.ProfileDetail;
import com.orgware.polling.fragments.menu.SettingsDetail;
import com.orgware.polling.fragments.menu.TimeLineDetail;
import com.orgware.polling.fragments.menu.WalletDetail;
import com.orgware.polling.fragments.poll.ResultPollNew;

/**
 * Created by nandagopal on 4/2/16.
 */
public class SearchDetailActivity extends BaseActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        int type = getIntent().getExtras().getInt("Search_Detail");
        setContentView(R.layout.activity_menu_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setContentInsetsAbsolute(0, 0);
        mToolbar.setTitleTextAppearance(this, R.style.MyTitleTextApperance);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            setMenuFragment(type);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Fragment setPagerFragment(Fragment fragment, String key, int pos, String json_key, String json_value) {
        Bundle args = new Bundle();
        args.putInt(key, pos);
        args.putString(json_key, json_value);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment setPagerFragment(Fragment fragment, int pos) {
        Bundle args = new Bundle();
        args.putInt(PAGER_COUNT, pos);
        fragment.setArguments(args);
        return fragment;
    }

    private void setMenuFragment(int type) {
        switch (type) {
            case 1:
                setNewFragment(setPagerFragment(new CurrentPollPager(), "page", 1, "activity", "not_main"), R.id.fragment_content, "Profile", false);
                break;
            case 2:
                setNewFragment(new ResultPollNew(), R.id.fragment_content, "TimeLine", false);
                break;
            case 3:
                setNewFragment(setPagerFragment(new CurrentPollPager(), "page", 1, "activity", "main"), R.id.fragment_content, "Profile", false);
                break;
            case 4:
                setNewFragment(new SurveyDetail(), R.id.fragment_content, "Wallet", false);
                break;
        }
    }

}
