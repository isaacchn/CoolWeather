package com.isaac.coolweather.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.isaac.coolweather.db.CoolWeatherDBOpenHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Name: Utilities
 * Created by IsaacCn on 2015/8/30.
 */

/*
* Find all cities in db, then find the city that is most close to the given geo info.
* */
public class Utilities {
    public static int getCityIdByLocation(Context context, float longitudeParam, float latitudeParam) {
        CoolWeatherDBOpenHelper dbOpenHelper = new CoolWeatherDBOpenHelper(context, "CityInfo.db", null, 1);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor resultCursor = db.rawQuery("SELECT * FROM city_info", null);

        /*Initialize one most close city to the given geo info*/
        int mostCloseCityId = 0;
        float mostCloseDistance = 259200;
        float mostCloseCityLongitude = 0;
        float mostCloseCityLatitude = 0;
        if (resultCursor.moveToFirst()) {
            int cityId;
            float longitude;
            float latitude;
            do {
                cityId = resultCursor.getInt(resultCursor.getColumnIndex("city_id"));
                longitude = resultCursor.getFloat(resultCursor.getColumnIndex("lon"));
                latitude = resultCursor.getFloat(resultCursor.getColumnIndex("lat"));
                /*Judge whether the chosen city is more close to the given geo info*/
                if ((Math.pow((longitudeParam - longitude), 2) + Math.pow((latitudeParam - latitude), 2)) < mostCloseDistance) {
                    mostCloseCityId = cityId;
                    mostCloseDistance = (float) (Math.pow((mostCloseCityLongitude - longitude), 2) + Math.pow((mostCloseCityLatitude - latitude), 2));
                }
            } while (resultCursor.moveToNext());
        }
        //LogUtil.d("Utilities","The most close ");
        resultCursor.close();
        return mostCloseCityId;
    }

    public static String sendHttpRequest(String address) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
