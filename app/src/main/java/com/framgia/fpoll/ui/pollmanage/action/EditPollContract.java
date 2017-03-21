package com.framgia.fpoll.ui.pollmanage.action;

import com.framgia.fpoll.data.model.DataInfoItem;
import com.framgia.fpoll.ui.base.BaseView;

/**
 * Created by tran.trung.phong on 01/03/2017.
 */
public interface EditPollContract {
    interface View extends BaseView {
        void showMessage(String msg);
        void startModifyPoll();
        void startUiPollCreation(DataInfoItem data);
    }

    interface Presenter {
        void updateLinkPoll();
        void viewHistory();
        void editPoll();
        void closePoll();
        void createDuplicate();
        void deleteVoting();
    }
}
