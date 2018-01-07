package com.github.eunsiljo.kakaoimagesearcher.api.services;

import com.github.eunsiljo.kakaoimagesearcher.api.config.APIAddress;
import com.github.eunsiljo.kakaoimagesearcher.api.config.APIParamValue;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by EunsilJo on 2018. 1. 6..
 */

public interface SearchServices {

    @GET(APIAddress.SEARCH + APIAddress.IMAGE + "?")
    Call<Object> requestSearchImageList(@Query(APIParamValue.QUERY) String search,
                                        @Query(APIParamValue.PAGE) int page,
                                        @Query(APIParamValue.SIZE) int size);
}
