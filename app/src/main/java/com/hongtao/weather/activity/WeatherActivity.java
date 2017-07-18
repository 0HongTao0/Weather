package com.hongtao.weather.activity;

import android.app.Activity;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.hongtao.weather.R;
import com.hongtao.weather.adapter.DailyForecastAdapter;
import com.hongtao.weather.adapter.HourForecastAdapter;
import com.hongtao.weather.bean.DailyForecast;
import com.hongtao.weather.bean.HourForecast;
import com.hongtao.weather.bean.NowWeather;
import com.hongtao.weather.service.ShowService;
import com.hongtao.weather.util.HandlerUtil;
import com.hongtao.weather.util.HttpUtil;
import com.hongtao.weather.util.ParseUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    private TextView mTvName, mTvNowTemperature, mTvNowWindDirection, mTvNowWindSpeed;
    private NetworkImageView mNivNowSky;
    private RecyclerView mRvDailyForecast, mRvHourForecast;
    private UpdateStatusReceiver mReceiver;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Button mBtChoose;

    private static final String WEATHER_ADDRESS = "http://guolin.tech/api/weather?cityid=";
    private static final String WEATHER_KEY = "&key=6c455039547e4d60a4da6c2e60d863b9";
    private static final String ICON_ADDRESS = "https://cdn.heweather.com/cond_icon/";

    private String nowWeatherId = "CN101280101";  //广州天气ID
    private static final int UPDATE_WEATHER_NOW = 1;
    private static final int UPDATE_WEATHER_DAILYFORECAST = 2;
    private static final int UPDATE_WEATHER_HOURFORECAST = 3;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_WEATHER_NOW:
                    NowWeather nowWeather = (NowWeather) msg.obj;
                    mTvName.setText(nowWeather.getCity());
                    mTvNowTemperature.setText(nowWeather.getTemperature());
                    mTvNowWindSpeed.setText(nowWeather.getAir());
                    mTvNowWindDirection.setText(nowWeather.getWindDirection());
                    RequestQueue queue = Volley.newRequestQueue(WeatherActivity.this);
                    ImageLoader imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
                        @Override
                        public Bitmap getBitmap(String s) {
                            return null;
                        }

                        @Override
                        public void putBitmap(String s, Bitmap bitmap) {

                        }
                    });
                    mNivNowSky.setImageUrl(ICON_ADDRESS + nowWeather.getSky() + ".png", imageLoader);
                    break;
                case UPDATE_WEATHER_DAILYFORECAST:
                    DailyForecastAdapter dailyForecastAdapter = new DailyForecastAdapter(WeatherActivity.this, (List<DailyForecast>) msg.obj);
                    LinearLayoutManager dailyLayoutManager = new LinearLayoutManager(WeatherActivity.this);
                    mRvDailyForecast.setLayoutManager(dailyLayoutManager);
                    mRvDailyForecast.setAdapter(dailyForecastAdapter);
                    break;
                case UPDATE_WEATHER_HOURFORECAST:
                    HourForecastAdapter hourForecastAdapter = new HourForecastAdapter(WeatherActivity.this, (List<HourForecast>) msg.obj);
                    LinearLayoutManager hourLayoutManager = new LinearLayoutManager(WeatherActivity.this);
                    mRvHourForecast.setLayoutManager(hourLayoutManager);
                    mRvHourForecast.setAdapter(hourForecastAdapter);
                    break;
            }
        }
    };

    public static void startWeatherActivity(String weatherId, Activity activity) {
        Intent intent = new Intent(activity, WeatherActivity.class);
        intent.putExtra("weatherId", weatherId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showWeather(nowWeatherId);
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(WeatherActivity.this, "已经更新天气状况", Toast.LENGTH_SHORT).show();
            }
        });

        mBtChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(WeatherActivity.this, PlaceActivity.class);
//                startActivityForResult(intent, 1);
                Intent intent = new Intent(WeatherActivity.this, ChoosePlaceActivity.class);
                startActivity(intent);
            }
        });

        IntentFilter mIntentFilter = new IntentFilter();
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
                try {
                    JSONObject jsonObject = ParseUtil.parseJSONForJSONObject(jsonData);
                    JSONArray weather = jsonObject.getJSONArray("HeWeather");
                    jsonObject = ParseUtil.parseJSONForJSONObject(weather.getJSONObject(0).toString());
                    showNowWeather(jsonObject);
                    showHourForecastWeather(jsonObject);
                    showDailyForecastWeather(jsonObject);
                    updateNotification(jsonObject);
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

    private void initView() {
        mTvName = (TextView) findViewById(R.id.weather_tv_where);
        mTvNowTemperature = (TextView) findViewById(R.id.now_tv_temperature);
        mTvNowWindDirection = (TextView) findViewById(R.id.now_tv_winddirection);
        mTvNowWindSpeed = (TextView) findViewById(R.id.now_tv_air);
        mBtChoose = (Button) findViewById(R.id.weatheractivity_bt_choose);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.weather_sl_refresh);
        mRvDailyForecast = (RecyclerView) findViewById(R.id.dailyforecast_rv_weather);
        mRvHourForecast = (RecyclerView) findViewById(R.id.hourforecast_rv_weather);
        mRvHourForecast.setNestedScrollingEnabled(false);
        mRvHourForecast.setNestedScrollingEnabled(false);
        mNivNowSky = (NetworkImageView) findViewById(R.id.now_iv_niv_sky);
    }

    private void showNowWeather(JSONObject jsonObject) {
        try {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showHourForecastWeather(JSONObject jsonObject) {
        List<HourForecast> hourForecasts = new ArrayList<>();
        try {
            for (int i = 0; i < jsonObject.getJSONArray("hourly_forecast").length(); i++) {
                HourForecast hourForecast = new HourForecast();
                hourForecast.setTime(jsonObject.getJSONArray("hourly_forecast").getJSONObject(i).getString("date"));
                hourForecast.setSky(jsonObject.getJSONArray("hourly_forecast").getJSONObject(i).getJSONObject("cond").getString("code"));
                hourForecast.setTemperature(jsonObject.getJSONArray("hourly_forecast").getJSONObject(i).getString("tmp") + "°");
                hourForecasts.add(hourForecast);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HandlerUtil.sendMessageToHandler(mHandler, UPDATE_WEATHER_HOURFORECAST, hourForecasts);
    }

    private void showDailyForecastWeather(JSONObject jsonObject) {
        List<DailyForecast> dailyForecastList = new ArrayList<>();
        try {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HandlerUtil.sendMessageToHandler(mHandler, UPDATE_WEATHER_DAILYFORECAST, dailyForecastList);
    }

    private void updateNotification(JSONObject jsonObject) {
        try {
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
}
