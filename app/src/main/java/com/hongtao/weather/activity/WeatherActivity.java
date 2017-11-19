package com.hongtao.weather.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.hongtao.weather.dateBase.PlaceDatabaseDeal;
import com.hongtao.weather.R;
import com.hongtao.weather.adapter.PlaceAdapter;
import com.hongtao.weather.adapter.WeatherViewPagerAdapter;
import com.hongtao.weather.bean.City;
import com.hongtao.weather.bean.District;
import com.hongtao.weather.bean.NowWeather;
import com.hongtao.weather.bean.Place;
import com.hongtao.weather.bean.Province;
import com.hongtao.weather.bean.Weather;
import com.hongtao.weather.fragment.LoadFragment;
import com.hongtao.weather.fragment.WeatherFragment;
import com.hongtao.weather.service.ShowService;
import com.hongtao.weather.util.DepthPageTransformer;
import com.hongtao.weather.util.DividerItemDecoration;
import com.hongtao.weather.util.DiyGSONRequest;
import com.hongtao.weather.util.NetwordUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends AppCompatActivity {

    @BindView(R.id.weather_tl_tab)
    TabLayout mWeatherTlTab;
    @BindView(R.id.weather_vp_message)
    ViewPager mWeatherVpMessage;
    @BindView(R.id.weather_bt_choose)
    FloatingActionButton mWeatherBtChoose;
    @BindView(R.id.weather_nv_place)
    NavigationView mWeatherNvPlace;
    @BindView(R.id.weather_dl)
    DrawerLayout mWeatherDl;

    private UpdateStatusReceiver mReceiver;
    private RecyclerView mRvPlace;
    private List<Fragment> mFragments = new ArrayList<>();
    private List<Place> mPlaceList = new ArrayList<>();
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private static PlaceAdapter mPlaceAdapter;
    private static WeatherViewPagerAdapter mWeatherViewPagerAdapter;

    private final static String PLACE_ADDRESS = "http://guolin.tech/api/china";
    private final static String PLACE_TYPE_PROVINCE = "Province";
    private final static String PLACE_TYPE_CITY = "City";
    private final static String PLACE_TYPE_DISTRICT = "District";

    private static final String WEATHER_ADDRESS = "http://guolin.tech/api/weather?cityid=";
    private static final String WEATHER_KEY = "&key=6c455039547e4d60a4da6c2e60d863b9";
    private static final String ICON_ADDRESS = "https://cdn.heweather.com/cond_icon/";

    private String nowWeatherId = "CN101280101";  //广州天气ID

    private static final String NOTHING = "nothing";

    private static final String SP_DATA = "data";
    private static final String NOW_WEATHER_CITY = "now_weather_city";
    private static final String NOW_WEATHER_TEMPERATURE = "now_weather_temperature";
    private static final String NOW_WEATHER_SKY = "now_weather_sky";
    private static final String NOW_WEATHER_WIND_DIRECTION = "now_weather_wind_direction";
    private static final String NOW_WEATHER_AIR = "now_weather_air";

    private static final String NOW_WEATHER_ID = "now_weather_id";
    private static final String SP_NOW = "now";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        initView();
        if (!getSharedPreferences(SP_NOW, MODE_PRIVATE).getString(NOW_WEATHER_ID, NOTHING).equals(NOTHING)) {
            SharedPreferences sharedPreferences = getSharedPreferences(SP_NOW, MODE_PRIVATE);
            nowWeatherId = sharedPreferences.getString(NOW_WEATHER_ID, "");
        }

        if (NetwordUtil.netIsWork(WeatherActivity.this)) {
            mPlaceAdapter = new PlaceAdapter();
            displayPlaceList();
            IntentFilter mIntentFilter = new IntentFilter();
            mIntentFilter.addAction("com.weather.update");
            mReceiver = new UpdateStatusReceiver();
            registerReceiver(mReceiver, mIntentFilter);
            showWeather(nowWeatherId);
        } else {
            NowWeather nowWeather = getNowWeatherFromSP();
            WeatherFragment weatherFragment = WeatherFragment.newInstance(nowWeather);
            mFragments.add(weatherFragment);
            WeatherViewPagerAdapter adapter = new WeatherViewPagerAdapter(mFragmentManager, mFragments);
            mWeatherVpMessage.setAdapter(adapter);
        }
    }

    private void showWeather(final String weatherId) {
        StringRequest stringRequest = new StringRequest(WEATHER_ADDRESS + weatherId + WEATHER_KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LoadFragment loadFragment = new LoadFragment();
                        loadFragment.show(getFragmentManager(), "load");
                    }
                }).start();
                Gson gson = new Gson();
                Weather weather = gson.fromJson(s, Weather.class);
                WeatherFragment fragment = WeatherFragment.newInstance(weather);
                setFragmentListener(fragment);
                mFragments.add(fragment);
                mWeatherViewPagerAdapter = new WeatherViewPagerAdapter(mFragmentManager, mFragments);
                mWeatherVpMessage.setAdapter(mWeatherViewPagerAdapter);
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
        SharedPreferences.Editor editor = getSharedPreferences(SP_DATA, MODE_PRIVATE).edit();
        editor.putString(NOW_WEATHER_CITY, weather.getHeWeather().get(0).getBasic().getCity());
        editor.putString(NOW_WEATHER_TEMPERATURE, weather.getHeWeather().get(0).getNow().getTemperature());
        editor.putString(NOW_WEATHER_SKY, weather.getHeWeather().get(0).getNow().getCond().getCode());
        editor.putString(NOW_WEATHER_WIND_DIRECTION, weather.getHeWeather().get(0).getNow().getWind().getWindDirection());
        editor.putString(NOW_WEATHER_AIR, weather.getHeWeather().get(0).getAqi().getCity().getQlty());
        editor.apply();
    }


    /**
     * 从 SharePreferences 中取出缓存的 NowWeather 信息
     *
     * @return nowWeather
     */
    private NowWeather getNowWeatherFromSP() {
        SharedPreferences sharedPreferences = getSharedPreferences(SP_DATA, MODE_PRIVATE);
        NowWeather nowWeather = new NowWeather();
        nowWeather.setCity(sharedPreferences.getString(NOW_WEATHER_CITY, ""));
        nowWeather.setTemperature(sharedPreferences.getString(NOW_WEATHER_TEMPERATURE, ""));
        nowWeather.setSky(sharedPreferences.getString(NOW_WEATHER_SKY, ""));
        nowWeather.setWindDirection(sharedPreferences.getString(NOW_WEATHER_WIND_DIRECTION, ""));
        nowWeather.setAir(sharedPreferences.getString(NOW_WEATHER_AIR, ""));
        return nowWeather;
    }

    /**
     * 将设置的天气 ID 存入 SharePreferences
     */
    private void saveNowWeatherIdInSP(String weatherId) {
        SharedPreferences.Editor editor = getSharedPreferences(SP_NOW, MODE_PRIVATE).edit();
        editor.putString(NOW_WEATHER_ID, weatherId);
        editor.apply();
    }

    /**
     * 更新状态栏通知
     */
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

    /**
     * 展示地方列表并且处理监听回调
     */
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
                        if (isPlaceDisplay(place.getWeatherId())) {
                            Toast.makeText(WeatherActivity.this, "已经展示该地区天气", Toast.LENGTH_SHORT).show();
                        } else {
                            showWeather(place.getWeatherId());
                            nowWeatherId = place.getWeatherId();
                        }
                        mWeatherDl.closeDrawer(mWeatherNvPlace);
                        updateRecyclerView(PlaceDatabaseDeal.searchPlaceByCityType(PLACE_TYPE_PROVINCE), mRvPlace, mPlaceAdapter);
                        break;
                }
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mRvPlace = (RecyclerView) mWeatherNvPlace.getHeaderView(0).findViewById(R.id.weather_rv_choose);
        mWeatherVpMessage.setPageTransformer(true, new DepthPageTransformer());
        mWeatherBtChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeatherActivity.this, "已经设置该城市为当前城市", Toast.LENGTH_SHORT).show();
                WeatherFragment fragment = (WeatherFragment) mFragmentManager.getFragments().get(mWeatherVpMessage.getCurrentItem());
                saveNowWeatherIdInSP(fragment.getFragmentWeatherId());
            }
        });
    }

    /**
     * 把省份列表从网络端取下来并存入数据库
     */
    private void saveProvinceInDatabase() {
        DiyGSONRequest<Province> request = new DiyGSONRequest<>(PLACE_ADDRESS, Province.class, new Response.Listener<List<Province>>() {
            @Override
            public void onResponse(List<Province> provinces) {
                mPlaceList.clear();
                for (int i = 0; i < provinces.size(); i++) {
                    Place place = new Place();
                    place.setAboveId(NOTHING);
                    place.setWeatherId(NOTHING);
                    place.setId(provinces.get(i).getId());
                    place.setName(provinces.get(i).getName());
                    place.setPlaceType(PLACE_TYPE_PROVINCE);
                    mPlaceList.add(place);
                }
                updateRecyclerView(mPlaceList, mRvPlace, mPlaceAdapter);
                PlaceDatabaseDeal.addPlace(mPlaceList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
            }
        });
        WeatherApplication.getRequestQueue().add(request);
    }

    /**
     * 把该 place 下的城市列表从网络端取下来并存入数据库
     */
    private void saveCityInDatabase(Place place) {
        final String aboveId = place.getId();
        DiyGSONRequest<City> request = new DiyGSONRequest<>(PLACE_ADDRESS + "/" + place.getId(), City.class, new Response.Listener<List<City>>() {
            @Override
            public void onResponse(List<City> cities) {
                mPlaceList.clear();
                for (int i = 0; i < cities.size(); i++) {
                    Place place = new Place();
                    place.setAboveId(aboveId);
                    place.setWeatherId(NOTHING);
                    place.setId(cities.get(i).getId());
                    place.setName(cities.get(i).getName());
                    place.setPlaceType(PLACE_TYPE_CITY);
                    mPlaceList.add(place);
                }
                updateRecyclerView(mPlaceList, mRvPlace, mPlaceAdapter);
                PlaceDatabaseDeal.addPlace(mPlaceList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
            }
        });
        WeatherApplication.getRequestQueue().add(request);
    }

    /**
     * 把该 place 下的地区列表从网络上取下来并存入数据库
     */
    private void saveDistrictInDatabase(Place place) {
        final String aboveId = place.getId();
        StringBuilder address = new StringBuilder();
        address.append(PLACE_ADDRESS).append("/").append(place.getAboveId()).append("/").append(place.getId());
        DiyGSONRequest<District> request = new DiyGSONRequest<>(address.toString(), District.class, new Response.Listener<List<District>>() {
            @Override
            public void onResponse(List<District> districts) {
                mPlaceList.clear();
                for (int i = 0; i < districts.size(); i++) {
                    Place place = new Place();
                    place.setAboveId(aboveId);
                    place.setWeatherId(districts.get(i).getWeatherId());
                    place.setId(districts.get(i).getId());
                    place.setName(districts.get(i).getName());
                    place.setPlaceType(PLACE_TYPE_DISTRICT);
                    mPlaceList.add(place);
                }
                updateRecyclerView(mPlaceList, mRvPlace, mPlaceAdapter);
                PlaceDatabaseDeal.addPlace(mPlaceList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
            }
        });
        WeatherApplication.getRequestQueue().add(request);
    }

    /**
     * 更新 RecyclerView 的地点列表
     *
     * @param places
     * @param recyclerView
     * @param placeAdapter
     */
    private void updateRecyclerView(List<Place> places, RecyclerView recyclerView, PlaceAdapter placeAdapter) {
        placeAdapter.updatePlaceList(places);
        recyclerView.setLayoutManager(new LinearLayoutManager(WeatherActivity.this));
        recyclerView.setAdapter(placeAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(WeatherActivity.this, DividerItemDecoration.VERTICAL_LIST));
        mPlaceAdapter.notifyDataSetChanged();
    }

    /**
     * 判断选择的地点是否已经存在 FragmentManager 里面（避免重复加载）
     *
     * @param weatherId
     * @return
     */
    private Boolean isPlaceDisplay(String weatherId) {
        for (int i = 0; i < mFragments.size(); i++) {
            WeatherFragment weatherFragment = (WeatherFragment) mFragments.get(i);
            if (weatherFragment.getFragmentWeatherId().equals(weatherId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 用于下拉更新天气（所选的全部地区）
     *
     * @param weatherFragment
     */
    private void setFragmentListener(WeatherFragment weatherFragment) {
        weatherFragment.setCallBackToActivity(new WeatherFragment.CallBackToActivity() {
            @Override
            public void UpdateFragment() {
                final List<Fragment> fragments = new ArrayList<>();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < mFragments.size(); i++) {
                            WeatherFragment fragment = (WeatherFragment) mFragments.get(i);
                            RequestFuture requestFuture = RequestFuture.newFuture();
                            StringRequest stringRequest = new StringRequest(WEATHER_ADDRESS + fragment.getFragmentWeatherId() + WEATHER_KEY, requestFuture, requestFuture);
                            try {
                                Weather weather = new Gson().fromJson((String) requestFuture.get(), Weather.class);
                                WeatherFragment newFragment = WeatherFragment.newInstance(weather);
                                fragments.add(newFragment);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            WeatherApplication.getRequestQueue().add(stringRequest);
                        }
                        mFragments.clear();
                        mFragments.addAll(fragments);
                        fragments.clear();
                    }
                }).start();
                mWeatherViewPagerAdapter = new WeatherViewPagerAdapter(mFragmentManager, mFragments);
                mWeatherVpMessage.setAdapter(mWeatherViewPagerAdapter);
                Toast.makeText(WeatherActivity.this, "已经把展示的地点天气更新", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
