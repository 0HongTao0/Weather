package com.hongtao.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.hongtao.weather.R;
import com.hongtao.weather.adapter.PlaceAdapter;
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

/**
 * author：Administrator on 2017/7/13/013 09:11
 * email：935245421@qq.com
 */
public class PlaceActivity extends AppCompatActivity {
    private ListView LVPlace;
    private static final int UPDATE_PLACE = 1;

    private int placeType = 1;
    private static final int TYPE_PROVINCE = 1;
    private static final int TYPE_CITY = 2;
    private static final int TYPE_DISTRICT = 3;

    private final static String PLACE_ADDRESS = "http://guolin.tech/api/china";
    private String requestAddress = PLACE_ADDRESS;

    private List<Province> mProvinces;
    private List<City> mCities;
    private List<District> mDistricts;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PLACE:
                    PlaceAdapter weatherAdapter = new PlaceAdapter(PlaceActivity.this, (List<Province>) msg.obj);
                    LVPlace.setAdapter(weatherAdapter);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        LVPlace = (ListView) findViewById(R.id.weatheractivity_lv_place);
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
                        Intent intent = new Intent();
                        intent.putExtra("weatherId", mDistricts.get(position).getWeather_id());
                        setResult(RESULT_OK, intent);
                        finish();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("weatherId", "CN101280101");
        setResult(RESULT_OK, intent);
        finish();
    }
}
