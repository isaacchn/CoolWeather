package com.isaac.coolweather.util;

/**
 * Created by IsaacCn on 2015/9/2.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
