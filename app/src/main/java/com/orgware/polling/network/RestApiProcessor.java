package com.orgware.polling.network;

/**
 * Created by nandagopal on 30/10/15.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.orgware.polling.exceptions.InternalServerException;
import com.orgware.polling.exceptions.InvalidParameterException;
import com.orgware.polling.exceptions.NoNetworkException;
import com.orgware.polling.exceptions.NoRecordsException;
import com.orgware.polling.exceptions.ServerFailureException;
import com.orgware.polling.interfaces.Appinterface;
import com.orgware.polling.interfaces.RestApiListener;
import com.orgware.polling.utils.Methodutils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.NoSuchElementException;

/**
 * Created by Raja Mohamed on 24/9/15.
 * <p>
 * This async task handles the api calls from the UI part mostly
 */
public class RestApiProcessor extends AsyncTask<String, String, String> implements Appinterface {


    /**
     * mContext is the context current activity passed from any of the activity or fragment, purpose to handle UI feature
     * like showing progress while the background process is in progress
     */
    private Context mContext;

    private Activity mActivity;

    /**
     * mHttpMethod is the method for particular api call, based on this will be formatting data writing to the api call
     */
    private HttpMethod mHttpMethod;

    /**
     * mApiUrl url for the method call
     */
    private String mApiUrl;

    // progress variable
    private boolean showProgress;

    /**
     * mRestApiListener call back to response to the UI source for the method call either onRequestFailed with the exception
     * or onRequestCompleted with response
     */
    private RestApiListener<String> mRestApiListener;


    private SharedPreferences mPreferences;

    /**
     * mProgressDialog loading dialog shown when back ground process is in progress to avoid user interaction
     */
    private ProgressDialog mProgressDialog;

    // Thrown exception to return to the source call
    private Exception mException;


    // Flag to Authorisation header for the service call
    private boolean mAuthorisation;

    /**
     * Constructor to initialize the fields which will be used on the execute
     *
     * @param context       activity context
     * @param httpMethod    api method type POST, PUT, DELETE, remove this argument if GET method
     * @param url           api url to connect the method
     * @param listener      call back listener, call either onRequestCompleted with response or onRequestFailed with exception
     * @param authorisation flag for set the authorisation header
     */
    public RestApiProcessor(Context context, HttpMethod httpMethod, String url, boolean progress, boolean authorisation, RestApiListener<String> listener) {
        mContext = context;
        mHttpMethod = httpMethod;
        mApiUrl = url;
        this.showProgress = progress;
        mRestApiListener = listener;
        mAuthorisation = authorisation;
        mPreferences = mContext.getSharedPreferences(SHARED_PREFERENCES_POLLING, Context.MODE_PRIVATE);
    }

    /**
     * Constructor to initialize the fields which will be used on the execute, for only GET method
     *
     * @param context  activity context
     * @param url      api url to connect the method
     * @param listener call back listener, call either onRequestCompleted with response or onRequestFailed with exception
     */
    public RestApiProcessor(Context context, String url, boolean progress, RestApiListener<String> listener) {
        this(context, HttpMethod.GET, url, true, true, listener);
    }

    /**
     * Reads the response from the inputStream of httpsURLConnection and appends the same to stringBuilder
     *
     * @param inputStream response inputStream from httpsURLConnection
     * @return stringBuilder with inputStream data
     */
    private static StringBuilder readResponse(InputStream inputStream) throws IOException, NullPointerException {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder;
    }

