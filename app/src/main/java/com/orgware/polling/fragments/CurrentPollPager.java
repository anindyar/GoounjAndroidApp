package com.orgware.polling.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.orgware.polling.MainHomeActivity;
import com.orgware.polling.R;
import com.orgware.polling.adapters.PollPagerAdapter;
import com.orgware.polling.interfaces.RestApiListener;
import com.orgware.polling.network.RestApiProcessor;
import com.orgware.polling.utils.Methodutils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 30/10/15.
 */
public class CurrentPollPager extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    ViewPager mViewPager;
    TextView mBtnPrevious, mBtnNext, mBtnSubmit;
    RadioButton mRadioIndicatorOne, mRadioIndicatorTwo, mRadioIndicatorThree;
    RadioGroup mRadioGroupIndicator;
    PollPagerAdapter mPagerAdapter;
    List<Fragment> mFragmentList;
    int mType;
    int qtsSize;
    JSONObject mSubmitAnsOne = new JSONObject();
    JSONObject mSubmitAnsTwo = new JSONObject();
    JSONObject mSubmitAnsThree = new JSONObject();
    JSONArray mSubmitValuesArray;
    Dialog mSurveyDialog;
    EditText mFirstName, mLastName, mMobileNumber;
    Button mBtnDialogSubmit;
    ProgressDialog mProgressFake;

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressFake = new ProgressDialog(act);
        mProgressFake.setMessage("Loading");
        mProgressFake.setCancelable(false);
        mFragmentList = new ArrayList<>();
        qtsSize = preferences.getInt(QUESTION_SIZE, 0);
        if (qtsSize == 1) {
            mFragmentList.clear();
            mFragmentList.add(setPagerFragment(new CurrentPollDetailOne(), 0));
        } else if (qtsSize == 2) {
            mFragmentList.clear();
            mFragmentList.add(setPagerFragment(new CurrentPollDetailOne(), 0));
            mFragmentList.add(setPagerFragment(new CurrentPollDetailTwo(), 1));
        } else if (qtsSize == 3) {
            mFragmentList.clear();
            mFragmentList.add(setPagerFragment(new CurrentPollDetailOne(), 0));
            mFragmentList.add(setPagerFragment(new CurrentPollDetailTwo(), 1));
            mFragmentList.add(setPagerFragment(new CurrentPollDetailThree(), 3));
        } else
            Log.e("Qts Size", "" + qtsSize);
//        mFragmentList.add(setPagerFragment(new CurrentPollDetailOne(), 2));
        mPagerAdapter = new PollPagerAdapter(getChildFragmentManager(), mFragmentList);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_currentpoll_pager, container, false);
        mViewPager = (ViewPager) v.findViewById(R.id.pagerCurrentPoll);
        mBtnNext = (TextView) v.findViewById(R.id.btn_next);
        mBtnPrevious = (TextView) v.findViewById(R.id.btn_previous);
        mBtnSubmit = (TextView) v.findViewById(R.id.btn_submit);
        mRadioGroupIndicator = (RadioGroup) v.findViewById(R.id.radio_currentpoll_pager);
        mRadioIndicatorOne = (RadioButton) v.findViewById(R.id.radio_currentpoll_fragment_1);
        mRadioIndicatorTwo = (RadioButton) v.findViewById(R.id.radio_currentpoll_fragment_2);
        mRadioIndicatorThree = (RadioButton) v.findViewById(R.id.radio_currentpoll_fragment_3);
        mViewPager.setOnPageChangeListener(this);
        mBtnNext.setOnClickListener(this);
        mBtnPrevious.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
        mRadioIndicatorOne.setOnCheckedChangeListener(this);
        mRadioIndicatorTwo.setOnCheckedChangeListener(this);
        mRadioIndicatorThree.setOnCheckedChangeListener(this);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ((HomeActivity) act).mSearchPollsTxt.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewPager.setAdapter(mPagerAdapter);
