package com.bvocal.goounj.fragments.vote;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bvocal.goounj.R;
import com.bvocal.goounj.activities.vote.CurrentVoteActivity;
import com.bvocal.goounj.adapters.CandidateListAdapter;
import com.bvocal.goounj.adapters.VoteListAdapter;
import com.bvocal.goounj.fragments.BaseFragment;
import com.bvocal.goounj.interfaces.Appinterface;
import com.bvocal.goounj.interfaces.RestApiListener;
import com.bvocal.goounj.network.RestApiProcessor;
import com.bvocal.goounj.pojo.CandidateItem;
import com.bvocal.goounj.pojo.VoteItem;
import com.bvocal.goounj.utils.Methodutils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 13/1/16.
 */
public class CandidateDetail extends BaseFragment implements Appinterface, View.OnClickListener {

    RecyclerView mCandidateRecyclerView;
    List<CandidateItem> itemList = new ArrayList<>();
    CandidateListAdapter mAdapter;
    Button mBtnSelfNomination;
    int id, mVotePosition;
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
        id = getArguments().getInt(PAGER_COUNT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_candidate_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        (mBtnSelfNomination = (Button) view.findViewById(R.id.btnSelfNomination)).setOnClickListener(this);
        mCandidateRecyclerView = (RecyclerView) view.findViewById(R.id.candidate_list);
        mCandidateRecyclerView.setLayoutManager(new LinearLayoutManager(act));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            getVoteList("http://" + preferences.getString("voting", "") + ":3000/" + CANDIDATE_LIST + id, true);
            refreshCurrentPollListview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_confirm:
                mConfirmDialog.dismiss();
                try {
                    pushRequestForOtp("http://" + preferences.getString("voting", "") + ":3000/" + VOTE_REQ_OTP, true);
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
                    verifyOtp("http://" + preferences.getString("voting", "") + ":3000/" + VOTE_VERIFY_OTP, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mOTPDialog.dismiss();
                break;
            case R.id.btn_thanks_ok:
                mThanksDialog.dismiss();
                act.finish();
                break;

        }
    }

    private void getVoteList(String url, boolean pullDownType) {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.GET, url, pullDownType, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                Log.e("Poll List Response", "" + response.toString());
                if (response.equals("[]"))
                    makeToast("No records found");
                try {
                    showPollList(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestFailed(Exception e) {
                mCandidateRecyclerView.setVisibility(View.GONE);
//                mNoVoteImage.setVisibility(View.GONE);
//                mPageError.setVisibility(View.VISIBLE);
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
        processor.execute();
    }

    public void showPollList(String response) {
        try {
            JSONObject mParentObject = new JSONObject(response);
            JSONArray objectArray = mParentObject.optJSONArray("candidates");
            itemList.clear();
            for (int i = 0; i < objectArray.length(); i++) {
                JSONObject objectPolls = objectArray.optJSONObject(i);
                Log.e("Array Values", "" + i);
                itemList.add(new CandidateItem(objectPolls.optInt("candidateId"), objectPolls.optString("name"),
                        objectPolls.optString("nickName"), objectPolls.optString("about"), objectPolls.optString("manifesto")));
            }

            refreshCurrentPollListview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshCurrentPollListview() {
        if (itemList.size() > 0) {
            mCandidateRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new CandidateListAdapter(act, itemList);
            mCandidateRecyclerView.setAdapter(mAdapter);
        }
        mAdapter.setOnDetailClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int position = (Integer) v.getTag();
                    String description = itemList.get(position).about;
                    Bundle bundle = new Bundle();
                    bundle.putInt("electionID", id);
                    bundle.putInt("can_position", itemList.get(position).candidateId);
                    bundle.putString("can_name", itemList.get(position).name);
                    bundle.putString("can_desc", description);
                    Fragment fragmentDescription = new CandidateDescription();
                    fragmentDescription.setArguments(bundle);

                    setNewFragment(fragmentDescription, R.id.fragment_content_candidate, "Candidate", true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mAdapter.setOnVoteClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int position = (Integer) v.getTag();
                    mVotePosition = itemList.get(position).candidateId;
                    String candidate_name = itemList.get(position).name;

                    showConfirmDialog(candidate_name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
                        pushVote("http://" + preferences.getString("voting", "") + ":3000/" + VOTE_PUSH, true);
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
            mShowPollOnject.put("candidateId", "" + mVotePosition);
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
