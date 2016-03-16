package com.bvocal.goounj.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bvocal.goounj.R;
import com.bvocal.goounj.pojo.SocialTrendsItem;

import java.util.List;

/**
 * Created by nandagopal on 22/10/15.
 */
public class SocialTrendsAdapter extends RecyclerView.Adapter<SocialTrendsAdapter.SocialViewHolder> {
    List<SocialTrendsItem> menuListItemList;
    Context mContext;

    public SocialTrendsAdapter(Context context, List<SocialTrendsItem> menuListItemList) {
        super();
        mContext = context;
        this.menuListItemList = menuListItemList;
    }

    @Override
    public SocialViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_socialtrends, viewGroup, false);
        return new SocialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SocialViewHolder viewHolder, int position) {
        SocialTrendsItem items = menuListItemList.get(position);
        viewHolder.socialTitle.setText("" + items.socialContent);
        viewHolder.socialImage.setImageResource(items.socialImage);
    }

    @Override
    public int getItemCount() {
        return menuListItemList.size();
    }

    public class SocialViewHolder extends RecyclerView.ViewHolder {
        public TextView socialTitle;
        public ImageView socialImage;

        public SocialViewHolder(View itemView) {
            super(itemView);
            socialTitle = (TextView) itemView.findViewById(R.id.txtSocialTrends);
            socialImage = (ImageView) itemView.findViewById(R.id.imgSocialTrends);
        }
    }
}
