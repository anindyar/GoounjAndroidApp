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
import android.widget.Toast;

import com.bvocal.goounj.R;
import com.bvocal.goounj.pojo.CandidateItem;

import java.util.List;

/**
 * Created by nandagopal on 12/1/16.
 */
public class CandidateListAdapter extends RecyclerView.Adapter<CandidateListAdapter.CurrentPollViewHolder> implements View.OnClickListener {

    List<CandidateItem> itemList;
    Context mContext;
    AdapterView.OnItemClickListener mOnItemClickListener;
    View.OnClickListener onClickListenerDetail;
    View.OnClickListener onClickListenerVote;

    public CandidateListAdapter(Context mContext, List<CandidateItem> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    /**
     * Called when RecyclerView needs a new {@link CurrentPollViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(CurrentPollViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param position The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(CurrentPollViewHolder, int)
     */
    @Override
    public CurrentPollViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_candidate_list, parent, false);
        return new CurrentPollViewHolder(view, this);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link CurrentPollViewHolder#itemView} to reflect the item at the given
     * position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link CurrentPollViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p/>
     * Override {@link #onBindViewHolder(CurrentPollViewHolder, int)} instead if Adapter can
     * handle effcient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(CurrentPollViewHolder holder, int position) {
        CandidateItem items = itemList.get(position);
        holder.candidaterName.setText(items.name);
        holder.candidateAbout.setText(items.about);
//        holder.imgCandidateImage.setImageResource(items.candidateImage);
        holder.mBtnCandidateVote.setTag(position);
        holder.mBtnCandidateView.setTag(position);

    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(CurrentPollViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    public void setOnDetailClickListener(View.OnClickListener onClickListener) {
        this.onClickListenerDetail = onClickListener;
    }

    private void onDetailClick(CurrentPollViewHolder currentPollViewHolder) {
        if (onClickListenerDetail != null) {
            onClickListenerDetail.onClick(currentPollViewHolder.mBtnCandidateView);
        }
    }

    public void setOnVoteClickListener(View.OnClickListener onClickListener) {
        this.onClickListenerVote = onClickListener;
    }

    private void onVoteClick(CurrentPollViewHolder currentPollViewHolder) {
        if (onClickListenerVote != null) {
            onClickListenerVote.onClick(currentPollViewHolder.mBtnCandidateVote);
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.candidate_vote:
                Toast.makeText(mContext, "Vote", Toast.LENGTH_SHORT).show();
                break;
            case R.id.candidate_detail_view:
                Toast.makeText(mContext, "Detail", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public class CurrentPollViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView candidaterName, candidateAbout;
        ImageView imgCandidateImage, mBtnCandidateVote, mBtnCandidateView;
        CandidateListAdapter mAdapter;

        public CurrentPollViewHolder(View itemView, final CandidateListAdapter mAdapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.mAdapter = mAdapter;
            candidaterName = (TextView) itemView.findViewById(R.id.candidate_name);
            candidateAbout = (TextView) itemView.findViewById(R.id.candidate_content);
            imgCandidateImage = (ImageView) itemView.findViewById(R.id.candidate_icon);
            mBtnCandidateView = (ImageView) itemView.findViewById(R.id.candidate_detail_view);
            mBtnCandidateVote = (ImageView) itemView.findViewById(R.id.candidate_vote);
            mBtnCandidateView.setOnClickListener(this);
            mBtnCandidateVote.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.candidate_vote:
                    mAdapter.onVoteClick(this);
                    break;
                case R.id.candidate_detail_view:
                    mAdapter.onDetailClick(this);
                    break;
            }

//            mAdapter.onItemHolderClick(this);
        }
    }
}


