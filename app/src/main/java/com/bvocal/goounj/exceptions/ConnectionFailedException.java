package com.bvocal.goounj.exceptions;

/**
 * Created by Nanda on 03/04/16.
 */
public class ConnectionFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public ConnectionFailedException() {
        super("Failed to connect to the server");
    }


}
