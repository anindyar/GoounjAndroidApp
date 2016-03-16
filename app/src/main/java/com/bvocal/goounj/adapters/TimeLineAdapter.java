package com.bvocal.goounj.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bvocal.goounj.R;
import com.bvocal.goounj.interfaces.Appinterface;
import com.bvocal.goounj.pojo.TimeLine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nandagopal on 22/1/16.
 */
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> implements Appinterface {

    Context context;
    List<TimeLine> timLineList = new ArrayList<>();
    AdapterView.OnItemClickListener mOnItemClickListener;

    public TimeLineAdapter(Context context, List<TimeLine> timLineList) {
        this.context = context;
        this.timLineList = timLineList;
    }

    /**
     * Called when RecyclerView needs a new {@link TimeLineViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(TimeLineViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(TimeLineViewHolder, int)
     */
    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_timeline, parent, false);
        return new TimeLineViewHolder(v, this);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link TimeLineViewHolder#itemView} to reflect the item at the given
     * position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link TimeLineViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p/>
     * Override {@link #onBindViewHolder(TimeLineViewHolder, int)} instead if Adapter can
     * handle effcient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {
        TimeLine item = timLineList.get(position);

        try {
            Date date = mUTCFormat.parse(item.getDate().split("T")[0]);
            SimpleDateFormat myformat = new SimpleDateFormat("dd-MM-yyyy");
            holder.mTimeLineDate.setText(myformat.format(date));
            holder.mTimeLinePollTitle.setText("" + item.getPollName());
            holder.mTimeLinePollCreator.setText("" + item.getCreatedUser());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return timLineList.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(TimeLineViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TimeLineAdapter mAdapter;
        TextView mTimeLineDate, mTimeLinePollTitle, mTimeLinePollCreator;

        public TimeLineViewHolder(View itemView, TimeLineAdapter mAdapter) {
            super(itemView);
            this.mAdapter = mAdapter;
            mTimeLineDate = (TextView) itemView.findViewById(R.id.timeline_date);
            mTimeLinePollTitle = (TextView) itemView.findViewById(R.id.timeline_polltitle);
            mTimeLinePollCreator = (TextView) itemView.findViewById(R.id.timeline_pollcreator);
            itemView.setOnClickListener(this);
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
