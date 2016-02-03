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
    int choiceImage[];

    int type;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param itemList The objects to represent in the ListView.
     */
    public ChoicesListviewAdapter(Context context, int resource, List<ChoicesItem> itemList, int type) {
        super(context, resource, itemList);
        this.mContext = context;
        this.itemList = itemList;
        this.type = type;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        choiceImage = new int[]{R.drawable.bg_choice_circle_opinion, R.drawable.bg_choice_circle_quick, R.drawable.bg_choice_circle_feedback,
                R.drawable.bg_choice_circle_red, R.drawable.bg_circle_home_green, R.drawable.bg_circle_dark_grey};
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
        if (type == 1) {
            mTxtChoice.setCheckMarkDrawable(null);
            ChoicesItem item = itemList.get(position);
            mTxtChoice.setText("" + item.mChoiceName);
        } else {
            mTxtChoice.setCheckMarkDrawable(null);
            mTxtChoice.setCompoundDrawablesWithIntrinsicBounds(choiceImage[position], 0, 0, 0);
            ChoicesItem item = itemList.get(position);
            mTxtChoice.setText("" + item.mChoiceName + " - " + item.mChoicePercent);
        }

        return convertView;
    }
}
