package com.isaac.coolweather.util;

/**
 * Created by IsaacCn on 2015/9/8.
 */
public interface UpdateUIListener {
    void onFinish(String weatherJson);//Return weather data in json format
    void onError(Exception e);
    void onUpdateCurrentCityId(int cityId);//Change 'currentCityId' in WeatherDetailActivity.class
}
