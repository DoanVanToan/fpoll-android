package com.framgia.fpoll.ui.polledition;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import com.framgia.fpoll.R;
import com.framgia.fpoll.data.model.PollItem;
import com.framgia.fpoll.data.source.remote.polldatasource.PollRepository;
import com.framgia.fpoll.databinding.ActivityModifyPollBinding;
import com.framgia.fpoll.ui.base.BaseActivity;
import com.framgia.fpoll.ui.pollcreation.PollCreationType;
import com.framgia.fpoll.ui.polledition.editinformation.EditInforFragment;
import com.framgia.fpoll.ui.polledition.editoption.EditOptionFragment;
import com.framgia.fpoll.ui.polledition.editsetting.EditSettingFragment;
import com.framgia.fpoll.util.ActivityUtil;

import static com.framgia.fpoll.ui.pollcreation.PollCreationType.OPTION;
import static com.framgia.fpoll.ui.pollcreation.PollCreationType.SETTING;
import static com.framgia.fpoll.util.Constant.BundleConstant.BUNDLE_POLL_ITEM;

/**
 * Created by framgia on 15/03/2017.
 */
public class ModifyPollActivity extends BaseActivity implements ModifyPollContract.View {
    private ActivityModifyPollBinding mBinding;
    private ModifyPollContract.Presenter mPresenter;
    private PollItem mPoll;
    private PollCreationType mType = PollCreationType.INFORMATION;
    private EditInforFragment mInformationFragment;
    private EditOptionFragment mOptionFragment;
    private EditSettingFragment mSettingFragment;
    private boolean mIsUpdatePoll;

    public static Intent getModifyIntent(Context context, PollItem data) {
        Intent intent = new Intent(context, ModifyPollActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_POLL_ITEM, data);
        intent.putExtras(bundle);
        return intent;
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.getParcelable(BUNDLE_POLL_ITEM) == null) return;
        mPoll = bundle.getParcelable(BUNDLE_POLL_ITEM);
        if (mPoll == null) mPoll = new PollItem();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_modify_poll);
        getDataFromIntent();
        mPresenter = new ModifyPollPresenter(this, mPoll, PollRepository.getInstance(this));
        mBinding.setPresenter((ModifyPollPresenter) mPresenter);
        mBinding.setActivity(this);
        addInformation();
    }

    @Override
    public void start() {
        setSupportActionBar(mBinding.layoutToolbar.toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modify, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_information:
                addInformation();
                break;
            case R.id.action_option:
                addOption();
                break;
            case R.id.action_setting:
                addSetting();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addFragment(Fragment fragment) {
        ActivityUtil.addFragment(getSupportFragmentManager(), fragment, R.id.frame_layout);
    }

    private void addInformation() {
        if (mInformationFragment == null) {
            mInformationFragment = EditInforFragment.newInstance(mPoll);
        }
        addFragment(mInformationFragment);
        mType = PollCreationType.INFORMATION;
        setTitle(R.string.title_information);
    }

    private void addOption() {
        if (mOptionFragment == null) {
            mOptionFragment = EditOptionFragment.newInstance(mPoll);
        }
        addFragment(mOptionFragment);
        mType = OPTION;
        setTitle(R.string.title_option);
    }

    private void addSetting() {
        if (mSettingFragment == null) {
            mSettingFragment = EditSettingFragment.newInstance(mPoll);
        }
        addFragment(mSettingFragment);
        mType = SETTING;
        setTitle(R.string.title_setting);
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void submitEditPoll() {
        switch (mType) {
            case INFORMATION:
                mPresenter.updateInformation();
                break;
            case OPTION:
                mPresenter.updateOption();
                break;
            case SETTING:
                mPresenter.updateSetting();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mIsUpdatePoll) {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }

    @Override
    public void notifyUI(PollItem poll) {
        if (mOptionFragment != null) mOptionFragment.notifyUI(poll);
    }

    @Override
    public void setUpdatePoll(boolean isUpdatePoll) {
        mIsUpdatePoll = isUpdatePoll;
    }

    @Override
    public void showMessage(String message) {
        ActivityUtil.showToast(getApplicationContext(), message);
    }

    @Override
    public void showMessage(int message) {
        ActivityUtil.showToast(getApplicationContext(), message);
    }
}
