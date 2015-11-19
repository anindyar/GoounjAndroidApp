package com.orgware.polling.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.orgware.polling.HomeActivity;
import com.orgware.polling.R;

/**
 * Created by nandagopal on 21/10/15.
 */
public class HomeDashboard extends BaseFragment implements View.OnClickListener {
    ImageView imgVote, imgPoll, imgChat, imgSurvey;

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeToast("Git Test");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_dashboard, container, false);
        imgVote = (ImageView) v.findViewById(R.id.home_vote);
        imgPoll = (ImageView) v.findViewById(R.id.home_poll);
        imgChat = (ImageView) v.findViewById(R.id.home_chat);
        imgSurvey = (ImageView) v.findViewById(R.id.home_survey);
        imgVote.setOnClickListener(this);
        imgPoll.setOnClickListener(this);
        imgChat.setOnClickListener(this);
        imgSurvey.setOnClickListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivity) act).openHome.setVisibility(View.GONE);
        ((HomeActivity) act).mPageTitle.setText("Goounj");
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
//                ((HomeActivity) act).setNewFragment(new ContactGrid(), "Poll Pager", true);
                break;
            case R.id.home_poll:
                ((HomeActivity) act).setNewFragment(new ShowPollPager(), "Poll Pager", true);
                editor.putInt(DASHBOARD_ID, 0).commit();
                break;
            case R.id.home_chat:
                break;
            case R.id.home_survey:
                ((HomeActivity) act).setNewFragment(new CurrentPoll(), "Poll Pager", true);
                editor.putInt(DASHBOARD_ID, 1).commit();
                break;
        }
    }
}
