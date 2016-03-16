package com.bvocal.goounj.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bvocal.goounj.MainHomeActivity;
import com.bvocal.goounj.R;
import com.bvocal.goounj.fragments.survey.SurveyPager;
import com.bvocal.goounj.utils.Methodutils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nandagopal on 21/10/15.
 */
public class HomeDashboard extends BaseFragment implements View.OnClickListener {
    public ImageView imgVote, imgPoll, imgChat, imgSurvey;
    TextView mUsername;
    CircleImageView mProfileimage;

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("User Id", "" + preferences.getString(USER_ID, ""));
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        mUsername.setText(preferences.getString(USERNAME, ""));
        mProfileimage.setImageBitmap(Methodutils.decodeProfile(preferences.getString(ENCODE_IMAGE, "")));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            mUsername.setText(preferences.getString(USERNAME, ""));
            mProfileimage.setImageBitmap(Methodutils.decodeProfile(preferences.getString(ENCODE_IMAGE, "")));
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_dashboard, container, false);
        mProfileimage = (CircleImageView) v.findViewById(R.id.menu_profile_image);
        imgVote = (ImageView) v.findViewById(R.id.home_vote);
        imgPoll = (ImageView) v.findViewById(R.id.home_poll);
        imgChat = (ImageView) v.findViewById(R.id.home_chat);
        imgSurvey = (ImageView) v.findViewById(R.id.home_survey);
        mUsername = (TextView) v.findViewById(R.id.username);
        imgVote.setOnClickListener(this);
        imgPoll.setOnClickListener(this);
        imgChat.setOnClickListener(this);
        imgSurvey.setOnClickListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainHomeActivity) act).setTitle("Home");
        mUsername.setText("" + preferences.getString(USERNAME, "NA"));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_home).setVisible(false);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_vote:
//                ((MainHomeActivity) act).setNewFragment(new VotePager(), "Poll Pager", true);
                break;
            case R.id.home_poll:
                ((MainHomeActivity) act).setNewFragment(new ShowPollPager(), "Poll Pager", true);
                editor.putInt(DASHBOARD_ID, 0).commit();
                break;
            case R.id.home_chat:
//                ((MainHomeActivity) act).setNewFragment(new ChatHome(), "Poll Pager", true);
                break;
            case R.id.home_survey:
                ((MainHomeActivity) act).setNewFragment(new SurveyPager(), "Poll Pager", true);
                editor.putInt(DASHBOARD_ID, 1).commit();
                break;
        }
    }
}
