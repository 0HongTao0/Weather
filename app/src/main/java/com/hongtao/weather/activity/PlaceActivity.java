package com.hongtao.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hongtao.weather.DateBase.PlaceDatabaseDeal;
import com.hongtao.weather.R;
import com.hongtao.weather.adapter.PlaceAdapter;
import com.hongtao.weather.bean.City;
import com.hongtao.weather.bean.District;
import com.hongtao.weather.bean.Place;
import com.hongtao.weather.bean.Province;
import com.hongtao.weather.util.DividerItemDecoration;
import com.hongtao.weather.util.DiyGSONRequest;

import java.util.List;

/**
 * author：hongtao on 2017/7/18/018 10:18
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class PlaceActivity extends AppCompatActivity {
    private final static String PLACE_ADDRESS = "http://guolin.tech/api/china";
    private final static String PLACE_TYPE_PROVINCE = "Province";
    private final static String PLACE_TYPE_CITY = "City";
    private final static String PLACE_TYPE_DISTRICT = "District";
    private RecyclerView mRvCityName;
    private List<Place> placeList;
    private PlaceAdapter mChoosePlaceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_place);
        mRvCityName = (RecyclerView) findViewById(R.id.choose_place_rv_list);
        mRvCityName.addItemDecoration(new DividerItemDecoration(PlaceActivity.this, DividerItemDecoration.VERTICAL_LIST));

        if (!PlaceDatabaseDeal.isTableExit()) {
            saveProvinceInDatabase();
        }
        placeList = PlaceDatabaseDeal.searchPlaceByCityType(PLACE_TYPE_PROVINCE);
        mChoosePlaceAdapter = new PlaceAdapter(placeList);
        LinearLayoutManager manager = new LinearLayoutManager(PlaceActivity.this);
        mRvCityName.setLayoutManager(manager);
        mRvCityName.setAdapter(mChoosePlaceAdapter);
        try {
            mChoosePlaceAdapter.setAdapterCallBack(new PlaceAdapter.ItemOnClickCallBackListener() {
                @Override
                public void onClickCallBackPlace(Place place) {
                    if (PLACE_TYPE_PROVINCE.equals(place.getPlaceType())) {

                        if (!PlaceDatabaseDeal.isPlaceExit(place.getId(), PLACE_TYPE_CITY)) {
                            saveCityInDatabase(place);
                        }
                        mChoosePlaceAdapter.setPlaceList(PlaceDatabaseDeal.searchPlaceByAboveId(place.getId()));
                        mChoosePlaceAdapter.notifyDataSetChanged();
                    }
                    if (PLACE_TYPE_CITY.equals(place.getPlaceType())) {
                        if (!PlaceDatabaseDeal.isPlaceExit(place.getId(), PLACE_TYPE_DISTRICT)) {
                            saveDistrictInDatabase(place);
                        }
                        mChoosePlaceAdapter.setPlaceList(PlaceDatabaseDeal.searchPlaceByAboveId(place.getId()));
                        mChoosePlaceAdapter.notifyDataSetChanged();

                    }
                    if (PLACE_TYPE_DISTRICT.equals(place.getPlaceType())) {
                        Intent intent = new Intent();
                        intent.putExtra("weatherId", place.getWeatherId());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveProvinceInDatabase() {
        DiyGSONRequest<Province> request = new DiyGSONRequest<>(PLACE_ADDRESS, Province.class, new Response.Listener<List<Province>>() {
            @Override
            public void onResponse(List<Province> provinces) {
                placeList.clear();
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
                placeList.clear();
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
                placeList.clear();
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
}
