package com.framgia.fpoll.ui.pollcreation.option;

import com.framgia.fpoll.data.model.poll.Option;

/**
 * Created by framgia on 22/02/2017.
 * <></>
 */
public class OptionHandler {
    private OptionPollContract.Presenter mListener;

    public OptionHandler(OptionPollContract.Presenter listener) {
        mListener = listener;
    }

    public void clickPickImage(Option optionItem, int position) {
        if (mListener != null) {
            mListener.pickImage(optionItem, position);
            mListener.augmentPoll(position);
        }
    }

    public void clickPickDate(Option optionItem, int position) {
        if (mListener != null) {
            mListener.pickDate(optionItem, position);
            mListener.augmentPoll(position);
        }
    }

    public void clickDeletePoll(Option optionItem, int position) {
        if (mListener != null) mListener.deletePoll(optionItem, position);
    }

    public void clickAugmentPoll(int position) {
        if (mListener != null) mListener.augmentPoll(position);
    }

    public void onDeleteImageClicked(Option option) {
        if (mListener != null) mListener.deleteImage(option);
    }

    public void clickDeleteDate(Option option) {
        if (mListener != null) mListener.deleteDateOfOption(option);
    }
}

