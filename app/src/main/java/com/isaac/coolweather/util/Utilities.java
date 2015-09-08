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
    public static final double KELVIN_ZERO_DEGREE = 273.15;
    public static int getCityIdByLocation(Context context, double longitudeParam, double latitudeParam) {
        CoolWeatherDBOpenHelper dbOpenHelper = new CoolWeatherDBOpenHelper(context, "CityInfo.db", null, 1);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor resultCursor = db.rawQuery("SELECT * FROM city_info", null);

        /*Initialize one most close city to the given geo info*/
        int mostCloseCityId = 0;
        double mostCloseDistance = 259200;
        if (resultCursor.moveToFirst()) {
            int cityId;
            double longitude;
            double latitude;
            do {
                cityId = resultCursor.getInt(resultCursor.getColumnIndex("city_id"));
                longitude = resultCursor.getFloat(resultCursor.getColumnIndex("lon"));
                latitude = resultCursor.getFloat(resultCursor.getColumnIndex("lat"));
                /*Judge whether the chosen city is more close to the given geo info*/
                if ((Math.pow((longitudeParam - longitude), 2) + Math.pow((latitudeParam - latitude), 2)) < mostCloseDistance) {
                    mostCloseCityId = cityId;
                    mostCloseDistance = Math.pow((longitudeParam - longitude), 2) + Math.pow((latitudeParam - latitude), 2);
                    LogUtil.d("Utilities", "Most close distance is " + mostCloseDistance);
                }
            } while (resultCursor.moveToNext());
        }
        //LogUtil.d("Utilities","The most close ");
        resultCursor.close();
        return mostCloseCityId;
    }

    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void updateWeatherInfoByLocation2(final Context context, final String address, final double longitudeParam, final double latitudeParam, final GetCityIdListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CoolWeatherDBOpenHelper dbOpenHelper = new CoolWeatherDBOpenHelper(context, "CityInfo.db", null, 1);
                SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
                Cursor resultCursor = db.rawQuery("SELECT * FROM city_info", null);
                /*Initialize one most close city to the given geo info*/
                int mostCloseCityId = 0;
                double mostCloseDistance = 259200;
                if (resultCursor.moveToFirst()) {
                    int cityId;
                    double longitude;
                    double latitude;
                    do {
                        cityId = resultCursor.getInt(resultCursor.getColumnIndex("city_id"));
                        longitude = resultCursor.getFloat(resultCursor.getColumnIndex("lon"));
                        latitude = resultCursor.getFloat(resultCursor.getColumnIndex("lat"));
                        /*Judge whether the chosen city is more close to the given geo info*/
                        if ((Math.pow((longitudeParam - longitude), 2) + Math.pow((latitudeParam - latitude), 2)) < mostCloseDistance) {
                            mostCloseCityId = cityId;
                            mostCloseDistance = Math.pow((longitudeParam - longitude), 2) + Math.pow((latitudeParam - latitude), 2);
                        }
                    } while (resultCursor.moveToNext());
                }
                resultCursor.close();
                db.close();
                //到目前为止已经获得了距离给定坐标最近的城市ID
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address + mostCloseCityId);
                    LogUtil.d("Utilities", address + mostCloseCityId);
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
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    public static void updateWeatherInfoByLocation(final Context context, final String address, final double longitudeParam, final double latitudeParam, final UpdateUIListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CoolWeatherDBOpenHelper dbOpenHelper = new CoolWeatherDBOpenHelper(context, "CityInfo.db", null, 1);
                SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
                Cursor resultCursor = db.rawQuery("SELECT * FROM city_info", null);
                /*Initialize one most close city to the given geo info*/
                int mostCloseCityId = 0;
                double mostCloseDistance = 259200;
                if (resultCursor.moveToFirst()) {
                    int cityId;
                    double longitude;
                    double latitude;
                    do {
                        cityId = resultCursor.getInt(resultCursor.getColumnIndex("city_id"));
                        longitude = resultCursor.getFloat(resultCursor.getColumnIndex("lon"));
                        latitude = resultCursor.getFloat(resultCursor.getColumnIndex("lat"));
                        /*Judge whether the chosen city is more close to the given geo info*/
                        if ((Math.pow((longitudeParam - longitude), 2) + Math.pow((latitudeParam - latitude), 2)) < mostCloseDistance) {
                            mostCloseCityId = cityId;
                            mostCloseDistance = Math.pow((longitudeParam - longitude), 2) + Math.pow((latitudeParam - latitude), 2);
                        }
                    } while (resultCursor.moveToNext());
                }
                resultCursor.close();
                db.close();
                //到目前为止已经获得了距离给定坐标最近的城市ID
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address + mostCloseCityId);
                    LogUtil.d("Utilities", address + mostCloseCityId);
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
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                listener.onUpdateCurrentCityId(mostCloseCityId);
            }
        }).start();
    }
    public static void updateCurrentCityWeatherInfo(final String urlWithCityId,final UpdateUIListener listener)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(urlWithCityId);
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
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
