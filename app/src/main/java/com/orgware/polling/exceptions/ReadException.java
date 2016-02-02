package com.orgware.polling.exceptions;

import android.util.Log;

/**
 * Created by Nandagopal on 01-Feb-16.
 */
public class ReadException extends Exception {
    private static String EXCEPTION_DESCRIPTION = "Failed, No records found";

    public ReadException() {
        super(EXCEPTION_DESCRIPTION);
    }

    public ReadException(String mDescription) {
        super(mDescription != null ? mDescription : EXCEPTION_DESCRIPTION);
        Log.e("Status Code", "" + mDescription);
    }
}
