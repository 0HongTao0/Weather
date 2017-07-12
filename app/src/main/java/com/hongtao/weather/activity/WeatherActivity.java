package com.hongtao.weather.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hongtao.weather.R;
import com.hongtao.weather.adapter.PlaceAdapter;
import com.hongtao.weather.bean.Basic;
import com.hongtao.weather.bean.City;
import com.hongtao.weather.bean.District;
import com.hongtao.weather.bean.Province;
import com.hongtao.weather.util.HttpUtil;
import com.hongtao.weather.util.ParseUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    private ListView LVPlace;
    private TextView TVName, TVToday, TVNext1, TVNext2, TVForcast1, TVForcast2, TVForcast3;

    private final static String PLACE_ADDRESS = "http://guolin.tech/api/china";
    private static final String WEATHER_ADDRESS = "http://guolin.tech/api/weather?cityid=";
    private static final String WEATHER_KEY = "&key=6c455039547e4d60a4da6c2e60d863b9";

    private static final int TYPE_PROVINCE = 1;
    private static final int TYPE_CITY = 2;
    private static final int TYPE_DISTRICT = 3;

    private int placeType = 1;
    private static final int UPDATE_PLACE = 1;
    private static final int UPDATE_MESSAGE_TEXT = 2;
    private String requestAddress = PLACE_ADDRESS;

    private List<Province> mProvinces;
    private List<City> mCities;
    private List<District> mDistricts;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PLACE:
                    PlaceAdapter weatherAdapter = new PlaceAdapter(WeatherActivity.this, (List<Province>) msg.obj);
                    LVPlace.setAdapter(weatherAdapter);
                    break;
                case UPDATE_MESSAGE_TEXT:
                    List<String> textList = (List<String>) msg.obj;
                    TVName.setText(textList.get(0));
                    TVToday.setText(textList.get(1));
                    TVNext1.setText(textList.get(2));
                    TVNext2.setText(textList.get(3));
                    TVForcast1.setText(textList.get(4));
                    TVForcast2.setText(textList.get(5));
                    TVForcast3.setText(textList.get(6));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        LVPlace = (ListView) findViewById(R.id.weatheractivity_lv_place);
        TVToday = (TextView) findViewById(R.id.fragment_tv_today);
        TVName = (TextView) findViewById(R.id.fragment_tv_where);
        TVNext1 = (TextView) findViewById(R.id.fragment_tv_next1);
        TVNext2 = (TextView) findViewById(R.id.fragment_tv_next2);
        TVForcast1 = (TextView) findViewById(R.id.fragment_lv_forecast1);
        TVForcast2 = (TextView) findViewById(R.id.fragment_lv_forecast2);
        TVForcast3 = (TextView) findViewById(R.id.fragment_lv_forecast3);
        showProvince(PLACE_ADDRESS);
        LVPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (placeType) {
                    case TYPE_PROVINCE:
                        requestAddress = requestAddress + "/" + mProvinces.get(position).getId();
                        showCity(requestAddress);
                        placeType = 2;
                        break;
                    case TYPE_CITY:
                        requestAddress = requestAddress + "/" + mCities.get(position).getId();
                        showDistrict(requestAddress);
                        placeType = 3;
                        break;
                    case TYPE_DISTRICT:
                        showWeather(mDistricts.get(position).getWeather_id());
                        break;
                }
            }
        });
        Button button = (Button) findViewById(R.id.fragment_bt_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (placeType) {
                    case TYPE_DISTRICT:
                        requestAddress = requestAddress.substring(0, requestAddress.lastIndexOf("/"));
                        showCity(requestAddress);
                        placeType = 2;
                        break;
                    case TYPE_CITY:
                        requestAddress = PLACE_ADDRESS;
                        showProvince(requestAddress);
                        placeType = 1;
                        break;
                }
            }
        });
    }

    private void showWeather(final String weatherId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonData = HttpUtil.sendRequest(WEATHER_ADDRESS + weatherId + WEATHER_KEY);
                Basic basic = new Basic();
                ArrayList<String> textList = new ArrayList();
                try {
                    JSONObject jsonObject = ParseUtil.parseJSONForJSONObject(jsonData);
                    JSONArray weather = jsonObject.getJSONArray("HeWeather");
                    jsonObject = ParseUtil.parseJSONForJSONObject(weather.getJSONObject(0).toString());
                    textList.add(jsonObject.getJSONObject("basic").getString("city"));
                    textList.add("温度" + jsonObject.getJSONObject("now").getString("tmp") + "            " +
                            jsonObject.getJSONObject("now").getJSONObject("cond").getString("txt") + "            \n" +
                            jsonObject.getJSONObject("now").getJSONObject("wind").getString("dir") + "            " +
                            "风力" + jsonObject.getJSONObject("now").getJSONObject("wind").getString("spd"));
                    if (jsonObject.getJSONArray("hourly_forecast").length() == 2) {
                        for (int i = 0; i <= 1; i++) {
                            textList.add(jsonObject.getJSONArray("hourly_forecast").getJSONObject(i).getString("date") + "\n" +
                                    "温度" + jsonObject.getJSONArray("hourly_forecast").getJSONObject(i).getString("tmp") + "            " +
                                    jsonObject.getJSONArray("hourly_forecast").getJSONObject(i).getJSONObject("cond").getString("txt") + "            \n" +
                                    jsonObject.getJSONArray("hourly_forecast").getJSONObject(i).getJSONObject("wind").getString("dir") + "            " +
                                    "风力" + jsonObject.getJSONArray("hourly_forecast").getJSONObject(i).getJSONObject("wind").getString("spd"));
                        }
                    } else {
                        textList.add(jsonObject.getJSONArray("hourly_forecast").getJSONObject(0).getString("date") + "\n" +
                                "温度" + jsonObject.getJSONArray("hourly_forecast").getJSONObject(0).getString("tmp") + "            " +
                                jsonObject.getJSONArray("hourly_forecast").getJSONObject(0).getJSONObject("cond").getString("txt") + "            \n" +
                                jsonObject.getJSONArray("hourly_forecast").getJSONObject(0).getJSONObject("wind").getString("dir") + "            " +
                                "风力" + jsonObject.getJSONArray("hourly_forecast").getJSONObject(0).getJSONObject("wind").getString("spd"));
                        textList.add("暂时没有");
                    }
                    for (int i = 0; i <= 2; i++) {
                        textList.add(jsonObject.getJSONArray("daily_forecast").getJSONObject(0).getString("date") + "\n" +
                                "最高温度" + jsonObject.getJSONArray("daily_forecast").getJSONObject(0).getJSONObject("tmp").getString("max") + "/最低温度" + jsonObject.getJSONArray("daily_forecast").getJSONObject(0).getJSONObject("tmp").getString("min") + "            " +
                                "白天" + jsonObject.getJSONArray("daily_forecast").getJSONObject(0).getJSONObject("cond").getString("txt_d") + "/夜晚" + jsonObject.getJSONArray("daily_forecast").getJSONObject(0).getJSONObject("cond").getString("txt_d") + "            \n" +
                                jsonObject.getJSONArray("daily_forecast").getJSONObject(0).getJSONObject("wind").getString("dir") + "            " +
                                "风力" +jsonObject.getJSONArray("daily_forecast").getJSONObject(0).getJSONObject("wind").getString("spd"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = textList;
                msg.what = UPDATE_MESSAGE_TEXT;
                mHandler.sendMessage(msg);
            }
        }).start();

    }

    private void showDistrict(final String Address) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<District> districts = new ArrayList();
                mDistricts = districts;
                String jsonData = HttpUtil.sendRequest(Address);
                JSONArray jsonArray = ParseUtil.parseJSONForJSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        District district = new District();
                        district.setId(jsonObject.getString("id"));
                        district.setName(jsonObject.getString("name"));
                        district.setWeather_id(jsonObject.getString("weather_id"));
                        districts.add(district);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Message msg = new Message();
                msg.obj = districts;
                msg.what = UPDATE_PLACE;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    private void showCity(final String Address) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<City> cities = new ArrayList();
                mCities = cities;
                String jsonData = HttpUtil.sendRequest(Address);
                JSONArray jsonArray = ParseUtil.parseJSONForJSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        City city = new City();
                        city.setId(jsonObject.getString("id"));
                        city.setName(jsonObject.getString("name"));
                        cities.add(city);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Message msg = new Message();
                msg.obj = cities;
                msg.what = UPDATE_PLACE;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    private void showProvince(final String Address) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Province> provinces = new ArrayList();
                mProvinces = provinces;
                String jsonData = HttpUtil.sendRequest(Address);
                JSONArray jsonArray = ParseUtil.parseJSONForJSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Province province = new Province();
                        province.setId(jsonObject.getString("id"));
                        province.setName(jsonObject.getString("name"));
                        provinces.add(province);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Message msg = new Message();
                msg.obj = provinces;
                msg.what = UPDATE_PLACE;
                mHandler.sendMessage(msg);
            }
        }).start();
    }
}
