package com.isaac.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.isaac.coolweather.R;
import com.isaac.coolweather.adapter.SavedCityDetailAdapter;
import com.isaac.coolweather.db.CoolWeatherDBOpenHelper;
import com.isaac.coolweather.model.SavedCityDetail;
import com.isaac.coolweather.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class CitySelectionActivity extends Activity {
    EditText inputCity;
    ImageButton search;
    ImageButton settings;
    ListView savedCityListView;

    private List<SavedCityDetail> savedCityDetailList = new ArrayList<SavedCityDetail>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);

        inputCity = (EditText) findViewById(R.id.inputCity);
        settings = (ImageButton) findViewById(R.id.settings);
        savedCityListView = (ListView) findViewById(R.id.savedCityList);
        search = (ImageButton) findViewById(R.id.search);

        initSavedCityDetailList();

        SavedCityDetailAdapter adapter = new SavedCityDetailAdapter(this, R.layout.saved_city_item, savedCityDetailList);
        savedCityListView.setAdapter(adapter);

        savedCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SavedCityDetail clickedItem = savedCityDetailList.get(i);
                LogUtil.d("CitySelectionActivity","You clicked "+i+", "+clickedItem.getCityName());
                Intent intent=new Intent(CitySelectionActivity.this,WeatherDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("cityId",clickedItem.getCityId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inputCity.getText().toString().isEmpty()) {//inputCity EditText is not empty & is not null
                    String matchStr = inputCity.getText().toString();
                    Intent intent = new Intent(CitySelectionActivity.this, ChooseCityDialogActivity.class);
                    intent.putExtra("matchStr", matchStr);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Search text cannot be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                savedCityDetailList.add(new SavedCityDetail(city_id, city_name, country, longitude, latitude));
            }
        }
        db.close();
        dbOpenHelper.close();
    }
}
