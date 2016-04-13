package com.bvocal.goounj.fragments.vote;

import android.app.Dialog;
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

import com.bvocal.goounj.R;
import com.bvocal.goounj.fragments.BaseFragment;
import com.bvocal.goounj.interfaces.RestApiListener;
import com.bvocal.goounj.network.RestApiProcessor;
import com.bvocal.goounj.utils.Methodutils;

import org.json.JSONObject;

/**
 * Created by Nanda on 02/04/16.
 */
public class SelfNominationForm extends BaseFragment implements View.OnClickListener {

    private EditText mCandidateName, mCandidateNickName, mAbout, mManifesto;
    private Button mRetry, mSubmit, mBtnThanksOk;
    private int id;
    private Dialog mThanksDialog;

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getInt(PAGER_COUNT);
        Log.e("SElf Id", "" + id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layout_self_nomination, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCandidateName = (EditText) view.findViewById(R.id.self_candidate_name);
        mCandidateNickName = (EditText) view.findViewById(R.id.self_nick_name);
        mAbout = (EditText) view.findViewById(R.id.self_about_edittext);
        mManifesto = (EditText) view.findViewById(R.id.self_manifesto_edittext);
        (mRetry = (Button) view.findViewById(R.id.btn_self_reset)).setOnClickListener(this);
        (mSubmit = (Button) view.findViewById(R.id.btn_self_submit)).setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_self_reset:
                mCandidateName.setText("");
                mCandidateNickName.setText("");
                mAbout.setText("");
                mManifesto.setText("");
                break;
            case R.id.btn_self_submit:
                if (TextUtils.isEmpty(mCandidateName.getText())) {
                    makeToast("Candidate Name should not be empty!");
                    return;
                }

                if (TextUtils.isEmpty(mCandidateNickName.getText())) {
                    makeToast("Nick Name should not be empty!");
                    return;
                }

                if (TextUtils.isEmpty(mAbout.getText())) {
                    makeToast("About field should not be empty!");
                    return;
                }

                if (TextUtils.isEmpty(mManifesto.getText())) {
                    makeToast("Manifesto field should not be empty!");
                    return;
                }

                try {
                    pushSelfNomination(BASE_URL + SELF_NOMINATION, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_thanks_ok:
                mThanksDialog.dismiss();
                act.finish();
                break;
        }

    }

    private void showThanksDialog() throws Exception {
        mThanksDialog = new Dialog(act, R.style.dialog);
        mThanksDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mThanksDialog.setCancelable(true);
        mThanksDialog.setContentView(R.layout.dialog_thanks);
        (mBtnThanksOk = (Button) mThanksDialog.findViewById(R.id.btn_thanks_ok)).setOnClickListener(this);

        mThanksDialog.show();
    }

    private void pushSelfNomination(String url, boolean pullDownType) {
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
                } else
                    try {
                        showThanksDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
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
            mShowPollOnject.put("electionId", "" + id);
            mShowPollOnject.put("userName", "" + mCandidateName.getText().toString());
            mShowPollOnject.put("nickName", "" + mCandidateNickName.getText().toString());
            mShowPollOnject.put("about", "" + mAbout.getText().toString());
            mShowPollOnject.put("manifesto", "" + mManifesto.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mShowPollOnject.toString();
    }

}
