package com.orgware.polling;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orgware.polling.interfaces.RestApiListener;
import com.orgware.polling.network.NetworkHelper;
import com.orgware.polling.network.RestApiProcessor;
import com.orgware.polling.utils.Methodutils;

import org.json.JSONObject;

public class OTPActivity extends BaseActivity implements View.OnClickListener {

    Button btnAccept;
    EditText mOTP;
    LinearLayout mLoginFooter;
    BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(GET_INCOMING_SMS_CONTENT);
            editor.putString(OTP_VALUE, "" + msg).commit();
            if (!preferences.getString(OTP_VALUE, "").equals("")) {
                mOTP.setText("" + preferences.getString(OTP_VALUE, ""));
            } else {
                mOTP.setText("");
                Log.e("OTP", "Empty Otp");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        mLoginFooter = (LinearLayout) findViewById(R.id.login_footer);
        (btnAccept = (Button) findViewById(R.id.acceptBtn)).setOnClickListener(this);
        mOTP = (EditText) findViewById(R.id.otp);
        if (!preferences.getString(OTP_VALUE, "").equals("")) {
            mOTP.setText("" + preferences.getString(OTP_VALUE, ""));
        } else {
            mOTP.setText("");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(smsReceiver, new IntentFilter(INCOMING_RECEIVER_SMS));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(smsReceiver, new IntentFilter(INCOMING_RECEIVER_SMS));
    }

    private String OtpParams() {
        JSONObject mOtpParams = new JSONObject();
        try {
            mOtpParams.put("authCode", mOTP.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mOtpParams.toString();
    }

    private void verifyOTPData(String url) throws Exception {

        RestApiProcessor processor = new RestApiProcessor(this, RestApiProcessor.HttpMethod.PUT, url, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                try {
                    Log.e("Otp response", "" + response);
                    startActivity(new Intent(OTPActivity.this, MainHomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestFailed(Exception e) {
                Log.e("Error", "" + e.getMessage());
                if (e == null) {
                    Log.e("Error", "" + e.getMessage());
                    Methodutils.message(OTPActivity.this, "Internal Server Error. Requested Action Failed", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            finish();
                        }
                    });
                } else
                    Methodutils.message(OTPActivity.this, "" + e.getMessage(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            finish();
                        }
                    });
            }
        });
        processor.execute(OtpParams().toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.acceptBtn:
                Log.e("OTP Url", "" + BASE_URL + OTP_VERIFY_URL + preferences.getString(USER_ID, ""));
                if (NetworkHelper.checkActiveInternet(this)) {
                    if (!mOTP.getText().toString().equals("")) {
                        try {
                            verifyOTPData(BASE_URL + OTP_VERIFY_URL + preferences.getString(USER_ID, ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else
                        makeToast(this, "OTP field should not be empty!");
                } else
                    Methodutils.messageWithTitle(OTPActivity.this, "No Internet Connection", "Please check your internet connection.", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            return;
                        }
                    });
                break;
        }
    }

}
