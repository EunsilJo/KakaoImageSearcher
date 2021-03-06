package com.github.eunsiljo.kakaoimagesearcher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.github.eunsiljo.kakaoimagesearcher.R;
import com.github.eunsiljo.kakaoimagesearcher.adapter.SearchAdapter;
import com.github.eunsiljo.kakaoimagesearcher.api.APIRequestManager;
import com.github.eunsiljo.kakaoimagesearcher.api.APIResponseListener;
import com.github.eunsiljo.kakaoimagesearcher.api.data.ImageItemVO;
import com.github.eunsiljo.kakaoimagesearcher.api.data.SearchImageResult;
import com.github.eunsiljo.kakaoimagesearcher.api.requests.SearchRequests;
import com.github.eunsiljo.kakaoimagesearcher.api.utils.APIUtils;
import com.github.eunsiljo.kakaoimagesearcher.config.Tags;
import com.github.eunsiljo.kakaoimagesearcher.data.NoItemData;
import com.github.eunsiljo.kakaoimagesearcher.data.SearchItemData;
import com.github.eunsiljo.kakaoimagesearcher.utils.SystemUtils;
import com.github.eunsiljo.kakaoimagesearcher.utils.log;
import com.github.eunsiljo.kakaoimagesearcher.viewholder.OnItemClickListener;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


public class SearchActivity extends BaseActivity {

    private View progressBar;
    private EditText editSearch;

    private RecyclerView mListView;
    private SearchAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private SwipeRefreshLayout refreshView;

    private int mListPage = 1;
    private boolean isLast = false;

    private ArrayList<String> mSearchIDs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SystemUtils.setStatusBarColor(SearchActivity.this, getResources().getColor(R.color.colorPrimaryDark));

        setContentView(R.layout.activity_search);
        initLayout();
        initListener();
        initRx();
        initData();
    }

    private void initLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        editSearch = (EditText) findViewById(R.id.editSearch);

        mAdapter = new SearchAdapter();
        mListView = (RecyclerView) findViewById(R.id.recyclerSearch);
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mListView.setLayoutManager(mLayoutManager);
        mAdapter.setNoItemData(new NoItemData(getString(R.string.no_search)));

        progressBar = findViewById(R.id.progressBar);
        CircleProgressBar circleProgressBar = (CircleProgressBar)progressBar.findViewById(R.id.circleProgressBar);
        circleProgressBar.setColorSchemeColors(getResources().getColor(R.color.color_accent));

        refreshView = (SwipeRefreshLayout)findViewById(R.id.refreshView);
        refreshView.setColorSchemeColors(getResources().getColor(R.color.color_accent));
    }

    private void initListener() {
        /*
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                log.d("JJO beforeTextChanged - " + s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                log.d("JJO onTextChanged - " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                log.d("JJO afterTextChanged - " + s.toString());
                filterData(s.toString());
            }
        });
        */

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageItemVO image = mAdapter.getItem(position).getImage();
                if(image != null){
                    Intent intent = new Intent(SearchActivity.this, PhotoViewActivity.class);
                    intent.putExtra(Tags.TAG_IMAGE, image);
                    startActivity(intent);
                }
            }
        });

        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(isLast){
                        // more data...
                        if(mAdapter.isShowFooter()) {
                            loadData(++mListPage, editSearch.getText().toString());
                        }
                    }
                }else if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    SystemUtils.hideSoftKeyboard(SearchActivity.this);
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
                filterData(editSearch.getText().toString());
            }
        });
    }

    private void initRx(){
        RxTextView.textChanges(editSearch)
                .skip(1)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CharSequence>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        log.d("JJO onNext - " + charSequence.toString());
                        filterData(charSequence.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void initData(){
        mAdapter.setShowFooter(false);
        mAdapter.setShowDefault(false);
        mAdapter.clear();
        mListPage = 1;
    }

    private void loadData(int page, String search){
        startLoading();
        APIRequestManager.getInstance().cancelAllRequest(mSearchIDs, true);

        if(search != null && search.length() > 0) {
            SearchRequests request = new SearchRequests().requestSearchImageList(page, search)
                    .setListener(new APIResponseListener() {
                        @Override
                        public void onSuccess(Object vo) {
                            stopLoading();
                            setData((SearchImageResult) vo);
                        }

                        @Override
                        public void onFail(Object error, boolean canceled) {
                            stopLoading();
                            if (!canceled) {
                                APIUtils.onError(getApplicationContext(), error);
                            }
                        }
                    });
            request.build();
            mSearchIDs.add(request.getUniqueID());
        }else{
            stopLoading();
        }
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
        mAdapter.setShowFooter(!result.getMeta().isIs_end());
    }

    private void filterData(String search){
        mAdapter.setShowFooter(false);
        mAdapter.setShowDefault(false);
        mAdapter.clear();
        mListPage = 1;

        loadData(mListPage, search);
    }

    private void refresh() {
        initData();
    }

    @Override
    public void startLoading(){
        if(!refreshView.isRefreshing() && !mAdapter.isShowFooter()) {
            startProgressLoading();
        }
    }

    @Override
    public void stopLoading(){
        if (isProgressLoading()) {
            stopProgressLoading();
        } else if (refreshView.isRefreshing()) {
            refreshView.setRefreshing(false);
        }
    }

    private void startProgressLoading(){
        if(progressBar != null && progressBar.getVisibility() != View.VISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void stopProgressLoading(){
        if(progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private boolean isProgressLoading(){
        return progressBar != null && (progressBar.getVisibility() == View.VISIBLE);
    }
}
