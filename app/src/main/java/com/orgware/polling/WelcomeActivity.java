package com.orgware.polling;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
public class WelcomeActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 100;
    private static final long FASTEST_INTERVAL = 50;
    boolean welcomeScreen, countryScreen;
    String device_id, os_release;
    int os_version;
    LocationRequest mLocationRequest;
    LocationManager locManager;
    Location currentLocation;
    GoogleApiClient client;
    String DBDIR = "GOOUNJDB", DBPATH = Environment.getExternalStorageDirectory() + File.separator + DBDIR, DBNAME = "countrydb";

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
        countryScreen = preferences.getBoolean(COUNTRY_DATA, true);
        device_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        os_version = android.os.Build.VERSION.SDK_INT;
        os_release = Build.VERSION.RELEASE;
        Log.e("Device", "" + device_id + "-" + os_release);

        try {
            editor.putString(DEVICE_ID, "" + device_id).putString(OS_TYPE, "Android").putString(OS_VERSION, "" + os_release).commit();
//                getJsonData(loadJSONFromAsset());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!preferences.getString(COUNTRY, "").equals("NA")) {
            startService(new Intent(this, CurrentLocationService.class));
        } else
            stopService(new Intent(this, CurrentLocationService.class));

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

    private void enableGPS() {
        if (!isGooglePlayServicesAvailable()) {
            Toast.makeText(this, "No Google Play", Toast.LENGTH_LONG)
                    .show();
            System.exit(0);
        }
        createLocationRequest();
        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        client.connect();
    }

    private void getLocationDetails(double latitude, double longitude) {
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

    public void copyDbToSdcard(File dbFilePath) throws Exception {
        AssetManager assetManager = getResources().getAssets();
        Log.e("File Path", "" + dbFilePath.toString());
        InputStream in;
        FileOutputStream out;
        try {
            in = assetManager.open("countrydb");
            if (in != null)
                Log.e("Is File Exist?", "Yes");
            else
                Log.e("Is File Exist? ", "No");
            out = new FileOutputStream(dbFilePath);
            byte[] buffer = new byte[8024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        client.disconnect();

    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(client,
                mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        client.connect();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        client.disconnect();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        client.disconnect();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        location = LocationServices.FusedLocationApi.getLastLocation(client);
        if (location != null) {
            try {
                editor.putString("latitude", "" + location.getLatitude()).putString("longitude", "" + location.getLongitude()).commit();
                getLocationDetails(location.getLatitude(), location.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
