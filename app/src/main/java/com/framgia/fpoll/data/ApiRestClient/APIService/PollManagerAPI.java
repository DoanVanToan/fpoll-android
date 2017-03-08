package com.framgia.fpoll.data.ApiRestClient.APIService;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

/**
 * Created by Nhahv0902 on 3/7/2017.
 * <></>
 */
public interface PollManagerAPI {
    @DELETE("api/v1/poll/{id}")
    Call<ResponseItem> switchPollStatus(@Path("id") String id);
    @DELETE("api/v1/poll/participants/{token}")
    Call<ResponseItem> deleteVoting(@Path("token") String token);
}