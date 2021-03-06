package com.framgia.fpoll.ui.pollmanage.information;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import com.framgia.fpoll.R;
import com.framgia.fpoll.data.model.DataInfoItem;
import com.framgia.fpoll.data.model.poll.Poll;
import com.framgia.fpoll.data.source.DataCallback;
import com.framgia.fpoll.data.source.remote.polldatasource.PollRepository;
import com.framgia.fpoll.data.source.remote.pollmanager.ManagerRepository;
import com.framgia.fpoll.networking.api.UpdatePollService;
import com.framgia.fpoll.util.SharePreferenceUtil;

import static com.framgia.fpoll.util.Constant.TypeChoose.TYPE_MULTI;
import static com.framgia.fpoll.util.Constant.TypeChoose.TYPE_SINGER;

/**
 * Created by Nhahv0902 on 2/24/2017.
 * <></>
 */
public class PollInformationPresenter implements PollInformationContract.Presenter {
    private final PollInformationContract.View mView;
    private final String mToken;
    private PollRepository mRepository;
    private ManagerRepository mManagerRepository;
    private ObservableField<Poll> mPoll = new ObservableField<>();
    private ObservableBoolean mIsLogin = new ObservableBoolean();

    public PollInformationPresenter(PollInformationContract.View view, PollRepository repository,
            ManagerRepository manageRepository, SharePreferenceUtil preference, DataInfoItem poll,
            String token) {
        mView = view;
        mRepository = repository;
        mToken = token;
        mManagerRepository = manageRepository;
        mIsLogin.set(preference.isLogin());
        if (poll != null && poll.getPoll() != null) mPoll.set(poll.getPoll());
        if (poll == null && token != null) loadData();
        mView.start();
    }

    @Override
    public void loadData() {
        if (mManagerRepository == null || mView == null || mToken == null) return;
        mManagerRepository.getPollDetail(mToken, new DataCallback<DataInfoItem>() {
            @Override
            public void onSuccess(DataInfoItem data) {
                mPoll.set(data.getPoll());
                mView.onGetPollSuccessful(data);
            }

            @Override
            public void onError(String msg) {
                mView.onGetPollFailed(msg);
            }
        });
    }

    @Override
    public void clickLinkVote() {
        mView.startUiVoting();
    }

    @Override
    public void clickViewOption() {
        mView.showDialogOption();
    }

    @Override
    public void clickViewSetting() {
        mView.showDialogSetting();
    }

    @Override

    public void saveInformation() {
        if (mPoll.get() == null || mRepository == null) return;
        String username = mPoll.get().getUser().getUsername();
        String email = mPoll.get().getUser().getEmail();
        String title = mPoll.get().getTitle();
        int type = mPoll.get().isMultiple() ? TYPE_MULTI : TYPE_SINGER;
        String dateClose = mPoll.get().getDateClose();
        String description = mPoll.get().getDescription();
        UpdatePollService.PollInfoBody body =
                new UpdatePollService.PollInfoBody(username, email, title, type, dateClose,
                        description, mPoll.get().getLocation());
        mView.showProgress();
        mRepository.updateInformation(mPoll.get().getId(), body, new DataCallback<DataInfoItem>() {
            @Override
            public void onSuccess(DataInfoItem data) {
                mView.showMessage(R.string.update_success);
                mPoll.set(data.getPoll());
                mView.hideProgress();
            }

            @Override
            public void onError(String msg) {
                mView.showMessage(msg);
                mView.hideProgress();
            }
        });
    }

    @Override
    public void showDateTimePicker() {
        if (mView != null) mView.showDateTimePicker();
    }

    public ObservableField<Poll> getPoll() {
        return mPoll;
    }

    public ObservableBoolean getIsLogin() {
        return mIsLogin;
    }
}
