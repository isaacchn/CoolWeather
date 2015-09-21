package com.isaac.coolweather.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.isaac.coolweather.R;
import com.isaac.coolweather.adapter.NativeCityDetailAdapter;
import com.isaac.coolweather.adapter.SavedCityDetailAdapter;
import com.isaac.coolweather.db.CoolWeatherDBOpenHelper;
import com.isaac.coolweather.model.NativeCityDetail;
import com.isaac.coolweather.model.SavedCityDetail;
import com.isaac.coolweather.util.LogUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CitySelectionActivity extends Activity {
    EditText inputCity;
    Button search;
    Button settings;
    //ListView queriedCityList;
    ListView savedCityList;

    private List<SavedCityDetail> savedCityDetailList = new ArrayList<SavedCityDetail>();
    //private List<NativeCityDetail> queriedCityDetailList = new ArrayList<NativeCityDetail>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);

        inputCity = (EditText) findViewById(R.id.inputCity);
        settings = (Button) findViewById(R.id.settings);
        //queriedCityList = (ListView) findViewById(R.id.queriedCityList);
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
//        inputCity.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                // LogUtil.d("CitySelectionActivity","beforeTextChanged,"+charSequence.toString()+","+i+","+i1+","+i2);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                //LogUtil.d("CitySelectionActivity","onTextChanged,"+charSequence.toString()+","+i+","+i1+","+i2);
//                if (queriedCityList.getVisibility() == View.VISIBLE) {
//                    queriedCityList.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                //LogUtil.d("CitySelectionActivity","afterTextChanged,"+editable.toString());
//            }
//        });

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
