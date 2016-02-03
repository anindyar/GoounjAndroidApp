package com.orgware.polling.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orgware.polling.R;
import com.orgware.polling.interfaces.RestApiListener;
import com.orgware.polling.network.RestApiProcessor;
import com.orgware.polling.utils.Methodutils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 22/10/15.
 */
public class PollQuick extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    LinearLayout mLayoutQtsOne, mLayoutQtsTwo, mLayoutQtsThree, mLayoutSubQtsOne, mLayoutSubQtsTwo, mLayoutSubQtsThree;
    CheckBox mCbOpenQtsOne, mCbOpenQtsTwo, mCbOpenQtsThree;
    RadioButton mRbNumQtsOne, mRbNumQtsTwo, mRbNumQtsThree;
    RelativeLayout mLayoutQuestionOne, mLayoutQuestionTwo, mLayoutQuestionThree;
    TextView mBtnReset, mBtnSubmit;
    EditText mEdittextQuickPollName, quick_ques_one, quick_ques_two, quick_ques_three;
    TextView txtCategory;
    JSONArray mContactArray = new JSONArray();
    JSONArray mChoicesArray = new JSONArray();
    Button btnContactOpen, btnViewContact;
    JSONArray mQtsOne;
    JSONArray mContactJsonArray, mContactArrayNames;
    JSONObject mQtsObjectOne, mQtsObjectTwo, mQtsObjectThree;
    TextView mPollName, mPollTypeOne, mPollTypeTwo, mPollTypeTh, mPollQtsOne, mPollQtsTwo, mPollQtsTh;
    int mPollType;

    @Override
    public void setTitle() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPollType = getArguments().getInt(PAGER_COUNT);
        try {
            mContactJsonArray = new JSONArray("[]");
            mContactArrayNames = new JSONArray("[]");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 2; i++) {
//            mCategoryList.add(" Category - " + i);
            mContactArray.put("909591454" + i);
        }
        mChoicesArray.put("Yes").put("No").put("Maybe");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quick_poll, container, false);

        mPollName = (TextView) v.findViewById(R.id.txt_pollname);
        mPollTypeOne = (TextView) v.findViewById(R.id.txt_polltype_one);
        mPollTypeTwo = (TextView) v.findViewById(R.id.txt_polltype_two);
        mPollTypeTh = (TextView) v.findViewById(R.id.txt_polltype_th);
        mPollQtsOne = (TextView) v.findViewById(R.id.txt_poll_qts_one);
        mPollQtsTwo = (TextView) v.findViewById(R.id.txt_poll_qts_two);
        mPollQtsTh = (TextView) v.findViewById(R.id.txt_poll_qts_th);

        txtCategory = (TextView) v.findViewById(R.id.txtCategory);
        txtCategory.setOnClickListener(this);
        btnViewContact = (Button) v.findViewById(R.id.btnViewContact);
        btnViewContact.setOnClickListener(this);
        mEdittextQuickPollName = (EditText) v.findViewById(R.id.edittext_quick_pollname);
        quick_ques_one = (EditText) v.findViewById(R.id.edittext_quick_qts_one_pollqts_one);
        quick_ques_two = (EditText) v.findViewById(R.id.edittext_quick_qts_two_pollqts_one);
        quick_ques_three = (EditText) v.findViewById(R.id.edittext_quick_qts_three_pollqts_one);

        (mCbOpenQtsOne = (CheckBox) v.findViewById(R.id.cb_quick_qts_one)).setOnCheckedChangeListener(this);
        (mCbOpenQtsTwo = (CheckBox) v.findViewById(R.id.cb_quick_qts_two)).setOnCheckedChangeListener(this);
        (mCbOpenQtsThree = (CheckBox) v.findViewById(R.id.cb_quick_qts_three)).setOnCheckedChangeListener(this);

        (btnContactOpen = (Button) v.findViewById(R.id.btnQuickContact)).setOnClickListener(this);
        mLayoutQtsOne = (LinearLayout) v.findViewById(R.id.layout_quick_qts_one);
        mLayoutQtsTwo = (LinearLayout) v.findViewById(R.id.layout_quick_qts_two);
        mLayoutQtsThree = (LinearLayout) v.findViewById(R.id.layout_quick_qts_three);
        mLayoutSubQtsOne = (LinearLayout) v.findViewById(R.id.quick_qts_one_exten);
        mLayoutSubQtsTwo = (LinearLayout) v.findViewById(R.id.quick_qts_two_exten);
        mLayoutSubQtsThree = (LinearLayout) v.findViewById(R.id.quick_qts_three_exten);
        mLayoutQuestionOne = (RelativeLayout) v.findViewById(R.id.layout_quick_ques_one);
        mLayoutQuestionTwo = (RelativeLayout) v.findViewById(R.id.layout_quick_ques_two);
        mLayoutQuestionThree = (RelativeLayout) v.findViewById(R.id.layout_quick_ques_three);

