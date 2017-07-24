package com.hongtao.weather.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.hongtao.weather.R;

/**
 * author：hongtao on 2017/7/24/024 09:54
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class LoadFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void show(android.app.FragmentManager manager, String tag) {
        super.show(manager, tag);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.dismiss();
    }
}
