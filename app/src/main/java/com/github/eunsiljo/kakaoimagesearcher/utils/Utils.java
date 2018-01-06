package com.github.eunsiljo.kakaoimagesearcher.utils;

import android.app.Activity;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by xperi on 2018. 1. 6..
 */

public class Utils {

    // =============================================================================
    // Toast
    // =============================================================================

    public static void showToast(Activity activity, String msg){
        Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }
}
