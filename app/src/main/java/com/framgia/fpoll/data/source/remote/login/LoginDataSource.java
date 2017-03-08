package com.framgia.fpoll.data.source.remote.login;

import com.android.annotations.NonNull;
import com.framgia.fpoll.data.model.LoginNormalData;
import com.framgia.fpoll.data.model.SocialData;
import com.framgia.fpoll.data.source.DataCallback;

/**
 * Created by Nhahv0902 on 3/3/2017.
 * <></>
 */
public interface LoginDataSource {
    void loginSocial(String provider, String token, @NonNull DataCallback<SocialData> callback);
    void loginNormal(String email, String password,
                     @NonNull DataCallback<LoginNormalData> callback);
    void logout(String header, @NonNull DataCallback<String> callback);
}