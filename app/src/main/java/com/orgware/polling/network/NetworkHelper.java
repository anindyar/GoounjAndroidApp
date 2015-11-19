package com.orgware.polling.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkHelper {

    public static final String PREFERENCE_NAME = "fieldserv";
    public static final String NETWORK_URL = "mainurl";
    Context mContext;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isAvailable();
    }

    public static NetworkInfo getActiveNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();

        return activeNetworkInfo;
    }

    public static boolean isHostAvailable(Context context, String host) {
        try {
            URL url = new URL(host);

            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkActiveInternet(Context mContext) {
        ConnectivityManager conMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo != null) {
            return true;
        }
        return false;
    }

}
