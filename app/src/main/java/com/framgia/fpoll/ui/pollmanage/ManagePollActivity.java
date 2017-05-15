package com.framgia.fpoll.ui.pollmanage;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import com.framgia.fpoll.R;
import com.framgia.fpoll.data.model.DataInfoItem;
import com.framgia.fpoll.databinding.ActivityManagePollBinding;
import com.framgia.fpoll.ui.base.BaseActivity;
import com.framgia.fpoll.ui.history.ViewPagerAdapter;
import com.framgia.fpoll.ui.pollmanage.action.EditPollFragment;
import com.framgia.fpoll.ui.pollmanage.information.PollInformationFragment;
import com.framgia.fpoll.ui.pollmanage.result.ResultVoteFragment;
import java.util.ArrayList;
import java.util.List;
import retrofit2.http.HEAD;

import static com.framgia.fpoll.util.Constant.BundleConstant.BUNDLE_POLL_ITEM;
import static com.framgia.fpoll.util.Constant.BundleConstant.BUNDLE_TOKEN;
import static com.framgia.fpoll.util.Constant.WebUrl.OPTION_TITLE;

public class ManagePollActivity extends BaseActivity implements ManagePollContract.View {
    private ActivityManagePollBinding mBinding;
    private ManagePollContract.Presenter mPresenter;
    private DataInfoItem mPoll;
    private String mToken;
    private ViewPagerAdapter mAdapter;
    private boolean mIsUpdatePoll;

    private PollInformationFragment mInformationFragment;
    private ResultVoteFragment mVoteFragment;
    private EditPollFragment mEditPollFragment;

    public static Intent getPollIntent(Context context, DataInfoItem poll) {
        Intent intent = new Intent(context, ManagePollActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_POLL_ITEM, poll);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getTokenIntent(Context context, String token) {
        Intent intent = new Intent(context, ManagePollActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TOKEN, token);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_manage_poll);
        getDataFromIntent();
        mPresenter = new ManagePollPresenter(this);
        mBinding.setView(this);
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        if (bundle.getString(BUNDLE_TOKEN) != null) {
            mToken = bundle.getString(BUNDLE_TOKEN);
        }
        if (bundle.getParcelable(BUNDLE_POLL_ITEM) != null) {
            mPoll = bundle.getParcelable(BUNDLE_POLL_ITEM);
            if (mPoll != null && mPoll.getPoll() != null && mPoll.getPoll().getLink().size() > 0) {
                mToken = mPoll.getPoll().getLink().get(OPTION_TITLE).getToken();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void start() {
        List<Fragment> fragments = new ArrayList<>();
        mInformationFragment = PollInformationFragment.newInstance(mPoll, mToken);
        fragments.add(mInformationFragment);
        mVoteFragment = ResultVoteFragment.newInstance(mToken);
        fragments.add(mVoteFragment);
        mEditPollFragment = EditPollFragment.newInstance(mToken);
        fragments.add(mEditPollFragment);
        String[] titles = getResources().getStringArray(R.array.array_manage);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
    }

    public void setUpdatePoll(boolean isUpdatePoll) {
        mIsUpdatePoll = isUpdatePoll;
    }

    @Override
    public void onBackPressed() {
        if (mIsUpdatePoll) setResult(RESULT_OK);
        super.onBackPressed();
    }

    public ViewPagerAdapter getAdapter() {
        return mAdapter;
    }

    public void updateData() {
        if (mInformationFragment != null) mInformationFragment.loadData();
        if (mVoteFragment != null) mVoteFragment.loadData();
        if (mEditPollFragment != null) mEditPollFragment.loadData();
    }
}
