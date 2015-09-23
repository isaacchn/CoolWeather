package com.isaac.coolweather.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.isaac.coolweather.R;
import com.isaac.coolweather.adapter.NativeCityDetailAdapter;
import com.isaac.coolweather.db.CoolWeatherDBOpenHelper;
import com.isaac.coolweather.model.NativeCityDetail;
import com.isaac.coolweather.util.LogUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ChooseCityDialogActivity extends Activity {
    ListView queriedCityListView;
    private List<NativeCityDetail> queriedCityDetailList = new ArrayList<NativeCityDetail>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city_dialog);
        LogUtil.d("ChooseCityDialogActivity", getIntent().getStringExtra("matchStr"));

        queriedCityListView = (ListView) findViewById(R.id.queriedCityList);

        Intent intent = getIntent();
        String matchStr = intent.getStringExtra("matchStr");
        LogUtil.d("ChooseCityDialogActivity", matchStr);
        Gson gson = new Gson();
        InputStream inputStream = getResources().openRawResource(R.raw.city);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<NativeCityDetail> nativeCityList = gson.fromJson(reader, new TypeToken<List<NativeCityDetail>>() {
        }.getType());
        int nativeCityListSize = nativeCityList.size();
        int matchStrLength = matchStr.length();
        for (int i = 0, j = 0; i < nativeCityListSize && j < 100; i++) {
            if (nativeCityList.get(i).getName().substring(0, matchStrLength).equalsIgnoreCase(matchStr)) {
                LogUtil.d("ChooseCityDialogActivity", nativeCityList.get(i).getName());
                queriedCityDetailList.add(nativeCityList.get(i));
                j++;
            }
        }
        NativeCityDetailAdapter adapter = new NativeCityDetailAdapter(this, R.layout.queried_city_item, queriedCityDetailList);
        queriedCityListView.setAdapter(adapter);


        queriedCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NativeCityDetail clickedCityItem = queriedCityDetailList.get(position);
                CoolWeatherDBOpenHelper dbOpenHelper = new CoolWeatherDBOpenHelper(ChooseCityDialogActivity.this, "OpenWeather.db", null, 1);
                SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
                Cursor cursor = db.rawQuery("select city_id from saved_city_detail where city_id=?", new String[]{Integer.toString(clickedCityItem.getId())});
                if(cursor.getCount()==0){   //city not stored
                    ContentValues values=new ContentValues();
                    values.put("city_id",clickedCityItem.getId());
                    values.put("city_name",clickedCityItem.getName());
                    values.put("country",clickedCityItem.getCountry());
                    values.put("lon",clickedCityItem.getCoord().getLon());
                    values.put("lat",clickedCityItem.getCoord().getLat());
                    db.insert("saved_city_detail",null,values);
                }
                db.close();
                dbOpenHelper.close();
                Intent intent1 = new Intent(ChooseCityDialogActivity.this, WeatherDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("cityId", clickedCityItem.getId());
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });
    }
}
