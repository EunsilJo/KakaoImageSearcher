package com.github.eunsiljo.kakaoimagesearcher.data;

import com.github.eunsiljo.kakaoimagesearcher.api.data.ImageItemVO;

/**
 * Created by xperi on 2018. 1. 6..
 */

public class SearchItemData {
    private ImageItemVO image;

    public SearchItemData(ImageItemVO image) {
        this.image = image;
    }

    public ImageItemVO getImage() {
        return image;
    }
}
