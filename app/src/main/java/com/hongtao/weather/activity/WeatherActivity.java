package com.hongtao.weather.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hongtao.weather.R;
import com.hongtao.weather.adapter.DailyForecastAdapter;
import com.hongtao.weather.adapter.HourForecastAdapter;
import com.hongtao.weather.bean.DailyForecast;
import com.hongtao.weather.bean.HourForecast;
import com.hongtao.weather.bean.NowWeather;
import com.hongtao.weather.service.ShowService;
import com.hongtao.weather.util.HandlerUtil;
import com.hongtao.weather.util.HttpUtil;
import com.hongtao.weather.util.UIUtil;
import com.hongtao.weather.util.ParseUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    private TextView TvName, TvNowTemperature, TvNowWindDirection, TvNowWindSpeed;
    private ImageView IvNowSky;
    private ListView LvHourForecast, LvDailyForecast;
    private UpdateStatusReceiver mReceiver;
    private IntentFilter mIntentFilter;

    private static final String WEATHER_ADDRESS = "http://guolin.tech/api/weather?cityid=";
    private static final String WEATHER_KEY = "&key=6c455039547e4d60a4da6c2e60d863b9";
    private static final String ICON_ADDRESS = "https://cdn.heweather.com/cond_icon/";

    private String nowWeatherId = "CN101280101";  //广州天气ID
    private static final int UPDATE_WEATHER_NOW = 1;
    private static final int UPDATE_WEATHER_DAILYFORECAST = 2;
    private static final int UPDATE_WEATHER_HOURFORECAST = 3;
    private static final int UPDATE_IMAGEVIEW = 4;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_WEATHER_NOW:
                    NowWeather nowWeather = (NowWeather) msg.obj;
                    TvName.setText(nowWeather.getCity());
                    TvNowTemperature.setText(nowWeather.getTemperature());
                    TvNowWindSpeed.setText(nowWeather.getAir());
                    TvNowWindDirection.setText(nowWeather.getWindDirection());
                    break;
                case UPDATE_WEATHER_DAILYFORECAST:
                    DailyForecastAdapter dailyForecastAdapter = new DailyForecastAdapter(WeatherActivity.this, (List<DailyForecast>) msg.obj);
                    LvDailyForecast.setAdapter(dailyForecastAdapter);
                    break;
                case UPDATE_WEATHER_HOURFORECAST:
                    HourForecastAdapter adapter = new HourForecastAdapter(WeatherActivity.this, (List<HourForecast>) msg.obj);
                    LvHourForecast.setAdapter(adapter);
                    break;
                case UPDATE_IMAGEVIEW:
                    IvNowSky.setImageBitmap((Bitmap) msg.obj);
                    break;
            }
            UIUtil.setListViewHeightBasedOnChildren(LvDailyForecast);
            UIUtil.setListViewHeightBasedOnChildren(LvHourForecast);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        TvName = (TextView) findViewById(R.id.weather_tv_where);

        TvNowTemperature = (TextView) findViewById(R.id.now_tv_temperature);
        TvNowWindDirection = (TextView) findViewById(R.id.now_tv_winddirection);
        IvNowSky = (ImageView) findViewById(R.id.now_iv_sky);
        TvNowWindSpeed = (TextView) findViewById(R.id.now_tv_air);

        LvHourForecast = (ListView) findViewById(R.id.hourforecast_lv_weather);
        LvDailyForecast = (ListView) findViewById(R.id.dailyforecast_lv_weather);
        Button BTChoose = (Button) findViewById(R.id.weatheractivity_bt_choose);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.weather_sl_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showWeather(nowWeatherId);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(WeatherActivity.this, "已经更新天气状况", Toast.LENGTH_SHORT).show();
            }
        });

        BTChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startPlaceActivity = new Intent(WeatherActivity.this, PlaceActivity.class);
                startActivityForResult(startPlaceActivity, 1);
            }
        });
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.weather.update");
        mReceiver = new UpdateStatusReceiver();
        registerReceiver(mReceiver, mIntentFilter);
        showWeather(nowWeatherId);
    }

    private void showWeather(final String weatherId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonData = HttpUtil.sendRequest(WEATHER_ADDRESS + weatherId + WEATHER_KEY);
                List<HourForecast> hourForecasts = new ArrayList<>();
                try {
                    JSONObject jsonObject = ParseUtil.parseJSONForJSONObject(jsonData);
                    JSONArray weather = jsonObject.getJSONArray("HeWeather");
                    jsonObject = ParseUtil.parseJSONForJSONObject(weather.getJSONObject(0).toString());

                    final NowWeather nowWeather = new NowWeather();
                    nowWeather.setCity(jsonObject.getJSONObject("basic").getString("city"));
                    nowWeather.setTemperature(jsonObject.getJSONObject("now").getString("tmp") + "°");
                    nowWeather.setSky(jsonObject.getJSONObject("now").getJSONObject("cond").getString("code"));
                    nowWeather.setWindDirection(jsonObject.getJSONObject("now").getJSONObject("wind").getString("dir"));
                    if (jsonObject.isNull("aqi")) {
                        nowWeather.setAir("暂无空气质量状况");
                    } else {
                        nowWeather.setAir("空气质量" + jsonObject.getJSONObject("aqi").getJSONObject("city").getString("qlty"));
                    }
                    HandlerUtil.sendMessageToHandler(mHandler, UPDATE_WEATHER_NOW, nowWeather);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = HttpUtil.downloadPic(ICON_ADDRESS + nowWeather.getSky() + ".png");
                            Message msg = new Message();
                            msg.what = UPDATE_IMAGEVIEW;
                            msg.obj = bitmap;
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                    List<DailyForecast> dailyForecastList = new ArrayList<>();
                    for (int i = 0; i < jsonObject.getJSONArray("daily_forecast").length(); i++) {
                        DailyForecast dailyForecast = new DailyForecast();
                        dailyForecast.setDate(jsonObject.getJSONArray("daily_forecast").getJSONObject(i).getString("date"));
                        dailyForecast.setDaySky(jsonObject.getJSONArray("daily_forecast").getJSONObject(i).getJSONObject("cond").getString("code_d"));
                        dailyForecast.setNightSky(jsonObject.getJSONArray("daily_forecast").getJSONObject(i).getJSONObject("cond").getString("code_n"));
                        dailyForecast.setTemperature(jsonObject.getJSONArray("daily_forecast").getJSONObject(i).getJSONObject("tmp").getString("max") + "°~" + jsonObject.getJSONArray("daily_forecast").getJSONObject(0).getJSONObject("tmp").getString("min") + "°");
                        dailyForecast.setWindDirection(jsonObject.getJSONArray("daily_forecast").getJSONObject(i).getJSONObject("wind").getString("dir"));
                        dailyForecast.setWindSpeed(jsonObject.getJSONArray("daily_forecast").getJSONObject(i).getJSONObject("wind").getString("spd") + "km/h");
                        dailyForecastList.add(dailyForecast);
                    }
                    HandlerUtil.sendMessageToHandler(mHandler, UPDATE_WEATHER_DAILYFORECAST, dailyForecastList);

                    for (int i = 0; i < jsonObject.getJSONArray("hourly_forecast").length(); i++) {
                        HourForecast hourForecast = new HourForecast();
                        hourForecast.setTime(jsonObject.getJSONArray("hourly_forecast").getJSONObject(i).getString("date"));
                        hourForecast.setSky(jsonObject.getJSONArray("hourly_forecast").getJSONObject(i).getJSONObject("cond").getString("code"));
                        hourForecast.setTemperature(jsonObject.getJSONArray("hourly_forecast").getJSONObject(i).getString("tmp") + "°");
                        hourForecasts.add(hourForecast);
                    }
                    HandlerUtil.sendMessageToHandler(mHandler, UPDATE_WEATHER_HOURFORECAST, hourForecasts);

                    List<String> msgList = new ArrayList<>();
                    msgList.add(jsonObject.getJSONObject("basic").getString("city"));
                    msgList.add("温度" + jsonObject.getJSONObject("now").getString("tmp") + "°/" +
                            jsonObject.getJSONObject("now").getJSONObject("cond").getString("txt") + "/" +
                            jsonObject.getJSONObject("now").getJSONObject("wind").getString("dir") + "/" +
                            "风速" + jsonObject.getJSONObject("now").getJSONObject("wind").getString("spd") + "km/h");
                    msgList.add(ICON_ADDRESS + jsonObject.getJSONObject("now").getJSONObject("cond").getString("code") + ".png");

                    Intent bindIntent = new Intent(WeatherActivity.this, ShowService.class);
                    bindIntent.putStringArrayListExtra("List", (ArrayList<String>) msgList);
                    startService(bindIntent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                nowWeatherId = data.getStringExtra("weatherId");
                showWeather(nowWeatherId);
                break;
        }
    }

    public class UpdateStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            showWeather(nowWeatherId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
