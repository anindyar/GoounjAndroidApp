package com.bvocal.goounj.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bvocal.goounj.R;
import com.bvocal.goounj.pojo.CurrentPollItem;

import java.util.List;

/**
 * Created by nandagopal on 26/10/15.
 */
public class HistoryPollAdapter extends RecyclerView.Adapter<HistoryPollAdapter.HistoryPollViewHolder> {


    List<CurrentPollItem> itemList;
    Context mContext;
    AdapterView.OnItemClickListener mOnItemClickListener;

    public HistoryPollAdapter(Context mContext, List<CurrentPollItem> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    /**
     * Called when RecyclerView needs a new {@link HistoryPollViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(HistoryPollViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param position The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(HistoryPollViewHolder, int)
     */
    @Override
    public HistoryPollViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_current_poll, parent, false);
        return new HistoryPollViewHolder(view, this);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link HistoryPollViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link HistoryPollViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(HistoryPollViewHolder, int)} instead if Adapter can
     * handle effcient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(HistoryPollViewHolder holder, int position) {
        CurrentPollItem items = itemList.get(position);
        holder.txtPollTitle.setText("" + items.mCurrentPollTitle);
        holder.txtPollCreatedBy.setText("Created by: " + items.mCurrentPollCreatedBy);
        holder.txtPollStartDate.setText("" + items.mCurrentPollStart);
        holder.txtPollEndDate.setText("" + items.mCurrentPollEnd);
//        holder.imgPollImage.setImageResource(items.mCurrentPollImage);

    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(HistoryPollViewHolder itemHolder) {
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

    public class HistoryPollViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtPollTitle, txtPollCreatedBy, txtPollStartDate, txtPollEndDate;
        ImageView imgPollImage;

        HistoryPollAdapter mAdapter;

        public HistoryPollViewHolder(View itemView, HistoryPollAdapter mAdapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.mAdapter = mAdapter;
            txtPollTitle = (TextView) itemView.findViewById(R.id.poll_title);
            txtPollCreatedBy = (TextView) itemView.findViewById(R.id.poll_createdBy);
            txtPollStartDate = (TextView) itemView.findViewById(R.id.poll_startDate);
            txtPollEndDate = (TextView) itemView.findViewById(R.id.poll_endDate);
            imgPollImage = (ImageView) itemView.findViewById(R.id.poll_image);
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
