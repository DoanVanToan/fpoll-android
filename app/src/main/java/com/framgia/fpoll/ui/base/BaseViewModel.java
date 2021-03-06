package com.framgia.fpoll.ui.base;

/**
 * Created by framgia on 19/04/2017.
 */

public interface BaseViewModel<T extends BasePresenter> {

    void onStart();

    void onStop();

    void setPresenter(T presenter);
}
