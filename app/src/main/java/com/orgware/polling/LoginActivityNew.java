package com.orgware.polling;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.orgware.polling.interfaces.RestApiListener;
import com.orgware.polling.network.NetworkHelper;
import com.orgware.polling.network.RestApiProcessor;
import com.orgware.polling.service.CurrentLocationService;
import com.orgware.polling.utils.Methodutils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by nandagopal on 20/1/16.
 */
public class LoginActivityNew extends BaseActivity implements View.OnClickListener {

    boolean get_device_token;
    Animation mSlideFromLeftAnimation, mSlideFromRightAnimation;
    private double currentLat, currentLongt;
    // receives new location when location changes
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("LOCATION CHANGE ACTION")) {
                if (intent.getExtras().containsKey("latLongStatus")) {
                    if (intent.getExtras().getBoolean(("latLongStatus"))) {
                        currentLat = intent.getExtras().getDouble("currentLat");
                        currentLongt = intent.getExtras().getDouble(
                                "currentLong");
                        Log.e("Mine", currentLat + " - " + currentLongt);
//                        mCountryText.setText("India");
//                        mCityText.setText("Chennai");
                        if (preferences.getString(COUNTRY, "").equals("") && preferences.getString(CITY, "").equals(""))
                            try {
                                getLocationDetails(currentLat, currentLongt);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        else
                            stopService(new Intent(LoginActivityNew.this, CurrentLocationService.class));
                    }
                }

            }
        }
    };
    private String mGCM_ID;
    private EditText mNameText, mCountryText, mCityText, mMobileNumber;
    private CheckBox mCBAccept;
    private Button mBtnAccept;
    private TextView mTermsOfUse;

    private void getLocationDetails(double latitude, double longitude) throws Exception {
        Geocoder geocoder;
        StringBuilder sb = new StringBuilder();
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (Exception e) {

        }
        String city = addresses.get(0).getLocality();
        String country = addresses.get(0).getCountryName();
        sb.append("City - " + city).append(" \n ").append("Country - " + country);
        Log.e("Country", " - " + sb.toString());
        mCountryText.setText("" + country);
        mCityText.setText("" + city);
        editor.putString(COUNTRY, "" + mCountryText.getText().toString()).
                putString(CITY, "" + mCityText.getText().toString()).commit();

    }


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        mSlideFromLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.slideleft_to_right);
        mSlideFromRightAnimation = AnimationUtils.loadAnimation(this, R.anim.slideleft_to_right);

        setContentView(R.layout.activity_login_new);
        get_device_token = preferences.getBoolean(GET_DEVICE_TOKEN_KEY, true);
        if (NetworkHelper.checkActiveInternet(this)) {

            if (get_device_token)
                new RegisterDevice(false).execute();
            else
                Log.e("Device Token", "" + preferences.getString(DEVICE_TOKEN, ""));
        } else
            Methodutils.messageWithTitle(LoginActivityNew.this, "No Internet Connection", "Please check your internet connection.", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        mNameText = (EditText) findViewById(R.id.login_name);
        mCountryText = (EditText) findViewById(R.id.login_country);
        mCityText = (EditText) findViewById(R.id.login_city);
        mMobileNumber = (EditText) findViewById(R.id.login_mobile);
        mNameText.startAnimation(mSlideFromLeftAnimation);
        mCountryText.startAnimation(mSlideFromLeftAnimation);
        mCityText.startAnimation(mSlideFromLeftAnimation);
        mMobileNumber.startAnimation(mSlideFromLeftAnimation);
        mCBAccept = (CheckBox) findViewById(R.id.login_cb_accept);
        (mBtnAccept = (Button) findViewById(R.id.login_btn_accept)).setOnClickListener(this);
        ((mTermsOfUse = (TextView) findViewById(R.id.login_termsofuse))).setOnClickListener(this);
        mCountryText.setText("" + preferences.getString(COUNTRY, "India"));
        mCityText.setText("" + preferences.getString(CITY, "Chennai"));

    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                new IntentFilter("LOCATION CHANGE ACTION"));
        // startService(new Intent(this, GetSpeedService.class));
        // startService(new Intent(this, CurrentLocationService.class));

    }

    //
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                new IntentFilter("LOCATION CHANGE ACTION"));
        startService(new Intent(this, CurrentLocationService.class));
        // startService(new Intent(this, GetSpeedService.class));

    }

    //
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        stopService(new Intent(this,
                CurrentLocationService.class));
        // stopService(new Intent(AdvertisementActivity.this,
        // GetSpeedService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        stopService(new Intent(this,
                CurrentLocationService.class));
        // stopService(new Intent(AdvertisementActivity.this,
        // GetSpeedService.class));
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_termsofuse:
                Intent intentTermsOfUse = new Intent(this, TermsAndConditionsActivity.class);
                startActivity(intentTermsOfUse);
                break;
            case R.id.login_btn_accept:
                if (NetworkHelper.checkActiveInternet(this)) {

                    if (mNameText.getText().toString().equals("")) {
                        makeToast(this, "Please enter your name");
                        return;
                    }

                    if (mCountryText.getText().toString().equals("")) {
                        makeToast(this, "Getting location details...");
                        return;
                    }
                    if (mCityText.getText().toString().equals("")) {
                        makeToast(this, "Getting location details...");
                        return;
                    }
                    if (mMobileNumber.getText().toString().length() != 10) {
                        makeToast(this, "Mobile number should be valueable!");
                        return;
                    }

                    if (!mCBAccept.isChecked()) {
                        makeToast(this, "Please accept the terms of use!");
                        return;
                    }

                    try {
                        editor.putString(USERNAME, "" + mNameText.getText().toString()).
                                putString(COUNTRY, "" + mCountryText.getText().toString()).
                                putString(CITY, "" + mCityText.getText().toString()).putString(MOBILE, "" + mMobileNumber.getText().toString()).commit();
                        pushLoginData(BASE_URL + USER_LOGIN_URL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    Methodutils.messageWithTitle(this, "No Internet Connection", "Please check your internet connection", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            return;
                        }
                    });
                break;
        }
    }

    private String prepareLoginParams() {
        JSONObject mLoginDetails = new JSONObject();

        try {
            mLoginDetails.put(P_NAME, preferences.getString(USERNAME, ""));
            mLoginDetails.put(P_COUNRTY, preferences.getString(COUNTRY, ""));  // India
            mLoginDetails.put(P_CITY, preferences.getString(CITY, ""));  // Chennai
            mLoginDetails.put(P_PHONE, preferences.getString(MOBILE, ""));  // 9095914543
            mLoginDetails.put(P_DEVICEID, preferences.getString(DEVICE_ID, "")); // c2ef06f6aef1b77f
            mLoginDetails.put(P_DEVICETOKEN, preferences.getString(DEVICE_TOKEN, ""));
            //APA91bExDdNSodk9XQxkPMsniDFBXspeheMDY5jYs_vy3mZtD9YbJUjqrxBI8nX7dtUmxg34Ix2U_KfwWFFP9b0KKvE8-pS9qhO4BLN5RlUrkzWi350Ws9cb8BNIl3z_W2-OO0ecAjHc
            mLoginDetails.put(P_OSTYPE, preferences.getString(OS_TYPE, "Android")); // Android
            mLoginDetails.put(P_OSVERSION, preferences.getString(OS_VERSION, "")); // 4.4.2
            Log.e("Login", "" + mLoginDetails.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mLoginDetails.toString();
    }

    private void pushLoginData(String url) throws Exception {

        RestApiProcessor processor = new RestApiProcessor(this, RestApiProcessor.HttpMethod.POST, url, true, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                try {
                    saveLoginCredentials(response);
                    stopService(new Intent(LoginActivityNew.this, CurrentLocationService.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestFailed(Exception e) {
                if (e == null) {
                    Log.e("Error", "Exception is null");
                    Methodutils.message(LoginActivityNew.this, "Internal Server Error. Requested Action Failed", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                } else {
                    Log.e("Error", "Exception is not null");
                    Methodutils.message(LoginActivityNew.this, "" + e.getMessage(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }
            }
        });
        processor.execute(prepareLoginParams().toString());
    }

    private void saveLoginCredentials(String result) throws Exception {
        try {
            JSONObject resultObject = new JSONObject(result);
            editor.putString(USER_ID, "" + resultObject.optInt(USER_ID)).putString(PUBLIC_KEY, "" + resultObject.optString(PUBLIC_KEY)).putString(SECRET_KEY,
                    "" + resultObject.optString(SECRET_KEY)).commit();
            Log.e("Login Values", "UserId-" + resultObject.optInt(USER_ID) + "\nPublic Key-" + resultObject.optString(PUBLIC_KEY) + "\nSecret-" + resultObject.optString(SECRET_KEY));
            startActivity(new Intent(LoginActivityNew.this, OTPActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class RegisterDevice extends AsyncTask<String, String, String> {

        boolean showProg;
        ProgressDialog dialog;

        private GoogleCloudMessaging cloudMessaging;

        public RegisterDevice(boolean prog) {
            showProg = prog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!showProg)
                return;
            dialog = new ProgressDialog(LoginActivityNew.this);
            dialog.setMessage("loading...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            cloudMessaging = GoogleCloudMessaging
                    .getInstance(LoginActivityNew.this);
            try {
                mGCM_ID = cloudMessaging.register(APP_ID);
                Log.e("Device Token", "" + mGCM_ID);
                editor.putString(DEVICE_TOKEN, "" + mGCM_ID).commit();
                if (mGCM_ID != null) {
                    return mGCM_ID;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(String regid) {
            super.onPostExecute(regid);
            if (showProg) {
                dialog.dismiss();
            }
            if (regid == null)
                return;
            if (!regid.equals(""))
                editor.putBoolean(GET_DEVICE_TOKEN_KEY, false).commit();
        }
    }


}
