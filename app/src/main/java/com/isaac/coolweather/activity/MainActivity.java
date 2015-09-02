package com.isaac.coolweather.activity;

import android.app.Activity;
import android.os.Bundle;

import com.isaac.coolweather.R;
import com.isaac.coolweather.util.LogUtil;
import com.isaac.coolweather.util.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int temp = Utilities.getCityIdByLocation(this, 117.08, 36.19);
        LogUtil.d("MainActivity", "city_id= "+temp);
        /*
        String jsonData = load();
        //LogUtil.d("MainActivity",json.substring(1,100));
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i <= 100; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int cityId = jsonObject.getInt("_id");
                JSONObject coordObject = jsonObject.getJSONObject("coord");
                double longitude = coordObject.getDouble("lon");
                double latitude = coordObject.getDouble("lat");
                LogUtil.d("MainActivity", "cityId is " + cityId);
                LogUtil.d("MainActivity", "longitude is " + longitude);
                LogUtil.d("MainActivity", "latitude is " + latitude);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

    /*
    private String load() {
        FileInputStream inputStream = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            inputStream = openFileInput("city.list.json");
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return content.toString();
    }
    */
}
