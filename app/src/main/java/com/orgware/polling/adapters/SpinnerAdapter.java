package com.orgware.polling.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.orgware.polling.R;

import java.util.List;

/**
 * Created by Nandagopal on 02-Nov-15.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {

    List<String> itemList;
    LayoutInflater inflater;

    public SpinnerAdapter(Context context, int resource, List<String> itemList) {
        super(context, resource, itemList);
        this.itemList = itemList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_spinner, null);
        TextView txtSpinner = (TextView) convertView.findViewById(R.id.txtSpinnerNormal);
        txtSpinner.setText("" + itemList.get(position));

        return convertView;
    }
}
