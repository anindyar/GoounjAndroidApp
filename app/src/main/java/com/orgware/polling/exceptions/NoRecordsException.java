package com.orgware.polling.exceptions;

/**
 * Created by rajeshwaran on 6/10/15.
 */
public class NoRecordsException extends Exception {

    private static String EXCEPTION_DESCRIPTION = "Failed, No records found";

    public NoRecordsException() {
        super(EXCEPTION_DESCRIPTION);
    }

    public NoRecordsException(String mDescription) {
        super(mDescription != null ? mDescription : EXCEPTION_DESCRIPTION);
    }
}
