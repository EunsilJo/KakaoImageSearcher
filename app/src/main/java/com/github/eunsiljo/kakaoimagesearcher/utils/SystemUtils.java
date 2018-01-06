package com.github.eunsiljo.kakaoimagesearcher.utils;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by xperi on 2018. 1. 6..
 */

public class SystemUtils {

    /**
     * Destroy app
     */
    public static void onDestroyApp() {
        System.exit(0);
    }

    /**
     * Process to shut down apps
     */
    public static void killProcess(Activity activity) {
        activity.moveTaskToBack(true);
        activity.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    // =============================================================================
    // Device
    // =============================================================================

    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
    }
}
