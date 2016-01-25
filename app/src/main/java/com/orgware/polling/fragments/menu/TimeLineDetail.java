package com.orgware.polling.fragments.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orgware.polling.MenuDetailActivity;
import com.orgware.polling.R;
import com.orgware.polling.adapters.TimeLineAdapter;
import com.orgware.polling.fragments.BaseFragment;
import com.orgware.polling.pojo.TimeLineItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 21/1/16.
 */
public class TimeLineDetail extends BaseFragment {

    RecyclerView mTimeLineRecycler;
    TimeLineAdapter mAdapter;
    List<TimeLineItem> timeLineItems = new ArrayList<>();

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String me;
        for (int i = 0; i < 10; i++) {
            if (i == 3 || i == 5 || i == 9)
                me = "me";
            else
                me = "nanda";
            timeLineItems.add(new TimeLineItem("Jan 22", "Title", "Creator", me));
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_timeline, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTimeLineRecycler = (RecyclerView) view.findViewById(R.id.timelineRecyclerview);
        mTimeLineRecycler.setLayoutManager(new LinearLayoutManager(act));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MenuDetailActivity) act).setTitle("TimeLine");
        mAdapter = new TimeLineAdapter(act, timeLineItems);
        mTimeLineRecycler.setAdapter(mAdapter);
    }
}
