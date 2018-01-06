package com.github.eunsiljo.kakaoimagesearcher.api.data;

import java.util.ArrayList;

/**
 * Created by xperi on 2018. 1. 6..
 */

public class SearchImageResult {
    private MetaVO meta;
    private ArrayList<ImageItemVO> documents;

    public MetaVO getMeta() {
        return meta;
    }

    public ArrayList<ImageItemVO> getDocuments() {
        return documents;
    }
}
