package com.orgware.polling;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.orgware.polling.network.NetworkHelper;
import com.orgware.polling.service.CurrentLocationService;
import com.orgware.polling.utils.Methodutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nandagopal on 31-Aug-15.
 */
public class WelcomeActivity extends BaseActivity {

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 100;
    private static final long FASTEST_INTERVAL = 50;
    boolean welcomeScreen, countryScreen;
    String device_id, os_release;
    int os_version;

    Runnable loginHandler = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(WelcomeActivity.this, LoginActivityNew.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    };
    Runnable homeHandler = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(WelcomeActivity.this, MainHomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        welcomeScreen = preferences.getBoolean(WELCOME_SCREEN, true);

        device_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        os_version = android.os.Build.VERSION.SDK_INT;
        os_release = Build.VERSION.RELEASE;
        Log.e("Device", "" + device_id + "-" + os_release);

        try {
            editor.putString(DEVICE_ID, "" + device_id).putString(OS_TYPE, "Android").putString(OS_VERSION, "" + os_release).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (preferences.getString(COUNTRY, "").equals("")) {
            if (!NetworkHelper.isGpsEnabled(this))
                Methodutils.messageWithTitle(WelcomeActivity.this, "No Gps Connection", "Please check your gps connection.", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
            else
                startService(new Intent(this, CurrentLocationService.class));
        }

        if (!NetworkHelper.checkActiveInternet(this)) {
            Methodutils.messageWithTitle(WelcomeActivity.this, "No Internet Connection", "Please check your internet connection.", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            if (welcomeScreen) {
                new Handler().postDelayed(loginHandler, 2000);
            } else
                new Handler().postDelayed(homeHandler, 2000);
        }
    }

    public void turnGPSOn() {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        this.sendBroadcast(intent);

        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            this.sendBroadcast(poke);


        }
    }

    // automatic turn off the gps
    public void turnGPSOff() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            this.sendBroadcast(poke);
        }
    }


}
