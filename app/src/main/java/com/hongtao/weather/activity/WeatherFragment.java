package com.hongtao.weather.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hongtao.weather.bean.Weather;
import com.hongtao.weather.util.DividerItemDecoration;
import com.hongtao.weather.util.HandlerUtil;
import com.hongtao.weather.util.NetwordUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * author：hongtao on 2017/7/21/021 18:07
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class WeatherFragment extends Fragment {
    private TextView mTvName, mTvNowTemperature, mTvNowWindDirection, mTvNowWindSpeed;
    private NetworkImageView mNivNowSky;
    private RecyclerView mRvDailyForecast, mRvHourForecast;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Weather mWeather;
    private NowWeather mNowWeather;

    private static final String ICON_ADDRESS = "https://cdn.heweather.com/cond_icon/";

    private static final int UPDATE_WEATHER_NOW = 1;
    private static final int UPDATE_WEATHER_DAILY_FORECAST = 2;
    private static final int UPDATE_WEATHER_HOURLY_FORECAST = 3;

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
                    RequestQueue queue = Volley.newRequestQueue(getContext());
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
                case UPDATE_WEATHER_DAILY_FORECAST:
                    DailyForecastAdapter dailyForecastAdapter = new DailyForecastAdapter(getActivity(), (List<DailyForecast>) msg.obj);
                    LinearLayoutManager dailyLayoutManager = new LinearLayoutManager(getContext());
                    mRvDailyForecast.setLayoutManager(dailyLayoutManager);
                    mRvDailyForecast.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
                    mRvDailyForecast.setAdapter(dailyForecastAdapter);
                    break;
                case UPDATE_WEATHER_HOURLY_FORECAST:
                    HourForecastAdapter hourForecastAdapter = new HourForecastAdapter(getActivity(), (List<HourForecast>) msg.obj);
                    LinearLayoutManager hourLayoutManager = new LinearLayoutManager(getContext());
                    mRvHourForecast.setLayoutManager(hourLayoutManager);
                    mRvHourForecast.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
                    mRvHourForecast.setAdapter(hourForecastAdapter);
                    break;
            }
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (NetwordUtil.netIsWork(getActivity())) {
            showNowWeather(mWeather);
            showHourForecastWeather(mWeather);
            showDailyForecastWeather(mWeather);
        } else {
            HandlerUtil.sendMessageToHandler(mHandler, UPDATE_WEATHER_NOW, mNowWeather);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        initView(view);
        return view;
    }

    public void setOnlineDataInFragment(Weather weather) {
        this.mWeather = weather;
    }

    public void setOfflineDataInFragment(NowWeather nowWeather) {
        this.mNowWeather = nowWeather;
    }

    private void initView(View view) {
        mTvName = (TextView) view.findViewById(R.id.weather_tv_where);
        mTvNowTemperature = (TextView) view.findViewById(R.id.now_tv_temperature);
        mTvNowWindDirection = (TextView) view.findViewById(R.id.now_tv_winddirection);
        mTvNowWindSpeed = (TextView) view.findViewById(R.id.now_tv_air);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.weather_sl_refresh);
        mRvDailyForecast = (RecyclerView) view.findViewById(R.id.dailyforecast_rv_weather);
        mRvHourForecast = (RecyclerView) view.findViewById(R.id.hourforecast_rv_weather);
        mRvHourForecast.setNestedScrollingEnabled(false);
        mRvHourForecast.setNestedScrollingEnabled(false);
        mNivNowSky = (NetworkImageView) view.findViewById(R.id.now_iv_niv_sky);
        if (NetwordUtil.netIsWork(getActivity())) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "已经更新天气状况", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void showDailyForecastWeather(Weather weather) {
        List<DailyForecast> dailyForecastList = new ArrayList<>();

        for (int i = 0; i < weather.getHeWeather().get(0).getDailyForecast().size(); i++) {
            DailyForecast dailyForecast = new DailyForecast();
            dailyForecast.setDate(weather.getHeWeather().get(0).getDailyForecast().get(i).getDate());
            dailyForecast.setDaySky(weather.getHeWeather().get(0).getDailyForecast().get(i).getCond().getCodeDay());
            dailyForecast.setNightSky(weather.getHeWeather().get(0).getDailyForecast().get(i).getCond().getCodeNight());
            dailyForecast.setTemperature(weather.getHeWeather().get(0).getDailyForecast().get(i).getTmp().getMinTemperature() + "°~" + weather.getHeWeather().get(0).getDailyForecast().get(i).getTmp().getMaxTemperature() + "°");
            dailyForecast.setWindDirection(weather.getHeWeather().get(0).getDailyForecast().get(i).getWind().getWindDirection());
            dailyForecast.setWindSpeed(weather.getHeWeather().get(0).getDailyForecast().get(i).getWind().getWindSpeed() + "km/h");
            dailyForecastList.add(dailyForecast);
        }
        HandlerUtil.sendMessageToHandler(mHandler, UPDATE_WEATHER_DAILY_FORECAST, dailyForecastList);
    }


    private void showNowWeather(Weather weather) {
        NowWeather nowWeather = new NowWeather();
        nowWeather.setCity(weather.getHeWeather().get(0).getBasic().getCity());
        nowWeather.setTemperature(weather.getHeWeather().get(0).getNow().getTemperature());
        nowWeather.setSky(weather.getHeWeather().get(0).getNow().getCond().getCode());
        nowWeather.setWindDirection(weather.getHeWeather().get(0).getNow().getWind().getWindDirection());
        nowWeather.setAir(weather.getHeWeather().get(0).getAqi().getCity().getQlty());
        HandlerUtil.sendMessageToHandler(mHandler, UPDATE_WEATHER_NOW, nowWeather);
    }

    private void showHourForecastWeather(Weather weather) {
        List<HourForecast> hourForecasts = new ArrayList<>();
        for (int i = 0; i < weather.getHeWeather().get(0).getHourlyForecast().size(); i++) {
            HourForecast hourForecast = new HourForecast();
            hourForecast.setTime(weather.getHeWeather().get(0).getHourlyForecast().get(i).getDate());
            hourForecast.setSky(weather.getHeWeather().get(0).getHourlyForecast().get(i).getCond().getCode());
            hourForecast.setTemperature(weather.getHeWeather().get(0).getHourlyForecast().get(i).getTemperature() + "°");
            hourForecasts.add(hourForecast);
        }
        HandlerUtil.sendMessageToHandler(mHandler, UPDATE_WEATHER_HOURLY_FORECAST, hourForecasts);
    }

}
