package com.orgware.polling.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.orgware.polling.R;
import com.orgware.polling.pojo.CurrentPollItem;
import com.orgware.polling.pojo.Search_SurveyPoll;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 2/2/16.
 */
public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder> {

    List<Search_SurveyPoll> itemList;
    Context mContext;
    AdapterView.OnItemClickListener mOnItemClickListener;
    int type;

    public SearchListAdapter(Context mContext, List<Search_SurveyPoll> itemList, int type) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.type = type;
    }

    @Override
    public SearchListViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_current_poll, parent, false);
        return new SearchListViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(SearchListViewHolder holder, int position) {
        Search_SurveyPoll items = itemList.get(position);
        if (type == 1) {
            holder.imgRightArrow.setVisibility(View.VISIBLE);
            holder.imgStatistics.setVisibility(View.GONE);
        } else {
            holder.imgRightArrow.setVisibility(View.GONE);
            holder.imgStatistics.setVisibility(View.VISIBLE);
        }
        holder.txtPollTitle.setText("" + items.getPollName());
        holder.txtPollCreatedBy.setText("Created by: " + items.getCreatedUserName());
        holder.txtPollStartDate.setText("" + items.getStartDate());
        holder.txtPollEndDate.setText("" + items.getEndDate());
//        holder.imgPollImage.setImageResource(items.mCurrentPollImage);

    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(SearchListViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }


    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class SearchListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtPollTitle, txtPollCreatedBy, txtPollStartDate, txtPollEndDate;
        ImageView imgPollImage, imgRightArrow, imgStatistics;

        SearchListAdapter mAdapter;

        public SearchListViewHolder(View itemView, SearchListAdapter mAdapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.mAdapter = mAdapter;
            txtPollTitle = (TextView) itemView.findViewById(R.id.poll_title);
            txtPollCreatedBy = (TextView) itemView.findViewById(R.id.poll_createdBy);
            txtPollStartDate = (TextView) itemView.findViewById(R.id.poll_startDate);
            txtPollEndDate = (TextView) itemView.findViewById(R.id.poll_endDate);
            imgPollImage = (ImageView) itemView.findViewById(R.id.poll_image);
            imgRightArrow = (ImageView) itemView.findViewById(R.id.logo_right_arrow);
            imgStatistics = (ImageView) itemView.findViewById(R.id.logo_statistics);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }
}
