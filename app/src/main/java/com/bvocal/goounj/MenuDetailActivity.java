package com.bvocal.goounj;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bvocal.goounj.fragments.menu.AboutUsDetail;
import com.bvocal.goounj.fragments.menu.ChangeNumberDetail;
import com.bvocal.goounj.fragments.menu.ProfileDetail;
import com.bvocal.goounj.fragments.menu.SettingsDetail;
import com.bvocal.goounj.fragments.menu.TimeLineDetail;
import com.bvocal.goounj.fragments.menu.WalletDetail;

/**
 * Created by nandagopal on 21/1/16.
 */
public class MenuDetailActivity extends BaseActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        int type = getIntent().getExtras().getInt("Menu_Detail");
        setContentView(R.layout.activity_menu_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setContentInsetsAbsolute(0, 0);
        mToolbar.setTitleTextAppearance(this, R.style.MyTitleTextApperance);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setMenuFragment(type);

    }

    private void setMenuFragment(int type) {
        switch (type) {
            case 1:
                setNewFragment(new ProfileDetail(), R.id.fragment_content, "Profile", false);
                break;
            case 2:
                setNewFragment(new TimeLineDetail(), R.id.fragment_content, "TimeLine", false);
                break;
            case 3:
                setNewFragment(new ChangeNumberDetail(), R.id.fragment_content, "Change Number", false);
                break;
            case 4:
                setNewFragment(new WalletDetail(), R.id.fragment_content, "Wallet", false);
                break;
            case 5:
                setNewFragment(new AboutUsDetail(), R.id.fragment_content, "About us", false);
                break;
            case 6:
                setNewFragment(new SettingsDetail(), R.id.fragment_content, "Settings", false);
                break;
            case 7:

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
