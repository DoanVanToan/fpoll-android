package com.framgia.fpoll.data.source.remote.feedback;

import android.content.Context;
import android.support.annotation.NonNull;

import com.framgia.fpoll.data.ApiRestClient.APIService.FeedbackAPI;
import com.framgia.fpoll.data.ApiRestClient.APIService.ResponseItem;
import com.framgia.fpoll.data.ApiRestClient.CallbackManager;
import com.framgia.fpoll.data.ApiRestClient.ServiceGenerator;
import com.framgia.fpoll.data.source.DataCallback;

/**
 * Created by Nhahv0902 on 3/6/2017.
 * <></>
 */
public class FeedbackRemoteDataSource implements FeedbackDataSource {
    private static FeedbackDataSource sDataSource;
    private Context mContext;

    private FeedbackRemoteDataSource(Context context) {
        mContext = context;
    }

    public static FeedbackDataSource getInstance(Context context) {
        if (sDataSource == null) sDataSource = new FeedbackRemoteDataSource(context);
        return sDataSource;
    }

    @Override
    public void sendFeedback(String name, String email, String content,
                             @NonNull final DataCallback<String> callback) {
        FeedbackAPI.FeedbackBody body = new FeedbackAPI.FeedbackBody(name, email, content);
        ServiceGenerator.createService(FeedbackAPI.class).feedback(body).enqueue(
            new CallbackManager<>(mContext, new CallbackManager.CallBack<ResponseItem<String>>() {
                @Override
                public void onResponse(ResponseItem<String> data) {
                    if (data != null) callback.onSuccess(data.getData());
                }

                @Override
                public void onFailure(String message) {
                    callback.onError(message);
                }
            }));
    }
}