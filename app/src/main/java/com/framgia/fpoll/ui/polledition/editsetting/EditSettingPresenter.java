package com.framgia.fpoll.ui.polledition.editsetting;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.framgia.fpoll.data.model.PollItem;
import com.framgia.fpoll.ui.pollcreation.setting.RequireVoteType;
import com.framgia.fpoll.util.ActivityUtil;

import static com.framgia.fpoll.util.Constant.DataConstant.NUMBER_MIN_LIMIT;
import static com.framgia.fpoll.util.Constant.DataConstant.NUMBER_SPACE;
import static com.framgia.fpoll.util.Constant.WebUrl.POLL_URL;

/**
 * Created by framgia on 17/03/2017.
 */
public class EditSettingPresenter implements EditSettingContract.Presenter {
    private EditSettingContract.View mView;
    private ObservableBoolean mShowPassword = new ObservableBoolean();
    private ObservableInt mNumberLimit = new ObservableInt();
    private ObservableField<String> mPass = new ObservableField<>();
    private ObservableField<String> mLinkPoll = new ObservableField<>();
    private RequireVoteType mRequireVoteType = RequireVoteType.NAME;
    private PollItem mPollItem;
    private ObservableBoolean mRequireVote = new ObservableBoolean();
    private ObservableBoolean mOptimizePoll = new ObservableBoolean();
    private ObservableBoolean mVoteLimit = new ObservableBoolean();
    private ObservableBoolean mSetPassword = new ObservableBoolean();

    public EditSettingPresenter(EditSettingContract.View view, PollItem pollItem) {
        mView = view;
        mShowPassword.set(false);
        mNumberLimit.set(NUMBER_MIN_LIMIT);
        mLinkPoll.set(ActivityUtil.subLinkPoll(POLL_URL));
        mPollItem = pollItem;
        mView.start();
    }

    @Override
    public void onCheckedRequireVote(boolean checked) {
        mRequireVote.set(checked);
        mPollItem.setRequireVote(checked);
        if (checked) mPollItem.setRequiteType(mRequireVoteType.getValue());
    }

    @Override
    public void onCheckedVotingResult(boolean checked) {
        mPollItem.setHideResult(checked);
    }

    @Override
    public void onCheckedLinkPoll(boolean checked) {
        mOptimizePoll.set(checked);
    }

    @Override
    public void onCheckedVotingLimit(boolean checked) {
        mVoteLimit.set(checked);
        mPollItem.setMaxVote(checked);
        if (checked) mPollItem.setNumMaxVote(mNumberLimit.get());
    }

    @Override
    public void onCheckedSetPassword(boolean checked) {
        mSetPassword.set(checked);
        mPollItem.setHasPass(checked);
        if (checked) mPollItem.setPass(mPass.get());
    }

    @Override
    public void nextStep() {
        if (mView != null) mView.nextStep();
    }

    @Override
    public void previousStep() {
        if (mView != null) mView.previousStep();
    }

    @Override
    public void onShowPassword() {
        mShowPassword.set(!mShowPassword.get());
    }

    @Override
    public void clickAugment() {
        mNumberLimit.set(mNumberLimit.get() + NUMBER_SPACE);
    }

    @Override
    public void clickMinus() {
        if (mNumberLimit.get() > 0) mNumberLimit.set(mNumberLimit.get() - NUMBER_SPACE);
    }

    @Override
    public void changeAllowEditPoll(boolean checked) {
        // TODO: 3/13/2017 checked change allow edit
    }

    @Override
    public void changeAllowAddAnswer(boolean checked) {
        // TODO: 3/13/2017 checked change allow edit
    }

    @Override
    public void setRequireVote(RequireVoteType requireVote) {
        mRequireVoteType = requireVote;
    }

    public ObservableBoolean getShowPassword() {
        return mShowPassword;
    }

    public ObservableInt getNumberLimit() {
        return mNumberLimit;
    }

    public ObservableField<String> getPass() {
        return mPass;
    }

    public ObservableField<String> getLinkPoll() {
        return mLinkPoll;
    }

    public ObservableBoolean getRequireVote() {
        return mRequireVote;
    }

    public ObservableBoolean getOptimizePoll() {
        return mOptimizePoll;
    }

    public ObservableBoolean getVoteLimit() {
        return mVoteLimit;
    }

    public ObservableBoolean getSetPassword() {
        return mSetPassword;
    }
}