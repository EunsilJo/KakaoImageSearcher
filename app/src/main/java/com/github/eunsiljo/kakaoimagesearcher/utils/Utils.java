package com.github.eunsiljo.kakaoimagesearcher.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.github.eunsiljo.kakaoimagesearcher.R;

/**
 * Created by xperi on 2018. 1. 6..
 */

public class Utils {

    // =============================================================================
    // Image
    // =============================================================================

    /**
     * image setting
     *
     * @param view SimpleDraweeView
     * @param url  image url
     */
    public static void setImage(Context context, SimpleDraweeView view, String url,
                                float ratio) {
        if (url == null) {
            view.setImageURI(Uri.EMPTY);
        } else {
            if (url.length() == 0) {
                view.setImageURI(Uri.EMPTY);
            }else {
                GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(context.getResources())
                        .setFadeDuration(300)
                        //.setPlaceholderImage(R.color.colorPrimaryDark)
                        .build();
                view.setHierarchy(hierarchy);
                view.setAspectRatio(ratio);
                view.setImageURI(Uri.parse(url));
            }
        }
    }
}
