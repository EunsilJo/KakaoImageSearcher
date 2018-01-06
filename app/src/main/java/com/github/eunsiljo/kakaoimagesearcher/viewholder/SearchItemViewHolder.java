package com.github.eunsiljo.kakaoimagesearcher.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.github.eunsiljo.kakaoimagesearcher.R;
import com.github.eunsiljo.kakaoimagesearcher.data.SearchItemData;

/**
 * Created by xperi on 2018. 1. 6..
 */

public class SearchItemViewHolder extends RecyclerView.ViewHolder{

    private SearchItemData mSearchItemData;
    private Context mContext;

    private ImageView imgSearch;

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public SearchItemViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        imgSearch = (ImageView)itemView.findViewById(R.id.imgSearch);

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
            //TODO
        }
    }
}