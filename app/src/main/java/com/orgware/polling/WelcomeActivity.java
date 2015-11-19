package com.orgware.polling;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.orgware.polling.network.NetworkHelper;
import com.orgware.polling.utils.Methodutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Nandagopal on 31-Aug-15.
 */
public class WelcomeActivity extends BaseActivity {

    boolean welcomeScreen, countryScreen;
    String device_id, os_release;
    int os_version;

    Runnable loginHandler = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    };
    Runnable homeHandler = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(WelcomeActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeScreen = preferences.getBoolean(WELCOME_SCREEN, true);
        countryScreen = preferences.getBoolean(COUNTRY_DATA, true);
        device_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        os_version = android.os.Build.VERSION.SDK_INT;
        os_release = Build.VERSION.RELEASE;
        Log.e("Device", "" + device_id + "-" + os_release);
        if (countryScreen) {
            try {
                editor.putString(DEVICE_ID, "" + device_id).putString(OS_TYPE, "Android").putString(OS_VERSION, "" + os_release).commit();
                getJsonData(loadJSONFromAsset());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!NetworkHelper.checkActiveInternet(this)) {
            Methodutils.messageWithTitle(WelcomeActivity.this, "No Internet Connection", "Please check your internet connection.", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            if (welcomeScreen)
                new Handler().postDelayed(loginHandler, 2000);
            else
                new Handler().postDelayed(homeHandler, 2000);
        }
    }

    public void getJsonData(String jsonFile) throws Exception {
        try {
            JSONObject obj = new JSONObject(jsonFile);
            JSONArray m_jArry = obj.getJSONArray("country");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                JSONObject jo_inside_names = jo_inside.optJSONObject("name");
                String country_name = "" + jo_inside_names.getString("common");
                String country_code = "" + jo_inside.getString("callingCode");
                country_code = country_code.replaceAll("[\\[\\]\"]", "");
                Log.d("Total-->", i + " - " + country_name);
                db.insertCountryAndCodeValues("" + country_name, "+" + country_code);
                editor.putBoolean(COUNTRY_DATA, false).commit();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String loadJSONFromAsset() throws Exception {
        String json = null;
        try {
            InputStream is = getAssets().open("country.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return json;
    }

}
