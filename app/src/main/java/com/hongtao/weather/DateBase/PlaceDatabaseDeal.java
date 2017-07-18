package com.hongtao.weather.DateBase;

import android.database.sqlite.SQLiteDatabase;

import com.hongtao.weather.activity.WeatherApplication;

/**
 * author：hongtao on 2017/7/18/018 10:44
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class PlaceDatabaseDeal {
    private static final String DATABASE_PLACE_NAME = "Place.db";
    private PlaceDatabaseHelper mDatabaseHelper = new PlaceDatabaseHelper(WeatherApplication.getContext(), DATABASE_PLACE_NAME, null, 1);
    private SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();


}
