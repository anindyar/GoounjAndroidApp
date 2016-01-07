package com.orgware.polling.exceptions;

/**
 * Created by nandagopal on 31/12/15.
 */
public class ServerFailureException extends Exception {
    private static String EXCEPTION_DESCRIPTION = "Failed to connect to server. Please try again later.";

    public ServerFailureException() {
        super(EXCEPTION_DESCRIPTION);
    }

    public ServerFailureException(String mDescription) {
        super(mDescription != null ? mDescription : EXCEPTION_DESCRIPTION);
    }
}