//        mCbQtsOne = (CheckBox) v.findViewById(R.id.cb_opinion_qts_one);
//        mCbQtsTwo = (CheckBox) v.findViewById(R.id.cb_opinion_qts_two);
//        mCbQtsThree = (CheckBox) v.findViewById(R.id.cb_opinion_qts_three);
        mRbNumQtsOne = (RadioButton) v.findViewById(R.id.radio_quick_qtsno_one);
        mRbNumQtsTwo = (RadioButton) v.findViewById(R.id.radio_quick_qtsno_two);
        mRbNumQtsThree = (RadioButton) v.findViewById(R.id.radio_quick_qtsno_three);
        mBtnReset = (TextView) v.findViewById(R.id.btnReset_Quick);
        mBtnSubmit = (TextView) v.findViewById(R.id.btnSubmit_quick);
        mBtnReset.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
//        mCbQtsOne.setOnCheckedChangeListener(this);
//        mCbQtsTwo.setOnCheckedChangeListener(this);
//        mCbQtsThree.setOnCheckedChangeListener(this);
        mRbNumQtsOne.setOnCheckedChangeListener(this);
        mRbNumQtsTwo.setOnCheckedChangeListener(this);
        mRbNumQtsThree.setOnCheckedChangeListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRbNumQtsOne.setChecked(true);
        filterPollType(mPollType);
    }


    private void filterPollType(int mPollType) {
        switch (mPollType) {
            case 1:
                mPollName.setText("Poll Name");
                mEdittextQuickPollName.setHint("Enter Poll Name");
                mPollTypeOne.setText("Poll One");
                mPollQtsOne.setText("Poll Question");
                mPollTypeTwo.setText("Poll Two");
                mPollQtsTwo.setText("Poll Question");
                mPollTypeTh.setText("Poll Three");
                mPollQtsTh.setText("Poll Question");
                break;
            case 2:
                mPollName.setText("Survey Name");
                mEdittextQuickPollName.setHint("Enter Survey Name");
                mPollTypeOne.setText("Survey Type");
                mPollQtsOne.setText("Survey Question");
                mPollTypeTwo.setText("Survey Type");
                mPollQtsTwo.setText("Survey Question");
                mPollTypeTh.setText("Survey Type");
                mPollQtsTh.setText("Survey Question");
                break;
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnViewContact:
                Methodutils.showNamesDialog(act, mContactArrayNames);
                break;
            case R.id.btnReset_Quick:
                resetValues();
                editor.putString(CONTACT_ARRAY, "").commit();
                break;
            case R.id.btnSubmit_quick:
                validateSubmitValues();
                break;
            case R.id.txtCategory:
                Methodutils.showCategoryDialog(act, txtCategory, Methodutils.setCategoryName());
                break;
            case R.id.btnQuickContact:
                showContactDialog(mContactJsonArray, mContactArrayNames);
                Log.e("Contact Array", "" + mContactJsonArray.toString());
                if (mContactJsonArray.length() != 0)
                    btnViewContact.setVisibility(View.VISIBLE);
                else
                    btnViewContact.setVisibility(View.GONE);
                Log.e("Contact Array", "" + mContactJsonArray.toString());
                break;
        }
    }

    private void showNamesDialog(JSONArray mContactArrayNames) {
        Dialog mContactDialog = new Dialog(act, R.style.dialog);
        mContactDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContactDialog.setCancelable(true);
        mContactDialog.setContentView(R.layout.dialog_names);
        ListView textView = (ListView) mContactDialog.findViewById(R.id.list_contact_names);
        ArrayAdapter<String> mNamesAdapter;
        List<String> mNamesList = new ArrayList<>();
        mNamesList.clear();
        try {
            for (int i = 0; i < mContactArrayNames.length(); i++) {
                mNamesList.add("" + mContactArrayNames.get(i));
            }
            mNamesAdapter = new ArrayAdapter<String>(act, android.R.layout.simple_list_item_1, mNamesList);
            textView.setAdapter(mNamesAdapter);
        } catch (Exception e) {

        }

        mContactDialog.show();
    }

    private void resetValues() {
        mEdittextQuickPollName.setText("");
        txtCategory.setText("Select Category");
        quick_ques_one.setText("");
        quick_ques_two.setText("");
        quick_ques_three.setText("");
    }

    private void validateSubmitValues() {
        int numOfQts = preferences.getInt(QUICK_NUMOFQTS, 1);
        if (mEdittextQuickPollName.getText().toString().equals("")) {
            makeToast("Please enter poll name");
            return;
        }
        if (txtCategory.getText().toString().equals("Select Category")) {
            makeToast("Please choose category.");
            return;
        }
        if (numOfQts == 1) {
            if (quick_ques_one.getText().toString().equals("")) {
                makeToast("Question 1 should not be empty!");
                return;
            } else {
                mQtsOne = new JSONArray();
                mQtsObjectOne = new JSONObject();
                try {
                    mQtsObjectOne.put("question", "" + quick_ques_one.getText().toString());
                    mQtsObjectOne.put("questionType", "text");
                    mQtsObjectOne.put("choices", mChoicesArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mQtsOne.put(mQtsObjectOne);
                Log.e("No", "No.1 - " + mQtsOne);
            }
        }
        if (numOfQts == 2) {
            if (quick_ques_one.getText().toString().equals("")) {
                makeToast("Question 1 should not be empty!");
                return;
            } else if (quick_ques_two.getText().toString().equals("")) {
                makeToast("Question 2 should not be empty!");
                return;
            } else {
                mQtsOne = new JSONArray();
                mQtsObjectOne = new JSONObject();
                mQtsObjectTwo = new JSONObject();
                try {
                    mQtsObjectOne.put("question", "" + quick_ques_one.getText().toString());
                    mQtsObjectOne.put("questionType", "text");
                    mQtsObjectOne.put("choices", mChoicesArray);
                    mQtsObjectTwo.put("question", "" + quick_ques_two.getText().toString());
                    mQtsObjectTwo.put("questionType", "text");
                    mQtsObjectTwo.put("choices", mChoicesArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mQtsOne.put(mQtsObjectOne);
                mQtsOne.put(mQtsObjectTwo);
                Log.e("No", "No.2 - " + mQtsOne);
            }
        }
        if (numOfQts == 3) {
            if (quick_ques_one.getText().toString().equals("")) {
                makeToast("Question 1 should not be empty!");
                return;
            } else if (quick_ques_two.getText().toString().equals("")) {
                makeToast("Question 2 should not be empty!");
                return;
            } else if (quick_ques_three.getText().toString().equals("")) {
                makeToast("Question 3 should not be empty!");
                return;
            } else {
                mQtsOne = new JSONArray();
                mQtsObjectOne = new JSONObject();
                mQtsObjectTwo = new JSONObject();
                mQtsObjectThree = new JSONObject();
                try {
                    mQtsObjectOne.put("question", "" + quick_ques_one.getText().toString());
                    mQtsObjectOne.put("questionType", "text");
                    mQtsObjectOne.put("choices", mChoicesArray);
                    mQtsObjectTwo.put("question", "" + quick_ques_two.getText().toString());
                    mQtsObjectTwo.put("questionType", "text");
                    mQtsObjectTwo.put("choices", mChoicesArray);
                    mQtsObjectThree.put("question", "" + quick_ques_three.getText().toString());
                    mQtsObjectThree.put("questionType", "text");
                    mQtsObjectThree.put("choices", mChoicesArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mQtsOne.put(mQtsObjectOne);
                mQtsOne.put(mQtsObjectTwo);
                mQtsOne.put(mQtsObjectThree);
                Log.e("No", "No.3 - " + mQtsOne);
            }
        }

        if (mContactJsonArray.length() == 0) {
            makeToast("You have to add atleast one contact");
            return;
        }

//        if (preferences.getString(CONTACT_ARRAY, "").equals("")) {
//            makeToast("Please add atleast one contact");
//            return;
//        }

        try {
            processQuickPollCreation(BASE_URL + CREATE_POLL_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String quickParameter() {
//        String contactArray = preferences.getString(CONTACT_ARRAY, "");
//        contactArray = contactArray.replaceAll("\\\\", "");
        JSONObject userdetails = new JSONObject();
        try {
            userdetails.put(POLL_NAME, "" + mEdittextQuickPollName.getText().toString());
            userdetails.put(POLL_ISBOOST, "0");
            userdetails.put(POLL_VISIBILITY_TYPE, "visible");
            userdetails.put(POLL_REWARD_TYPE, "free");
            userdetails.put(POLL_CATEGORY, "" + txtCategory.getText().toString());
            userdetails.put(POLL_CREATE_USERID, "" + preferences.getString(USER_ID, ""));
            userdetails.put(POLL_TYPE, QUICK_POLL);
            userdetails.put(POLL_QUESTION_LIST, mQtsOne);
            userdetails.put(POLL_AUDIENCE, mContactJsonArray);
            Log.e("Array", mContactJsonArray.toString());
            Log.e("jsonmessage", userdetails.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userdetails.toString();
    }

    private void processQuickPollCreation(String url) throws Exception {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.POST, url, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                Methodutils.messageWithTitle(act, "Success", "Quick Poll Created successfully", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        act.getSupportFragmentManager().popBackStack();
                    }
                });
            }

            @Override
            public void onRequestFailed(Exception e) {
                Methodutils.messageWithTitle(act, "Error", "Error from server while creating poll", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });
        processor.execute(quickParameter().toString());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (mRbNumQtsOne.isChecked()) {
            editor.putInt(QUICK_NUMOFQTS, 1).commit();
            mLayoutQtsOne.setVisibility(View.VISIBLE);
            mLayoutQtsTwo.setVisibility(View.GONE);
            mLayoutQtsThree.setVisibility(View.GONE);

        }
        if (mRbNumQtsTwo.isChecked()) {
            editor.putInt(QUICK_NUMOFQTS, 2).commit();
            if (mLayoutQtsOne.getVisibility() != View.VISIBLE)
                mLayoutQtsOne.setVisibility(View.VISIBLE);
            mLayoutQtsTwo.setVisibility(View.VISIBLE);
            mLayoutQtsThree.setVisibility(View.GONE);
        }
        if (mRbNumQtsThree.isChecked()) {
            editor.putInt(QUICK_NUMOFQTS, 3).commit();
            if (mLayoutQtsOne.getVisibility() != View.VISIBLE)
                mLayoutQtsOne.setVisibility(View.VISIBLE);
            if (mLayoutQtsTwo.getVisibility() != View.VISIBLE)
                mLayoutQtsTwo.setVisibility(View.VISIBLE);
            mLayoutQtsThree.setVisibility(View.VISIBLE);
        }

        if (mCbOpenQtsOne.isChecked())
            mLayoutSubQtsOne.setVisibility(View.VISIBLE);
        else
            mLayoutSubQtsOne.setVisibility(View.GONE);

        if (mCbOpenQtsTwo.isChecked())
            mLayoutSubQtsTwo.setVisibility(View.VISIBLE);
        else
            mLayoutSubQtsTwo.setVisibility(View.GONE);

        if (mCbOpenQtsThree.isChecked())
            mLayoutSubQtsThree.setVisibility(View.VISIBLE);
        else
            mLayoutSubQtsThree.setVisibility(View.GONE);


    }
}