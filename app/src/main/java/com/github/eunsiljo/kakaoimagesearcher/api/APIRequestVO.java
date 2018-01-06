package com.github.eunsiljo.kakaoimagesearcher.api;

import retrofit2.Call;
import retrofit2.Callback;


public class APIRequestVO<T> {

    Call<T> call;
    Callback<T> callback;

    public APIRequestVO() {}

    public Call<T> getCall() {
        return call;
    }

    public void setCall(Call<T> call) {
        this.call = call;
    }

    public Callback<T> getCallback() {
        return callback;
    }

    public void setCallback(Callback<T> callback) {
        this.callback = callback;
    }
}