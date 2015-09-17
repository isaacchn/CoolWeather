package com.isaac.coolweather.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.isaac.coolweather.R;
import com.isaac.coolweather.adapter.SavedCityDetailAdapter;
import com.isaac.coolweather.db.CoolWeatherDBOpenHelper;
import com.isaac.coolweather.model.SavedCityDetail;
import com.isaac.coolweather.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class CitySelectionActivity extends Activity {
    EditText inputCity;
    Button settings;
    ListView queriedCityList;
    ListView savedCityList;

    private List<SavedCityDetail> savedCityDetailList = new ArrayList<SavedCityDetail>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);

        inputCity = (EditText) findViewById(R.id.inputCity);
        settings = (Button) findViewById(R.id.settings);
        queriedCityList = (ListView) findViewById(R.id.queriedCityList);
        savedCityList = (ListView) findViewById(R.id.savedCityList);

        initSavedCityDetailList();

        SavedCityDetailAdapter adapter = new SavedCityDetailAdapter(this,R.layout.saved_city_item,savedCityDetailList);
        savedCityList.setAdapter(adapter);
    }

    void initSavedCityDetailList() {
        CoolWeatherDBOpenHelper dbOpenHelper = new CoolWeatherDBOpenHelper(this, "OpenWeather.db", null, 1);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select city_id,city_name,country,lon,lat from saved_city_detail", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int city_id = cursor.getInt(cursor.getColumnIndex("city_id"));
                String city_name = cursor.getString(cursor.getColumnIndex("city_name"));
                String country = cursor.getString(cursor.getColumnIndex("country"));
                double longitude = cursor.getDouble(cursor.getColumnIndex("lon"));
                double latitude = cursor.getDouble(cursor.getColumnIndex("lat"));
                savedCityDetailList.add(new SavedCityDetail(city_id,city_name,country,longitude,latitude));
            }
        }
    }
}
