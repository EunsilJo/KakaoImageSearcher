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

    private static final String FIRST_OPEN = "first_open";

    public boolean getFirstOpen() {
        return mPrefs.getBoolean(FIRST_OPEN, true);
    }

    public void setFirstOpen(boolean first) {
        mEditor.putBoolean(FIRST_OPEN, first);
        mEditor.commit();
    }
}
