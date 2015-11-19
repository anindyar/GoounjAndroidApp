package com.orgware.polling.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.orgware.polling.R;
import com.orgware.polling.pojo.CountryItem;

import java.util.ArrayList;

/**
 * Created by nandagopal on 23/10/15.
 */
public class CountryAdapter extends ArrayAdapter<CountryItem> {

    Context mContext;
    ArrayList<CountryItem> listItems;
    int resource;
    LayoutInflater mInflater;

    public CountryAdapter(Context mContext, int resource,
                          ArrayList<CountryItem> listItems) {
        super(mContext, resource, listItems);
        this.mContext = mContext;
        this.resource = resource;
        this.listItems = listItems;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    View getCustomView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.item_spinner_drop_down, null);
        TextView country = (TextView) convertView
                .findViewById(R.id.text_dropDown);
        CountryItem item = getItem(position);

        country.setText(item.mCountryName);

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.item_country,
                    null);
        final CountryItem item = getItem(position);
        TextView title = (TextView) convertView
                .findViewById(R.id.country_text);

        title.setText(item.mCountryName);

        return convertView;
    }

}
