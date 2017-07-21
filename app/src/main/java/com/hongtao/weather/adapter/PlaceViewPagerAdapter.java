package com.hongtao.weather.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * author：hongtao on 2017/7/21/021 14:55
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class PlaceViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragmentList;

    public PlaceViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

}
