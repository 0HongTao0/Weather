package com.hongtao.weather.DateBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hongtao.weather.activity.WeatherApplication;
import com.hongtao.weather.bean.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * 对数据库操作的类
 * <p>
 * author：hongtao on 2017/7/18/018 10:44
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class PlaceDatabaseDeal {
    private static final String DATABASE_PLACE_NAME = "Place.db";
    private static final String PLACE_NAME = "name";
    private static final String CITY_ID = "city_id";
    private static final String ABOVE_ID = "above_id";
    private static final String WEATHER_ID = "weather_id";
    private static final String PLACE_TYPE = "place_type";

    private static PlaceDatabaseHelper mDatabaseHelper = new PlaceDatabaseHelper(WeatherApplication.getContext(), DATABASE_PLACE_NAME, null, 1);
    private static SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

    /**
     * 把一个 List<Place> 添加到数据库中 Place 表中
     *
     * @param places
     */
    public static void addPlace(List<Place> places) {
        for (int i = 0; i < places.size(); i++) {
            ContentValues values = new ContentValues();
            values.put("city_id", places.get(i).getId());
            values.put("above_id", places.get(i).getAboveId());
            values.put("name", places.get(i).getName());
            values.put("weather_id", places.get(i).getWeatherId());
            values.put("place_type", places.get(i).getPlaceType());
            db.insert("Place", null, values);
        }
    }


    /**
     * 通过传入的 Place  Id 在数据库中查找
     *
     * @param id
     * @return
     */
    public static List<Place> searchPlaceByAboveId(String id) {
        List<Place> placeList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select *from Place where above_id = ?", new String[]{id});
        if (cursor.moveToNext()) {
            do {
                Place place = new Place();
                place.setName(cursor.getString(cursor.getColumnIndex(PLACE_NAME)));
                place.setId(cursor.getString(cursor.getColumnIndex(CITY_ID)));
                place.setAboveId(cursor.getString(cursor.getColumnIndex(ABOVE_ID)));
                place.setWeatherId(cursor.getString(cursor.getColumnIndex(WEATHER_ID)));
                place.setPlaceType(cursor.getString(cursor.getColumnIndex(PLACE_TYPE)));
                placeList.add(place);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return placeList;
    }


    /**
     * 判断数据库中 Place 表是否存在
     *
     * @return
     */
    public static Boolean isTableExit() {
        Cursor cursor = db.rawQuery("select * from Place", null);
        if (cursor.moveToNext()) {
            return true;
        }
        cursor.close();
        return false;
    }

    /**
     * 判断数据库中 above_id = place_id  和对应 place_type 的 Place 是否存在
     *
     * @param placeId
     * @param placeType
     * @return
     */
    public static Boolean isPlaceExit(String placeId, String placeType) {
        Cursor cursor = db.rawQuery("select *from Place where above_id = ? and place_type = ?", new String[]{placeId, placeType});
        if (cursor.moveToNext()) {
            return true;
        }
        cursor.close();
        return false;
    }

    /**
     * 删除 Place 表
     */
    public static void clearTable() {
        db.execSQL("drop table Place");
    }

    /**
     * 通过 place_type 在数据库中查找
     *
     * @param cityType
     * @return
     */
    public static List<Place> searchPlaceByCityType(String cityType) {
        List<Place> placeList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select *from Place where place_type = ?", new String[]{cityType});
        if (cursor.moveToNext()) {
            do {
                Place place = new Place();
                place.setName(cursor.getString(cursor.getColumnIndex(PLACE_NAME)));
                place.setId(cursor.getString(cursor.getColumnIndex(CITY_ID)));
                place.setAboveId(cursor.getString(cursor.getColumnIndex(ABOVE_ID)));
                place.setWeatherId(cursor.getString(cursor.getColumnIndex(WEATHER_ID)));
                place.setPlaceType(cursor.getString(cursor.getColumnIndex(PLACE_TYPE)));
                placeList.add(place);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return placeList;
    }
}
