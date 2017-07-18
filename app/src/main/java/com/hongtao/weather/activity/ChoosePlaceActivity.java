package com.hongtao.weather.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hongtao.weather.DateBase.PlaceDatabaseDeal;
import com.hongtao.weather.R;
import com.hongtao.weather.bean.City;
import com.hongtao.weather.bean.District;
import com.hongtao.weather.bean.Place;
import com.hongtao.weather.bean.Province;
import com.hongtao.weather.util.GsonRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * author：hongtao on 2017/7/18/018 10:18
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class ChoosePlaceActivity extends AppCompatActivity {
    private final static String PLACE_ADDRESS = "http://guolin.tech/api/china";
    private List<Province> mProvinceList;
    private List<Place> mPlaceList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_place);
        savePlaceInDatabase();
    }

    private void savePlaceInDatabase() {
        RequestQueue queue = Volley.newRequestQueue(ChoosePlaceActivity.this);
        saveProvinceInDatabase(queue);
//        saveCityInDatabase(queue);
//        saveDistrictInDatabase(queue);
    }

    private void saveProvinceInDatabase(RequestQueue queue) {
        GsonRequest<Province> request = new GsonRequest<>(PLACE_ADDRESS, Province.class, new Response.Listener<List<Province>>() {
            @Override
            public void onResponse(List<Province> provinces) {
                mProvinceList = provinces;
                List<Place> placeList = new ArrayList<>();
                for (int i = 0; i < provinces.size(); i++) {
                    Place place = new Place();
                    place.setAboveId("0");
                    place.setWeatherId("0");
                    place.setId(provinces.get(i).getId());
                    place.setName(provinces.get(i).getName());
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
        queue.add(request);
    }

    private void saveCityInDatabase(RequestQueue queue) {
        for (int i = 0; i < mProvinceList.size(); i++) {
            final Province province = mProvinceList.get(i);
            GsonRequest<City> request = new GsonRequest<>(PLACE_ADDRESS + "/" + province.getId(), City.class, new Response.Listener<List<City>>() {
                @Override
                public void onResponse(List<City> cities) {

                    for (int i = 0; i < cities.size(); i++) {
                        Place place = new Place();
                        place.setAboveId(province.getId());
                        place.setWeatherId("0");
                        place.setId(cities.get(i).getId());
                        place.setName(cities.get(i).getName());
                        mPlaceList.add(place);
                    }
                    PlaceDatabaseDeal.addPlace(mPlaceList);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("TAG", volleyError.getMessage(), volleyError);
                }
            });
            queue.add(request);
        }
    }

    private void saveDistrictInDatabase(RequestQueue queue) {
        for (int i = 0; i < mPlaceList.size(); i++) {
            final Place place = mPlaceList.get(i);
            GsonRequest<District> request = new GsonRequest<>(PLACE_ADDRESS + "/" + place.getAboveId() + "/" + place.getId(), District.class, new Response.Listener<List<District>>() {
                @Override
                public void onResponse(List<District> districts) {
                    List<Place> placeList = new ArrayList<>();

                    for (int i = 0; i < districts.size(); i++) {
                        Place place = new Place();
                        place.setAboveId(place.getId());
                        place.setWeatherId(districts.get(i).getWeatherId());
                        place.setId(districts.get(i).getId());
                        place.setName(districts.get(i).getName());
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
            queue.add(request);
        }
    }
}
