package com.github.eunsiljo.kakaoimagesearcher.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.eunsiljo.kakaoimagesearcher.view.ProgressBarDialog;

/**
 * Created by EunsilJo on 2018. 1. 6..
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressBarDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = ProgressBarDialog.getInstance(this);
    }

    public void setProgressBarDialog(ProgressBarDialog dialog){
        mProgressDialog = dialog;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLoading();
    }

    public void startLoading(){
        if(mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public void stopLoading(){
        if(mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Abstract Method In Activity
    ///////////////////////////////////////////////////////////////////////////
}
