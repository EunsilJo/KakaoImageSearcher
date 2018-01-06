package com.github.eunsiljo.kakaoimagesearcher.api.data;

/**
 * Created by xperi on 2018. 1. 6..
 */

public class MetaVO {
    private int total_count;
    private int pageable_count;
    private boolean is_end;

    public int getTotal_count() {
        return total_count;
    }

    public int getPageable_count() {
        return pageable_count;
    }

    public boolean isIs_end() {
        return is_end;
    }
}
