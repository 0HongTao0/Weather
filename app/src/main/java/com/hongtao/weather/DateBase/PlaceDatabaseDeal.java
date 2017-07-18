package com.hongtao.weather.DateBase;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.hongtao.weather.util.ContextUtil;
import com.hongtao.weather.bean.Place;

import java.util.List;

/**
 * author：hongtao on 2017/7/18/018 10:44
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class PlaceDatabaseDeal {
    private static final String DATABASE_PLACE_NAME = "Place.db";
    private static PlaceDatabaseHelper mDatabaseHelper = new PlaceDatabaseHelper(ContextUtil.getContext(), DATABASE_PLACE_NAME, null, 1);
    private static SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

    public static void addPlace(List<Place> places) {
        for (int i = 0; i < places.size(); i++) {
            ContentValues values = new ContentValues();
            values.put("id", places.get(i).getId());
            values.put("above_id", places.get(i).getAboveId());
            values.put("name", places.get(i).getName());
            values.put("weather_id", places.get(i).getWeatherId());
            db.insert("Place", null, values);
        }
    }

}
