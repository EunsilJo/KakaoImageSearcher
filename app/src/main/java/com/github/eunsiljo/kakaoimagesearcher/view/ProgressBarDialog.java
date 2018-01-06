package com.github.eunsiljo.kakaoimagesearcher.view;

import android.app.Dialog;
import android.content.Context;

import com.github.eunsiljo.kakaoimagesearcher.R;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

/**
 * Created by xperi on 2018. 1. 6..
 */

public class ProgressBarDialog extends Dialog {

    public static ProgressBarDialog getInstance(Context context){
        return new ProgressBarDialog(context, R.style.ProgressBarDialog);
    }

    public ProgressBarDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_progress_bar);
        setCancelable(false);
    }
}

