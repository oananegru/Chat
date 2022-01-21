package com.example.socialnetwork.result;

public class ResultSuccess<T> implements Result {

    private final T data;

    public ResultSuccess() {
        this.data = null;
    }
    public ResultSuccess(T data) {
        this.data = data;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    public T getData() {
        return data;
    }
}
