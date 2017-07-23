package com.hongtao.weather.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.hongtao.weather.DateBase.PlaceDatabaseDeal;
import com.hongtao.weather.R;
import com.hongtao.weather.adapter.PlaceAdapter;
import com.hongtao.weather.adapter.WeatherViewPagerAdapter;
import com.hongtao.weather.bean.City;
import com.hongtao.weather.bean.District;
import com.hongtao.weather.bean.NowWeather;
import com.hongtao.weather.bean.Place;
import com.hongtao.weather.bean.Province;
import com.hongtao.weather.bean.Weather;
import com.hongtao.weather.service.ShowService;
import com.hongtao.weather.util.DividerItemDecoration;
import com.hongtao.weather.util.DiyGSONRequest;
import com.hongtao.weather.util.NetwordUtil;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private UpdateStatusReceiver mReceiver;
    private ViewPager mVpWeather;
    private RecyclerView mRvPlace;
    private List<Fragment> mFragments = new ArrayList<>();
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private PlaceAdapter mPlaceAdapter = new PlaceAdapter();

    private final static String PLACE_ADDRESS = "http://guolin.tech/api/china";
    private final static String PLACE_TYPE_PROVINCE = "Province";
    private final static String PLACE_TYPE_CITY = "City";
    private final static String PLACE_TYPE_DISTRICT = "District";

    private static final String WEATHER_ADDRESS = "http://guolin.tech/api/weather?cityid=";
    private static final String WEATHER_KEY = "&key=6c455039547e4d60a4da6c2e60d863b9";
    private static final String ICON_ADDRESS = "https://cdn.heweather.com/cond_icon/";

    private String nowWeatherId = "CN101280101";  //广州天气ID


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();
        if (!getSharedPreferences("now", MODE_PRIVATE).getString("now_weather_id", "nothing").equals("nothing")) {
            SharedPreferences sharedPreferences = getSharedPreferences("now", MODE_PRIVATE);
            nowWeatherId = sharedPreferences.getString("now_weather_id", "");
        }

        if (NetwordUtil.netIsWork(WeatherActivity.this)) {
            displayPlaceList();
            IntentFilter mIntentFilter = new IntentFilter();
            mIntentFilter.addAction("com.weather.update");
            mReceiver = new UpdateStatusReceiver();
            registerReceiver(mReceiver, mIntentFilter);
            showWeather(nowWeatherId);
        } else {
            NowWeather nowWeather = getNowWeatherFromSP();
            WeatherFragment weatherFragment = new WeatherFragment();
            weatherFragment.setOfflineDataInFragment(nowWeather);
            mFragments.add(weatherFragment);
            WeatherViewPagerAdapter adapter = new WeatherViewPagerAdapter(mFragmentManager, mFragments);
            mVpWeather.setAdapter(adapter);
        }
    }

    private void showWeather(final String weatherId) {
        StringRequest stringRequest = new StringRequest(WEATHER_ADDRESS + weatherId + WEATHER_KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                Weather weather = gson.fromJson(s, Weather.class);
                WeatherFragment fragment = new WeatherFragment();
                fragment.setOnlineDataInFragment(weather);
                mFragments.add(fragment);
                WeatherViewPagerAdapter adapter = new WeatherViewPagerAdapter(mFragmentManager, mFragments);
                mVpWeather.setAdapter(adapter);
                updateNotification(weather);
                saveNowWeatherInSP(weather);
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

    private void saveNowWeatherIdInSP() {
        SharedPreferences.Editor editor = getSharedPreferences("now", MODE_PRIVATE).edit();
        editor.putString("now_weather_id", nowWeatherId);
        editor.apply();
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

    private void displayPlaceList() {
        if (!PlaceDatabaseDeal.isTableExit()) {
            saveProvinceInDatabase();
        } else {
            List<Place> placeList = PlaceDatabaseDeal.searchPlaceByCityType(PLACE_TYPE_PROVINCE);
            updateRecyclerView(placeList, mRvPlace, mPlaceAdapter);
        }
        mPlaceAdapter.setAdapterCallBack(new PlaceAdapter.ItemOnClickCallBackListener() {
            @Override
            public void onClickCallBackPlace(Place place) {
                switch (place.getPlaceType()) {
                    case PLACE_TYPE_PROVINCE:
                        if (PlaceDatabaseDeal.isPlaceExit(place.getId(), PLACE_TYPE_CITY)) {
                            mPlaceAdapter.updatePlaceList(PlaceDatabaseDeal.searchPlaceByAboveId(place.getId()));
                        } else {
                            saveCityInDatabase(place);
                        }
                        break;
                    case PLACE_TYPE_CITY:
                        if (PlaceDatabaseDeal.isPlaceExit(place.getId(), PLACE_TYPE_DISTRICT)) {
                            mPlaceAdapter.updatePlaceList(PlaceDatabaseDeal.searchPlaceByAboveId(place.getId()));
                        } else {
                            saveDistrictInDatabase(place);
                        }
                        break;
                    case PLACE_TYPE_DISTRICT:
                        showWeather(place.getWeatherId());
                        nowWeatherId = place.getWeatherId();
                        List<Place> placeList = PlaceDatabaseDeal.searchPlaceByCityType(PLACE_TYPE_PROVINCE);
                        updateRecyclerView(placeList, mRvPlace, mPlaceAdapter);
                        break;
                }
            }
        });
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.weather_dl);
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.weather_nv_place);
        mRvPlace = (RecyclerView) mNavigationView.getHeaderView(0).findViewById(R.id.weather_rv_choose);
        mVpWeather = (ViewPager) findViewById(R.id.weather_vp_message);
        FloatingActionButton mBtChoose = (FloatingActionButton) findViewById(R.id.weather_bt_choose);
        mBtChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeatherActivity.this, "已经设置该城市为当前城市", Toast.LENGTH_SHORT).show();
                saveNowWeatherIdInSP();
            }
        });
    }

    private void saveProvinceInDatabase() {
        DiyGSONRequest<Province> request = new DiyGSONRequest<>(PLACE_ADDRESS, Province.class, new Response.Listener<List<Province>>() {
            @Override
            public void onResponse(List<Province> provinces) {
                List<Place> placeList = new ArrayList<>();
                for (int i = 0; i < provinces.size(); i++) {
                    Place place = new Place();
                    place.setAboveId("nothing");
                    place.setWeatherId("nothing");
                    place.setId(provinces.get(i).getId());
                    place.setName(provinces.get(i).getName());
                    place.setPlaceType(PLACE_TYPE_PROVINCE);
                    placeList.add(place);
                }
                updateRecyclerView(placeList, mRvPlace, mPlaceAdapter);
                PlaceDatabaseDeal.addPlace(placeList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
            }
        });
        WeatherApplication.getRequestQueue().add(request);
    }

    private void saveCityInDatabase(Place place) {
        final String aboveId = place.getId();
        DiyGSONRequest<City> request = new DiyGSONRequest<>(PLACE_ADDRESS + "/" + place.getId(), City.class, new Response.Listener<List<City>>() {
            @Override
            public void onResponse(List<City> cities) {
                List<Place> placeList = new ArrayList<>();
                for (int i = 0; i < cities.size(); i++) {
                    Place place = new Place();
                    place.setAboveId(aboveId);
                    place.setWeatherId("nothing");
                    place.setId(cities.get(i).getId());
                    place.setName(cities.get(i).getName());
                    place.setPlaceType(PLACE_TYPE_CITY);
                    placeList.add(place);
                }
                updateRecyclerView(placeList, mRvPlace, mPlaceAdapter);
                PlaceDatabaseDeal.addPlace(placeList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
            }
        });
        WeatherApplication.getRequestQueue().add(request);
    }

    private void saveDistrictInDatabase(Place place) {
        final String aboveId = place.getId();
        StringBuilder address = new StringBuilder();
        address.append(PLACE_ADDRESS).append("/").append(place.getAboveId()).append("/").append(place.getId());
        DiyGSONRequest<District> request = new DiyGSONRequest<>(address.toString(), District.class, new Response.Listener<List<District>>() {
            @Override
            public void onResponse(List<District> districts) {
                List<Place> placeList = new ArrayList<>();
                for (int i = 0; i < districts.size(); i++) {
                    Place place = new Place();
                    place.setAboveId(aboveId);
                    place.setWeatherId(districts.get(i).getWeatherId());
                    place.setId(districts.get(i).getId());
                    place.setName(districts.get(i).getName());
                    place.setPlaceType(PLACE_TYPE_DISTRICT);
                    placeList.add(place);
                }
                updateRecyclerView(placeList, mRvPlace, mPlaceAdapter);
                PlaceDatabaseDeal.addPlace(placeList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
            }
        });
        WeatherApplication.getRequestQueue().add(request);
    }

    private void updateRecyclerView(List<Place> places, RecyclerView recyclerView, PlaceAdapter placeAdapter) {
        placeAdapter.updatePlaceList(places);
        recyclerView.setLayoutManager(new LinearLayoutManager(WeatherActivity.this));
        recyclerView.setAdapter(placeAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(WeatherActivity.this, DividerItemDecoration.VERTICAL_LIST));
        mPlaceAdapter.notifyDataSetChanged();
    }
}
