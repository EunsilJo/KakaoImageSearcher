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
import com.github.eunsiljo.kakaoimagesearcher.api.data.SearchImageResult;
import com.github.eunsiljo.kakaoimagesearcher.api.requests.SearchRequests;
import com.github.eunsiljo.kakaoimagesearcher.api.utils.APIUtils;
import com.github.eunsiljo.kakaoimagesearcher.data.NoItemData;
import com.github.eunsiljo.kakaoimagesearcher.utils.SystemUtils;
import com.github.eunsiljo.kakaoimagesearcher.utils.Utils;
import com.github.eunsiljo.kakaoimagesearcher.viewholder.OnItemClickListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

public class SearchActivity extends BaseActivity {

    private View progressBar;
    private EditText editSearch;

    private RecyclerView mListView;
    private SearchAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private SwipeRefreshLayout refreshView;


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

        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO
            }
        });
    }

    private void initData(){
        //loadData("kakao");
    }

    private void loadData(String search){
        if(!refreshView.isRefreshing()) {
            startLoading();
        }
        new SearchRequests().requestSearchImageList(search)
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
