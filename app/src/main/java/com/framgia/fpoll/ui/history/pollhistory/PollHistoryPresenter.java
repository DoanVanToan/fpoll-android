package com.framgia.fpoll.ui.history.pollhistory;

import com.framgia.fpoll.R;
import com.framgia.fpoll.data.model.authorization.User;
import com.framgia.fpoll.data.model.poll.HistoryPoll;
import com.framgia.fpoll.data.source.DataCallback;
import com.framgia.fpoll.data.source.remote.pollmanager.ManagerRepository;
import com.framgia.fpoll.ui.history.PollHistoryType;
import com.framgia.fpoll.util.SharePreferenceUtil;
import java.util.List;

import static com.framgia.fpoll.util.Constant.DataConstant.DATA_PREFIX_TOKEN;

/**
 * Created by Nhahv0902 on 2/14/2017.
 * <></>
 */
public class PollHistoryPresenter implements PollHistoryContract.Presenter {
    private static final int NUMBER_LINK_ADMIN = 1;
    private PollHistoryContract.View mView;
    private PollHistoryType mHistoryType;
    private ManagerRepository mRepository;
    private User mUser;
    private SharePreferenceUtil mPreference;

    public PollHistoryPresenter(PollHistoryContract.View view, PollHistoryType typeHistory,
            ManagerRepository repository, SharePreferenceUtil preference) {
        mView = view;
        mRepository = repository;
        mHistoryType = typeHistory;
        mUser = preference.getUser();
        mPreference = preference;
        mView.start();
    }

    @Override
    public void getData() {
        if (mView == null || mRepository == null) return;
        if (!mPreference.isLogin()) {
            mView.setLoadingTrue();
            mView.setLoadingFalse();
            return;
        }
        mView.setLoadingTrue();
        switch (mHistoryType) {
            case INITIATE:
                mRepository.getHistory(DATA_PREFIX_TOKEN + mUser.getToken(),
                        new DataCallback<List<HistoryPoll>>() {
                            @Override
                            public void onSuccess(List<HistoryPoll> data) {
                                loadDataSuccess(data);
                            }

                            @Override
                            public void onError(String msg) {
                                loadDataError();
                            }
                        });
                break;
            case PARTICIPATE:
                mRepository.getPollParticipated(DATA_PREFIX_TOKEN + mUser.getToken(),
                        new DataCallback<List<HistoryPoll>>() {
                            @Override
                            public void onSuccess(List<HistoryPoll> data) {
                                loadDataSuccess(data);
                            }

                            @Override
                            public void onError(String msg) {
                                loadDataError();
                            }
                        });
                break;
            case CLOSE:
                mRepository.getPollClosed(DATA_PREFIX_TOKEN + mUser.getToken(),
                        new DataCallback<List<HistoryPoll>>() {
                            @Override
                            public void onSuccess(List<HistoryPoll> data) {
                                loadDataSuccess(data);
                            }

                            @Override
                            public void onError(String msg) {
                                loadDataError();
                            }
                        });
                break;
            default:
                break;
        }
    }

    private void loadDataError() {
        mView.showMessage(R.string.msg_not_load_item);
        mView.setLoadingFalse();
    }

    private void loadDataSuccess(List<HistoryPoll> data) {
        mView.setLoadingFalse();
        mView.setPollHistory(data);
    }

    @Override
    public void openPollHistory(HistoryPoll data) {
        switch (mHistoryType) {
            case INITIATE:
                if (data == null
                        || data.getLink().get(NUMBER_LINK_ADMIN) == null
                        || data.getLink().get(NUMBER_LINK_ADMIN).getToken() == null) {
                    return;
                }
                if (data.isOpen()) {
                    mView.onOpenManagerPollClick(data.getLink().get(NUMBER_LINK_ADMIN).getToken());
                }
                break;
            case PARTICIPATE:
                if (data == null
                        || data.getLink().get(NUMBER_LINK_ADMIN) == null
                        || data.getLink().get(NUMBER_LINK_ADMIN).getToken() == null) {
                    return;
                }
                if (data.isOpen()) {
                    mView.onOpenVoteClick(data.getLink().get(NUMBER_LINK_ADMIN).getToken());
                } else {
                    mView.showPollClosedDialog();
                }
                break;
            case CLOSE:
                mView.showConfirmDialog(data);
                break;
            default:
                break;
        }
    }

    @Override
    public void reopenPoll(HistoryPoll data) {
        mView.showDialog();
        if (mRepository == null) return;
        mRepository.switchPollStatus(String.valueOf(data.getId()), new DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                mView.showMessage(data);
                getData();
                mView.hideDialog();
            }

            @Override
            public void onError(String msg) {
                mView.showMessage(msg);
                mView.hideDialog();
            }
        });
    }
}
