package com.framgia.fpoll.ui.polledition;

import com.framgia.fpoll.data.model.PollItem;
import com.framgia.fpoll.ui.base.BaseView;

/**
 * Created by framgia on 15/03/2017.
 */
public interface ModifyPollContract {
    interface View extends BaseView {
        void showProgress();

        void hideProgress();

        void showMessage(String message);

        void submitEditPoll();

        void showMessage(int message);

        void notifyUI(PollItem poll);

        void setUpdatePoll(boolean isUpdatePoll);
    }

    interface Presenter {
        void onUpdatePoll();

        void updateInformation();

        void updateOption();

        void updateSetting();
    }
}
