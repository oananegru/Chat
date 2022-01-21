package com.example.socialnetwork.listener;

import com.example.socialnetwork.result.ResultError;
import com.example.socialnetwork.result.ResultSuccess;

public interface ObserverUpdateListener<T> {
    void onLoading();
    void onSuccess(ResultSuccess<T> resultSuccess);
    void onError(ResultError resultError);
}
