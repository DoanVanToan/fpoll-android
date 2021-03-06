package com.framgia.fpoll.ui.pollcreation;

import com.framgia.fpoll.ui.base.BaseView;

/**
 * Created by Nhahv0902 on 3/28/2017.
 * <></>
 */
public interface PollCreationContract {
    interface View extends BaseView {
        void previousUI();

        void nextUI();

        void finishCreate();
    }

    interface Presenter {
        void previous();

        void next();

        void finish();
    }
}
