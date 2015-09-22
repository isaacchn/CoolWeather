package com.isaac.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.isaac.coolweather.R;
import com.isaac.coolweather.adapter.SavedCityDetailAdapter;
import com.isaac.coolweather.db.CoolWeatherDBOpenHelper;
import com.isaac.coolweather.model.SavedCityDetail;

import java.util.ArrayList;
import java.util.List;

public class CitySelectionActivity extends Activity {
    EditText inputCity;
    Button search;
    Button settings;
    ListView savedCityList;

    private List<SavedCityDetail> savedCityDetailList = new ArrayList<SavedCityDetail>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);

        inputCity = (EditText) findViewById(R.id.inputCity);
        settings = (Button) findViewById(R.id.settings);
        savedCityList = (ListView) findViewById(R.id.savedCityList);
        search = (Button) findViewById(R.id.search);

        initSavedCityDetailList();

        SavedCityDetailAdapter adapter = new SavedCityDetailAdapter(this, R.layout.saved_city_item, savedCityDetailList);
        savedCityList.setAdapter(adapter);

        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inputCity.getText().toString().isEmpty()) {//inputCity EditText is not empty & is not null
                    String matchStr = inputCity.getText().toString();
                    Intent intent = new Intent(CitySelectionActivity.this, ChooseCityDialogActivity.class);
                    intent.putExtra("matchStr", matchStr);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Search text cannot be empty.",Toast.LENGTH_SHORT).show();
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
    }
}
