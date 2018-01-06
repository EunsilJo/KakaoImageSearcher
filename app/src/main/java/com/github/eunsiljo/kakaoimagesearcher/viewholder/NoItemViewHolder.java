package com.github.eunsiljo.kakaoimagesearcher.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.eunsiljo.kakaoimagesearcher.R;
import com.github.eunsiljo.kakaoimagesearcher.data.NoItemData;


/**
 * Created by EunsilJo on 2017. 8. 10..
 */

public class NoItemViewHolder extends RecyclerView.ViewHolder{

    private NoItemData mNoItemData;
    private Context mContext;

    private TextView txtDefault;


    public NoItemViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        txtDefault = (TextView)itemView.findViewById(R.id.txtDefault);
    }

    public void setNoItem(NoItemData noItem) {
        if(noItem != null) {
            mNoItemData = noItem;
            txtDefault.setText(noItem.getText());
        }
    }
}
