package com.framgia.fpoll.ui.pollmanage.action;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.fpoll.R;
import com.framgia.fpoll.data.model.DataInfoItem;
import com.framgia.fpoll.data.model.PollItem;
import com.framgia.fpoll.data.source.remote.pollmanager.ManagerRepository;
import com.framgia.fpoll.databinding.FragmentActionBinding;
import com.framgia.fpoll.ui.pollcreation.PollCreationActivity;
import com.framgia.fpoll.ui.polledition.ModifyPollActivity;
import com.framgia.fpoll.ui.pollmanage.history.HistoryManageActivity;
import com.framgia.fpoll.util.ActivityUtil;
import com.framgia.fpoll.util.SharePreferenceUtil;
import com.framgia.fpoll.widget.FPollProgressDialog;

import static com.framgia.fpoll.util.Constant.BundleConstant.BUNDLE_POLL_ITEM;
import static com.framgia.fpoll.util.Constant.BundleConstant.BUNDLE_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPollFragment extends Fragment implements EditPollContract.View {
    private FragmentActionBinding mBinding;
    private EditPollContract.Presenter mPresenter;
    private String mToken;
    private DataInfoItem mPoll;
    private FPollProgressDialog mDialog;

    public static EditPollFragment newInstance(String token) {
        EditPollFragment fragment = new EditPollFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TOKEN, token);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void getDataFromActivity() {
        Bundle bundle = getArguments();
        if (bundle.getString(BUNDLE_TOKEN) != null) {
            mToken = bundle.getString(BUNDLE_TOKEN);
        }
        if (bundle.getParcelable(BUNDLE_POLL_ITEM) != null) {
            mPoll = bundle.getParcelable(BUNDLE_POLL_ITEM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_action, container, false);
        getDataFromActivity();
        mPresenter = new EditPollPresenter(this, ManagerRepository.getInstance(getActivity()),
                SharePreferenceUtil.getIntances(getActivity()), mToken, mPoll);
        mBinding.setPresenter((EditPollPresenter) mPresenter);
        mBinding.setHandler(new EditPollHandler(mPresenter));
        if (mToken != null) mPresenter.loadData();
        if (mPoll != null) mPresenter.initData();
        return mBinding.getRoot();
    }

    @Override
    public void start() {
    }

    @Override
    public void showProgressDialog() {
        if (mDialog == null) mDialog = new FPollProgressDialog(getActivity());
        mDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
    }

    @Override
    public void showMessage(String msg) {
        ActivityUtil.showToast(getActivity(), msg);
    }

    @Override
    public void showMessage(int msg) {
        ActivityUtil.showToast(getActivity(), msg);
    }

    @Override
    public void startUiPollCreation(PollItem data) {
        startActivity(PollCreationActivity.getIntent(getActivity(), data));
    }

    @Override
    public void viewHistory() {
        startActivity(HistoryManageActivity.getInstance(getActivity(), mToken));
    }

    @Override
    public void setResult() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void startModifyPoll(PollItem poll) {
        startActivity(ModifyPollActivity.getModifyIntent(getActivity(), poll));
    }

    public void loadData() {
        if (mPresenter != null) mPresenter.loadData();
    }
}
