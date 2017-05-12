package com.framgia.fpoll.ui.polledition;

import android.support.annotation.Nullable;
import com.framgia.fpoll.R;
import com.framgia.fpoll.data.model.PollItem;
import com.framgia.fpoll.data.model.poll.Option;
import com.framgia.fpoll.data.source.DataCallback;
import com.framgia.fpoll.data.source.remote.polldatasource.PollRepository;
import com.framgia.fpoll.networking.api.UpdatePollService;
import com.framgia.fpoll.util.ActivityUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by framgia on 15/03/2017.
 */
public class ModifyPollPresenter implements ModifyPollContract.Presenter {
    private PollItem mPoll;
    private ModifyPollContract.View mView;
    private PollRepository mRepository;

    public ModifyPollPresenter(ModifyPollContract.View view, PollItem poll,
            PollRepository repository) {
        mView = view;
        mPoll = poll;
        mRepository = repository;
        mView.start();
    }

    @Override
    public void onUpdatePoll() {
        if (mView == null || mRepository == null) return;
        mView.submitEditPoll();
    }

    @Override
    public void updateInformation() {
        mView.showProgress();
        UpdatePollService.PollInfoBody body =
                new UpdatePollService.PollInfoBody(mPoll.getUser().getUsername(),
                        mPoll.getUser().getEmail(), mPoll.getTitle(), mPoll.isMultiple(),
                        mPoll.getDateClose(), mPoll.getDescription(), mPoll.getLocation());
        mRepository.updateInformation(mPoll.getId(), body, new DataCallback<PollItem>() {
            @Override
            public void onSuccess(PollItem data) {
                onUpdateSuccess(data);
            }

            @Override
            public void onError(String msg) {
                mView.hideProgress();
                mView.showMessage(msg);
            }
        });
    }

    @Override
    public void updateOption() {
        List<Option> options = validateOption();
        if (options == null) return;
        mView.showProgress();
        mRepository.updateOption(mPoll.getId(), options, new DataCallback<PollItem>() {
            @Override
            public void onSuccess(PollItem data) {
                onUpdateSuccess(data);
            }

            @Override
            public void onError(String msg) {
                mView.hideProgress();
                mView.showMessage(msg);
            }
        });
    }

    @Nullable
    private List<Option> validateOption() {
        List<Option> listOptionReal = new ArrayList<>();
        for (Option option : mPoll.getOptions()) {
            if (option.getName() != null || option.getImage() != null) listOptionReal.add(option);
        }
        if (listOptionReal.size() == 0) {
            mView.showMessage(R.string.msg_option_blank);
            return null;
        }
        return listOptionReal;
    }

    @Override
    public void updateSetting() {
        mView.showProgress();
        mRepository.updateSetting(mPoll, new DataCallback<PollItem>() {
            @Override
            public void onSuccess(PollItem data) {
                onUpdateSuccess(data);
            }

            @Override
            public void onError(String msg) {
                mView.showMessage(msg);
                mView.hideProgress();
            }
        });
    }

    private void onUpdateSuccess(PollItem data) {
        if (mView != null) mView.setUpdatePoll(true);
        mPoll = data;
        ActivityUtil.splitDateOptionOfPoll(mPoll);
        mView.hideProgress();
        mView.showMessage(R.string.update_success);
        mView.notifyUI(mPoll);
    }
}
