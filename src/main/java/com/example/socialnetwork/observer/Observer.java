package com.example.socialnetwork.observer;

import com.example.socialnetwork.listener.ObserverUpdateListener;
import com.example.socialnetwork.result.Result;
import com.example.socialnetwork.result.ResultError;
import com.example.socialnetwork.result.ResultSuccess;

public class Observer<T> {
    private Result value;
    private ObserverUpdateListener<T> observerUpdateListener;
    public void setOnUpdateListener(ObserverUpdateListener<T> observerUpdateListener) {
        this.observerUpdateListener = observerUpdateListener;
    }

    @SuppressWarnings("unchecked")
    public void setValue(Result value) {
        this.value = value;
        if (value.isSuccess()) {
            observerUpdateListener.onSuccess((ResultSuccess<T>) value);
        } else if (value.isLoading()) {
            observerUpdateListener.onLoading();
        } else {
            observerUpdateListener.onError((ResultError) value);
        }
    }

    public Result getValue() {
        return value;
    }
}
