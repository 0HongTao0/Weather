package com.hongtao.weather.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.hongtao.weather.R;
import com.hongtao.weather.adapter.WeatherViewPagerAdapter;
import com.hongtao.weather.bean.NowWeather;
import com.hongtao.weather.bean.Weather;
import com.hongtao.weather.service.ShowService;
import com.hongtao.weather.util.NetwordUtil;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    private UpdateStatusReceiver mReceiver;
    private Button mBtChoose;
    private ViewPager mVpWeather;
    private List<Fragment> mFragments = new ArrayList<>();
    private FragmentManager mFragmentManager = getSupportFragmentManager();

    private static final String WEATHER_ADDRESS = "http://guolin.tech/api/weather?cityid=";
    private static final String WEATHER_KEY = "&key=6c455039547e4d60a4da6c2e60d863b9";
    private static final String ICON_ADDRESS = "https://cdn.heweather.com/cond_icon/";

    private String nowWeatherId = "CN101280101";  //广州天气ID


    public static void startWeatherActivity(String weatherId, Activity activity) {
        Intent intent = new Intent(activity, WeatherActivity.class);
        intent.putExtra("weatherId", weatherId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mVpWeather = (ViewPager) findViewById(R.id.weather_vp_message);
        mBtChoose = (Button) findViewById(R.id.weatheractivity_bt_choose);
        mBtChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, PlaceActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        if (NetwordUtil.netIsWork(WeatherActivity.this)) {
            IntentFilter mIntentFilter = new IntentFilter();
            mIntentFilter.addAction("com.weather.update");
            mReceiver = new UpdateStatusReceiver();
            registerReceiver(mReceiver, mIntentFilter);
            showWeather(nowWeatherId);
        } else {
            NowWeather nowWeather = getNowWeatherFromSP();

        }
    }

    private void showWeather(final String weatherId) {
        StringRequest stringRequest = new StringRequest(WEATHER_ADDRESS + weatherId + WEATHER_KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                Weather weather = gson.fromJson(s, Weather.class);
                saveNowWeatherInSP(weather);
                WeatherFragment fragment = new WeatherFragment();
                fragment.setOnlineDataInFragment(weather);
                mFragments.add(fragment);
                WeatherViewPagerAdapter adapter = new WeatherViewPagerAdapter(mFragmentManager, mFragments);
                mVpWeather.setAdapter(adapter);
                updateNotification(weather);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
            }
        });
        WeatherApplication.getRequestQueue().add(stringRequest);
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


    private void saveNowWeatherInSP(Weather weather) {
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("now_weather_city", weather.getHeWeather().get(0).getBasic().getCity());
        editor.putString("now_weather_temperature", weather.getHeWeather().get(0).getNow().getTemperature());
        editor.putString("now_weather_sky", weather.getHeWeather().get(0).getNow().getCond().getCode());
        editor.putString("now_weather_wind_direction", weather.getHeWeather().get(0).getNow().getWind().getWindDirection());
        editor.putString("now_weather_air", weather.getHeWeather().get(0).getAqi().getCity().getQlty());
        editor.apply();
    }

    private NowWeather getNowWeatherFromSP() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        NowWeather nowWeather = new NowWeather();
        nowWeather.setCity(sharedPreferences.getString("now_weather_city", ""));
        nowWeather.setTemperature(sharedPreferences.getString("now_weather_temperature", ""));
        nowWeather.setSky(sharedPreferences.getString("now_weather_sky", ""));
        nowWeather.setWindDirection(sharedPreferences.getString("now_weather_wind_direction", ""));
        nowWeather.setAir(sharedPreferences.getString("now_weather_air", ""));
        return nowWeather;
    }


    private void updateNotification(Weather weather) {
        List<String> msgList = new ArrayList<>();
        msgList.add(weather.getHeWeather().get(0).getBasic().getCity());
        msgList.add("温度" + weather.getHeWeather().get(0).getNow().getTemperature() + "°/" +
                weather.getHeWeather().get(0).getNow().getWind().getWindDirection() + "/" +
                "风速" + weather.getHeWeather().get(0).getNow().getWind().getWindSpeed() + "km/h");
        msgList.add(ICON_ADDRESS + weather.getHeWeather().get(0).getNow().getCond().getCode() + ".png");

        Intent bindIntent = new Intent(WeatherActivity.this, ShowService.class);
        bindIntent.putStringArrayListExtra("List", (ArrayList<String>) msgList);
        startService(bindIntent);

    }
}
