package com.github.eunsiljo.kakaoimagesearcher.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.eunsiljo.kakaoimagesearcher.R;
import com.github.eunsiljo.kakaoimagesearcher.api.data.ImageItemVO;
import com.github.eunsiljo.kakaoimagesearcher.data.SearchItemData;
import com.github.eunsiljo.kakaoimagesearcher.utils.Utils;

/**
 * Created by EunsilJo on 2018. 1. 6..
 */

public class SearchItemViewHolder extends RecyclerView.ViewHolder{

    private SearchItemData mSearchItemData;
    private Context mContext;

    private SimpleDraweeView imgSearch;

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public SearchItemViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        imgSearch = (SimpleDraweeView)itemView.findViewById(R.id.imgSearch);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onItemClick(v, getLayoutPosition());
                }
            }
        });
    }

    public void setSearchItem(SearchItemData searchItem) {
        if(searchItem != null) {
            mSearchItemData = searchItem;

            ImageItemVO image = searchItem.getImage();
            float ratio = (float)image.getWidth() / (float)image.getHeight();
            Utils.setFrescoImage(imgSearch, image.getImage_url(), ratio);
        }
    }
}