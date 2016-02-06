package com.orgware.polling;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
    private static final int REQUEST_CODE_CONTACTS = 1, REQUEST_CODE_LOCATION = 2, REQUEST_READ_SMS = 3, REQUEST_CODE_STORAGE = 4;
    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS};
    boolean welcomeScreen, countryScreen;
    String device_id, os_release;
    int os_version;
    int[] test = {1, 2};
    String[] mPermission = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    ImageView mSplashLogo;
    int INTERNET_CODE = 1, GPS_CODE = 2;
    Runnable loginHandler = new Runnable() {

        @Override
        public void run() {
            mSplashLogo.startAnimation(mEndAnimation);
            startActivity(new Intent(WelcomeActivity.this, LoginHomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    };
    Runnable homeHandler = new Runnable() {
        @Override
        public void run() {
            mSplashLogo.startAnimation(mEndAnimation);
            startActivity(new Intent(WelcomeActivity.this, MainHomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    };
    Animation mStartAnimation, mEndAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mSplashLogo = (ImageView) findViewById(R.id.splash_logo);
        mStartAnimation = AnimationUtils.makeInAnimation(this, true);
        mEndAnimation = AnimationUtils.makeOutAnimation(this, true);
        mSplashLogo.startAnimation(mStartAnimation);
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


        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                    != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[1])
                            != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[2])
                            != MockPackageManager.PERMISSION_GRANTED) {
                // Request missing location permission.
                ActivityCompat.requestPermissions(this,
                        mPermission, REQUEST_CODE_LOCATION);
            } else {
                try {
                    if (preferences.getString(COUNTRY, "").equals("")) {
//                        startService(new Intent(this, CurrentLocationService.class));
                        checkInternetAndGpsConnection();
                    } else
                        checkInternetConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        if (requestCode == 2) {
            if (grantResults.length == 3 &&
                    grantResults[0] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == MockPackageManager.PERMISSION_GRANTED) {
                // success!
                try {
                    if (preferences.getString(COUNTRY, "").equals("")) {
//                        startService(new Intent(this, CurrentLocationService.class));
                        checkInternetAndGpsConnection();
                    } else
                        checkInternetConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Methodutils.message(this, "Permission Denied", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        }

    }

    private void checkInternetAndGpsConnection() throws Exception {
        if (!NetworkHelper.checkActiveInternet(this)) {
            Methodutils.messageWithTitle(WelcomeActivity.this, "No Internet Connection", "Please check your internet connection.", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else if (!NetworkHelper.isGpsEnabled(this)) {
            Methodutils.messageWithTitle(WelcomeActivity.this, "No Gps Connection", "Please enable your Gps connection.", new View.OnClickListener() {
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

    private void checkInternetConnection() throws Exception {
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

}
