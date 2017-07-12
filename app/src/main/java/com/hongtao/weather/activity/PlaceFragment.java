package com.hongtao.weather.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hongtao.weather.R;

/**
 * author：Administrator on 2017/7/12/012 12:41
 * email：935245421@qq.com
 */
public class PlaceFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place, container, false);
        return view;
    }
}
