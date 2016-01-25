package com.orgware.polling.fragments.chat;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orgware.polling.R;
import com.orgware.polling.adapters.LoadMoreAdapter;
import com.orgware.polling.fragments.BaseFragment;
import com.orgware.polling.interfaces.OnLoadMoreListener;
import com.orgware.polling.pojo.CurrentPollItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 25/1/16.
 */
public class ChatHome extends BaseFragment {

    protected Handler handler = new Handler();
    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private List<CurrentPollItem> studentList = new ArrayList<>();
    private LoadMoreAdapter mAdapter;

    @Override
    public void setTitle() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvEmptyView = (TextView) view.findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(act));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
        mAdapter = new LoadMoreAdapter(studentList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        if (studentList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                studentList.add(null);
                mAdapter.notifyItemInserted(studentList.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        studentList.remove(studentList.size() - 1);
                        mAdapter.notifyItemRemoved(studentList.size());
                        //add items one by one
                        int start = studentList.size();
                        int end = start + 20;

                        for (int i = start + 1; i <= end; i++) {
                            studentList.add(new CurrentPollItem("Student " + i, "AndroidStudent" + i + "@gmail.com"));
                            mAdapter.notifyItemInserted(studentList.size());
                        }
                        mAdapter.setLoaded();
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);

            }
        });
    }

    // load initial data
    private void loadData() {
        for (int i = 1; i <= 20; i++) {
            studentList.add(new CurrentPollItem("Student " + i, "androidstudent" + i + "@gmail.com"));
        }
    }

}
