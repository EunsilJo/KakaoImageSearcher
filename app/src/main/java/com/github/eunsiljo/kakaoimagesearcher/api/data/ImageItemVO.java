package com.github.eunsiljo.kakaoimagesearcher.api.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by EunsilJo on 2018. 1. 6..
 */

public class ImageItemVO implements Parcelable{
    private String collection;
    private String thumbnail_url;
    private String image_url;
    private int width;
    private int height;
    private String display_sitename;
    private String doc_url;
    private String datetime;

    public String getCollection() {
        return collection;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getDisplay_sitename() {
        return display_sitename;
    }

    public String getDoc_url() {
        return doc_url;
    }

    public String getDatetime() {
        return datetime;
    }

    // =============================================================================
    // Parcelable
    // =============================================================================

    protected ImageItemVO(Parcel in) {
        collection = in.readString();
        thumbnail_url = in.readString();
        image_url = in.readString();
        width = in.readInt();
        height = in.readInt();
        display_sitename = in.readString();
        doc_url = in.readString();
        datetime = in.readString();
    }

    public static final Creator<ImageItemVO> CREATOR = new Creator<ImageItemVO>() {
        @Override
        public ImageItemVO createFromParcel(Parcel in) {
            return new ImageItemVO(in);
        }

        @Override
        public ImageItemVO[] newArray(int size) {
            return new ImageItemVO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(collection);
        dest.writeString(thumbnail_url);
        dest.writeString(image_url);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(display_sitename);
        dest.writeString(doc_url);
        dest.writeString(datetime);
    }
}
