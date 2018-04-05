package com.github.eunsiljo.kakaoimagesearcher.application;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.eunsiljo.kakaoimagesearcher.api.requests.Requests;
import com.github.eunsiljo.kakaoimagesearcher.utils.log;

/**
 * Created by EunsilJo on 2018. 1. 6..
 */

public class KakaoImageSearcher extends Application {
    private static KakaoImageSearcher instance;
    private static Context context;

    public static final boolean isDebug = false;

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        setContext(this);

        // true : develop, false : release
        Requests.init(this, isDebug);
        Fresco.initialize(this);

        // true : show log, false : hide log
        log.init(isDebug);
    }

    public static KakaoImageSearcher getInstance() {
        return instance;
    }

    public static void setInstance(KakaoImageSearcher instance) {
        KakaoImageSearcher.instance = instance;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        KakaoImageSearcher.context = context;
    }
}
