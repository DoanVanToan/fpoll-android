package com.framgia.fpoll.ui.polledition.editinformation;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.fpoll.R;
import com.framgia.fpoll.data.model.PollInformation;
import com.framgia.fpoll.databinding.FragmentEditInforBinding;
import com.framgia.fpoll.ui.pollcreation.infomation.CreationContract;
import com.framgia.fpoll.ui.pollcreation.infomation.CreationPresenter;
import com.framgia.fpoll.util.ActivityUtil;
import com.framgia.fpoll.util.Constant;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by framgia on 16/03/2017.
 */
public class EditInforFragment extends Fragment
    implements DatePickerDialog.OnDateSetListener
    , TimePickerDialog.OnTimeSetListener
    , EditInforContract.View {
    private FragmentEditInforBinding mBinding;
    private EditInforContract.Presenter mPresenter;
    public final ObservableField<Calendar> mTime = new ObservableField<>(Calendar.getInstance());
    private PollInformation mPollInformation = new PollInformation();
    private Calendar mSavePickCalendar = Calendar.getInstance();

    public static EditInforFragment newInstance() {
        return new EditInforFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_infor, container, false);
        mPresenter = new EditInforPresenter(this, mPollInformation);
        mBinding.setInformation(mPollInformation);
        mBinding.setHandler(new EditInforHandle(mPresenter));
        mBinding.setPresenter((EditInforPresenter) mPresenter);
        mBinding.setFragment(this);
        mTime.set(mSavePickCalendar);
        return mBinding.getRoot();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mSavePickCalendar.set(Calendar.YEAR, year);
        mSavePickCalendar.set(Calendar.MONTH, monthOfYear);
        mSavePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        showTimePicker();
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        mSavePickCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mSavePickCalendar.set(Calendar.MINUTE, minute);
        mSavePickCalendar.set(Calendar.SECOND, second);
        if (mSavePickCalendar.before(Calendar.getInstance())) {
            ActivityUtil.showToast(getContext(), R.string.msg_date_error);
        } else mTime.notifyChange();
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
    public void showTimePicker() {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
            this,
            mTime.get().get(Calendar.HOUR_OF_DAY),
            mTime.get().get(Calendar.MINUTE),
            mTime.get().get(Calendar.SECOND),
            true
        );
        timePickerDialog.show(getActivity().getFragmentManager(), Constant.Tag.TIME_PICKER_TAG);
    }

    @Override
    public void nextStep() {
    }

    @Override
    public void start() {
    }
}