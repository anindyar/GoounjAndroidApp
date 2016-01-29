package com.orgware.polling.fragments.menu;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.orgware.polling.MainHomeActivity;
import com.orgware.polling.MenuDetailActivity;
import com.orgware.polling.R;
import com.orgware.polling.fragments.BaseFragment;
import com.orgware.polling.interfaces.RestApiListener;
import com.orgware.polling.network.RestApiProcessor;
import com.orgware.polling.pojo.TimeLineItem;
import com.orgware.polling.utils.Methodutils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by nandagopal on 21/1/16.
 */
public class ChangeNumberDetail extends BaseFragment implements View.OnClickListener {


    private EditText mOldNumberTxt, mNewNumberTxt, mDialogOTPTxt;
    private Button mChangeNumberDone, mOTPDone;
    private Dialog mOTPDialog;

    @Override
    public void setTitle() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_change_number, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOldNumberTxt = (EditText) view.findViewById(R.id.change_number_old);
        mNewNumberTxt = (EditText) view.findViewById(R.id.change_number_new);
        (mChangeNumberDone = (Button) view.findViewById(R.id.change_number_done)).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MenuDetailActivity) act).setTitle("Change Number");
    }

    private String showPollParams() {
        JSONObject mShowPollOnject = new JSONObject();
        try {
//            mShowPollOnject.put(USER_ID, "" + preferences.getString(USER_ID, ""));
            mShowPollOnject.put("oldNumber", mOldNumberTxt.getText().toString());
            mShowPollOnject.put("newNumber", mNewNumberTxt.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mShowPollOnject.toString();
    }

    private void getChangeNumberPage(String url) throws Exception {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.PUT, url, true, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
            }

            @Override
            public void onRequestFailed(Exception e) {
                Methodutils.messageWithTitle(act, "Failed", "" + e.getMessage(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        act.finish();
                    }
                });
            }
        });
        processor.execute(showPollParams().toString());
    }

    private void showOtpDialog() {
        mOTPDialog = new Dialog(act, R.style.dialog);
        mOTPDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mOTPDialog.setCancelable(false);
        mDialogOTPTxt = (EditText) mOTPDialog.findViewById(R.id.txt_otp_change_number);
        mOTPDone = (Button) mOTPDialog.findViewById(R.id.btn_change_number_otp_submit);
        mOTPDone.setOnClickListener(this);

    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.change_number_done) {

            if (mOldNumberTxt.getText().toString().equals("")) {
                makeToast("Old number field should not be empty");
                return;
            }

            if (mOldNumberTxt.getText().toString().length() < 10) {
                makeToast("Please enter valid mobile number");
                return;
            }
            if (mOldNumberTxt.getText().toString().length() > 10) {
                makeToast("Please enter valid mobile number");
                return;
            }

            if (mNewNumberTxt.getText().toString().equals("")) {
                makeToast("New number field should not be empty");
                return;
            }
            if (mNewNumberTxt.getText().toString().length() < 10) {
                makeToast("Please enter valid mobile number");
                return;
            }
            if (mNewNumberTxt.getText().toString().length() > 10) {
                makeToast("Please enter valid mobile number");
                return;
            }

            try {
                getChangeNumberPage(BASE_URL + CHANGE_NUMBER + preferences.getString(USER_ID, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        if (v.getId() == R.id.btn_change_number_otp_submit) {
            if (mDialogOTPTxt.getText().toString().equals("")) {
                makeToast("OTP field should not be empty");
            }


        }
    }
}
