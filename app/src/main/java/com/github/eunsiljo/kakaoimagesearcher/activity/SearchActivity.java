package com.github.eunsiljo.kakaoimagesearcher.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.eunsiljo.kakaoimagesearcher.R;
import com.github.eunsiljo.kakaoimagesearcher.adapter.SearchAdapter;
import com.github.eunsiljo.kakaoimagesearcher.api.APIResponseListener;
import com.github.eunsiljo.kakaoimagesearcher.api.data.ErrorVO;
import com.github.eunsiljo.kakaoimagesearcher.api.data.ImageItemVO;
import com.github.eunsiljo.kakaoimagesearcher.api.data.SearchImageResult;
import com.github.eunsiljo.kakaoimagesearcher.api.requests.SearchRequests;
import com.github.eunsiljo.kakaoimagesearcher.api.utils.APIUtils;
import com.github.eunsiljo.kakaoimagesearcher.data.NoItemData;
import com.github.eunsiljo.kakaoimagesearcher.data.SearchItemData;
import com.github.eunsiljo.kakaoimagesearcher.utils.SystemUtils;
import com.github.eunsiljo.kakaoimagesearcher.utils.Utils;
import com.github.eunsiljo.kakaoimagesearcher.viewholder.OnItemClickListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity {

    private View progressBar;
    private EditText editSearch;

    private RecyclerView mListView;
    private SearchAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private SwipeRefreshLayout refreshView;

    private int mListPage = 1;
    private boolean isLast = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SystemUtils.setStatusBarColor(SearchActivity.this, getResources().getColor(R.color.colorPrimaryDark));

        setContentView(R.layout.activity_search);
        initLayout();
        initListener();
        initData();
    }

    private void initLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressBar);
        editSearch = (EditText) findViewById(R.id.editSearch);

        mAdapter = new SearchAdapter();
        mListView = (RecyclerView) findViewById(R.id.recyclerSearch);
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mListView.setLayoutManager(mLayoutManager);
        mAdapter.setNoItemData(new NoItemData(getString(R.string.no_search)));

        refreshView = (SwipeRefreshLayout)findViewById(R.id.refreshView);
        refreshView.setEnabled(false);
    }

    private void initListener() {

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //TODO
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //TODO
            }
        });

        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isLast && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // more data...
                    if(mAdapter.isShowFooter()) {
                        loadData(++mListPage, editSearch.getText().toString());
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mAdapter.getItemCount() -1 <= mLayoutManager.findLastVisibleItemPosition()) {
                    isLast = true;
                } else {
                    isLast = false;
                }
            }
        });

        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String search = editSearch.getText().toString();
                if(search != null && search.length() > 0){
                    filterData(search);
                }
            }
        });
    }

    private void initData(){
        mAdapter.setShowFooter(false);
        mAdapter.setShowDefault(false);
        mAdapter.clear();
        mListPage = 1;

        //loadData(mListPage, "kakao");
    }

    private void loadData(int page, String search){
        if(!refreshView.isRefreshing()) {
            startLoading();
        }
        new SearchRequests().requestSearchImageList(page, search)
                .setListener(new APIResponseListener() {
                    @Override
                    public void onSuccess(Object vo) {
                        if(!refreshView.isRefreshing()) {
                            stopLoading();
                        }else{
                            refreshView.setRefreshing(false);
                        }
                        setData((SearchImageResult)vo);
                    }

                    @Override
                    public void onFail(Object error) {
                        if(!refreshView.isRefreshing()) {
                            stopLoading();
                        }else{
                            refreshView.setRefreshing(false);
                        }
                        APIUtils.onError(getApplicationContext(), error);
                    }
                }).build();
    }

    private void setData(SearchImageResult result) {
        if (result == null) {
            return;
        }
        ArrayList<SearchItemData> items = new ArrayList<>();
        ArrayList<ImageItemVO> images = result.getDocuments();
        if(images != null && images.size() > 0){
            for(ImageItemVO image : images){
                items.add(new SearchItemData(image));
            }
            mAdapter.addAll(items);
        }else{
            if(mListPage == 1){
                mAdapter.setShowDefault(true);
            }
        }
        mAdapter.setShowFooter(result.getMeta().isIs_end());
    }

    private void filterData(String search){
        mAdapter.setShowFooter(false);
        mAdapter.setShowDefault(false);
        mAdapter.clear();
        mListPage = 1;

        loadData(mListPage, search);
    }

    public void refresh() {
        initData();
    }

    @Override
    public void startLoading(){
        if(progressBar != null && progressBar.getVisibility() != View.VISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void stopLoading(){
        if(progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }
}
