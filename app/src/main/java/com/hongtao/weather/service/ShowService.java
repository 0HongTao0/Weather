package com.hongtao.weather.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.hongtao.weather.R;
import com.hongtao.weather.activity.WeatherActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * author：Administrator on 2017/7/13/013 11:24
 * email：935245421@qq.com
 */
public class ShowService extends Service {
    private Notification mNotification;
    private ShowBinder mShowBinder = new ShowBinder();

    public ShowService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mShowBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mShowBinder.updateStatus(null);
    }


    public class ShowBinder extends Binder {
        public void updateStatus(List<String> msg) {
            Intent intent = new Intent(ShowService.this, WeatherActivity.class);
            PendingIntent pi = PendingIntent.getActivity(ShowService.this, 0, intent, 0);
            if (msg == null) {
                mNotification = new NotificationCompat.Builder(ShowService.this)
                        .setContentTitle("正在获取地点")
                        .setContentText("正在获取天气状况")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentIntent(pi)
                        .build();
            } else {
                mNotification = new NotificationCompat.Builder(ShowService.this)
                        .setContentTitle(msg.get(0))
                        .setContentText(msg.get(1))
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
//                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .build();
            }
            startForeground(1, mNotification);
        }
    }

}