//        makeToast("Pager" + mType);
        if (qtsSize == 1) {
            mRadioGroupIndicator.setVisibility(View.INVISIBLE);
            mBtnSubmit.setVisibility(View.VISIBLE);
            mBtnPrevious.setVisibility(View.GONE);
            mBtnNext.setVisibility(View.GONE);
        }
        if (qtsSize == 2) {
            mRadioIndicatorThree.setVisibility(View.GONE);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        final int mPagerPosition = mViewPager.getCurrentItem();
        switch (v.getId()) {
            case R.id.btn_next:
                mViewPager.setCurrentItem(mPagerPosition + 1);
                break;
            case R.id.btn_previous:
                mViewPager.setCurrentItem(mPagerPosition - 1);
                break;
            case R.id.btn_submit:
                submitParams(qtsSize);
                answerPollParams();
//                if (preferences.getInt(DASHBOARD_ID, 0) == 0) {
                try {
                    pushAnwersPoll(BASE_URL + ANSWER_POLL_URL, answerPollParams().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                } else {
//                    showSurveyDialog();
//                }
                break;
            case R.id.btn_survey_submit:
                if (mFirstName.getText().toString().equals("")) {
                    makeToast("Please enter firstname");
                    return;
                }
                if (mLastName.getText().toString().equals("")) {
                    makeToast("Please enter firstname");
                    return;
                }
                if (mMobileNumber.getText().toString().equals("")) {
                    makeToast("Please enter firstname");
                    return;
                }
                editor.putString(FIRSTNAME_SURVEY, mFirstName.getText().toString()).putString(LASTNAME_SURVEY, mLastName.getText().toString())
                        .putString(SURVEY_MOBILE, mMobileNumber.getText().toString()).commit();
                if (preferences.getInt(DASHBOARD_ID, 0) == 1) {
//                    try {
//                        pushAnwersPoll(BASE_URL + SURVEY_POLL_URL, answerSurveyParams().toString());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    mProgressFake.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Methodutils.messageWithTitle(act, "Message", "Survey Poll Updated Successfully.", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mSurveyDialog.dismiss();
                                    mProgressFake.dismiss();
                                    act.getSupportFragmentManager().popBackStack();
                                }
                            });
                        }
                    }, 500);
                } else {
                    Log.e("Test", "Test");
                }
                break;
        }
    }

    private void showSurveyDialog() {
        mSurveyDialog = new Dialog(act, R.style.dialog);
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

    private String answerPollParams() {
        JSONObject mAnswerObject = new JSONObject();
        try {
            mAnswerObject.put("pollId", "" + preferences.getInt(POLL_ID, 0));
            mAnswerObject.put("userId", preferences.getString(USER_ID, ""));
            mAnswerObject.put("questionList", mSubmitValuesArray);
            Log.e("Answer Params", "" + mAnswerObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mAnswerObject.toString();
    }

    private String answerSurveyParams() {
        JSONObject mAnswerObject = new JSONObject();
        try {
            mAnswerObject.put("pollId", preferences.getInt(POLL_ID, 0));
            mAnswerObject.put("fname", preferences.getString(FIRSTNAME_SURVEY, ""));
            mAnswerObject.put("lname", preferences.getString(LASTNAME_SURVEY, ""));
            mAnswerObject.put("phone", preferences.getString(SURVEY_MOBILE, ""));
            mAnswerObject.put("questionList", mSubmitValuesArray);
            Log.e("Answer Params", "" + mAnswerObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mAnswerObject.toString();
    }


    private void pushAnwersPoll(String url, String params) throws Exception {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.POST, url, true, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                Log.e("Answer Response", "" + response);
                try {
                    getResultPollForCreatedUser(BASE_URL + RESULT_URL + preferences.getInt(POLL_ID, 0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                ((HomeActivity) act).setNewFragment(new ResultPoll(), "Submit", true);
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
                    Methodutils.message(act, "Try again. Failed to connect to server.", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            return;
                        }
                    });
            }
        });
        processor.execute(params.toString());
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

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {
        if (qtsSize >= 2)
            enableButtons(qtsSize, position);
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void getResultPollForCreatedUser(String url) {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.GET, url, true, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                if (response.equals("[]"))
                    makeToast("No Records Found");
                else
                    try {
                        showResultPollList(response);
                        ((MainHomeActivity) act).setNewFragment(new ResultPoll(), "", true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onRequestFailed(Exception e) {
                if (e == null) {
                    Log.e("Error", "" + e.getMessage());
                    Methodutils.message(act, "Internal Server Error. Requested Action Failed", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            act.getSupportFragmentManager().popBackStack();
                        }
                    });
                } else {
                    if (e.getMessage() == null)
                        Methodutils.message(act, "No Records Found", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                act.getSupportFragmentManager().popBackStack();
                            }
                        });
                    else
                        Methodutils.message(act, "Try again, Failed to connect to server", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                act.getSupportFragmentManager().popBackStack();
                            }
                        });

                }
            }
        });
        processor.execute();
    }

    public void showResultPollList(String response) {
        try {
            JSONObject mPollDetailObject = new JSONObject(response);
            JSONArray mQuestionsArray = mPollDetailObject.optJSONArray(ANS_QUESTIONLIST);
            editor.putInt(RESULT_QUESTION_SIZE, mQuestionsArray.length()).commit();
            for (int j = 0; j < mQuestionsArray.length(); j++) {
                JSONObject mQuestions = mQuestionsArray.optJSONObject(j);
                JSONArray mChoicesArray = mQuestions.optJSONArray(CHOICES);
                Log.e("Choice Array - " + j, "" + mChoicesArray.toString());
                editor.putString("RES_QUESTION_" + j, "" + mQuestions.optString(QUESTION)).
                        putString("RES_CHOICE_" + j, "" + mChoicesArray.toString()).commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enableButtons(int questionSize, int position) {
        if (questionSize == 2) {
            mRadioGroupIndicator.setVisibility(View.VISIBLE);
            switch (position) {
                case 0:
                    mBtnNext.setVisibility(View.VISIBLE);
                    mBtnSubmit.setVisibility(View.GONE);
                    mBtnPrevious.setVisibility(View.GONE);
                    mRadioIndicatorOne.setChecked(true);
                    break;
                case 1:
                    mBtnNext.setVisibility(View.GONE);
                    mBtnSubmit.setVisibility(View.VISIBLE);
                    mBtnPrevious.setVisibility(View.VISIBLE);
                    mRadioIndicatorTwo.setChecked(true);
                    break;
            }
        } else if (questionSize == 3) {
            mRadioGroupIndicator.setVisibility(View.VISIBLE);
            switch (position) {
                case 0:
                    mBtnNext.setVisibility(View.VISIBLE);
                    mBtnSubmit.setVisibility(View.GONE);
                    mBtnPrevious.setVisibility(View.GONE);
                    mRadioIndicatorOne.setChecked(true);
                    break;
                case 1:
                    mBtnNext.setVisibility(View.VISIBLE);
                    mBtnSubmit.setVisibility(View.GONE);
                    mBtnPrevious.setVisibility(View.VISIBLE);
                    mRadioIndicatorTwo.setChecked(true);
                    break;
                case 2:
                    mBtnNext.setVisibility(View.GONE);
                    mBtnSubmit.setVisibility(View.VISIBLE);
                    mBtnPrevious.setVisibility(View.VISIBLE);
                    mRadioIndicatorThree.setChecked(true);
                    break;
            }
        } else
            Log.e("Enable", "Test");
    }

}
