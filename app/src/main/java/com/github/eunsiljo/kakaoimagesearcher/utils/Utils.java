package com.github.eunsiljo.kakaoimagesearcher.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
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
     * @param ratio  image ratio
     */
    public static void setFrescoImage(SimpleDraweeView view, String url, float ratio) {
        if (url == null) {
            view.setImageURI(Uri.EMPTY);
        } else {
            if (url.length() == 0) {
                view.setImageURI(Uri.EMPTY);
            }else {
                GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(view.getResources())
                        .setFadeDuration(300)
                        .setPlaceholderImage(R.drawable.ic_more_horiz_black_24dp)
                        .build();
                view.setHierarchy(hierarchy);
                view.setAspectRatio(ratio);
                view.setImageURI(Uri.parse(url));
            }
        }
    }

    /**
     * image setting
     *
     * @param view ImageView
     * @param url  image url
     */
    public static void setGlideImage(ImageView view, String url){
        if(url == null){
            Glide.with(view.getContext()).load(Uri.EMPTY).into(view);
        }else{
            if(url.length() == 0){
                Glide.with(view.getContext()).load(Uri.EMPTY).into(view);
            }else{
                Glide.with(view.getContext())
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .thumbnail(0.3f)
                        .into(view);
            }
        }
    }

    // =============================================================================
    // Toast
    // =============================================================================

    public static void showToast(Activity activity, String msg){
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}
