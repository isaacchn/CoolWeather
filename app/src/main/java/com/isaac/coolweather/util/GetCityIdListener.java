package com.isaac.coolweather.util;

/**
 * Created by IsaacCn on 2015/9/3.
 */
public interface GetCityIdListener {
    void onFinish(String cityId);
    void onError(Exception e);
}
