package com.github.eunsiljo.kakaoimagesearcher.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.eunsiljo.kakaoimagesearcher.R;
import com.github.eunsiljo.kakaoimagesearcher.config.Tags;
import com.github.eunsiljo.kakaoimagesearcher.data.NoItemData;
import com.github.eunsiljo.kakaoimagesearcher.data.SearchItemData;
import com.github.eunsiljo.kakaoimagesearcher.viewholder.CommonFooterViewHolder;
import com.github.eunsiljo.kakaoimagesearcher.viewholder.NoItemViewHolder;
import com.github.eunsiljo.kakaoimagesearcher.viewholder.OnItemClickListener;
import com.github.eunsiljo.kakaoimagesearcher.viewholder.SearchItemViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by EunsilJo on 2017. 8. 8..
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<SearchItemData> items = new ArrayList<SearchItemData>();
    private boolean isShowFooter = false;
    private boolean isShowDefault = false;
    private NoItemData mNoItemData;

    public void setNoItemData(NoItemData noItem) {
        this.mNoItemData = noItem;
    }

    public SearchItemData getItem(int position){
        return items.get(position);
    }

    public boolean isShowFooter() {
        return isShowFooter;
    }

    public void setShowFooter(boolean showFooter) {
        isShowFooter = showFooter;
        notifyDataSetChanged();
    }

    public boolean isShowDefault() {
        return isShowDefault;
    }

    public void setShowDefault(boolean showDefault) {
        isShowDefault = showDefault;
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void remove(int position) {
        items.remove(position);
        notifyDataSetChanged();
    }

    public void add(SearchItemData SearchItemData) {
        items.add(SearchItemData);
        notifyDataSetChanged();
    }

    public void addAll(List<SearchItemData> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (getRealItemCount() == 0 && isShowDefault) {
            return Tags.SEARCH_VIEW_TYPE.SEARCH_DEFAULT;
        }else {
            if(position == items.size() && isShowFooter){
                return Tags.SEARCH_VIEW_TYPE.SEARCH_FOOTER;
            }else{
                return Tags.SEARCH_VIEW_TYPE.SEARCH_ITEM;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;

        switch (viewType){
            case Tags.SEARCH_VIEW_TYPE.SEARCH_DEFAULT:
                view = inflater.inflate(R.layout.view_no_item, parent, false);
                return new NoItemViewHolder(view);
            case Tags.SEARCH_VIEW_TYPE.SEARCH_FOOTER:
                view = inflater.inflate(R.layout.view_common_footer, parent, false);
                return new CommonFooterViewHolder(view);
            case Tags.SEARCH_VIEW_TYPE.SEARCH_ITEM:
                view = inflater.inflate(R.layout.view_search_item, parent, false);
                return new SearchItemViewHolder(view);
        }
        throw new IllegalArgumentException("invalid position");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case Tags.SEARCH_VIEW_TYPE.SEARCH_DEFAULT:
                NoItemViewHolder noItem = (NoItemViewHolder)holder;
                noItem.setNoItem(mNoItemData);
                break;
            case Tags.SEARCH_VIEW_TYPE.SEARCH_FOOTER:
                CommonFooterViewHolder footer = (CommonFooterViewHolder)holder;
                break;
            case Tags.SEARCH_VIEW_TYPE.SEARCH_ITEM:
                SearchItemViewHolder item = (SearchItemViewHolder)holder;
                item.setSearchItem(items.get(position));
                item.setOnItemClickListener(mListener);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(getRealItemCount() == 0 && isShowDefault){
            return 1;
        }else{
            if(getRealItemCount() >= 0 && isShowFooter){
                return items.size() + 1;
            }else{
                return items.size();
            }
        }
    }

    public int getRealItemCount(){
        return items.size();
    }
}
