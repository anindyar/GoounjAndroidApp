package com.orgware.polling;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.orgware.polling.adapters.CountryAutoCompleteAdapter;
import com.orgware.polling.adapters.OTPSpinnerAdapter;
import com.orgware.polling.interfaces.Appinterface;
import com.orgware.polling.interfaces.RestApiListener;
import com.orgware.polling.network.NetworkHelper;
import com.orgware.polling.network.RestApiProcessor;
import com.orgware.polling.pojo.CountryItem;
import com.orgware.polling.utils.Methodutils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 23/10/15.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, Appinterface, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    Spinner mCountryNameSpinner;
    TextView mCountryCode;
    EditText mCity, mMobile;
    List<CountryItem> mCountryNameList = new ArrayList<>();
    OTPSpinnerAdapter mCountryNameAdapter;
    Button mAccept;
    boolean get_device_token;
    AutoCompleteTextView mCountryAutoComplete;
    CountryAutoCompleteAdapter mCountryAdapter;

    private String mGCM_ID;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_login);
        (mCountryAutoComplete = (AutoCompleteTextView) findViewById(R.id.country)).setOnItemClickListener(this);
        mCountryAutoComplete.setThreshold(1);
        get_device_token = preferences.getBoolean(GET_DEVICE_TOKEN_KEY, true);
        editor.putString(OTP_VALUE, "").commit();
        if (NetworkHelper.checkActiveInternet(this)) {
            if (get_device_token)
                new RegisterDevice(false).execute();
            else
                Log.e("Device Token", "" + preferences.getString(DEVICE_TOKEN, ""));
        } else
            Methodutils.messageWithTitle(LoginActivity.this, "No Internet Connection", "Please check your internet connection.", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        mCountryCode = (TextView) findViewById(R.id.countryCode);
        mCountryCode.setText("0");
        mCity = (EditText) findViewById(R.id.city);
        mMobile = (EditText) findViewById(R.id.mobile_number);
        try {
            mCountryNameList.clear();
            for (CountryItem items : db.getCountryData()) {
                mCountryNameList.add(new CountryItem("" + items.mCountryName, "" + items.mCountryCode));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAccept = (Button) findViewById(R.id.acceptBtn);
        mCountryAdapter = new CountryAutoCompleteAdapter(this, R.layout.spinner_menu, mCountryNameList);
//        mCountryNameSpinner = (Spinner) findViewById(R.id.countryNameSpinner);
//        mCountryNameSpinner.setOnItemSelectedListener(this);

//        mCountryNameAdapter = new OTPSpinnerAdapter(LoginActivity.this, R.layout.spinner_menu, COUNTRY_NAME_ID, mCountryNameList);
//        mCountryNameSpinner.setAdapter(mCountryNameAdapter);
        mCountryAutoComplete.setAdapter(mCountryAdapter);
//        mCountryNameAdapter.setDropDownViewResource(R.layout.dropdown_item);
        mAccept.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acceptBtn:
                if (NetworkHelper.checkActiveInternet(this)) {

                    if (mCountryAutoComplete.getText().toString().equals("")) {
                        makeToast(this, "Please choose country");
                        return;
                    }

                    if (mCity.getText().toString().equals("")) {
                        makeToast(this, "Please enter the city");
                        return;
                    }
                    if (mMobile.getText().toString().equals("")) {
                        makeToast(this, "Please enter the mobile number");
                        return;
                    }
                    if (mMobile.getText().toString().length() != 10) {
                        makeToast(this, "Mobile number should be valueable!");
                        return;
                    }
                    try {
                        editor.putString(CITY, "" + mCity.getText().toString()).putString(MOBILE, "" + mMobile.getText().toString()).commit();
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
            mLoginDetails.put(P_COUNRTY, mCountryAutoComplete.getText().toString());  // India
            mLoginDetails.put(P_CITY, preferences.getString(CITY, ""));  // Chennai
            mLoginDetails.put(P_PHONE, preferences.getString(MOBILE, ""));  // 9095914543
            mLoginDetails.put(P_DEVICEID, preferences.getString(DEVICE_ID, "")); // c2ef06f6aef1b77f
            mLoginDetails.put(P_DEVICETOKEN, preferences.getString(DEVICE_TOKEN, ""));
            //APA91bExDdNSodk9XQxkPMsniDFBXspeheMDY5jYs_vy3mZtD9YbJUjqrxBI8nX7dtUmxg34Ix2U_KfwWFFP9b0KKvE8-pS9qhO4BLN5RlUrkzWi350Ws9cb8BNIl3z_W2-OO0ecAjHc
            mLoginDetails.put(P_OSTYPE, preferences.getString(OS_TYPE, "Android")); // Android
            mLoginDetails.put(P_OSVERSION, preferences.getString(OS_VERSION, "")); // 4.4.2
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestFailed(Exception e) {
                if (e == null) {
                    Log.e("Error", "" + e.getMessage());
                    Methodutils.message(LoginActivity.this, "Internal Server Error. Requested Action Failed", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                } else
                    Methodutils.message(LoginActivity.this, "" + e.getMessage(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
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
            startActivity(new Intent(LoginActivity.this, OTPActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p/>
     * Impelmenters can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        makeToast(this, "" + mCountryNameList.get(mCountryNameSpinner.getSelectedItemPosition()).mCountryName);
        editor.putString(COUNTRY, "" + mCountryNameList.get(mCountryNameSpinner.getSelectedItemPosition()).mCountryName).commit();
        if (mCountryNameList.get(mCountryNameSpinner.getSelectedItemPosition()).mCountryName.equals("Select Country"))
            mCountryCode.setText("0");
        else {
            String mCountryCodeText = mCountryNameList.get(mCountryNameSpinner.getSelectedItemPosition()).mCountryCode;
            mCountryCode.setText("" + mCountryCodeText);
        }
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        try {
            String mCountryCodeText = mCountryNameList.get(position).mCountryCode;
            mCountryCode.setText("" + mCountryCodeText);
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
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("loading...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            cloudMessaging = GoogleCloudMessaging
                    .getInstance(LoginActivity.this);
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
