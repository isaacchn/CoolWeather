package com.isaac.coolweather.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.isaac.coolweather.R;

public class CitySelectionActivity extends AppCompatActivity {
    EditText inputCity;
    Button settings;
    ListView queriedCityList;
    ListView savedCityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);

        inputCity = (EditText) findViewById(R.id.inputCity);
        settings = (Button) findViewById(R.id.settings);
        queriedCityList = (ListView) findViewById(R.id.queriedCityList);
        savedCityList = (ListView) findViewById(R.id.savedCityList);
    }
}
