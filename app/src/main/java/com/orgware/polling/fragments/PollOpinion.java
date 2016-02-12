package com.orgware.polling.fragments;

import android.app.Dialog;
import android.opengl.Visibility;
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
public class PollOpinion extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    LinearLayout mLayoutQtsOne, mLayoutQtsTwo, mLayoutQtsThree, mLayoutSubQtsOne, mLayoutSubQtsTwo, mLayoutSubQtsThree;
    CheckBox mCbQtsOne, mCbQtsTwo, mCbQtsThree;
    RadioButton mRbNumQtsOne, mRbNumQtsTwo, mRbNumQtsThree, mRbQtsOneChoiceOne, mRbQtsOneChoiceTwo, mRbQtsOneChoiceThree, mRbQtsOneChoiceFour, mRbQtsOneChoiceFive,
            mRbQtsTwoChoiceOne, mRbQtsTwoChoiceTwo, mRbQtsTwoChoiceThree, mRbQtsTwoChoiceFour, mRbQtsTwoChoiceFive, mRbQtsThChoiceOne, mRbQtsThChoiceTwo, mRbQtsThChoiceThree,
            mRbQtsThChoiceFour, mRbQtsThChoiceFive;
    RelativeLayout mLayoutQuestionOne, mLayoutQuestionTwo, mLayoutQuestionThree, mLayoutQtsOneOne, mLayoutQtsOneTwo, mLayoutQtsOneThree, mLayoutQtsOneFour, mLayoutQtsOneFive,
            mLayoutQtsTwoOne, mLayoutQtsTwoTwo, mLayoutQtsTwoThree, mLayoutQtsTwoFour, mLayoutQtsTwoFive, mLayoutQtsThOne, mLayoutQtsThTwo, mLayoutQtsThThree, mLayoutQtsThFour, mLayoutQtsThFive;
    EditText mEdittextopinionPollName, opinion_ques_one, opinion_ques_two, opinion_ques_three, mTxtQtsOneOne, mTxtQtsOneTwo, mTxtQtsOneThree, mTxtQtsOneFour, mTxtQtsOneFive,
            mTxtQtsTwoOne, mTxtQtsTwoTwom, mTxtQtsTwoThree, mTxtQtsTwoFour, mTxtQtsTwoFive, mTxtQtsThOne, mTxtQtsThTwo, mTxtQtsThThree, mTxtQtsThFour, mTxtQtsThFive;
    TextView btnReset_opinion, btnSubmit_opinion;
    TextView txtCategory, txtNoOpinionOne, txtNoOpinionTwo, txtNoOpinionThree;
    JSONArray mContactArray = new JSONArray();
    JSONArray mChoicesArrayOne, mChoicesArrayTwo, mChoicesArrayThree;
    Button btnContactOpinion, btnViewContact;
    JSONArray mQtsOne;
    JSONArray mContactJsonArray, mContactArrayNames;
    JSONObject mQtsObjectOne, mQtsObjectTwo, mQtsObjectThree;
    TextView mPollName, mPollTypeOne, mPollTypeTwo, mPollTypeTh, mPollQtsOne, mPollQtsTwo, mPollQtsTh;
    int mPollType, mMyPoll;

    @Override
    public void setTitle() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPollType = getArguments().getInt("page_type");
        mMyPoll = getArguments().getInt("myPoll");
        Log.e("MyPoll Opinion", "" + mMyPoll);

