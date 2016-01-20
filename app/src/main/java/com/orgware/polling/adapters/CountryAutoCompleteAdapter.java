package com.orgware.polling.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.orgware.polling.R;
import com.orgware.polling.pojo.CountryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 15/11/15.
 */
public class CountryAutoCompleteAdapter extends ArrayAdapter<CountryItem> {

    Context mContext;
    List<CountryItem> itemList, tempItems, suggestions;
    LayoutInflater inflater;
    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {


        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((CountryItem) resultValue).mCountryName;
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (CountryItem people : tempItems) {
                    if (people.mCountryName.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<CountryItem> filterList = (ArrayList<CountryItem>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (CountryItem people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }

    };

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param itemList The objects to represent in the ListView.
     */
    public CountryAutoCompleteAdapter(Context context, int resource, List<CountryItem> itemList) {
        super(context, resource, itemList);
        this.mContext = context;
        this.itemList = itemList;
        tempItems = new ArrayList<CountryItem>(itemList);
        suggestions = new ArrayList<CountryItem>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null)
//            convertView = inflater.inflate(R.layout.spinner_menu, parent, false);
//        CountryItem item = itemList.get(position);
//
//        TextView txtTitle = (TextView) convertView.findViewById(R.id.keyTxt);
//        txtTitle.setText(item.mCountryName);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_menu, parent, false);
        }
        CountryItem people = itemList.get(position);
        if (people != null) {
            TextView lblName = (TextView) convertView.findViewById(R.id.keyTxt);
            if (lblName != null)
                lblName.setText(people.mCountryName);
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }
}