package com.orgware.polling.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.orgware.polling.R;
import com.orgware.polling.adapters.ChoicesListviewAdapter;
import com.orgware.polling.pojo.ChoicesItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 13/11/15.
 */
public class CurrentPollDetailThree extends BaseFragment implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener {
    int mType;
    RadioButton mChoiceRadioOne, mChoiceRadioTwo, mChoiceRadioThree, mChoiceRadioFour, mChoiceRadioFive;
    String mQuestionThree, mChoice_1, mChoice_2, mChoice_3, mChoice_4, mChoice_5, mPollNameValue, mCreatedByValue;
    int mChoiceSize, mQtsId;
    TextView mPollName, mPollCreatedBy, mQuestionTitleThree;
    ListView mChoiceListview;
    ChoicesListviewAdapter mAdapter;
    List<ChoicesItem> itemList = new ArrayList<>();
    JSONArray mChoiceArray;

    @Override
    public void setTitle() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mChoiceArray = new JSONArray("" + preferences.getString(CHOICE_2, ""));
            mQtsId = preferences.getInt(QUESTION_ID_2, 0);
            for (int i = 0; i < mChoiceArray.length(); i++) {
                JSONObject mChoiceObject = mChoiceArray.optJSONObject(i);
                itemList.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("optionId")));
                Log.e("Qts One", "" + itemList.get(i).mChoiceName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        for (int i = 0; i < 5; i++) {
//            itemList.add(new ChoicesItem("Choice - " + i));
//        }
        mAdapter = new ChoicesListviewAdapter(act, android.R.layout.simple_list_item_single_choice, itemList);
        mQuestionThree = preferences.getString(QUESTION_SIZE_2, "");
        mChoiceSize = preferences.getInt(CHOICE_SIZE_2, 0);
        mPollNameValue = preferences.getString(POLL_NAME, "");
        mCreatedByValue = preferences.getString(CURRENT_CREATED_USER_NAME, "");
        mChoice_1 = preferences.getString("CHOICE_2_0", "");
        mChoice_2 = preferences.getString("CHOICE_2_1", "");
        mChoice_3 = preferences.getString("CHOICE_2_2", "");
        mChoice_4 = preferences.getString("CHOICE_2_3", "");
        mChoice_5 = preferences.getString("CHOICE_2_4", "");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_poll_detail_three, container, false);
        mPollName = (TextView) v.findViewById(R.id.qts_detail_three_poll_title);
        mPollCreatedBy = (TextView) v.findViewById(R.id.qts_detail_three_user_name);
        mQuestionTitleThree = (TextView) v.findViewById(R.id.qts_detail_three);
        (mChoiceListview = (ListView) v.findViewById(R.id.choicesListviewThree)).setOnItemClickListener(this);
        mChoiceListview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        (mChoiceRadioOne = (RadioButton) v.findViewById(R.id.qts_detail_three_first_radio)).setOnCheckedChangeListener(this);
//        (mChoiceRadioTwo = (RadioButton) v.findViewById(R.id.qts_detail_three_second_radio)).setOnCheckedChangeListener(this);
//        (mChoiceRadioThree = (RadioButton) v.findViewById(R.id.qts_detail_three_third_radio)).setOnCheckedChangeListener(this);
//        (mChoiceRadioFour = (RadioButton) v.findViewById(R.id.qts_detail_three_fourth_radio)).setOnCheckedChangeListener(this);
//        (mChoiceRadioFive = (RadioButton) v.findViewById(R.id.qts_detail_three_fifth_radio)).setOnCheckedChangeListener(this);
        mPollName.setText(mPollNameValue);
        mPollCreatedBy.setText("Created By:" + mCreatedByValue);
        mQuestionTitleThree.setText(mQuestionThree);
        editor.putString(QUESTION_THREE, "" + mQuestionThree).commit();
//        enableChoices(mChoiceSize);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mChoiceRadioOne.setChecked(true);
        mChoiceListview.setAdapter(mAdapter);
        mChoiceListview.setItemChecked(0, true);
        editor.putInt(OPTION_THREE, itemList.get(0).mChoiceOptionId).commit();
    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mChoiceRadioOne.isChecked())
            editor.putString(OPTION_THREE, mChoiceRadioOne.getText().toString()).commit();
        if (mChoiceRadioTwo.isChecked())
            editor.putString(OPTION_THREE, mChoiceRadioTwo.getText().toString()).commit();
        if (mChoiceRadioThree.isChecked())
            editor.putString(OPTION_THREE, mChoiceRadioThree.getText().toString()).commit();
        if (mChoiceRadioFour.isChecked())
            editor.putString(OPTION_THREE, mChoiceRadioFour.getText().toString()).commit();
        if (mChoiceRadioFive.isChecked())
            editor.putString(OPTION_THREE, mChoiceRadioFive.getText().toString()).commit();
    }

    private void enableChoices(int choiceSize) {
        switch (choiceSize) {
            case 1:
                mChoiceRadioOne.setVisibility(View.VISIBLE);
                mChoiceRadioTwo.setVisibility(View.GONE);
                mChoiceRadioThree.setVisibility(View.GONE);
                mChoiceRadioFour.setVisibility(View.GONE);
                mChoiceRadioFive.setVisibility(View.GONE);
                mChoiceRadioOne.setText(mChoice_1);
                break;
            case 2:
                mChoiceRadioOne.setVisibility(View.VISIBLE);
                mChoiceRadioTwo.setVisibility(View.VISIBLE);
                mChoiceRadioThree.setVisibility(View.GONE);
                mChoiceRadioFour.setVisibility(View.GONE);
                mChoiceRadioFive.setVisibility(View.GONE);
                mChoiceRadioOne.setText(mChoice_1);
                mChoiceRadioTwo.setText(mChoice_2);
                break;
            case 3:
                mChoiceRadioOne.setVisibility(View.VISIBLE);
                mChoiceRadioTwo.setVisibility(View.VISIBLE);
                mChoiceRadioThree.setVisibility(View.VISIBLE);
                mChoiceRadioFour.setVisibility(View.GONE);
                mChoiceRadioFive.setVisibility(View.GONE);
                mChoiceRadioOne.setText(mChoice_1);
                mChoiceRadioTwo.setText(mChoice_2);
                mChoiceRadioThree.setText(mChoice_3);
                break;
            case 4:
                mChoiceRadioOne.setVisibility(View.VISIBLE);
                mChoiceRadioTwo.setVisibility(View.VISIBLE);
                mChoiceRadioThree.setVisibility(View.VISIBLE);
                mChoiceRadioFour.setVisibility(View.VISIBLE);
                mChoiceRadioFive.setVisibility(View.GONE);
                mChoiceRadioOne.setText(mChoice_1);
                mChoiceRadioTwo.setText(mChoice_2);
                mChoiceRadioThree.setText(mChoice_3);
                mChoiceRadioFour.setText(mChoice_4);
                break;
            case 5:
                mChoiceRadioOne.setVisibility(View.VISIBLE);
                mChoiceRadioTwo.setVisibility(View.VISIBLE);
                mChoiceRadioThree.setVisibility(View.VISIBLE);
                mChoiceRadioFour.setVisibility(View.VISIBLE);
                mChoiceRadioFive.setVisibility(View.VISIBLE);
                mChoiceRadioOne.setText(mChoice_1);
                mChoiceRadioTwo.setText(mChoice_2);
                mChoiceRadioThree.setText(mChoice_3);
                mChoiceRadioFour.setText(mChoice_4);
                mChoiceRadioFive.setText(mChoice_5);
                break;
        }
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        editor.putInt(OPTION_THREE, itemList.get(position).mChoiceOptionId).commit();
    }
}
