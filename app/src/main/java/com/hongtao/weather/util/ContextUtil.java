package com.hongtao.weather.util;

import android.app.Application;
import android.content.Context;

/**
 * author：hongtao on 2017/7/18/018 11:01
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class ContextUtil extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}