package com.hongtao.weather.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * author：hongtao on 2017/7/21/021 18:50
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class WeatherViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragmentList;

    public WeatherViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.mFragmentList = fragmentList;
    }

    public void updateViewPager(List<Fragment> fragmentList) {
        mFragmentList.clear();
        mFragmentList.addAll(fragmentList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList == null ? 0 : mFragmentList.size();
    }
}
