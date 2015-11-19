package com.orgware.polling.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.orgware.polling.R;
import com.orgware.polling.pojo.GridItems;

import java.util.List;

/**
 * Created by nandagopal on 26/10/15.
 */
public class GridTabAdapter extends ArrayAdapter<GridItems> {

    List<GridItems> items;
    Context mContext;
    LayoutInflater mInflater;

    public GridTabAdapter(Context context, int resource,
                          List<GridItems> items) {
        super(context, resource, items);

        this.mContext = context;
        this.items = items;
        this.mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.item_grid_show_poll,
                    parent, false);
        GridItems item = items.get(position);
        TextView name = (TextView) convertView
                .findViewById(R.id.grid_type);
        name.setText(item.mGridName);
        name.setCompoundDrawablesWithIntrinsicBounds(item.mGridImage, 0, 0, 0);
        return convertView;
    }
}