//        mPollType = getArguments().getInt("page_type");
//        try {
//            mContactJsonArray = new JSONArray("[]");
//            mContactArrayNames = new JSONArray("[]");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_opinion_poll, container, false);

        mPollName = (TextView) v.findViewById(R.id.txt_pollname);
        mPollTypeOne = (TextView) v.findViewById(R.id.txt_polltype_one);
        mPollTypeTwo = (TextView) v.findViewById(R.id.txt_polltype_two);
        mPollTypeTh = (TextView) v.findViewById(R.id.txt_polltype_th);
        mPollQtsOne = (TextView) v.findViewById(R.id.txt_poll_qts_one);
        mPollQtsTwo = (TextView) v.findViewById(R.id.txt_poll_qts_two);
        mPollQtsTh = (TextView) v.findViewById(R.id.txt_poll_qts_th);

        txtCategory = (TextView) v.findViewById(R.id.txtCategory);
        btnViewContact = (Button) v.findViewById(R.id.btnViewContact);
        btnViewContact.setOnClickListener(this);
        txtCategory.setOnClickListener(this);
        txtNoOpinionOne = (TextView) v.findViewById(R.id.text_opinion_qts_one_choice_six);
        txtNoOpinionTwo = (TextView) v.findViewById(R.id.text_opinion_qts_two_choice_six);
        txtNoOpinionThree = (TextView) v.findViewById(R.id.text_opinion_qts_three_choice_six);
        (btnContactOpinion = (Button) v.findViewById(R.id.btnOpinionContact)).setOnClickListener(this);
        mLayoutQtsOne = (LinearLayout) v.findViewById(R.id.layout_qts_one);
        mLayoutQtsTwo = (LinearLayout) v.findViewById(R.id.layout_qts_two);
        mLayoutQtsThree = (LinearLayout) v.findViewById(R.id.layout_qts_three);
        mLayoutSubQtsOne = (LinearLayout) v.findViewById(R.id.layout_opinion_qts_polltype_one);
        mLayoutSubQtsTwo = (LinearLayout) v.findViewById(R.id.layout_opinion_qts_polltype_two);
        mLayoutSubQtsThree = (LinearLayout) v.findViewById(R.id.layout_opinion_qts_polltype_three);
        mLayoutQuestionOne = (RelativeLayout) v.findViewById(R.id.layout_opinion_qts_one);
        mLayoutQuestionTwo = (RelativeLayout) v.findViewById(R.id.layout_opinion_qts_two);
        mLayoutQuestionThree = (RelativeLayout) v.findViewById(R.id.layout_opinion_qts_three);

        mLayoutQtsOneOne = (RelativeLayout) v.findViewById(R.id.layout_opinion_qts_one_edittext_choice_one);
        mLayoutQtsOneTwo = (RelativeLayout) v.findViewById(R.id.layout_opinion_qts_one_edittext_choice_two);
        mLayoutQtsOneThree = (RelativeLayout) v.findViewById(R.id.layout_opinion_qts_one_edittext_choice_three);
        mLayoutQtsOneFour = (RelativeLayout) v.findViewById(R.id.layout_opinion_qts_one_edittext_choice_four);
        mLayoutQtsOneFive = (RelativeLayout) v.findViewById(R.id.layout_opinion_qts_one_edittext_choice_five);

        mLayoutQtsTwoOne = (RelativeLayout) v.findViewById(R.id.layout_opinion_qts_two_edittext_choice_one);
        mLayoutQtsTwoTwo = (RelativeLayout) v.findViewById(R.id.layout_opinion_qts_two_edittext_choice_two);
        mLayoutQtsTwoThree = (RelativeLayout) v.findViewById(R.id.layout_opinion_qts_two_edittext_choice_three);
        mLayoutQtsTwoFour = (RelativeLayout) v.findViewById(R.id.layout_opinion_qts_two_edittext_choice_four);
        mLayoutQtsTwoFive = (RelativeLayout) v.findViewById(R.id.layout_opinion_qts_two_edittext_choice_five);

        mLayoutQtsThOne = (RelativeLayout) v.findViewById(R.id.layout_opinion_qts_three_edittext_choice_one);
        mLayoutQtsThTwo = (RelativeLayout) v.findViewById(R.id.layout_opinion_qts_three_edittext_choice_two);
        mLayoutQtsThThree = (RelativeLayout) v.findViewById(R.id.layout_opinion_qts_three_edittext_choice_three);
        mLayoutQtsThFour = (RelativeLayout) v.findViewById(R.id.layout_opinion_qts_three_edittext_choice_four);
        mLayoutQtsThFive = (RelativeLayout) v.findViewById(R.id.layout_opinion_qts_three_edittext_choice_five);

        mTxtQtsOneOne = (EditText) v.findViewById(R.id.edittext_opinion_qts_one_choice_one);
        mTxtQtsOneTwo = (EditText) v.findViewById(R.id.edittext_opinion_qts_one_choice_two);
        mTxtQtsOneThree = (EditText) v.findViewById(R.id.edittext_opinion_qts_one_choice_three);
        mTxtQtsOneFour = (EditText) v.findViewById(R.id.edittext_opinion_qts_one_choice_four);
        mTxtQtsOneFive = (EditText) v.findViewById(R.id.edittext_opinion_qts_one_choice_five);

        mTxtQtsTwoOne = (EditText) v.findViewById(R.id.edittext_opinion_qts_two_choice_one);
        mTxtQtsTwoTwom = (EditText) v.findViewById(R.id.edittext_opinion_qts_two_choice_two);
        mTxtQtsTwoThree = (EditText) v.findViewById(R.id.edittext_opinion_qts_two_choice_three);
        mTxtQtsTwoFour = (EditText) v.findViewById(R.id.edittext_opinion_qts_two_choice_four);
        mTxtQtsTwoFive = (EditText) v.findViewById(R.id.edittext_opinion_qts_two_choice_five);

        mTxtQtsThOne = (EditText) v.findViewById(R.id.edittext_opinion_qts_three_choice_one);
        mTxtQtsThTwo = (EditText) v.findViewById(R.id.edittext_opinion_qts_three_choice_two);
        mTxtQtsThThree = (EditText) v.findViewById(R.id.edittext_opinion_qts_three_choice_three);
        mTxtQtsThFour = (EditText) v.findViewById(R.id.edittext_opinion_qts_three_choice_four);
        mTxtQtsThFive = (EditText) v.findViewById(R.id.edittext_opinion_qts_three_choice_five);


        mEdittextopinionPollName = (EditText) v.findViewById(R.id.edittext_opinion_poll_name);
        opinion_ques_one = (EditText) v.findViewById(R.id.edittext_opinion_qts_one_pollqts_one);
        opinion_ques_two = (EditText) v.findViewById(R.id.edittext_opinion_qts_two_pollqts_one);
        opinion_ques_three = (EditText) v.findViewById(R.id.edittext_opinion_qts_three_pollqts_one);
        btnReset_opinion = (TextView) v.findViewById(R.id.btnReset_opinion);
        btnSubmit_opinion = (TextView) v.findViewById(R.id.btnSubmit_opinion);

        mCbQtsOne = (CheckBox) v.findViewById(R.id.cb_opinion_qts_one);
        mCbQtsTwo = (CheckBox) v.findViewById(R.id.cb_opinion_qts_two);
        mCbQtsThree = (CheckBox) v.findViewById(R.id.cb_opinion_qts_three);
        mRbNumQtsOne = (RadioButton) v.findViewById(R.id.radio_opinion_qts_one);
        mRbNumQtsTwo = (RadioButton) v.findViewById(R.id.radio_opinion_qts_two);
        mRbNumQtsThree = (RadioButton) v.findViewById(R.id.radio_opinion_qts_three);
        (mRbQtsOneChoiceOne = (RadioButton) v.findViewById(R.id.radio_opinion_qts_one_choices_one)).setOnCheckedChangeListener(this);
        (mRbQtsOneChoiceTwo = (RadioButton) v.findViewById(R.id.radio_opinion_qts_one_choices_two)).setOnCheckedChangeListener(this);
        (mRbQtsOneChoiceThree = (RadioButton) v.findViewById(R.id.radio_opinion_qts_one_choices_three)).setOnCheckedChangeListener(this);
        (mRbQtsOneChoiceFour = (RadioButton) v.findViewById(R.id.radio_opinion_qts_one_choices_four)).setOnCheckedChangeListener(this);
        (mRbQtsOneChoiceFive = (RadioButton) v.findViewById(R.id.radio_opinion_qts_one_choices_five)).setOnCheckedChangeListener(this);

        (mRbQtsTwoChoiceOne = (RadioButton) v.findViewById(R.id.radio_opinion_qts_two_choices_one)).setOnCheckedChangeListener(this);
        (mRbQtsTwoChoiceTwo = (RadioButton) v.findViewById(R.id.radio_opinion_qts_two_choices_two)).setOnCheckedChangeListener(this);
        (mRbQtsTwoChoiceThree = (RadioButton) v.findViewById(R.id.radio_opinion_qts_two_choices_three)).setOnCheckedChangeListener(this);
        (mRbQtsTwoChoiceFour = (RadioButton) v.findViewById(R.id.radio_opinion_qts_two_choices_four)).setOnCheckedChangeListener(this);
        (mRbQtsTwoChoiceFive = (RadioButton) v.findViewById(R.id.radio_opinion_qts_two_choices_five)).setOnCheckedChangeListener(this);

        (mRbQtsThChoiceOne = (RadioButton) v.findViewById(R.id.radio_opinion_qts_three_choices_one)).setOnCheckedChangeListener(this);
        (mRbQtsThChoiceTwo = (RadioButton) v.findViewById(R.id.radio_opinion_qts_three_choices_two)).setOnCheckedChangeListener(this);
        (mRbQtsThChoiceThree = (RadioButton) v.findViewById(R.id.radio_opinion_qts_three_choices_three)).setOnCheckedChangeListener(this);
        (mRbQtsThChoiceFour = (RadioButton) v.findViewById(R.id.radio_opinion_qts_three_choices_four)).setOnCheckedChangeListener(this);
        (mRbQtsThChoiceFive = (RadioButton) v.findViewById(R.id.radio_opinion_qts_three_choices_five)).setOnCheckedChangeListener(this);

        mCbQtsOne.setOnCheckedChangeListener(this);
        mCbQtsTwo.setOnCheckedChangeListener(this);
        mCbQtsThree.setOnCheckedChangeListener(this);
        mRbNumQtsOne.setOnCheckedChangeListener(this);
        mRbNumQtsTwo.setOnCheckedChangeListener(this);
        mRbNumQtsThree.setOnCheckedChangeListener(this);
        btnReset_opinion.setOnClickListener(this);
        btnSubmit_opinion.setOnClickListener(this);


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mMyPoll == 1) {
            restrictUIOnPollUpdate();
        }
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
        txtNoOpinionOne.setText("3");
        txtNoOpinionTwo.setText("3");
        txtNoOpinionThree.setText("3");
        filterPollType(mPollType);
    }

    private void filterPollType(int mPollType) {
        switch (mPollType) {
            case 1:
                mPollName.setText("Poll Name");
                mEdittextopinionPollName.setHint("Enter Poll Name");
                mPollTypeOne.setText("Poll One");
                mPollQtsOne.setText("Poll Question");
                mPollTypeTwo.setText("Poll Two");
                mPollQtsTwo.setText("Poll Question");
                mPollTypeTh.setText("Poll Three");
                mPollQtsTh.setText("Poll Question");
                btnContactOpinion.setVisibility(View.VISIBLE);
                btnViewContact.setVisibility(View.VISIBLE);
                break;
            case 2:
                btnContactOpinion.setVisibility(View.GONE);
                btnViewContact.setVisibility(View.GONE);
                mPollName.setText("Survey Name");
                mEdittextopinionPollName.setHint("Enter Survey Name");
                mPollTypeOne.setText("Survey One");
                mPollQtsOne.setText("Survey Question");
                mPollTypeTwo.setText("Survey Two");
                mPollQtsTwo.setText("Survey Question");
                mPollTypeTh.setText("Survey Three");
                mPollQtsTh.setText("Survey Question");
                break;
        }
    }

    private void restrictUIOnPollUpdate() {
        mEdittextopinionPollName.setText("" + preferences.getString(POLL_NAME, ""));
        mEdittextopinionPollName.setEnabled(false);
        mRbNumQtsOne.setEnabled(false);
        mRbNumQtsTwo.setEnabled(false);
        mRbNumQtsThree.setEnabled(false);
        txtCategory.setEnabled(false);
        mCbQtsOne.setEnabled(false);
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
            case R.id.btnReset_opinion:
                resetValues();
                editor.putString(CONTACT_ARRAY, "").commit();
                break;
            case R.id.btnSubmit_opinion:
                if (mMyPoll == 0)
                    validateSubmitValues();
                else if (mMyPoll == 1)
                    try {
                        if (mContactJsonArray.length() == 0) {
                            makeToast("You have to add atleast one contact");
                            return;
                        }
                        processOpinionPollCreation(RestApiProcessor.HttpMethod.PUT, opinionContactsArray().toString(), BASE_URL + CREATE_POLL_URL + "/" + preferences.getInt(POLL_ID, 0), "Updated successfully");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                break;
            case R.id.txtCategory:
                Methodutils.showCategoryDialog(act, txtCategory, Methodutils.setCategoryName());
                break;
            case R.id.btnOpinionContact:
                showContactDialog(mContactJsonArray, mContactArrayNames);
//                if (mContactJsonArray.length() != 0)
//                    btnViewContact.setVisibility(View.VISIBLE);
//                else
//                    btnViewContact.setVisibility(View.GONE);
                Log.e("Contact Array", "" + mContactJsonArray.toString());
//                ((HomeActivity) act).setNewFragment(new ContactGrid(), "Contacts", true);
                break;
        }
    }

    private void showNamesDialog(JSONArray mContactArrayNames) {
        Dialog mContactDialog = new Dialog(act, R.style.dialog);
        mContactDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContactDialog.setCancelable(true);
        mContactDialog.setContentView(R.layout.dialog_names);
        ListView textView = (ListView) mContactDialog.findViewById(R.id.list_contact_names);
        SpinnerAdapter mSpinnerAdapter;
        List<String> mNamesList = new ArrayList<>();
        mNamesList.clear();
        try {
            for (int i = 0; i < mContactArrayNames.length(); i++) {
                mNamesList.add("" + mContactArrayNames.get(i));
            }
            mSpinnerAdapter = new SpinnerAdapter(act, R.layout.item_spinner, mNamesList);
            textView.setAdapter(mSpinnerAdapter);
        } catch (Exception e) {

        }

        mContactDialog.show();
    }

    private void resetValues() {
        mEdittextopinionPollName.setText("");
        txtCategory.setText("Select Category");
        opinion_ques_one.setText("");
        opinion_ques_two.setText("");
        opinion_ques_three.setText("");
        mTxtQtsOneOne.setText("");
        mTxtQtsOneTwo.setText("");
        mTxtQtsOneThree.setText("");
        mTxtQtsOneFour.setText("");
        mTxtQtsOneFive.setText("");
        mTxtQtsTwoOne.setText("");
        mTxtQtsTwoTwom.setText("");
        mTxtQtsTwoThree.setText("");
        mTxtQtsTwoFour.setText("");
        mTxtQtsTwoFive.setText("");
        mTxtQtsThOne.setText("");
        mTxtQtsThTwo.setText("");
        mTxtQtsThThree.setText("");
        mTxtQtsThFour.setText("");
        mTxtQtsThFive.setText("");
    }

    private void validateQuestionChoicesOne() {
        int numOfQtsChoiceOne = preferences.getInt(OPNINON_RB_ONE, 1);
//        int numOfQtsChoiceTwo = preferences.getInt(OPNINON_RB_TWO, 1);
//        int numOfQtsChoiceTh = preferences.getInt(OPNINON_RB_THREE, 1);
        if (numOfQtsChoiceOne == 1) {
            if (mTxtQtsOneOne.getText().toString().equals("")) {
                makeToast("Question one Choice 1 should not be empty");
                return;
            } else if (mTxtQtsOneTwo.getText().toString().equals("")) {
                makeToast("Question one Choice 2 should not be empty");
                return;
            } else {
                mChoicesArrayOne = new JSONArray();
                mChoicesArrayOne.put(mTxtQtsOneOne.getText().toString());
                mChoicesArrayOne.put(mTxtQtsOneTwo.getText().toString());
                mChoicesArrayOne.put("No opinion");
                Log.e("Choice Array", "" + mChoicesArrayOne);
            }
        } else if (numOfQtsChoiceOne == 2) {
            if (mTxtQtsOneOne.getText().toString().equals("")) {
                makeToast("Question one Choice 1 should not be empty");
                return;
            } else if (mTxtQtsOneTwo.getText().toString().equals("")) {
                makeToast("Question one Choice 2 should not be empty");
                return;
            } else {
                mChoicesArrayOne = new JSONArray();
                mChoicesArrayOne.put(mTxtQtsOneOne.getText().toString());
                mChoicesArrayOne.put(mTxtQtsOneTwo.getText().toString());
                mChoicesArrayOne.put("No opinion");
                Log.e("Choice Array", "" + mChoicesArrayOne);
            }
        } else if (numOfQtsChoiceOne == 3) {
            if (mTxtQtsOneOne.getText().toString().equals("")) {
                makeToast("Question one Choice 1 should not be empty");
                return;
            } else if (mTxtQtsOneTwo.getText().toString().equals("")) {
                makeToast("Question one Choice 2 should not be empty");
                return;
            } else if (mTxtQtsOneThree.getText().toString().equals("")) {
                makeToast("Question one Choice 3 should not be empty");
                return;
            } else {
                mChoicesArrayOne = new JSONArray();
                mChoicesArrayOne.put(mTxtQtsOneOne.getText().toString());
                mChoicesArrayOne.put(mTxtQtsOneTwo.getText().toString());
                mChoicesArrayOne.put(mTxtQtsOneThree.getText().toString());
                mChoicesArrayOne.put("No opinion");
                Log.e("Choice Array", "" + mChoicesArrayOne);
            }
        } else if (numOfQtsChoiceOne == 4) {
            if (mTxtQtsOneOne.getText().toString().equals("")) {
                makeToast("Question one Choice 1 should not be empty");
                return;
            } else if (mTxtQtsOneTwo.getText().toString().equals("")) {
                makeToast("Question one Choice 2 should not be empty");
                return;
            } else if (mTxtQtsOneThree.getText().toString().equals("")) {
                makeToast("Question one Choice 3 should not be empty");
                return;
            } else if (mTxtQtsOneFour.getText().toString().equals("")) {
                makeToast("Question one Choice 4 should not be empty");
                return;
            } else {
                mChoicesArrayOne = new JSONArray();
                mChoicesArrayOne.put(mTxtQtsOneOne.getText().toString());
                mChoicesArrayOne.put(mTxtQtsOneTwo.getText().toString());
                mChoicesArrayOne.put(mTxtQtsOneThree.getText().toString());
                mChoicesArrayOne.put(mTxtQtsOneFour.getText().toString());
                mChoicesArrayOne.put("No opinion");
                Log.e("Choice Array", "" + mChoicesArrayOne);
            }
        } else if (numOfQtsChoiceOne == 5) {
            if (mTxtQtsOneOne.getText().toString().equals("")) {
                makeToast("Question one Choice 1 should not be empty");
                return;
            } else if (mTxtQtsOneTwo.getText().toString().equals("")) {
                makeToast("Question one Choice 2 should not be empty");
                return;
            } else if (mTxtQtsOneThree.getText().toString().equals("")) {
                makeToast("Question one Choice 3 should not be empty");
                return;
            } else if (mTxtQtsOneFour.getText().toString().equals("")) {
                makeToast("Question one Choice 4 should not be empty");
                return;
            } else if (mTxtQtsOneFive.getText().toString().equals("")) {
                makeToast("Question one Choice 5 should not be empty");
                return;
            } else {
                mChoicesArrayOne = new JSONArray();
                mChoicesArrayOne.put(mTxtQtsOneOne.getText().toString());
                mChoicesArrayOne.put(mTxtQtsOneTwo.getText().toString());
                mChoicesArrayOne.put(mTxtQtsOneThree.getText().toString());
                mChoicesArrayOne.put(mTxtQtsOneFour.getText().toString());
                mChoicesArrayOne.put(mTxtQtsOneFive.getText().toString());
                mChoicesArrayOne.put("No opinion");
                Log.e("Choice Array", "" + mChoicesArrayOne);
            }
        } else {
            Log.e("Qts 1 Ch", "No -" + mChoicesArrayOne);
        }
    }

    private void validateQuestionChoicesTwo() {
        int numOfQtsChoiceTwo = preferences.getInt(OPNINON_RB_TWO, 1);
//        int numOfQtsChoiceTwo = preferences.getInt(OPNINON_RB_TWO, 1);
//        int numOfQtsChoiceTh = preferences.getInt(OPNINON_RB_THREE, 1);
        if (numOfQtsChoiceTwo == 1) {
            if (mTxtQtsTwoOne.getText().toString().equals("")) {
                makeToast("Question Two Choice 1 should not be empty");
                return;
            } else {
                mChoicesArrayTwo = new JSONArray();
                mChoicesArrayTwo.put(mTxtQtsOneOne.getText().toString());
                mChoicesArrayTwo.put("No opinion");
                Log.e("Choice Array", "" + mChoicesArrayTwo);
            }
        } else if (numOfQtsChoiceTwo == 2) {
            if (mTxtQtsTwoOne.getText().toString().equals("")) {
                makeToast("Question Two Choice 1 should not be empty");
                return;
            } else if (mTxtQtsTwoTwom.getText().toString().equals("")) {
                makeToast("Question Two Choice 2 should not be empty");
                return;
            } else {
                mChoicesArrayTwo = new JSONArray();
                mChoicesArrayTwo.put(mTxtQtsOneOne.getText().toString());
                mChoicesArrayTwo.put(mTxtQtsOneTwo.getText().toString());
                mChoicesArrayTwo.put("No opinion");
                Log.e("Choice Array", "" + mChoicesArrayTwo);
            }
        } else if (numOfQtsChoiceTwo == 3) {
            if (mTxtQtsTwoOne.getText().toString().equals("")) {
                makeToast("Question Two Choice 1 should not be empty");
                return;
            } else if (mTxtQtsTwoTwom.getText().toString().equals("")) {
                makeToast("Question Two Choice 2 should not be empty");
                return;
            } else if (mTxtQtsTwoThree.getText().toString().equals("")) {
                makeToast("Question Two Choice 3 should not be empty");
                return;
            } else {
                mChoicesArrayTwo = new JSONArray();
                mChoicesArrayTwo.put(mTxtQtsOneOne.getText().toString());
                mChoicesArrayTwo.put(mTxtQtsOneTwo.getText().toString());
                mChoicesArrayTwo.put(mTxtQtsOneThree.getText().toString());
                mChoicesArrayTwo.put("No opinion");
                Log.e("Choice Array", "" + mChoicesArrayTwo);
            }
        } else if (numOfQtsChoiceTwo == 4) {
            if (mTxtQtsTwoOne.getText().toString().equals("")) {
                makeToast("Question Two Choice 1 should not be empty");
                return;
            } else if (mTxtQtsTwoTwom.getText().toString().equals("")) {
                makeToast("Question Two Choice 2 should not be empty");
                return;
            } else if (mTxtQtsTwoThree.getText().toString().equals("")) {
                makeToast("Question Two Choice 3 should not be empty");
                return;
            } else if (mTxtQtsTwoFour.getText().toString().equals("")) {
                makeToast("Question Two Choice 4 should not be empty");
                return;
            } else {
                mChoicesArrayTwo = new JSONArray();
                mChoicesArrayTwo.put(mTxtQtsOneOne.getText().toString());
                mChoicesArrayTwo.put(mTxtQtsOneTwo.getText().toString());
                mChoicesArrayTwo.put(mTxtQtsOneThree.getText().toString());
                mChoicesArrayTwo.put(mTxtQtsOneFour.getText().toString());
                mChoicesArrayTwo.put("No opinion");
                Log.e("Choice Array", "" + mChoicesArrayTwo);
            }
        } else if (numOfQtsChoiceTwo == 5) {
            if (mTxtQtsTwoOne.getText().toString().equals("")) {
                makeToast("Question Two Choice 1 should not be empty");
                return;
            } else if (mTxtQtsTwoTwom.getText().toString().equals("")) {
                makeToast("Question Two Choice 2 should not be empty");
                return;
            } else if (mTxtQtsTwoThree.getText().toString().equals("")) {
                makeToast("Question Two Choice 3 should not be empty");
                return;
            } else if (mTxtQtsTwoFour.getText().toString().equals("")) {
                makeToast("Question Two Choice 4 should not be empty");
                return;
            } else if (mTxtQtsTwoFive.getText().toString().equals("")) {
                makeToast("Question Two Choice 5 should not be empty");
                return;
            } else {
                mChoicesArrayTwo = new JSONArray();
                mChoicesArrayTwo.put(mTxtQtsOneOne.getText().toString());
                mChoicesArrayTwo.put(mTxtQtsOneTwo.getText().toString());
                mChoicesArrayTwo.put(mTxtQtsOneThree.getText().toString());
                mChoicesArrayTwo.put(mTxtQtsOneFour.getText().toString());
                mChoicesArrayTwo.put(mTxtQtsOneFive.getText().toString());
                mChoicesArrayTwo.put("No opinion");
                Log.e("Choice Array", "" + mChoicesArrayTwo);
            }
        } else {
            Log.e("Qts 1 Ch", "No - " + mChoicesArrayTwo);
        }
    }

    private void validateQuestionChoicesThree() {
        int numOfQtsChoiceThree = preferences.getInt(OPNINON_RB_THREE, 1);
//        int numOfQtsChoiceTwo = preferences.getInt(OPNINON_RB_TWO, 1);
//        int numOfQtsChoiceTh = preferences.getInt(OPNINON_RB_THREE, 1);
        if (numOfQtsChoiceThree == 1) {
            if (mTxtQtsThOne.getText().toString().equals("")) {
                makeToast("Question Three Choice 1 should not be empty");
                return;
            } else {
                mChoicesArrayThree = new JSONArray();
                mChoicesArrayThree.put(mTxtQtsOneOne.getText().toString());
                mChoicesArrayThree.put("No opinion");
                Log.e("Choice Array", "" + mChoicesArrayThree);
            }
        } else if (numOfQtsChoiceThree == 2) {
            if (mTxtQtsThOne.getText().toString().equals("")) {
                makeToast("Question Three Choice 1 should not be empty");
                return;
            } else if (mTxtQtsThTwo.getText().toString().equals("")) {
                makeToast("Question Three Choice 2 should not be empty");
                return;
            } else {
                mChoicesArrayThree = new JSONArray();
                mChoicesArrayThree.put(mTxtQtsOneOne.getText().toString());
                mChoicesArrayThree.put(mTxtQtsOneTwo.getText().toString());
                mChoicesArrayThree.put("No opinion");
                Log.e("Choice Array", "" + mChoicesArrayThree);
            }
        } else if (numOfQtsChoiceThree == 3) {
            if (mTxtQtsThOne.getText().toString().equals("")) {
                makeToast("Question Three Choice 1 should not be empty");
                return;
            } else if (mTxtQtsThTwo.getText().toString().equals("")) {
                makeToast("Question Three Choice 2 should not be empty");
                return;
            } else if (mTxtQtsThThree.getText().toString().equals("")) {
                makeToast("Question Three Choice 3 should not be empty");
                return;
            } else {
                mChoicesArrayThree = new JSONArray();
                mChoicesArrayThree.put(mTxtQtsOneOne.getText().toString());
                mChoicesArrayThree.put(mTxtQtsOneTwo.getText().toString());
                mChoicesArrayThree.put(mTxtQtsOneThree.getText().toString());
                mChoicesArrayThree.put("No opinion");
                Log.e("Choice Array", "" + mChoicesArrayThree);
            }
        } else if (numOfQtsChoiceThree == 4) {
            if (mTxtQtsThOne.getText().toString().equals("")) {
                makeToast("Question Three Choice 1 should not be empty");
                return;
            } else if (mTxtQtsThTwo.getText().toString().equals("")) {
                makeToast("Question Three Choice 2 should not be empty");
                return;
            } else if (mTxtQtsThThree.getText().toString().equals("")) {
                makeToast("Question Three Choice 3 should not be empty");
                return;
            } else if (mTxtQtsThFour.getText().toString().equals("")) {
                makeToast("Question Three Choice 4 should not be empty");
                return;
            } else {
                mChoicesArrayThree = new JSONArray();
                mChoicesArrayThree.put(mTxtQtsOneOne.getText().toString());
                mChoicesArrayThree.put(mTxtQtsOneTwo.getText().toString());
                mChoicesArrayThree.put(mTxtQtsOneThree.getText().toString());
                mChoicesArrayThree.put(mTxtQtsOneFour.getText().toString());
                mChoicesArrayThree.put("No opinion");
                Log.e("Choice Array", "" + mChoicesArrayThree);
            }
        } else if (numOfQtsChoiceThree == 5) {
            if (mTxtQtsThOne.getText().toString().equals("")) {
                makeToast("Question Three Choice 1 should not be empty");
                return;
            } else if (mTxtQtsThTwo.getText().toString().equals("")) {
                makeToast("Question Three Choice 2 should not be empty");
                return;
            } else if (mTxtQtsThThree.getText().toString().equals("")) {
                makeToast("Question Three Choice 3 should not be empty");
                return;
            } else if (mTxtQtsThFour.getText().toString().equals("")) {
                makeToast("Question Three Choice 4 should not be empty");
                return;
            } else if (mTxtQtsThFive.getText().toString().equals("")) {
                makeToast("Question Three Choice 5 should not be empty");
                return;
            } else {
                mChoicesArrayThree = new JSONArray();
                mChoicesArrayThree.put(mTxtQtsOneOne.getText().toString());
                mChoicesArrayThree.put(mTxtQtsOneTwo.getText().toString());
                mChoicesArrayThree.put(mTxtQtsOneThree.getText().toString());
                mChoicesArrayThree.put(mTxtQtsOneFour.getText().toString());
                mChoicesArrayThree.put(mTxtQtsOneFive.getText().toString());
                mChoicesArrayThree.put("No opinion");
                Log.e("Choice Array", "" + mChoicesArrayThree);
            }
        } else {
            Log.e("Qts 1 Ch", "No - " + mChoicesArrayThree);
        }
    }


    private void validateSubmitValues() {
        int numOfQts = preferences.getInt(OPINION_NUMOFQTS, 1);
        if (mEdittextopinionPollName.getText().toString().equals("")) {
            makeToast("Please enter Poll Name");
            return;
        }

        if (txtCategory.getText().toString().equals("Select Category")) {
            makeToast("Please choose category");
            return;
        }

        if (numOfQts == 1) {
            if (opinion_ques_one.getText().toString().equals("")) {
                makeToast("Question 1 should not be empty!");
                return;
            } else {
                validateQuestionChoicesOne();
                mQtsOne = new JSONArray();
                mQtsObjectOne = new JSONObject();
                try {
                    mQtsObjectOne.put("question", "" + opinion_ques_one.getText().toString());
                    mQtsObjectOne.put("questionType", "text");
                    mQtsObjectOne.put("choices", mChoicesArrayOne);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mQtsOne.put(mQtsObjectOne);
                Log.e("No", "No.1 - " + mQtsOne);
            }
        }
        if (numOfQts == 2) {
            if (opinion_ques_one.getText().toString().equals("")) {
                makeToast("Question 1 should not be empty!");
                return;
            } else if (opinion_ques_two.getText().toString().equals("")) {
                makeToast("Question 2 should not be empty!");
                return;
            } else {
                validateQuestionChoicesOne();
                validateQuestionChoicesTwo();
                mQtsOne = new JSONArray();
                mQtsObjectOne = new JSONObject();
                mQtsObjectTwo = new JSONObject();
                try {
                    mQtsObjectOne.put("question", "" + opinion_ques_one.getText().toString());
                    mQtsObjectOne.put("questionType", "text");
                    mQtsObjectOne.put("choices", mChoicesArrayOne);
                    mQtsObjectTwo.put("question", "" + opinion_ques_two.getText().toString());
                    mQtsObjectTwo.put("questionType", "text");
                    mQtsObjectTwo.put("choices", mChoicesArrayTwo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mQtsOne.put(mQtsObjectOne);
                mQtsOne.put(mQtsObjectTwo);
                Log.e("No", "No.2 - " + mQtsOne);
            }
        }
        if (numOfQts == 3) {
            if (opinion_ques_one.getText().toString().equals("")) {
                makeToast("Question 1 should not be empty!");
                return;
            } else if (opinion_ques_two.getText().toString().equals("")) {
                makeToast("Question 2 should not be empty!");
                return;
            } else if (opinion_ques_three.getText().toString().equals("")) {
                makeToast("Question 3 should not be empty!");
                return;
            } else {
                validateQuestionChoicesOne();
                validateQuestionChoicesTwo();
                validateQuestionChoicesThree();
                mQtsOne = new JSONArray();
                mQtsObjectOne = new JSONObject();
                mQtsObjectTwo = new JSONObject();
                mQtsObjectThree = new JSONObject();
                try {
                    mQtsObjectOne.put("question", "" + opinion_ques_one.getText().toString());
                    mQtsObjectOne.put("questionType", "text");
                    mQtsObjectOne.put("choices", mChoicesArrayOne);
                    mQtsObjectTwo.put("question", "" + opinion_ques_two.getText().toString());
                    mQtsObjectTwo.put("questionType", "text");
                    mQtsObjectTwo.put("choices", mChoicesArrayTwo);
                    mQtsObjectThree.put("question", "" + opinion_ques_three.getText().toString());
                    mQtsObjectThree.put("questionType", "text");
                    mQtsObjectThree.put("choices", mChoicesArrayThree);
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
                processOpinionPollCreation(RestApiProcessor.HttpMethod.POST, opinionParameter().toString(), BASE_URL + CREATE_POLL_URL, "Opinion Poll Created successfully");
            else
                processOpinionPollCreation(RestApiProcessor.HttpMethod.POST, opinionParameter().toString(), BASE_URL + CREATE_SURVEY_URL, "Opinion Survey Created successfully");
//            else if (mPollType == 1 && mMyPoll == 1) {
//                processOpinionPollCreation(RestApiProcessor.HttpMethod.PUT, opinionContactsArray().toString(), BASE_URL + CREATE_POLL_URL + preferences.getString(POLL_ID, ""), "Updated successfully");
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String opinionContactsArray() {
        JSONObject userContacts = new JSONObject();
        try {
            userContacts.put(POLL_AUDIENCE, mContactJsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userContacts.toString();

    }

    public String opinionParameter() {
        JSONObject userdetails = new JSONObject();
        try {
            userdetails.put(POLL_NAME, "" + mEdittextopinionPollName.getText().toString());
            userdetails.put(POLL_ISBOOST, "0");
            userdetails.put(POLL_VISIBILITY_TYPE, "visible");
            userdetails.put(POLL_REWARD_TYPE, "free");
            userdetails.put(POLL_CATEGORY, "" + txtCategory.getText().toString());
            userdetails.put(POLL_CREATE_USERID, "" + preferences.getString(USER_ID, ""));
            userdetails.put(POLL_TYPE, OPINION_POLL);
            userdetails.put(POLL_QUESTION_LIST, mQtsOne);
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

    private void processOpinionPollCreation(RestApiProcessor.HttpMethod httpMethod, String params, String url, final String reqMessage) throws Exception {
        RestApiProcessor processor = new RestApiProcessor(act, httpMethod, url, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                String message;
                Methodutils.messageWithTitle(act, "Success", "" + reqMessage, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        act.getSupportFragmentManager().popBackStack();
                    }
                });
            }

            @Override
            public void onRequestFailed(Exception e) {
                Methodutils.messageWithTitle(act, "Error", "Error from server while creating opinion", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });
        processor.execute(params);
    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (mRbQtsOneChoiceOne.isChecked()) {
            editor.putInt(OPNINON_RB_ONE, 1).commit();
            mLayoutQtsOneOne.setVisibility(View.VISIBLE);
            if (mLayoutQtsOneTwo.getVisibility() != View.VISIBLE)
                mLayoutQtsOneTwo.setVisibility(View.VISIBLE);
            mLayoutQtsOneThree.setVisibility(View.GONE);
            mLayoutQtsOneFour.setVisibility(View.GONE);
            mLayoutQtsOneFive.setVisibility(View.GONE);
        }
        if (mRbQtsOneChoiceTwo.isChecked()) {
            editor.putInt(OPNINON_RB_ONE, 2).commit();
            txtNoOpinionOne.setText("3");
            mLayoutQtsOneOne.setVisibility(View.VISIBLE);
            mLayoutQtsOneTwo.setVisibility(View.VISIBLE);
            mLayoutQtsOneThree.setVisibility(View.GONE);
            mLayoutQtsOneFour.setVisibility(View.GONE);
            mLayoutQtsOneFive.setVisibility(View.GONE);
        }
        if (mRbQtsOneChoiceThree.isChecked()) {
            txtNoOpinionOne.setText("4");
            editor.putInt(OPNINON_RB_ONE, 3).commit();
            mLayoutQtsOneOne.setVisibility(View.VISIBLE);
            mLayoutQtsOneTwo.setVisibility(View.VISIBLE);
            mLayoutQtsOneThree.setVisibility(View.VISIBLE);
            mLayoutQtsOneFour.setVisibility(View.GONE);
            mLayoutQtsOneFive.setVisibility(View.GONE);
        }

        if (mRbQtsOneChoiceFour.isChecked()) {
            txtNoOpinionOne.setText("5");
            editor.putInt(OPNINON_RB_ONE, 4).commit();
            mLayoutQtsOneOne.setVisibility(View.VISIBLE);
            mLayoutQtsOneTwo.setVisibility(View.VISIBLE);
            mLayoutQtsOneThree.setVisibility(View.VISIBLE);
            mLayoutQtsOneFour.setVisibility(View.VISIBLE);
            mLayoutQtsOneFive.setVisibility(View.GONE);
        }
        if (mRbQtsOneChoiceFive.isChecked()) {
            txtNoOpinionOne.setText("6");
            editor.putInt(OPNINON_RB_ONE, 5).commit();
            mLayoutQtsOneOne.setVisibility(View.VISIBLE);
            mLayoutQtsOneTwo.setVisibility(View.VISIBLE);
            mLayoutQtsOneThree.setVisibility(View.VISIBLE);
            mLayoutQtsOneFour.setVisibility(View.VISIBLE);
            mLayoutQtsOneFive.setVisibility(View.VISIBLE);
        }

        if (mRbQtsTwoChoiceOne.isChecked()) {
            editor.putInt(OPNINON_RB_TWO, 1).commit();
            mLayoutQtsTwoOne.setVisibility(View.VISIBLE);
            if (mLayoutQtsTwoTwo.getVisibility() != View.VISIBLE)
                mLayoutQtsTwoTwo.setVisibility(View.VISIBLE);
            mLayoutQtsTwoThree.setVisibility(View.GONE);
            mLayoutQtsTwoFour.setVisibility(View.GONE);
            mLayoutQtsTwoFive.setVisibility(View.GONE);
        }
        if (mRbQtsTwoChoiceTwo.isChecked()) {
            txtNoOpinionTwo.setText("3");
            editor.putInt(OPNINON_RB_TWO, 2).commit();
            mLayoutQtsTwoOne.setVisibility(View.VISIBLE);
            mLayoutQtsTwoTwo.setVisibility(View.VISIBLE);
            mLayoutQtsTwoThree.setVisibility(View.GONE);
            mLayoutQtsTwoFour.setVisibility(View.GONE);
            mLayoutQtsTwoFive.setVisibility(View.GONE);
        }
        if (mRbQtsTwoChoiceThree.isChecked()) {
            txtNoOpinionTwo.setText("4");
            editor.putInt(OPNINON_RB_TWO, 3).commit();
            mLayoutQtsTwoOne.setVisibility(View.VISIBLE);
            mLayoutQtsTwoTwo.setVisibility(View.VISIBLE);
            mLayoutQtsTwoThree.setVisibility(View.VISIBLE);
            mLayoutQtsTwoFour.setVisibility(View.GONE);
            mLayoutQtsTwoFive.setVisibility(View.GONE);
        }

        if (mRbQtsTwoChoiceFour.isChecked()) {
            txtNoOpinionTwo.setText("5");
            editor.putInt(OPNINON_RB_TWO, 4).commit();
            mLayoutQtsTwoOne.setVisibility(View.VISIBLE);
            mLayoutQtsTwoTwo.setVisibility(View.VISIBLE);
            mLayoutQtsTwoThree.setVisibility(View.VISIBLE);
            mLayoutQtsTwoFour.setVisibility(View.VISIBLE);
            mLayoutQtsTwoFive.setVisibility(View.GONE);
        }
        if (mRbQtsTwoChoiceFive.isChecked()) {
            txtNoOpinionTwo.setText("6");
            editor.putInt(OPNINON_RB_TWO, 5).commit();
            mLayoutQtsTwoOne.setVisibility(View.VISIBLE);
            mLayoutQtsTwoTwo.setVisibility(View.VISIBLE);
            mLayoutQtsTwoThree.setVisibility(View.VISIBLE);
            mLayoutQtsTwoFour.setVisibility(View.VISIBLE);
            mLayoutQtsTwoFive.setVisibility(View.VISIBLE);
        }

        if (mRbQtsThChoiceOne.isChecked()) {
            editor.putInt(OPNINON_RB_THREE, 1).commit();
            mLayoutQtsThOne.setVisibility(View.VISIBLE);
            if (mLayoutQtsThTwo.getVisibility() != View.VISIBLE)
                mLayoutQtsThTwo.setVisibility(View.VISIBLE);
            mLayoutQtsThThree.setVisibility(View.GONE);
            mLayoutQtsThFour.setVisibility(View.GONE);
            mLayoutQtsThFive.setVisibility(View.GONE);
        }
        if (mRbQtsThChoiceTwo.isChecked()) {
            txtNoOpinionThree.setText("3");
            editor.putInt(OPNINON_RB_THREE, 2).commit();
            mLayoutQtsThOne.setVisibility(View.VISIBLE);
            mLayoutQtsThTwo.setVisibility(View.VISIBLE);
            mLayoutQtsThThree.setVisibility(View.GONE);
            mLayoutQtsThFour.setVisibility(View.GONE);
            mLayoutQtsThFive.setVisibility(View.GONE);
        }
        if (mRbQtsThChoiceThree.isChecked()) {
            txtNoOpinionThree.setText("4");
            editor.putInt(OPNINON_RB_THREE, 3).commit();
            mLayoutQtsThOne.setVisibility(View.VISIBLE);
            mLayoutQtsThTwo.setVisibility(View.VISIBLE);
            mLayoutQtsThThree.setVisibility(View.VISIBLE);
            mLayoutQtsThFour.setVisibility(View.GONE);
            mLayoutQtsThFive.setVisibility(View.GONE);
        }

        if (mRbQtsThChoiceFour.isChecked()) {
            txtNoOpinionThree.setText("5");
            editor.putInt(OPNINON_RB_THREE, 4).commit();
            mLayoutQtsThOne.setVisibility(View.VISIBLE);
            mLayoutQtsThTwo.setVisibility(View.VISIBLE);
            mLayoutQtsThThree.setVisibility(View.VISIBLE);
            mLayoutQtsThFour.setVisibility(View.VISIBLE);
            mLayoutQtsThFive.setVisibility(View.GONE);
        }
        if (mRbQtsThChoiceFive.isChecked()) {
            txtNoOpinionThree.setText("6");
            editor.putInt(OPNINON_RB_THREE, 5).commit();
            mLayoutQtsThOne.setVisibility(View.VISIBLE);
            mLayoutQtsThTwo.setVisibility(View.VISIBLE);
            mLayoutQtsThThree.setVisibility(View.VISIBLE);
            mLayoutQtsThFour.setVisibility(View.VISIBLE);
            mLayoutQtsThFive.setVisibility(View.VISIBLE);
        }

        if (mRbNumQtsOne.isChecked()) {
            editor.putInt(OPINION_NUMOFQTS, 1).commit();
            mLayoutQtsOne.setVisibility(View.VISIBLE);
            mLayoutQtsTwo.setVisibility(View.GONE);
            mLayoutQtsThree.setVisibility(View.GONE);
        }
        if (mRbNumQtsTwo.isChecked()) {
            editor.putInt(OPINION_NUMOFQTS, 2).commit();
            if (mLayoutQtsOne.getVisibility() != View.VISIBLE)
                mLayoutQtsOne.setVisibility(View.VISIBLE);
            mLayoutQtsTwo.setVisibility(View.VISIBLE);
            mLayoutQtsThree.setVisibility(View.GONE);
        }
        if (mRbNumQtsThree.isChecked()) {
            editor.putInt(OPINION_NUMOFQTS, 3).commit();
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