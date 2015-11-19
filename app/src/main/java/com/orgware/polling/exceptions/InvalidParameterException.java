package com.orgware.polling.exceptions;

/**
 * Created by rajeshwaran on 6/10/15.
 */
public class InvalidParameterException extends Exception {

    private static String EXCEPTION_DESCRIPTION = "Failed, Invalid parameter";

    public InvalidParameterException() {
        super(EXCEPTION_DESCRIPTION);
    }

    public InvalidParameterException(String mDescription) {
        super(mDescription != null ? mDescription : EXCEPTION_DESCRIPTION);
    }
}
