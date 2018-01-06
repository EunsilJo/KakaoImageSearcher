package com.github.eunsiljo.kakaoimagesearcher.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.github.eunsiljo.kakaoimagesearcher.R;


/**
 * Created by EunsilJo on 2016. 10. 12..
 */

public class BackPressedHandler {

    private static long backKeyPressedTime = 0;
    private static Toast toast;

    public static void onBackPressed(Activity activity, String message){
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            toast.cancel();
            activity.finish();
        }
    }

    public static void onBackPressed(Activity activity) {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(activity, activity.getString(R.string.toast_common_guide_finish), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            toast.cancel();
            SystemUtils.onDestroyApp();
        }
    }


    public static void onBackPressed(Context context) {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(context, context.getString(R.string.toast_common_guide_finish), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            toast.cancel();
            SystemUtils.onDestroyApp();
        }
    }

    public static void onBackPressedKillProcess(Activity activity) {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(activity, activity.getString(R.string.toast_common_guide_finish), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            toast.cancel();
            SystemUtils.killProcess(activity);
        }
    }
}
