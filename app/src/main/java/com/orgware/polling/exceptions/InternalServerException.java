package com.orgware.polling.exceptions;

/**
 * Created by nandagopal on 14/11/15.
 */
public class InternalServerException extends Exception {
    private static String EXCEPTION_DESCRIPTION = "Requested Action Failed. Database could not be reached.";

    public InternalServerException() {
        super(EXCEPTION_DESCRIPTION);
    }

    public InternalServerException(String mDescription) {
        super(mDescription != null ? mDescription : EXCEPTION_DESCRIPTION);
    }
}
