package com.hongtao.weather.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hongtao.weather.R;
import com.hongtao.weather.adapter.HourForecastAdapter;
import com.hongtao.weather.bean.HourForecast;
import com.hongtao.weather.service.ShowService;
import com.hongtao.weather.util.HttpUtil;
import com.hongtao.weather.util.ParseUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    private TextView TVName, TVToday, TVForecast1, TVForecast2, TVForecast3;
    private ListView LVHourForecast;

    private static final String WEATHER_ADDRESS = "http://guolin.tech/api/weather?cityid=";
    private static final String WEATHER_KEY = "&key=6c455039547e4d60a4da6c2e60d863b9";

    private static final int UPDATE_MESSAGE_TEXT = 1;
    private static final int UPDATE_MESSAGE_LIST = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_MESSAGE_TEXT:
                    List<String> textList = (List<String>) msg.obj;
                    TVName.setText(textList.get(0));
                    TVToday.setText(textList.get(1));
                    TVForecast1.setText(textList.get(2));
                    TVForecast2.setText(textList.get(3));
                    TVForecast3.setText(textList.get(4));
                    break;
                case UPDATE_MESSAGE_LIST:
                    HourForecastAdapter adapter = new HourForecastAdapter(WeatherActivity.this, (List<HourForecast>) msg.obj);
                    LVHourForecast.setAdapter(adapter);
                    break;
            }
        }
    };

    private List<String> msgList = new ArrayList();
    private ShowService.ShowBinder showBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            showBinder = (ShowService.ShowBinder) service;
            showBinder.updateStatus(msgList);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        TVToday = (TextView) findViewById(R.id.weather_tv_today);
        TVName = (TextView) findViewById(R.id.weather_tv_where);
        TVForecast1 = (TextView) findViewById(R.id.weather_tv_forecast1);
        TVForecast2 = (TextView) findViewById(R.id.weather_tv_forecast2);
        TVForecast3 = (TextView) findViewById(R.id.weather_tv_forecast3);
        LVHourForecast = (ListView) findViewById(R.id.forecast_lv_weather);
        Button BTChoose = (Button) findViewById(R.id.weatheractivity_bt_choose);

        BTChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startPlaceActivity = new Intent(WeatherActivity.this, PlaceActivity.class);
                startActivityForResult(startPlaceActivity, 1);
            }
        });

        showWeather("CN101280101");
    }

    private void showWeather(final String weatherId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonData = HttpUtil.sendRequest(WEATHER_ADDRESS + weatherId + WEATHER_KEY);
                ArrayList<String> textList = new ArrayList();
                List<HourForecast> hourForecasts = new ArrayList<>();
                try {
                    JSONObject jsonObject = ParseUtil.parseJSONForJSONObject(jsonData);
                    JSONArray weather = jsonObject.getJSONArray("HeWeather");
                    jsonObject = ParseUtil.parseJSONForJSONObject(weather.getJSONObject(0).toString());
                    textList.add(jsonObject.getJSONObject("basic").getString("city"));
                    textList.add("温度" + jsonObject.getJSONObject("now").getString("tmp") + "            " +
                            jsonObject.getJSONObject("now").getJSONObject("cond").getString("txt") + "            \n" +
                            jsonObject.getJSONObject("now").getJSONObject("wind").getString("dir") + "            " +
                            "风力" + jsonObject.getJSONObject("now").getJSONObject("wind").getString("spd"));
                    for (int i = 0; i <= 2; i++) {
                        textList.add(jsonObject.getJSONArray("daily_forecast").getJSONObject(0).getString("date") + "\n" +
                                "最高温度" + jsonObject.getJSONArray("daily_forecast").getJSONObject(0).getJSONObject("tmp").getString("max") + "/最低温度" + jsonObject.getJSONArray("daily_forecast").getJSONObject(0).getJSONObject("tmp").getString("min") + "                        " +
                                "白天" + jsonObject.getJSONArray("daily_forecast").getJSONObject(0).getJSONObject("cond").getString("txt_d") + "/夜晚" + jsonObject.getJSONArray("daily_forecast").getJSONObject(0).getJSONObject("cond").getString("txt_d") + "            \n" +
                                jsonObject.getJSONArray("daily_forecast").getJSONObject(0).getJSONObject("wind").getString("dir") + "                                          " +
                                "风力" + jsonObject.getJSONArray("daily_forecast").getJSONObject(0).getJSONObject("wind").getString("spd"));
                    }

                    for (int i = 0; i < jsonObject.getJSONArray("hourly_forecast").length(); i++) {
                        HourForecast hourForecast = new HourForecast();
                        hourForecast.setTime(jsonObject.getJSONArray("hourly_forecast").getJSONObject(i).getString("date"));
                        hourForecast.setWea(jsonObject.getJSONArray("hourly_forecast").getJSONObject(i).getJSONObject("cond").getString("txt"));
                        hourForecast.setTem(jsonObject.getJSONArray("hourly_forecast").getJSONObject(i).getString("tmp") + "°");
                        hourForecasts.add(hourForecast);
                    }

                    msgList.add(jsonObject.getJSONObject("basic").getString("city"));
                    msgList.add("温度" + jsonObject.getJSONObject("now").getString("tmp") + "            " +
                            jsonObject.getJSONObject("now").getJSONObject("cond").getString("txt") + "\n" +
                            jsonObject.getJSONObject("now").getJSONObject("wind").getString("dir") + "            " +
                            "风力" + jsonObject.getJSONObject("now").getJSONObject("wind").getString("spd"));
                    Intent bindIntent = new Intent(WeatherActivity.this, ShowService.class);
                    bindService(bindIntent, connection, BIND_AUTO_CREATE);

                    Message updateTextMsg = new Message();
                    updateTextMsg.obj = textList;
                    updateTextMsg.what = UPDATE_MESSAGE_TEXT;
                    mHandler.sendMessage(updateTextMsg);

                    Message updateListMsg = new Message();
                    updateListMsg.obj = hourForecasts;
                    updateListMsg.what = UPDATE_MESSAGE_LIST;
                    mHandler.sendMessage(updateListMsg);
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
                showWeather(data.getStringExtra("weatherId"));
                break;
        }
    }
}
