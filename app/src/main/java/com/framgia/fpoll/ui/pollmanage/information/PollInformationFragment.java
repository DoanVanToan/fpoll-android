package com.framgia.fpoll.ui.pollmanage.information;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.fpoll.R;
import com.framgia.fpoll.data.model.DataInfoItem;
import com.framgia.fpoll.data.model.poll.Option;
import com.framgia.fpoll.data.source.remote.polldatasource.PollRepository;
import com.framgia.fpoll.data.source.remote.pollmanager.ManagerRepository;
import com.framgia.fpoll.databinding.FragmentInformationBinding;
import com.framgia.fpoll.ui.polledition.ModifyPollActivity;
import com.framgia.fpoll.ui.pollmanage.ManagePollActivity;
import com.framgia.fpoll.ui.pollmanage.information.pollsetting.PollSettingDialogFragment;
import com.framgia.fpoll.ui.pollmanage.information.viewoption.PollOptionDialogFragment;
import com.framgia.fpoll.ui.votemanager.LinkVoteActivity;
import com.framgia.fpoll.util.ActivityUtil;
import com.framgia.fpoll.util.Constant;
import com.framgia.fpoll.util.SharePreferenceUtil;
import com.framgia.fpoll.util.TimeUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static com.framgia.fpoll.util.Constant.BundleConstant.BUNDLE_POLL_ITEM;
import static com.framgia.fpoll.util.Constant.BundleConstant.BUNDLE_TOKEN;
import static com.framgia.fpoll.util.Constant.POSITION_LINK_INVITE;
import static com.framgia.fpoll.util.Constant.RequestCode.REQUEST_CODE_RESULT;
import static com.framgia.fpoll.util.Constant.Tag.DATE_PICKER_TAG;
import static com.framgia.fpoll.util.Constant.Tag.TIME_PICKER_TAG;
import static com.framgia.fpoll.util.Constant.WebUrl.OPTION_DATE;
import static com.framgia.fpoll.util.Constant.WebUrl.OPTION_FORMAT;
import static com.framgia.fpoll.util.Constant.WebUrl.OPTION_SIZE;
import static com.framgia.fpoll.util.Constant.WebUrl.OPTION_SPLIT;
import static com.framgia.fpoll.util.Constant.WebUrl.OPTION_TITLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PollInformationFragment extends Fragment
        implements PollInformationContract.View, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    private Calendar mCalendar = Calendar.getInstance();
    private String mToken;
    private DataInfoItem mPoll;
    private FragmentInformationBinding mBinding;
    private PollInformationContract.Presenter mPresenter;

    public static PollInformationFragment newInstance(DataInfoItem poll, String token) {
        PollInformationFragment fragment = new PollInformationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TOKEN, token);
        bundle.putParcelable(BUNDLE_POLL_ITEM, poll);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void getDataFromActivity() {
        Bundle bundle = getArguments();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mBinding = FragmentInformationBinding.inflate(inflater, container, false);
        getDataFromActivity();
        mPresenter = new PollInformationPresenter(this, PollRepository.getInstance(getActivity()),
                ManagerRepository.getInstance(getActivity()),
                SharePreferenceUtil.getIntances(getActivity()), mPoll, mToken);
        mBinding.setPresenter((PollInformationPresenter) mPresenter);
        mBinding.setHandler(new PollInformationHandler(mPresenter));
        return mBinding.getRoot();
    }

    @Override
    public void start() {
        getActivity().setTitle(R.string.title_information);
        if (mPoll != null && mPoll.getPoll() != null && mPoll.getPoll().getTitle() != null) {
            getActivity().setTitle(mPoll.getPoll().getTitle());
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_information_manager, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit && mPoll != null && mPoll.getPoll() != null) {
            if (mPoll.getPoll().getOptions() != null && mPoll.getPoll().getOptions().size() > 0) {
                for (Option option : mPoll.getPoll().getOptions()) {
                    String[] title = option.getName().split(OPTION_SPLIT);
                    if (title.length > 0) {
                        option.setName(title[OPTION_TITLE].replace(OPTION_FORMAT, ""));
                    }
                    if (title.length == OPTION_SIZE) option.setDate(title[OPTION_DATE]);
                }
            }
            startActivityForResult(
                    ModifyPollActivity.getModifyIntent(getActivity(), mPoll.getPoll()),
                    REQUEST_CODE_RESULT);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_RESULT) {
            ((ManagePollActivity) getActivity()).setUpdatePoll(true);
            ((ManagePollActivity) getActivity()).updateData();

            if (mPresenter != null) mPresenter.loadData();
        }
    }

    @Override
    public void startUiVoting() {
        if (mToken == null && mPoll != null) {
            mToken = mPoll.getPoll().getLink().get(POSITION_LINK_INVITE).getToken();
        }
        if (mToken == null) return;
        startActivity(LinkVoteActivity.getTokenIntent(getActivity(), mToken));
    }

    @Override
    public void showDialogOption() {
        if (mPoll == null || mPoll.getPoll().getOptions() == null) return;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        DialogFragment optionDialog =
                PollOptionDialogFragment.newInstance(mPoll.getPoll().getOptions());
        optionDialog.show(transaction, Constant.TYPE_DIALOG_FRAGMENT);
    }

    @Override
    public void showDialogSetting() {
        if (mPoll == null || mPoll.getPoll().getSettings() == null) return;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        DialogFragment optionDialog =
                PollSettingDialogFragment.newInstance(mPoll.getPoll().getSettings());
        optionDialog.show(transaction, Constant.TYPE_DIALOG_FRAGMENT);
    }

    @Override
    public void showDateTimePicker() {
        if (mCalendar == null) mCalendar = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(this, mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        dpd.show(getActivity().getFragmentManager(), DATE_PICKER_TAG);
    }

    public void showTimePicker() {
        if (mCalendar == null) mCalendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog =
                TimePickerDialog.newInstance(this, mCalendar.get(Calendar.HOUR_OF_DAY),
                        mCalendar.get(Calendar.MINUTE), mCalendar.get(Calendar.SECOND), true);
        timePickerDialog.show(getActivity().getFragmentManager(), TIME_PICKER_TAG);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        showTimePicker();
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, second);
        mPoll.getPoll().setDateClose(TimeUtil.convertTimeToString(mCalendar));
    }

    @Override
    public void onGetPollSuccessful(DataInfoItem data) {
        mPoll = data;
        if (mPoll != null && mPoll.getPoll() != null && mPoll.getPoll().getTitle() != null) {
            getActivity().setTitle(mPoll.getPoll().getTitle());
        }
    }

    @Override
    public void onGetPollFailed(String message) {
        ActivityUtil.showToast(getActivity(), message);
    }

    @Override
    public void showProgress() {
        ((ManagePollActivity) getActivity()).showProgressDialog();
    }

    @Override
    public void hideProgress() {
        ((ManagePollActivity) getActivity()).hideProgressDialog();
    }

    @Override
    public void showMessage(String data) {
        ActivityUtil.showToast(getActivity(), data);
    }

    @Override
    public void showMessage(int msg) {
        ActivityUtil.showToast(getActivity(), msg);
    }

    public void loadData() {
        if (mPresenter != null) mPresenter.loadData();
    }
}
