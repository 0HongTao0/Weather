package com.hongtao.weather.activity;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.leakcanary.LeakCanary;

/**
 * author：hongtao on 2017/7/18/018 11:01
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class WeatherApplication extends Application {
    private static Context sContext;
    private static RequestQueue sRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        sContext = getApplicationContext();
        sRequestQueue = Volley.newRequestQueue(sContext);
    }

    public static Context getContext() {
        return sContext;
    }

    public static RequestQueue getRequestQueue() {
        return sRequestQueue;
    }
}