package com.github.eunsiljo.kakaoimagesearcher.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.facebook.appevents.AppEventsLogger;
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
import com.github.eunsiljo.kakaoimagesearcher.manager.PropertyManager;
import com.github.eunsiljo.kakaoimagesearcher.utils.SystemUtils;
import com.github.eunsiljo.kakaoimagesearcher.utils.Utils;
import com.github.eunsiljo.kakaoimagesearcher.utils.log;
import com.github.eunsiljo.kakaoimagesearcher.viewholder.OnItemClickListener;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

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

    private FirebaseAnalytics mFirebaseAnalytics;
    private Tracker mTracker;

    private FloatingActionButton fabShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mTracker = GoogleAnalytics.getInstance(this).newTracker(getString(R.string.ga_trackingId));

        SystemUtils.setStatusBarColor(SearchActivity.this, getResources().getColor(R.color.colorPrimaryDark));

        setContentView(R.layout.activity_search);
        initLayout();
        initListener();
        initRx();
        initData();

        if(PropertyManager.getInstance().getFirstOpen()) {
            PropertyManager.getInstance().setFirstOpen(false);
            checkInvitebyDynamicLink();
        }
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

        fabShare = (FloatingActionButton)findViewById(R.id.fabShare);
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

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = editSearch.getText().toString();
                if(search != null && search.length() > 0) {
                    makeInviteDynamicLink(search);
                }
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
                        String search = charSequence.toString();
                        log.d("JJO onNext - " + search);
                        filterData(search);

                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, search);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);

                        AppEventsLogger.newLogger(getApplicationContext())
                                .logEvent(FirebaseAnalytics.Event.SEARCH, bundle);

                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory(FirebaseAnalytics.Event.SEARCH)
                                .setAction(search).build());
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

    private void makeInviteDynamicLink(String userId){
        String link = "https://github.com/EunsilJo/KakaoImageSearcher/?invitedby=" + userId;
        String imageUrl = "https://lh3.googleusercontent.com/"
                + "ZkOQb0GycVmxzAN5rx2fxD8N7HhbtQ3FPOzCIPiE3uSNw_hKG_DCi9mmInY8ba_QgcFv=w3360-h1820-rw";
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(link))
            .setDynamicLinkDomain("vm2vg.app.goo.gl")
            .setAndroidParameters(
                    new DynamicLink.AndroidParameters.Builder("com.github.eunsiljo.kakaoimagesearcher")
                            .build())
            .setGoogleAnalyticsParameters(
                    new DynamicLink.GoogleAnalyticsParameters.Builder()
                            .setCampaign("test_campaign")
                            .setSource(userId)
                            .setMedium("test_medium")
                            .setTerm("test_term")
                            .build())
            .setSocialMetaTagParameters(
                    new DynamicLink.SocialMetaTagParameters.Builder()
                    .setTitle("ImageSearcher")
                    .setDescription("The sample app that helps you to search images with the kakao open api.")
                    .setImageUrl(Uri.parse(imageUrl))
                    .build())
            .buildShortDynamicLink()
            .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                @Override
                public void onSuccess(ShortDynamicLink shortDynamicLink) {
                    sendInviteDynamicLink(shortDynamicLink.getShortLink());
                }
            });
    }

    private void sendInviteDynamicLink(Uri link){
        String subject = "친구추천 이벤트 공유";
        Intent intent =  new Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_SUBJECT, subject)
                .putExtra(Intent.EXTRA_TEXT, link.toString());
        startActivity(Intent.createChooser(intent, subject));
    }

    private void checkInvitebyDynamicLink(){
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        if (deepLink != null
                                && deepLink.getBooleanQueryParameter("invitedby", false)) {
                            Utils.showAlertDialogOk(SearchActivity.this,
                                    String.format(getString(R.string.dialog_dynamic_link),
                                            deepLink.getQueryParameter("invitedby")),
                                    null);
                        }
                    }
                });
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
