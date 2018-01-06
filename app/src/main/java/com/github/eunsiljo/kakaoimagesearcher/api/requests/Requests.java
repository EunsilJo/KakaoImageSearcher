package com.github.eunsiljo.kakaoimagesearcher.api.requests;

import android.content.Context;

import com.github.eunsiljo.kakaoimagesearcher.api.APIRequestManager;
import com.github.eunsiljo.kakaoimagesearcher.api.RequestClient;
import com.github.eunsiljo.kakaoimagesearcher.api.utils.APIManager;


/**
 * Created by EunsilJo on 2017. 8. 11..
 */

public class Requests {

    protected static Context mContext;
    protected APIRequestManager mAPIRequestManager = null;


    public Requests() {
        mAPIRequestManager = APIRequestManager.getInstance();
    }

    public static void init(Context context, boolean debug){
        if (context == null) {
            throw new IllegalArgumentException("Non-null context required.");
        }
        mContext = context;
        RequestClient.isLog = debug;
        APIManager.isDebug = debug;
    }
}
