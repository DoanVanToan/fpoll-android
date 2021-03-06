package com.framgia.fpoll.ui.authenication.resetpassword;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.fpoll.R;
import com.framgia.fpoll.data.model.authorization.User;
import com.framgia.fpoll.data.source.remote.login.LoginRepository;
import com.framgia.fpoll.databinding.FragmentForgotPasswordBinding;
import com.framgia.fpoll.ui.authenication.activity.AuthenticationActivity;
import com.framgia.fpoll.util.ActivityUtil;

/**
 * Created by tuanbg on 2/21/17.
 */
public class ForgotPasswordFragment extends Fragment implements ForgotPasswordContract.View {
    private ForgotPasswordPresenter mPresenter;
    private User mUser = new User();
    private FragmentForgotPasswordBinding mBinding;

    public static ForgotPasswordFragment newInstance() {
        return new ForgotPasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container,
                false);
        mPresenter = new ForgotPasswordPresenter(this, mUser,
                LoginRepository.getInstance(getActivity()));
        mBinding.setHandler((new ForgotPasswordHandler(mPresenter)));
        mBinding.setPresenter(mPresenter);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start();
    }

    @Override
    public void showMessageError() {
        ActivityUtil.showToast(getActivity(), R.string.msg_email_invalidate);
    }

    @Override
    public void showMessage(String message) {
        ActivityUtil.showToast(getActivity(), message);
    }

    @Override
    public void showDialog() {
        ((AuthenticationActivity) getActivity()).showProgressDialog();
    }

    @Override
    public void dismissDialog() {
        ((AuthenticationActivity) getActivity()).hideProgressDialog();
    }

    @Override
    public void start() {
    }
}
