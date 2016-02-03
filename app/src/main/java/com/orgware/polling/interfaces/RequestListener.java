package com.orgware.polling.interfaces;

public interface RequestListener<T> {

    void onRequestCompleted(T result);
}
