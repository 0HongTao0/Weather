package com.hongtao.weather.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * author：hongtao on 2017/7/20/020 15:00
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class NetwordUtil {
    /**
     * 判断当前是否连接网络
     * @param activity
     * @return
     */
    public static boolean netIsWork(Activity activity) {
        ConnectivityManager manager = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable()) {
            return false;
        }
        return true;
    }
}
