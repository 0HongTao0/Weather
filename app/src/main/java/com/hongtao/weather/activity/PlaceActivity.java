package com.hongtao.weather.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hongtao.weather.DateBase.PlaceDatabaseDeal;
import com.hongtao.weather.R;
import com.hongtao.weather.adapter.PlaceViewPagerAdapter;
import com.hongtao.weather.bean.City;
import com.hongtao.weather.bean.District;
import com.hongtao.weather.bean.Place;
import com.hongtao.weather.bean.Province;
import com.hongtao.weather.util.DiyGSONRequest;

import java.util.ArrayList;
import java.util.List;


/**
 * author：hongtao on 2017/7/18/018 10:18
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class PlaceActivity extends AppCompatActivity implements PlaceFragment.CallBackToActivity {
    private final static String PLACE_ADDRESS = "http://guolin.tech/api/china";
    private final static String PLACE_TYPE_PROVINCE = "Province";
    private final static String PLACE_TYPE_CITY = "City";
    private final static String PLACE_TYPE_DISTRICT = "District";
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private ViewPager mVpPlace;
    private List<Fragment> mFragmentList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_place);
        if (!PlaceDatabaseDeal.isTableExit()) {
            saveProvinceInDatabase();
        }
        mVpPlace = (ViewPager) findViewById(R.id.place_vp_display_fragment);
        mVpPlace.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                Toast.makeText(PlaceActivity.this, ""+position, Toast.LENGTH_SHORT).show();
            }
        });
        List<Place> placeList = PlaceDatabaseDeal.searchPlaceByCityType(PLACE_TYPE_PROVINCE);
        PlaceFragment fragment = new PlaceFragment();
        fragment.setDataInFragment(placeList);
        mFragmentList.add(fragment);
        PlaceViewPagerAdapter adapter = new PlaceViewPagerAdapter(fragmentManager, mFragmentList);
        mVpPlace.setAdapter(adapter);
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
        StringBuilder address = new StringBuilder(PLACE_ADDRESS + "/" + place.getAboveId() + "/" + place.getId());
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("weatherId", "CN101280101");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void callBackPlace(Place place) {
        switch (place.getPlaceType()) {
            case PLACE_TYPE_PROVINCE:
                if (!PlaceDatabaseDeal.isPlaceExit(place.getId(), PLACE_TYPE_CITY)) {
                    saveCityInDatabase(place);
                }
                addFragmentInViewPager(place);
                break;
            case PLACE_TYPE_CITY:
                if (!PlaceDatabaseDeal.isPlaceExit(place.getId(), PLACE_TYPE_DISTRICT)) {
                    saveDistrictInDatabase(place);
                }
                addFragmentInViewPager(place);
                break;
            case PLACE_TYPE_DISTRICT:
                Intent intent = new Intent();
                intent.putExtra("weatherId", place.getWeatherId());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    private void addFragmentInViewPager(Place place) {
        List<Place> placeList = PlaceDatabaseDeal.searchPlaceByAboveId(place.getId());
        PlaceFragment fragment = new PlaceFragment();
        fragment.setDataInFragment(placeList);
        mFragmentList.add(fragment);
        PlaceViewPagerAdapter adapter = new PlaceViewPagerAdapter(fragmentManager, mFragmentList);
        mVpPlace.setAdapter(adapter);
        mVpPlace.setCurrentItem(mFragmentList.size());
    }
}
