package com.orgware.polling.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orgware.polling.HomeActivity;
import com.orgware.polling.R;
import com.orgware.polling.adapters.ChoicesListviewAdapter;
import com.orgware.polling.interfaces.RestApiListener;
import com.orgware.polling.network.RestApiProcessor;
import com.orgware.polling.pojo.ChoicesItem;
import com.orgware.polling.utils.Methodutils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 15/11/15.
 */
public class SurveyDetail extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    ListView mChoiceListviewOne, mChoiceListviewTwo, mChoiceListviewThree;
    ChoicesListviewAdapter mAdapterOne, mAdapterTwo, mAdapterThree;
    List<ChoicesItem> itemListOne = new ArrayList<>();
    List<ChoicesItem> itemListTwo = new ArrayList<>();
    List<ChoicesItem> itemListThree = new ArrayList<>();
    int qtsSize, mQtsIdOne, mQtsIdTwo, mQtsIdThree;
    String mQuestionOne, mQuestionTwo, mQuestionThree;
    JSONArray mChoiceArrayOne, mChoiceArrayTwo, mChoiceArrayThree, mSubmitValuesArray;
    LinearLayout mLayoutQtsOne, mLayoutQtsTwo, mLayoutQtsThree, mBtnSubmit;
    TextView mQuestionTitleOne, mQuestionTitleTwo, mQuestionTitleThree, mBtnSub;
    JSONObject mSubmitAnsOne = new JSONObject();
    JSONObject mSubmitAnsTwo = new JSONObject();
    JSONObject mSubmitAnsThree = new JSONObject();
    Dialog mSurveyDialog;
    EditText mFirstName, mLastName, mMobileNumber;
    Button mBtnDialogSubmit;
    RelativeLayout mBackToLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qtsSize = preferences.getInt(QUESTION_SIZE, 0);
        if (qtsSize == 1)
            setQtsOneContent();
        else if (qtsSize == 2)
            setQtsTwoContent();
        else if (qtsSize == 3)
            setQtsThreeContent();
        else
            Log.e("Survey Details", "Others");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_survey_detail, container, false);
        mChoiceListviewOne = (ListView) v.findViewById(R.id.choicesListviewOne);
        mChoiceListviewTwo = (ListView) v.findViewById(R.id.choicesListviewTwo);
        mChoiceListviewThree = (ListView) v.findViewById(R.id.choicesListviewThree);
        mBackToLayout = (RelativeLayout) v.findViewById(R.id.layout_statics_back);
        mBackToLayout.setVisibility(View.GONE);
        (mBtnSubmit = (LinearLayout) v.findViewById(R.id.layout_survey_detail_submit)).setOnClickListener(this);
        (mBtnSub = (TextView) v.findViewById(R.id.btn_submit)).setOnClickListener(this);
        mLayoutQtsOne = (LinearLayout) v.findViewById(R.id.layout_survey_detail_qts_one);
        mLayoutQtsTwo = (LinearLayout) v.findViewById(R.id.layout_survey_detail_qts_two);
        mLayoutQtsThree = (LinearLayout) v.findViewById(R.id.layout_survey_detail_qts_three);
        mQuestionTitleOne = (TextView) v.findViewById(R.id.qts_survey_detail_one);
        mQuestionTitleTwo = (TextView) v.findViewById(R.id.qts_survey_detail_two);
        mQuestionTitleThree = (TextView) v.findViewById(R.id.qts_survey_detail_three);
        mChoiceListviewOne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Qts Survey", itemListOne.get(position).mChoiceOptionId + " - " + itemListOne.get(position).mChoiceName);
                editor.putInt(OPTION_ONE, itemListOne.get(position).mChoiceOptionId).commit();
            }
        });
        mChoiceListviewTwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Qts Survey", itemListTwo.get(position).mChoiceOptionId + " - " + itemListTwo.get(position).mChoiceName);
                editor.putInt(OPTION_TWO, itemListTwo.get(position).mChoiceOptionId).commit();
            }
        });
        mChoiceListviewThree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Qts Survey", itemListThree.get(position).mChoiceOptionId + " - " + itemListThree.get(position).mChoiceName);
                editor.putInt(OPTION_THREE, itemListThree.get(position).mChoiceOptionId).commit();
            }
        });
        return v;
    }

    @Override
    public void setTitle() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (qtsSize == 1) {
            mChoiceListviewOne.setAdapter(mAdapterOne);
            mQuestionTitleOne.setText(mQuestionOne);
            mLayoutQtsTwo.setVisibility(View.GONE);
            mLayoutQtsThree.setVisibility(View.GONE);
            mChoiceListviewOne.setItemChecked(0, true);
        } else if (qtsSize == 2) {
            mChoiceListviewOne.setAdapter(mAdapterOne);
            mChoiceListviewTwo.setAdapter(mAdapterTwo);
            mQuestionTitleOne.setText(mQuestionOne);
            mQuestionTitleTwo.setText(mQuestionTwo);
            mLayoutQtsThree.setVisibility(View.GONE);
            mChoiceListviewOne.setItemChecked(0, true);
            mChoiceListviewTwo.setItemChecked(0, true);
        } else if (qtsSize == 3) {
            mChoiceListviewOne.setAdapter(mAdapterOne);
            mChoiceListviewTwo.setAdapter(mAdapterTwo);
            mChoiceListviewThree.setAdapter(mAdapterThree);
            mQuestionTitleOne.setText(mQuestionOne);
            mQuestionTitleTwo.setText(mQuestionTwo);
            mQuestionTitleThree.setText(mQuestionThree);
            mChoiceListviewOne.setItemChecked(0, true);
            mChoiceListviewTwo.setItemChecked(0, true);
            mChoiceListviewThree.setItemChecked(0, true);
        }

    }

    private void submitParams(int qtsSize) {
        if (qtsSize == 1) {
            try {
                mSubmitValuesArray = new JSONArray();
                mSubmitAnsOne.put("questionId", "" + preferences.getInt(QUESTION_ID_0, 0));
                mSubmitAnsOne.put("optionId", "" + preferences.getInt(OPTION_ONE, 0));
                mSubmitValuesArray.put(mSubmitAnsOne);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (qtsSize == 2) {
            try {
                mSubmitValuesArray = new JSONArray();
                mSubmitAnsOne.put("questionId", "" + preferences.getInt(QUESTION_ID_0, 0));
                mSubmitAnsOne.put("optionId", "" + preferences.getInt(OPTION_ONE, 0));
                mSubmitAnsTwo.put("questionId", "" + preferences.getInt(QUESTION_ID_1, 0));
                mSubmitAnsTwo.put("optionId", "" + preferences.getInt(OPTION_TWO, 0));
                mSubmitValuesArray.put(mSubmitAnsOne);
                mSubmitValuesArray.put(mSubmitAnsTwo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (qtsSize == 3) {
            try {
                mSubmitValuesArray = new JSONArray();
                mSubmitAnsOne.put("questionId", "" + preferences.getInt(QUESTION_ID_0, 0));
                mSubmitAnsOne.put("optionId", "" + preferences.getInt(OPTION_ONE, 0));
                mSubmitAnsTwo.put("questionId", "" + preferences.getInt(QUESTION_ID_1, 0));
                mSubmitAnsTwo.put("optionId", "" + preferences.getInt(OPTION_TWO, 0));
                mSubmitAnsThree.put("questionId", "" + preferences.getInt(QUESTION_ID_2, 0));
                mSubmitAnsThree.put("optionId", "" + preferences.getInt(OPTION_THREE, 0));
                mSubmitValuesArray.put(mSubmitAnsOne);
                mSubmitValuesArray.put(mSubmitAnsTwo);
                mSubmitValuesArray.put(mSubmitAnsThree);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Log.e("Qts Size", "" + qtsSize);
    }

    private void setQtsOneContent() {
        try {
            mChoiceArrayOne = new JSONArray("" + preferences.getString(CHOICE_0, ""));
            mQtsIdOne = preferences.getInt(QUESTION_ID_0, 0);
            mQuestionOne = preferences.getString(QUESTION_SIZE_0, "");
            itemListOne.clear();
            for (int i = 0; i < mChoiceArrayOne.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayOne.optJSONObject(i);
                itemListOne.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("optionId")));
                Log.e("Qts One", "" + itemListOne.get(i).mChoiceName);
            }
            mAdapterOne = new ChoicesListviewAdapter(act, android.R.layout.simple_list_item_single_choice, itemListOne);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setQtsTwoContent() {
        try {
            mChoiceArrayOne = new JSONArray("" + preferences.getString(CHOICE_0, ""));
            mQtsIdOne = preferences.getInt(QUESTION_ID_0, 0);
            mChoiceArrayTwo = new JSONArray("" + preferences.getString(CHOICE_1, ""));
            mQtsIdTwo = preferences.getInt(QUESTION_ID_1, 0);
            mQuestionOne = preferences.getString(QUESTION_SIZE_0, "");
            mQuestionTwo = preferences.getString(QUESTION_SIZE_1, "");
            itemListOne.clear();
            itemListTwo.clear();
            for (int i = 0; i < mChoiceArrayOne.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayOne.optJSONObject(i);
                itemListOne.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("optionId")));
                Log.e("Qts One", "" + itemListOne.get(i).mChoiceName);
            }
            for (int i = 0; i < mChoiceArrayTwo.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayTwo.optJSONObject(i);
                itemListTwo.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("optionId")));
                Log.e("Qts One", "" + itemListTwo.get(i).mChoiceName);
            }
            mAdapterOne = new ChoicesListviewAdapter(act, android.R.layout.simple_list_item_single_choice, itemListOne);
            mAdapterTwo = new ChoicesListviewAdapter(act, android.R.layout.simple_list_item_single_choice, itemListTwo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setQtsThreeContent() {
        try {
            mChoiceArrayOne = new JSONArray("" + preferences.getString(CHOICE_0, ""));
            mQtsIdOne = preferences.getInt(QUESTION_ID_0, 0);
            mChoiceArrayTwo = new JSONArray("" + preferences.getString(CHOICE_1, ""));
            mQtsIdTwo = preferences.getInt(QUESTION_ID_1, 0);
            mChoiceArrayThree = new JSONArray("" + preferences.getString(CHOICE_2, ""));
            mQtsIdThree = preferences.getInt(QUESTION_ID_2, 0);
            mQuestionOne = preferences.getString(QUESTION_SIZE_0, "");
            mQuestionTwo = preferences.getString(QUESTION_SIZE_1, "");
            mQuestionThree = preferences.getString(QUESTION_SIZE_2, "");
            itemListOne.clear();
            itemListTwo.clear();
            itemListThree.clear();
            for (int i = 0; i < mChoiceArrayOne.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayOne.optJSONObject(i);
                itemListOne.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("optionId")));
                Log.e("Qts One", "" + itemListOne.get(i).mChoiceName);
            }
            for (int i = 0; i < mChoiceArrayTwo.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayTwo.optJSONObject(i);
                itemListTwo.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("optionId")));
                Log.e("Qts One", "" + itemListTwo.get(i).mChoiceName);
            }
            for (int i = 0; i < mChoiceArrayThree.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayThree.optJSONObject(i);
                itemListThree.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("optionId")));
                Log.e("Qts One", "" + itemListThree.get(i).mChoiceName);
            }
            mAdapterOne = new ChoicesListviewAdapter(act, android.R.layout.simple_list_item_single_choice, itemListOne);
            mAdapterTwo = new ChoicesListviewAdapter(act, android.R.layout.simple_list_item_single_choice, itemListTwo);
            mAdapterThree = new ChoicesListviewAdapter(act, android.R.layout.simple_list_item_single_choice, itemListThree);
        } catch (Exception e) {
            e.printStackTrace();
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
        switch (view.getId()) {
            case R.id.choicesListviewOne:
                Log.e("Qts Survey", itemListOne.get(position).mChoiceOptionId + " - " + itemListOne.get(position).mChoiceName);
                editor.putInt(OPTION_ONE, itemListOne.get(position).mChoiceOptionId).commit();
                break;
            case R.id.choicesListviewTwo:
                Log.e("Qts Survey", itemListOne.get(position).mChoiceOptionId + " - " + itemListOne.get(position).mChoiceName);
                editor.putInt(OPTION_TWO, itemListTwo.get(position).mChoiceOptionId).commit();
                break;
            case R.id.choicesListviewThree:
                Log.e("Qts Survey", itemListOne.get(position).mChoiceOptionId + " - " + itemListOne.get(position).mChoiceName);
                editor.putInt(OPTION_THREE, itemListThree.get(position).mChoiceOptionId).commit();
                break;
        }
    }

    private void pushAnwersPoll(String url, String params) throws Exception {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.POST, url, true, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                Log.e("Answer Response", "" + response);
//                act.getSupportFragmentManager().popBackStack();
                makeToast("survey answered successfully!");
            }

            @Override
            public void onRequestFailed(Exception e) {
                if (e == null) {
                    Log.e("Error", "" + e.getMessage());
                    Methodutils.message(act, "Internal Server Error. Requested Action Failed", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            return;
                        }
                    });
                } else
                    Methodutils.message(act, "" + e.getMessage(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            return;
                        }
                    });
            }
        });
        processor.execute(params.toString());
    }

    private void showSurveyDialog() {
        mSurveyDialog = new Dialog(act);
        mSurveyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSurveyDialog.setCancelable(true);
        mSurveyDialog.setContentView(R.layout.dialog_survey_dashboard);
        mFirstName = (EditText) mSurveyDialog.findViewById(R.id.txt_survey_fname);
        mLastName = (EditText) mSurveyDialog.findViewById(R.id.txt_survey_lname);
        mMobileNumber = (EditText) mSurveyDialog.findViewById(R.id.txt_survey_mobile);
        mBtnDialogSubmit = (Button) mSurveyDialog.findViewById(R.id.btn_survey_submit);
        mBtnDialogSubmit.setOnClickListener(this);
        mSurveyDialog.show();
    }


    private String answerSurveyParams() {
        JSONObject mAnswerObject = new JSONObject();
        try {
            mAnswerObject.put("pollId", "" + preferences.getInt(POLL_ID, 0));
            mAnswerObject.put("fname", "" + preferences.getString(FIRSTNAME_SURVEY, ""));
            mAnswerObject.put("lname", "" + preferences.getString(LASTNAME_SURVEY, ""));
            mAnswerObject.put("phone", "" + preferences.getString(SURVEY_MOBILE, ""));
            mAnswerObject.put("questionList", mSubmitValuesArray);
            Log.e("Answer Params", "" + mAnswerObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mAnswerObject.toString();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
            case R.id.layout_survey_detail_submit:
                showSurveyDialog();
                break;
            case R.id.btn_survey_submit:
                if (mFirstName.getText().toString().equals("")) {
                    makeToast("Please enter firstname");
                    return;
                }
                if (mLastName.getText().toString().equals("")) {
                    makeToast("Please enter lastname");
                    return;
                }
                if (mMobileNumber.getText().toString().equals("")) {
                    makeToast("Please enter mobile number");
                    return;
                }

                if (mMobileNumber.getText().toString().length() != 10) {
                    makeToast("Mobile number should be valueable!");
                    return;
                }

                editor.putString(FIRSTNAME_SURVEY, mFirstName.getText().toString()).putString(LASTNAME_SURVEY, mLastName.getText().toString())
                        .putString(SURVEY_MOBILE, mMobileNumber.getText().toString()).commit();
                submitParams(qtsSize);
                Log.e("Survey Params", "" + answerSurveyParams().toString());
                try {
                    pushAnwersPoll(BASE_URL + SURVEY_POLL_URL, answerSurveyParams().toString());
                    mSurveyDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
