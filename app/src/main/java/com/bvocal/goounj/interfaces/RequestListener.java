package com.bvocal.goounj.interfaces;

public interface RequestListener<T> {

    void onRequestCompleted(T result);
}
