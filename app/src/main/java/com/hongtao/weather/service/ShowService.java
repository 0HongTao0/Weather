package com.hongtao.weather.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hongtao.weather.R;
import com.hongtao.weather.activity.WeatherActivity;
import com.hongtao.weather.util.HttpUtil;

import java.util.List;

/**
 * 更新状态栏通知与隔 6 小时更新 Activity 天气信息的前台服务
 *
 * author：Administrator on 2017/7/13/013 11:24
 * email：935245421@qq.com
 */
public class ShowService extends Service {
    private Notification mNotification;
    private ShowBinder mShowBinder = new ShowBinder();

    public ShowService() {

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mShowBinder.updateStatus(intent.getStringArrayListExtra("List"));
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 6 * 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent broadcastIntent = new Intent("com.weather.update");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ShowService.this, 0, broadcastIntent, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mShowBinder.updateStatus(null);
    }


    public class ShowBinder extends Binder {
        public void updateStatus(List<String> msg) {
            if (msg == null) {
                mNotification = new NotificationCompat.Builder(ShowService.this)
                        .setContentTitle("正在获取地点")
                        .setContentText("正在获取天气状况")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .build();
            } else {
                Bitmap bitmap = HttpUtil.downloadPic(msg.get(2));
                mNotification = new NotificationCompat.Builder(ShowService.this)
                        .setContentTitle(msg.get(0))
                        .setContentText(msg.get(1))
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(bitmap)
                        .setAutoCancel(true)
                        .build();
            }
            startForeground(1, mNotification);
        }
    }

}
