package com.framgia.fpoll.ui.profile;

import android.content.Intent;
import android.support.annotation.NonNull;
import com.framgia.fpoll.data.model.authorization.User;
import com.framgia.fpoll.ui.base.BasePresenter;
import com.framgia.fpoll.ui.base.BaseViewModel;

/**
 * Created by framgia on 19/04/2017.
 */

public interface ProfileContract {
    interface ViewModel extends BaseViewModel<Presenter> {

        void onGetUserSuccess(User user);

        void onGetUserFailed();

        void onEditUserClick();

        void onChangeAvtClick();

        boolean onBackPressed();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                @NonNull int[] grantResults);

        void onValidateNameError();

        void onValidateEmailError();

        void onValidatePasswordError();

        void showProgressDialog();

        void onUpdateUserSuccess(User user);

        void hideProgressDialog();

        void onUpdateUserFaile(String msg);

        void onUpdateUserFaile(int msg);

        void getDataUserError(String msg);

        void onHideBottomNavigation();

        void onShowBottomNavigation();

        void startLoginScreen();

        void showMessage(String data);

        void setNullUser();

        void reloadMainView();
    }

    interface Presenter extends BasePresenter {
        void getUser();

        void editUser(User user);

        void logout(User user);
    }
}
