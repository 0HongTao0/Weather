package com.hongtao.weather.DateBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.hongtao.weather.util.ContextUtil;


/**
 * author：hongtao on 2017/7/18/018 10:30
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class PlaceDatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_PLACE = "create table Place (" +
            "id text," +
            "above_id text," +
            "name text," +
            "weather_id text)";

    public PlaceDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Toast.makeText(ContextUtil.getContext(),"66666666666",Toast.LENGTH_SHORT).show();
        db.execSQL(CREATE_PLACE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
