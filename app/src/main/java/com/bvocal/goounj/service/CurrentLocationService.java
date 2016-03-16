package com.bvocal.goounj.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by nandagopal on 20/1/16.
 */
public class CurrentLocationService extends Service implements
        GoogleApiClient.ConnectionCallbacks, LocationListener,
        GoogleApiClient.OnConnectionFailedListener {

    LocationRequest locationRequest;
    GoogleApiClient googleApiClient;
    Activity act;
    double duplicateLat = 0.0, duplicateLong = 0.0;

    //
    public CurrentLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //
    @Override
    public void onCreate() {

        createLocationRequest();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        if (!googleApiClient.isConnected())
            googleApiClient.connect();

    }

    //
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    //
    @Override
    public void onDestroy() {
        super.onDestroy();
        googleApiClient.disconnect();

    }

    //
    @Override
    public void onConnectionFailed(ConnectionResult result) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            sendCurrentLatLong(location.getLatitude(), location.getLongitude(),
                    true);
        } else {
            Log.e("no location", "error");
            sendCurrentLatLong(duplicateLat, duplicateLong, false);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        Location location = LocationServices.FusedLocationApi
                .getLastLocation(googleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, this);
        if (location != null) {
            Log.e("location:",
                    "" + location.getLatitude() + " - " + location.getLongitude());
            sendCurrentLatLong(location.getLatitude(), location.getLongitude(),
                    true);
        } else {
            sendCurrentLatLong(duplicateLat, duplicateLong, false);
        }

    }

    public void checkAvailability() {
        if (!isGooglePlayServicesAvailable()) {
            Toast.makeText(getApplicationContext(),
                    "Google play cannot be found: ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, act, 0).show();
            return false;
        }
    }

    //
    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(50);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    //
    public void sendCurrentLatLong(double latitude, double longitude,
                                   boolean status) {
        Intent in = new Intent();
        if (status) {
            in.putExtra("currentLat", latitude);
            in.putExtra("currentLong", longitude);
            in.putExtra("latLongStatus", status);
        } else {
            in.putExtra("currentLat", latitude);
            in.putExtra("currentLong", longitude);
            in.putExtra("latLongStatus", status);
        }
        in.setAction("LOCATION CHANGE ACTION");
        LocalBroadcastManager.getInstance(this).sendBroadcast(in);

    }
}