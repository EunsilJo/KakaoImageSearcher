package com.github.eunsiljo.kakaoimagesearcher.api.data;

/**
 * Created by xperi on 2018. 1. 6..
 */

public class ImageItemVO {
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
}
