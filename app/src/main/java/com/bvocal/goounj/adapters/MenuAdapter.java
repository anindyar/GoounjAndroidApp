package com.bvocal.goounj.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bvocal.goounj.R;
import com.bvocal.goounj.pojo.MenuListItem;

import java.util.List;

/**
 * Created by nandagopal on 20/10/15.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    List<MenuListItem> menuListItemList;
    Context mContext;
    AdapterView.OnItemClickListener mOnItemClickListener;

    public MenuAdapter(Context context, List<MenuListItem> menuListItemList) {
        super();
        mContext = context;
        this.menuListItemList = menuListItemList;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_menulist, viewGroup, false);
        return new MenuViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(MenuViewHolder viewHolder, int position) {
        MenuListItem items = menuListItemList.get(position);
        viewHolder.menuTitle.setText("" + items.menuTitle);
        viewHolder.menuTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, items.menuImage, 0);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(MenuViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    @Override
    public int getItemCount() {
        return menuListItemList.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView menuTitle;
        MenuAdapter mAdapter;

        public MenuViewHolder(View itemView, MenuAdapter mAdapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.mAdapter = mAdapter;
            menuTitle = (TextView) itemView.findViewById(R.id.menuListText);
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
