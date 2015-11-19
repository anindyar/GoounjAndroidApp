package com.orgware.polling.interfaces;

/**
 * Created by nandagopal on 27/10/15.
 */
public interface RestApiListener<T> {

    void onRequestCompleted(T response);

    void onRequestFailed(Exception e);
}
