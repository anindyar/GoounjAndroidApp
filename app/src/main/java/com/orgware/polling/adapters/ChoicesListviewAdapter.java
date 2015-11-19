package com.orgware.polling.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.RadioButton;

import com.orgware.polling.R;
import com.orgware.polling.pojo.ChoicesItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 14/11/15.
 */
public class ChoicesListviewAdapter extends ArrayAdapter<ChoicesItem> {
    Integer selected_position = -1;

    List<ChoicesItem> itemList = new ArrayList<>();
    Context mContext;
    LayoutInflater inflater;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param itemList The objects to represent in the ListView.
     */
    public ChoicesListviewAdapter(Context context, int resource, List<ChoicesItem> itemList) {
        super(context, resource, itemList);
        this.mContext = context;
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
            convertView = inflater.inflate(R.layout.item_choices, parent, false);
        final CheckedTextView mTxtChoice = (CheckedTextView) convertView.findViewById(android.R.id.text1);
        mTxtChoice.setCheckMarkDrawable(null);
        mTxtChoice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.selector_radio_checked_bg, 0, 0, 0);
        ChoicesItem item = itemList.get(position);
        mTxtChoice.setText("" + item.mChoiceName);

        return convertView;
    }
}
