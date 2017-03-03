package com.framgia.fpoll.ui.pollcreation.infomation;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.fpoll.R;
import com.framgia.fpoll.data.model.PollInformation;
import com.framgia.fpoll.databinding.FragmentCreatePollBinding;
import com.framgia.fpoll.ui.pollcreation.option.OptionPollFragment;
import com.framgia.fpoll.util.ActivityUtil;
import com.framgia.fpoll.util.Constant;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class CreatePollFragment extends Fragment
    implements DatePickerDialog.OnDateSetListener
    , TimePickerDialog.OnTimeSetListener
    , CreationContract.View {
    private FragmentCreatePollBinding mBinding;
    private CreationContract.Presenter mPresenter;
    public final ObservableField<Calendar> mTime = new ObservableField<>(Calendar.getInstance());
    private PollInformation mPollInformation = new PollInformation();

    public static CreatePollFragment newInstance() {
        return new CreatePollFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_poll, container, false);
        mPresenter = new CreationPresenter(this, mPollInformation);
        mBinding.setInformation(mPollInformation);
        mBinding.setHandler(new CreatePollActionHandle(mPresenter));
        mBinding.setPresenter((CreationPresenter) mPresenter);
        mBinding.setFragment(this);
        return mBinding.getRoot();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(Calendar.YEAR, year);
        newCalendar.set(Calendar.MONTH, monthOfYear);
        newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        if (newCalendar.before(Calendar.getInstance())) {
            ActivityUtil.showToast(getContext(), R.string.msg_date_error);
        } else mTime.set(newCalendar);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
    }

    @Override
    public void showDatePicker() {
        DatePickerDialog dpd = DatePickerDialog.newInstance(
            this,
            mTime.get().get(Calendar.YEAR),
            mTime.get().get(Calendar.MONTH),
            mTime.get().get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getActivity().getFragmentManager(), Constant.Tag.DATE_PICKER_TAG);
    }

    @Override
    public void bindError() {
        mBinding.setMsgError(getString(R.string.msg_content_error));
        mBinding.setMsgErrorEmail(getString(R.string.msg_email_invalidate));
    }

    @Override
    public void nextStep() {
        OptionPollFragment optionPollFragment = OptionPollFragment.newInstance();
        getFragmentManager().beginTransaction()
            .add(R.id.frame_layout, optionPollFragment, null)
            .addToBackStack(null)
            .commit();
    }

    @Override
    public void start() {
    }
}
