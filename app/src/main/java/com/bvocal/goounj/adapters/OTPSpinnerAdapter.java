package com.bvocal.goounj.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bvocal.goounj.R;
import com.bvocal.goounj.interfaces.Appinterface;
import com.bvocal.goounj.pojo.CountryItem;

import java.util.List;

/**
 * Created by nandagopal on 23/10/15.
 */
public class OTPSpinnerAdapter extends ArrayAdapter<CountryItem> implements Appinterface {

    public Context mContext;
    int type;
    List<CountryItem> itemList;
    LayoutInflater inflater;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     *                 instantiating views.
     * @param itemList The objects to represent in the ListView.
     */
    public OTPSpinnerAdapter(Context context, int resource, int type, List<CountryItem> itemList) {
        super(context, resource, type, itemList);
        this.mContext = context;
        this.itemList = itemList;
        this.type = type;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txtTitle;
        ImageView imgDropdown;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.spinner_menu, parent, false);

        CountryItem items = itemList.get(position);
        if (type == COUNTRY_NAME_ID) {
            txtTitle = (TextView) convertView.findViewById(R.id.keyTxt);
            txtTitle.setText(items.mCountryName);
            imgDropdown = (ImageView) convertView.findViewById(R.id.keyImg);
            imgDropdown.setVisibility(View.VISIBLE);
        } else {
            txtTitle = (TextView) convertView.findViewById(R.id.keyTxt);
            txtTitle.setText(items.mCountryCode);
            imgDropdown = (ImageView) convertView.findViewById(R.id.keyImg);
            imgDropdown.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        TextView txtTitle;
        ImageView imgDropdown;

        CountryItem items = itemList.get(position);
        if (type == COUNTRY_NAME_ID) {
            if (position == 0 && itemList.size() > 1) {
                convertView = inflater.inflate(R.layout.spinner_dropdown_empty,
                        parent, false);
                convertView.setOnClickListener(null);
                return convertView;
            }

            convertView = inflater.inflate(R.layout.spinner_menu, parent, false);
            txtTitle = (TextView) convertView.findViewById(R.id.keyTxt);
            imgDropdown = (ImageView) convertView.findViewById(R.id.keyImg);
            imgDropdown.setVisibility(View.GONE);
            txtTitle.setText(items.mCountryName);
        } else {
            if (position == 0 && itemList.size() > 1) {
                convertView = inflater.inflate(R.layout.spinner_dropdown_empty,
                        parent, false);
                convertView.setOnClickListener(null);
                return convertView;
            }

            convertView = inflater.inflate(R.layout.spinner_menu, parent, false);
            txtTitle = (TextView) convertView.findViewById(R.id.keyTxt);
            imgDropdown = (ImageView) convertView.findViewById(R.id.keyImg);
            imgDropdown.setVisibility(View.GONE);
            txtTitle.setText(items.mCountryCode);
        }
        return convertView;
    }

}
