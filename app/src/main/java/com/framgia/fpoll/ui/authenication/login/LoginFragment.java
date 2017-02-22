package com.framgia.fpoll.ui.authenication.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.login.LoginManager;
import com.framgia.fpoll.R;
import com.framgia.fpoll.databinding.FragmentLoginBinding;
import com.framgia.fpoll.ui.authenication.activity.AuthenticationActivity;
import com.framgia.fpoll.util.ActivityUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Collections;

import static com.framgia.fpoll.util.Constant.BundleConstant.BUNDLE_EVENT_SWITCH_UI;
import static com.framgia.fpoll.util.Constant.DataConstant.DATA_PUBLIC_PROFILE;
import static com.framgia.fpoll.util.Constant.RequestCode.REQUEST_GOOGLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements LoginContract.View {
    private FragmentLoginBinding mBinding;
    private LoginContract.Presenter mPresenter;
    private AuthenticationActivity.EventSwitchUI mEventSwitchUI;

    public static LoginFragment getInstance(AuthenticationActivity.EventSwitchUI event) {
        LoginFragment fragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EVENT_SWITCH_UI, event);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        mPresenter = new LoginPresenter(this, new FPollGoogleApiClient(getActivity()),
            new FPollTwitterAuthClient(getActivity()));
        mBinding.setPresenter((LoginPresenter) mPresenter);
        mBinding.setHandler(new LoginActionHandler(mPresenter));
        mPresenter.initGoogle();
        mPresenter.initFacebook();
        mPresenter.initTwitter();
        return mBinding.getRoot();
    }

    public void getDataFromActivity() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mEventSwitchUI = bundle.getParcelable(BUNDLE_EVENT_SWITCH_UI);
        }
    }

    @Override
    public void start() {
        getDataFromActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            mPresenter.checkLoginGoogleResult(result);
            return;
        }
        if (mPresenter.checkLoginFacebook(requestCode, resultCode, data)) return;
        mPresenter.checkLoginTwitter(requestCode, resultCode, data);
    }

    @Override
    public void loginGoogle(GoogleApiClient googleApiClient) {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQUEST_GOOGLE);
    }

    @Override
    public void loginFacebook() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Collections.singletonList(DATA_PUBLIC_PROFILE));
    }

    @Override
    public void changeUiForgotPassword() {
        if (mEventSwitchUI == null) return;
        mEventSwitchUI.switchUiForgotPassword();
    }

    @Override
    public void changeUiRegister() {
        if (mEventSwitchUI == null) return;
        mEventSwitchUI.switchUiRegister();
    }

    @Override
    public void loginSuccess() {
        ActivityUtil.showToast(getActivity(), R.string.msg_login_success);
    }

    @Override
    public void showMessageError(int msg) {
        ActivityUtil.showToast(getActivity(), msg);
    }

    @Override
    public void loginError() {
        ActivityUtil.showToast(getActivity(), R.string.msg_login_error);
    }
}