    /**
     * Creates the progress loading dialog, sets cancelable false, sets title and display the dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (!showProgress)
            return;
        else {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("loading...");
            mProgressDialog.show();
        }

    }

    /*Cancel the progress dialog when on Cancel is called*/
    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (showProgress && mProgressDialog != null)
            mProgressDialog.cancel();
    }


    /**
     * Receive the RestApiProcessor instance execute call with Varchar array parameter, create  doInBackground#url from
     * {@link RestApiProcessor#mApiUrl}, opens the httpsURLConnection,
     * checks the {@link RestApiProcessor#mHttpMethod} then changes method name and if has params write the same
     * httpsURLConnection instance finally connects and read response from httpsURLConnection
     *
     * @param params parameters to be send for the api call, it should be the form of KEY, VALUE, KEY, VALUE...
     * @return response from the httpsURLConnection
     */
    @Override
    protected String doInBackground(String... params) {
        try {

            if (!NetworkHelper.isNetworkAvailable(mContext)) {
                mException = new NoNetworkException();
                return null;
            }
            URL url = new URL(mApiUrl);
            HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
            httpsURLConnection.setReadTimeout(15000);
            httpsURLConnection.setConnectTimeout(20000);
            httpsURLConnection.setDoInput(true);
//            httpsURLConnection.setRequestProperty(ApiToken, API_TOKEN_FOR_APP);
            httpsURLConnection.setRequestProperty("Content-Type", "application/json");
//            if (mAuthorisation) {
//                httpsURLConnection.setRequestProperty("UserId", mPreferences.getString(id, ""));
//            }
            switch (mHttpMethod) {
                case POST:
                case PUT:
                    httpsURLConnection.setRequestMethod(mHttpMethod == HttpMethod.POST ? "POST" : "PUT");
                    if (params.length == 0)
                        break;

                    OutputStream outputStream = httpsURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    bufferedWriter.write(params[0]);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    break;

                case DELETE:
                    httpsURLConnection.setRequestMethod("DELETE");
                    break;
                case GET:
                    httpsURLConnection.setRequestMethod("GET");
                    break;

            }
            httpsURLConnection.connect();

            int mStatus = httpsURLConnection.getResponseCode();
            Log.e("Status Code", "" + mStatus);
            if (mStatus == 200 || mStatus == 201)
                return readResponse(httpsURLConnection.getInputStream()).toString();

            if (mStatus == 404) throw new NoRecordsException();

            if (mStatus == 422) throw new InvalidParameterException();

            if (mStatus == 204) throw new NoSuchElementException();

            if (mStatus == 500) throw new InternalServerException();

            return null;
        } catch (Exception e) {
            if (!showProgress)
                Log.e("No Dialog", "No Dialog");
            else
                mProgressDialog.dismiss();
            mException = e;
            return null;
        }
    }

    /**
     * Creates URL encoded parameters for the api method call POST and PUT with params of {@link RestApiProcessor#doInBackground(String...)}
     *
     * @param params parameters received in the method doInBackground
     * @return URL encoded parameter String
     * @throws UnsupportedEncodingException
     */

    /**
     * Closes the {@link RestApiProcessor#mProgressDialog} if it is shown
     * <p>Calls the onRequestCompleted of {@link RestApiProcessor#mRestApiListener} if response is not null</p>
     * <p>Calls the onRequestFailed of {@link RestApiProcessor#mRestApiListener} if response is null with NullPointerException</p>
     *
     * @param response String received from the doInBackground method
     */
    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        Log.e("Error", " " + response);
        if (!showProgress)
            Log.e("No Dialog", "No Dialog - " + response);
        else if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.dismiss();
        if (mException != null) {
            if (mException instanceof NoNetworkException || mException instanceof ConnectException) {
                Log.e("Exception", "" + mException.getMessage());
                mException = new ServerFailureException();
//                Toast.makeText(mContext, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                mRestApiListener.onRequestFailed(mException != null ? mException : new NullPointerException());
                return;
            }
        }

        if (response == null || response.equals("null") || response.equals("")) {
            mException = new ServerFailureException();
            mRestApiListener.onRequestFailed(mException != null ? mException : new NullPointerException());
            return;
        }

        mRestApiListener.onRequestCompleted(response);

    }

    /**
     * HttpMethod enum get what type of method is the api call from the source
     */
    public enum HttpMethod {
        GET, POST, PUT, DELETE
    }
}
