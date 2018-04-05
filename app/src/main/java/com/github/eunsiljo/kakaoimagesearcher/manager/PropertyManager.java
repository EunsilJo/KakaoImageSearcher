package com.github.eunsiljo.kakaoimagesearcher.manager;

import com.github.eunsiljo.kakaoimagesearcher.application.KakaoImageSearcher;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jjo on 2018. 4. 5..
 */

public class PropertyManager {
    private static PropertyManager instance;
    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private PropertyManager() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(KakaoImageSearcher.getContext());
        mEditor = mPrefs.edit();
    }

    private static final String REFERRER_UID = "referrer_uid";

    public String getReferrerUid() {
        return mPrefs.getString(REFERRER_UID, null);
    }

    public void setReferrerUid(String referrerUid) {
        mEditor.putString(REFERRER_UID, referrerUid);
        mEditor.commit();
    }
}
