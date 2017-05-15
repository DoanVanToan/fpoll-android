package com.framgia.fpoll.ui.mainstart;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.List;

/**
 * Created by framgia on 28/04/2017.
 */

public class NewMainViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments;

    public NewMainViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }
}
