package com.orgware.polling.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orgware.polling.R;
import com.orgware.polling.adapters.SpinnerAdapter;
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
public class PollSurvey extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final int PICK_CONTACT = 1;
    LinearLayout mLayoutQtsOne, mLayoutQtsTwo, mLayoutQtsThree, mLayoutSubQtsOne, mLayoutSubQtsTwo, mLayoutSubQtsThree;
    CheckBox mCbQtsOne, mCbQtsTwo, mCbQtsThree;
    RadioButton mRbNumQtsOne, mRbNumQtsTwo, mRbNumQtsThree;
    RelativeLayout mLayoutQuestionOne, mLayoutQuestionTwo, mLayoutQuestionThree;
    EditText mEdittextSurveyPollName, survey_ques_one, survey_ques_two, survey_ques_three;
    TextView btnReset_survey, btnSubmit_Survey;
    JSONObject mSurveyObject = new JSONObject();
    JSONArray mContactArray = new JSONArray();
    JSONArray mQuestionArray = new JSONArray();
    JSONArray mChoicesArray = new JSONArray();
    JSONArray mQtsOne;
    JSONArray mContactJsonArray, mContactArrayNames;
    JSONObject mQtsObjectOne, mQtsObjectTwo, mQtsObjectThree;
    Button btnContactDevice, btnViewContect;
    Dialog mCategoryDialog;
    ListView mCategoryListview;
    SpinnerAdapter mSpinnerAdapter;
    List<String> mCategoryList = new ArrayList<>();
    TextView txtCategory;
    TextView mPollName, mPollTypeOne, mPollTypeTwo, mPollTypeTh, mPollQtsOne, mPollQtsTwo, mPollQtsTh;
    int mPollType;

    TextWatcher mPollNameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                editor.putString(SURVEY_POLLNAME, s.toString()).commit();
            }
        }
    };

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPollType = getArguments().getInt("page_type");

        mChoicesArray.put("Totally Agree").put("Somewhat Agree").put("Satisfactory").put("Somewhat Disagree").put("Totally Disagree");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_survey_poll, container, false);

        mPollName = (TextView) v.findViewById(R.id.txt_pollname);
        mPollTypeOne = (TextView) v.findViewById(R.id.txt_polltype_one);
        mPollTypeTwo = (TextView) v.findViewById(R.id.txt_polltype_two);
        mPollTypeTh = (TextView) v.findViewById(R.id.txt_polltype_th);
        mPollQtsOne = (TextView) v.findViewById(R.id.txt_poll_qts_one);
        mPollQtsTwo = (TextView) v.findViewById(R.id.txt_poll_qts_two);
        mPollQtsTh = (TextView) v.findViewById(R.id.txt_poll_qts_th);

        btnContactDevice = (Button) v.findViewById(R.id.btncontactFromDevice);
        btnViewContect = (Button) v.findViewById(R.id.btnViewContact);
        btnViewContect.setOnClickListener(this);
        btnContactDevice.setOnClickListener(this);
        txtCategory = (TextView) v.findViewById(R.id.txtCategory);
        txtCategory.setOnClickListener(this);
        mEdittextSurveyPollName = (EditText) v.findViewById(R.id.edittext_survey_pollname);
        survey_ques_one = (EditText) v.findViewById(R.id.edittext_survey_qts_one_pollqts);
        survey_ques_two = (EditText) v.findViewById(R.id.edittext_survey_qts_two_pollqts);
        survey_ques_three = (EditText) v.findViewById(R.id.edittext_survey_qts_three_pollqts);
        btnSubmit_Survey = (TextView) v.findViewById(R.id.btnSubmit_Survey);
        btnReset_survey = (TextView) v.findViewById(R.id.btnReset_survey);

        mLayoutQtsOne = (LinearLayout) v.findViewById(R.id.layout_survey_qts_one);
        mLayoutQtsTwo = (LinearLayout) v.findViewById(R.id.layout_survey_qts_two);
        mLayoutQtsThree = (LinearLayout) v.findViewById(R.id.layout_survey_qts_three);
        mLayoutSubQtsOne = (LinearLayout) v.findViewById(R.id.ques_survey_one_exten);
        mLayoutSubQtsTwo = (LinearLayout) v.findViewById(R.id.ques_survey_two_exten);
        mLayoutSubQtsThree = (LinearLayout) v.findViewById(R.id.ques_survey_three_exten);
        mLayoutQuestionOne = (RelativeLayout) v.findViewById(R.id.layout_survey_ques_one);
        mLayoutQuestionTwo = (RelativeLayout) v.findViewById(R.id.layout_survey_ques_two);
        mLayoutQuestionThree = (RelativeLayout) v.findViewById(R.id.layout_survey_ques_three);

        mCbQtsOne = (CheckBox) v.findViewById(R.id.cb_survey_qts_one);
        mCbQtsTwo = (CheckBox) v.findViewById(R.id.cb_survey_qts_two);
        mCbQtsThree = (CheckBox) v.findViewById(R.id.cb_survey_qts_three);
        mRbNumQtsOne = (RadioButton) v.findViewById(R.id.radio_survey_qtsno_one);
        mRbNumQtsTwo = (RadioButton) v.findViewById(R.id.radio_survey_qtsno_two);
        mRbNumQtsThree = (RadioButton) v.findViewById(R.id.radio_survey_qtsno_three);
        btnReset_survey.setOnClickListener(this);
        btnSubmit_Survey.setOnClickListener(this);
        mCbQtsOne.setOnCheckedChangeListener(this);
        mCbQtsTwo.setOnCheckedChangeListener(this);
        mCbQtsThree.setOnCheckedChangeListener(this);
        mEdittextSurveyPollName.addTextChangedListener(mPollNameWatcher);
        mRbNumQtsOne.setOnCheckedChangeListener(this);
        mRbNumQtsTwo.setOnCheckedChangeListener(this);
        mRbNumQtsThree.setOnCheckedChangeListener(this);


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            mContactJsonArray = new JSONArray("[]");
            mContactArrayNames = new JSONArray("[]");
        } catch (Exception e) {
            e.printStackTrace();
        }
        mRbNumQtsOne.setChecked(true);
        filterPollType(mPollType);
    }

    private void filterPollType(int mPollType) {
        switch (mPollType) {
            case 1:
                mPollName.setText("Poll Name");
                mEdittextSurveyPollName.setHint("Enter Poll Name");
                mPollTypeOne.setText("Poll One");
                mPollQtsOne.setText("Poll Question");
                mPollTypeTwo.setText("Poll Two");
                mPollQtsTwo.setText("Poll Question");
                mPollTypeTh.setText("Poll Three");
                mPollQtsTh.setText("Poll Question");
                btnContactDevice.setVisibility(View.VISIBLE);
                btnViewContect.setVisibility(View.VISIBLE);
                break;
            case 2:
                btnContactDevice.setVisibility(View.GONE);
                btnViewContect.setVisibility(View.GONE);
                mPollName.setText("Survey Name");
                mEdittextSurveyPollName.setHint("Enter Survey Name");
                mPollTypeOne.setText("Survey Type");
                mPollQtsOne.setText("Survey Question");
                mPollTypeTwo.setText("Survey Type");
                mPollQtsTwo.setText("Survey Question");
                mPollTypeTh.setText("Survey Type");
                mPollQtsTh.setText("Survey Question");
                break;
        }
    }

    private void launchMultiplePhonePicker() {
        try {
            Intent phonebookIntent = new Intent("intent.action.INTERACTION_TOPMENU");
            phonebookIntent.putExtra("additional", "phone-multi");
            startActivityForResult(phonebookIntent, PICK_CONTACT);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getData(String contact, int which) {
        return contact.split(";")[which];
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT) {
            final int URI = 0;
            final int NUMBER = 1;
            if (act.RESULT_OK != resultCode) return;
            Bundle contactUri = data.getExtras();
            if (null == contactUri) return;
            ArrayList<String> contacts = (ArrayList<String>) contactUri.get("result");
            for (int i = 0; i < contacts.size(); i++) {
                Toast.makeText(act, getData(contacts.get(i), NUMBER), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public String surveyParameter() {
        JSONObject userdetails = new JSONObject();
        try {
            userdetails.put(POLL_NAME, "" + preferences.getString(SURVEY_POLLNAME, ""));
            userdetails.put(POLL_ISBOOST, "0");
            userdetails.put(POLL_VISIBILITY_TYPE, "visible");
            userdetails.put(POLL_REWARD_TYPE, "free");
            userdetails.put(POLL_CATEGORY, "" + txtCategory.getText().toString());
            userdetails.put(POLL_CREATE_USERID, "" + preferences.getString(USER_ID, ""));
            userdetails.put(POLL_TYPE, FEEDBACK);
            userdetails.put(POLL_QUESTION_LIST, mQtsOne);
//            userdetails.put(POLL_AUDIENCE, mContactJsonArray);
            if (mPollType == 1) {
                userdetails.put(POLL_AUDIENCE, mContactJsonArray);
            } else
                userdetails.put("isSurvey", 1);
            Log.e("jsonmessage", userdetails.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userdetails.toString();
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
                if (mContactJsonArray.length() == 0)
                    makeToast("No Contacts Added");
                else
                    Methodutils.showNamesDialog(act, mContactArrayNames);
                break;
            case R.id.btnReset_survey:
                resetValues();
                editor.putString(CONTACT_ARRAY, "").commit();
                break;
            case R.id.btnSubmit_Survey:
                validateSubmitValues();
                break;
            case R.id.btncontactFromDevice:
                showContactDialog(mContactJsonArray, mContactArrayNames);
                Log.e("Contact Array", "" + mContactJsonArray.toString());
//                if (mContactJsonArray.length() != 0)
//                    btnViewContect.setVisibility(View.VISIBLE);
//                else
//                    btnViewContect.setVisibility(View.GONE);
                Log.e("Contact Array", "" + mContactJsonArray.toString());
                break;
            case R.id.txtCategory:
                Methodutils.showCategoryDialog(act, txtCategory, Methodutils.setCategoryName());
                break;
        }

    }

    private void resetValues() {
        mEdittextSurveyPollName.setText("");
        txtCategory.setText("Select Category");
        survey_ques_one.setText("");
        survey_ques_two.setText("");
        survey_ques_three.setText("");
    }

    private void validateSubmitValues() {
        int numOfQts = preferences.getInt(SURVEY_NUMOFQTS, 1);
        if (mEdittextSurveyPollName.getText().toString().equals("")) {
            makeToast("Please enter poll name");
            return;
        }

        if (txtCategory.getText().toString().equals("Select Category")) {
            makeToast("Please choose category.");
            return;
        }

        if (numOfQts == 1) {
            if (survey_ques_one.getText().toString().equals("")) {
                makeToast("Question 1 should not be empty!");
                return;
            } else {
                mQtsOne = new JSONArray();
                mQtsObjectOne = new JSONObject();
                try {
                    mQtsObjectOne.put("question", "" + survey_ques_one.getText().toString());
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
            if (survey_ques_one.getText().toString().equals("")) {
                makeToast("Question 1 should not be empty!");
                return;
            } else if (survey_ques_two.getText().toString().equals("")) {
                makeToast("Question 2 should not be empty!");
                return;
            } else {
                mQtsOne = new JSONArray();
                mQtsObjectOne = new JSONObject();
                mQtsObjectTwo = new JSONObject();
                try {
                    mQtsObjectOne.put("question", "" + survey_ques_one.getText().toString());
                    mQtsObjectOne.put("questionType", "text");
                    mQtsObjectOne.put("choices", mChoicesArray);
                    mQtsObjectTwo.put("question", "" + survey_ques_two.getText().toString());
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
            if (survey_ques_one.getText().toString().equals("")) {
                makeToast("Question 1 should not be empty!");
                return;
            } else if (survey_ques_two.getText().toString().equals("")) {
                makeToast("Question 2 should not be empty!");
                return;
            } else if (survey_ques_three.getText().toString().equals("")) {
                makeToast("Question 3 should not be empty!");
                return;
            } else {
                mQtsOne = new JSONArray();
                mQtsObjectOne = new JSONObject();
                mQtsObjectTwo = new JSONObject();
                mQtsObjectThree = new JSONObject();
                try {
                    mQtsObjectOne.put("question", "" + survey_ques_one.getText().toString());
                    mQtsObjectOne.put("questionType", "text");
                    mQtsObjectOne.put("choices", mChoicesArray);
                    mQtsObjectTwo.put("question", "" + survey_ques_two.getText().toString());
                    mQtsObjectTwo.put("questionType", "text");
                    mQtsObjectTwo.put("choices", mChoicesArray);
                    mQtsObjectThree.put("question", "" + survey_ques_two.getText().toString());
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
        if (mPollType == 1) {
            if (mContactJsonArray.length() == 0) {
                makeToast("You have to add atleast one contact");
                return;
            }
        }

        try {
            if (mPollType == 1)
                processSurveyPollCreation(BASE_URL + CREATE_POLL_URL, "Feedback Poll Created successfully");
            else
                processSurveyPollCreation(BASE_URL + CREATE_SURVEY_URL, "Feedback Survey Created successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void processSurveyPollCreation(String url, final String reqMessage) throws Exception {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.POST, url, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                Methodutils.messageWithTitle(act, "Success", "" + reqMessage, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        act.getSupportFragmentManager().popBackStack();
                    }
                });
            }

            @Override
            public void onRequestFailed(Exception e) {
                if (e == null) {
                    Log.e("Error", "Exception is null");
                    Methodutils.message(act, "Internal Server Error. Requested Action Failed", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                } else {
                    Log.e("Error", "Exception is not null");
                    Methodutils.message(act, "" + e.getMessage(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                }
            }
        });
        processor.execute(surveyParameter().toString());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (mRbNumQtsOne.isChecked()) {
//            mLayoutQuestionOne.setVisibility(View.VISIBLE);
//            mLayoutQuestionTwo.setVisibility(View.GONE);
//            mLayoutQuestionThree.setVisibility(View.GONE);
            editor.putInt(SURVEY_NUMOFQTS, 1).commit();
            mLayoutQtsOne.setVisibility(View.VISIBLE);
            mLayoutQtsTwo.setVisibility(View.GONE);
            mLayoutQtsThree.setVisibility(View.GONE);

        }
        if (mRbNumQtsTwo.isChecked()) {
            editor.putInt(SURVEY_NUMOFQTS, 2).commit();
            if (mLayoutQtsOne.getVisibility() != View.VISIBLE)
                mLayoutQtsOne.setVisibility(View.VISIBLE);
            mLayoutQtsTwo.setVisibility(View.VISIBLE);
            mLayoutQtsThree.setVisibility(View.GONE);
        }
        if (mRbNumQtsThree.isChecked()) {
            editor.putInt(SURVEY_NUMOFQTS, 3).commit();
            if (mLayoutQtsOne.getVisibility() != View.VISIBLE)
                mLayoutQtsOne.setVisibility(View.VISIBLE);
            if (mLayoutQtsTwo.getVisibility() != View.VISIBLE)
                mLayoutQtsTwo.setVisibility(View.VISIBLE);

            mLayoutQtsThree.setVisibility(View.VISIBLE);
        }
        if (mCbQtsOne.isChecked())
            mLayoutSubQtsOne.setVisibility(View.VISIBLE);
        else
            mLayoutSubQtsOne.setVisibility(View.GONE);

        if (mCbQtsTwo.isChecked())
            mLayoutSubQtsTwo.setVisibility(View.VISIBLE);
        else
            mLayoutSubQtsTwo.setVisibility(View.GONE);

        if (mCbQtsThree.isChecked())
            mLayoutSubQtsThree.setVisibility(View.VISIBLE);
        else
            mLayoutSubQtsThree.setVisibility(View.GONE);


    }
}