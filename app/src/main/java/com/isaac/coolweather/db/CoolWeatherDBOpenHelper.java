package com.isaac.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Name: CoolWeatherDBOpenHelper
 * Comment: SQLite Database Open Helper
 * Override onCreate() onUpgrade()
 * Created by IsaacCn on 2015/8/30.
 */
public class CoolWeatherDBOpenHelper extends SQLiteOpenHelper {
    private static final String CREATE_CITY_INFO =
            "create table if not exists city_info ("
                    + "city_id integer primary key,"
                    + "city_name text,"
                    + "country text,"
                    + "lon real,"
                    + "lat real);";

    public CoolWeatherDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CITY_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
