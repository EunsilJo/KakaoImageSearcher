package com.github.eunsiljo.kakaoimagesearcher.api;


public interface APIResponseListener {
    void onSuccess(Object vo);
    void onFail(Object error, boolean canceled);
}
