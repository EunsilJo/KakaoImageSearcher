package com.github.eunsiljo.kakaoimagesearcher.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.facebook.appevents.AppEventsLogger;
import com.github.eunsiljo.kakaoimagesearcher.R;
import com.github.eunsiljo.kakaoimagesearcher.api.data.ImageItemVO;
import com.github.eunsiljo.kakaoimagesearcher.config.Tags;
import com.github.eunsiljo.kakaoimagesearcher.utils.Utils;
import com.google.firebase.analytics.FirebaseAnalytics;

import uk.co.senab.photoview.PhotoView;

public class PhotoViewActivity extends BaseActivity {

    private PhotoView photoView;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setContentView(R.layout.activity_photo_view);
        initLayout();
        initListener();
        initData();
    }

    private void initLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photoView = (PhotoView)findViewById(R.id.photoView);
    }

    private void initListener() {
    }

    private void initData(){
        ImageItemVO image = getIntent().getParcelableExtra(Tags.TAG_IMAGE);

        if(image != null){
            Utils.setGlideImage(photoView, image.getImage_url());

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ucccuccc");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Eunsil Jo");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            bundle.putString(FirebaseAnalytics.Param.VALUE, image.getImage_url());
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            AppEventsLogger.newLogger(getApplicationContext())
                    .logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
