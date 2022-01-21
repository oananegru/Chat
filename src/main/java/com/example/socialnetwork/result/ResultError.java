package com.example.socialnetwork.result;

public class ResultError implements Result {

    private final String error;

    public ResultError(String error) {
        this.error = error;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    public String getError() {
        return error;
    }
}
