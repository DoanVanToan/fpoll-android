package com.framgia.fpoll.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.framgia.fpoll.BR;
import com.framgia.fpoll.R;
import com.framgia.fpoll.data.model.authorization.User;
import com.framgia.fpoll.ui.authenication.activity.AuthenticationActivity;
import com.framgia.fpoll.ui.authenication.changepass.ChangePassFragment;
import com.framgia.fpoll.ui.mainstart.NewMainActivity;
import com.framgia.fpoll.ui.profile.language.LanguageDialogFragment;
import com.framgia.fpoll.util.ActivityUtil;
import com.framgia.fpoll.util.Constant;
import com.framgia.fpoll.util.PermissionsUtil;
import com.framgia.fpoll.util.SharePreferenceUtil;
import com.framgia.fpoll.widget.FPollProgressDialog;

import static android.app.Activity.RESULT_OK;
import static com.framgia.fpoll.util.Constant.DataConstant.DATA_IMAGE;
import static com.framgia.fpoll.util.Constant.PreferenceConstant.PREF_USER;
import static com.framgia.fpoll.util.Constant.RequestCode.IMAGE_PICKER_SELECT;
import static com.framgia.fpoll.util.Constant.RequestCode.PERMISSIONS_REQUEST_WRITE_EXTERNAL;

/**
 * Created by framgia on 19/04/2017.
 */

public class ProfileViewModel extends BaseObservable implements ProfileContract.ViewModel {
    private ProfileContract.Presenter mPresenter;
    private AppCompatActivity mActivity;
    private ObservableField<User> mUser = new ObservableField<>();
    private ObservableField<User> mEditUser = new ObservableField<>();
    private SharePreferenceUtil mPreferenceUtil;
    private boolean mEditing;
    private FPollProgressDialog mDialog;
    private Context mContext;

    public Context getContext() {
        return mContext;
    }

    public ProfileViewModel(AppCompatActivity activity, SharePreferenceUtil sharePreferenceUtil) {
        mActivity = activity;
        mContext = activity;
        mPreferenceUtil = sharePreferenceUtil;
    }

    public AppCompatActivity getActivity() {
        return mActivity;
    }

    @Override
    public void onGetUserSuccess(User user) {
        mUser.set(user);
        try {
            mEditUser.set((User) user.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetUserFailed() {
        mActivity.finish();
    }

    @Override
    public void onUpdateUserSuccess(User user) {
        setEditing(false);
        try {
            mUser.set((User) user.clone());
            mEditUser.set((User) user.clone());
            mActivity.setTitle(user.getUsername());
            mActivity.setResult(RESULT_OK, new Intent());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateUserFaile(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateUserFaile(int msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getDataUserError(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditUserClick() {
        if (mEditing) {
            mPresenter.editUser(mEditUser.get());
        } else {
            setEditing(true);
        }
    }

    @Override
    public void onChangeAvtClick() {
        if (PermissionsUtil.isAllowPermissions(mActivity)) pickImage();
    }

    @Override
    public boolean onBackPressed() {
        if (mEditing) {
            try {
                mEditUser.set((User) mUser.get().clone());
                setEditing(!mEditing);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return false;
        }

        return true;
    }

    public void pickImage() {
        Intent intent = new Intent();
        intent.setType(DATA_IMAGE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivity.startActivityForResult(
                Intent.createChooser(intent, mActivity.getString(R.string.app_name)),
                IMAGE_PICKER_SELECT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICKER_SELECT && resultCode == RESULT_OK && data != null) {
            String url = ActivityUtil.getPathFromUri(mActivity, data.getData());
            mEditUser.get().setAvatar(url);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode != PERMISSIONS_REQUEST_WRITE_EXTERNAL) return;
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        } else {
            Toast.makeText(mActivity, R.string.msg_image_not_choose, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onValidateNameError() {
        Toast.makeText(mActivity, R.string.msg_username_not_empty, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidateEmailError() {
        Toast.makeText(mActivity, R.string.msg_email_invalidate, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidatePasswordError() {
        Toast.makeText(mActivity, R.string.msg_password_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressDialog() {
        if (mDialog == null) {
            mDialog = new FPollProgressDialog(mActivity);
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    @Override
    public void hideProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMessage(String data) {
        ActivityUtil.showToast(mActivity, data);
    }

    @Override
    public void setNullUser() {
        mUser.set(null);
    }

    @Override
    public void reloadMainView() {
        mActivity.startActivity(NewMainActivity.getInstance(mContext));
    }

    @Bindable
    public boolean isEditing() {
        return mEditing;
    }

    public void setEditing(boolean editing) {
        mEditing = editing;
        notifyPropertyChanged(BR.editing);
    }

    @Override
    public void onHideBottomNavigation() {
        ((NewMainActivity) getActivity()).hideBottomNavigation();
    }

    @Override
    public void onShowBottomNavigation() {
        ((NewMainActivity) getActivity()).showBottomNavigation();
    }

    @Override
    public void startLoginScreen() {
        mActivity.startActivity(AuthenticationActivity.getAuthenticationIntent(mContext, true));
    }

    public ObservableField<User> getUser() {
        return mUser;
    }

    public ObservableField<User> getEditUser() {
        return mEditUser;
    }

    public void showChangePasswordDialog() {
        FragmentTransaction transaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        DialogFragment dialog = ChangePassFragment.newInstance();
        dialog.show(transaction, Constant.TYPE_DIALOG_FRAGMENT);
    }

    public void logout() {
        if (mUser.get() == null) {
            ActivityUtil.showToast(mContext, mContext.getString(R.string.action_show_not_user));
            startLoginScreen();
            return;
        }
        mPresenter.logout(mUser.get());
        mPreferenceUtil.writeUser(null);
        mPreferenceUtil.clearKey(PREF_USER);
    }

    public void changeLanguage() {
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        DialogFragment optionDialog = LanguageDialogFragment.newInstance();
        optionDialog.show(transaction, Constant.TYPE_DIALOG_FRAGMENT);
    }
}
