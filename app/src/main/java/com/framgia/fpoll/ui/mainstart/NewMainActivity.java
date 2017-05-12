package com.framgia.fpoll.ui.mainstart;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.framgia.fpoll.R;
import com.framgia.fpoll.databinding.ActivityNewMainBinding;
import com.framgia.fpoll.ui.base.BaseActivity;

/**
 * NewMain Screen.
 */
public class NewMainActivity extends BaseActivity {

    private NewMainContract.ViewModel mViewModel;

    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context, NewMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new NewMainViewModel(this);

        NewMainContract.Presenter presenter = new NewMainPresenter();
        mViewModel.setPresenter(presenter);

        ActivityNewMainBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_new_main);
        binding.setViewModel((NewMainViewModel) mViewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    protected void onStop() {
        mViewModel.onStop();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mViewModel != null) mViewModel.onActivityResult(requestCode, resultCode, data);
    }

    public void hideBottomNavigation() {
        mViewModel.hideBottomNavigation();
    }

    public void showBottomNavigation() {
        mViewModel.showBottomNavigation();
    }
}
