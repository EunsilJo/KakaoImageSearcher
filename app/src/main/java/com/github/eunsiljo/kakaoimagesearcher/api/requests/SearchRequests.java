package com.github.eunsiljo.kakaoimagesearcher.api.requests;

import com.github.eunsiljo.kakaoimagesearcher.api.APIRequestVO;
import com.github.eunsiljo.kakaoimagesearcher.api.APIResponseListener;
import com.github.eunsiljo.kakaoimagesearcher.api.RequestClient;
import com.github.eunsiljo.kakaoimagesearcher.api.config.APIConfig;
import com.github.eunsiljo.kakaoimagesearcher.api.data.ErrorVO;
import com.github.eunsiljo.kakaoimagesearcher.api.data.SearchImageResult;
import com.github.eunsiljo.kakaoimagesearcher.api.services.SearchServices;
import com.github.eunsiljo.kakaoimagesearcher.api.utils.APIUtils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by xperi on 2018. 1. 6..
 */

public class SearchRequests extends Requests{

    private APIResponseListener mAPIResponseListener = null;

    private APIRequestVO<Object> mRequestItem;
    private String mUniqueID;


    public SearchRequests() {
        super();
        if (mContext == null) {
            throw new IllegalStateException("Call `Requests.init(Context..)` before calling this method.");
        }
        mUniqueID = APIUtils.getAPIRandomCode();
    }

    public SearchRequests requestSearchImageList(int page, String search) {
        return requestSearchImageList(search, page, APIConfig.SIZE_DEFAULT);
    }

    public SearchRequests requestSearchImageList(String search, int page, int size) {
        mRequestItem = new APIRequestVO<>();

        final Retrofit retrofit = new RequestClient(mContext).getClient();
        final SearchServices service = retrofit.create(SearchServices.class);
        final String uniqueID = APIUtils.getAPIRandomCode();

        mRequestItem.setCall(service.requestSearchImageList(search, page, size));
        mRequestItem.setCallback(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    SearchImageResult vo = (SearchImageResult) APIUtils.toJsonObject(response.body(), SearchImageResult.class);
                    mAPIRequestManager.successResponse(uniqueID, vo, mAPIResponseListener);
                } else {
                    ErrorVO errorVO = APIUtils.parseError(retrofit, response.errorBody());
                    mAPIRequestManager.errorResponse(uniqueID, errorVO, mAPIResponseListener);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                mAPIRequestManager.failResponse(uniqueID, t, call.isCanceled(), mAPIResponseListener);
            }
        });
        return this;
    }

    /**
     * Set api response listener
     *
     * @param listener APIResponseListener
     */
    public SearchRequests setListener(APIResponseListener listener) {
        mAPIResponseListener = listener;
        return this;
    }

    /**
     * API Request build
     */
    public void build() {
        mAPIRequestManager.addRequestCall(mUniqueID, mRequestItem);
    }
}