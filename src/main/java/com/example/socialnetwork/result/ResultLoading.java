package com.example.socialnetwork.result;

public class ResultLoading implements Result {
    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public boolean isLoading() {
        return true;
    }
}
