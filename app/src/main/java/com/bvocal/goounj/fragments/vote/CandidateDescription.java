package com.bvocal.goounj.fragments.vote;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bvocal.goounj.R;
import com.bvocal.goounj.activities.vote.CurrentVoteActivity;
import com.bvocal.goounj.fragments.BaseFragment;
import com.bvocal.goounj.interfaces.RestApiListener;
import com.bvocal.goounj.network.RestApiProcessor;
import com.bvocal.goounj.utils.Methodutils;

import org.json.JSONObject;

/**
 * Created by nandagopal on 19/1/16.
 */
public class CandidateDescription extends BaseFragment implements View.OnClickListener {

    private TextView mCandidateDescription, mNoOfCandidate;
    private Button mVote;
    private String mDesc, mCanName, mCanCount;
    private int mCanId, id;
    private Dialog mConfirmDialog, mOTPDialog, mThanksDialog;
    private Button mCancel, mConfirm, mOtpCancel, mOtpSubmit, mBtnOtpOk, mBtnThanksOk;
    private EditText mTxtVerifyOTP;
    private TextView mCandidateName;

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mDesc = getArguments().getString("can_desc");
            id = getArguments().getInt("electionID");
            mCanName = getArguments().getString("can_name");
            mCanId = getArguments().getInt("can_position");
            mCanCount = getArguments().getString("candidate_count");
//            makeToast("" + getArguments().getString("can_desc"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_candidate_description, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCandidateDescription = (TextView) view.findViewById(R.id.candidate_detail_description);
        mNoOfCandidate = (TextView) view.findViewById(R.id.vote_candidate);
        mNoOfCandidate.setText("" + mCanCount);
        mCandidateDescription.setText("" + mDesc);
        (mVote = (Button) view.findViewById(R.id.btnVote)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_confirm:
                mConfirmDialog.dismiss();
                try {
                    pushRequestForOtp(BASE_URL + VOTE_REQ_OTP, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_confirm_cancel:
                mConfirmDialog.dismiss();
                break;
            case R.id.btnSelfNomination:
                startActivity(new Intent(act, CurrentVoteActivity.class).putExtra("electionId", id));
                break;
            case R.id.btn_otp_cancel:
                mOTPDialog.dismiss();
                break;
            case R.id.btn_otp_submit:
                if (TextUtils.isEmpty(mTxtVerifyOTP.getText())) {
                    makeToast("Otp should not be empty!");
                    return;
                }
                try {
                    verifyOtp(BASE_URL + VOTE_VERIFY_OTP, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mOTPDialog.dismiss();
                break;
            case R.id.btn_thanks_ok:
                mThanksDialog.dismiss();
                act.finish();
                break;
            case R.id.btnVote:
                try {
                    showConfirmDialog(mCanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void showConfirmDialog(String name) throws Exception {
        mConfirmDialog = new Dialog(act, R.style.dialog);
        mConfirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mConfirmDialog.setCancelable(true);
        mConfirmDialog.setContentView(R.layout.dialog_vote_confirmation);
        mCandidateName = (TextView) mConfirmDialog.findViewById(R.id.dialog_candidate_name);
        mCandidateName.setText("" + name);
        mConfirm = (Button) mConfirmDialog.findViewById(R.id.btn_confirm_confirm);
        mCancel = (Button) mConfirmDialog.findViewById(R.id.btn_confirm_cancel);
        mConfirm.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mConfirmDialog.show();
    }

    private void showOTPDialog() throws Exception {
        mOTPDialog = new Dialog(act, R.style.dialog);
        mOTPDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mOTPDialog.setCancelable(false);
        mOTPDialog.setContentView(R.layout.dialog_vote_otp);
        mTxtVerifyOTP = (EditText) mOTPDialog.findViewById(R.id.edittext_otp_verify);
        (mOtpSubmit = (Button) mOTPDialog.findViewById(R.id.btn_otp_submit)).setOnClickListener(this);
        (mOtpCancel = (Button) mOTPDialog.findViewById(R.id.btn_otp_cancel)).setOnClickListener(this);

        mOTPDialog.show();
    }

    private void showThanksDialog() throws Exception {
        mThanksDialog = new Dialog(act, R.style.dialog);
        mThanksDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mThanksDialog.setCancelable(true);
        mThanksDialog.setContentView(R.layout.dialog_thanks);
        (mBtnThanksOk = (Button) mThanksDialog.findViewById(R.id.btn_thanks_ok)).setOnClickListener(this);

        mThanksDialog.show();
    }

    private void pushRequestForOtp(String url, boolean pullDownType) {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.POST, url, pullDownType, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                Log.e("Poll List Response", "" + response.toString());
                if (response.equals("OK")) {
                    try {
                        showOTPDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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
        processor.execute(showPollParams().toString());
    }

    private String showPollParams() {
        JSONObject mShowPollOnject = new JSONObject();
        try {
            mShowPollOnject.put(USER_ID, "" + preferences.getString(USER_ID, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mShowPollOnject.toString();
    }

    private void verifyOtp(String url, boolean pullDownType) {
        JSONObject mShowPollOnject = new JSONObject();
        try {
            mShowPollOnject.put(USER_ID, "" + preferences.getString(USER_ID, ""));
            mShowPollOnject.put("authCode", "" + mTxtVerifyOTP.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.POST, url, pullDownType, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                Log.e("Poll List Response", "" + response.toString());
                if (response.equals("OK")) {
                    try {
                        pushVote(BASE_URL + VOTE_PUSH, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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
        processor.execute(mShowPollOnject.toString());
    }

    private void pushVote(String url, boolean pullDownType) {
        JSONObject mShowPollOnject = new JSONObject();
        try {
            mShowPollOnject.put(USER_ID, "" + preferences.getString(USER_ID, ""));
            mShowPollOnject.put("electionId", "" + id);
            mShowPollOnject.put("candidateId", "" + mCanId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.POST, url, pullDownType, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                Log.e("Poll List Response", "" + response.toString());
                if (response.equals("OK")) {
                    try {
                        showThanksDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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
        processor.execute(mShowPollOnject.toString());
    }

}
