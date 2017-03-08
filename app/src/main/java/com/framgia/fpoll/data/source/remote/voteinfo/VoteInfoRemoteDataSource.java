package com.framgia.fpoll.data.source.remote.voteinfo;

import android.content.Context;
import android.support.annotation.NonNull;

import com.framgia.fpoll.data.ApiRestClient.APIService.ResponseItem;
import com.framgia.fpoll.data.ApiRestClient.APIService.VoteInfoAPI;
import com.framgia.fpoll.data.ApiRestClient.CallbackManager;
import com.framgia.fpoll.data.ApiRestClient.ServiceGenerator;
import com.framgia.fpoll.data.model.voteinfo.VoteInfo;
import com.framgia.fpoll.data.source.DataCallback;

/**
 * Created by anhtv on 07/03/2017.
 */
public class VoteInfoRemoteDataSource implements VoteInfoDataSource {
    private static VoteInfoRemoteDataSource sVoteInfoRemoteDataSource;
    private Context mContext;

    private VoteInfoRemoteDataSource(Context context) {
        mContext = context;
    }

    public static VoteInfoRemoteDataSource getInstance(Context context) {
        if (sVoteInfoRemoteDataSource == null) sVoteInfoRemoteDataSource = new
            VoteInfoRemoteDataSource(context);
        return sVoteInfoRemoteDataSource;
    }

    @Override
    public void getVoteInfo(String token, @NonNull final DataCallback<VoteInfo> callback) {
        ServiceGenerator.createService(VoteInfoAPI.class).showVoteInfo(token).enqueue(
            new CallbackManager<>(mContext, new CallbackManager.CallBack<ResponseItem<VoteInfo>>() {
                @Override
                public void onResponse(ResponseItem<VoteInfo> data) {
                    if (data != null) callback.onSuccess(data.getData());
                }

                @Override
                public void onFailure(String message) {
                    callback.onError(message);
                }
            }));
    }
}