package com.github.eunsiljo.kakaoimagesearcher.api;

import android.content.Context;

import com.github.eunsiljo.kakaoimagesearcher.api.utils.APIManager;
import com.github.eunsiljo.kakaoimagesearcher.utils.SystemUtils;

import okhttp3.Interceptor;
import okhttp3.Request;


public class RequestHeaders {

    public static final String CLIENT_ID = "Client-ID";
    public static final String CLIENT_SECRET = "Client-Secret";
    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json; charset=utf-8";
    public static final String CONTENT_TYPE_APPLICATION_URLENCODED = "application/x-www-form-urlencoded; charset=utf-8";
    public static final String CACHE_NO_CACHE = "no-cache";
    public static final String CONTENT_TYPE_FILE = "multipart/form-data";
    public static final String USER_AGENT = "User-Agent";

    private static RequestHeaders mInstance;
    private Context mContext;


    public static RequestHeaders getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestHeaders();
        }
        mInstance.mContext = context;
        return mInstance;
    }

    /**
     * Header that use a Base
     *
     * @param chain Interceptor Chain
     * @return Request client
     */
    public Request getBasicHeader(Interceptor.Chain chain) {
        Request.Builder builder = chain.request().newBuilder();
        builder.header(CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON);
        builder.header(CACHE_CONTROL, CACHE_NO_CACHE);

        return builder.build();
    }

    /**
     * Common Header
     *
     * @param chain Interceptor Chain
     * @return Request client
     */
    public Request getHeader(Interceptor.Chain chain) {
        Request.Builder builder = chain.request().newBuilder();
        builder.header(AUTHORIZATION, APIManager.getInstance().getToken(mContext));
        builder.header(CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON);
        builder.header(CACHE_CONTROL, CACHE_NO_CACHE);

        return builder.build();
    }

    /**
     * Common Header
     *
     * @param chain Interceptor Chain
     * @return Request client
     */
    public Request getLoginHeader(Interceptor.Chain chain) {
        Request.Builder builder = chain.request().newBuilder();
        builder.header(CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON);
        builder.header(CACHE_CONTROL, CACHE_NO_CACHE);

        return builder.build();
    }


    /**
     * Header that use the File Upload
     *
     * @param chain Interceptor Chain
     * @return Request client
     */
    public Request getFileUploadHeader(Interceptor.Chain chain) {
        Request.Builder builder = chain.request().newBuilder();
        builder.header(CONTENT_TYPE, CONTENT_TYPE_FILE);
        builder.header(CACHE_CONTROL, CACHE_NO_CACHE);

        return builder.build();
    }

}
