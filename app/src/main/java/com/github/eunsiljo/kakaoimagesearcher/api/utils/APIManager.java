package com.github.eunsiljo.kakaoimagesearcher.api.utils;

import android.content.Context;

import com.github.eunsiljo.kakaoimagesearcher.R;


public class APIManager {

    /**
     * true : develop, false : release
     */
    public static boolean isDebug = false;

    private static APIManager mInstance;

    public static APIManager getInstance() {
        if (mInstance == null) {
            mInstance = new APIManager();
        }
        return mInstance;
    }

    // =============================================================================
    // Token
    // =============================================================================

    public static String getToken(Context context) {
        return context.getString(R.string.app_key);
    }

    // =============================================================================
    // Base
    // =============================================================================

    /**
     * Get base api url
     *
     * @return base api rul
     */
    public String getBaseUrl(Context context) {
        if (isDebug)
            return context.getString(R.string.develop_base_url);
        else
            return context.getString(R.string.develop_base_url);
    }
}
