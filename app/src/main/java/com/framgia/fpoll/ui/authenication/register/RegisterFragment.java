package com.framgia.fpoll.ui.authenication.register;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.fpoll.R;
import com.framgia.fpoll.data.model.User;
import com.framgia.fpoll.databinding.FragmentRegisterBinding;
import com.framgia.fpoll.util.ActivityUtil;
import com.framgia.fpoll.util.Constant;
import com.framgia.fpoll.util.PermissionsUtil;

import static android.app.Activity.RESULT_OK;

/**
 * Created by tuanbg on 2/9/17.
 */
public class RegisterFragment extends Fragment implements RegisterContract.View {
    private FragmentRegisterBinding mBinding;
    private RegisterContract.Presenter mPresenter;
    private User mUser = new User();

    public RegisterFragment() {
    }

    public static RegisterFragment getInstance() {
        return new RegisterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        mPresenter = new RegisterPresenter(this, mUser);
        mBinding.setHandler(new RegisterItemActionHandle(mPresenter));
        mBinding.setPresenter((RegisterPresenter) mPresenter);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        start();
    }

    @Override
    public void start() {
    }

    @Override
    public void chooseImage() {
        if (PermissionsUtil.isAllowPermissions(getActivity())) {
            pickImage();
        }
    }

    public void pickImage() {
        Intent intent = new Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Constant.RequestCode.IMAGE_PICKER_SELECT);
    }

    public void showMessageError(int message) {
        ActivityUtil.showToast(getActivity(), message);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.RequestCode.IMAGE_PICKER_SELECT && resultCode == RESULT_OK &&
            null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = this.getActivity().getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String urlAvatar = cursor.getString(columnIndex);
            mPresenter.setUserUrlImage(urlAvatar);
            cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == Constant.RequestCode.PERMISSIONS_REQUEST_READ_EXTERNAL) {
            if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            } else {
                ActivityUtil.showToast(getActivity(), R.string.msg_image_not_choose);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
