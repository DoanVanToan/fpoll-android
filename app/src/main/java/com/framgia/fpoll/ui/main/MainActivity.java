package com.framgia.fpoll.ui.main;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.framgia.fpoll.R;
import com.framgia.fpoll.data.model.PollItem;
import com.framgia.fpoll.data.model.poll.HistoryPoll;
import com.framgia.fpoll.data.source.remote.login.LoginRepository;
import com.framgia.fpoll.data.source.remote.settings.SettingRepository;
import com.framgia.fpoll.databinding.ActivityMainBinding;
import com.framgia.fpoll.ui.authenication.activity.AuthenticationActivity;
import com.framgia.fpoll.ui.profile.ProfileActivity;
import com.framgia.fpoll.ui.profile.ProfileContract;
import com.framgia.fpoll.ui.profile.editprofile.EditProfileActivity;
import com.framgia.fpoll.ui.feedback.FeedbackFragment;
import com.framgia.fpoll.ui.history.HistoryFragment;
import com.framgia.fpoll.ui.introduction.IntroduceActivity;
import com.framgia.fpoll.ui.joinpoll.JoinPollActivity;
import com.framgia.fpoll.ui.pollcreation.PollCreationActivity;
import com.framgia.fpoll.util.ActivityUtil;
import com.framgia.fpoll.util.Constant;
import com.framgia.fpoll.util.LanguageUtil;
import com.framgia.fpoll.util.SharePreferenceUtil;
import com.framgia.fpoll.widget.FPollProgressDialog;

import static com.framgia.fpoll.util.Constant.BundleConstant.BUNDLE_POLL_ITEM;
import static com.framgia.fpoll.util.Constant.Language.LANGUAGE_EN;
import static com.framgia.fpoll.util.Constant.Language.LANGUAGE_JP;
import static com.framgia.fpoll.util.Constant.Language.LANGUAGE_VN;
import static com.framgia.fpoll.util.Constant.RequestCode.REQUEST_CREATE_POLL;
import static com.framgia.fpoll.util.Constant.RequestCode.REQUEST_LOGIN;
import static com.framgia.fpoll.util.Constant.RequestCode.REQUEST_PROFILE_DETAIL;

public class MainActivity extends AppCompatActivity
        implements MainContract.View, NavigationView.OnNavigationItemSelectedListener {
    private static final int NO_ANIMATION = 0;
    private final ObservableBoolean mIsShowAddPoll = new ObservableBoolean(true);
    private MainContract.Presenter mPresenter;
    private ActivityMainBinding mBinding;
    private DrawerLayout mDrawerLayout;
    private FPollProgressDialog mProgressDialog;

    public static Intent getInstance(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mPresenter = new MainPresenter(this, LoginRepository.getInstance(getApplicationContext()),
                SettingRepository.getInstance(getApplicationContext()),
                SharePreferenceUtil.getIntances(this));
        mBinding.setPresenter((MainPresenter) mPresenter);
        mBinding.setHandler(new MainHandler(mPresenter));
        mBinding.setView(this);
    }

    @Override
    public void start() {
        Toolbar toolbar = mBinding.toolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = mBinding.drawerLayout;
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.action_open,
                        R.string.action_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mBinding.navView.setNavigationItemSelectedListener(this);
        addFragment(HistoryFragment.newInstance(), R.string.title_home);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new FPollProgressDialog(this);
        }
        if (!mProgressDialog.isShowing()) mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.hide();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                setIsShowAddPoll(false);
                startActivityForResult(AuthenticationActivity.getAuthenticationIntent(this, true),
                        REQUEST_LOGIN);
                break;
            case R.id.action_home:
                addHomeFragment();
                break;
            case R.id.action_join_poll:
                startActivity(JoinPollActivity.getIntent(this));
                break;
            case R.id.action_guide:
                setIsShowAddPoll(false);
                showHelp();
                break;
            case R.id.action_feedback:
                setIsShowAddPoll(false);
                addFragment(FeedbackFragment.newInstance(), R.string.title_feedback);
                break;
            case R.id.action_introduce:
                setIsShowAddPoll(false);
                startActivity(IntroduceActivity.getInstance(this, true));
                break;
            case R.id.action_log_out:
                setIsShowAddPoll(false);
                mPresenter.logout();
                break;
            case R.id.action_english:
                String lang = LANGUAGE_EN;
                showConfirmDialog(lang);
                break;
            case R.id.action_vietnamese:
                lang = LANGUAGE_VN;
                showConfirmDialog(lang);
                break;
            case R.id.action_japanese:
                lang = LANGUAGE_JP;
                showConfirmDialog(lang);
                break;
            default:
                break;
        }
        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addHomeFragment() {
        setIsShowAddPoll(true);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (!(fragment instanceof HistoryFragment)) {
            addFragment(HistoryFragment.newInstance(), R.string.title_home);
        }
    }

    public void changeLang(String lang) {
        mPresenter.changeLanguage(lang);
        LanguageUtil.changeLang(lang, MainActivity.this);
        finish();
        overridePendingTransition(NO_ANIMATION, NO_ANIMATION);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    public void addFragment(Fragment fragment, int title) {
        ActivityUtil.addFragment(getSupportFragmentManager(), fragment, R.id.frame_layout);
        setTitle(title);
    }

    @Override
    public void showMessage(String msg) {
        ActivityUtil.showToast(getApplicationContext(), msg);
    }

    @Override
    public void showHelp() {
        Uri helpUri = Uri.parse(Constant.WebUrl.HELP_URL);
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.color_teal_500))
                .setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.color_teal_800))
                .build()
                .launchUrl(this, helpUri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_LOGIN:
                setIsShowAddPoll(true);
                addFragment(HistoryFragment.newInstance(), R.string.title_home);
                mPresenter.setInformation();
                openNavigation();
                break;
            case REQUEST_CREATE_POLL:
                if (data == null) return;
                HistoryPoll poll = data.getExtras().getParcelable(BUNDLE_POLL_ITEM);
                if (poll == null) return;
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
                if (fragment != null && fragment instanceof HistoryFragment) {
                    ((HistoryFragment) fragment).updatePollHistory(poll);
                }
                break;
            case REQUEST_PROFILE_DETAIL:
                // TODO: 20/04/2017 update profile navigation 
                break;
            default:
                break;
        }
    }

    private void openNavigation() {
        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void startUIPollCreation() {
        startActivityForResult(PollCreationActivity.getIntent(this, new PollItem()),
                REQUEST_CREATE_POLL);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void startUiProfileEdition() {
        startActivityForResult(ProfileActivity.getInstance(this), Constant.RequestCode.REQUEST_PROFILE_DETAIL);
    }

    @Override
    public void changeLangStatus(String message) {
        ActivityUtil.showToast(this, message);
    }

    private void showConfirmDialog(final String lang) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this).setCancelable(true)
                .setTitle(R.string.title_change_language)
                .setMessage(R.string.msg_change_language)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        changeLang(lang);
                    }
                })
                .setNegativeButton(android.R.string.no, null);
        alertBuilder.show();
    }

    @Override
    public void clearDataHome() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (fragment != null && fragment instanceof HistoryFragment) {
            ((HistoryFragment) fragment).clearData();
            setIsShowAddPoll(true);
        }
    }

    public ObservableBoolean getIsShowAddPoll() {
        return mIsShowAddPoll;
    }

    public void setIsShowAddPoll(boolean isShow) {
        mIsShowAddPoll.set(isShow);
    }
}
